package com.gdn.venice.server.app.kpi.presenter;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.server.app.kpi.presenter.commands.AddKpiSetupPartySlaDataCommand;
import com.gdn.venice.server.app.kpi.presenter.commands.AddPeriodKpiSetupCommand;
import com.gdn.venice.server.app.kpi.presenter.commands.DeleteKpiSetupPartySlaDataCommand;
import com.gdn.venice.server.app.kpi.presenter.commands.DeletePeriodeKpiSetupDataCommand;
import com.gdn.venice.server.app.kpi.presenter.commands.FetchItemPartyData;
import com.gdn.venice.server.app.kpi.presenter.commands.FetchKpiDashboardDataCommand;
import com.gdn.venice.server.app.kpi.presenter.commands.FetchKpiDetailViewerDataCommand;
import com.gdn.venice.server.app.kpi.presenter.commands.FetchKpiPeriodDataCommand;
import com.gdn.venice.server.app.kpi.presenter.commands.FetchKpiSetupPartySlaDataCommand;
import com.gdn.venice.server.app.kpi.presenter.commands.FetchKpiSetupPartySlaDataCommandComboBoxBaseline;
import com.gdn.venice.server.app.kpi.presenter.commands.FetchKpiSetupPartySlaDataCommandComboBoxKpi;
import com.gdn.venice.server.app.kpi.presenter.commands.FetchKpiSetupPartySlaDataCommandComboBoxParty;
import com.gdn.venice.server.app.kpi.presenter.commands.FetchKpiSetupPartySlaDataCommandComboBoxPeriod;
import com.gdn.venice.server.app.kpi.presenter.commands.FetchKpiTopMerchantDataDataCommand;
import com.gdn.venice.server.app.kpi.presenter.commands.UpdateKpiSetupPartySlaDataCommand;
import com.gdn.venice.server.app.kpi.presenter.commands.UpdatePeriodeKpiSetupDataCommand;
import com.gdn.venice.server.app.kpi.presenter.commands.fetchValueBaselineFromPartyTarget;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Servlet implementation class BlackListMaintenancePresenterServlet
 */
public class KpiMaintenancePresenterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public KpiMaintenancePresenterServlet() {
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
		
