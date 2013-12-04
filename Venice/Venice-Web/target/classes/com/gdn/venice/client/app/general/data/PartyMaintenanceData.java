package com.gdn.venice.client.app.general.data;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.general.presenter.PartyMaintenancePresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;

/**
 * Data class to manage the data sources for the Party Maintenance screen
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian
 * Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class PartyMaintenanceData {

	/**
	 * This method gets the data source for the party maintenance data table at
	 * the top of the screen
	 * 
	 * @return the data source with the required party maintenance data.
	 */
	public static DataSource getPartyMaintenanceData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENPARTY_PARTYID, "Party ID"),
				new DataSourceIntegerField(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEID, "Party Type ID"),
				new DataSourceTextField(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC, "Party Type"),
				new DataSourceTextField(DataNameTokens.VENPARTY_FIRSTNAME, "First Name"),
				new DataSourceTextField(DataNameTokens.VENPARTY_MIDDLENAME, "Middle Name"),
				new DataSourceTextField(DataNameTokens.VENPARTY_LASTNAME, "Last Name"),
				new DataSourceTextField(DataNameTokens.VENPARTY_FULLORLEGALNAME, "Full or Legal Name"),
				new DataSourceTextField(DataNameTokens.VENPARTY_POSITION, "Position"), };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + PartyMaintenancePresenter.partyMaintenanceServlet + "?method=fetchPartyMaintenanceData&type=DataSource",
				GWT.getHostPageBaseURL() + PartyMaintenancePresenter.partyMaintenanceServlet + "?method=addPartyMaintenanceData&type=DataSource",
				GWT.getHostPageBaseURL() + PartyMaintenancePresenter.partyMaintenanceServlet + "?method=updatePartyMaintenanceData&type=DataSource",
				GWT.getHostPageBaseURL() + PartyMaintenancePresenter.partyMaintenanceServlet + "?method=removePartyMaintenanceData&type=DataSource",
				dataSourceFields);

		return dataSource;
	}
	
	/**
	 * Returns the data source with the party type data
	 * @return the data source
	 */
	public static DataSource getPartyTypeData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENPARTYTYPE_PARTYTYPEID, "Party Type ID"),
				new DataSourceTextField(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC, "Party Type Desc"),
				 };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + PartyMaintenancePresenter.partyMaintenanceServlet + "?method=fetchPartyTypeData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);

		return dataSource;
	}
	
	/**
	 * Returns the data source with the contact detail data for the party
	 * @param partyId is the party id to use for the query
	 * @return the data source
	 */
	public static DataSource getPartyContactDetailData(String partyId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID, "Contact Detail ID"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID, "Party ID"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, "Type"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL, "Detail")

		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + PartyMaintenancePresenter.partyMaintenanceServlet + "?method=fetchPartyContactDetailData&type=DataSource",
				GWT.getHostPageBaseURL() + PartyMaintenancePresenter.partyMaintenanceServlet + "?method=addPartyContactDetailData&type=DataSource",
				GWT.getHostPageBaseURL() + PartyMaintenancePresenter.partyMaintenanceServlet + "?method=updatePartyContactDetailData&type=DataSource",
				GWT.getHostPageBaseURL() + PartyMaintenancePresenter.partyMaintenanceServlet + "?method=removePartyContactDetailData&type=DataSource",
				dataSourceFields); 
		
		HashMap<String, String> params = new HashMap<String, String>();
		if(partyId != null) {
			params.put(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID, partyId);
			dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}
		return dataSource;
	}
	
	/**
	 * Returns a data source with the data for the contact detail type combo box data
	 * @return the data source
	 */
	public static DataSource getContactDetailTypeData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENCONTACTDETAILTYPE_CONTACTDETAILTYPEID, "Type ID"),
				new DataSourceTextField(DataNameTokens.VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC, "Type Desc"),
				 };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + PartyMaintenancePresenter.partyMaintenanceServlet + "?method=fetchContactDetailTypeData&type=DataSource",
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
	public static DataSource getAddressData(String partyId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID, "Party ID"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID, "Address ID"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1, "Street Address 1"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2, "Street Address 2"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN, "Kelurahan"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN, "Kecamatan"),
				new DataSourceTextField(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID, "City ID"),
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
				GWT.getHostPageBaseURL() + PartyMaintenancePresenter.partyMaintenanceServlet + "?method=fetchAddressData&type=DataSource",
				null,
				null,
				null, 
				dataSourceFields);
		HashMap<String, String> params = new HashMap<String, String>();
		if(partyId != null) {
			params.put(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID, partyId);
			dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}

		return dataSource;		  
	}
}