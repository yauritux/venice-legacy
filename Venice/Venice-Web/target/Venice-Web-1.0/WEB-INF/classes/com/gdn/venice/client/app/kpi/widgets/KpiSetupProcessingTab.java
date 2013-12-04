package com.gdn.venice.client.app.kpi.widgets;

import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class KpiSetupProcessingTab extends Tab{
	
	VLayout kpiSetupLayout;
	VLayout kpiSetupDetailLayout;
	IButton buttonProcessDetailKpiSetup;

	ListGrid kpiSetupListGrid;
	
	public KpiSetupProcessingTab(String title, ClickHandler buttonProcessDetailKpiSetupClickHandler) {
		super(title);
		
		VLayout kpiSetupProcessingLayout = new VLayout();
			
		kpiSetupDetailLayout = new VLayout();
		kpiSetupLayout = new VLayout();
		
		
		kpiSetupProcessingLayout.setMembers(kpiSetupDetailLayout,kpiSetupLayout);
		
		setPane(kpiSetupProcessingLayout);
	}
	
	public VLayout getKpiSetupDetailLayout() {
		return kpiSetupDetailLayout;
	}
	
	public VLayout getKpiSetupLayout() {
		return kpiSetupLayout;
	}
	
	public void setButtonProcessDetailKpiSetupDisabled(boolean eneble) {
		buttonProcessDetailKpiSetup.setDisabled(eneble);
		
	}
}
