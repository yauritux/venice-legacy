package com.gdn.venice.server.app.finance.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.finance.presenter.commands.FetchSalesRecordDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchSalesRecordDetailAdjustmentDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.RefundSalesRecordActionCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class SalesRecordPresenterServlet
 */
public class SalesRecordPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SalesRecordPresenterServlet() {
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
			if (method.equals("fetchSalesRecordData")) {
				if (request.getParameter(DataNameTokens.TASKID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.TASKID, request.getParameter(DataNameTokens.TASKID));
					rafDsRequest.setParams(params);
				}
				if (request.getParameter(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, request.getParameter(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
					rafDsRequest.setParams(params);
				}
				
				RafDsCommand fetchSalesRecordDataCommand = new FetchSalesRecordDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchSalesRecordDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchSalesRecordDetailAdjustmentData")) {
				if (request.getParameter(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID) != null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID, request.getParameter(DataNameTokens.FINSALESRECORD_VENORDERITEM_WCSORDERITEMID));
					rafDsRequest.setParams(params);
				}
				RafDsCommand fetchSalesRecordDetailAdjustmentDataCommand = new FetchSalesRecordDetailAdjustmentDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchSalesRecordDetailAdjustmentDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		} else if(type.equals(RafRpcCommand.RPC)){
			String requestBody = Util.extractRequestBody(request);
			String method = request.getParameter("method");
			
			if(method.equals("refund")){
				RafRpcCommand refundSalesRecordActionCommand = new RefundSalesRecordActionCommand(requestBody);
				retVal = refundSalesRecordActionCommand.execute();
			}
			
		}

		response.getOutputStream().println(retVal);
	}
}
