package com.gdn.venice.client.view;

import java.util.HashMap;

import com.gdn.venice.client.presenter.MainPagePresenter;
import com.gdn.venice.client.view.handlers.MainPageUiHandlers;
import com.gdn.venice.client.widgets.ContextArea;
import com.gdn.venice.client.widgets.Mastfoot;
import com.gdn.venice.client.widgets.Masthead;
import com.gdn.venice.client.widgets.NavigationPane;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.BkgndRepeat;
import com.smartgwt.client.widgets.grid.events.RecordClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.TreeGrid;

/**
 * @author Henry Chandra
 */
public class MainPageView extends ViewWithUiHandlers<MainPageUiHandlers>  implements MainPagePresenter.MyView {
	private static final int NORTH_HEIGHT = 20; // MASTHEAD_HEIGHT 
	private static final int SOUTH_HEIGHT = 20; // MASTFOOT_HEIGHT 

	/*
	 * Layout panels for the main splitters
	 */
	private VLayout mainLayout;	
	private HLayout northLayout;  
	private HLayout centerLayout;//Note that this contains the west layout and the context area
	private VLayout westLayout;
	private HLayout southLayout;

	/*
	 * Master header and master footer
	 */
	private Masthead mastHead;
	private Mastfoot mastFoot;
	private NavigationPane navigationPane;//Replaces the west layout
	private ContextArea contextArea;//Sits on the center layout
	

	@Inject
	public MainPageView(Masthead mastHead, Mastfoot mastFoot, NavigationPane navigationPane, ContextArea contextArea) {
		this.mastHead = mastHead;
		this.mastFoot = mastFoot;
		this.navigationPane = navigationPane;
		this.contextArea = contextArea;
		buildMainScreen();

		bindCustomUiHandlers();
	}
	
	private void buildMainScreen() {
		// initialise the main layout container
		mainLayout = new VLayout();
		mainLayout.setWidth100();  
		mainLayout.setHeight100();  
		
		/*
		 * This is the background image for the main page
		 */
		mainLayout.setBackgroundImage("gdn_background.jpg");
		mainLayout.setBackgroundRepeat(BkgndRepeat.NO_REPEAT);
		mainLayout.setBackgroundPosition("right bottom");

		// initialise the North layout container
		northLayout = new HLayout();  
		northLayout.setHeight(NORTH_HEIGHT); 

		VLayout headLayout = new VLayout(); 
		// add the Masthead to the nested layout container
		headLayout.addMember(mastHead);
		// add the nested layout container to the  North layout container
		northLayout.addMember(headLayout);

		// initialise the West layout container
		westLayout = navigationPane;

		// initialise the Center layout container
		centerLayout = new HLayout();
		centerLayout.setShowResizeBar(true);


		// initialise the South layout container
		southLayout = new HLayout();  
		southLayout.setHeight(SOUTH_HEIGHT);
		
		VLayout footLayout = new VLayout(); 
		// add the Mastfoot to the nested layout container
		footLayout.addMember(mastFoot);
		// add the nested layout container to the  South layout container
		southLayout.addMember(footLayout);

		// set the Navigation Pane and ContextArea as members of the South 
		// layout container 
		centerLayout.setMembers(westLayout, contextArea);  
		centerLayout.setShowResizeBar(false);
		
		
	}

	
	//This is temporary: shall be replaced by user permission!
	protected void bindCustomUiHandlers() {
		RecordClickHandler clickHandler = new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				onTreeNodeRecordClicked(event);
			}
		};
		navigationPane.addTreeNodeClickHandler(clickHandler);
		

	}

	private void onTreeNodeRecordClicked(RecordClickEvent event) {
		Record record = event.getRecord();  
		String name = record.getAttributeAsString("PageName");

		if (getUiHandlers() != null) {
			getUiHandlers().onNavigationPaneSectionClicked(((TreeGrid) event.getSource()).getSelectedPaths(), name);
		}  
		
	}

	@Override
	public Widget asWidget() {
		return mainLayout;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		
		if (slot == MainPagePresenter.TYPE_SetContextArea) {
			if (content != null) {
				if (!mainLayout.contains(northLayout)) {
					mainLayout.setMembers(northLayout, centerLayout, southLayout);
				}
				if (content instanceof RafViewLayout) {
					RafViewLayout viewLayout =  (RafViewLayout)content;
					
					//This is temporary: shall be enabled when by user permission is supported!
					getUiHandlers().onSetInSlot(viewLayout.getViewPageName());
					
					updateBreadCrumb(viewLayout.getViewPageName());
					
					GWT.log("Opening " + viewLayout.getViewPageName(), null);
					contextArea.setMembers(viewLayout);
				}
			}
		} else if (slot == MainPagePresenter.TYPE_LoginScreen) {
			mainLayout.setMembers((VLayout)content);  
		}
	}

	@Override
	public void updateBreadCrumb(String pageName) {
		String navigationTree = navigationPane.selectNavigationTree(pageName);
		if (navigationTree!=null) {
			mastHead.updateBreadCrumb(navigationTree);
		}
	}

	@Override
	public void updateSignedInUser(String user) {
		mastHead.updateSignedInUser(user);
	}

	@Override
	public void updateVersion(String version) {
		mastHead.updateVersion(version);
		
	}

	@Override
	public void updateMenuBasedOnUserPermission(
			HashMap<String, String> userPermission) {
		RecordClickHandler clickHandler = new RecordClickHandler() {
			@Override
			public void onRecordClick(RecordClickEvent event) {
				onTreeNodeRecordClicked(event);
			}
		};

		navigationPane.updateMenuBasedOnUserPermission(userPermission, clickHandler);
				
		
	}

	/**
	 * @return the navigationPane
	 */
	public NavigationPane getNavigationPane() {
		return navigationPane;
	}
	

}
