package com.gdn.venice.server.app.logistics.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchDeliveryStatusTrackingDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchDeliveryStatusTrackingDetailDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchLogisticProviderComboBoxDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchLogisticServiceComboBoxDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchMerchantPickUpInstructionDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchOrderStatusComboBoxDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.UpdateDeliveryStatusTrackingDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class DeliveryStatusTrackingPresenterServlet
 */
public class DeliveryStatusTrackingPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeliveryStatusTrackingPresenterServlet() {
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
			if (method.equals("fetchDeliveryStatusTrackingData")) {
				if (request.getParameter(DataNameTokens.TASKID)!=null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put(DataNameTokens.TASKID, request.getParameter(DataNameTokens.TASKID));
					rafDsRequest.setParams(params);
				}
				
				RafDsCommand fetchDeliveryStatusTrackingDataCommand = new FetchDeliveryStatusTrackingDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchDeliveryStatusTrackingDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("fetchMerchantPickUpInstructionData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, request.getParameter(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID));
				rafDsRequest.setParams(params);
				
				RafDsCommand fetchMerchantPickUpInstructionDataCommand = new FetchMerchantPickUpInstructionDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchMerchantPickUpInstructionDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("updateDeliveryStatusTrackingData")) {				
				RafDsCommand updateDeliveryStatusTrackingDataCommand = new UpdateDeliveryStatusTrackingDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateDeliveryStatusTrackingDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchDeliveryStatusTrackingDetailData")) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID, request.getParameter(DataNameTokens.LOGAIRWAYBILL_AIRWAYBILLID));
				rafDsRequest.setParams(params);
				
				RafDsCommand fetchDeliveryStatusTrackingDetailDataCommand = new FetchDeliveryStatusTrackingDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchDeliveryStatusTrackingDetailDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
		} else if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");			
			if(method.equals("fetchServiceComboBoxData")){
				RafRpcCommand fetchLogisticServiceComboBoxDataCommand = new FetchLogisticServiceComboBoxDataCommand();
				retVal = fetchLogisticServiceComboBoxDataCommand.execute();		
			}else if(method.equals("fetchOrderStatusComboBoxData")){
				RafRpcCommand fetchOrderStatusComboBoxDataCommand = new FetchOrderStatusComboBoxDataCommand();
				retVal = fetchOrderStatusComboBoxDataCommand.execute();		
			}else if(method.equals("fetchLogisticProviderComboBoxData")){	
				RafRpcCommand fetchLogisticProviderComboBoxDataCommand = new FetchLogisticProviderComboBoxDataCommand();
				retVal = fetchLogisticProviderComboBoxDataCommand.execute();				
			}
		}
		response.getOutputStream().println(retVal);
	}
}
