package com.gdn.venice.client.ui.data;

import com.gdn.venice.client.app.DataWidgetNameTokens;
import com.gdn.venice.client.app.NameTokens;

/**
 * This class contains the menu data definition for the general stack section
 * 
 * 
 *
 */
public class GeneralNavigationPaneSectionData {
	private static NavigationPaneTreeNodeRecord[] records;

	  public static NavigationPaneTreeNodeRecord[] getRecords() {
		if (records == null) {
		  records = getNewRecords();
		}
		return records;
	  }

	  public static NavigationPaneTreeNodeRecord[] getNewRecords() {
		return new NavigationPaneTreeNodeRecord[]{
		  new NavigationPaneTreeNodeRecord("2", "1", "General", null, "GM1", DataWidgetNameTokens.GENERAL_GENERALMODULE_TREENODE),
		  new NavigationPaneTreeNodeRecord("3", "2", "Order Data Viewer", NameTokens.generalOrderDataViewer, "GM2", DataWidgetNameTokens.GENERAL_ORDERDATAVIEWER_TREENODE),
		  new NavigationPaneTreeNodeRecord("5", "2", "Retur Data Viewer", NameTokens.generalReturDataViewer, "GM4", DataWidgetNameTokens.GENERAL_RETURDATAVIEWER_TREENODE),
		  new NavigationPaneTreeNodeRecord("4", "2", "Party Maintenance", NameTokens.generalPartyMaintenance, "GM3", DataWidgetNameTokens.GENERAL_ORDERDATAVIEWER_TREENODE)
		};
	  }
}