			if (method.equals("fetchPeriodKpiCaseData")) {				
				RafDsCommand fetchKpiPeriodDataCommand = new FetchKpiPeriodDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchKpiPeriodDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("addPeriodKpiCaseData")){
				RafDsCommand addPeriodKpiSetupCommand = new AddPeriodKpiSetupCommand(rafDsRequest);
				RafDsResponse rafDsResponse = addPeriodKpiSetupCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
						retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}
					else {
						String errorMessage = "Please check again of Date data is selected to be Added";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("updatePeriodKpiCaseData")){		
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.KPIMEASUREMENTPERIOD_PERIODID, request.getParameter(DataNameTokens.KPIMEASUREMENTPERIOD_PERIODID));
				rafDsRequest.setParams(params);	
				RafDsCommand updatePeriodeKpiSetupDataCommand = new UpdatePeriodeKpiSetupDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = updatePeriodeKpiSetupDataCommand.execute();
				try {
					if (rafDsResponse.getStatus() == 0){
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
					}else {
						String errorMessage = "Please check again of Date data is selected to be Updated";						
						retVal = "<response><status>-1</status><data>" + errorMessage + "</data></response>";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(method.equals("deletePeriodKpiCaseData")){										
				RafDsCommand deletePeriodKpiSetupDataCommand = new DeletePeriodeKpiSetupDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = deletePeriodKpiSetupDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchKpiSetupPartySlaDataCommand")) {		
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID, request.getParameter(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID));
				params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, request.getParameter(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchKpiSetupPartySlaDataCommand = new FetchKpiSetupPartySlaDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchKpiSetupPartySlaDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}	else if (method.equals("fetchItemPartyData")) {	
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, request.getParameter(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID));
				params.put(DataNameTokens.VENPARTY_PARTYID, request.getParameter(DataNameTokens.VENPARTY_PARTYID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchItemPartyData = new FetchItemPartyData(rafDsRequest);
				RafDsResponse rafDsResponse = fetchItemPartyData.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchKpiDetailViewerDataCommand")) {		
				
				RafDsCommand fetchKpiDetailViewerDataCommand = new FetchKpiDetailViewerDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchKpiDetailViewerDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchKKpiDashboardDataCommand")) {		
				HashMap<String, String> params = new HashMap<String, String>();
				params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, request.getParameter(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID));
				params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID, request.getParameter(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID));
				params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID, request.getParameter(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchKKpiDashboardDataCommand = new FetchKpiDashboardDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchKKpiDashboardDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if (method.equals("fetchKpiTopMerchantDataDataCommand")) {		
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("sum", request.getParameter("sum"));
				params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID, request.getParameter(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID));
				params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID, request.getParameter(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID));
				params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, request.getParameter(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID));
				rafDsRequest.setParams(params);
				RafDsCommand fetchKpiTopMerchantDataDataCommand = new FetchKpiTopMerchantDataDataCommand(rafDsRequest);
				RafDsResponse rafDsResponse = fetchKpiTopMerchantDataDataCommand.execute();
				try {
					retVal = RafDsResponse.convertRafDsResponsetoXml(rafDsResponse);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		} else if (type.equals(RafRpcCommand.RPC)) {
			String method = request.getParameter("method");			
			if(method.equals("fetchKpiSetupPartySlaDataCommandComboBoxKpi")){										
				RafRpcCommand fetchKpiSetupPartySlaDataCommandComboBoxKpi = new FetchKpiSetupPartySlaDataCommandComboBoxKpi();
				retVal = fetchKpiSetupPartySlaDataCommandComboBoxKpi.execute();				
			}else if(method.equals("fetchKpiSetupPartySlaDataCommandComboBoxBaseline")){										
				RafRpcCommand fetchKpiSetupPartySlaDataCommandComboBoxBaseline = new FetchKpiSetupPartySlaDataCommandComboBoxBaseline();
				retVal = fetchKpiSetupPartySlaDataCommandComboBoxBaseline.execute();				
			}
			else if(method.equals("fetchKpiSetupPartySlaDataCommandComboBoxPeriod")){										
				RafRpcCommand fetchKpiSetupPartySlaDataCommandComboBoxPeriod = new FetchKpiSetupPartySlaDataCommandComboBoxPeriod();
				retVal = fetchKpiSetupPartySlaDataCommandComboBoxPeriod.execute();				
			}else if(method.equals("fetchKpiSetupPartySlaDataCommandComboBoxParty")){										
				RafRpcCommand fetchKpiSetupPartySlaDataCommandComboBoxParty = new FetchKpiSetupPartySlaDataCommandComboBoxParty();
				retVal = fetchKpiSetupPartySlaDataCommandComboBoxParty.execute();				
			}else if(method.equals("addKpiSetupPartySlaDataCommand")){				
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand addKpiSetupPartySlaDataCommand = new AddKpiSetupPartySlaDataCommand(requestBody);
				retVal = addKpiSetupPartySlaDataCommand.execute();	
			}else if(method.equals("updateKpiSetupPartySlaDataCommand")){				
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand updateKpiSetupPartySlaDataCommand = new UpdateKpiSetupPartySlaDataCommand(requestBody);
				retVal = updateKpiSetupPartySlaDataCommand.execute();	
			}else if(method.equals("deleteKpiSetupPartySlaDataCommand")){										
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand deleteKpiSetupPartySlaDataCommand = new DeleteKpiSetupPartySlaDataCommand(requestBody);
				retVal = deleteKpiSetupPartySlaDataCommand.execute();					
			}else if(method.equals("fetchValueBaselineFromPartyTarget")){										
				String requestBody = Util.extractRequestBody(request);
				RafRpcCommand fetchValueBaselineFromPartyTarget = new fetchValueBaselineFromPartyTarget(requestBody);
				retVal = fetchValueBaselineFromPartyTarget.execute();
				
				
			}
		}
		response.getOutputStream().println(retVal);
	}
}
