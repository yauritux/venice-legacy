package com.gdn.venice.client.ui.data;

import com.gdn.venice.client.app.DataWidgetNameTokens;
import com.gdn.venice.client.app.NameTokens;

/**
 * This class contains the menu data definition for the KPI stack section
 *
 */
public class KpiNavigationPaneSectionData {
	private static NavigationPaneTreeNodeRecord[] records;

	  public static NavigationPaneTreeNodeRecord[] getRecords() {
		if (records == null) {
		  records = getNewRecords();
		}
		return records;
	  }

	  public static NavigationPaneTreeNodeRecord[] getNewRecords() {
		return new NavigationPaneTreeNodeRecord[]{
		  new NavigationPaneTreeNodeRecord("2", "1", "KPI Module", null, "KM1", DataWidgetNameTokens.KPI_KPIMODULETREENODE),
		  new NavigationPaneTreeNodeRecord("3", "2", "KPI Setup", NameTokens.kpiSetupPage, "KM2", DataWidgetNameTokens.KPI_KPISETUPTREENODE),
		  new NavigationPaneTreeNodeRecord("4", "2", "KPI Dashboard", NameTokens.kpiDashboardPage, "KM3", DataWidgetNameTokens.KPI_KPIDASHBOARDTREENODE),
		  new NavigationPaneTreeNodeRecord("5", "2", "KPI Detail Viewer", NameTokens.kpiDetailViewerPage, "KM4", DataWidgetNameTokens.KPI_KPIDETAILVIEWERTREENODE)
		};
	  }
}
