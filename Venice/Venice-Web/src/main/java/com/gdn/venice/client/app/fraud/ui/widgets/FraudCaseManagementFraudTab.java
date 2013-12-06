package com.gdn.venice.client.app.fraud.ui.widgets;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.gdn.venice.client.app.DataMessageTokens;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Widget for Fraud Case Management 
 * 
 * @author Roland
 */

public class FraudCaseManagementFraudTab extends Tab {
	TabSet fraudManagementTabSet = new TabSet();
	DynamicForm fraudManagementForm = new DynamicForm();
	DynamicForm fraudManagementTotalRiskForm = new DynamicForm();
	ToolStripButton closeCaseButton = new ToolStripButton();
	
	public FraudCaseManagementFraudTab(String title, DataSource riskScoreData, DataSource relatedOrderData, DataSource fraudManagementData, DataSource totalRiskScoreData, DataSource actionLogData, DataSource historyLogData, DataSource attachmentData, String caseId) {
		super(title);				
		setPane(createFraudDetail(riskScoreData, relatedOrderData, fraudManagementData, totalRiskScoreData, actionLogData, historyLogData, attachmentData, caseId));
	}
	
	private VLayout createFraudDetail(DataSource riskScoreData, DataSource relatedOrderData, DataSource fraudManagementData, DataSource totalRiskScoreData, DataSource actionLogData, DataSource historyLogData, DataSource attachmentData, final String caseId) {
		VLayout layout = new VLayout();
		layout.setMargin(5);
		
		//Generate toolbar
		ToolStrip fraudToolStrip = new ToolStrip();
		fraudToolStrip.setWidth100();
		fraudToolStrip.setPadding(2);
		
		//Save button
		ToolStripButton saveFraud = new ToolStripButton();
		saveFraud.setIcon("[SKIN]/icons/save.png");  
		saveFraud.setTooltip("Click here to save."); 
		saveFraud.setTitle("Save");
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
									//Refresh list gird dan hilangkan detail nya
									final ListGrid fraudCaseListGrid = (ListGrid) fraudManagementForm.getParentElement().getParentElement().getParentElement().getParentElement().getParentElement().getParentElement().getChildren()[0].getChildren()[1];
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
		
		//Escalate button
		ToolStripButton escalateFraud = new ToolStripButton();
		escalateFraud.setIcon("[SKIN]/icons/user_next.png");  
		escalateFraud.setTooltip("Click here to escalate."); 
		escalateFraud.setTitle("Escalate");
		escalateFraud.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to save and escalate this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value) {
							HashMap<String, String> params = new HashMap<String, String>();
							params.put("action", "escalate");
							
							fraudManagementForm.getDataSource().setDefaultParams(params);
							fraudManagementForm.setSaveOperationType(DSOperationType.UPDATE);
							fraudManagementForm.saveData(new DSCallback() {
								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									//Refresh list gird dan hilangkan detail nya
									final ListGrid fraudCaseListGrid = (ListGrid) fraudManagementForm.getParentElement().getParentElement().getParentElement().getParentElement().getParentElement().getParentElement().getChildren()[0].getChildren()[1];
									DSCallback callBack = new DSCallback() {
										@Override
										public void execute(DSResponse response, Object rawData, DSRequest request) {
											Record record[] = null;
											fraudCaseListGrid.setData(record);
											fraudCaseListGrid.setData(response.getData());
											
											if(response.getStatus()==0){
												SC.say("Data succesfully saved and escalated");
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
		fraudToolStrip.addButton(escalateFraud);
		fraudToolStrip.addSeparator(); 
				
		closeCaseButton.setIcon("[SKIN]/icons/notes_accept.png");  
		closeCaseButton.setTooltip("Click here to close case and complete task."); 
		closeCaseButton.setTitle("Close Case");		
		
		closeCaseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				//Kalau close case, pastikan tidak boleh statusnya SF (Suspected Fraud)
				Object statusId = fraudManagementForm.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID).getValue();
				if (statusId != null && statusId.toString().equalsIgnoreCase("2")) {
					SC.say("Can not close fraud case with SF status.");
					return;
				}
				
				SC.ask("Are you sure you want to close this case and complete task?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							HashMap<String, String> params = new HashMap<String, String>();
							params.put("action", "closecase");
							
							fraudManagementForm.getDataSource().setDefaultParams(params);
							fraudManagementForm.setSaveOperationType(DSOperationType.UPDATE);
							fraudManagementForm.saveData(new DSCallback() {
								
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									//Refresh list gird dan hilangkan detail nya
									final ListGrid fraudCaseListGrid = (ListGrid) fraudManagementForm.getParentElement().getParentElement().getParentElement().getParentElement().getParentElement().getParentElement().getChildren()[0].getChildren()[1];
									DSCallback callBack = new DSCallback() {
										@Override
										public void execute(DSResponse response, Object rawData, DSRequest request) {
											fraudCaseListGrid.setData(response.getData());

											//update order status and publish to esb
							            	RPCRequest rpcRequest=new RPCRequest();
							            	Object statusId = fraudManagementForm.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID).getValue();
							        		String method = "";
							        		if (statusId.toString().equalsIgnoreCase("2")) {
							        			method = "updateOrderStatusToSF";
							        		} else if (statusId.toString().equalsIgnoreCase("3")) {
							        			method = "updateOrderStatusToFC";
							        		} else if (statusId.toString().equalsIgnoreCase("4")) {
							        			method = "updateOrderStatusToFP";
							        		}
							        		
							        		rpcRequest.setData(caseId);
							        		rpcRequest.setActionURL(GWT.getHostPageBaseURL() +  "FraudCaseMaintenancePresenterServlet?method="+method+"&type=RPC");
							        		rpcRequest.setHttpMethod("POST");
							        		rpcRequest.setUseSimpleHttp(true);
							        		rpcRequest.setShowPrompt(false);
							        		
							        		RPCManager.sendRequest(rpcRequest, new RPCCallback () {
							    					public void execute(RPCResponse response, Object rawData, RPCRequest request) {
							    						String rpcResponse = rawData.toString();
							    						if (!rpcResponse.startsWith("0")) {							    					
							    							SC.warn(DataMessageTokens.GENERAL_ERROR_MESSAGE);
							    						}
							    					}
							        		});
											if(response.getStatus()==0){
												SC.say("Case successfully saved and closed, task successfully closed. <br> Order status successfully published to WCS and MTA.");
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
		fraudToolStrip.addButton(closeCaseButton);
		fraudToolStrip.addSeparator(); 
		
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
		fraudManagementForm.setNumCols(7);
		fraudManagementForm.setWidth100();

		HiddenItem taskIdItem = new HiddenItem(fraudManagementData.getField(DataNameTokens.TASKID).getName());
		HiddenItem ableToEscalateItem = new HiddenItem(fraudManagementData.getField(DataNameTokens.TASKABLETOESCALATE).getName());
		StaticTextItem fraudCaseItem = new StaticTextItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID).getName());		
		StaticTextItem dateTimeItem = new StaticTextItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME).getName());
//		dateTimeItem.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		TextAreaItem descItem = new TextAreaItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC).getName());
		descItem.setWidth("100%");
		descItem.setHeight(50);
		
		final TextAreaItem reasonItem = new TextAreaItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON).getName());
		reasonItem.setWidth("100%");
		reasonItem.setHeight(50);

		TextAreaItem notesItem = new TextAreaItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES).getName());
		notesItem.setWidth("100%");
		notesItem.setHeight(50);
		notesItem.setDisabled(true);
		
		final ComboBoxItem statusItem = new ComboBoxItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID).getName());
		statusItem.setWidth("100%");
		
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
		
