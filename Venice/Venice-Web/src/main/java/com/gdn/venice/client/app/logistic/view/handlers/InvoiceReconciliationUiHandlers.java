package com.gdn.venice.client.app.logistic.view.handlers;

import java.util.List;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;

public interface InvoiceReconciliationUiHandlers extends UiHandlers {
	public DataSource onExpandAirwayBillRow(String airwayBillId);
	public void onSubmitForApproval(List<String> airwayBillIds);
	public DataSource onShowMerchantPickUpDetail(String airwayBillId);
	public void onFetchComboBoxData(); 
}
