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
import com.gdn.venice.logistics.integration.AirwayBillEngineClientConnector;
import com.gdn.venice.logistics.integration.AirwayBillEngineConnector;
import com.gdn.venice.logistics.integration.bean.AirwayBillTransaction;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchDeliveryStatusTrackingDataCommand implements RafDsCommand {
	RafDsRequest request;	
	String userName;
	protected static Logger _log = null;
	
	public FetchDeliveryStatusTrackingDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
		
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory
				.getLog4JLogger("com.gdn.venice.server.app.logistics.presenter.commands.FetchDeliveryStatusTrackingDataCommand");
	}

	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		JPQLAdvancedQueryCriteria criteriaAndTaskCriteria = null;
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<LogAirwayBillSessionEJBRemote> airwayBillLocator = null;
		HashMap<String,String> airwayBillIds=null;
		
		if (request.getParams()!=null && request.getParams().get(DataNameTokens.TASKID)!=null) {
			String taskId = request.getParams().get(DataNameTokens.TASKID);
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
			
			bpmAdapter.synchronize();
			if(!taskId.equals("0")){
				airwayBillIds = bpmAdapter.getExternalDataVariableAsHashMap(new Long(taskId), ProcessNameTokens.AIRWAYBILLID);
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
					} else{
						airwayBillIdList = airwayBillIdList + "," + value;
					}
				}
				inCriteria.setValue(airwayBillIdList);
				inCriteria.setOperator("IN");
				
				criteriaAndTaskCriteria.add(inCriteria);
			}				
		}
		
		try {
			airwayBillLocator = new Locator<LogAirwayBillSessionEJBRemote>();
			
			LogAirwayBillSessionEJBRemote sessionHome = (LogAirwayBillSessionEJBRemote) airwayBillLocator
			.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
				
			List<LogAirwayBill> airwayBillList = null;
			
			if ((criteria == null && criteriaAndTaskCriteria == null)
					|| (criteria!=null && !criteria.getListIterator().hasNext())
					|| (criteriaAndTaskCriteria!=null && !criteriaAndTaskCriteria.getListIterator().hasNext())) {
				String select = "select o from LogAirwayBill o ";				
				airwayBillList = sessionHome.queryByRange(select, 0, 50);
			} else {
				LogAirwayBill airwayBill = new LogAirwayBill();
				if (criteriaAndTaskCriteria!=null) {
					airwayBillList = sessionHome.findByLogAirwayBillLike(airwayBill, criteriaAndTaskCriteria, 0, 0);
				}else{
					airwayBillList = sessionHome.findByLogAirwayBillLike(airwayBill, criteria, 0, 0);
				}				
			}
			
			//Remove airway bill(s) with activity report or invoice result status is "Invalid GDN Ref", this is to avoid cluttering the delivery status tracking
			//screen from invalid airway bills from activity report and invoice
			for (int i=0;i<airwayBillList.size();i++) {
				LogAirwayBill airwayBill = airwayBillList.get(i);
				if ((airwayBill.getActivityResultStatus() != null && airwayBill.getActivityResultStatus().toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_INVALIDGDNREF.toUpperCase())) || 
						(airwayBill.getLogInvoiceAirwaybillRecord()!=null && airwayBill.getLogInvoiceAirwaybillRecord().getInvoiceResultStatus() !=null && airwayBill.getLogInvoiceAirwaybillRecord().getInvoiceResultStatus().toUpperCase().contains(DataConstantNameTokens.LOGAIRWAYBILL_RESULTSTATUS_INVALIDGDNREF.toUpperCase()))) {
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
				String weight = "";
				
				if (airwayBill.getGdnReference() != null && !airwayBill.getGdnReference().equals("")) {
					gdnRef = airwayBill.getGdnReference();
					airwayBillNo = airwayBill.getAirwayBillNumber();
					weight = airwayBill.getVenDistributionCart()!=null?airwayBill.getVenDistributionCart().getPackageWeight().toPlainString():"";
				}else{
					AirwayBillTransaction awbTransaction = awbConn.getAirwayBillTransaction(airwayBill.getVenOrderItem().getVenOrder().getWcsOrderId(), airwayBill.getVenOrderItem().getWcsOrderItemId(), false);
					
					try{
						gdnRef = awbTransaction.getGdnRef();
					}catch (Exception e) {
						_log.error("Error getting GDN Ref from airway bill transaction detail", e);
					}
					
					try{
						airwayBillNo = awbTransaction.getAirwayBillNo();
					}catch (Exception e) {
						_log.error("Error getting Airway Bill No from airway bill transaction detail", e);
					}
					
					try{
						weight = awbTransaction.getAirwaybillWeight().toString();
					}catch (Exception e) {
						_log.error("Error getting weight from airway bill transaction detail", e);
					}
				}
								
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, airwayBill.getAirwayBillId()!=null?airwayBill.getAirwayBillId().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_DETAIL, "Detail");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_ORDERITEMID, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getOrderItemId()!=null?airwayBill.getVenOrderItem().getOrderItemId().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_WCSORDERID, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrder()!=null && airwayBill.getVenOrderItem().getVenOrder().getWcsOrderId()!=null?airwayBill.getVenOrderItem().getVenOrder().getWcsOrderId():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_WCSORDERITEMID, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getWcsOrderItemId()!=null?airwayBill.getVenOrderItem().getWcsOrderItemId():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_GDNREFERENCE, gdnRef);
				map.put(DataNameTokens.LOGAIRWAYBILL_VENDISTRIBUTIONCART_PACKAGEWEIGHT, weight);
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDER_ORDERDATE, 
						airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrder()!=null && airwayBill.getVenOrderItem().getVenOrder().getOrderDate()!=null?formatter.format(airwayBill.getVenOrderItem().getVenOrder().getOrderDate()):"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSID, 
						airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrderStatus()!=null && airwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusId()!=null?airwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusId().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, 
						airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getVenOrderStatus()!=null && airwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusCode()!=null?airwayBill.getVenOrderItem().getVenOrderStatus().getOrderStatusCode():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_VENORDERITEM_VENMERCHANTPRODUCT_VENMERCHANT_VENPARTY_FULLORLEGALNAME, 
						airwayBill.getVenOrderItem()!=null &&
						airwayBill.getVenOrderItem().getVenMerchantProduct()!=null &&
						airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant()!= null &&
						airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty()!= null &&
						airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName()!=null?
						airwayBill.getVenOrderItem().getVenMerchantProduct().getVenMerchant().getVenParty().getFullOrLegalName():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_DESTINATION, airwayBill.getDestination()!=null?airwayBill.getDestination():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLTIMESTAMP, airwayBill.getAirwayBillTimestamp()!=null?formatter.format(airwayBill.getAirwayBillTimestamp()):"");
				map.put(DataNameTokens.LOGAIRWAYBILL_SERVICE, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getLogLogisticService()!=null && airwayBill.getVenOrderItem().getLogLogisticService().getLogisticsServiceId()!=null?airwayBill.getVenOrderItem().getLogLogisticService().getLogisticsServiceId().toString():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_SERVICEDESC, airwayBill.getVenOrderItem()!=null && airwayBill.getVenOrderItem().getLogLogisticService()!=null && airwayBill.getVenOrderItem().getLogLogisticService().getLogisticsServiceDesc()!=null?airwayBill.getVenOrderItem().getLogLogisticService().getLogisticsServiceDesc():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLNUMBER, airwayBillNo);
				map.put(DataNameTokens.LOGAIRWAYBILL_STATUS, airwayBill.getStatus()!=null?airwayBill.getStatus():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_RECEIVED, airwayBill.getReceived()!=null?formatter.format(airwayBill.getReceived()):"");
				map.put(DataNameTokens.LOGAIRWAYBILL_RECIPIENT, airwayBill.getRecipient()!=null?airwayBill.getRecipient():"");
				map.put(DataNameTokens.LOGAIRWAYBILL_RELATION, airwayBill.getRelation()!=null?airwayBill.getRelation():"");
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
}
