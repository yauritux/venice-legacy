package com.gdn.venice.client.app.finance.view.handlers;

import java.util.HashMap;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;

public interface SalesRecordUiHandlers extends UiHandlers {
	public DataSource onExpandSalesRecordRow(String wcsOrderItemId);
	public void onRefundButtonClicked(HashMap<String, String> refundDataMap);
}

