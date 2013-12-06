package com.gdn.venice.server.app.task.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.task.presenter.commands.RetrieveTaskSummaryDataCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class TaskSummaryPresenterServlet
 */
public class TaskSummaryPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TaskSummaryPresenterServlet() {
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
			
			if (method.equals("retrieveTaskSummaryData")) {
				RafRpcCommand retrieveTaskSummaryDataCommand = new RetrieveTaskSummaryDataCommand(userName);
				retVal = "0:"+retrieveTaskSummaryDataCommand.execute();
			} 
		}
		response.getOutputStream().println(retVal);
	}
}
