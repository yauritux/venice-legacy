package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote;
import com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote;
import com.gdn.venice.facade.logistics.invoice.LogAWBInvoiceApprovalHelperSessionEJBRemote;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.persistence.LogInvoiceReconCommentHistory;
import com.gdn.venice.persistence.LogInvoiceReconCommentHistoryPK;
import com.gdn.venice.persistence.LogInvoiceReconRecord;
import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafRpcCommand;

public class InvoiceAirwayBillProcessCommand implements RafRpcCommand {
	
	HashMap<String,String> invoiceNumbers;
	String parameter;
	String userName;
	String method;
	HttpServletRequest request;
	
	public InvoiceAirwayBillProcessCommand(String parameter, String userName, String method, HttpServletRequest request) {
		Pattern p = Pattern.compile("[\\{\\}\\=\\,]++");
        String[] split = p.split( parameter );

        invoiceNumbers = new HashMap<String,String>();
        for ( int i=1; i< split.length; i+=2) {
            invoiceNumbers.put((split[i]).trim(), (split[i+1]).trim());
        }
        
		this.parameter = parameter;
		this.userName = userName;
		this.method = method;
		this.request = request;
	}

	@Override
	public String execute() {
		if (method.equals("submitForApproval")) {
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
			
			bpmAdapter.synchronize();
			
			HashMap<String, String> taskData = new HashMap<String, String>();
			taskData.put(ProcessNameTokens.INVOICERENUMBER, parameter);
		
			try {
				bpmAdapter.startBusinessProcess(ProcessNameTokens.LOGISTICSINVOICEAPPROVAL, taskData);
			} catch (Exception e) {
				e.printStackTrace();
				return "-1";
			}
		} else if (invoiceNumbers.isEmpty()) {
			return "O";
		} 

		ArrayList<LogInvoiceReportUpload> invoiceReportList = new ArrayList<LogInvoiceReportUpload>();		
		Locator<Object> locator = null;
		try {	
			locator = new Locator<Object>();

			LogInvoiceReportUploadSessionEJBRemote sessionHome = (LogInvoiceReportUploadSessionEJBRemote) locator
			.lookup(LogInvoiceReportUploadSessionEJBRemote.class, "LogInvoiceReportUploadSessionEJBBean");

			LogAWBInvoiceApprovalHelperSessionEJBRemote logAirwayBillApprovalHelperHome = (LogAWBInvoiceApprovalHelperSessionEJBRemote) locator
				.lookup(LogAWBInvoiceApprovalHelperSessionEJBRemote.class, "LogAWBInvoiceApprovalHelperSessionEJBBean");
			
			LogInvoiceReconCommentHistorySessionEJBRemote commentSessionHome = (LogInvoiceReconCommentHistorySessionEJBRemote) locator
				.lookup(LogInvoiceReconCommentHistorySessionEJBRemote.class, "LogInvoiceReconCommentHistorySessionEJBBean");
			
			LogInvoiceReconRecordSessionEJBRemote logInvoiceReconRecordSessionHome = (LogInvoiceReconRecordSessionEJBRemote) locator
				.lookup(LogInvoiceReconRecordSessionEJBRemote.class, "LogInvoiceReconRecordSessionEJBBean");	

			for(String invoiceNumber : invoiceNumbers.values()){
				List<LogInvoiceReportUpload> invoiceReportUploadList = sessionHome.queryByRange("select o from LogInvoiceReportUpload o where o.invoiceNumber = '"+invoiceNumber+"'", 0, 0);
				LogInvoiceReportUpload  invoiceReportUpload = invoiceReportUploadList.get(0);

				LogApprovalStatus approvalStatus = new LogApprovalStatus();
				if (method.equalsIgnoreCase("approveInvoiceAirwayBill")) {
					approvalStatus.setApprovalStatusId(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_APPROVED);
				} else if (method.equalsIgnoreCase("rejectInvoiceAirwayBill")) {
					approvalStatus.setApprovalStatusId(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_REJECTED);
				} else if (method.equalsIgnoreCase("submitForApproval")) {
					approvalStatus.setApprovalStatusId(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_SUBMITTED);
				}

				invoiceReportUpload.setLogApprovalStatus(approvalStatus);
				invoiceReportUpload.setApprovedBy(userName);

				invoiceReportList.add(invoiceReportUpload);
			}
			sessionHome.mergeLogInvoiceReportUploadList(invoiceReportList);
			
			/*
			 * Rewritten to do these as separate transactions so that the back
			 * end can keep track of the invoices that are approved and
			 * determine when the invoice document should be created. 
			 */
			for (LogInvoiceReportUpload invoiceReport : invoiceReportList) {
				/*
				 * Apply the reconciled data to the AWB if the method is
				 * approve
				 */
				if (method.equalsIgnoreCase("approveInvoiceAirwayBill")) {
					invoiceReport = logAirwayBillApprovalHelperHome.applyInvoiceReconciliationData(invoiceReport);

					/*
					 * Create the finance invoice from here
					 */
					logAirwayBillApprovalHelperHome.createLogisticsFinanceInvoice(invoiceReport);
				}
				

				/*
				 * Add a comment for each airway bill invoice reconciliation record
				 * if there are any. If the reconciliation is ok then 
				 * nothing to do because there are no recon records.
				 */
				List<LogInvoiceReconRecord> logInvoiceReconRecordList = logInvoiceReconRecordSessionHome.queryByRange("select o from LogInvoiceReconRecord o " +
						"where o.logInvoiceAirwaybillRecord.logInvoiceReportUpload.invoiceReportUploadId = " + invoiceReport.getInvoiceReportUploadId(), 0, 0);

				if(logInvoiceReconRecordList != null && !logInvoiceReconRecordList.isEmpty()){
					for (LogInvoiceReconRecord record :logInvoiceReconRecordList) {
						LogInvoiceReconCommentHistory logInvoiceReconCommentHistory = new LogInvoiceReconCommentHistory();
						LogInvoiceReconCommentHistoryPK id = new LogInvoiceReconCommentHistoryPK();
						id.setHistoryTimestamp(new Date());

						id.setInvoiceReconRecordId(record.getInvoiceReconRecordId());

						/*
						 * Set the comment based on the approval 
						 * status of the AWB
						 */
						
						String comment = "";
						
						if(invoiceReport.getLogApprovalStatus().getApprovalStatusId().equals(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_APPROVED)){
							comment = "Approved";
						}else if(invoiceReport.getLogApprovalStatus().getApprovalStatusId().equals(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_REJECTED)){
							comment = "Rejected";
						}else if(invoiceReport.getLogApprovalStatus().getApprovalStatusId().equals(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_SUBMITTED)){
							comment = "Submitted";
						}
						logInvoiceReconCommentHistory.setComment(comment);
						logInvoiceReconCommentHistory.setId(id);
						logInvoiceReconCommentHistory.setLogInvoiceReconRecord(record);
						logInvoiceReconCommentHistory.setUserLogonName(this.userName);

						commentSessionHome.persistLogInvoiceReconCommentHistory(logInvoiceReconCommentHistory);
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "-1";
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}			
		return "0";
	}
}
