package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.LogActionApplied;
import com.gdn.venice.persistence.LogInvoiceReconRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class UpdateInvoiceAirwayBillReconciliationProblemDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public UpdateInvoiceAirwayBillReconciliationProblemDataCommand(RafDsRequest request, String userName) {
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
		
		List<LogInvoiceReconRecord> invoiceReconRecordList = new ArrayList<LogInvoiceReconRecord>();		
		List<HashMap<String,String >> dataList = request.getData();		
		LogInvoiceReconRecord invoiceReconRecord = new LogInvoiceReconRecord();
		
		Locator<LogInvoiceReconRecordSessionEJBRemote> invoiceReconRecordLocator = null;
//		Locator<LogAirwayBillSessionEJBRemote> airwayBillLocator = null;
		
		try {
			invoiceReconRecordLocator = new Locator<LogInvoiceReconRecordSessionEJBRemote>();
			
			LogInvoiceReconRecordSessionEJBRemote sessionHome = (LogInvoiceReconRecordSessionEJBRemote) invoiceReconRecordLocator
			.lookup(LogInvoiceReconRecordSessionEJBRemote.class, "LogInvoiceReconRecordSessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);			
				Iterator<String> iter = data.keySet().iterator();
				
				while (iter.hasNext()) {
					String key = iter.next();

					if (key.equals(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID)) {
						try{
							invoiceReconRecord = sessionHome.queryByRange("select o from LogInvoiceReconRecord o where o.invoiceReconRecordId="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							invoiceReconRecord.setInvoiceReconRecordId(new Long(data.get(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID)));
						}
						break;
					}
				}			
				
				iter = data.keySet().iterator();
				
				while (iter.hasNext()) {
					String key = iter.next();

					//we do not need to set LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTDESC here					
//					} else if (key.equals(DataNameTokens.LOGINVOICERECONRECORD_VENICEDATA)) {
//						activityReconRecord.setVeniceData(data.get(key));
//					} else if (key.equals(DataNameTokens.LOGINVOICERECONRECORD_PROVIDERDATA)) {
//						activityReconRecord.setProviderData(data.get(key));
					if (key.equals(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID)) {
						LogActionApplied logActionApplied = new LogActionApplied();
						logActionApplied.setActionAppliedId(new Long(data.get(key)));
						invoiceReconRecord.setLogActionApplied(logActionApplied);
					} else if (key.equals(DataNameTokens.LOGINVOICERECONRECORD_MANUALLYENTEREDDATA)) {
						invoiceReconRecord.setManuallyEnteredData(data.get(key));
					}  else if (key.equals(DataNameTokens.LOGINVOICERECONRECORD_USERLOGONNAME)) {
						invoiceReconRecord.setUserLogonName(data.get(key));	
					}  else if (key.equals(DataNameTokens.LOGINVOICERECONRECORD_COMMENT)) {
						invoiceReconRecord.setComment(data.get(key));
					}
				}			
				
				invoiceReconRecord.setUserLogonName(userName);			
				invoiceReconRecordList.add(invoiceReconRecord);			
			}
			
			sessionHome.mergeLogInvoiceReconRecordList((ArrayList<LogInvoiceReconRecord>)invoiceReconRecordList);
			
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<invoiceReconRecordList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(invoiceReconRecordList.get(i).getInvoiceReconRecordId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID));
				criteria.add(simpleCriteria);
			}
			
			invoiceReconRecordList = sessionHome.findByLogInvoiceReconRecordLike(invoiceReconRecord, criteria, 0, 0);			
			dataList = new ArrayList<HashMap<String, String>>();			
//			Long invoiceAirwaybillRecordId = null;
			
			for (int i=0;i<invoiceReconRecordList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();				
				invoiceReconRecord = invoiceReconRecordList.get(i);
				
				map.put(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID, invoiceReconRecord.getInvoiceReconRecordId()!=null?invoiceReconRecord.getInvoiceReconRecordId().toString():"");
				map.put(DataNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID, invoiceReconRecord.getLogReconInvoiceRecordResult()!=null &&
						invoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId()!=null?invoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().toString():"");
				map.put(DataNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTDESC, invoiceReconRecord.getLogReconInvoiceRecordResult()!=null?invoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultDesc():"");
				map.put(DataNameTokens.LOGINVOICERECONRECORD_VENICEDATA, invoiceReconRecord.getVeniceData());
				map.put(DataNameTokens.LOGINVOICERECONRECORD_PROVIDERDATA, invoiceReconRecord.getProviderData());
				map.put(DataNameTokens.LOGINVOICERECONRECORD_MANUALLYENTEREDDATA, invoiceReconRecord.getManuallyEnteredData());
				map.put(DataNameTokens.LOGINVOICERECONRECORD_USERLOGONNAME, invoiceReconRecord.getUserLogonName());
				map.put(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, invoiceReconRecord.getLogActionApplied()!=null && invoiceReconRecord.getLogActionApplied().getActionAppliedId()!=null?invoiceReconRecord.getLogActionApplied().getActionAppliedId().toString():"");
				map.put(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDDESC, invoiceReconRecord.getLogActionApplied()!=null?invoiceReconRecord.getLogActionApplied().getActionAppliedDesc():"");
				map.put(DataNameTokens.LOGINVOICERECONRECORD_COMMENT, invoiceReconRecord.getComment());
				map.put(DataNameTokens.LOGINVOICERECONRECORD_COMMENTHISTORY, "&#9658");
//				invoiceAirwaybillRecordId = invoiceReconRecord.getLogInvoiceAirwaybillRecord()!=null?invoiceReconRecord.getLogInvoiceAirwaybillRecord().getInvoiceAirwaybillRecordId():null;
				
				dataList.add(map);
			}
			
//			if (invoiceAirwaybillRecordId!=null) {
//				//Update Airway Bill Data based on reconciliation actions			
//				airwayBillLocator = new Locator<LogAirwayBillSessionEJBRemote>();
//				
//				LogAirwayBillSessionEJBRemote airwayBillSessionHome = (LogAirwayBillSessionEJBRemote) airwayBillLocator
//				.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
//				
//				Long airwayBillId = new Long("0");
//				LogAirwayBill airwayBill;
//				try{
//					airwayBill = airwayBillSessionHome.queryByRange("select o from LogAirwayBill o where o.airwayBillId="+airwayBillId, 0, 1).get(0);
//				}catch(IndexOutOfBoundsException e){
//					airwayBill = new LogAirwayBill();
//					airwayBill.setAirwayBillId(airwayBillId);
//				}
//				
//				for (int i=0;i<invoiceReconRecordList.size();i++) {
//					invoiceReconRecord = invoiceReconRecordList.get(i);
//					String data = null;
//					if (invoiceReconRecord.getLogActionApplied()!=null && invoiceReconRecord.getLogActionApplied().getActionAppliedId()!=null &&
//							invoiceReconRecord.getLogActionApplied().getActionAppliedId().toString().equals(DataConstantNameTokens.LOGACTIONAPPLIED_ACTIONAPPLIEDID_VENICEDATAPPLIED)) {
//						//VENICE DATA APPLIED
//						data = invoiceReconRecord.getVeniceData();
//					} else if (invoiceReconRecord.getLogActionApplied()!=null && invoiceReconRecord.getLogActionApplied().getActionAppliedId()!=null &&
//							invoiceReconRecord.getLogActionApplied().getActionAppliedId().toString().equals(DataConstantNameTokens.LOGACTIONAPPLIED_ACTIONAPPLIEDID_PROVIDERDATAPPLIED)) {
//						//PROVIDER DATA APPLIED
//						data = invoiceReconRecord.getProviderData();
//					} else if (invoiceReconRecord.getLogActionApplied()!=null && invoiceReconRecord.getLogActionApplied().getActionAppliedId()!=null &&
//							invoiceReconRecord.getLogActionApplied().getActionAppliedId().toString().equals(DataConstantNameTokens.LOGACTIONAPPLIED_ACTIONAPPLIEDID_MANUALDATAPPLIED)) {
//						//MANUAL DATA APPLIED
//						data = invoiceReconRecord.getManuallyEnteredData();
//					} 
//					//What to do in case of "IGNORED"?
//					if (data!=null) { 
//						if (invoiceReconRecord.getLogReconInvoiceRecordResult()!=null && invoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId()!=null) {
//							if (invoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().toString().equals(
//									DataConstantNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_WEIGHTMISMATCH)) {
//								airwayBill.setPackageWeight(new BigDecimal(data));
//							} else if (invoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().toString().equals(
//									DataConstantNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_PRICEPERKGMISMATCH)) {
//								airwayBill.setPricePerKg(new BigDecimal(data));
//							} else if (invoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().toString().equals(
//									DataConstantNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_INSURANCECOSTMISMATCH)) {
//								airwayBill.setInsuranceCharge(new BigDecimal(data));
//							} else if (invoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().toString().equals(
//									DataConstantNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_OTHERCHARGEMISMATCH)) {
//								airwayBill.setOtherCharge(new BigDecimal(data));
//							} else if (invoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().toString().equals(
//									DataConstantNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_GIFTWRAPMISMATCH)) {
//								airwayBill.setGiftWrapCharge(new BigDecimal(data));
//							} else if (invoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().toString().equals(
//									DataConstantNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_TOTALCHARGEMISMATCH)) {
//								airwayBill.setTotalCharge(new BigDecimal(data));
//							} else if (invoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().toString().equals(
//									DataConstantNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID_AIRWAYBILLMISMATCH)){
//								if(invoiceReconRecord.getLogInvoiceAirwaybillRecord().getLogInvoiceReportUpload().getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("JNE")){
//									airwayBill.setAirwayBillNumber(data);
//								}else if(invoiceReconRecord.getLogInvoiceAirwaybillRecord().getLogInvoiceReportUpload().getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("NCS")){
//									airwayBill.setAirwayBillNumber(data);
//									airwayBill.setDeliveryOrder(data);
//								}else if(invoiceReconRecord.getLogInvoiceAirwaybillRecord().getLogInvoiceReportUpload().getLogLogisticsProvider().getLogisticsProviderCode().equalsIgnoreCase("RPX")){
//									airwayBill.setAirwayBillNumber(data);
//								}
//							}
//						}
//					}
//				}
//				airwayBillSessionHome.mergeLogAirwayBill(airwayBill);
//			}
			
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(invoiceReconRecordList.size());
			rafDsResponse.setEndRow(request.getStartRow()+invoiceReconRecordList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				invoiceReconRecordLocator.close();
//				airwayBillLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
