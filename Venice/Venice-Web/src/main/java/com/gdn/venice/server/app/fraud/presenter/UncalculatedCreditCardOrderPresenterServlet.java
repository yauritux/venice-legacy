package com.gdn.venice.server.app.fraud.presenter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.fraud.presenter.commands.FetchUncalculatedCreditCardOrderDataCommand;
import com.gdn.venice.server.app.fraud.presenter.commands.UpdateUncalculatedCreditCardOrderDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class UncalculatedCreditCardOrderPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public UncalculatedCreditCardOrderPresenterServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type") == null ? "" : request.getParameter("type");
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
			if (method.equals("fetchUncalculatedCreditCardOrderData")) {
				RafDsCommand rafDsCommand = new FetchUncalculatedCreditCardOrderDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = rafDsCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("updateUncalculatedCreditCardOrderData")) {
				UpdateUncalculatedCreditCardOrderDataCommand rafDsCommand = new UpdateUncalculatedCreditCardOrderDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = rafDsCommand.execute();
				
				if (rafDsResponse.getStatus() != -4) {
					try {
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					retVal = "<response><status>-4</status><errors><VenOrderPaymentAllocation.venOrderPayment.paymentConfirmationNumber><errorMessage>Unable to edit auth. code that is already matched with MIGS report.\nPlease Refresh!</errorMessage></VenOrderPaymentAllocation.venOrderPayment.paymentConfirmationNumber></errors></response>";
				}
			}
		}

		response.getOutputStream().println(retVal);
	}
}