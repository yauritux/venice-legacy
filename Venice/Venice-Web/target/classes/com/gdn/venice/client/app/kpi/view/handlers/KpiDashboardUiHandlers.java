package com.gdn.venice.client.app.kpi.view.handlers;

import java.util.HashMap;

import com.gwtplatform.mvp.client.UiHandlers;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public interface KpiDashboardUiHandlers extends UiHandlers {
	void onFetchValueBaselineFromPartyTarget(HashMap<String, String> DataMap,final ListGridRecord listGridRecord,final ListGridRecord[] record,final boolean bol) ;
}
