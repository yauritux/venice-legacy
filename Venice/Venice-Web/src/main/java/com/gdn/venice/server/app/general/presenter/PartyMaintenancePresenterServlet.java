package com.gdn.venice.server.app.general.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.general.presenter.commands.AddPartyContactDetailDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.AddPartyMaintenanceDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchAddressDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchContactDetailTypeDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchPartyContactDetailDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchPartyMaintenanceDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.FetchPartyTypeDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.RemovePartyContactDetailDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.RemovePartyMaintenanceDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.UpdatePartyContactDetailDataCommand;
import com.gdn.venice.server.app.general.presenter.commands.UpdatePartyMaintenanceDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.RemoveAddressDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.SaveAddressDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class PartyMaintenancePresenterServlet
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class PartyMaintenancePresenterServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;
	
	/**
	 * Basic constructor
	 */
	public PartyMaintenancePresenterServlet(){
		super();
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
	
	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String type = request.getParameter("type");
		String retVal =  "";
		
		if (type.equals(RafDsCommand.DataSource)){
			String requestBody = Util.extractRequestBody(request);
			
			RafDsRequest rafDsRequest = null;
			try{
				rafDsRequest = RafDsRequest.convertXmltoRafDsRequest(requestBody);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(request.getParameter(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID) != null){
				HashMap<String, String> params = rafDsRequest.getParams();
				if(params == null){
					params = new HashMap<String,String>();
				}
				params.put(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID, request.getParameter(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID));
				rafDsRequest.setParams(params);
			}else if(request.getParameter(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID) != null){
				HashMap<String, String> params = rafDsRequest.getParams();
				if(params == null){
					params = new HashMap<String,String>();
				}
				params.put(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID, request.getParameter(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID));
				rafDsRequest.setParams(params);
			}
			
			String method = request.getParameter("method");
			
			if(method.equals("fetchPartyMaintenanceData")){
				RafDsCommand fetchPartyMaintenanceDataCommand = new FetchPartyMaintenanceDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchPartyMaintenanceDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("addPartyMaintenanceData")){
				RafDsCommand addPartyMaintenanceDataCommand = new AddPartyMaintenanceDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addPartyMaintenanceDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("updatePartyMaintenanceData")){
				RafDsCommand updatePartyMaintenanceDataCommand = new UpdatePartyMaintenanceDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updatePartyMaintenanceDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("removePartyMaintenanceData")){
				RafDsCommand removePartyMaintenanceDataCommand = new RemovePartyMaintenanceDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = removePartyMaintenanceDataCommand.execute();
				try{
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}else {
						String errorMessage = "Unable to Remove because the Party ID still referenced from another table";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchPartyTypeData")){
				RafDsCommand fetchPartyTypeDataCommand = new FetchPartyTypeDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchPartyTypeDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchPartyContactDetailData")){
				RafDsCommand fetchPartyContactDetailDataCommand = new FetchPartyContactDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchPartyContactDetailDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("addPartyContactDetailData")){
				RafDsCommand addPartyContactDetailDataCommand = new AddPartyContactDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addPartyContactDetailDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("updatePartyContactDetailData")){
				RafDsCommand updatePartyContactDetailDataCommand = new UpdatePartyContactDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updatePartyContactDetailDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("removePartyContactDetailData")){
				RafDsCommand removePartyContactDetailDataCommand = new RemovePartyContactDetailDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = removePartyContactDetailDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchContactDetailTypeData")){
				RafDsCommand fetchContactDetailTypeDataCommand = new FetchContactDetailTypeDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchContactDetailTypeDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchAddressData")){
				RafDsCommand fetchAddressDataCommand = new FetchAddressDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchAddressDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}else if (type.equals(RafRpcCommand.RPC)){
			String method = request.getParameter("method");
			if (method.equals("saveAddressData")) {
				String requestBody = Util.extractRequestBody(request);				
				RafRpcCommand saveAddressDataCommand = new SaveAddressDataCommand(requestBody);
				retVal = saveAddressDataCommand.execute();
			} else if (method.equals("removeAddressData")) {
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand removeAddressDataCommand = new RemoveAddressDataCommand(requestBody);
				retVal = removeAddressDataCommand.execute();
			}
			
		}
		
		response.getOutputStream().println(retVal);
	}
}