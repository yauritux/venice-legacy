package com.gdn.venice.server.app.finance.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.server.app.finance.presenter.commands.AddPromotionDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.DeletePromotionShareCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchPromotionDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchPromotionPartyDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.FetchPromotionShareCalcMethodComboBoxData;
import com.gdn.venice.server.app.finance.presenter.commands.FetchPromotionShareDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.RemovePromotionDataCommand;
import com.gdn.venice.server.app.finance.presenter.commands.SavePromotionShareCommand;
import com.gdn.venice.server.app.finance.presenter.commands.UpdatePromotionDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class PromotionPresenterServlet
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class PromotionPresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 *Constructor for Promotion Presenter Servlet
	 * @see HttpServlet#HttpServlet()
	 */
	public PromotionPresenterServlet() {
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
				rafDsRequest = RafDsRequest
						.convertXmltoRafDsRequest(requestBody);
				/*
				 * Got sick of trying to pass this in the DsRequest so just grabbed it from the HTTP params - DF
				 */
				if(request.getParameter("VenPartyPromotionShare.venPromotion.promotionId") != null){
					HashMap<String, String> params = rafDsRequest.getParams();
					if(params == null){
						params = new HashMap<String,String>();
					}
					params.put("VenPartyPromotionShare.venPromotion.promotionId", request.getParameter("VenPartyPromotionShare.venPromotion.promotionId"));
					rafDsRequest.setParams(params);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			String method = request.getParameter("method");
			if (method.equals("fetchPromotionData")) {
				RafDsCommand fetchPromotionDataCommand = new FetchPromotionDataCommand(
						rafDsRequest);
				RafDsResponse rafDsResponse = fetchPromotionDataCommand
						.execute();
				try {
					retVal = RafDsResponse
							.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (method.equals("addPromotionData")) {
				RafDsCommand addPromotionDataCommand = new AddPromotionDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addPromotionDataCommand.execute();
				try {
					retVal = RafDsResponse
							.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("updatePromotionData")) {
				RafDsCommand updatePromotionDataCommand = new UpdatePromotionDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updatePromotionDataCommand.execute();
				try {
					retVal = RafDsResponse
							.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if (method.equals("removePromotionData")){
				RafDsCommand removePromotionDataCommand = new RemovePromotionDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = removePromotionDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchPromotionShareData")){
				RafDsCommand fetchPromotionShareDataCommand = new FetchPromotionShareDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchPromotionShareDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}							
			}else if (method.equals("fetchPromotionPartyData")){
				RafDsCommand fetchPromotionPartyDataCommand = new FetchPromotionPartyDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchPromotionPartyDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchPromotionShareCalcMethodComboBoxData")){
				RafDsCommand fetchPromotionShareCalcMethodComboBoxDataCommand = new FetchPromotionShareCalcMethodComboBoxData(rafDsRequest);
				RafDsResponse rafDsResponse = fetchPromotionShareCalcMethodComboBoxDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchPromotionShareTypeComboBoxData")){
				RafDsCommand fetchPromotionShareCalcMethodComboBoxDataCommand = new FetchPromotionShareCalcMethodComboBoxData(rafDsRequest);
				RafDsResponse rafDsResponse = fetchPromotionShareCalcMethodComboBoxDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}else if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");			
			if (method.equals("savePromotionShare")) {
				String requestBody = Util.extractRequestBody(request);			
				RafRpcCommand savePromotionShareCommand = new SavePromotionShareCommand(requestBody);
				retVal = savePromotionShareCommand.execute();
			} else if (method.equals("deletePromotionShare")) {
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand deletePromotionShareCommand = new DeletePromotionShareCommand(requestBody);
				retVal = deletePromotionShareCommand.execute();
			}
		}
		response.getOutputStream().println(retVal);
	}
}
