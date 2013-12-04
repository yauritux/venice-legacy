package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.facade.LogMerchantPickupInstructionSessionEJBRemote;
import com.gdn.venice.logistics.integration.AirwayBillEngineClientConnector;
import com.gdn.venice.logistics.integration.AirwayBillEngineConnector;
import com.gdn.venice.logistics.integration.bean.AirwayBillTransaction;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogMerchantPickupInstruction;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchActivityReportAirwayBillDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	protected static Logger _log = null;
	private Long startTime, endTime, duration;
	
	public FetchActivityReportAirwayBillDataCommand(RafDsRequest request, String userName) {
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.server.app.logistics.presenter.commands.FetchActivityReportAirwayBillDataCommand");
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		
		JPQLAdvancedQueryCriteria criteriaAndTaskCriteria = null;
		//check for taskid parameter, it shall be there if this screen is called from ToDoList for approval purpose
		if (request.getParams()!=null && request.getParams().get(DataNameTokens.TASKID)!=null) {
			String taskId = request.getParams().get(DataNameTokens.TASKID);
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
			
			bpmAdapter.synchronize();
			HashMap<String,String> airwayBillIds = bpmAdapter.getExternalDataVariableAsHashMap(new Long(taskId), ProcessNameTokens.AIRWAYBILLID);
			
			criteriaAndTaskCriteria = new JPQLAdvancedQueryCriteria("and");
			if (criteria!=null) {
				criteriaAndTaskCriteria.add(criteria);
			}
			request.setCriteria(criteriaAndTaskCriteria);	
			
			/*
			 * Build a new simple criteria as an IN() list
			 */
			JPQLSimpleQueryCriteria inCriteria = new JPQLSimpleQueryCriteria();
			inCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID));
			inCriteria.setFieldName(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID);
			
			String airwayBillIdList = "";
			for(String value:airwayBillIds.values()){
				if(airwayBillIdList.isEmpty()){
					airwayBillIdList = value;
				}
				else{
					airwayBillIdList = airwayBillIdList + "," + value;
				}
			}
			inCriteria.setValue(airwayBillIdList);
			inCriteria.setOperator("IN");
			
			criteriaAndTaskCriteria.add(inCriteria);
		}
		
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<LogAirwayBillSessionEJBRemote> airwayBillLocator = null;
		Locator<LogMerchantPickupInstructionSessionEJBRemote> pickupInstructionLocator = null;
		
		try {
			airwayBillLocator = new Locator<LogAirwayBillSessionEJBRemote>();	
			pickupInstructionLocator = new Locator<LogMerchantPickupInstructionSessionEJBRemote>();
			
			LogAirwayBillSessionEJBRemote sessionHome = (LogAirwayBillSessionEJBRemote) airwayBillLocator
				.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			LogMerchantPickupInstructionSessionEJBRemote pickupInstructionHome = (LogMerchantPickupInstructionSessionEJBRemote) pickupInstructionLocator
				.lookup(LogMerchantPickupInstructionSessionEJBRemote.class, "LogMerchantPickupInstructionSessionEJBBean");
			
			List<LogAirwayBill> airwayBillList = null;			
			if (criteria == null && criteriaAndTaskCriteria == null) {
				String select = "select * from log_airway_bill where activity_file_name_and_loc like '%' ||" +
						"(select to_char(max(t.times),'YYYY.MM.DD HH24:MI:SS') from " +
						"(select to_timestamp(substring(activity_file_name_and_loc, strpos(activity_file_name_and_loc,'/2')+1,19),'YYYY.MM.DD HH24:MI:SS') as times " +
						"from log_airway_bill where activity_file_name_and_loc is not null) as t) || '%'";				

//				String select = "select *, to_timestamp(substring(activity_file_name_and_loc, " +
//						"strpos(activity_file_name_and_loc,'/2')+1,19),'YYYY.MM.DD HH24:MI:SS') as times " +
//						"from log_airway_bill where activity_file_name_and_loc is not null " +
//						"order by times desc";
				
				_log.debug("LogAirwayBillSessionHome.queryByRange()");
				startTime = System.currentTimeMillis();
				airwayBillList = sessionHome.queryByRangeWithNativeQuery(select, request.getStartRow(), request.getEndRow()-request.getStartRow());
				endTime = System.currentTimeMillis();
				duration = startTime - endTime;
				_log.debug("LogAirwayBillSessionHome.queryByRange() duration:" + duration + "ms");
			} else {
				LogAirwayBill airwayBill = new LogAirwayBill();
				
				if (criteriaAndTaskCriteria!=null) {
					_log.debug("LogAirwayBillSessionHome.findByLogAirwayBillLike(criteriaAndTaskCriteria)");
					startTime = System.currentTimeMillis();
					airwayBillList = sessionHome.findByLogAirwayBillLike(airwayBill, criteriaAndTaskCriteria, 0, 0);
					endTime = System.currentTimeMillis();
					duration = startTime - endTime;
					_log.debug("LogAirwayBillSessionHome.findByLogAirwayBillLike(criteriaAndTaskCriteria) duration:" + duration + "ms");
				} else {
					_log.debug("LogAirwayBillSessionHome.findByLogAirwayBillLike(criteria)");
					startTime = System.currentTimeMillis();
					airwayBillList = sessionHome.findByLogAirwayBillLike(airwayBill, criteria, 0, 0);
					endTime = System.currentTimeMillis();
					duration = startTime - endTime;
					_log.debug("LogAirwayBillSessionHome.findByLogAirwayBillLike(criteria) duration:" + duration + "ms");
				}
			}
			
			//Remove airway bill(s) with invoice result status is "Invalid GDN Ref", this is to avoid cluttering the activity report
			//screen from invalid airway bills from invoice report
			for (int i=0;i<airwayBillList.size();i++) {
				LogAirwayBill airwayBill = airwayBillList.get(i);
				if (airwayBill.getLogInvoiceAirwaybillRecord()!=null && airwayBill.getLogInvoiceAirwaybillRecord().getInvoiceResultStatus()!=null && airwayBill.getLogInvoiceAirwaybillRecord().getInvoiceResultStatus().toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_INVALIDGDNREF.toUpperCase())) {
					airwayBillList.remove(i);
				}
			}
			
			AirwayBillEngineConnector awbConn = new AirwayBillEngineClientConnector();
			
			for (int i=0;i<airwayBillList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				LogAirwayBill airwayBill = airwayBillList.get(i);				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				String airwayBillNo = "";
				String gdnRef = "";
				String pickUpDate = "";
				String logProvider = "";
				String destination = "";
				String zipCode = "";
				
				if (airwayBill.getGdnReference() != null && !airwayBill.getGdnReference().equals("")) {
					gdnRef = airwayBill.getGdnReference();
					airwayBillNo = airwayBill.getAirwayBillNumber();
					pickUpDate = airwayBill.getAirwayBillPickupDateTime()!=null?formatter.format(airwayBill.getAirwayBillPickupDateTime()):"";
					logProvider = airwayBill.getLogLogisticsProvider()!=null?airwayBill.getLogLogisticsProvider().getLogisticsProviderCode():"";
					destination = airwayBill.getDestination()!=null?airwayBill.getDestination():"";
					zipCode = airwayBill.getZip()!=null?airwayBill.getZip():"";
				}else{
					
					AirwayBillTransaction awbTransaction = awbConn.getAirwayBillTransaction(airwayBill.getVenOrderItem().getVenOrder().getWcsOrderId(), airwayBill.getVenOrderItem().getWcsOrderItemId(), false);
					
					try{
						gdnRef = awbTransaction.getGdnRef();
						airwayBillNo = awbTransaction.getAirwayBillNo();
						pickUpDate = formatter.format(awbTransaction.getTanggalPickup());
						logProvider = awbTransaction.getKodeLogistik();
						destination = awbTransaction.getPropinsiPenerima();
						zipCode = awbTransaction.getKodeposPenerima();
					}catch (Exception e) {
						_log.error("Error getting airway bill transaction detail", e);
					}
					
					
				}
				
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, airwayBill.getAirwayBillId()!=null?airwayBill.getAirwayBillId().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID, 
						(airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrder()!=null)?airwayBill.getVenOrderItem().getVenOrder().getWcsOrderId():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID, airwayBill.getVenOrderItem()!=null?airwayBill.getVenOrderItem().getWcsOrderItemId():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE, airwayBill.getVenOrderItem().getVenOrder()!=null?airwayBill.getVenOrderItem().getVenOrder().getOrderDate().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE, gdnRef);
				map.put(DataNameTokens.LOGAIRWAYBILL_ACTIVITYRESULTSTATUS, airwayBill.getActivityResultStatus());
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP, formatter.format(airwayBill.getAirwayBillTimestamp()));
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrderStatus()!=null?airwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusId().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrderStatus()!=null?airwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusCode():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, 
						(airwayBill.getVenOrderItem()!=null &&
								airwayBill.getVenOrderItem().getVenMerchantProduct()!=null &&
								airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!= null &&
								airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!= null)
								?airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():"");
				
				_log.debug("LogAirwayBillSessionHome.findByLogAirwayBillLike(criteriaAndTaskCriteria)");
				startTime = System.currentTimeMillis();
				List<LogMerchantPickupInstruction> merchantInstructionList=pickupInstructionHome.queryByRange("select o from LogMerchantPickupInstruction o where o.venOrderItem.orderItemId =" + airwayBill.getVenOrderItem().getOrderItemId(), 0, 0);
				endTime = System.currentTimeMillis();
				duration = startTime - endTime;
				_log.debug("LogAirwayBillSessionHome.findByLogAirwayBillLike(criteriaAndTaskCriteria) duration:" + duration + "ms");				
				
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_LOGMERCHANTPICKUPINSTRUCTIONS_VENADDRESS_VENCITY_CITYNAME,(merchantInstructionList!=null&&merchantInstructionList.size() > 0)?(merchantInstructionList.get(0).getVenAddress().getVenCity()!=null?merchantInstructionList.get(0).getVenAddress().getVenCity().getCityName():""):"");
				map.put(DataNameTokens.LOGAIRWAYBILL_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, logProvider);
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLPICKUPDATETIME, pickUpDate);
				map.put(DataNameTokens.LOGAIRWAYBILL_DESTINATION, destination);
				map.put(DataNameTokens.LOGAIRWAYBILL_ZIP, zipCode);
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER, airwayBillNo);
				map.put(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSID, airwayBill.getLogApprovalStatus2()!=null?airwayBill.getLogApprovalStatus2().getApprovalStatusId().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_LOGAPPROVALSTATUS2_APPROVALSTATUSDESC, airwayBill.getLogApprovalStatus2()!=null?airwayBill.getLogApprovalStatus2().getApprovalStatusDesc().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_ACTIVITYAPPROVEDBYUSERID, airwayBill.getActivityApprovedByUserId());
				map.put(DataNameTokens.LOGAIRWAYBILL_ACTIVITYFILENAMEANDLOC, airwayBill.getActivityFileNameAndLoc());
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(airwayBillList.size());
			rafDsResponse.setEndRow(request.getStartRow()+airwayBillList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				airwayBillLocator.close();
				pickupInstructionLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
}
