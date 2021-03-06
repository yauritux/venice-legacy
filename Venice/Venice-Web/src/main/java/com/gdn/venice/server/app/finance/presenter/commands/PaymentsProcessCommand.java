package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.FinApPaymentSessionEJBRemote;
import com.gdn.venice.persistence.FinApPayment;
import com.gdn.venice.persistence.FinApprovalStatus;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * Command for submitting payments for approval to BPM
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PaymentsProcessCommand implements RafRpcCommand {
	
	/*
	 * A map of the payment ids to process - passed into command
	 */
	HashMap<String, String> apPaymentIds;
	
	/*
	 * Parameters to be passed as task data to BPM
	 */
	String parameter;
	
	/*
	 * The user name under which the BPM process is called
	 */
	String userName;
	
	/*
	 * The method of the command (submit, approve, reject)
	 */
	String method;
	HttpServletRequest request;
	
	/**
	 * A copy constructor that passes all the needed data for the BPM call.
	 * 
	 * @param parameter is a parameter to pass to the task as data
	 * @param userName is the user name that is used top call BPM
	 * @param method the method of the call (submit, approve, reject)
	 * @param request is the servlet request of the presenter servlet
	 */
	public PaymentsProcessCommand(String parameter,
			String userName, String method, HttpServletRequest request) {
		Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
		String[] split = p.split(parameter);

		apPaymentIds = new HashMap<String, String>();
		for (int i = 1; i < split.length; i += 2) {
			apPaymentIds.put(split[i], split[i + 1]);
		}
		this.parameter = parameter;
		this.userName = userName;
		this.method = method;
		this.request = request;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		/*
		 * If the method is submit then instantiate the BPM adapter
		 * and start the business process instance for payment approval
		 */
		if (method.equals("submitApPaymentForApproval")) {
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName,
					BPMAdapter.getUserPasswordFromLDAP(userName));

			bpmAdapter.synchronize();

			HashMap<String, String> taskData = new HashMap<String, String>();
			taskData.put(ProcessNameTokens.APPAYMENTID, parameter);

			try {
				bpmAdapter
						.startBusinessProcess(
								ProcessNameTokens.FINANCEAPPAYMENTRESULTAPPROVAL,
								taskData);
			} catch (Exception e) {
				String errorMsg = Util.extractMessageFromEJBExceptionText(e.getMessage());
				e.printStackTrace();
				return "-1" + ":" + errorMsg;
			}
		} else if (apPaymentIds.isEmpty()) {
			return "O";
		}

		/*
		 * A list of payment objects to be updated later with status
		 */
		ArrayList<FinApPayment> finApPaymentList = new ArrayList<FinApPayment>();
		
		/*
		 * Process each of the payment ids in the list to update its status
		 * (submit, approve or reject)
		 */
		
		Locator<Object> locator = null;

		try {
			locator = new Locator<Object>();

			FinApPaymentSessionEJBRemote sessionHome = (FinApPaymentSessionEJBRemote) locator
					.lookup(FinApPaymentSessionEJBRemote.class,
							"FinApPaymentSessionEJBBean");

			for (int i = 0; i < apPaymentIds.size(); i++) {
				String apPaymentId = apPaymentIds
						.get(ProcessNameTokens.APPAYMENTID + (i + 1));

				FinApPayment finApPayment;
				try
				{
					finApPayment = sessionHome.queryByRange("select o from FinApPayment o where o.apPaymentId="+apPaymentId, 0, 1).get(0);
				}catch(IndexOutOfBoundsException e){
					finApPayment = new FinApPayment();
					finApPayment.setApPaymentId(new Long(apPaymentId));
				}

				FinApprovalStatus approvalStatus = new FinApprovalStatus();
				if (method.equals("approveApPayment")) {
					approvalStatus
							.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_APPROVED);
				} else if (method.equals("rejectApPayment")) {
					approvalStatus
							.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_REJECTED);
				} else if (method.equals("submitApPaymentForApproval")) {
					approvalStatus
							.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_SUBMITTED);
				}else{
					approvalStatus
					.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_NEW);
				}

				finApPayment.setFinApprovalStatus(approvalStatus);

				finApPaymentList.add(finApPayment);
			}
			
			/*
			 * Merge to change the status of the payments in the database
			 */
			sessionHome.mergeFinApPaymentList(finApPaymentList);
		} catch (Exception ex) {
			String errorMsg = Util.extractMessageFromEJBExceptionText(ex.getMessage());
			ex.printStackTrace();
			return "-1" + ":" + errorMsg;
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
