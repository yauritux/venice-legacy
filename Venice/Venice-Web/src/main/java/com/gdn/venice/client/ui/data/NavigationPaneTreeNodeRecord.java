package com.gdn.venice.client.ui.data;

import com.smartgwt.client.widgets.tree.TreeNode;

/**
 * This is the tree node that is being used by the other data classes
 *
 */
public class NavigationPaneTreeNodeRecord extends TreeNode {

	public NavigationPaneTreeNodeRecord(String navigationId, String navigationParent, String navigationName, String pageName, String menuShortcut, String widgetId) {
		setNavigationId(navigationId);
		setNavigationParent(navigationParent);
		setNavigationName(navigationName);
		setPageName(pageName);
		setMenuShortcut(menuShortcut);
		setID(widgetId);
	}

	public void setNavigationId(String id) {
		setAttribute("NavigationId", id);
	}

	public void setNavigationParent(String navigationParent) {
		setAttribute("NavigationParent", navigationParent);
	}
	
	public String getNavigationParent() {
		return getAttributeAsString("NavigationParent");
	}

	public void setNavigationName(String navigationName) {
		setAttribute("NavigationName", navigationName);
	}
	
	public String getNavigationName() {
		return getAttributeAsString("NavigationName");
	}

	public void setPageName(String pageName) {
		setAttribute("PageName", pageName);
	}  
	
	public String getPageName() {
		return getAttributeAsString("PageName");
	}  
	
	public void setMenuShortcut(String menuShortcut) {
		setAttribute("MenuShortcut", menuShortcut);
	}  
	
	public String getMenuShortcut() {
		return getAttributeAsString("MenuShortcut");
	}  
}