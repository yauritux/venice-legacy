package com.gdn.venice.server.app.fraud.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.fraud.presenter.commands.AddBinCreditLimitDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchBankComboBoxCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchBinCreditLimitDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.FetchCardComboBoxCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.RemoveBinCreditLimitDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateBinCreditLimitDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class FraudBinCreditLimitPresenterServlet
 * 
 * @author Roland
 */
public class FraudBinCreditLimitPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FraudBinCreditLimitPresenterServlet() {
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
			if (method.equals("fetchBinCreditLimitData")) {				
				RafDsCommand fetchBinCreditLimitDataCommand = new FetchBinCreditLimitDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchBinCreditLimitDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("addBinCreditLimitData")){
				RafDsCommand addBinCreditLimitDataCommand = new AddBinCreditLimitDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addBinCreditLimitDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("updateBinCreditLimitData")){			
				RafDsCommand updateBinCreditLimitDataCommand = new UpdateBinCreditLimitDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateBinCreditLimitDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("removeBinCreditLimitData")){			
				RafDsCommand removeBinCreditLimitData = new RemoveBinCreditLimitDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = removeBinCreditLimitData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
		} 
		else if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");			
			if(method.equals("fetchBankComboBoxData")){	
				RafRpcCommand bankComboBox = new FetchBankComboBoxCommand();
				retVal = bankComboBox.execute();				
			}	else if(method.equals("fetchCardComboBoxData")){	
				RafRpcCommand cardComboBox = new FetchCardComboBoxCommand();
				retVal = cardComboBox.execute();				
			}	
		}
		response.getOutputStream().println(retVal);
	}
}
