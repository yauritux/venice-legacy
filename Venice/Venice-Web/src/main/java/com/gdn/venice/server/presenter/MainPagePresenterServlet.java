package com.gdn.venice.server.presenter;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class MainPagePresenterServlet
 */
public class MainPagePresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private Properties versionProperties;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPagePresenterServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		
		if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");
			
			if (method.equals("signOut")) {
				request.getSession().invalidate();
				response.sendRedirect("/Venice");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String retVal =  "";
		
		if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");
			
			if (method.equals("getSignedInUser")) {
				if (request.getUserPrincipal()!=null) {
					//TODO: Change this in Geronimo Servlet, hardcoded here for development purpose in GWT
					String userName = Util.getUserName(request);
					String roleName = "operations";
					retVal = userName + ":" + roleName;
				}
			} else if (method.equals("getVersion")) {
				if (versionProperties==null) {
					versionProperties = Util.getVersionProperties();
				}
				if (versionProperties!=null) {
					retVal =  versionProperties.getProperty("venice.major.version") + "." + versionProperties.getProperty("venice.minor.version"); 
				}
			}  
		}
		response.getOutputStream().println(retVal);
	}
}
