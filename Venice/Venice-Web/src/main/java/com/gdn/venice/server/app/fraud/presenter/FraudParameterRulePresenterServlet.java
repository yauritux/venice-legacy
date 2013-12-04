package com.gdn.venice.server.app.fraud.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.fraud.presenter.commands.AddFraudParameterRule31DataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.DeleteFraudParameterRule31DataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchFraudParameterRule31DataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateFraudParameterRule31DataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class FraudParameterRulePresenterServlet
 * 
 * @author Roland
 */
public class FraudParameterRulePresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FraudParameterRulePresenterServlet() {
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
			if (method.equals("fetchFraudParameterRule31Data")) {				
				RafDsCommand fetchFraudParameterRule31DataCommand = new FetchFraudParameterRule31DataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchFraudParameterRule31DataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("addFraudParameterRule31Data")){
				RafDsCommand addFraudParameterRule31DataCommand = new AddFraudParameterRule31DataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addFraudParameterRule31DataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("updateFraudParameterRule31Data")){			
				RafDsCommand updateFraudParameterRule31DataCommand = new UpdateFraudParameterRule31DataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateFraudParameterRule31DataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("deleteFraudParameterRule31Data")){			
				RafDsCommand removeFraudParameterRule31DataCommand = new DeleteFraudParameterRule31DataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = removeFraudParameterRule31DataCommand.execute();
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
