package com.gdn.venice.server.app.finance.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.finance.presenter.commands.FetchFussionChartRevenueHistoryCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchFussionChartRevenueStatusCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchPayablesDueDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchPeriodFinanceDashboardCommandComboBox;
import com.gdn.venice.server.app.finance.presenter.commands.FetchUnreconciledReceivablesCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FinanceDashboardPresenterServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	/**
	 *Constructor for Exports Presenter Servlet
	 * @see HttpServlet#HttpServlet()
	 */
	public FinanceDashboardPresenterServlet() {
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
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
			if (method.equals("fetchPayablesDueData")) {				
				RafDsCommand fetchPayablesDueData = new FetchPayablesDueDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchPayablesDueData.execute();

				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}			
			}else if (method.equals("fetchUnreconciledReceivable")) {				
					RafDsCommand fetchUnreconciledReceivables = new FetchUnreconciledReceivablesCommand(rafDsRequest);
					RafDsResponse rafDsResponse = fetchUnreconciledReceivables.execute();
					try {
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					} catch (Exception e) {
						e.printStackTrace();
					}			
				}
		} else if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");			
			 if(method.equals("fetchPeriodFinanceDashboardCommandComboBox")){										
				RafRpcCommand fetchPeriodFinanceDashboardCommandComboBox = new FetchPeriodFinanceDashboardCommandComboBox();
				retVal = fetchPeriodFinanceDashboardCommandComboBox.execute();				
			}else if(method.equals("fetchFussionChartRevenueStatus")){										
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand fetchFussionChartRevenueStatus = new FetchFussionChartRevenueStatusCommand(requestBody);
				retVal = fetchFussionChartRevenueStatus.execute();				
			}else if(method.equals("fetchFussionChartRevenueHistory")){										
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand fetchFussionChartRevenueHistory = new FetchFussionChartRevenueHistoryCommand(requestBody);
				retVal = fetchFussionChartRevenueHistory.execute();
			}
		}
		response.getOutputStream().println(retVal);
	}
}