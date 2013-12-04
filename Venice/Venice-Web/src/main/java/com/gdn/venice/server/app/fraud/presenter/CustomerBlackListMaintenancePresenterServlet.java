package com.gdn.venice.server.app.fraud.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.fraud.presenter.commands.AddCustomerBlackListDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.AddCustomerWhiteListDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.DeleteCustomerBlackListDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.DeleteCustomerWhiteListDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchCustomerBlackListDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchCustomerWhiteListDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateCustomerBlackListDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateCustomerWhiteListDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class CustomerBlackListMaintenancePresenterServlet
 * 
 * @author Roland
 */
public class CustomerBlackListMaintenancePresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerBlackListMaintenancePresenterServlet() {
        super();
       
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
			
			
			String method = request.getParameter("method");
			if (method.equals("fetchCustomerBlackListData")) {				
				RafDsCommand fetchCustomerBlackListDataCommand = new FetchCustomerBlackListDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchCustomerBlackListDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("addCustomerBlackListData")){
				String username = Util.getUserName(request);
				RafDsCommand addCustomerBlackListDataCommand = new AddCustomerBlackListDataCommand(rafDsRequest, username);
				RafDsResponse rafDsResponse = addCustomerBlackListDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("updateCustomerBlackListData")){
				String username = Util.getUserName(request);		
				RafDsCommand updateCustomerBlackListDataCommand = new UpdateCustomerBlackListDataCommand(rafDsRequest, username);
				RafDsResponse rafDsResponse = updateCustomerBlackListDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("deleteCustomerBlackListData")){										
				RafDsCommand deleteCustomerBlackListDataCommand = new DeleteCustomerBlackListDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteCustomerBlackListDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("fetchCustomerWhiteListData")){										
				RafDsCommand fetchCustomerWhiteListDataCommand = new FetchCustomerWhiteListDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchCustomerWhiteListDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("addCustomerWhiteListData")){								
				String username = Util.getUserName(request);		
				RafDsCommand addCustomerWhiteListDataCommand = new AddCustomerWhiteListDataCommand(rafDsRequest, username);
				RafDsResponse rafDsResponse = addCustomerWhiteListDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("updateCustomerWhiteListData")){
				String username = Util.getUserName(request);		

				RafDsCommand updateCustomerWhiteListDataCommand = new UpdateCustomerWhiteListDataCommand(rafDsRequest, username);
				RafDsResponse rafDsResponse = updateCustomerWhiteListDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("deleteCustomerWhiteListData")){
				RafDsCommand deleteCustomerWhiteListDataCommand = new DeleteCustomerWhiteListDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteCustomerWhiteListDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}							
		} else if (type.equals(RafRpcCommand.RPC)) {
		}
		response.getOutputStream().println(retVal);
	}
}
