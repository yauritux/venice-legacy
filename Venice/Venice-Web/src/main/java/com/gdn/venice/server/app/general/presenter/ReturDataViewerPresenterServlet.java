package com.gdn.venice.server.app.general.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.server.app.general.presenter.commands.FetchOrderLogisticsCartDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchReturCustomerAddressDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchReturCustomerContactDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchReturCustomerDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchReturDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchReturDetailDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchReturHistoryReturDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchReturHistoryReturItemDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchReturItemDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchReturLogisticsAirwayBillDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.GetTotalOrderDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchOrderStatusComboBoxDataCommand;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class ReturDataViewerPresenterServlet
 */
public class ReturDataViewerPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReturDataViewerPresenterServlet() {
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
		if (type.equals(RafDsCommand.DataSource)) {
			String requestBody = Util.extractRequestBody(request);
	
			RafDsRequest rafDsRequest = null;
			try {
				rafDsRequest = RafDsRequest.convertXmltoRafDsRequest(requestBody);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if (request.getParameter(DataNameTokens.VENRETUR_RETURID)!=null) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENRETUR_RETURID, request.getParameter(DataNameTokens.VENRETUR_RETURID));
				rafDsRequest.setParams(params);
			} else if (request.getParameter(DataNameTokens.VENORDER_ORDERID)!=null) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENORDER_ORDERID, request.getParameter(DataNameTokens.VENORDER_ORDERID));
				rafDsRequest.setParams(params);
			} else if (request.getParameter(DataNameTokens.VENRETURITEM_RETURITEMID)!=null) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENRETURITEM_RETURITEMID, request.getParameter(DataNameTokens.VENRETURITEM_RETURITEMID));
				rafDsRequest.setParams(params);
			} else if (request.getParameter(DataNameTokens.TASKID)!=null) {
				String taskId = request.getParameter(DataNameTokens.TASKID);
				String orderId = fetchOrderIdBasedOnTaskId(Util.getUserName(request), taskId);
				//orderId maybe null here when taskId is no longer there?
				if (orderId!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.VENRETUR_RETURID, orderId);
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
			if (method.equals("fetchReturData")) {
				rafDsRequest.setStartRow(Integer.parseInt(request.getParameter("firstResult")));
				RafDsCommand fetchReturDataCommand = new FetchReturDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchReturDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchReturDetailData")) {
				RafDsCommand fetchReturDetailDataCommand = new FetchReturDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchReturDetailDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchReturItemData")) {
				RafDsCommand fetchReturItemDataCommand = new FetchReturItemDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchReturItemDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchReturCustomerData")) {
				RafDsCommand fetchReturCustomerDataCommand = new FetchReturCustomerDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchReturCustomerDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchReturCustomerAddressData")) {
				RafDsCommand fetchReturCustomerAddressDataCommand = new FetchReturCustomerAddressDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchReturCustomerAddressDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchReturCustomerContactData")) {
				RafDsCommand fetchReturCustomerContactDataCommand = new FetchReturCustomerContactDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchReturCustomerContactDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchReturLogisticsCartData")) {
				RafDsCommand fetchReturLogisticsCartDataCommand = new FetchOrderLogisticsCartDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchReturLogisticsCartDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchReturLogisticsAirwayBillData")) {				
				RafDsCommand fetchReturLogisticsAirwayBillDataCommand = new FetchReturLogisticsAirwayBillDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchReturLogisticsAirwayBillDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchReturHistoryReturData")) {
				RafDsCommand fetchReturHistoryReturDataCommand = new FetchReturHistoryReturDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchReturHistoryReturDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchReturHistoryReturItemData")) {
				RafDsCommand fetchReturHistoryReturItemDataCommand = new FetchReturHistoryReturItemDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchReturHistoryReturItemDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (type.equals(RafRpcCommand.RPC)) {
			
			String method = request.getParameter("method");
			
			if(method.equals("fetchOrderStatusComboBoxData")){
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
