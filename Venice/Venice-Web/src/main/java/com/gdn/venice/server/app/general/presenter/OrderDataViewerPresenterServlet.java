package com.gdn.venice.server.app.general.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.server.app.general.presenter.commands.ApproveCSPaymentDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.ApproveVAPaymentDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.CheckUserRoleCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderCustomerAddressDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderCustomerContactDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderCustomerDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderDetailDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderFinancePaymentDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderFinanceReconciliationDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderHistoryOrderDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderHistoryOrderItemDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderItemDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderItemHistoryDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderLogisticsAirwayBillDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderLogisticsCartDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.GetTotalOrderDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.PendingCSPaymentDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.RejectCSPaymentDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.UpdateOrderBlockDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.UpdateOrderStatusDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchOrderStatusComboBoxDataCommand;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class OrderDataViewerPresenterServlet
 */
public class OrderDataViewerPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderDataViewerPresenterServlet() {
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
			
			if (request.getParameter(DataNameTokens.VENORDER_ORDERID)!=null) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				rafDsRequest.setParams(params);
			} else if (request.getParameter(DataNameTokens.VENORDERITEM_ORDERITEMID)!=null) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDERITEM_ORDERITEMID, request.getParameter(DataNameTokens.VENORDERITEM_ORDERITEMID));
				rafDsRequest.setParams(params);
			} else if (request.getParameter(DataNameTokens.TASKID)!=null) {
				String taskId = request.getParameter(DataNameTokens.TASKID);
				String orderId = fetchOrderIdBasedOnTaskId(Util.getUserName(request), taskId);
				//orderId maybe null here when taskId is no longer there?
				if (orderId!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.VENORDER_ORDERID, orderId);
					rafDsRequest.setParams(params);
				} else {
					//if orderId is null, return empty Response
					RafDsResponse rafDsResponse = new RafDsResponse();
					rafDsResponse.setStatus(0);
					rafDsResponse.setStartRow(0);
					rafDsResponse.setTotalRows(0);
					rafDsResponse.setEndRow(0);
					try {
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					} catch (Exception e) {
						e.printStackTrace();
					}
					response.getOutputStream().println(retVal);
					return;
				}
			}
			
			String method = request.getParameter("method");
			if (method.equals("fetchOrderData")) {
				rafDsRequest.setStartRow(Integer.parseInt(request.getParameter("firstResult")));
				RafDsCommand fetchOrderDataCommand = new FetchOrderDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchOrderDetailData")) {
				RafDsCommand fetchOrderDetailDataCommand = new FetchOrderDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderDetailDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchOrderItemData")) {
				RafDsCommand fetchOrderItemDataCommand = new FetchOrderItemDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderItemDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchOrderCustomerData")) {
				RafDsCommand fetchOrderCustomerDataCommand = new FetchOrderCustomerDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderCustomerDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchOrderCustomerAddressData")) {
				RafDsCommand fetchOrderCustomerAddressDataCommand = new FetchOrderCustomerAddressDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderCustomerAddressDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchOrderCustomerContactData")) {
				RafDsCommand fetchOrderCustomerContactDataCommand = new FetchOrderCustomerContactDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderCustomerContactDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchOrderLogisticsCartData")) {
				RafDsCommand fetchOrderLogisticsCartDataCommand = new FetchOrderLogisticsCartDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderLogisticsCartDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchOrderLogisticsAirwayBillData")) {
				if (request.getParameter(DataNameTokens.VENDISTRIBUTIONCART_DISTRIBUTIONCARTID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.VENDISTRIBUTIONCART_DISTRIBUTIONCARTID, request.getParameter(DataNameTokens.VENDISTRIBUTIONCART_DISTRIBUTIONCARTID));
					rafDsRequest.setParams(params);
				}
				
				RafDsCommand fetchOrderLogisticsAirwayBillDataCommand = new FetchOrderLogisticsAirwayBillDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderLogisticsAirwayBillDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchOrderFinancePaymentData")) {
				RafDsCommand fetchOrderFinancePaymentDataCommand = new FetchOrderFinancePaymentDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderFinancePaymentDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchOrderFinanceReconciliationData")) {
				if (request.getParameter(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID, request.getParameter(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID));
					rafDsRequest.setParams(params);
				}
				
				RafDsCommand fetchOrderFinanceReconciliationDataCommand = new FetchOrderFinanceReconciliationDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderFinanceReconciliationDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchOrderHistoryOrderData")) {
				RafDsCommand fetchOrderHistoryOrderDataCommand = new FetchOrderHistoryOrderDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderHistoryOrderDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchOrderHistoryOrderItemData")) {
				RafDsCommand fetchOrderHistoryOrderItemDataCommand = new FetchOrderHistoryOrderItemDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderHistoryOrderItemDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}  else if (method.equals("fetchOrderItemHistoryData")) {
				RafDsCommand fetchOrderItemHistoryDataCommand = new FetchOrderItemHistoryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchOrderItemHistoryDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
		} else if (type.equals(RafRpcCommand.RPC)) {
			String requestBody = Util.extractRequestBody(request);
			
			String method = request.getParameter("method");
			
			if (method.equals("approveVAPayment")) {
				RafRpcCommand approveVAPaymentDataCommand = new ApproveVAPaymentDataCommand(requestBody);
				retVal = approveVAPaymentDataCommand.execute();
			} else if (method.equals("approveCSPayment")) {
				RafRpcCommand approveCSPaymentDataCommand = new ApproveCSPaymentDataCommand(requestBody);
				retVal = approveCSPaymentDataCommand.execute();
			} else if (method.equals("rejectCSPayment")) {
				RafRpcCommand rejectCSPaymentDataCommand = new RejectCSPaymentDataCommand(requestBody);
				retVal = rejectCSPaymentDataCommand.execute();
			} else if (method.equals("pendingCSPayment")) {
				RafRpcCommand pendingCSPaymentDataCommand = new PendingCSPaymentDataCommand(requestBody);
				retVal = pendingCSPaymentDataCommand.execute();
			} else if (method.equals("updateOrderStatusToSF") || method.equals("updateOrderStatusToFP") ||  method.equals("updateOrderStatusToFC")) {
				String username = Util.getUserName(request);
				RafRpcCommand updateOrderStatusDataCommand = new UpdateOrderStatusDataCommand(requestBody, method, username);
				retVal = updateOrderStatusDataCommand.execute();
			} else if (method.equals("updateBlockOrder")) {
				RafRpcCommand updateOrderBlockDataCommand = new UpdateOrderBlockDataCommand(requestBody);
				retVal = updateOrderBlockDataCommand.execute();
			} else if(method.equals("checkUserRole")){
				String roleNeeded = request.getParameter("roleNeeded");
				RafRpcCommand checkUserRoleCommand = new CheckUserRoleCommand(requestBody, userName, roleNeeded);
				retVal = checkUserRoleCommand.execute();
			} else if(method.equals("fetchOrderStatusComboBoxData")){
				RafRpcCommand fetchOrderStatusComboBoxDataCommand = new FetchOrderStatusComboBoxDataCommand();
				retVal = fetchOrderStatusComboBoxDataCommand.execute();		
			} else if(method.equals("getTotalOrderData")){
				RafRpcCommand getTotalOrderData = new GetTotalOrderDataCommand();
				retVal = getTotalOrderData.execute();
			}			
		}
		
		response.getOutputStream().println(retVal);
	}
	
	private String fetchOrderIdBasedOnTaskId(String userName, String taskId) {
		BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));
		
		bpmAdapter.synchronize();
		return bpmAdapter.getExternalDataVariableAsString(new Long(taskId), ProcessNameTokens.ORDERID);
	}
}
