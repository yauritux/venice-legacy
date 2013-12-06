package com.gdn.venice.client.app.fraud.view.handlers;

import com.gwtplatform.mvp.client.UiHandlers;

public interface FraudDashboardUiHandlers extends UiHandlers {
	void onFetchValueToFussionChart();
	public void onFectFraudProcessingHistory(final String IdMap, final String countNerxOrBack);
}

