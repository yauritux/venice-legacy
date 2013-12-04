package com.gdn.venice.server.app.task.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.task.presenter.commands.ClaimUnclaimCompleteToDoListTaskCommand;
import com.gdn.venice.server.app.task.presenter.commands.FetchToDoListDataCommand;
import com.gdn.venice.server.app.task.presenter.commands.GetUserRoleCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class ToDoListPresenterServlet
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class ToDoListPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ToDoListPresenterServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 * 
	 * Note that this servlet acts as a proxy to the command that calls the BPM API
	 * for handling the various actions on the todo list and fetching its data
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String retVal =  "";
		
		String userName = Util.getUserName(request);;
		
		if (type.equals(RafDsCommand.DataSource)) {
			String requestBody = Util.extractRequestBody(request);
	
			RafDsRequest rafDsRequest = null;
			try {
				rafDsRequest = RafDsRequest.convertXmltoRafDsRequest(requestBody);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			String method = request.getParameter("method");
			
			if (method.equals("fetchToDoListData")) {
				RafDsCommand fetchToDoListDataCommand = new FetchToDoListDataCommand(rafDsRequest, userName);
				RafDsResponse rafDsResponse = fetchToDoListDataCommand.execute();
				try {
					if(rafDsResponse!=null){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
	
		} else if (type.equals(RafRpcCommand.RPC)) {
			String requestBody = Util.extractRequestBody(request);
			
			String method = request.getParameter("method");
			
			if (method.equals("claimToDoListTask")) {
				RafRpcCommand claimToDoListTaskCommand = new ClaimUnclaimCompleteToDoListTaskCommand(requestBody, "claim", userName);
				retVal = claimToDoListTaskCommand.execute();
			} else if (method.equals("unclaimToDoListTask")) {
				RafRpcCommand unclaimToDoListTaskCommand = new ClaimUnclaimCompleteToDoListTaskCommand(requestBody, "unclaim", userName);
				retVal = unclaimToDoListTaskCommand.execute();
			} else if (method.equals("completeToDoListTask")) {
				RafRpcCommand unclaimToDoListTaskCommand = new ClaimUnclaimCompleteToDoListTaskCommand(requestBody, "complete",userName);
				retVal = unclaimToDoListTaskCommand.execute();
			} else if(method.equals("getUserRole")){
				RafRpcCommand getUserRoleCommand = new GetUserRoleCommand(requestBody, userName);
				retVal = getUserRoleCommand.execute();
			}
		}
		response.getOutputStream().println(retVal);
	}

}
