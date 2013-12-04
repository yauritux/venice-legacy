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

public class ApPaymentProcessCommand implements RafRpcCommand {

	HashMap<String, String> apPaymentIds;
	String parameter;
	String userName;
	String method;
	HttpServletRequest request;

	public ApPaymentProcessCommand(String parameter, String userName,
			String method, HttpServletRequest request) {
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

	@Override
	public String execute() {
		if (method.equals("submitForApproval")) {
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName,
					BPMAdapter.getUserPasswordFromLDAP(userName));

			bpmAdapter.synchronize();

			HashMap<String, String> taskData = new HashMap<String, String>();
			taskData.put(ProcessNameTokens.APPAYMENTID, parameter);

			try {
				bpmAdapter
						.startBusinessProcess(
								ProcessNameTokens.FINANCEFUNDINRECONCILIATIONRESULTAPPROVAL,
								taskData);
			} catch (Exception e) {
				String errorText = e.getMessage();
				e.printStackTrace();
				return "-1" + ":" + errorText;
			}
		} else if (apPaymentIds.isEmpty()) {
			return "O";
		}

		ArrayList<FinApPayment> finApPaymentList = new ArrayList<FinApPayment>();
		ArrayList<Long> approvedApPaymentList = new ArrayList<Long>();

		Locator<Object> locator = null;
		FinApPaymentSessionEJBRemote sessionHome;
		try {
			locator = new Locator<Object>();

			sessionHome = (FinApPaymentSessionEJBRemote) locator.lookup(
					FinApPaymentSessionEJBRemote.class,
					"FinApPaymentSessionEJBBean");

			for (int i = 0; i < apPaymentIds.size(); i++) {
				String apPaymentId = apPaymentIds
						.get(ProcessNameTokens.APPAYMENTID + (i + 1));
				FinApPayment finApPayment;
				try {
					finApPayment = sessionHome.queryByRange(
							"select o from FinApPayment o where o.apPaymentId="
									+ apPaymentId, 0, 1).get(0);
				} catch (IndexOutOfBoundsException e) {
					finApPayment = new FinApPayment();
					finApPayment.setApPaymentId(new Long(apPaymentId));
				}

				FinApprovalStatus approvalStatus = new FinApprovalStatus();
				if (method.equals("approveApPayment")) {
					approvalStatus
							.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_APPROVED);
					approvedApPaymentList.add(new Long(apPaymentId));
				} else if (method.equals("rejectApPayment")) {
					approvalStatus
							.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_REJECTED);
				} else if (method.equals("submitForApproval")) {
					approvalStatus
							.setApprovalStatusId(DataConstantNameTokens.FINAPPROVALSTATUS_APPROVALSTATUSID_SUBMITTED);
				}

				finApPayment.setFinApprovalStatus(approvalStatus);

				finApPaymentList.add(finApPayment);
			}
			sessionHome.mergeFinApPaymentList(finApPaymentList);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				locator.close();
			} catch (Exception e) {
				String errorText = e.getMessage();
				e.printStackTrace();
				return "-1" + ":" + errorText;
			}
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "O";
	}
}
