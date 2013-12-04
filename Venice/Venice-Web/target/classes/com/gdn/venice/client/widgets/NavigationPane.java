package com.gdn.venice.client.widgets;

import java.util.HashMap;

import com.gdn.venice.client.ui.data.NavigationPaneTreeNodeRecord;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class NavigationPane extends VLayout {
	
	private static final int WEST_WIDTH = 200;

	NavigationPaneStack navigationPaneStack; 	

	public NavigationPane() {
		super();

		// initialise the layout container
		this.setWidth(WEST_WIDTH);
		this.setShowResizeBar(true);
//This is the search form for the menus etc.		
//		NavigationPaneForm navigationForm = new NavigationPaneForm();

		// initialise the navigation pane label
		navigationPaneStack = new NavigationPaneStack(); 

		// add the label to the layout container
//		this.setMembers(navigationForm, navigationPaneStack); 
		this.setMembers(navigationPaneStack);
	}	
	
	public String selectNavigationTree(String pageName) {
		SectionStackSection[] stackSections =  navigationPaneStack.getSections();
		for (int i=0;i<stackSections.length;i++) {
			if (stackSections[i] instanceof NavigationPaneStackSection) {
				NavigationPaneStackSection navigationStackSection = (NavigationPaneStackSection) stackSections[i];
				NavigationPaneTreeNodeRecord selectedRecord = navigationStackSection.getTreeNodeForPageName(pageName);
				if (selectedRecord != null) {
					navigationPaneStack.expandSection(i);
					String selectedPath = "[\""+navigationStackSection.getTreeGrid().getData().getPath(selectedRecord)+"\"]";
					navigationStackSection.getTreeGrid().setSelectedPaths(selectedPath);
					return selectedPath;
				}
			}
		}
		return null;
		
	}
	
	/**
	 * Sets the initial menu permissions for the navigation pane stack
	 * @param userPermissionBundle
	 */
	public void setInitialMenuPermissions(HashMap<String, String> userPermissionBundle){
		navigationPaneStack.setUserPermissionBundle(userPermissionBundle);
	}
	
	public void updateMenuBasedOnUserPermission(
			HashMap<String, String> userPermission,
			RecordClickHandler clickHandler) {
		navigationPaneStack.updateMenuBasedOnUserPermission(userPermission,clickHandler);
	}
	
	//This is temporary: shall be replaced by user permission!
	/*
	 * Actually this is a stupid waste of time by Henry - just disable the menus
	 */
	public void addTreeNodeClickHandler(RecordClickHandler clickHandler) {
		navigationPaneStack.addTreeNodeClickHandler(clickHandler);
		
	}

}
	
	

