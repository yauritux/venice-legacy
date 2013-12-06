package com.gdn.venice.server.app.finance.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.finance.presenter.commands.CancelAllocationFundInActionCommand;
import com.gdn.venice.server.app.finance.presenter.commands.CreateJournalForVAProcessCommand;
import com.gdn.venice.server.app.finance.presenter.commands.DeleteFundInReconciliationDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.DeleteFundsInActionCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchActionTakenHistoryDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchAllocateDataDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchFundInAllocateToOrderDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchFundInAllocateToOrderPaymentDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchFundInReconRecordCommentHistoryDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchFundInReconciliationDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchPaymentReportComboBoxData;
import com.gdn.venice.server.app.finance.presenter.commands.FetchPaymentTypeComboBoxCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchRefundDataDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchSalesRecordDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FundInReconciliationActionCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FundInReconciliationProcessCommand;
import com.gdn.venice.server.app.finance.presenter.commands.RefundFundInActionCommand;
import com.gdn.venice.server.app.finance.presenter.commands.UpdateFundInReconciliationDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchBankComboBoxCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class FundInReconciliationPresenterServlet
 */
public class FundInReconciliationPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FundInReconciliationPresenterServlet() {
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
			if (method.equals("fetchFundInReconciliationData")) {
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

				RafDsCommand fetchFundInReconciliationDataCommand = new FetchFundInReconciliationDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchFundInReconciliationDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("updateFundInReconciliationData")) {
				RafDsCommand updateFundInReconciliationDataCommand = new UpdateFundInReconciliationDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = updateFundInReconciliationDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("deleteFundInReconciliationData")){		
				RafDsCommand deleteFundInReconciliationDataCommand = new DeleteFundInReconciliationDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteFundInReconciliationDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFundInReconRecordCommentHistory")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, request.getParameter(DataNameTokens.FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
				rafDsRequest.setParams(params);

				RafDsCommand fetchFundInReconRecordCommentHistoryCommand = new FetchFundInReconRecordCommentHistoryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFundInReconRecordCommentHistoryCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			} else if (method.equals("fetchFundInAllocateToOrderData")) {
				RafDsCommand fetchFundInAllocateToOrderDataCommand = new FetchFundInAllocateToOrderDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchFundInAllocateToOrderDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchFundInAllocateToOrderPaymentData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDERPAYMENT_VENORDERPAYMENTALLOCATION_VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDERPAYMENT_VENORDERPAYMENTALLOCATION_VENORDER_ORDERID));
				params.put("AllocationAmount", request.getParameter("AllocationAmount"));
				rafDsRequest.setParams(params);				
				
				RafDsCommand fetchFundInAllocateToOrderPaymentDataCommand = new FetchFundInAllocateToOrderPaymentDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchFundInAllocateToOrderPaymentDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchSalesRecordData")) {
				RafDsCommand fetchSalesRecordDataCommand = new FetchSalesRecordDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchSalesRecordDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchAllocateData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD_SOURCE, request.getParameter(DataNameTokens.FINARFUNDSINALLOCATEPAYMENT_IDRECONRECORD_SOURCE));
				rafDsRequest.setParams(params);				
				
				RafDsCommand fetchAllocateDataDataCommand = new FetchAllocateDataDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchAllocateDataDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchRefundData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, request.getParameter(DataNameTokens.FINARFUNDSINREFUND_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
				rafDsRequest.setParams(params);				
				
				RafDsCommand fetchRefundDataDataCommand = new FetchRefundDataDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchRefundDataDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchActionTakenHistoryData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID, request.getParameter(DataNameTokens.FINARFUNDSINACTIONAPPLIEDHISTORY_FINARFUNDSINRECONRECORD_RECONCILIATIONRECORDID));
				rafDsRequest.setParams(params);				
				
				RafDsCommand fetchActionTakenHistoryDataCommand = new FetchActionTakenHistoryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchActionTakenHistoryDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 							
		}  else if (type.equals(RafRpcCommand.RPC)) {
			String requestBody = Util.extractRequestBody(request);
			String method = request.getParameter("method");
			
			if (method.equals("allocateFundIn")) {
				RafRpcCommand fundInReconciliationActionCommand = new FundInReconciliationActionCommand(requestBody, userName, method, request);
				retVal = fundInReconciliationActionCommand.execute();
			} else if (method.equals("refund")) {
				RafRpcCommand refundFundInActionCommand = new RefundFundInActionCommand(requestBody);
				retVal = refundFundInActionCommand.execute();
			} else if(method.equals("delete")){
				RafRpcCommand deleteFundsInActionCommand = new DeleteFundsInActionCommand(requestBody);
				retVal = deleteFundsInActionCommand.execute();				
			} else if(method.equals("fetchPaymentTypeComboBoxData")){										
				RafRpcCommand paymentTypeComboBox = new FetchPaymentTypeComboBoxCommand();
				retVal = paymentTypeComboBox.execute();				
			} else if(method.equals("fetchBankComboBoxData")){										
				RafRpcCommand bankComboBox = new FetchBankComboBoxCommand();
				retVal = bankComboBox.execute();	
			}else if(method.equals("fetchPaymentReportComboBoxData")){										
				RafRpcCommand fetchPaymentReportComboBoxData = new FetchPaymentReportComboBoxData();
				retVal = fetchPaymentReportComboBoxData.execute();
			}else if (method.equals("cancelAllocation")) {
				RafRpcCommand cancelAllocationFundInActionCommand = new CancelAllocationFundInActionCommand(requestBody);
				retVal = cancelAllocationFundInActionCommand.execute();
			}else if(method.equals("createJournalForVA")){
				RafRpcCommand createJournalForVAProcessCommand = new CreateJournalForVAProcessCommand(requestBody);
				retVal = createJournalForVAProcessCommand.execute();
			}else {
				RafRpcCommand fundInReconciliationProcessCommand = new FundInReconciliationProcessCommand(requestBody, userName, method, request);
				retVal = fundInReconciliationProcessCommand.execute();
			}
		}
		
		response.getOutputStream().println(retVal);
	}

}
