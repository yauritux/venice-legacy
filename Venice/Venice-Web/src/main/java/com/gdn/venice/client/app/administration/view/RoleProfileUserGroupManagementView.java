package com.gdn.venice.client.app.administration.view;

import java.util.Map;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.administration.data.AdministrationData;
import com.gdn.venice.client.app.administration.presenter.RoleProfileUserGroupManagementPresenter;
import com.gdn.venice.client.app.administration.view.handlers.RoleProfileUserGroupManagementUiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.tree.Tree;

/**
 * View for Role Profile User Group Management
 * 
 * @author Roland
 */

public class RoleProfileUserGroupManagementView extends
ViewWithUiHandlers<RoleProfileUserGroupManagementUiHandlers> implements
RoleProfileUserGroupManagementPresenter.MyView {
	@SuppressWarnings("unused")
	private static final int TITLE_HEIGHT = 20;  
	private static final int LIST_HEIGHT = 140;
	
	RafViewLayout roleProfileUserGroupLayout;
	String status="";
	VLayout profileDetailLayout;
	VLayout userDetailLayout;
	VLayout roleDetailLayout;
	VLayout groupDetailLayout;
	
	Tree roleTree;

	ListGrid profileListGrid = new ListGrid();
	ListGrid profileDetailListGrid = new ListGrid();
	ListGrid userListGrid = new ListGrid();
	ListGrid groupListGrid = new ListGrid();
	ListGrid groupDetailListGrid = new ListGrid();
	ListGrid userDetailGroupListGrid = new ListGrid();
	ListGrid userDetailRoleListGrid = new ListGrid();
	ListGrid roleListGrid = new ListGrid();
	ListGrid roleDetailUserListGrid = new ListGrid();
	ListGrid roleDetailProfileListGrid = new ListGrid();

	@Inject
	public RoleProfileUserGroupManagementView() {
		roleProfileUserGroupLayout = new RafViewLayout();
		
		TabSet roleProfileUserGroupTabSet = new TabSet();
		roleProfileUserGroupTabSet.setTabBarPosition(Side.TOP);
		roleProfileUserGroupTabSet.setWidth100();
		roleProfileUserGroupTabSet.setHeight100();
		
		roleProfileUserGroupTabSet.addTab(buildRoleTab());
		roleProfileUserGroupTabSet.addTab(buildProfileTab());
		roleProfileUserGroupTabSet.addTab(buildUserTab());
		roleProfileUserGroupTabSet.addTab(buildGroupTab());
		
		roleProfileUserGroupLayout.setMembers(roleProfileUserGroupTabSet);

		bindCustomUiHandlers();
	}

	private Tab buildUserTab() {
		Tab userTab = new Tab("User");		
		VLayout userLayout = new VLayout();
		
		VLayout userListLayout = new VLayout();
		userListLayout.setHeight(LIST_HEIGHT);
		userListLayout.setShowResizeBar(true);
		
		ToolStrip userToolStrip = new ToolStrip();		
		userToolStrip.setWidth100();
		
		ToolStripButton addButton = new ToolStripButton();  
		addButton.setIcon("[SKIN]/icons/user_add.png");  
		addButton.setTooltip("Add New User");
		addButton.setTitle("Add");
		
		ToolStripButton removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]/icons/user_delete.png");  
		removeButton.setTooltip("Delete Current User");
		removeButton.setTitle("Remove");
		
		userToolStrip.addButton(addButton);
		userToolStrip.addButton(removeButton);		
		
		userListGrid.setAutoFetchData(true);
		userListGrid.setCanEdit(true);
		userListGrid.setShowFilterEditor(true);
		userListGrid.setSelectionType(SelectionStyle.SIMPLE);
		userListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		userListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		userListGrid.setShowRowNumbers(true);
		
		userListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {		
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = userListGrid.getSelection();
				if (selectedRecords.length==1) {
					Record record = selectedRecords[0];
					showUserDetail(record.getAttributeAsString(DataNameTokens.RAFUSER_USERID));
				} else {
					HTMLFlow userDetailFlow = new HTMLFlow();
					userDetailFlow.setAlign(Alignment.CENTER);
					userDetailFlow.setWidth100();
					if (selectedRecords.length==0) {
						userDetailFlow.setContents("<h2 align=\"center\">Please select user to show the user detail</h2>");
					} else if (selectedRecords.length>1) {
						userDetailFlow.setContents("<h2 align=\"center\">More than one user selected, please select only one user to show the user detail</h2>");
					}
					userDetailLayout.setMembers(userDetailFlow);
				}
			}
		});
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				userListGrid.startEditingNew();
			}
		});		

		userListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				userListGrid.saveAllEdits();
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}	
				refreshUserData();			
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){							
							userListGrid.removeSelectedData();
							refreshUserData();
							SC.say("Data Removed");
						}
					}
				});
			}
		});
		
		userListLayout.setMembers(userToolStrip, userListGrid);		
		userDetailLayout = new VLayout();		
		userLayout.setMembers(userListLayout, userDetailLayout);		
		userTab.setPane(userLayout);		
		return userTab;
	}
	
	private void showUserDetail(final String userId) {
		VLayout layout = new VLayout();
		
		HLayout userDetailHLayout = new HLayout();		
		VLayout userDetailLeftLayout = new VLayout();		
		DynamicForm userDetailForm = new DynamicForm();
				
		ToolStrip assignedGroupToolStrip = new ToolStrip();
		assignedGroupToolStrip.setWidth100();
		
		ToolStripButton addAssignedGroupButton = new ToolStripButton();  
		addAssignedGroupButton.setIcon("[SKIN]/icons/business_users_add.png");  
		addAssignedGroupButton.setTooltip("Add");
		addAssignedGroupButton.setTitle("Add");
		
		ToolStripButton removeAssignedGroupButton = new ToolStripButton();  
		removeAssignedGroupButton.setIcon("[SKIN]/icons/business_users_delete.png");  
		removeAssignedGroupButton.setTooltip("Remove");
		removeAssignedGroupButton.setTitle("Remove");
		
		assignedGroupToolStrip.addButton(addAssignedGroupButton);
		assignedGroupToolStrip.addButton(removeAssignedGroupButton);
		
		addAssignedGroupButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				userDetailGroupListGrid.startEditingNew();
			}
		});
		
		userDetailGroupListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				userDetailGroupListGrid.saveAllEdits();
					
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}
				refreshUserDetailGroupData();
			}
		});
		
		removeAssignedGroupButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){				
							userDetailGroupListGrid.removeSelectedData();
							SC.say("Data Removed");
							refreshUserDetailGroupData();
						}
					}
				});
			}
		});
		
		//User detail group listgrid
		final DataSource userDetailGroupDataSource=AdministrationData.getUserDetailGroupData(userId);
		
		//Request User Combo
		RPCRequest requestUser = new RPCRequest();
		requestUser.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchUserComboBoxData&type=RPC");
		requestUser.setHttpMethod("POST");
		requestUser.setUseSimpleHttp(true);
		requestUser.setShowPrompt(false);
		RPCManager.sendRequest(requestUser, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseUser = rawData.toString();
						String xmlDataUser = rpcResponseUser;
						final Map<String, String> userMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataUser));
						
						//Request Group Combo
						RPCRequest requestGroup = new RPCRequest();
						requestGroup.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchGroupComboBoxData&type=RPC");
						requestGroup.setHttpMethod("POST");
						requestGroup.setUseSimpleHttp(true);
						requestGroup.setShowPrompt(false);
						
						RPCManager.sendRequest(requestGroup, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponseGroup = rawData.toString();
										String xmlDataGroup = rpcResponseGroup;
										final Map<String, String> groupMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataGroup));

										userDetailGroupListGrid.setDataSource(userDetailGroupDataSource);
										userDetailGroupListGrid.setFields(
												new ListGridField(DataNameTokens.RAFUSER_RAFUSERGROUP_USERID),
												new ListGridField(DataNameTokens.RAFUSER_RAFUSERGROUP_GROUPID)
										);										
										userDetailGroupListGrid.getField(DataNameTokens.RAFUSER_RAFUSERGROUP_USERID).setValueMap(userMap);
										userDetailGroupListGrid.getField(DataNameTokens.RAFUSER_RAFUSERGROUP_GROUPID).setValueMap(groupMap);
										userDetailGroupListGrid.getField(DataNameTokens.RAFUSER_RAFUSERGROUP_USERID).setCanEdit(false);
										userDetailGroupListGrid.getField(DataNameTokens.RAFUSER_RAFUSERGROUP_USERID).setDefaultValue(userId);
										userDetailGroupListGrid.getField(DataNameTokens.RAFUSER_RAFUSERGROUP_GROUPID).setRequired(true);
										userDetailGroupListGrid.fetchData();
									}
						});
				}
		});

		userDetailGroupListGrid.setCanEdit(true);
		userDetailGroupListGrid.setSelectionType(SelectionStyle.SIMPLE);
		userDetailGroupListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		userDetailGroupListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		userDetailGroupListGrid.setShowRowNumbers(true);
		
		ToolStrip assignedRoleToolStrip = new ToolStrip();
		assignedRoleToolStrip.setWidth100();
		
		ToolStripButton addAssignedRoleButton = new ToolStripButton();  
		addAssignedRoleButton.setIcon("[SKIN]/icons/business_user_add.png");  
		addAssignedRoleButton.setTooltip("Add");
		addAssignedRoleButton.setTitle("Add");
		
		ToolStripButton removeAssignedRoleButton = new ToolStripButton();  
		removeAssignedRoleButton.setIcon("[SKIN]/icons/business_user_delete.png");  
		removeAssignedRoleButton.setTooltip("Remove");
		removeAssignedRoleButton.setTitle("Remove");
		
		assignedRoleToolStrip.addButton(addAssignedRoleButton);
		assignedRoleToolStrip.addButton(removeAssignedRoleButton);
		
		addAssignedRoleButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				userDetailRoleListGrid.startEditingNew();
			}
		});
		
		userDetailRoleListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				userDetailRoleListGrid.saveAllEdits();
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}
				refreshUserDetailRoleData();
			}
		});
		
		removeAssignedRoleButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){				
							userDetailRoleListGrid.removeSelectedData();
							SC.say("Data Removed");
							refreshUserDetailRoleData();
						}
					}
				});
			}
		});		
		
		//User detail role listgrid
		final DataSource userDetailRoleDataSource=AdministrationData.getUserDetailRoleData(userId);

		//Request User Combo
		requestUser = new RPCRequest();
		requestUser.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchUserComboBoxData&type=RPC");
		requestUser.setHttpMethod("POST");
		requestUser.setUseSimpleHttp(true);
		requestUser.setShowPrompt(false);
		RPCManager.sendRequest(requestUser, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseUser = rawData.toString();
						String xmlDataUser = rpcResponseUser;
						final Map<String, String> userMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataUser));
						
						//Request Role Combo
						RPCRequest requestRole = new RPCRequest();
						requestRole.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchRoleComboBoxData&type=RPC");
						requestRole.setHttpMethod("POST");
						requestRole.setUseSimpleHttp(true);
						requestRole.setShowPrompt(false);
						
						RPCManager.sendRequest(requestRole, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponseRole = rawData.toString();
										String xmlDataRole = rpcResponseRole;
										final Map<String, String> roleMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataRole));

										userDetailRoleListGrid.setDataSource(userDetailRoleDataSource);
										userDetailRoleListGrid.setFields(
												new ListGridField(DataNameTokens.RAFUSER_RAFUSERROLES_USERID),
												new ListGridField(DataNameTokens.RAFUSER_RAFUSERROLES_ROLEID)
										);										
										userDetailRoleListGrid.getField(DataNameTokens.RAFUSER_RAFUSERROLES_USERID).setValueMap(userMap);
										userDetailRoleListGrid.getField(DataNameTokens.RAFUSER_RAFUSERROLES_ROLEID).setValueMap(roleMap);
										userDetailRoleListGrid.getField(DataNameTokens.RAFUSER_RAFUSERROLES_USERID).setCanEdit(false);
										userDetailRoleListGrid.getField(DataNameTokens.RAFUSER_RAFUSERROLES_USERID).setDefaultValue(userId);
										userDetailRoleListGrid.getField(DataNameTokens.RAFUSER_RAFUSERROLES_ROLEID).setRequired(true);
										userDetailRoleListGrid.fetchData();
									}
						});
				}
		});

		userDetailRoleListGrid.setCanEdit(true);
		userDetailRoleListGrid.setSelectionType(SelectionStyle.SIMPLE);
		userDetailRoleListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		userDetailRoleListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		userDetailRoleListGrid.setShowRowNumbers(true);
		
		userDetailLeftLayout.setMembers(userDetailForm, assignedGroupToolStrip, userDetailGroupListGrid, assignedRoleToolStrip, userDetailRoleListGrid);
		userDetailHLayout.setMembersMargin(5);
		userDetailHLayout.setMembers(userDetailLeftLayout);
		layout.setMembers(userDetailHLayout);
		userDetailLayout.setMembers(layout);
	}
	
	private Tab buildProfileTab() {
		Tab profileTab = new Tab("Profile");
		
		HLayout profileLayout = new HLayout();		
		VLayout profileListLayout = new VLayout();
		profileListLayout.setWidth("30%");
		profileListLayout.setShowResizeBar(true);
		
		ToolStrip profileToolStrip = new ToolStrip();		
		profileToolStrip.setWidth100();
		
		ToolStripButton addButton = new ToolStripButton();  
		addButton.setIcon("[SKIN]/icons/she_user_add.png");  
		addButton.setTooltip("Add New Profile");
		addButton.setTitle("Add");
		
		ToolStripButton removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]/icons/she_user_remove.png");  
		removeButton.setTooltip("Delete Current Profile");
		removeButton.setTitle("Remove");
		
		profileToolStrip.addButton(addButton);
		profileToolStrip.addButton(removeButton);
		
		profileListGrid.setAutoFetchData(true);
		profileListGrid.setCanEdit(true);
		profileListGrid.setShowFilterEditor(true);
		profileListGrid.setSelectionType(SelectionStyle.SIMPLE);
		profileListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		profileListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		profileListGrid.setShowRowNumbers(true);

		profileListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {		
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = profileListGrid.getSelection();
				if (selectedRecords.length==1) {
					Record record = selectedRecords[0];
					showProfileDetail(record.getAttributeAsString(DataNameTokens.RAFPROFILE_PROFILEID));
				} else {
					HTMLFlow profileDetailFlow = new HTMLFlow();
					profileDetailFlow.setAlign(Alignment.CENTER);
					profileDetailFlow.setWidth100();
					if (selectedRecords.length==0) {
						profileDetailFlow.setContents("<h2 align=\"center\">Please select profile to show the profile detail</h2>");
					} else if (selectedRecords.length>1) {
						profileDetailFlow.setContents("<h2 align=\"center\">More than one profile selected, please select only one profile to show the profile detail</h2>");
					}
					profileDetailLayout.setMembers(profileDetailFlow);
				}
			}
		});
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				profileListGrid.startEditingNew();
			}
		});		

		profileListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				profileListGrid.saveAllEdits();
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}	
				refreshProfileData();			
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){							
							profileListGrid.removeSelectedData();
							refreshProfileData();
							SC.say("Data Removed");
						}
					}
				});
			}
		});
		
		profileListLayout.setMembers(profileToolStrip, profileListGrid);		
		profileDetailLayout = new VLayout();		
		profileLayout.setMembers(profileListLayout, profileDetailLayout);
		profileTab.setPane(profileLayout);
		
		return profileTab;
	}
	
	private void showProfileDetail(final String profileId) {
		VLayout layout = new VLayout();		
		ToolStrip profileDetailToolStrip = new ToolStrip();
		profileDetailToolStrip.setWidth100();
		
		ToolStripButton addButton = new ToolStripButton();  
		addButton.setIcon("[SKIN]/icons/user_add.png");  
		addButton.setTooltip("Add");
		addButton.setTitle("Add");
		
		ToolStripButton removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]/icons/user_delete.png");  
		removeButton.setTooltip("Delete");
		removeButton.setTitle("Remove");
		
		profileDetailToolStrip.addButton(addButton);
		profileDetailToolStrip.addButton(removeButton);
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				profileDetailListGrid.startEditingNew();
			}
		});
		
		profileDetailListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				profileDetailListGrid.saveAllEdits();
					
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}
				refreshProfileDetailData();
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){				
							profileDetailListGrid.removeSelectedData();
							SC.say("Data Removed");
							refreshProfileDetailData();
						}
					}
				});
			}
		});

		//Profile detail listgrid
		final DataSource profileDetailDataSource=AdministrationData.getProfileDetailData(profileId);
		
		//Request Screen Combo
		RPCRequest requestScreen = new RPCRequest();
		requestScreen.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchScreenComboBoxData&type=RPC");
		requestScreen.setHttpMethod("POST");
		requestScreen.setUseSimpleHttp(true);
		requestScreen.setShowPrompt(false);
		RPCManager.sendRequest(requestScreen, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseScreen = rawData.toString();
						String xmlDataScreen = rpcResponseScreen;
						final Map<String, String> screenMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataScreen));
						
						//Request Permission Type Combo
						RPCRequest requestPermissionType = new RPCRequest();
						requestPermissionType.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchPermissionComboBoxData&type=RPC");
						requestPermissionType.setHttpMethod("POST");
						requestPermissionType.setUseSimpleHttp(true);
						requestPermissionType.setShowPrompt(false);
						
						RPCManager.sendRequest(requestPermissionType, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponsePermissionType = rawData.toString();
										String xmlDataPermissionType = rpcResponsePermissionType;
										final Map<String, String> permissionTypeMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataPermissionType));

										//Request Profile Name Combo
										RPCRequest requestProfile = new RPCRequest();
										requestProfile.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchProfileComboBoxData&type=RPC");
										requestProfile.setHttpMethod("POST");
										requestProfile.setUseSimpleHttp(true);
										requestProfile.setShowPrompt(false);
										
										RPCManager.sendRequest(requestProfile, 
												new RPCCallback () {
													public void execute(RPCResponse response,
															Object rawData, RPCRequest request) {
														String rpcResponseProfile = rawData.toString();
														String xmlDataProfile = rpcResponseProfile;
														final Map<String, String> profileMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataProfile));
														
														profileDetailListGrid.setDataSource(profileDetailDataSource);
														profileDetailListGrid.setFields(
																new ListGridField(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID),
																new ListGridField(DataNameTokens.RAFPROFILEPERMISSION_APPLICATIONOBJECTID),
																new ListGridField(DataNameTokens.RAFPROFILEPERMISSION_PERMISSIONTYPEID)
														);
														profileDetailListGrid.getField(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID).setValueMap(profileMap);
														profileDetailListGrid.getField(DataNameTokens.RAFPROFILEPERMISSION_APPLICATIONOBJECTID).setValueMap(screenMap);
														profileDetailListGrid.getField(DataNameTokens.RAFPROFILEPERMISSION_PERMISSIONTYPEID).setValueMap(permissionTypeMap);
														profileDetailListGrid.getField(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID).setCanEdit(false);
														profileDetailListGrid.getField(DataNameTokens.RAFPROFILEPERMISSION_PROFILEID).setDefaultValue(profileId);
														profileDetailListGrid.getField(DataNameTokens.RAFPROFILEPERMISSION_APPLICATIONOBJECTID).setRequired(true);
														profileDetailListGrid.getField(DataNameTokens.RAFPROFILEPERMISSION_PERMISSIONTYPEID).setRequired(true);
														profileDetailListGrid.fetchData();
													}
										});									
									}
						});
				}
		});

		profileDetailListGrid.setCanEdit(true);
		profileDetailListGrid.setSelectionType(SelectionStyle.SIMPLE);
		profileDetailListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		profileDetailListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		profileDetailListGrid.setShowRowNumbers(true);
		
		layout.setMembers(profileDetailToolStrip, profileDetailListGrid);
		profileDetailLayout.setMembers(layout);
	}

	private Tab buildRoleTab() {
		Tab roleTab = new Tab("Role");		
		HLayout roleLayout = new HLayout();
		
		VLayout roleTreeLayout = new VLayout();
		roleTreeLayout.setWidth("30%");
		roleTreeLayout.setShowResizeBar(true);
		
		ToolStrip roleToolStrip = new ToolStrip();		
		roleToolStrip.setWidth100();
		
		ToolStripButton addButton = new ToolStripButton();  
		addButton.setIcon("[SKIN]/icons/business_user_add.png");  
		addButton.setTooltip("Add New Role");
		addButton.setTitle("Add");
		
		ToolStripButton removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]/icons/business_user_delete.png");  
		removeButton.setTooltip("Delete Current Role");
		removeButton.setTitle("Remove");
		
		roleToolStrip.addButton(addButton);
		roleToolStrip.addButton(removeButton);

		roleListGrid.setAutoFetchData(true);
		roleListGrid.setCanEdit(true);
		roleListGrid.setShowFilterEditor(true);
		roleListGrid.setSelectionType(SelectionStyle.SIMPLE);
		roleListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		roleListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		roleListGrid.setShowRowNumbers(true);
		
		roleListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {		
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = roleListGrid.getSelection();

				if (selectedRecords.length==1) {
					Record record = selectedRecords[0];
					showRoleDetail(record.getAttributeAsString(DataNameTokens.RAFROLE_ROLEID));
				} else {
					HTMLFlow roleDetailFlow = new HTMLFlow();
					roleDetailFlow.setAlign(Alignment.CENTER);
					roleDetailFlow.setWidth100();
					if (selectedRecords.length==0) {
						roleDetailFlow.setContents("<h2 align=\"center\">Please select role to show the role detail</h2>");
					} else if (selectedRecords.length>1) {
						roleDetailFlow.setContents("<h2 align=\"center\">More than one role selected, please select only one role to show the role detail</h2>");
					}
					roleDetailLayout.setMembers(roleDetailFlow);
				}
			}
		});
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				roleListGrid.startEditingNew();
			}
		});		

		roleListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				roleListGrid.saveAllEdits();
				getRoleParentComboBox();
				refreshRoleData();
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}			
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){							
							roleListGrid.removeSelectedData();
							getRoleParentComboBox();
							refreshRoleData();
							SC.say("Data Removed");
						}
					}
				});
			}
		});

		roleTreeLayout.setMembers(roleToolStrip, roleListGrid);		
		roleDetailLayout = new VLayout();		
		roleLayout.setMembers(roleTreeLayout, roleDetailLayout);		
		roleTab.setPane(roleLayout);
		
		return roleTab;
	}
	
	private void showRoleDetail(final String roleId) {
		ToolStrip roleDetailToolStrip = new ToolStrip();
		roleDetailToolStrip.setWidth100();
		
		ToolStrip profilesToolStrip = new ToolStrip();
		profilesToolStrip.setWidth100();
		
		ToolStripButton addProfilesButton = new ToolStripButton();  
		addProfilesButton.setIcon("[SKIN]/icons/she_user_add.png");  
		addProfilesButton.setTooltip("Add Profile");
		addProfilesButton.setTitle("Add");
		
		ToolStripButton removeProfilesButton = new ToolStripButton();  
		removeProfilesButton.setIcon("[SKIN]/icons/she_user_remove.png");  
		removeProfilesButton.setTooltip("Remove Profile");
		removeProfilesButton.setTitle("Remove");
		
		profilesToolStrip.addButton(addProfilesButton);
		profilesToolStrip.addButton(removeProfilesButton);
		
		addProfilesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				roleDetailProfileListGrid.startEditingNew();
			}
		});
		
		roleDetailProfileListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				roleDetailProfileListGrid.saveAllEdits();
					
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}
				refreshRoleDetailProfileData();
			}
		});
		
		removeProfilesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){				
							roleDetailProfileListGrid.removeSelectedData();
							SC.say("Data Removed");
							refreshRoleDetailProfileData();
						}
					}
				});
			}
		});		
		
		//Role detail user listgrid
		final DataSource userDetailProfileDataSource=AdministrationData.getRoleDetailProfileData(roleId);

		//Request Profile Combo
		RPCRequest requestProfile = new RPCRequest();
		requestProfile.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchProfileComboBoxData&type=RPC");
		requestProfile.setHttpMethod("POST");
		requestProfile.setUseSimpleHttp(true);
		requestProfile.setShowPrompt(false);
		RPCManager.sendRequest(requestProfile, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseProfile = rawData.toString();
						String xmlDataProfile = rpcResponseProfile;
						final Map<String, String> profileMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataProfile));
						
						//Request Role Combo
						RPCRequest requestRole = new RPCRequest();
						requestRole.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchRoleComboBoxData&type=RPC");
						requestRole.setHttpMethod("POST");
						requestRole.setUseSimpleHttp(true);
						requestRole.setShowPrompt(false);
						
						RPCManager.sendRequest(requestRole, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponseRole = rawData.toString();
										String xmlDataRole = rpcResponseRole;
										final Map<String, String> roleMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataRole));

										roleDetailProfileListGrid.setDataSource(userDetailProfileDataSource);
										roleDetailProfileListGrid.setFields(
												new ListGridField(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID),
												new ListGridField(DataNameTokens.RAFROLE_RAFROLEPROFILES_PROFILEID)
										);										
										roleDetailProfileListGrid.getField(DataNameTokens.RAFROLE_RAFROLEPROFILES_PROFILEID).setValueMap(profileMap);
										roleDetailProfileListGrid.getField(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID).setValueMap(roleMap);
										roleDetailProfileListGrid.getField(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID).setCanEdit(false);
										roleDetailProfileListGrid.getField(DataNameTokens.RAFROLE_RAFROLEPROFILES_ROLEID).setDefaultValue(roleId);
										roleDetailProfileListGrid.getField(DataNameTokens.RAFROLE_RAFROLEPROFILES_PROFILEID).setRequired(true);
										roleDetailProfileListGrid.fetchData();
									}
						});
				}
		});

		roleDetailProfileListGrid.setCanEdit(true);
		roleDetailProfileListGrid.setSelectionType(SelectionStyle.SIMPLE);
		roleDetailProfileListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		roleDetailProfileListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		roleDetailProfileListGrid.setShowRowNumbers(true);
		
		ToolStrip usersToolStrip = new ToolStrip();
		usersToolStrip.setWidth100();
		
		ToolStripButton addUsersButton = new ToolStripButton();  
		addUsersButton.setIcon("[SKIN]/icons/user_add.png");  
		addUsersButton.setTooltip("Add User");
		addUsersButton.setTitle("Add");
		
		ToolStripButton removeUsersButton = new ToolStripButton();  
		removeUsersButton.setIcon("[SKIN]/icons/user_delete.png");  
		removeUsersButton.setTooltip("Remove User");
		removeUsersButton.setTitle("Remove");
		
		usersToolStrip.addButton(addUsersButton);
		usersToolStrip.addButton(removeUsersButton);
		
		addUsersButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				roleDetailUserListGrid.startEditingNew();
			}
		});
		
		roleDetailUserListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				roleDetailUserListGrid.saveAllEdits();
					
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}
				refreshRoleDetailUserData();
			}
		});
		
		removeUsersButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){				
							roleDetailUserListGrid.removeSelectedData();
							SC.say("Data Removed");
							refreshRoleDetailUserData();
						}
					}
				});
			}
		});		
		
		//Role detail user listgrid
		final DataSource roleDetailUserDataSource=AdministrationData.getRoleDetailUserData(roleId);

		//Request User Combo
		RPCRequest requestUser = new RPCRequest();
		requestUser.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchUserComboBoxData&type=RPC");
		requestUser.setHttpMethod("POST");
		requestUser.setUseSimpleHttp(true);
		requestUser.setShowPrompt(false);
		RPCManager.sendRequest(requestUser, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseUser = rawData.toString();
						String xmlDataUser = rpcResponseUser;
						final Map<String, String> userMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataUser));
						
						//Request Role Combo
						RPCRequest requestRole = new RPCRequest();
						requestRole.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchRoleComboBoxData&type=RPC");
						requestRole.setHttpMethod("POST");
						requestRole.setUseSimpleHttp(true);
						requestRole.setShowPrompt(false);
						
						RPCManager.sendRequest(requestRole, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponseRole = rawData.toString();
										String xmlDataRole = rpcResponseRole;
										final Map<String, String> roleMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataRole));

										roleDetailUserListGrid.setDataSource(roleDetailUserDataSource);
										roleDetailUserListGrid.setFields(
												new ListGridField(DataNameTokens.RAFROLE_RAFUSERROLES_ROLEID),
												new ListGridField(DataNameTokens.RAFROLE_RAFUSERROLES_USERID)
										);										
										roleDetailUserListGrid.getField(DataNameTokens.RAFROLE_RAFUSERROLES_USERID).setValueMap(userMap);
										roleDetailUserListGrid.getField(DataNameTokens.RAFROLE_RAFUSERROLES_ROLEID).setValueMap(roleMap);
										roleDetailUserListGrid.getField(DataNameTokens.RAFROLE_RAFUSERROLES_ROLEID).setCanEdit(false);
										roleDetailUserListGrid.getField(DataNameTokens.RAFROLE_RAFUSERROLES_ROLEID).setDefaultValue(roleId);
										roleDetailUserListGrid.getField(DataNameTokens.RAFROLE_RAFUSERROLES_USERID).setRequired(true);
										roleDetailUserListGrid.fetchData();
									}
						});
				}
		});

		roleDetailUserListGrid.setCanEdit(true);
		roleDetailUserListGrid.setSelectionType(SelectionStyle.SIMPLE);
		roleDetailUserListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		roleDetailUserListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		roleDetailUserListGrid.setShowRowNumbers(true);
		
		roleDetailLayout.setMembers(roleDetailToolStrip, profilesToolStrip, roleDetailProfileListGrid, usersToolStrip, roleDetailUserListGrid);
	}
	
	private Tab buildGroupTab() {
		Tab groupTab = new Tab("Group");		
		VLayout groupLayout = new VLayout();
		
		VLayout groupListLayout = new VLayout();
		groupListLayout.setHeight(LIST_HEIGHT);
		groupListLayout.setShowResizeBar(true);
		
		ToolStrip groupToolStrip = new ToolStrip();		
		groupToolStrip.setWidth100();
		
		ToolStripButton addButton = new ToolStripButton();  
		addButton.setIcon("[SKIN]/icons/business_users_add.png");  
		addButton.setTooltip("Add New Group");
		addButton.setTitle("Add");
		
		ToolStripButton removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]/icons/business_users_delete.png");  
		removeButton.setTooltip("Delete Current Group");
		removeButton.setTitle("Remove");
		
		groupToolStrip.addButton(addButton);
		groupToolStrip.addButton(removeButton);
				
		groupListGrid.setAutoFetchData(true);
		groupListGrid.setCanEdit(true);
		groupListGrid.setShowFilterEditor(true);
		groupListGrid.setSelectionType(SelectionStyle.SIMPLE);
		groupListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		groupListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		groupListGrid.setShowRowNumbers(true);
		
		groupListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {		
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				ListGridRecord[] selectedRecords = groupListGrid.getSelection();

				if (selectedRecords.length==1) {
					Record record = selectedRecords[0];
					showGroupDetail(record.getAttributeAsString(DataNameTokens.RAFGROUP_GROUPID));
				} else {
					HTMLFlow groupDetailFlow = new HTMLFlow();
					groupDetailFlow.setAlign(Alignment.CENTER);
					groupDetailFlow.setWidth100();
					if (selectedRecords.length==0) {
						groupDetailFlow.setContents("<h2 align=\"center\">Please select group to show the group detail</h2>");
					} else if (selectedRecords.length>1) {
						groupDetailFlow.setContents("<h2 align=\"center\">More than one group selected, please select only one group to show the group detail</h2>");
					}
					groupDetailLayout.setMembers(groupDetailFlow);
				}
			}
		});
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				groupListGrid.startEditingNew();
			}
		});		

		groupListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				groupListGrid.saveAllEdits();
				if(event.getDsResponse().getStatus()==0){
					SC.say("Data Added/Edited");
				}
				refreshGroupData();
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){							
							groupListGrid.removeSelectedData();
							SC.say("Data Removed");
						}
					}
				});
			}
		});
		
		groupListLayout.setMembers(groupToolStrip, groupListGrid);		
		groupDetailLayout = new VLayout();		
		groupLayout.setMembers(groupListLayout, groupDetailLayout);		
		groupTab.setPane(groupLayout);
		
		return groupTab;
	}
	
	private void showGroupDetail(final String groupId) {	
		ToolStrip assignedRolesToolStrip = new ToolStrip();
		assignedRolesToolStrip.setWidth100();
		
		ToolStripButton addRolesButton = new ToolStripButton();  
		addRolesButton.setIcon("[SKIN]/icons/business_user_add.png");  
		addRolesButton.setTooltip("Add Role");
		addRolesButton.setTitle("Add");
		
		ToolStripButton removeRolesButton = new ToolStripButton();  
		removeRolesButton.setIcon("[SKIN]/icons/business_user_delete.png");  
		removeRolesButton.setTooltip("Remove Role");
		removeRolesButton.setTitle("Remove");
		
		assignedRolesToolStrip.addButton(addRolesButton);
		assignedRolesToolStrip.addButton(removeRolesButton);
		
		addRolesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				groupDetailListGrid.startEditingNew();
			}
		});
		
		groupDetailListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
					groupDetailListGrid.saveAllEdits();
					
					if(event.getDsResponse().getStatus()==0){
						SC.say("Data Added/Edited");
					}
					refreshGroupDetailData();
			}
		});
		
		removeRolesButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){				
							groupDetailListGrid.removeSelectedData();
							SC.say("Data Removed");
							refreshGroupDetailData();
						}
					}
				});
			}
		});				
		
		final DataSource groupDetailDataSource=AdministrationData.getGroupDetailData(groupId);

		//Request Group Combo
		RPCRequest requestGroup = new RPCRequest();
		requestGroup.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchGroupComboBoxData&type=RPC");
		requestGroup.setHttpMethod("POST");
		requestGroup.setUseSimpleHttp(true);
		requestGroup.setShowPrompt(false);
		RPCManager.sendRequest(requestGroup, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseGroup = rawData.toString();
						String xmlDataGroup = rpcResponseGroup;
						final Map<String, String> groupMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataGroup));
						
						//Request Role Combo
						RPCRequest requestRole = new RPCRequest();
						requestRole.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchRoleComboBoxData&type=RPC");
						requestRole.setHttpMethod("POST");
						requestRole.setUseSimpleHttp(true);
						requestRole.setShowPrompt(false);
						
						RPCManager.sendRequest(requestRole, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponseRole = rawData.toString();
										String xmlDataRole = rpcResponseRole;
										final Map<String, String> roleMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataRole));

										groupDetailListGrid.setDataSource(groupDetailDataSource);
										groupDetailListGrid.setFields(
												new ListGridField(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID),
												new ListGridField(DataNameTokens.RAFGROUP_RAFGROUPROLES_ROLEID)
										);										
										groupDetailListGrid.getField(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID).setValueMap(groupMap);
										groupDetailListGrid.getField(DataNameTokens.RAFGROUP_RAFGROUPROLES_ROLEID).setValueMap(roleMap);
										groupDetailListGrid.getField(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID).setCanEdit(false);
										groupDetailListGrid.getField(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID).setDefaultValue(groupId);
										groupDetailListGrid.fetchData();
									}
						});
				}
		});

		groupDetailListGrid.setCanEdit(true);
		groupDetailListGrid.setSelectionType(SelectionStyle.SIMPLE);
		groupDetailListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		groupDetailListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		groupDetailListGrid.setShowRowNumbers(true);
		
		groupDetailLayout.setMembers(assignedRolesToolStrip, groupDetailListGrid);
	}
	
	@Override
	public Widget asWidget() {
		return roleProfileUserGroupLayout;
	}

	protected void bindCustomUiHandlers() {

	}
	
	protected void bindCustomRoleUiHandlers() {
		roleListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshRoleData();
			}
		});
	}
	
	protected void bindCustomGroupUiHandlers() {
		groupListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshGroupData();
			}
		});
	}
	
	protected void bindCustomUserUiHandlers() {
		userListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshUserData();
			}
		});
	}
	
	protected void bindCustomProfileUiHandlers() {
		profileListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshProfileData();
			}
		});
	}
	
	@Override
	public void loadAdministrationData(DataSource dsProfile, DataSource dsUser, DataSource dsGroup,  DataSource dsRole) {	
		//validator
		LengthRangeValidator lengthRangeValidator100 = new LengthRangeValidator();  
		lengthRangeValidator100.setMax(100);  
		LengthRangeValidator lengthRangeValidator200 = new LengthRangeValidator();  
		lengthRangeValidator200.setMax(200);
		LengthRangeValidator lengthRangeValidator300 = new LengthRangeValidator();  
		lengthRangeValidator300.setMax(300);
		
		//populate role listgrid
		roleListGrid.setDataSource(dsRole);
		roleListGrid.setFields(
				new ListGridField(DataNameTokens.RAFROLE_ROLENAME),
				new ListGridField(DataNameTokens.RAFROLE_ROLEDESC),
				new ListGridField(DataNameTokens.RAFROLE_PARENTROLE)
		);
		
		//validation
		//required
		roleListGrid.getField(DataNameTokens.RAFROLE_ROLENAME).setRequired(true);
		roleListGrid.getField(DataNameTokens.RAFROLE_ROLEDESC).setRequired(true);
		
		//length
		roleListGrid.getField(DataNameTokens.RAFROLE_ROLENAME).setValidators(lengthRangeValidator100);
		roleListGrid.getField(DataNameTokens.RAFROLE_ROLEDESC).setValidators(lengthRangeValidator200);
		
		//Request Role Parent Combo
		getRoleParentComboBox();
		
		//populate profile listgrid
		profileListGrid.setDataSource(dsProfile);
		profileListGrid.setFields(
				new ListGridField(DataNameTokens.RAFPROFILE_PROFILENAME),
				new ListGridField(DataNameTokens.RAFPROFILE_PROFILEDESC)
		);
		
		//validation
		//required
		profileListGrid.getField(DataNameTokens.RAFPROFILE_PROFILENAME).setRequired(true);
		profileListGrid.getField(DataNameTokens.RAFPROFILE_PROFILEDESC).setRequired(true);
		
		//length
		profileListGrid.getField(DataNameTokens.RAFPROFILE_PROFILENAME).setValidators(lengthRangeValidator100);
		profileListGrid.getField(DataNameTokens.RAFPROFILE_PROFILEDESC).setValidators(lengthRangeValidator300);
		
		//populate user listgrid
		userListGrid.setDataSource(dsUser);
		userListGrid.setFields(
				new ListGridField(DataNameTokens.RAFUSER_LOGINNAME)
		);
		
		//validation
		//required
		userListGrid.getField(DataNameTokens.RAFUSER_LOGINNAME).setRequired(true);
		
		//length
		userListGrid.getField(DataNameTokens.RAFUSER_LOGINNAME).setValidators(lengthRangeValidator100);
		
		//populate group listgrid
		groupListGrid.setDataSource(dsGroup);
		groupListGrid.setFields(
				new ListGridField(DataNameTokens.RAFGROUP_GROUPNAME),
				new ListGridField(DataNameTokens.RAFGROUP_GROUPDESC)
		);
		
		//validation
		//required
		groupListGrid.getField(DataNameTokens.RAFGROUP_GROUPNAME).setRequired(true);
		groupListGrid.getField(DataNameTokens.RAFGROUP_GROUPDESC).setRequired(true);
		
		//length
		groupListGrid.getField(DataNameTokens.RAFGROUP_GROUPNAME).setValidators(lengthRangeValidator100);
		groupListGrid.getField(DataNameTokens.RAFGROUP_GROUPDESC).setValidators(lengthRangeValidator200);
		
        bindCustomUiHandlers();
        bindCustomGroupUiHandlers();
        bindCustomUserUiHandlers();
        bindCustomProfileUiHandlers();
        bindCustomRoleUiHandlers();
	}
	
	public void refreshProfileData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				profileListGrid.setData(response.getData());
			}
		};
		
		profileListGrid.getDataSource().fetchData(profileListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void refreshProfileDetailData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				profileDetailListGrid.setData(response.getData());
			}
		};
		
		profileDetailListGrid.getDataSource().fetchData(profileDetailListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void refreshUserData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				userListGrid.setData(response.getData());
			}
		};
		
		userListGrid.getDataSource().fetchData(userListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void refreshUserDetailRoleData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				userDetailRoleListGrid.setData(response.getData());
			}
		};
		
		userDetailRoleListGrid.getDataSource().fetchData(userDetailRoleListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void refreshUserDetailGroupData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				userDetailGroupListGrid.setData(response.getData());
			}
		};
		
		userDetailGroupListGrid.getDataSource().fetchData(userDetailGroupListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void refreshRoleData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				roleListGrid.setData(response.getData());
			}
		};
		
		roleListGrid.getDataSource().fetchData(roleListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void refreshRoleDetailUserData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				roleDetailUserListGrid.setData(response.getData());
			}
		};
		
		roleDetailUserListGrid.getDataSource().fetchData(roleDetailUserListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void refreshRoleDetailProfileData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				roleDetailProfileListGrid.setData(response.getData());
			}
		};
		
		roleDetailProfileListGrid.getDataSource().fetchData(roleDetailProfileListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void refreshGroupData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				groupListGrid.setData(response.getData());
			}
		};
		
		groupListGrid.getDataSource().fetchData(groupListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void refreshGroupDetailData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				groupDetailListGrid.setData(response.getData());
			}
		};
		
		groupDetailListGrid.getDataSource().fetchData(groupDetailListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void getRoleParentComboBox(){
		//Request Role Parent Combo
		RPCRequest requestRoleParent = new RPCRequest();
		requestRoleParent.setActionURL(GWT.getHostPageBaseURL() + "RoleProfileUserGroupManagementPresenterServlet?method=fetchRoleComboBoxData&type=RPC");
		requestRoleParent.setHttpMethod("POST");
		requestRoleParent.setUseSimpleHttp(true);
		requestRoleParent.setShowPrompt(false);
						
		RPCManager.sendRequest(requestRoleParent, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseRoleParent = rawData.toString();
						String xmlDataRoleParent = rpcResponseRoleParent;
						final Map<String, String> roleParentMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataRoleParent));
						roleListGrid.getField(DataNameTokens.RAFROLE_PARENTROLE).setValueMap(roleParentMap);
						roleListGrid.fetchData();
					}
		});
	}
}