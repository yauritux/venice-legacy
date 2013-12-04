package com.gdn.venice.server.app.logistics.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.logistics.presenter.commands.AddCityDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.DeleteProviderDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchAddressTypeDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchCityDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchCountryDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchDaysDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchLogServiceTypeDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchLogisticProviderAgreementDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchLogisticProviderScheduleDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchLogisticProviderServiceData;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchPartyTypeComboBoxDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchProviderAddressDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchProviderContactData;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchProviderDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchProvinceDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.FetchTemplateDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.RemoveAddressDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.RemoveAgreementDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.RemoveCityDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.RemoveContactDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.RemoveServiceDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.SaveAddressDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.SaveAgreementDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.SaveContactDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.SaveProviderDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.SaveScheduleDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.SaveServiceDataCommand;
import com.gdn.venice.server.app.logistics.presenter.commands.UpdateCityDataCommand;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;


/**
 * Servlet implementation class ProviderManagementPresenterServlet
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ProviderManagementPresenterServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	/**
	 *Constructor for Provider Management Presenter Servlet
	 * @see HttpServlet#HttpServlet()
	 */
	public ProviderManagementPresenterServlet(){
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

			if (request.getParameter(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID)!=null) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID, request.getParameter(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID));
				rafDsRequest.setParams(params);
			} else if (request.getParameter(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)!=null) {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, request.getParameter(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
				rafDsRequest.setParams(params);
			}
			
			String method = request.getParameter("method");
			
			if(method.equals("fetchProviderData")){
				
				RafDsCommand fetchProviderDataCommand = new FetchProviderDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchProviderDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchProviderAddressData")){
				RafDsCommand fetchProviderAddressDataCommand = new FetchProviderAddressDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchProviderAddressDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchTemplateData")){
				RafDsCommand fetchTemplateDataCommand = new FetchTemplateDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchTemplateDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchProviderContactData")){
				RafDsCommand fetchProviderContactData = new FetchProviderContactData(rafDsRequest);
				RafDsResponse rafDsResponse = fetchProviderContactData.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchLogisticProviderServiceData")){
				RafDsCommand fetchLogisticProviderServiceDataCommand = new FetchLogisticProviderServiceData(rafDsRequest);
				RafDsResponse rafDsResponse = fetchLogisticProviderServiceDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchLogisticProviderScheduleData")){
				RafDsCommand fetchLogisticProviderScheduleDataCommand = new FetchLogisticProviderScheduleDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchLogisticProviderScheduleDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchLogisticProviderAgreementData")){
				RafDsCommand fetchLogisticProviderAgreementDataCommand = new FetchLogisticProviderAgreementDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchLogisticProviderAgreementDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchCityData")){
				RafDsCommand fetchCityDataCommand = new FetchCityDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchCityDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("addCityData")){
				RafDsCommand addCityDataCommand = new AddCityDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addCityDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("updateCityData")){
				RafDsCommand updateCityDataCommand = new UpdateCityDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updateCityDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("removeCityData")){
				RafDsCommand removeCityDataCommand = new RemoveCityDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = removeCityDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchProvinceData")){
				RafDsCommand fetchProvinceDataCommand = new FetchProvinceDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchProvinceDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchCountryData")){
				RafDsCommand fetchCountryDataCommand = new FetchCountryDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchCountryDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchAddressTypeData")){
				RafDsCommand fetchAddressTypeDataCommand = new FetchAddressTypeDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchAddressTypeDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchLogServiceTypeData")){
				RafDsCommand fetchLogServiceTypeDataCommand = new FetchLogServiceTypeDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchLogServiceTypeDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchDaysData")){
				RafDsCommand fetchDaysDataCommand = new FetchDaysDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchDaysDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}else if(method.equals("fetchPartyTypeData")){
				RafDsCommand fetchPartyTypeDataCommand = new FetchPartyTypeComboBoxDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchPartyTypeDataCommand.execute();
				try{
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			
			
			
		}else if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");	
			if(method.equals("saveProviderData")){										
				String requestBody = Util.extractRequestBody(request);				
				RafRpcCommand saveProviderDataCommand = new SaveProviderDataCommand(requestBody);
				retVal = saveProviderDataCommand.execute();
			} else if (method.equals("deleteProviderData")) {
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand deleteProviderDataCommand = new DeleteProviderDataCommand(requestBody);
				retVal = deleteProviderDataCommand.execute();
			}else if (method.equals("saveAddressData")) {
				String requestBody = Util.extractRequestBody(request);				
				RafRpcCommand saveAddressDataCommand = new SaveAddressDataCommand(requestBody);
				retVal = saveAddressDataCommand.execute();
			} else if (method.equals("removeAddressData")) {
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand removeAddressDataCommand = new RemoveAddressDataCommand(requestBody);
				retVal = removeAddressDataCommand.execute();
			}else if (method.equals("saveContactData")) {
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand saveContactDataCommand = new SaveContactDataCommand(requestBody);
				retVal = saveContactDataCommand.execute();
			}else if (method.equals("removeContactData")) {
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand removeContactDataCommand = new RemoveContactDataCommand(requestBody);
				retVal = removeContactDataCommand.execute();
			}else if (method.equals("saveServiceData")) {
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand saveServiceDataCommand = new SaveServiceDataCommand(requestBody);
				retVal = saveServiceDataCommand.execute();
			}else if (method.equals("removeServiceData")) {
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand removeServiceDataCommand = new RemoveServiceDataCommand(requestBody);
				retVal = removeServiceDataCommand.execute();
			}else if (method.equals("saveScheduleData")) {
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand saveScheduleDataCommand = new SaveScheduleDataCommand(requestBody);
				retVal = saveScheduleDataCommand.execute();
			}else if (method.equals("saveAgreementData")) {
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand saveAgreementDataCommand = new SaveAgreementDataCommand(requestBody);
				retVal = saveAgreementDataCommand.execute();
			}else if (method.equals("removeAgreementData")) {
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand removeAgreementDataCommand = new RemoveAgreementDataCommand(requestBody);
				retVal = removeAgreementDataCommand.execute();
			}
		}
		
		response.getOutputStream().println(retVal);
	}

}
