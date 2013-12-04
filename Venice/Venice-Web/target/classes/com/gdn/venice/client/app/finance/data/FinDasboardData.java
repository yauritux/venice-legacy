package com.gdn.venice.client.app.finance.data;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.presenter.FinanceDashboardPresenter;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class FinDasboardData {

	public static DataSource getPayablesDue() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_VENPARTY_FULLORLEGALNAME, "Entity"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_AMOUNT, "A/P Amoun"),
				new DataSourceTextField(DataNameTokens.FINAPPAYMENT_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC, "Type"),
		};
			
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FinanceDashboardPresenter.financeDashboardPresenterServlet + "?method=fetchPayablesDueData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
		
	}
	
	public static DataSource getUnreconciledReceivable() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_WCSPAYMENTID,"Order Id"),
				//new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_VENORDERPAYMENT_AMOUNT,"Payment Amount"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_PAYMENT_AMOUNT,"Payment Amount"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_PROVIDERREPORTPAIDAMOUNT,"Paid Amount"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_REMAININGBALANCEAMOUNT,"Balance"),
				new DataSourceTextField(DataNameTokens.FINARFUNDSINRECONRECORD_FINARRECONRESULT_RECONRESULTDESC,"Status")
		};
			
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + FinanceDashboardPresenter.financeDashboardPresenterServlet + "?method=fetchUnreconciledReceivable&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		return dataSource;
		
	}
	
	
	
}
