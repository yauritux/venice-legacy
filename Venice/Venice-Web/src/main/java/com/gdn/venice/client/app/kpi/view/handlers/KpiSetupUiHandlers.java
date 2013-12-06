package com.gdn.venice.client.app.kpi.view.handlers;

import java.util.HashMap;

import com.gwtplatform.mvp.client.UiHandlers;

public interface KpiSetupUiHandlers extends UiHandlers {

	void addKpiSetupPartySlaDataCommand(HashMap<String, String> DataMap);
	void deleteKpiSetupPartySlaDataCommand(HashMap<String, String> DataMap);
	void updateKpiSetupPartySlaDataCommand(HashMap<String, String> DataMap) ;
}
