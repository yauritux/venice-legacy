package com.gdn.venice.client.app.finance.data;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.presenter.PromotionPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;

/**
 * Data class to manage the data sources for the promotion screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PromotionData {

	/**
	 * This method gets the data source for the promotion data table at the top
	 * of the screen
	 * 
	 * @return the data source for promotion data
	 */
	public static DataSource getPromotionData() {
		
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(DataNameTokens.VENPROMOTION_PROMOTIONID, "Id"),
				new DataSourceTextField(DataNameTokens.VENPROMOTION_PROMOTIONCODE, "Promotion Code"),
				new DataSourceTextField(DataNameTokens.VENPROMOTION_PROMOTIONNAME, "Promotion Name"), 
				new DataSourceTextField(DataNameTokens.VENPROMOTION_VENPROMOTIONTYPE_PROMOTIONTYPEDESC, "Promotion Type"),
				new DataSourceTextField(DataNameTokens.VENPROMOTION_GDNMARGIN, "GDN Margin"), 
				new DataSourceTextField(DataNameTokens.VENPROMOTION_MERCHANTMARGIN, "Merchant Margin"), 
				new DataSourceTextField(DataNameTokens.VENPROMOTION_OTHERSMARGIN, "Others Margin")
		
		};
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL() + PromotionPresenter.promotionPresenterServlet + "?method=fetchPromotionData&type=DataSource",
				GWT.getHostPageBaseURL() + PromotionPresenter.promotionPresenterServlet + "?method=addPromotionData&type=DataSource",
				GWT.getHostPageBaseURL() + PromotionPresenter.promotionPresenterServlet + "?method=updatePromotionData&type=DataSource",
				GWT.getHostPageBaseURL() + PromotionPresenter.promotionPresenterServlet + "?method=removePromotionData&type=DataSource", 
				dataSourceFields);

		return dataSource;
		
	}

	/**
	 * This method gets the promotion share detail data for the detail table at
	 * the bottom of the screen
	 * 
	 * @param promotionId
	 *            the promotion ID used to query the detail rows
	 * @return the data source with the mapped data source fields
	 */
	public static DataSource getPromotionShareData(String promotionId) {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(
						DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID,
						"Promotion Id"),
				new DataSourceIntegerField(
						DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID,
						"Calc Method Id"),
				new DataSourceTextField(
						DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC,
						"Calc Method Desc"),
				new DataSourceIntegerField(
						DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID,
						"Party Id"),
				new DataSourceTextField(
						DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_FULLORLEGALNAME,
						"Party  Name"),
				new DataSourceTextField(
						DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE,
						"Share") };
		//dataSourceFields[0].setPrimaryKey(true);

		/*
		 * Set the data source for the Promotion share data to the
		 * appropriate servlet method
		 */
		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL()+ PromotionPresenter.promotionPresenterServlet+ "?method=fetchPromotionShareData&type=DataSource",
				null,
				null, 
				null, 
				dataSourceFields);
		/*
		 * Set the promotion id to narrow the query to the specific
		 * promotionData
		 */
		HashMap<String, String> params = new HashMap<String, String>();
		if(promotionId != null) {
			params.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID, promotionId);
			dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}
		return dataSource;
	}

	/**
	 * This method returns the data source for the promotion share data for the
	 * popup dialog screen when the party is chosen
	 * 
	 * @return the data source with the required party data.
	 */
	public static DataSource getPromotionPartyData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(
						DataNameTokens.VENPARTY_PARTYID,
						"Party Id"),
				new DataSourceTextField(
						DataNameTokens.VENPARTY_FULLORLEGALNAME,
						"Party Name") };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL()+ PromotionPresenter.promotionPresenterServlet+ "?method=fetchPromotionPartyData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields);

		return dataSource;
	}

	/**
	 * This method returns the data source for the calculation methods for the drop down list of calculation methods
	 * @return the data source for the drop down list
	 */
	public static DataSource getPromotionShareCalcMethodData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(
						DataNameTokens.VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID,
						"Calc Method Id"),
				new DataSourceTextField(
						DataNameTokens.VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC,
						"Calc Method Desc"), };
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL()+ PromotionPresenter.promotionPresenterServlet+ "?method=fetchPromotionShareCalcMethodComboBoxData&type=DataSource",
						null,
						null,
						null,
				dataSourceFields);

		return dataSource;
	}
}
