package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote;
import com.gdn.venice.persistence.LogInvoiceAirwaybillRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchInvoiceAirwaybillRecordDataCommand  implements RafDsCommand {
	private RafDsRequest request;
//	private String userName;
	
	public FetchInvoiceAirwaybillRecordDataCommand(RafDsRequest request) {
		this.request = request;
//		this.userName = userName;
	}
	
	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		String invoiceReportUploadId = request.getParams().get(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID);
		
		JPQLAdvancedQueryCriteria criteriaAndTaskCriteria = null;
		//check for taskid parameter, it shall be there if this screen is called from ToDoList for approval purpose
//		if (request.getParams()!=null && request.getParams().get(DataNameTokens.TASKID)!=null) {
//			String taskId = request.getParams().get(DataNameTokens.TASKID);
//			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
//			
//			bpmAdapter.synchronize();
//			HashMap<String,String> invoiceAirwaybillRecordIds = bpmAdapter.getExternalDataVariableAsHashMap(new Long(taskId), ProcessNameTokens.INVOICEAIRWAYBILLRECORDID);
//			
//			criteriaAndTaskCriteria = new JPQLAdvancedQueryCriteria("and");
//			if (criteria!=null) {
//				criteriaAndTaskCriteria.add(criteria);
//			}
////			criteriaAndTaskCriteria.add(taskCriteria);			
//			request.setCriteria(criteriaAndTaskCriteria);
//			
//			/*
//			 * Build a new simple criteria as an IN() list
//			 */
//			JPQLSimpleQueryCriteria inCriteria = new JPQLSimpleQueryCriteria();
//			inCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID));
//			inCriteria.setFieldName(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID);
//			
//			String invoiceAirwaybillRecordIdList = "";
//			for(String value:invoiceAirwaybillRecordIds.values()){
//				if(invoiceAirwaybillRecordIdList.isEmpty()){
//					invoiceAirwaybillRecordIdList = value;
//				}
//				else{
//					invoiceAirwaybillRecordIdList = invoiceAirwaybillRecordIdList + "," + value;
//				}
//			}
//			inCriteria.setValue(invoiceAirwaybillRecordIdList);
//			inCriteria.setOperator("IN");
//			
//			criteriaAndTaskCriteria.add(inCriteria);
//		}
		
		if (criteria!=null) {
			criteriaAndTaskCriteria = new JPQLAdvancedQueryCriteria("and");
			criteriaAndTaskCriteria.add(criteria);
		} else {
			criteriaAndTaskCriteria = new JPQLAdvancedQueryCriteria();
		}
//		criteriaAndTaskCriteria.add(taskCriteria);			
		request.setCriteria(criteriaAndTaskCriteria);
		
		/*
		 * Build a new simple criteria as an equals
		 */
		JPQLSimpleQueryCriteria inCriteria = new JPQLSimpleQueryCriteria();
		inCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID));
		inCriteria.setFieldName(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID);
				
		inCriteria.setValue(invoiceReportUploadId);
		inCriteria.setOperator("equals");
		
		criteriaAndTaskCriteria.add(inCriteria);
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			
			LogInvoiceAirwaybillRecordSessionEJBRemote sessionHome = (LogInvoiceAirwaybillRecordSessionEJBRemote) locator
				.lookup(LogInvoiceAirwaybillRecordSessionEJBRemote.class, "LogInvoiceAirwaybillRecordSessionEJBBean");
			
			List<LogInvoiceAirwaybillRecord> invoiceAirwaybillRecordList = null;
			
			LogInvoiceAirwaybillRecord invoiceAirwaybillRecord = new LogInvoiceAirwaybillRecord();
			
