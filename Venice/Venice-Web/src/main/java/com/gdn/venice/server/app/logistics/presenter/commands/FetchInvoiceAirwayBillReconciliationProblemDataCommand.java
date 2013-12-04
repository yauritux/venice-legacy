package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote;
import com.gdn.venice.persistence.LogInvoiceReconRecord;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;

public class FetchInvoiceAirwayBillReconciliationProblemDataCommand implements
		RafDsCommand {
RafDsRequest request;
	
	public FetchInvoiceAirwayBillReconciliationProblemDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		String invoiceAirwaybillRecordId = request.getParams().get(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID);
		
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		if (invoiceAirwaybillRecordId!=null) {
			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			
			Locator<LogInvoiceReconRecord> invoiceAirwayBillReconciliationLocator = null;
			
			try {
				invoiceAirwayBillReconciliationLocator = new Locator<LogInvoiceReconRecord>();
				
				LogInvoiceReconRecordSessionEJBRemote invoiceAirwayBillReconciliationHome = (LogInvoiceReconRecordSessionEJBRemote) invoiceAirwayBillReconciliationLocator
				.lookup(LogInvoiceReconRecordSessionEJBRemote.class, "LogInvoiceReconRecordSessionEJBBean");
				
				JPQLAdvancedQueryCriteria criteria;
				if (request.getCriteria()!=null){
					 criteria = request.getCriteria();
					criteria.setBooleanOperator("and");
				}
				else criteria = new JPQLAdvancedQueryCriteria();
				
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.LOGINVOICERECONRECORD_LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(invoiceAirwaybillRecordId);
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.LOGINVOICERECONRECORD_LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID));
				criteria.add(simpleCriteria);
				
				LogInvoiceReconRecord logInvoiceReconRecord = new LogInvoiceReconRecord();
				
//				List<LogInvoiceReconRecord> logInvoiceReconRecordList  = invoiceAirwayBillReconciliationHome.findByLogInvoiceReconRecordLike(logInvoiceReconRecord, criteria,request.getStartRow(), request.getEndRow());
				List<LogInvoiceReconRecord> logInvoiceReconRecordList  = invoiceAirwayBillReconciliationHome.findByLogInvoiceReconRecordLike(logInvoiceReconRecord, criteria, 0, 0);
				
				
				for (int i=0;i<logInvoiceReconRecordList.size();i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					
					logInvoiceReconRecord = logInvoiceReconRecordList.get(i);
					
					map.put(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID, logInvoiceReconRecord.getInvoiceReconRecordId()!=null?logInvoiceReconRecord.getInvoiceReconRecordId().toString():"");
					map.put(DataNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTID, 
							logInvoiceReconRecord.getLogReconInvoiceRecordResult()!=null && 
							logInvoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId() != null?
									logInvoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultId().toString():"");
					map.put(DataNameTokens.LOGINVOICERECONRECORD_LOGRECONINVOICERECORDRESULT_RECONRECORDRESULTDESC, logInvoiceReconRecord.getLogReconInvoiceRecordResult()!=null?logInvoiceReconRecord.getLogReconInvoiceRecordResult().getReconRecordResultDesc():"");
					map.put(DataNameTokens.LOGINVOICERECONRECORD_VENICEDATA, logInvoiceReconRecord.getVeniceData());
					map.put(DataNameTokens.LOGINVOICERECONRECORD_PROVIDERDATA, logInvoiceReconRecord.getProviderData());
					map.put(DataNameTokens.LOGINVOICERECONRECORD_MANUALLYENTEREDDATA, logInvoiceReconRecord.getManuallyEnteredData());
					map.put(DataNameTokens.LOGINVOICERECONRECORD_USERLOGONNAME, logInvoiceReconRecord.getUserLogonName());
					map.put(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDID, logInvoiceReconRecord.getLogActionApplied()!=null && logInvoiceReconRecord.getLogActionApplied().getActionAppliedId()!=null?logInvoiceReconRecord.getLogActionApplied().getActionAppliedId().toString():"");
					map.put(DataNameTokens.LOGINVOICERECONRECORD_LOGACTIONAPPLIED_ACTIONAPPLIEDDESC, logInvoiceReconRecord.getLogActionApplied()!=null?logInvoiceReconRecord.getLogActionApplied().getActionAppliedDesc():"");
					map.put(DataNameTokens.LOGINVOICERECONRECORD_COMMENT, logInvoiceReconRecord.getComment());
					map.put(DataNameTokens.LOGINVOICERECONRECORD_COMMENTHISTORY, "&#9658");
					
					dataList.add(map);
				}
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(logInvoiceReconRecordList.size());
				rafDsResponse.setEndRow(request.getStartRow()+logInvoiceReconRecordList.size());
			} catch (Exception e) {
				e.printStackTrace();
				rafDsResponse.setStatus(-1);
			} finally {
				try {
					invoiceAirwayBillReconciliationLocator.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			

			rafDsResponse.setData(dataList);

			
		} else {
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(0);
			rafDsResponse.setEndRow(request.getStartRow());
		}
		
		
		return rafDsResponse;
	}

}
