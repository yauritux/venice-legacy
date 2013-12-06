package com.gdn.venice.client.ui.data;

import com.gdn.venice.client.app.DataWidgetNameTokens;
import com.gdn.venice.client.app.NameTokens;

/**
 * This class contains the menau data definition for the administration stack section
 *
 */
public class AdministrationNavigationPaneSectionData {
	private static NavigationPaneTreeNodeRecord[] records;

	  public static NavigationPaneTreeNodeRecord[] getRecords() {
		if (records == null) {
		  records = getNewRecords();
		}
		return records;
	  }

	  public static NavigationPaneTreeNodeRecord[] getNewRecords() {
		return new NavigationPaneTreeNodeRecord[]{
		  new NavigationPaneTreeNodeRecord("2", "1", "Administration Module", null, "AM1", DataWidgetNameTokens.ADM_ADMINISTRATIONMODULETREENODE),
		  new NavigationPaneTreeNodeRecord("3", "2", "Role, Profile, User, Group", NameTokens.adminRoleProfileUserGroup, "AM2", DataWidgetNameTokens.ADM_ROLEPROFILEUSERGROUPTREENODE),
		  new NavigationPaneTreeNodeRecord("4", "2", "Module Configuration", NameTokens.adminModuleConfiguration, "AM3", DataWidgetNameTokens.ADM_MODULECONFIGURATIONTREENODE)
		};
	  }
}
