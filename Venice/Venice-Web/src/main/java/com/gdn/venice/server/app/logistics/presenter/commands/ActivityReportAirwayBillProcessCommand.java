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
import com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBRemote;
import com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote;
import com.gdn.venice.facade.LogAirwayBillSessionEJBRemote;
import com.gdn.venice.persistence.LogActivityReconCommentHistory;
import com.gdn.venice.persistence.LogActivityReconCommentHistoryPK;
import com.gdn.venice.persistence.LogActivityReconRecord;
import com.gdn.venice.persistence.LogAirwayBill;
import com.gdn.venice.persistence.LogApprovalStatus;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafRpcCommand;

public class ActivityReportAirwayBillProcessCommand implements RafRpcCommand {

	HashMap<String, String> airwayBillIds;
	String parameter;
	String userName;
	String method;
	HttpServletRequest request;

	public ActivityReportAirwayBillProcessCommand(String parameter,
			String userName, String method, HttpServletRequest request) {
		Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
		String[] split = p.split(parameter);

		airwayBillIds = new HashMap<String, String>();
		for (int i = 1; i < split.length; i += 2) {
			airwayBillIds.put(split[i], split[i + 1]);
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
			taskData.put(ProcessNameTokens.AIRWAYBILLID, parameter);
			taskData.put(ProcessNameTokens.SUBMITTEDBY, userName);

			try {
				bpmAdapter.startBusinessProcess(ProcessNameTokens.LOGISTICSACTIVITYREPORTAPPROVAL, taskData);
			} catch (Exception e) {
				e.printStackTrace();
				return "-1";
			}
		} else if (airwayBillIds.isEmpty()) {
			return "O";
		}

		Locator<LogAirwayBillSessionEJBRemote> locator = null;

		try {
			locator = new Locator<LogAirwayBillSessionEJBRemote>();

			LogAirwayBillSessionEJBRemote airwayBillSessionHome = (LogAirwayBillSessionEJBRemote) locator
			.lookup(LogAirwayBillSessionEJBRemote.class, "LogAirwayBillSessionEJBBean");
			
			LogActivityReconCommentHistorySessionEJBRemote commentSessionHome = (LogActivityReconCommentHistorySessionEJBRemote) locator
			.lookup(LogActivityReconCommentHistorySessionEJBRemote.class, "LogActivityReconCommentHistorySessionEJBBean");
			
			LogActivityReconRecordSessionEJBRemote logActivityReconRecordSessionHome = (LogActivityReconRecordSessionEJBRemote) locator
			.lookup(LogActivityReconRecordSessionEJBRemote.class, "LogActivityReconRecordSessionEJBBean");

			ArrayList<LogAirwayBill> airwayBillList = new ArrayList<LogAirwayBill>();

			LogAirwayBill airwayBill =new LogAirwayBill();
			for (int i = 0; i < airwayBillIds.size(); i++) {
				String airwayBillId = airwayBillIds.get(ProcessNameTokens.AIRWAYBILLID + (i + 1));
				
				List<LogAirwayBill> awbList = airwayBillSessionHome.queryByRange("select o from LogAirwayBill o where o.airwayBillId =" + airwayBillId, 0, 0);

				airwayBill = awbList.get(0);

				LogApprovalStatus approvalStatus = new LogApprovalStatus();
				if (method.equals("approveActivityReportAirwayBill")) {
					approvalStatus.setApprovalStatusId(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_APPROVED);
				} else if (method.equals("rejectActivityReportAirwayBill")) {
					approvalStatus.setApprovalStatusId(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_REJECTED);
				} else if (method.equals("submitForApproval")) {
					approvalStatus.setApprovalStatusId(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_SUBMITTED);
				}

				airwayBill.setLogApprovalStatus2(approvalStatus);
				airwayBill.setActivityApprovedByUserId(userName);

				airwayBillList.add(airwayBill);
			}

			airwayBillSessionHome.mergeLogAirwayBillList(airwayBillList);
			
			/*
			 * Add a comment for each airway bill activity reconciliation record
			 * if there are any. If the reconciliation is ok then 
			 * nothing to do because there are no recon records.
			 */

			for (LogAirwayBill awb : airwayBillList) {
				
				List<LogActivityReconRecord> logActivityReconRecordList = logActivityReconRecordSessionHome.queryByRange("select o from LogActivityReconRecord o where o.logAirwayBill.airwayBillId = " + awb.getAirwayBillId(), 0, 0);
				
				if(logActivityReconRecordList != null && !logActivityReconRecordList.isEmpty()){
					for (LogActivityReconRecord record : logActivityReconRecordList) {
						LogActivityReconCommentHistory logActivityReconCommentHistory = new LogActivityReconCommentHistory();
						LogActivityReconCommentHistoryPK id = new LogActivityReconCommentHistoryPK();
						id.setHistoryTimestamp(new Date());

						id.setActivityReconRecordId(record
								.getActivityReconRecordId());

						/*
						 * Set the comment based on the approval 
						 * status of the AWB
						 */
						
						String comment = "";
						
						if(awb.getLogApprovalStatus2().getApprovalStatusId().equals(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_APPROVED)){
							comment = "Approved";
						}else if(awb.getLogApprovalStatus2().getApprovalStatusId().equals(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_REJECTED)){
							comment = "Rejected";
						}else if(awb.getLogApprovalStatus2().getApprovalStatusId().equals(DataConstantNameTokens.LOGAPPROVALSTATUS_APPROVALSTATUSID_SUBMITTED)){
							comment = "Submitted";
						}
						
						logActivityReconCommentHistory.setComment(comment);
						logActivityReconCommentHistory.setId(id);
						logActivityReconCommentHistory.setLogActivityReconRecord(record);
						logActivityReconCommentHistory.setUserLogonName(this.userName);

						commentSessionHome.persistLogActivityReconCommentHistory(logActivityReconCommentHistory);

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
