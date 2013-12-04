package com.gdn.venice.client.app.finance.view.handlers;

import java.util.Map;

import com.gwtplatform.mvp.client.UiHandlers;

public interface FinanceDashboardUiHandlers extends UiHandlers {
	public void onFetchFussionChartRevenueStatus(Map mapPeriod,String IdPeriod) ;
	public void onFetchFussionChartRevenueHistory(String IdMap, String countNerxOrBack);
}

