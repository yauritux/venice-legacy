package com.gdn.venice.server.app.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.presenter.command.FetchUserPermissionListCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class AuthorizationUtilServlet
 */
public class AuthorizationUtilServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthorizationUtilServlet() {
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
		
		if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");
			
			if (method.equals("fetchUserPermissionList")) {
				RafRpcCommand fetchUserPermissionListCommand = new FetchUserPermissionListCommand(userName);
				retVal = fetchUserPermissionListCommand.execute();
			}
		}
		response.getOutputStream().println(retVal);
	}
}
