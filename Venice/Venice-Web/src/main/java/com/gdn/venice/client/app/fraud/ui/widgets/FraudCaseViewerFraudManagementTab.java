package com.gdn.venice.client.app.fraud.ui.widgets;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Widget for Fraud Management Viewer
 * 
 * @author Roland
 */

public class FraudCaseViewerFraudManagementTab extends Tab {
	TabSet fraudManagementTabSet = new TabSet();
	DynamicForm fraudManagementForm = new DynamicForm();
	DynamicForm fraudManagementTotalRiskForm = new DynamicForm();
	public FraudCaseViewerFraudManagementTab(String title, DataSource fraudPointData, DataSource relatedOrderData, DataSource fraudManagementData, DataSource totalRiskScoreData, DataSource actionLogData, DataSource historyLogData, DataSource attachmentData, String caseId) {
		super(title);		
		setPane(createFraudDetail(fraudPointData, relatedOrderData, fraudManagementData, totalRiskScoreData, actionLogData, historyLogData, attachmentData, caseId));
	}
	
	private VLayout createFraudDetail(DataSource fraudPointData, DataSource relatedOrderData, DataSource fraudManagementData, DataSource totalRiskScoreData, DataSource actionLogData, DataSource historyLogData, DataSource attachmentData, final String caseId) {
		VLayout layout = new VLayout();
		layout.setMargin(5);
		//Generate toolbar
		ToolStrip fraudToolStrip = new ToolStrip();
		fraudToolStrip.setWidth100();
		fraudToolStrip.setPadding(2);
		
		//Save button
		ToolStripButton saveFraud = new ToolStripButton();
		saveFraud.setIcon("[SKIN]/icons/save.png");  
		saveFraud.setTooltip("Click here to save notes. Only notes will be saved."); 
		saveFraud.setTitle("Save Notes");
		saveFraud.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to save this data?", new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						if(value != null && value) {
							fraudManagementForm.getDataSource().setDefaultParams(new HashMap<String, String>());
							fraudManagementForm.setSaveOperationType(DSOperationType.UPDATE);
							fraudManagementForm.saveData(new DSCallback() {
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									final ListGrid fraudCaseListGrid = (ListGrid) fraudManagementForm.getParentElement().getParentElement().getParentElement().getParentElement().getParentElement().getParentElement().getChildren()[0].getChildren()[0];
									DSCallback callBack = new DSCallback() {
										@Override
										public void execute(DSResponse response, Object rawData, DSRequest request) {
											fraudCaseListGrid.setData(response.getData());
											
											if(response.getStatus()==0){
												SC.say("Data Succesfully Saved");
											}	
										}
									};
									
									fraudCaseListGrid.getDataSource().fetchData(fraudCaseListGrid.getFilterEditorCriteria(), callBack);
								}
							});
						}
					}
				});
			}
		});
		fraudToolStrip.addButton(saveFraud);
		fraudToolStrip.addSeparator();
		
		//button for print report
		ToolStripButton printCaseButton = new ToolStripButton();
		printCaseButton.setIcon("[SKIN]/icons/notes_accept.png");  
		printCaseButton.setTooltip("Click here to print case detail."); 
		printCaseButton.setTitle("Print Case");
		printCaseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to print this case?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value) {
							RPCRequest request = new RPCRequest();
							
			            	request.setActionURL(GWT.getHostPageBaseURL() + "FraudCaseMaintenancePresenterServlet?type=RPC&method=printFraudCaseData&fraudcaseid=" + caseId);
			        		request.setHttpMethod("POST");
			        		request.setUseSimpleHttp(true);
			        		request.setShowPrompt(false);
			        		
			        		RPCManager.sendRequest(request, 
			    				new RPCCallback () {
			    					public void execute(RPCResponse response,
			    							Object rawData, RPCRequest request) {
			    						String rpcResponse = rawData.toString();
			    						
			    						if (rpcResponse.equalsIgnoreCase("")) {
				    						SC.say("Failed generating order detail report.");
			    						}
			    						else {
			    							Window.open(GWT.getHostPageBaseURL() + "FraudCaseMaintenancePresenterServlet?method=downloadFraudCaseReport&filename=" + rpcResponse, "_blank", "enabled");			
			    						}
			    					}
			        		});
						}
					}
				});
			}
		});
		fraudToolStrip.addButton(printCaseButton);
		
		VLayout fraudCasefraudManagementLayout = new VLayout();
		
		fraudManagementForm = new DynamicForm();
		fraudManagementForm.setDataSource(fraudManagementData);
		fraudManagementForm.setUseAllDataSourceFields(false);
		fraudManagementForm.setNumCols(6);		

		StaticTextItem fraudCaseItem = new StaticTextItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).getName());		
    	StaticTextItem dateTimeItem = new StaticTextItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME).getName());
