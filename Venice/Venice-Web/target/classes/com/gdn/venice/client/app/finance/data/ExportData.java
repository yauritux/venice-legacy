package com.gdn.venice.client.app.finance.data;

import java.util.HashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.presenter.ExportPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;

/**
 * Data class to manage the data sources for the Export screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class ExportData {
	
	/**
	 * This method gets the data source for the journal voucher data(RolledUp Journal Header) table at the top
	 * of the screen
	 * 
	 * @return the data source for journal voucher data
	 */
	public static DataSource getJournalVoucherData() {

		DataSourceField[] dataSourceFields = {
				new DataSourceIntegerField(
						DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID,
						"Journal Header ID"),
				new DataSourceTextField(
						DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALDESC,
						"Voucher Desc"),
				new DataSourceTextField(
						DataNameTokens.FINROLLEDUPJOURNALHEADER_RUJOURNALFILENAMEANDPATH,
						"Filename"),
				new DataSourceTextField(
						DataNameTokens.FINROLLEDUPJOURNALHEADER_FINROLLEDUPJOURNALTYPE_FINANCEJOURNALTYPEDESC,
						"Journal Type"), };
		dataSourceFields[0].setPrimaryKey(true);
		/*
		 * Set the data source for the Account Lines data to the
		 * appropriate servlet method
		 */
		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL()
						+ ExportPresenter.exportPresenterServlet
						+ "?method=fetchExportData&type=DataSource", null,
				null, null, dataSourceFields);

		return dataSource;
	}

	/**
	 * This method gets the data source for the account lines data(RolledUp Journal Entry) table at the bottom
	 * of the screen
	 * 
	 * @param ruJournalHeaderId
	 *            the RolledUp Journal ID(journal voucher) used to query the detail rows
	 * @return the data source with the mapped data source fields
	 */
	public static DataSource getAccountLinesData(String ruJournalHeaderId) {

		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(	DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID,"Journal Header ID"),
				new DataSourceTextField(	DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTID,"Account Number"),
				new DataSourceTextField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINACCOUNT_ACCOUNTDESC,"Account Description"),
				//new DataSourceTextField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE,"Value"),
				new DataSourceIntegerField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_DEBET,"Debet"),
				new DataSourceIntegerField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUVALUE_CREDIT,"Credit"),
				new DataSourceTextField(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALSTATUS_JOURNALENTRYSTATUSDESC,"Journal Status"),
				new DataSourceTextField(DataNameTokens.FINROLLEDUPJOURNALENTRY_RUJOURNALENTRYTIMESTAMP,"Date"),
				new DataSourceTextField(DataNameTokens.FINROLLEDUPJOURNALENTRY_GROUPID,"Group")};
		dataSourceFields[0].setPrimaryKey(true);

		RafDataSource dataSource = new RafDataSource("/response/data/*",
				GWT.getHostPageBaseURL()
						+ ExportPresenter.exportPresenterServlet
						+ "?method=fetchAccountLinesData&type=DataSource", null,
				null, null, dataSourceFields);
		/*
		 * Set the Journal Header id(Journal Voucher) to narrow the query to the specific
		 * Account Lines (Rolled Up Journal Entry)
		 */
		HashMap<String, String> params = new HashMap<String, String>();
		if(ruJournalHeaderId != null) {
			params.put(DataNameTokens.FINROLLEDUPJOURNALENTRY_FINROLLEDUPJOURNALHEADER_RUJOURNALHEADERID, ruJournalHeaderId);
			dataSource.getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
		}
		return dataSource;
	}

}