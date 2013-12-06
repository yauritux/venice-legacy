package com.gdn.venice.server.app.fraud.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.fraud.presenter.commands.AddBlackListDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.DeleteBlackListDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchBlackListDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateBlackListDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class BlackListMaintenancePresenterServlet
 * 
 * @author Roland
 */
public class BlackListMaintenancePresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BlackListMaintenancePresenterServlet() {
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
			if (method.equals("fetchBlackListData")) {				
				RafDsCommand fetchBlackListDataCommand = new FetchBlackListDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchBlackListDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("addBlackListData")){
				String username = Util.getUserName(request);
				RafDsCommand addBlackListDataCommand = new AddBlackListDataCommand(rafDsRequest, username);
				RafDsResponse rafDsResponse = addBlackListDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("updateBlackListData")){
				String username = Util.getUserName(request);			
				RafDsCommand updateBlackListDataCommand = new UpdateBlackListDataCommand(rafDsRequest, username);
				RafDsResponse rafDsResponse = updateBlackListDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("deleteBlackListData")){										
				RafDsCommand deleteBlackListDataCommand = new DeleteBlackListDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteBlackListDataCommand.execute();
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
