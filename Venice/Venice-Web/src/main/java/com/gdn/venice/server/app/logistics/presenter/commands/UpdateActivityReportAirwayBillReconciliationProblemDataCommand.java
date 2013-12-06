package com.gdn.venice.server.app.logistics.presenter.commands;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.persistence.LogActionApplied;
import com.gdn.venice.persistence.LogActivityReconRecord;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class UpdateActivityReportAirwayBillReconciliationProblemDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public UpdateActivityReportAirwayBillReconciliationProblemDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		rafDsResponse.setStatus(0);
		rafDsResponse.setStartRow(request.getStartRow());
		rafDsResponse.setTotalRows(0);
		rafDsResponse.setEndRow(request.getStartRow());
		
		List<LogActivityReconRecord> activityReconRecordList = new ArrayList<LogActivityReconRecord>();		
		List<HashMap<String,String >> dataList = request.getData();		
		LogActivityReconRecord activityReconRecord = new LogActivityReconRecord();
					
		Locator<LogActivityReconRecordSessionEJBRemote> activityReconRecordLocator = null;
		Locator<LogAirwayBillSessionEJBRemote> airwayBillLocator = null;
		
		try {
			activityReconRecordLocator = new Locator<LogActivityReconRecordSessionEJBRemote>();
			
			LogActivityReconRecordSessionEJBRemote activityReconRecordSessionHome = (LogActivityReconRecordSessionEJBRemote) activityReconRecordLocator
			.lookup(LogActivityReconRecordSessionEJBRemote.class, "LogActivityReconRecordSessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);			
				Iterator<String> iter = data.keySet().iterator();
				
				while (iter.hasNext()) {
					String key = iter.next();

					if (key.equals(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID)) {
						try{
							activityReconRecord = activityReconRecordSessionHome.queryByRange("Select o from LogActivityReconRecord o where o.activityReconRecordId="+new Long(data.get(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							activityReconRecord.setActivityReconRecordId(new Long(data.get(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID)));
						}
						break;
					}
				}
				
				iter = data.keySet().iterator();
				
				while (iter.hasNext()) {
					String key = iter.next();

					//we do not need to set LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTDESC here					
//					} else if (key.equals(DataNameTokens.LOGACTIVITYRECONRECORD_VENICEDATA)) {
//						activityReconRecord.setVeniceData(data.get(key));
//					} else if (key.equals(DataNameTokens.LOGACTIVITYRECONRECORD_PROVIDERDATA)) {
//						activityReconRecord.setProviderData(data.get(key));
					if (key.equals(DataNameTokens.LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID)) {
						LogActionApplied logActionApplied = new LogActionApplied();
						logActionApplied.setActionAppliedId(new Long(data.get(key)));
						activityReconRecord.setLogActionApplied(logActionApplied);
					} else if (key.equals(DataNameTokens.LOGACTIVITYRECONRECORD_MANUALLYENTEREDDATA)) {
						activityReconRecord.setManuallyEnteredData(data.get(key));
					}  else if (key.equals(DataNameTokens.LOGACTIVITYRECONRECORD_USERLOGONNAME)) {
						activityReconRecord.setUserLogonName(data.get(key));	
					}  else if (key.equals(DataNameTokens.LOGACTIVITYRECONRECORD_COMMENT)) {
						activityReconRecord.setComment(data.get(key));
					}
				}
				
				activityReconRecord.setUserLogonName(userName);			
				activityReconRecordList.add(activityReconRecord);			
			}
			
			activityReconRecordSessionHome.mergeLogActivityReconRecordList((ArrayList<LogActivityReconRecord>)activityReconRecordList);
			
			//Retrieve the data again for display in the screen			
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<activityReconRecordList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(activityReconRecordList.get(i).getActivityReconRecordId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID));
				criteria.add(simpleCriteria);
			}
			
			activityReconRecordList = activityReconRecordSessionHome.findByLogActivityReconRecordLike(activityReconRecord, criteria, 0, 0);			
			dataList = new ArrayList<HashMap<String, String>>();			
			Long airwayBillId = null;
			
			for (int i=0;i<activityReconRecordList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();				
				activityReconRecord = activityReconRecordList.get(i);
				
				map.put(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID, activityReconRecord.getActivityReconRecordId()!=null?activityReconRecord.getActivityReconRecordId().toString():"");
				map.put(DataNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID, activityReconRecord.getLogReconActivityRecordResult()!=null &&
						activityReconRecord.getLogReconActivityRecordResult().getReconRecordResultId()!=null?activityReconRecord.getLogReconActivityRecordResult().getReconRecordResultId().toString():"");
				map.put(DataNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTDESC, activityReconRecord.getLogReconActivityRecordResult()!=null?activityReconRecord.getLogReconActivityRecordResult().getReconRecordResultDesc():"");
				map.put(DataNameTokens.LOGACTIVITYRECONRECORD_VENICEDATA, activityReconRecord.getVeniceData());
				map.put(DataNameTokens.LOGACTIVITYRECONRECORD_PROVIDERDATA, activityReconRecord.getProviderData());
				map.put(DataNameTokens.LOGACTIVITYRECONRECORD_MANUALLYENTEREDDATA, activityReconRecord.getManuallyEnteredData());
				map.put(DataNameTokens.LOGACTIVITYRECONRECORD_USERLOGONNAME, activityReconRecord.getUserLogonName());
				map.put(DataNameTokens.LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, activityReconRecord.getLogActionApplied()!=null && activityReconRecord.getLogActionApplied().getActionAppliedId()!=null?activityReconRecord.getLogActionApplied().getActionAppliedId().toString():"");
				map.put(DataNameTokens.LOGACTIVITYRECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDDESC, activityReconRecord.getLogActionApplied()!=null?activityReconRecord.getLogActionApplied().getActionAppliedDesc():"");
				map.put(DataNameTokens.LOGACTIVITYRECONRECORD_COMMENT, activityReconRecord.getComment());
				map.put(DataNameTokens.LOGACTIVITYRECONRECORD_COMMENTHISTORY, "&#9658");
				airwayBillId = activityReconRecord.getLogAirwayBill()!=null?activityReconRecord.getLogAirwayBill().getAirwayBillId():null;
								
				dataList.add(map);
			}
			
			if (airwayBillId!=null) {
				//Update Airway Bill Data based on reconciliation actions			
				airwayBillLocator = new Locator<LogAirwayBillSessionEJBRemote>();
				
				LogAirwayBillSessionEJBRemote airwayBillSessionHome = (LogAirwayBillSessionEJBRemote) airwayBillLocator
				.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				LogAirwayBill airwayBill = activityReconRecord.getLogAirwayBill();
				//airwayBill.setAirwayBillId(airwayBillId);
				
				for (int i=0;i<activityReconRecordList.size();i++) {
					activityReconRecord = activityReconRecordList.get(i);
					String data = null;
					if (activityReconRecord.getLogActionApplied()!=null && activityReconRecord.getLogActionApplied().getActionAppliedId()!=null &&
							activityReconRecord.getLogActionApplied().getActionAppliedId().toString().equals(DataConstantNameTokens.LOGACTIONAPPLIED_ACTIONAPPLIEDID_VENICEDATAPPLIED)) {
						//VENICE DATA APPLIED
						data = activityReconRecord.getVeniceData();
					} else if (activityReconRecord.getLogActionApplied()!=null && activityReconRecord.getLogActionApplied().getActionAppliedId()!=null &&
							activityReconRecord.getLogActionApplied().getActionAppliedId().toString().equals(DataConstantNameTokens.LOGACTIONAPPLIED_ACTIONAPPLIEDID_PROVIDERDATAPPLIED)) {
						//PROVIDER DATA APPLIED
						data = activityReconRecord.getProviderData();
					} else if (activityReconRecord.getLogActionApplied()!=null && activityReconRecord.getLogActionApplied().getActionAppliedId()!=null &&
							activityReconRecord.getLogActionApplied().getActionAppliedId().toString().equals(DataConstantNameTokens.LOGACTIONAPPLIED_ACTIONAPPLIEDID_MANUALDATAPPLIED)) {
						//MANUAL DATA APPLIED
						data = activityReconRecord.getManuallyEnteredData();
					} 
					//What to do in case of "IGNORED"?
					if (data!=null) { 
						if (activityReconRecord.getLogReconActivityRecordResult()!=null && activityReconRecord.getLogReconActivityRecordResult().getReconRecordResultId()!=null) {
							if (activityReconRecord.getLogReconActivityRecordResult().getReconRecordResultId().toString().equals(
									DataConstantNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_TRACKINGNUMBERDOESNOTEXIST)) {
								airwayBill.setTrackingNumber(data);
							} else if (activityReconRecord.getLogReconActivityRecordResult().getReconRecordResultId().toString().equals(
									DataConstantNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_PICKUPDATELATE)) {
								try {
									//try to parse using XML format
									airwayBill.setAirwayBillPickupDateTime(new Timestamp(formatter.parse(data).getTime()));
								} catch (ParseException e) {
									//if fails, try to parse using DD-MM-YYYY
									airwayBill.setAirwayBillPickupDateTime(new Timestamp(formatter.parseFromDDMMYYYY(data).getTime()));
								}
							} else if (activityReconRecord.getLogReconActivityRecordResult().getReconRecordResultId().toString().equals(
									DataConstantNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_SETTLEMENTCODEMISMATCH)) {
								
								if(activityReconRecord.getLogAirwayBill().getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("JNE")){
									airwayBill.setAirwayBillNumber(data);
								}else if(activityReconRecord.getLogAirwayBill().getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("NCS")){
									airwayBill.setDeliveryOrder(data);
								}else if(activityReconRecord.getLogAirwayBill().getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("RPX")){
									airwayBill.setAirwayBillNumber(data);
								}
								
							} else if (activityReconRecord.getLogReconActivityRecordResult().getReconRecordResultId().toString().equals(
									DataConstantNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_SERVICEMISMATCH)) {
								airwayBill.setService(data);
							} else if (activityReconRecord.getLogReconActivityRecordResult().getReconRecordResultId().toString().equals(
									DataConstantNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_RECIPIENTMISMATCH)) {
								airwayBill.setConsignee(data);
							} else if (activityReconRecord.getLogReconActivityRecordResult().getReconRecordResultId().toString().equals(
									DataConstantNameTokens.LOGACTIVITYRECONRECORD_LOGRECONACTIVITYRECORDRESULT_RECONRECORDRESULTID_WEIGHTMISMATCH)) {
								airwayBill.setPackageWeight(new BigDecimal(data));
							}
						}
					}
				}
				airwayBillSessionHome.mergeLogAirwayBill(airwayBill);
			}
			
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(activityReconRecordList.size());
			rafDsResponse.setEndRow(request.getStartRow()+activityReconRecordList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(activityReconRecordLocator!=null)
					activityReconRecordLocator.close();
				if(airwayBillLocator!=null)
					airwayBillLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
