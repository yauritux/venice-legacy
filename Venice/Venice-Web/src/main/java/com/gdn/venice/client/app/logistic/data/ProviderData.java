package com.gdn.venice.client.app.logistic.data;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.presenter.ProviderManagementPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceFloatField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.FieldType;

/**
 * Data class to manage the data sources for the Provider Management screen
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class ProviderData {
	
	/**
	 * Returns the data source with the party type data
	 * @return the data source
	 */
	public static DataSource getPartyTypeData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENPARTYTYPE_PARTYTYPEID, "Party Type ID"),
				new DataSourceTextField(DataNameTokens.VENPARTYTYPE_PARTYTYPEDESC, "Party Type Desc"),
				 };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchPartyTypeData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);

		return dataSource;
	}
		
	/**
	 * This method gets the data source for the Logistics Provider data table at
	 * the top of the screen
	 * 
	 * @return the data source with the required Logistics Provider data.
	 */
	public static DataSource getLogisticsProviderData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, "Provider ID"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID, "Party ID"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME, "Provider Name"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEID, "party Type ID"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC, "Party Type"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE, "Provider Code"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEID, "Report Template ID"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC, "Report Template"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEID, "Invoice Template ID"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC, "Invoice Template"),};
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchProviderData&type=DataSource",
				null,
				null,
				null, 
				dataSourceFields);

		return dataSource;		  
	}
	
	/**
 	 *This method gets the data source for the template combo box
	 * @return the data source for report template
	 */
	public static DataSource getTemplateData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEID, "Template Id"),
				new DataSourceTextField(DataNameTokens.LOGREPORTTEMPLATE_TEMPLATEDESC, "Template Desc"),
				 };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchTemplateData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);

		return dataSource;
	}
	
	/**
	 * This method gets the data source for the Logistics Provider Address data table at
	 * the Address Tab
	 * 
	 * @return the data source with the required Logistics Provider Address data.
	 */
	public static DataSource getLogisticsProviderAddressData(String partyId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID, "Provider"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID, "Address ID"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1, "Street Address 1"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2, "Street Address 2"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN, "Kelurahan"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN, "Kecamatan"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID,	"City ID"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME, "City"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATEID, "Province ID"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME, "Province"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE, "Postal Code"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYID, "Country ID"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME, "Country"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID, "Address Type ID"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC, "Address Type"), };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchProviderAddressData&type=DataSource",
				null,
				null,
				null, 
				dataSourceFields);
		HashMap<String, String> params = new HashMap<String, String>();
		if(partyId != null) {
			params.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID, partyId);
			dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}

		return dataSource;
		  
	}
	
	/**
	 * This method returns the data source for the logistic provider address data for the
	 * popup dialog screen when the city/region is chosen
	 * 
	 * @return the data source with the required city/region data.
	 */
	public static DataSource getCityData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENCITY_CITYID, "City ID"),
				new DataSourceTextField(DataNameTokens.VENCITY_CITYCODE, "City Code"),
				new DataSourceTextField(DataNameTokens.VENCITY_CITYNAME, "City Name "), };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL()+ ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchCityData&type=DataSource",
				GWT.getHostPageBaseURL()+ ProviderManagementPresenter.providerManagementPresenterServlet + "?method=addCityData&type=DataSource",
				GWT.getHostPageBaseURL()+ ProviderManagementPresenter.providerManagementPresenterServlet + "?method=updateCityData&type=DataSource",
				GWT.getHostPageBaseURL()+ ProviderManagementPresenter.providerManagementPresenterServlet + "?method=removeCityData&type=DataSource",
				dataSourceFields);

		return dataSource;
	}
	
	/**
	 *  This method gets the data source for the province combo box
	 * @return the data source for province(state)
	 */
	public static DataSource getProvinceData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENSTATE_STATEID, "Province ID"),
				new DataSourceTextField(DataNameTokens.VENSTATE_STATECODE, "Province Code"),
				new DataSourceTextField(DataNameTokens.VENSTATE_STATENAME, "Province Name "), };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL()+ ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchProvinceData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);

		return dataSource;
	}
	
	/**
	 *  This method gets the data source for the country combo box
	 * @return the data source for country
	 */
	public static DataSource getCountryData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENCOUNTRY_COUNTRYID, "Country ID"),
				new DataSourceTextField(DataNameTokens.VENCOUNTRY_COUNTRYCODE, "Country Code"),
				new DataSourceTextField(DataNameTokens.VENCOUNTRY_COUNTRYNAME, "Country Name "), };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL()+ ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchCountryData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);

		return dataSource;
	}
	
	/**
	 *  This method gets the data source for the Address Type combo box
	 * @return the data source for address type
	 */
	public static DataSource getAddressTypeData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENADDRESSTYPE_ADDRESSTYPEID, "Address Type ID"),
				new DataSourceTextField(DataNameTokens.VENADDRESSTYPE_ADDRESSTYPEDESC, "Address Type Desc"),};
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL()+ ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchAddressTypeData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);

		return dataSource;
	}

	/**
	 * This method gets the data source for the Logistics Provider Contact data table at
	 * the Address Tab
	 * 
	 * @return the data source with the required Logistics Provider Contact data.
	 */
	public static DataSource getLogisticsProviderContactData(String partyId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID, "Party ID"),
				new DataSourceIntegerField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID, "Contact Detail ID"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID, "Type ID"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, "Type"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL, "Contact Detail "),	
						};
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchProviderContactData&type=DataSource",
				null,
				null,
				null, 
				dataSourceFields);
		
		HashMap<String, String> params = new HashMap<String, String>();
		if(partyId != null) {
			params.put(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID, partyId);
			dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}

		return dataSource;
		  
	}
	
	
	/**
	 * This method gets the data source for the Logistics Provider Service data at
	 * the Service Tab
	 * 
	 * @return the data source with the required Logistics Provider Service data.
	 */
	public static DataSource getLogisticsProviderServiceData(String logProviderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID, "Service ID"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, "Provider ID"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE, "Service Code"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC, "Service Description"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID, "Service Type ID"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC, "Service Type"),
				 };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchLogisticProviderServiceData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);
		
		HashMap<String, String> params = new HashMap<String, String>();
		if(logProviderId != null) {
			params.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, logProviderId);
			dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}else{
			dataSource.getOperationBinding(DSOperationType.ADD).setDefaultParams(params);
		}

		return dataSource;
	}
	
	
	/**
	 *  This method gets the data source for the country combo box
	 * @return the data source for Logistic Provider Service Type
	 */
	public static DataSource getLogisticsProviderServiceTypeData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEID, "ID"),
				new DataSourceTextField(DataNameTokens.LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC, "Desc"),
				new DataSourceBooleanField(DataNameTokens.LOGLOGISTICSSERVICETYPE_EXPRESSFLAG, "Express "), };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL()+ ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchLogServiceTypeData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);

		return dataSource;
	}
	
	/**
	 * This method gets the data source for the Logistics Provider Schedule data at
	 * the Schedule Tab
	 * 
	 * @return the data source with the required Logistics Provider Service data.
	 */
	public static DataSource getLogisticsProviderScheduleData(String logProviderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULESID, "Pickup ID"),
				new DataSourceIntegerField(DataNameTokens.LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, "Provider ID"),
				new DataSourceTextField(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME, "From Time"),
				new DataSourceTextField(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME, "To Time"),
				new DataSourceTextField(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC, "Description"),
				new DataSourceBooleanField(DataNameTokens.LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS, "Include Public Holidays"),
				new DataSourceTextField(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID, "Days ID"),
				new DataSourceTextField(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC, "Days"),
				 };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchLogisticProviderScheduleData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);
		
		HashMap<String, String> params = new HashMap<String, String>();
		if(logProviderId != null) {
			params.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, logProviderId);
			dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}

		return dataSource;
	}
	
	/**
	 *  This method gets the data source for the schedule days
	 * @return the data source for days
	 */
	public static DataSource getDaysData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID, "Days ID"),
				new DataSourceTextField(DataNameTokens.LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC, "Days"),};
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL()+ ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchDaysData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);

		return dataSource;
	}
	
	/**
	 * Returns a data source with the data for the contact detail type combo box data
	 * @return the data source
	 */
	public static DataSource getLogisticsProviderAgreementData(String logProviderId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID, "Agreement ID"),
				new DataSourceIntegerField(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, "Provider ID"),
				new DataSourceTextField(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDESC, "Agreement Description"),
				new DataSourceField(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE, FieldType.DATE, "Date"),
				new DataSourceField(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE, FieldType.DATE, "Exp. Date"),
				new DataSourceIntegerField(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT, "Pickup Days"),
				new DataSourceIntegerField(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT, "Delivery Days"),
				new DataSourceFloatField(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT, "Disc. Level(%)"),
				new DataSourceFloatField(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE, "PPN (%)"),
				 };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + ProviderManagementPresenter.providerManagementPresenterServlet + "?method=fetchLogisticProviderAgreementData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);
		
		HashMap<String, String> params = new HashMap<String, String>();
		if(logProviderId != null) {
			params.put(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID, logProviderId);
			dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}
		return dataSource;
	}
}
