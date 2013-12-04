package com.gdn.venice.server.app.finance.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.finance.presenter.commands.AddAccountDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchAccountCategoryComboBoxDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchAccountDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchAccountTypeComboBoxDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.RemoveAccountDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.UpdateAccountDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;


/**
 * Servlet implementation class CoaSetupPresenterServlet
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class CoaSetupPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 *Constructor for COA Setup Presenter Servlet
	 * @see HttpServlet#HttpServlet()
	 */
	public CoaSetupPresenterServlet() {
		super();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String retVal = "";

		if (type.equals(RafDsCommand.DataSource)) {
			String requestBody = Util.extractRequestBody(request);

			RafDsRequest rafDsRequest = null;
			try {
				rafDsRequest = RafDsRequest
						.convertXmltoRafDsRequest(requestBody);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String method = request.getParameter("method");
			if (method.equals("fetchAccountData")) {
				RafDsCommand fetchAccountDataCommand = new FetchAccountDataCommand(
						rafDsRequest);
				RafDsResponse rafDsResponse = fetchAccountDataCommand
						.execute();
				try {
					retVal = RafDsResponse
							.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("addAccountData")) {
				RafDsCommand addAccountDataCommand = new AddAccountDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addAccountDataCommand.execute();
				try {
					retVal = RafDsResponse
							.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("updateAccountData")) {
				RafDsCommand updateAccountDataCommand = new UpdateAccountDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateAccountDataCommand.execute();
				try {
					retVal = RafDsResponse
							.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (method.equals("removeAccountData")){
				RafDsCommand removeAccountDataCommand = new RemoveAccountDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = removeAccountDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchAccountTypeComboBoxData")){
				RafDsCommand fetchAccountTypeComboBoxDataCommand = new FetchAccountTypeComboBoxDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchAccountTypeComboBoxDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}							
			}else if (method.equals("fetchAccountCategoryComboBoxData")){
				RafDsCommand fetchAccountCategoryComboBoxDataCommand = new FetchAccountCategoryComboBoxDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchAccountCategoryComboBoxDataCommand.execute();
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