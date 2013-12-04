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
import com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.LogInvoiceAirwaybillRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class UpdateInvoiceAirwaybillRecordDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public UpdateInvoiceAirwaybillRecordDataCommand(RafDsRequest request, String userName) {
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
		
		List<LogInvoiceAirwaybillRecord> invoiceAWBRecordList = new ArrayList<LogInvoiceAirwaybillRecord>();		
		List<HashMap<String,String >> dataList = request.getData();		
		LogInvoiceAirwaybillRecord invoiceAWBRecord = new LogInvoiceAirwaybillRecord();
		
		Locator<Object> locator = null;
//		Locator<LogAirwayBillSessionEJBRemote> airwayBillLocator = null;
		
		try {
			locator = new Locator<Object>();
			
			LogInvoiceAirwaybillRecordSessionEJBRemote sessionHome = (LogInvoiceAirwaybillRecordSessionEJBRemote) locator
			.lookup(LogInvoiceAirwaybillRecordSessionEJBRemote.class, "LogInvoiceAirwaybillRecordSessionEJBBean");
			
			LogInvoiceReconRecordSessionEJBRemote reconSessionHome = (LogInvoiceReconRecordSessionEJBRemote) locator
			.lookup(LogInvoiceReconRecordSessionEJBRemote.class, "LogInvoiceReconRecordSessionEJBBean");
			
			String ids = "";
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);			
				Iterator<String> iter = data.keySet().iterator();
			
				while (iter.hasNext()) {
					String key = iter.next();
					
					if (key.equals(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID)) {
						ids += data.get(key);
						if(i<dataList.size()-1)
							ids += ",";
						try{
							invoiceAWBRecord = sessionHome.queryByRange("select o from LogInvoiceAirwaybillRecord o where o.invoiceAirwaybillRecordId="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							invoiceAWBRecord.setInvoiceAirwaybillRecordId((new Long(data.get(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID))));
						}
						break;
					}
				}			
				
				iter = data.keySet().iterator();
				
				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS)) {
						invoiceAWBRecord.setInvoiceResultStatus(data.get(key));
					}
				}
				
			if(!reconSessionHome.queryByRange("select o from LogInvoiceReconRecord o where o.logInvoiceAirwaybillRecord.invoiceAirwaybillRecordId="+invoiceAWBRecord.getInvoiceAirwaybillRecordId(), 0, 0).isEmpty())
				invoiceAWBRecordList.add(invoiceAWBRecord);			
			}
			
			sessionHome.mergeLogInvoiceAirwaybillRecordList(invoiceAWBRecordList);
			
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord : invoiceAWBRecordList) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(logInvoiceAirwaybillRecord.getInvoiceAirwaybillRecordId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID));
				criteria.add(simpleCriteria);
			}
			
			if(invoiceAWBRecordList.isEmpty()){
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID);
				simpleCriteria.setOperator("IN");
				simpleCriteria.setValue(ids);
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID));
				criteria.add(simpleCriteria);
			}
			
			invoiceAWBRecordList = sessionHome.findByLogInvoiceAirwaybillRecordLike(invoiceAWBRecord, criteria, 0, 0);			
			dataList = new ArrayList<HashMap<String, String>>();			
			
			for (LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord : invoiceAWBRecordList) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID, logInvoiceAirwaybillRecord.getInvoiceAirwaybillRecordId().toString());
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_AIRWAYBILLNUMBER, logInvoiceAirwaybillRecord.getAirwayBillNumber()!=null ?logInvoiceAirwaybillRecord.getAirwayBillNumber():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEOTHERCHARGE, logInvoiceAirwaybillRecord.getVeniceOtherCharge()!=null ?logInvoiceAirwaybillRecord.getVeniceOtherCharge().toString():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEPACKAGEWEIGHT, logInvoiceAirwaybillRecord.getVenicePackageWeight() != null ? logInvoiceAirwaybillRecord.getVenicePackageWeight().toString():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEPRICEPERKG, logInvoiceAirwaybillRecord.getVenicePricePerKg() != null ? logInvoiceAirwaybillRecord.getVenicePricePerKg().toString() :"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEGIFTWRAPCHARGE, logInvoiceAirwaybillRecord.getVeniceGiftWrapCharge() != null ? logInvoiceAirwaybillRecord.getVeniceGiftWrapCharge().toString() : "");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEINSURANCECHARGE, logInvoiceAirwaybillRecord.getVeniceInsuranceCharge() != null ? logInvoiceAirwaybillRecord.getVeniceInsuranceCharge().toString() : "");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICETOTALCHARGE, logInvoiceAirwaybillRecord.getVeniceTotalCharge() != null ?  logInvoiceAirwaybillRecord.getVeniceTotalCharge().toString() : "");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS, logInvoiceAirwaybillRecord.getInvoiceResultStatus());
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID, logInvoiceAirwaybillRecord.getLogInvoiceReportUpload().getLogApprovalStatus().getApprovalStatusId().toString());
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC, logInvoiceAirwaybillRecord.getLogInvoiceReportUpload().getLogApprovalStatus().getApprovalStatusDesc());
				
				dataList.add(map);
			}
			
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(invoiceAWBRecordList.size());
			rafDsResponse.setEndRow(request.getStartRow()+invoiceAWBRecordList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				locator.close();
//				airwayBillLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
