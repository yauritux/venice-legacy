package com.gdn.venice.server.app.logistics.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.logistics.presenter.commands.FetchOrderProcessingHistoryCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class LogisticsDashboardPresenterServlet
 */
public class LogisticsDashboardPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LogisticsDashboardPresenterServlet() {
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
	
			@SuppressWarnings("unused")
			RafDsRequest rafDsRequest = null;
			try {
				rafDsRequest = RafDsRequest.convertXmltoRafDsRequest(requestBody);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			@SuppressWarnings("unused")
			String method = request.getParameter("method");			
		} else if (type.equals(RafRpcCommand.RPC)) {
			String requestBody = Util.extractRequestBody(request);			
			String method = request.getParameter("method");	
			if(method.equalsIgnoreCase("fetchOrderProcessingHistory")){			
				RafRpcCommand fetchOrderProcessingHistory = new FetchOrderProcessingHistoryCommand(requestBody);
				retVal = fetchOrderProcessingHistory.execute();
			} 
		}
		response.getOutputStream().println(retVal);
	}
}
