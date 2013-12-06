package com.gdn.venice.server.app.logistics.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.logistics.presenter.commands.ActivityReportAirwayBillProcessCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchActivityReportAirwayBillDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchActivityReportAirwayBillReconciliationProblemDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchActivityReportCommentHistoryDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchApprovalStatusComboBoxDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchFileUploadLogDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchLogisticProviderComboBoxDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchOrderStatusComboBoxDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.GetTotalLogAirwayBillCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.UpdateActivityReportAirwayBillReconciliationProblemDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class ActivityReconciliationPresenterServlet
 */
public class ActivityReportReconciliationPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActivityReportReconciliationPresenterServlet() {
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
			if (method.equals("fetchAirwayBillData")) {
				if (request.getParameter(DataNameTokens.TASKID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.TASKID, request.getParameter(DataNameTokens.TASKID));
					rafDsRequest.setParams(params);
				}
				
				rafDsRequest.setStartRow(Integer.parseInt(request.getParameter("startRow")));
				rafDsRequest.setEndRow(rafDsRequest.getStartRow()+Integer.parseInt(request.getParameter("totalRow")));
				
				RafDsCommand fetchAirwayBillDataCommand = new FetchActivityReportAirwayBillDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchAirwayBillDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchAirwayBillReconciliationProblemData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, request.getParameter(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID));
				rafDsRequest.setParams(params);
				
				RafDsCommand fetchAirwayBillReconciliationProblemDataCommand = new FetchActivityReportAirwayBillReconciliationProblemDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchAirwayBillReconciliationProblemDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("updateAirwayBillReconciliationProblemData")) {				
				RafDsCommand updateAirwayBillReconciliationProblemDataCommand = new UpdateActivityReportAirwayBillReconciliationProblemDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = updateAirwayBillReconciliationProblemDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchActivityCommentHistory")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID, request.getParameter(DataNameTokens.LOGACTIVITYRECONRECORD_ACTIVITYRECONRECORDID));
				rafDsRequest.setParams(params);
				
				RafDsCommand fetchActivityReportCommentHistoryDataCommand = new FetchActivityReportCommentHistoryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchActivityReportCommentHistoryDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFileUploadLogData")) {
				
				RafDsCommand fetchFileUploadLogDataCommand = new FetchFileUploadLogDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFileUploadLogDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} else if (type.equals(RafRpcCommand.RPC)) {
			String requestBody = Util.extractRequestBody(request);
			
			String method = request.getParameter("method");
			if(method.equals("fetchLogisticProviderComboBoxData")){	
				RafRpcCommand fetchLogisticProviderComboBoxDataCommand = new FetchLogisticProviderComboBoxDataCommand();
				retVal = fetchLogisticProviderComboBoxDataCommand.execute();				
			}else if(method.equals("fetchOrderStatusComboBoxData")){
				RafRpcCommand fetchOrderStatusComboBoxDataCommand = new FetchOrderStatusComboBoxDataCommand();
				retVal = fetchOrderStatusComboBoxDataCommand.execute();		
			}else if(method.equals("fetchApprovalStatusComboBoxData")){
				RafRpcCommand fetchApprovalStatusComboBoxDataCommand = new FetchApprovalStatusComboBoxDataCommand();
				retVal = fetchApprovalStatusComboBoxDataCommand.execute();		
			}else if(method.equals("getTotalLogAirwaybillData")){
				RafRpcCommand getTotalLogAirwayBillCommand = new GetTotalLogAirwayBillCommand();
				retVal = getTotalLogAirwayBillCommand.execute();
			}else{
				RafRpcCommand activityReportAirwayBillProcessCommand = new ActivityReportAirwayBillProcessCommand(requestBody, userName, method, request);
				retVal = activityReportAirwayBillProcessCommand.execute();
			}
		}

		response.getOutputStream().println(retVal);
	}

}
