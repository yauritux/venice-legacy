package com.gdn.venice.client.app.fraud.view.handlers;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.data.DataSource;

public interface FraudCalculateUiHandlers extends UiHandlers {
	public DataSource onShowFraudCalculateOrder(String dateParam);
}