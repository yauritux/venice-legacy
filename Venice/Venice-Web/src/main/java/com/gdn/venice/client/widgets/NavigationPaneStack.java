package com.gdn.venice.client.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.DataWidgetNameTokens;
import com.gdn.venice.client.ui.data.AdministrationNavigationPaneSectionData;
import com.gdn.venice.client.ui.data.FinanceNavigationPaneSectionData;
import com.gdn.venice.client.ui.data.FraudNavigationPaneSectionData;
import com.gdn.venice.client.ui.data.GeneralNavigationPaneSectionData;
import com.gdn.venice.client.ui.data.KpiNavigationPaneSectionData;
import com.gdn.venice.client.ui.data.LogisticNavigationPaneSectionData;
import com.gdn.venice.client.ui.data.NavigationPaneTreeNodeRecord;
import com.gdn.venice.client.ui.data.ReservationNavigationPaneSectionData;
import com.gdn.venice.client.ui.data.TaskManagementNavigationPaneSectionData;
import com.gdn.venice.client.util.PermissionsUtil;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.grid.events.RecordClickHandler;
import com.smartgwt.client.widgets.layout.SectionStack;

public class NavigationPaneStack extends SectionStack {
	
	static NavigationPaneStackSection taskManagementSection;
	static NavigationPaneStackSection fraudModuleSection;
	static NavigationPaneStackSection logisticsModuleSection;
	static NavigationPaneStackSection financeModuleSection;
	static NavigationPaneStackSection kpiModuleSection;
	static NavigationPaneStackSection administrationModuleSection;
	static NavigationPaneStackSection reservationModuleSection;
	static NavigationPaneStackSection generalModuleSection;
	List<NavigationPaneStackSection> navigationPaneStackSectionArrayList;
	
	boolean autorizationReady = false;
	
	String currentPageName = null;

	/*
	 * This is used to extract the menu permissions
	 */
	private HashMap<String, String> userPermissionBundle;

	public NavigationPaneStack() {
		super();
		
		// initialise the Section Stack
		this.setWidth100();
		this.setVisibilityMode(VisibilityMode.MUTEX);
		this.setShowExpandControls(false);
		this.setAnimateSections(true);	
		this.setID(DataWidgetNameTokens.VENICE_NAVIGATIONPANESTACK);
		
		navigationPaneStackSectionArrayList = new ArrayList<NavigationPaneStackSection>();

		// initialise the Task Management section 
		taskManagementSection = new NavigationPaneStackSection("Task Management", DataWidgetNameTokens.VENICE_TASKMANAGEMENTSECTION, TaskManagementNavigationPaneSectionData.getRecords());
		taskManagementSection.setExpanded(true);
		navigationPaneStackSectionArrayList.add(taskManagementSection);

		// initialise the Fraud Module section 
		fraudModuleSection = new NavigationPaneStackSection("Fraud Module", DataWidgetNameTokens.VENICE_FRAUDMODULESECTION, FraudNavigationPaneSectionData.getRecords());
		fraudModuleSection.setExpanded(true);
		navigationPaneStackSectionArrayList.add(fraudModuleSection);

		// initialise the Logistic Module section 
		logisticsModuleSection = new NavigationPaneStackSection("Logistics Module", DataWidgetNameTokens.VENICE_LOGISTICSMODULESECTION, LogisticNavigationPaneSectionData.getRecords());
		logisticsModuleSection.setExpanded(true);
		navigationPaneStackSectionArrayList.add(logisticsModuleSection);

		// initialise the Finance Module section 
		financeModuleSection = new NavigationPaneStackSection("Finance Module", DataWidgetNameTokens.VENICE_FINANCEMODULESECTION, FinanceNavigationPaneSectionData.getRecords()); 
		financeModuleSection.setExpanded(true);
		navigationPaneStackSectionArrayList.add(financeModuleSection);
		
		
		// initialise the KPI Module section 
		kpiModuleSection = new NavigationPaneStackSection("KPI Module", DataWidgetNameTokens.VENICE_KPIMODULESECTION, KpiNavigationPaneSectionData.getRecords());
		kpiModuleSection.setExpanded(true);
		navigationPaneStackSectionArrayList.add(kpiModuleSection);

		// initialise the Administration Module section 
		administrationModuleSection = new NavigationPaneStackSection("Administration Module", DataWidgetNameTokens.VENICE_ADMINISTRATIONMODULESECTION, AdministrationNavigationPaneSectionData.getRecords());
		administrationModuleSection.setExpanded(true);
		navigationPaneStackSectionArrayList.add(administrationModuleSection);
		
		// initialise the Administration Module section 
		reservationModuleSection = new NavigationPaneStackSection("Reservation Module", DataWidgetNameTokens.VENICE_RESERVATIONMODULESECTION, ReservationNavigationPaneSectionData.getRecords());
		reservationModuleSection.setExpanded(true);
		navigationPaneStackSectionArrayList.add(reservationModuleSection);
		
		// initialise the General Module section 
		generalModuleSection = new NavigationPaneStackSection("General", DataWidgetNameTokens.VENICE_GENERALMODULESECTION, GeneralNavigationPaneSectionData.getRecords());
		generalModuleSection.setExpanded(true);
		navigationPaneStackSectionArrayList.add(generalModuleSection);

		

		//This is temporary: shall be replaced by user permission!
		// add the sections to the Section Stack
		addSection(taskManagementSection);  
		addSection(reservationModuleSection);
		addSection(fraudModuleSection);  
		addSection(logisticsModuleSection);
		addSection(financeModuleSection); 
		addSection(kpiModuleSection); 
		addSection(administrationModuleSection);		
		addSection(generalModuleSection);
		
		expandSection(0);
		
	}
	
