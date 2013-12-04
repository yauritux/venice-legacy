package com.gdn.venice.server.app.finance.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.finance.presenter.commands.DeleteManualJournalApprovalGroupCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchJournalDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchJournalDetailDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchManualJournalAccountComboBoxDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchManualJournalDetailDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchManualJournalOrderDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchManualJournalPartyDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.JournalProcessCommand;
import com.gdn.venice.server.app.finance.presenter.commands.SaveManualJournalDetailCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class JournalPresenterServlet
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class JournalPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JournalPresenterServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String retVal =  "";
		
		String userName = Util.getUserName(request);
		
		if (type.equals(RafDsCommand.DataSource)) {
			String requestBody = Util.extractRequestBody(request);
	
			RafDsRequest rafDsRequest = null;
			try {
				rafDsRequest = RafDsRequest.convertXmltoRafDsRequest(requestBody);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String method = request.getParameter("method");
			if (method.equals("fetchJournalData")) {
				if (request.getParameter(DataNameTokens.TASKID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.TASKID, request.getParameter(DataNameTokens.TASKID));
					rafDsRequest.setParams(params);
				}

				RafDsCommand fetchJournalDataCommand = new FetchJournalDataCommand(rafDsRequest, userName, false);
				RafDsResponse rafDsResponse = fetchJournalDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchJournalDetailData")) {
				if (request.getParameter(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, request.getParameter(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
					rafDsRequest.setParams(params);
				}

				RafDsCommand fetchJournalDetailDataCommand = new FetchJournalDetailDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchJournalDetailDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchManualJournalData")) {
				if (request.getParameter(DataNameTokens.TASKID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.TASKID, request.getParameter(DataNameTokens.TASKID));
					rafDsRequest.setParams(params);
				}

				RafDsCommand fetchJournalDataCommand = new FetchJournalDataCommand(rafDsRequest, userName, true);
				RafDsResponse rafDsResponse = fetchJournalDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchManualJournalDetailData")) {
				if (request.getParameter(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, request.getParameter(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
					rafDsRequest.setParams(params);
				}

				RafDsCommand fetchManualJournalDetailDataCommand = new FetchManualJournalDetailDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchManualJournalDetailDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchManualJournalOrderData")) {
				RafDsCommand fetchManualJournalOrderDataCommand = new FetchManualJournalOrderDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchManualJournalOrderDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchManualJournalPartyData")) {
				RafDsCommand fetchManualJournalPartyDataCommand = new FetchManualJournalPartyDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchManualJournalPartyDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} else if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");			
			if(method.equals("fetchManualJournalAccountComboBoxData")){										
				RafRpcCommand fetchManualJournalAccountComboBoxDataCommand = new FetchManualJournalAccountComboBoxDataCommand();
				retVal = fetchManualJournalAccountComboBoxDataCommand.execute();				
			} else if (method.equals("saveManualJournalDetail")) {
				String requestBody = Util.extractRequestBody(request);
				
				RafRpcCommand saveManualJournalDetailCommand = new SaveManualJournalDetailCommand(requestBody);
				retVal = saveManualJournalDetailCommand.execute();
			} else if (method.equals("deleteManualJournalDetail")) {
				String requestBody = Util.extractRequestBody(request);
				
				RafRpcCommand deleteManualJournalApprovalGroupCommand = new DeleteManualJournalApprovalGroupCommand(requestBody);
				retVal = deleteManualJournalApprovalGroupCommand.execute();
			}else {
				String requestBody = Util.extractRequestBody(request);

				/*
				 * Assume that any other RPC request method will be for business process
				 * (submitJournalForApproval, approveJournal or rejectJournal)
				 */
				RafRpcCommand submitJournalForApprovalCommand = new JournalProcessCommand(requestBody, userName, method, request);
				retVal = submitJournalForApprovalCommand.execute();
			}
		}
		

		response.getOutputStream().println(retVal);
	}

}
