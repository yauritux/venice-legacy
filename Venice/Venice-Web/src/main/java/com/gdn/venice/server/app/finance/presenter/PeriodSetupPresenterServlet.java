package com.gdn.venice.server.app.finance.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.finance.presenter.commands.AddPeriodDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.DeletePeriodeDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchPeriodDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.UpdatePeriodeDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class PeriodSetupPresenterServlet
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class PeriodSetupPresenterServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PeriodSetupPresenterServlet() {
        super();
       
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
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
		
			if (method.equals("fetchPeriodData")) {				
				RafDsCommand fetchPeriodDataCommand = new FetchPeriodDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchPeriodDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("addPeriodData")){
				RafDsCommand addPeriodDataCommand = new AddPeriodDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addPeriodDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else {
						String errorMessage = "Please check again of Date data is selected to be Added";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("updatePeriodData")){		
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.FINPERIOD_PERIODID, request.getParameter(DataNameTokens.FINPERIOD_PERIODID));
				rafDsRequest.setParams(params);	
				RafDsCommand updatePeriodeDataCommand = new UpdatePeriodeDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updatePeriodeDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}else {
						String errorMessage = "Please check again of Date data is selected to be Updated";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("deletePeriodData")){										
				RafDsCommand deletePeriodDataCommand = new DeletePeriodeDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deletePeriodDataCommand.execute();
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
