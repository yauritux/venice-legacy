package com.gdn.venice.client.app.finance.data;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.presenter.CoaSetupPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * Data class to manage the data sources for the COA setup screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class CoaSetupData {

	
	/**
	 * This method gets the data source for the account data table
	 * 
	 * @return the data source for account data
	 */
	public static DataSource getAccountData() {
		
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(
						DataNameTokens.FINACCOUNT_ACCOUNTID, "Id"),
				new DataSourceTextField(
						DataNameTokens.FINACCOUNT_ACCOUNTNUMBER,
						"Account Number"),
				new DataSourceTextField(
						DataNameTokens.FINACCOUNT_ACCOUNTDESC,
						"Account Description"),
				new DataSourceIntegerField(
						DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEID,
						"Account Type"),
				new DataSourceTextField(
						DataNameTokens.FINACCOUNT_FINACCOUNTTYPE_ACCOUNTTYPEDESC,
						"Account Type"),
				new DataSourceIntegerField(
						DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYID,
						"Account Category"),
				new DataSourceTextField(
						DataNameTokens.FINACCOUNT_FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC,
						"Account Category"),
				new DataSourceBooleanField(
						DataNameTokens.FINACCOUNT_SUMMARYACCOUNT,
						"Summary Account"),};
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + CoaSetupPresenter.coaSetupPresenterServlet + "?method=fetchAccountData&type=DataSource",
				GWT.getHostPageBaseURL() + CoaSetupPresenter.coaSetupPresenterServlet + "?method=addAccountData&type=DataSource",
				GWT.getHostPageBaseURL() + CoaSetupPresenter.coaSetupPresenterServlet + "?method=updateAccountData&type=DataSource",
				GWT.getHostPageBaseURL() + CoaSetupPresenter.coaSetupPresenterServlet + "?method=removeAccountData&type=DataSource", 
				dataSourceFields);

		return dataSource;
		
	}
	
	/**
	 *  This method gets the data source for the account type combo box
	 * @return the data source for account type
	 */
	public static DataSource getAccountTypeData() {
		DataSourceField[] dataSourceFields = {

				new DataSourceIntegerField(
						DataNameTokens.FINACCOUNTTYPE_ACCOUNTTYPEID,
						"Account Type Id"),
				new DataSourceTextField(
						DataNameTokens.FINACCOUNTTYPE_ACCOUNTTYPEDESC,
						"Account Type Desc"),
				 };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + CoaSetupPresenter.coaSetupPresenterServlet + "?method=fetchAccountTypeComboBoxData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);

		return dataSource;
	}
	
	/**
	 * This method gets the data source for the account category combo box
	 * @return the data source for account category
	 */
	public static DataSource getAccountCategoryData() {
		DataSourceField[] dataSourceFields = {

				new DataSourceIntegerField(
						DataNameTokens.FINACCOUNTCATEGORY_ACCOUNTCATEGORYID,
						"Account Category Id"),
				new DataSourceTextField(
						DataNameTokens.FINACCOUNTCATEGORY_ACCOUNTCATEGORYDESC,
						"Account Category Desc"),
				 };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + CoaSetupPresenter.coaSetupPresenterServlet + "?method=fetchAccountCategoryComboBoxData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);

		return dataSource;
	}
}
