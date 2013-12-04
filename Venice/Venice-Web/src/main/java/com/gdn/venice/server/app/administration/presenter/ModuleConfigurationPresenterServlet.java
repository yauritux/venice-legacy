package com.gdn.venice.server.app.administration.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.administration.presenter.commands.AddModuleConfigurationDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.DeleteModuleConfigurationDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchModuleConfigurationDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchModuleTypeComboBoxDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.FetchParentModuleComboBoxDataCommand;
import com.gdn.venice.server.app.administration.presenter.commands.UpdateModuleConfigurationDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class ModuleConfigurationPresenterServlet
 * 
 * @author Roland
 */
public class ModuleConfigurationPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ModuleConfigurationPresenterServlet() {
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
			if (method.equals("fetchModuleConfigurationData")) {				
				RafDsCommand fetchModuleConfigurationDataCommand = new FetchModuleConfigurationDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchModuleConfigurationDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("addModuleConfigurationData")){
				RafDsCommand addModuleConfigurationDataCommand = new AddModuleConfigurationDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addModuleConfigurationDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("updateModuleConfigurationData")){	
				RafDsCommand updateModuleConfigurationDataCommand = new UpdateModuleConfigurationDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateModuleConfigurationDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(method.equals("deleteModuleConfigurationData")){										
				RafDsCommand deleteModuleConfigurationDataCommand = new DeleteModuleConfigurationDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deleteModuleConfigurationDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} 
		else if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");			
			if(method.equals("fetchModuleTypeComboBoxData")){	
				RafRpcCommand fetchModuleTypeComboBoxDataCommand = new FetchModuleTypeComboBoxDataCommand();
				retVal = fetchModuleTypeComboBoxDataCommand.execute();				
			}	else if(method.equals("fetchParentModuleComboBoxData")){	
				RafRpcCommand fetchParentModuleComboBoxDataCommand = new FetchParentModuleComboBoxDataCommand();
				retVal = fetchParentModuleComboBoxDataCommand.execute();				
			} 
		}
		response.getOutputStream().println(retVal);
	}
}
