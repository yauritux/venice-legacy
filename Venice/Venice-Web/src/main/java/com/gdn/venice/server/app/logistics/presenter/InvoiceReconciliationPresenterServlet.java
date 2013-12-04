package com.gdn.venice.server.app.logistics.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchApprovalStatusComboBoxDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchInvoiceAirwayBillDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchInvoiceAirwayBillReconciliationProblemDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchInvoiceAirwaybillRecordDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchInvoiceCommentHistoryDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchInvoiceReportUploadDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchInvoiceUploadLogDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchLogisticProviderComboBoxDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchOrderStatusComboBoxDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.GetInvoiceReportUploadIdToApproveRejectCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.GetInvoiceReportUploadIdToSubmitCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.InvoiceAirwayBillProcessCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.UpdateInvoiceAirwayBillReconciliationProblemDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.UpdateInvoiceAirwaybillRecordDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class InvoiceReconciliationPresenterServlet
 */
public class InvoiceReconciliationPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InvoiceReconciliationPresenterServlet() {
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
		
		String userName = Util.getUserName(request);;
		
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
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID, request.getParameter("invoiceAirwaybillRecordId"));
				rafDsRequest.setParams(params);

				RafDsCommand fetchAirwayBillDataCommand = new FetchInvoiceAirwayBillDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchAirwayBillDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchAirwayBillReconciliationProblemData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID, request.getParameter(DataNameTokens.LOGINVOICEAIRWAYBILLRECORD_INVOICEAIRWAYBILLRECORDID));
				rafDsRequest.setParams(params);
				
				RafDsCommand fetchAirwayBillReconciliationProblemDataCommand = new FetchInvoiceAirwayBillReconciliationProblemDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchAirwayBillReconciliationProblemDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("updateAirwayBillReconciliationProblemData")) {
				RafDsCommand updateAirwayBillReconciliationProblemDataCommand = new UpdateInvoiceAirwayBillReconciliationProblemDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = updateAirwayBillReconciliationProblemDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}  else if (method.equals("fetchInvoiceCommentHistory")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID, request.getParameter(DataNameTokens.LOGINVOICERECONRECORD_INVOICERECONRECORDID));
				rafDsRequest.setParams(params);
				
				RafDsCommand fetchInvoiceReportCommentHistoryDataCommand = new FetchInvoiceCommentHistoryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchInvoiceReportCommentHistoryDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchInvoiceUploadLogData")) {				
				RafDsCommand fetchInvoiceUploadLogDataCommand = new FetchInvoiceUploadLogDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchInvoiceUploadLogDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchInvoiceReportUploadData")) {				
				if(!request.getParameter("taskId").isEmpty()){
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.TASKID, request.getParameter("taskId"));
					rafDsRequest.setParams(params);
				}
				RafDsCommand fetchInvoiceReportUploadDataCommand = new FetchInvoiceReportUploadDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchInvoiceReportUploadDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchInvoiceAirwaybillRecordData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.LOGINVOICEREPORTUPLOAD_INVOICEREPORTUPLOADID, request.getParameter("reportUploadId"));
				rafDsRequest.setParams(params);
				RafDsCommand fetchInvoiceAirwaybillRecordDataCommand = new FetchInvoiceAirwaybillRecordDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchInvoiceAirwaybillRecordDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("updateInvoiceAirwaybillRecordData")) {
				RafDsCommand updateInvoiceAirwaybillRecordDataCommand = new UpdateInvoiceAirwaybillRecordDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = updateInvoiceAirwaybillRecordDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (type.equals(RafRpcCommand.RPC)) { //this is invoice submit for approval, approve, or reject
			String requestBody = Util.extractRequestBody(request);
			
			String method = request.getParameter("method");
			if(method.equals("fetchLogisticProviderComboBoxData")){	
				RafRpcCommand fetchLogisticProviderComboBoxDataCommand = new FetchLogisticProviderComboBoxDataCommand();
				retVal = fetchLogisticProviderComboBoxDataCommand.execute();				
			} else if (method.equals("fetchOrderStatusComboBoxData")){
				RafRpcCommand fetchOrderStatusComboBoxDataCommand = new FetchOrderStatusComboBoxDataCommand();
				retVal = fetchOrderStatusComboBoxDataCommand.execute();		
			} else if (method.equals("fetchApprovalStatusComboBoxData")){
				RafRpcCommand fetchApprovalStatusComboBoxDataCommand = new FetchApprovalStatusComboBoxDataCommand();
				retVal = fetchApprovalStatusComboBoxDataCommand.execute();		
			} else if (method.equals("getInvoiceReportUploadIdToSubmit")){
				RafRpcCommand getInvoiceReportUploadIdToSubmitCommand = new GetInvoiceReportUploadIdToSubmitCommand(request.getParameter("invoiceNumbers"));
				retVal = getInvoiceReportUploadIdToSubmitCommand.execute();		
			} else if (method.equals("getInvoiceReportUploadIdToApprove")){
				RafRpcCommand getInvoiceReportUploadIdToApproveRejectCommand = new GetInvoiceReportUploadIdToApproveRejectCommand(request.getParameter("invoiceNumbers"), "approve");
				retVal = getInvoiceReportUploadIdToApproveRejectCommand.execute();		
			} else if (method.equals("getInvoiceReportUploadIdToReject")){
				RafRpcCommand getInvoiceReportUploadIdToApproveRejectCommand = new GetInvoiceReportUploadIdToApproveRejectCommand(request.getParameter("invoiceNumbers"), "reject");
				retVal = getInvoiceReportUploadIdToApproveRejectCommand.execute();		
			} else {
				RafRpcCommand invoiceAirwayBillProcessCommand = new InvoiceAirwayBillProcessCommand(requestBody, userName, method, request);
				retVal = invoiceAirwayBillProcessCommand.execute();
			}
		}
		response.getOutputStream().println(retVal);
	}
}