		final ComboBoxItem statusItemType = new ComboBoxItem(fraudManagementData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_TYPE_FC).getName());
		statusItemType.setWidth("100%");
		LinkedHashMap<String, String> typeMap =  new LinkedHashMap<String,String>();	
		typeMap.put("FC", "Fraud Confirmed");
		typeMap.put("HRSF", "High Risk Suspicious Fraud");
		statusItemType.setValueMap(typeMap);		        
		
		statusItem.addChangedHandler(new ChangedHandler() {			
			@Override
			public void onChanged(ChangedEvent event) {
				/**
				 * FC = 3
				 */
				if(event.getValue().toString().equals("3")){
					statusItemType.setVisible(true);
					statusItemType.setValue("FC");
					reasonItem.setValue("Fraud By Bank");
					fraudManagementForm.redraw();
				}else{
					statusItemType.setVisible(false);
					fraudManagementForm.redraw();
				}
				
			}			
		});
		
		statusItemType.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if(statusItem.getValue().toString().equals("3")){
						if(event.getValue().toString().equals("FC")){
							reasonItem.setValue("Fraud By Bank");
						}else{
							reasonItem.setValue("Late replay from bank, No confirmation from bank, address not valid,Bin not listed, Denied from card holder, ECI 7");
						}
				}else{
					statusItemType.setVisible(false);
					statusItemType.setValue("");
					fraudManagementForm.redraw();
				}
			}			
		});	
		
		
		fraudManagementForm.setFields(
			taskIdItem,
			ableToEscalateItem,
			fraudCaseItem,
			statusItem,			
			dateTimeItem,
			descItem,
			statusItemType,			
			reasonItem,
			notesItem			
		);
		
		//validation, required
		fraudManagementForm.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC).setRequired(true);
		fraudManagementForm.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON).setRequired(true);
		fraudManagementForm.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES).setRequired(true);
		
		//length
		 LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();  
		 lengthRangeValidator.setMax(1000);  
		 fraudManagementForm.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC).setValidators(lengthRangeValidator);
		 fraudManagementForm.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON).setValidators(lengthRangeValidator);
		 fraudManagementForm.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES).setValidators(lengthRangeValidator);
		 
		 HLayout genuinelayout = new HLayout();	
		 genuinelayout.setMargin(10);
			
		fraudManagementTotalRiskForm.setDataSource(totalRiskScoreData);
		fraudManagementTotalRiskForm.setUseAllDataSourceFields(true);
		fraudManagementTotalRiskForm.setNumCols(4);	
		fraudManagementTotalRiskForm.setWidth(350);
		
		final CheckboxItem checkboxItem = new CheckboxItem(totalRiskScoreData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_GENUINE).getName());  
		
		StaticTextItem totalRiskPoint = new StaticTextItem(totalRiskScoreData.getField(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_TOTALRISKSCORE).getName());	
        
        final IButton button = new IButton("Save Genuine");      
        button.setIcon("[SKIN]/icons/save.png");  
        button.setTooltip("Click here to save genuine."); 
        button.setTitle("Save Genuine");
        button.setWidth(150);
        button.setDisabled(true);
        
        button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if(checkboxItem.getValueAsBoolean()){					
					
						RPCRequest request = new RPCRequest();
						
						request.setData(caseId);
		            	request.setActionURL(GWT.getHostPageBaseURL() + "FraudCaseMaintenancePresenterServlet?type=RPC&method=saveGenuine");
		        		request.setHttpMethod("POST");
		        		request.setUseSimpleHttp(true);
		        		request.setShowPrompt(false);
		        		
		        		RPCManager.sendRequest(request, 
		    				new RPCCallback () {
		    					public void execute(RPCResponse response,
		    							Object rawData, RPCRequest request) {    
		    						String rpcResponse = rawData.toString();
		    						if (rpcResponse.startsWith("0")) {
										SC.say("Data Succesfully Saved");
									}	else{    											    					
		    							SC.warn(rpcResponse.split("&")[1]);
		    						}
		    					}
		        		});						
					}else{
						SC.warn("Please chek first!!!!");
					}
			}
		});
        
        checkboxItem.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				 button.setDisabled(false);				
			}
        	
        });       
		
		fraudManagementTotalRiskForm.setFields(totalRiskPoint,checkboxItem);        
		
		HLayout fraudCasefraudManagementHeaderLayout = new HLayout();
		fraudCasefraudManagementHeaderLayout.setWidth100();
		fraudCasefraudManagementHeaderLayout.setMargin(10);
		fraudCasefraudManagementHeaderLayout.setMembers(fraudManagementForm);
		
		genuinelayout.setMembers(fraudManagementTotalRiskForm,button);
		fraudCasefraudManagementLayout.setMembers(fraudCasefraudManagementHeaderLayout,genuinelayout);
		
		setPane(fraudCasefraudManagementLayout);
		fraudManagementTabSet.addTab(new FraudCaseManagementFraudPointReferenceTab("Fraud Point Reference", riskScoreData));
		fraudManagementTabSet.addTab(new FraudCaseManagementRelatedOrderTab("Related Order", relatedOrderData, relatedOrderData));
		fraudManagementTabSet.addTab(new FraudCaseManagementActionLogTab("Action Log", actionLogData, caseId));
		fraudManagementTabSet.addTab(new FraudCaseManagementHistoryLogTab("History Log", historyLogData));
		fraudManagementTabSet.addTab(new FraudCaseManagementAttachmentTab("Attachment", attachmentData));
		
		layout.setMembersMargin(5);
		layout.setMembers(fraudToolStrip, fraudManagementForm,genuinelayout, fraudManagementTabSet);
		
		return layout;
	}
	
	public DynamicForm getFraudCaseManagementForm() {
		return fraudManagementForm;
	}	
	
	public DynamicForm getFraudCaseManagementTotalRiskForm() {
		return fraudManagementTotalRiskForm;
	}
	
	public ToolStripButton getCloseCaseButton() {
		return closeCaseButton;
	}
}
