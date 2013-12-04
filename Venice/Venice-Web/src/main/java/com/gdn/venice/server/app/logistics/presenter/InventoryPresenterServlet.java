package com.gdn.venice.server.app.logistics.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.logistics.presenter.commands.FetchInventoryDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.RemoveInventoryDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class InventoryPresenterServlet
 */
public class InventoryPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InventoryPresenterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			if (method.equals("fetchInventoryData")) {
				RafDsCommand fetchInventoryDataCommand = new FetchInventoryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchInventoryDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("removeInventoryData")){
				RafDsCommand removeInventoryDataCommand = new RemoveInventoryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = removeInventoryDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} 
		
		response.getOutputStream().println(retVal);
	}

}