//			if (criteria==null && criteriaAndTaskCriteria == null) {
//				invoiceAirwaybillRecordList = sessionHome.queryByRange("select o from LogInvoiceAirwaybillRecord o where o.logInvoiceReportUpload.invoiceReportUploadId=" + invoiceReportUploadId +
//						" order by o.logInvoiceReportUpload.reportReconciliationTimestamp desc", 0, 0);
//			} else {
//				if(criteria != null)
//					invoiceAirwaybillRecordList = sessionHome.findByLogInvoiceAirwaybillRecordLike(invoiceAirwaybillRecord, criteriaAndTaskCriteria, 0, 0);
//				else
					invoiceAirwaybillRecordList = sessionHome.findByLogInvoiceAirwaybillRecordLike(invoiceAirwaybillRecord, criteriaAndTaskCriteria, 0, 0);
//			}
			
			for (LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord : invoiceAirwaybillRecordList) {
				HashMap<String, String> map = new HashMap<String, String>();
								
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID, logInvoiceAirwaybillRecord.getInvoiceAirwaybillRecordId().toString());
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_AIRWAYBILLNUMBER, logInvoiceAirwaybillRecord.getAirwayBillNumber()!=null ?logInvoiceAirwaybillRecord.getAirwayBillNumber():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEOTHERCHARGE, logInvoiceAirwaybillRecord.getVeniceOtherCharge()!=null ?logInvoiceAirwaybillRecord.getVeniceOtherCharge().toString():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDEROTHERCHARGE, logInvoiceAirwaybillRecord.getProviderOtherCharge()!=null ?logInvoiceAirwaybillRecord.getProviderOtherCharge().toString():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEPACKAGEWEIGHT, logInvoiceAirwaybillRecord.getVenicePackageWeight() != null ? logInvoiceAirwaybillRecord.getVenicePackageWeight().toString():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERPACKAGEWEIGHT, logInvoiceAirwaybillRecord.getProviderPackageWeight() != null ? logInvoiceAirwaybillRecord.getProviderPackageWeight().toString():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEPRICEPERKG, logInvoiceAirwaybillRecord.getVenicePricePerKg() != null ? logInvoiceAirwaybillRecord.getVenicePricePerKg().toString() :"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERPRICEPERKG, logInvoiceAirwaybillRecord.getProviderPricePerKg()!=null ?logInvoiceAirwaybillRecord.getProviderPricePerKg().toString():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEGIFTWRAPCHARGE, logInvoiceAirwaybillRecord.getVeniceGiftWrapCharge() != null ? logInvoiceAirwaybillRecord.getVeniceGiftWrapCharge().toString() : "");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERGIFTWRAPCHARGE, logInvoiceAirwaybillRecord.getProviderGiftWrapCharge()!=null ?logInvoiceAirwaybillRecord.getProviderGiftWrapCharge().toString():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICEINSURANCECHARGE, logInvoiceAirwaybillRecord.getVeniceInsuranceCharge() != null ? logInvoiceAirwaybillRecord.getVeniceInsuranceCharge().toString() : "");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERINSURANCECHARGE, logInvoiceAirwaybillRecord.getProviderInsuranceCharge()!=null ?logInvoiceAirwaybillRecord.getProviderInsuranceCharge().toString():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_VENICETOTALCHARGE, logInvoiceAirwaybillRecord.getVeniceTotalCharge() != null ?  logInvoiceAirwaybillRecord.getVeniceTotalCharge().toString() : "");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_PROVIDERTOTALCHARGE, logInvoiceAirwaybillRecord.getProviderTotalCharge()!=null ?logInvoiceAirwaybillRecord.getProviderTotalCharge().toString():"");
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICERESULTSTATUS, logInvoiceAirwaybillRecord.getInvoiceResultStatus());
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID, logInvoiceAirwaybillRecord.getLogInvoiceReportUpload().getLogApprovalStatus().getApprovalStatusId().toString());
				map.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC, logInvoiceAirwaybillRecord.getLogInvoiceReportUpload().getLogApprovalStatus().getApprovalStatusDesc());
				
				dataList.add(map);
				
			}
			
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(invoiceAirwaybillRecordList.size());
			rafDsResponse.setEndRow(request.getStartRow()+invoiceAirwaybillRecordList.size());
			
		}catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
}
