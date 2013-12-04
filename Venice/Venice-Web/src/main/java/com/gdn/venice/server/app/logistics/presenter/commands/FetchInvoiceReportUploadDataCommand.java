package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote;
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class FetchInvoiceReportUploadDataCommand  implements RafDsCommand {
	private RafDsRequest request;
	private String userName;
	
	public FetchInvoiceReportUploadDataCommand(RafDsRequest request, String userName) {
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
			HashMap<String,String> invoiceNumbers = bpmAdapter.getExternalDataVariableAsHashMap(new Long(taskId), ProcessNameTokens.INVOICERENUMBER);
			
			if (criteria!=null) {
				criteriaAndTaskCriteria = new JPQLAdvancedQueryCriteria("and");
				criteriaAndTaskCriteria.add(criteria);
			}
			else criteriaAndTaskCriteria = new JPQLAdvancedQueryCriteria();
//			criteriaAndTaskCriteria.add(taskCriteria);			
			request.setCriteria(criteriaAndTaskCriteria);
			
			/*
			 * Build a new simple criteria as an IN() list
			 */
			JPQLSimpleQueryCriteria inCriteria = new JPQLSimpleQueryCriteria();
			inCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICENUMBER));
			inCriteria.setFieldName(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICENUMBER);
			
			String invoiceNumberList = "";
			for(String value:invoiceNumbers.values()){
				if(invoiceNumberList.isEmpty()){
					invoiceNumberList = value;
				}
				else{
					invoiceNumberList += "," + value;
				}
			}
			inCriteria.setValue(invoiceNumberList);
			inCriteria.setOperator("IN");
			inCriteria.setCaseSensitive(true);
			
			criteriaAndTaskCriteria.add(inCriteria);
		}
		
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		
		Locator<Object> locator = null;
		
		try{
			locator = new Locator<Object>();
			
			LogInvoiceReportUploadSessionEJBRemote sessionHome = (LogInvoiceReportUploadSessionEJBRemote) locator
				.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");
			
			List<LogInvoiceReportUpload> invoiceReportUploadList = null;
			
			LogInvoiceReportUpload invoiceReportUpload= new LogInvoiceReportUpload();
			
			if (criteria==null && criteriaAndTaskCriteria == null) {
				invoiceReportUploadList = sessionHome.queryByRange("select o from LogInvoiceReportUpload o order by o.reportReconciliationTimestamp desc", 0, 20);
			} else {
				if(criteriaAndTaskCriteria != null)
					invoiceReportUploadList = sessionHome.findByLogInvoiceReportUploadLike(invoiceReportUpload, criteriaAndTaskCriteria, 0, 0);
				else
					invoiceReportUploadList = sessionHome.findByLogInvoiceReportUploadLike(invoiceReportUpload, criteria, 0, 0);
			}
			
			for (LogInvoiceReportUpload logInvoiceReportUpload : invoiceReportUploadList) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				map.put(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID, logInvoiceReportUpload.getInvoiceReportUploadId().toString());
				map.put(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICENUMBER, logInvoiceReportUpload.getInvoiceNumber()!=null ?logInvoiceReportUpload.getInvoiceNumber():"");
				map.put(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, logInvoiceReportUpload.getLogLogisticsProvider().getLogisticsProviderCode()!=null ?logInvoiceReportUpload.getLogLogisticsProvider().getLogisticsProviderCode():"");
				map.put(DataNameTokens.LOGINVOICEREPORTUPLOAD_FILENAMEANDLOCATION, logInvoiceReportUpload.getFileNameAndLocation()!=null ?logInvoiceReportUpload.getFileNameAndLocation():"");
				map.put(DataNameTokens.LOGINVOICEREPORTUPLOAD_REPORTRECONCILIATIONTIMESTAMP, formatter.format(logInvoiceReportUpload.getReportReconciliationTimestamp()));
				map.put(DataNameTokens.LOGINVOICEREPORTUPLOAD_USERLOGONNAME, logInvoiceReportUpload.getUserLogonName() != null ? logInvoiceReportUpload.getUserLogonName() :"");
				map.put(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICERECONTOLERANCE, logInvoiceReportUpload.getInvoiceReconTolerance() != null ? logInvoiceReportUpload.getInvoiceReconTolerance() : "");
				map.put(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSID, logInvoiceReportUpload.getLogApprovalStatus().getApprovalStatusId() != null ? logInvoiceReportUpload.getLogApprovalStatus().getApprovalStatusId().toString() : "");
				map.put(DataNameTokens.LOGINVOICEREPORTUPLOAD_LOGAPPROVALSTATUS_APPROVALSTATUSDESC, logInvoiceReportUpload.getLogApprovalStatus().getApprovalStatusDesc() != null ?  logInvoiceReportUpload.getLogApprovalStatus().getApprovalStatusDesc() : "");
				map.put(DataNameTokens.LOGINVOICEREPORTUPLOAD_APPROVEDBY, logInvoiceReportUpload.getApprovedBy()!=null?logInvoiceReportUpload.getApprovedBy():"");
				
				dataList.add(map);
				
			}
			
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(invoiceReportUploadList.size());
			rafDsResponse.setEndRow(request.getStartRow()+invoiceReportUploadList.size());
			
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