//    	dateTimeItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
    	final StaticTextItem statusItem = new StaticTextItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID).getName());
    	
		//Request Fraud Case Status Combo
		RPCRequest requestStatus = new RPCRequest();
		requestStatus.setActionURL(GWT.getHostPageBaseURL() + "FraudCaseMaintenancePresenterServlet?method=fetchFraudCaseStatusComboBoxData&type=RPC");
		requestStatus.setHttpMethod("POST");
		requestStatus.setUseSimpleHttp(true);
		requestStatus.setShowPrompt(false);
		RPCManager.sendRequest(requestStatus, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseStatus = rawData.toString();
						String xmlDataStatus = rpcResponseStatus;
						LinkedHashMap<String, String> statusMap =  Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataStatus));	
						statusItem.setValueMap(statusMap);
				}
		});
		
		TextAreaItem descItem = new TextAreaItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC).getName());
		descItem.setWidth("100%");
		descItem.setHeight(50);
		
		TextAreaItem reasonItem = new TextAreaItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON).getName());
		reasonItem.setWidth("100%");
		reasonItem.setHeight(50);

		TextAreaItem notesItem = new TextAreaItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES).getName());
		notesItem.setWidth("100%");
		notesItem.setHeight(50);
		
		fraudManagementForm.setFields(
				fraudCaseItem,
				statusItem,
				dateTimeItem,
				descItem,
				reasonItem,
				notesItem				
		);
		
		fraudManagementTotalRiskForm.setDataSource(totalRiskScoreData);
		fraudManagementTotalRiskForm.setUseAllDataSourceFields(true);
		fraudManagementTotalRiskForm.setNumCols(2);	
		fraudManagementTotalRiskForm.setFields(Util.getReadOnlyFormItemFromDataSource(totalRiskScoreData));
		HLayout fraudCasefraudManagementHeaderLayout = new HLayout();
		fraudCasefraudManagementHeaderLayout.setWidth100();
		fraudCasefraudManagementHeaderLayout.setMargin(10);
		
		final CheckboxItem checkboxItem = new CheckboxItem(totalRiskScoreData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_GENUINE).getName());  
		fraudManagementTotalRiskForm.setFields(checkboxItem);   
		fraudCasefraudManagementHeaderLayout.setMembers(fraudManagementForm, fraudManagementTotalRiskForm);
		
		fraudCasefraudManagementLayout.setMembers(fraudCasefraudManagementHeaderLayout);
		
		setPane(fraudCasefraudManagementLayout);
		fraudManagementTabSet.addTab(new FraudCaseManagementFraudPointReferenceTab("Fraud Point Reference", fraudPointData));
		fraudManagementTabSet.addTab(new FraudCaseViewerRelatedOrderTab("Related Order", relatedOrderData, relatedOrderData));
		fraudManagementTabSet.addTab(new FraudCaseViewerActionLogTab("Action Log", actionLogData));
		fraudManagementTabSet.addTab(new FraudCaseManagementHistoryLogTab("History Log", historyLogData));
		fraudManagementTabSet.addTab(new FraudCaseViewerManagementAttachmentTab("Attachment", attachmentData));
	
		layout.setMembersMargin(5);
		layout.setMembers(fraudToolStrip, fraudManagementForm, fraudManagementTotalRiskForm, fraudManagementTabSet);
		
		return layout;
	}
	
	public DynamicForm getFraudCaseManagementForm() {
		return fraudManagementForm;
	}	
	
	public DynamicForm getFraudCaseManagementTotalRiskForm() {
		return fraudManagementTotalRiskForm;
	}	
}
