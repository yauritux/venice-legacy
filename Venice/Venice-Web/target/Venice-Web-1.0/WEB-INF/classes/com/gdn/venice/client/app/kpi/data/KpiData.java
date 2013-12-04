package com.gdn.venice.client.app.kpi.data;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.kpi.presenter.KpiSetupPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.FieldType;

public class KpiData {
	private static DataSource kpiDetailViewerDs= null;
	private static DataSource merchantKpiDashboardDs= null;
	private static DataSource kpiSetupPeriodDs= null;
	private static DataSource kpiSetupPartySlaDs= null;

	public static DataSource getKpiSetupPartySlaData(String idTypeParty,String idParty) {
		if (kpiSetupPartySlaDs != null) {
			return kpiSetupPartySlaDs;
		} else {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID,  "ID Party Target"),
				new DataSourceTextField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLAID,  "ID Party Sla"),
				new DataSourceTextField(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID, "KPI"),				
				new DataSourceTextField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID, "Party ID"),	
				new DataSourceTextField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME, "Party"),	
				new DataSourceTextField(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID,  "Baseline"),
				new DataSourceTextField(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE, "Target")
							
		};
		dataSourceFields[0].setPrimaryKey(true);
		dataSourceFields[1].setPrimaryKey(true);
		dataSourceFields[4].setPrimaryKey(true);
		
		RafDataSource kpiPartySlaDataDs = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + KpiSetupPresenter.kpiMaintenancePresenterServlet + "?method=fetchKpiSetupPartySlaDataCommand&type=DataSource",
				null,null,null,	
				dataSourceFields);		

		HashMap<String, String> params = new HashMap<String, String>();		
		if (idParty != null) {			
			params.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID, idParty);
		} else {
			params.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID, "");
		}
		if (idParty != null) {			
			params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, idTypeParty);
		} else {
			params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, "");
		}
		kpiPartySlaDataDs.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		return kpiPartySlaDataDs;
	}    
		
	}
	
	
	public static DataSource getKpiSetupPeriodData() {
		if (kpiSetupPeriodDs != null) {
			return kpiSetupPeriodDs;
		} else {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.KPIMEASUREMENTPERIOD_PERIODID, "Period Id"),
				new DataSourceTextField(DataNameTokens.KPIMEASUREMENTPERIOD_DESCRIPTION, "Description"),
				new DataSourceField(DataNameTokens.KPIMEASUREMENTPERIOD_FROMDATETIME,  FieldType.DATETIME, "Period From"),
				new DataSourceField(DataNameTokens.KPIMEASUREMENTPERIOD_TODATETIME,  FieldType.DATETIME, "Period To"),
							
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		
		RafDataSource kpiPeriodDataDs = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + KpiSetupPresenter.kpiMaintenancePresenterServlet + "?method=fetchPeriodKpiCaseData&type=DataSource",
				GWT.getHostPageBaseURL() + KpiSetupPresenter.kpiMaintenancePresenterServlet + "?method=addPeriodKpiCaseData&type=DataSource",
				GWT.getHostPageBaseURL() + KpiSetupPresenter.kpiMaintenancePresenterServlet + "?method=updatePeriodKpiCaseData&type=DataSource",
				GWT.getHostPageBaseURL() + KpiSetupPresenter.kpiMaintenancePresenterServlet + "?method=deletePeriodKpiCaseData&type=DataSource",
				dataSourceFields);		

		return kpiPeriodDataDs;
	}    
		
	}
	
	

	public static DataSource getKpiDetailViewerData() {
			if (kpiDetailViewerDs != null) {
				return kpiDetailViewerDs;
			} else {
			DataSourceField[] dataSourceFields = {
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODTRANSACTION_ID,  "Id Transaksi"),
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODTRANSACTION_KPIPARTYMEASUREMENTPERIOD_PARTYID, "Party"),				
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODTRANSACTION_KPIPARTYMEASUREMENTPERIOD_KPIPERIODID,  "Period"),
					new DataSourceField(DataNameTokens.KPIPARTYPERIODTRANSACTION_TIMESTAMP,  FieldType.DATETIME, "Timestamp"),
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODTRANSACTION_KPIKEYPERMORMANCEINDICATOR_KPIID, "Kpi"),
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODTRANSACTION_TRANSACTIONVALUE, "Value"),
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODTRANSACTION_TRANSACTIONREASON, "Reason"),
								
			};
			dataSourceFields[0].setPrimaryKey(true);
			
			RafDataSource kpiDetailViewer = new RafDataSource(
					"/response/data/*",
					GWT.getHostPageBaseURL() + KpiSetupPresenter.kpiMaintenancePresenterServlet + "?method=fetchKpiDetailViewerDataCommand&type=DataSource",
					null,null,null,			
					dataSourceFields);		

			
			return kpiDetailViewer;
		}    
			
		
	}


	public static DataSource getKpiDashboardData(String TypeVenParty, String periodID, String partyID) {
		if (merchantKpiDashboardDs != null) {
			return merchantKpiDashboardDs;
		} else {
			DataSourceField[] dataSourceFields = {
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID, "Period ID"),	
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODDESC, "Period"),	
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID,  "Party ID"),					
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME, "Party"),
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID, "Kpi ID"),
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIDESC, "KPI"),
					new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE, "Performance"),
	        
			};
			dataSourceFields[0].setPrimaryKey(true);
			
			RafDataSource kpiDashboard = new RafDataSource(
					"/response/data/*",
					GWT.getHostPageBaseURL() + KpiSetupPresenter.kpiMaintenancePresenterServlet + "?method=fetchKKpiDashboardDataCommand&type=DataSource",
					null,null,null,			
					dataSourceFields);		
			HashMap<String, String> params = new HashMap<String, String>();		
			if (TypeVenParty != null) {			
				params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, TypeVenParty);
			} else {
				params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, "");
			}
			if (periodID != null) {			
				params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID, periodID);
			} else {
				params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID, "");
			}
			if (partyID != null) {			
				params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID, partyID);
			} else {
				params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID, "");
			}
			kpiDashboard.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
			
			return kpiDashboard;
		}    
		
	}


	public static DataSource getItemParty(String TypeVenParty,String idParty) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENPARTY_PARTYID, "Parti ID"),
				new DataSourceTextField(DataNameTokens.VENPARTY_FULLORLEGALNAME, "Name Party")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource retVal = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + KpiSetupPresenter.kpiMaintenancePresenterServlet + "?method=fetchItemPartyData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();		
		if (TypeVenParty != null) {			
			params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, TypeVenParty);
		} else {
			params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, "");
		}
		if (idParty != null) {			
			params.put(DataNameTokens.VENPARTY_PARTYID, idParty);
		} else {
			params.put(DataNameTokens.VENPARTY_PARTYID, "");
		}
		retVal.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		return retVal;

	}
	
	public static DataSource getKpiTopMerchantData(String sum,String idKpi,String idPeriod,String typeIdMerchant) {
	
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID, "Period ID"),	
				new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODDESC, "Period"),	
				new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID,  "Party ID"),					
				new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME, "Party"),
				new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID, "Kpi ID"),
				new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIDESC, "KPI"),
				new DataSourceTextField(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE, "Performance"),
        
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource kpiPartySlaDataDs = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + KpiSetupPresenter.kpiMaintenancePresenterServlet + "?method=fetchKpiTopMerchantDataDataCommand&type=DataSource",
				null,null,null,	
				dataSourceFields);		

		HashMap<String, String> params = new HashMap<String, String>();		
		if (sum != null) {			
			params.put("sum", sum);
		} else {
			params.put("sum", "");
		}
		if (idKpi != null) {			
			params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID, idKpi);
		} else {
			params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID, "");
		}
		if (idPeriod != null) {			
			params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID, idPeriod);
		} else {
			params.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID, "");
		}
		if (typeIdMerchant != null) {			
			params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, typeIdMerchant);
		} else {
			params.put(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID, "");
		}
		kpiPartySlaDataDs.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		return kpiPartySlaDataDs;
	}    

	
	

	
}
