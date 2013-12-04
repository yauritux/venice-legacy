package com.gdn.venice.server.app.finance.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.finance.presenter.commands.FetchExportDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchRolledUpJournalEntryDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class ExportPresenterServlet
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ExportPresenterServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	/**
	 *Constructor for Exports Presenter Servlet
	 * @see HttpServlet#HttpServlet()
	 */
	public ExportPresenterServlet() {
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
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String retVal = "";

		if (type.equals(RafDsCommand.DataSource)) {
			String requestBody = Util.extractRequestBody(request);

			RafDsRequest rafDsRequest = null;
			try {
				rafDsRequest = RafDsRequest.convertXmltoRafDsRequest(requestBody);
				
				if(request.getParameter(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID) != null){
					HashMap<String, String> params = rafDsRequest.getParams();
					if(params == null){
						params = new HashMap<String,String>();
					}
					params.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID, request.getParameter(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID));
					rafDsRequest.setParams(params);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			String method = request.getParameter("method");
			if (method.equals("fetchExportData")) {
				RafDsCommand fetchExportDataCommand = new FetchExportDataCommand(
						rafDsRequest);
				RafDsResponse rafDsResponse = fetchExportDataCommand
						.execute();
				try {
					retVal = RafDsResponse
							.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			else if (method.equals("fetchAccountLinesData")){
				RafDsCommand fetchRolledUpJournalEntryDataCommand = new FetchRolledUpJournalEntryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchRolledUpJournalEntryDataCommand.execute();
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