	public void updateMenuBasedOnUserPermission(
			HashMap<String, String> userPermission, 
			RecordClickHandler clickHandler) {
		
		
		/*
		 * This is where the menus can be enabled and disabled.
		 * Need a utility class to call the RafPermissionSessionEJB class
		 * and get the permissions for the menu from the DB
		 * based on the canonical name that is equal to the menuCode
		 * 
		 * --See below.
		 */
		for(NavigationPaneStackSection section:navigationPaneStackSectionArrayList){
			for(int i = 0; i < section.getSectionData().length; ++i){
				
				/*
				 * Determine if the user has any permission 
				 * on the menu. If they have then enable it
				 * else disable it.
				 */
				NavigationPaneTreeNodeRecord node = section.getSectionData()[i];
				if(DataNameTokens.GLOBAL_SECURITY_ENABLED){
					node.setEnabled(false);
				}else{
					node.setEnabled(true);
				}
				
				if(userPermissionBundle != null && userPermissionBundle.containsKey(node.getMenuShortcut())){
					Integer permissions = new Integer(userPermissionBundle.get(node.getMenuShortcut()));
					if(PermissionsUtil.hasAny(permissions)){
						node.setEnabled(true);
					}
				}
			}
		}

		
		String navigationPaneStackSectionClass = "com.gdn.venice.client.widgets.NavigationPaneStackSection";

		if (userPermission.containsKey(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_TASKMANAGEMENTSECTION)) {
			long permission = new Long(userPermission.get(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_TASKMANAGEMENTSECTION)).longValue();
			if (PermissionsUtil.hasExecute(new Integer(new Long(permission).intValue()))) {
				if (getSection(taskManagementSection.getID()) == null ){
					addSection(taskManagementSection);
					taskManagementSection.getTreeGrid().addRecordClickHandler(clickHandler);
				}
			}
		}
		if (userPermission.containsKey(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_FRAUDMODULESECTION)) {
			long permission = new Long(userPermission.get(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_FRAUDMODULESECTION)).longValue();
			if (PermissionsUtil.hasExecute(new Integer(new Long(permission).intValue()))) {
				if (getSection(fraudModuleSection.getID()) == null ){
					addSection(fraudModuleSection);
					fraudModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
				}
			}
		}
		if (userPermission.containsKey(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_LOGISTICSMODULESECTION)) {
			long permission = new Long(userPermission.get(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_LOGISTICSMODULESECTION)).longValue();
			if (PermissionsUtil.hasExecute(new Integer(new Long(permission).intValue()))) {
				if (getSection(logisticsModuleSection.getID()) == null ){
					addSection(logisticsModuleSection);
					logisticsModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
				}
			}
		}
		if (userPermission.containsKey(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_FINANCEMODULESECTION)) {
			long permission = new Long(userPermission.get(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_FINANCEMODULESECTION)).longValue();
			if (PermissionsUtil.hasExecute(new Integer(new Long(permission).intValue()))) {
				if (getSection(financeModuleSection.getID()) == null ){
					addSection(financeModuleSection);
					financeModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
				}
			}
		}
		if (userPermission.containsKey(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_KPIMODULESECTION)) {
			long permission = new Long(userPermission.get(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_KPIMODULESECTION)).longValue();
			if (PermissionsUtil.hasExecute(new Integer(new Long(permission).intValue()))) {
				if (getSection(kpiModuleSection.getID()) == null ){
					addSection(kpiModuleSection);
					kpiModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
				}
			}
		}
		if (userPermission.containsKey(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_ADMINISTRATIONMODULESECTION)) {
			long permission = new Long(userPermission.get(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_ADMINISTRATIONMODULESECTION)).longValue();
			if (PermissionsUtil.hasExecute(new Integer(new Long(permission).intValue()))) {
				if (getSection(administrationModuleSection.getID()) == null ){
					addSection(administrationModuleSection);
					administrationModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
				}
			}
		}
//		if (userPermission.containsKey(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_ADMINISTRATIONMODULESECTION)) {
//			long permission = new Long(userPermission.get(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_ADMINISTRATIONMODULESECTION)).longValue();
//			if (PermissionsUtil.hasExecute(new Integer(new Long(permission).intValue()))) {
//				if (getSection(reservationModuleSection.getID()) == null ){
//					addSection(reservationModuleSection);
//					reservationModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
//				}
//			}
//		}

		if (userPermission.containsKey(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_GENERALMODULESECTION)) {
			long permission = new Long(userPermission.get(navigationPaneStackSectionClass + "." + DataWidgetNameTokens.VENICE_GENERALMODULESECTION)).longValue();
			if (PermissionsUtil.hasExecute(new Integer(new Long(permission).intValue()))) {
				if (getSection(generalModuleSection.getID()) == null ){
					addSection(generalModuleSection);
					generalModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
				}
			}
		}
	}
	
	//This is temporary: shall be replaced by user permission!
	public void addTreeNodeClickHandler(RecordClickHandler clickHandler) {
		taskManagementSection.getTreeGrid().addRecordClickHandler(clickHandler);
		fraudModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
		logisticsModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
		financeModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
		kpiModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
		administrationModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
		reservationModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
		generalModuleSection.getTreeGrid().addRecordClickHandler(clickHandler);
	}

	/**
	 * @return the userPermissionBundle
	 */
	public HashMap<String, String> getUserPermissionBundle() {
		return userPermissionBundle;
	}

	/**
	 * @param userPermissionBundle the userPermissionBundle to set
	 */
	public void setUserPermissionBundle(HashMap<String, String> userPermissionBundle) {
		this.userPermissionBundle = userPermissionBundle;
	}
	


}
