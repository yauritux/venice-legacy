package com.gdn.venice.client.app.finance.data;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.presenter.PeriodSetupPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.FieldType;

/**
 * Data class to manage the data sources for the Period Setup screen
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PeriodSetupData {

	public static DataSource getPeriodSetupData() {
		/**
		 * This method gets the data source for the period data table
		 * 
		 * @return the data source for period data
		 */
			DataSourceField[] dataSourceFields = {
					new DataSourceIntegerField(DataNameTokens.FINPERIOD_PERIODID,
							"Period Id"),
					new DataSourceTextField(
							DataNameTokens.FINPERIOD_PERIODDESC,
							"Period Description"),
					new DataSourceField(DataNameTokens.FINPERIOD_FROMDATETIME,
							FieldType.DATETIME, "Period From"),
					new DataSourceField(DataNameTokens.FINPERIOD_TODATETIME,
							FieldType.DATETIME, "Period To"),

			};
			dataSourceFields[0].setPrimaryKey(true);

			RafDataSource periodDataDs = new RafDataSource("/response/data/*",
					GWT.getHostPageBaseURL()
							+ PeriodSetupPresenter.periodSetupServlet
							+ "?method=fetchPeriodData&type=DataSource",
					GWT.getHostPageBaseURL()
							+ PeriodSetupPresenter.periodSetupServlet
							+ "?method=addPeriodData&type=DataSource",
					GWT.getHostPageBaseURL()
							+ PeriodSetupPresenter.periodSetupServlet
							+ "?method=updatePeriodData&type=DataSource",
					GWT.getHostPageBaseURL()
							+ PeriodSetupPresenter.periodSetupServlet
							+ "?method=deletePeriodData&type=DataSource",
					dataSourceFields);

			return periodDataDs;
		}
}