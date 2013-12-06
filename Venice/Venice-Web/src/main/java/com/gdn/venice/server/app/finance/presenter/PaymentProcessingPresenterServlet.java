package com.gdn.venice.server.app.finance.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.finance.presenter.commands.FetchBankAccountComboBoxDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchJournalRelatedLogisticsInvoiceDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchLogisticsPaymentDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchLogisticsPaymentProcessingDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchManualJournalTransactionPaymentData;
import com.gdn.venice.server.app.finance.presenter.commands.FetchMerchantPaymentDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchMerchantPaymentProcessingDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchPaymentDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchRefundPaymentDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchRefundPaymentProcessingDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FinishPaymentCommand;
import com.gdn.venice.server.app.finance.presenter.commands.MakePaymentCommand;
import com.gdn.venice.server.app.finance.presenter.commands.PaymentsProcessCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Presenter servlet for payment processing
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PaymentProcessingPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PaymentProcessingPresenterServlet() {
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
			if (method.equals("fetchMerchantPaymentProcessingData")) {
				RafDsCommand fetchMerchantPaymentProcessingDataCommand = new FetchMerchantPaymentProcessingDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchMerchantPaymentProcessingDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchMerchantPaymentData")) {
				if (request.getParameter(DataNameTokens.FINSALESRECORD_SALESRECORDID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.FINSALESRECORD_SALESRECORDID, request.getParameter(DataNameTokens.FINSALESRECORD_SALESRECORDID));
					rafDsRequest.setParams(params);
				}
				
				RafDsCommand fetchMerchantPaymentDataCommand = new FetchMerchantPaymentDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchMerchantPaymentDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchLogisticsPaymentProcessingData")) {
				RafDsCommand fetchLogisticsPaymentProcessingData = new FetchLogisticsPaymentProcessingDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchLogisticsPaymentProcessingData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchJournalRelatedLogisticsInvoiceData")) {
				if (request.getParameter(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID, request.getParameter(DataNameTokens.FINJOURNALAPPROVALGROUP_JOURNALGROUPID));
					rafDsRequest.setParams(params);
				}
				
				RafDsCommand fetchInvoiceData = new FetchJournalRelatedLogisticsInvoiceDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchInvoiceData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchLogisticsPaymentData")) {
				if (request.getParameter(DataNameTokens.FINAPINVOICE_APINVOICEID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.FINAPINVOICE_APINVOICEID, request.getParameter(DataNameTokens.FINAPINVOICE_APINVOICEID));
					rafDsRequest.setParams(params);
				}
				
				RafDsCommand fetchLogisticsPaymentDataCommand = new FetchLogisticsPaymentDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchLogisticsPaymentDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}  else if (method.equals("fetchRefundPaymentProcessingData")) {
				RafDsCommand fetchRefundPaymentProcessingDataCommand = new FetchRefundPaymentProcessingDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchRefundPaymentProcessingDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchRefundPaymentData")) {
				if (request.getParameter(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID, request.getParameter(DataNameTokens.FINARFUNDSINREFUND_REFUNDRECORDID));
					rafDsRequest.setParams(params);
				}
				
				RafDsCommand fetchRefundPaymentDataCommand = new FetchRefundPaymentDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchRefundPaymentDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			} else if (method.equals("fetchManualJournalTransactionPaymentData")) {
				if (request.getParameter(DataNameTokens.FINAPPAYMENT_FINSALESRECORDS)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.FINAPPAYMENT_FINSALESRECORDS, request.getParameter(DataNameTokens.FINAPPAYMENT_FINSALESRECORDS));
					rafDsRequest.setParams(params);
				} else	if (request.getParameter(DataNameTokens.FINAPPAYMENT_FINAPINVOICES)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.FINAPPAYMENT_FINAPINVOICES, request.getParameter(DataNameTokens.FINAPPAYMENT_FINAPINVOICES));
					rafDsRequest.setParams(params);
				}

				RafDsCommand fetchManualJournalTransactionMerchantPaymentData = new FetchManualJournalTransactionPaymentData(rafDsRequest);
				RafDsResponse rafDsResponse = fetchManualJournalTransactionMerchantPaymentData.execute();
				
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			} if (method.equals("fetchPaymentData")) {
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

				RafDsCommand fetchPaymentDataCommand = new FetchPaymentDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchPaymentDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
		} else if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");			
			String requestBody = Util.extractRequestBody(request);
			if(method.equals("fetchBankAccountComboBoxData")){			
				/*
				 * Get the combo box data for bank
				 */
				RafRpcCommand fetchBankAccountComboBoxDataCommand = new FetchBankAccountComboBoxDataCommand();
				retVal = fetchBankAccountComboBoxDataCommand.execute();				
			} else if (method.equals("makePayment")) {
				/*
				 * Call the make payment command
				 */
				RafRpcCommand makePaymentCommand = new MakePaymentCommand(requestBody);
				retVal = makePaymentCommand.execute();
			} else if(method.equals("finishPaymentProcessing")) {
				/*
				 * Call the finish payment command
				 */
				RafRpcCommand finishPaymentCommand = new FinishPaymentCommand(requestBody);
				retVal = finishPaymentCommand.execute();
			} else {
				/*
				 * Assume that any other RPC request method will be for business process
				 * (submitApPaymentForApproval, approveApPayment or rejectApPayment)
				 */
				RafRpcCommand submitPaymentForApprovalCommand = new PaymentsProcessCommand(requestBody, userName, method, request);
				retVal = submitPaymentForApprovalCommand.execute();
			}
		}

		response.getOutputStream().println(retVal);
	}
}
