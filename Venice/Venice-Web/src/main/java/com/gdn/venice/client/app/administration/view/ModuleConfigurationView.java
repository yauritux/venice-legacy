package com.gdn.venice.client.app.administration.view;

import java.util.Map;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.administration.presenter.ModuleConfigurationPresenter;
import com.gdn.venice.client.app.administration.view.handlers.ModuleConfigurationUiHandlers;
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
import com.smartgwt.client.rpc.RPCCallback;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.rpc.RPCRequest;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for Module Configuration
 * 
 * @author Roland
 */

public class ModuleConfigurationView extends
ViewWithUiHandlers<ModuleConfigurationUiHandlers> implements
ModuleConfigurationPresenter.MyView {
	@SuppressWarnings("unused")
	private static final int TITLE_HEIGHT = 20;  
	@SuppressWarnings("unused")
	private static final int LIST_HEIGHT = 140;
	
	RafViewLayout moduleConfigurationLayout;
	ListGrid moduleListGrid = new ListGrid();
	@Inject
	public ModuleConfigurationView() {
		moduleConfigurationLayout = new RafViewLayout();
		ToolStrip moduleToolStrip = new ToolStrip();
		moduleToolStrip.setWidth100();
		
//		ToolStripButton importButton = new ToolStripButton();  
//		importButton.setIcon("[SKIN]/icons/window_up.png");  
//		importButton.setTooltip("Import Module");
//		
//		importButton.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				showImportDialog();
//			}
//		});
		
		ToolStripButton addButton = new ToolStripButton();  
		addButton.setIcon("[SKIN]/icons/add.png");  
		addButton.setTooltip("Add New Module");
		addButton.setTitle("Add");
		
		ToolStripButton removeButton = new ToolStripButton();  
		removeButton.setIcon("[SKIN]/icons/remove.png");  
		removeButton.setTooltip("Delete Current Module");
		removeButton.setTitle("Remove");
		
		moduleToolStrip.addButton(addButton);
		moduleToolStrip.addButton(removeButton);	
		
//		ToolStripButton exportButton = new ToolStripButton();  
//		exportButton.setIcon("[SKIN]/icons/window_down.png");  
//		exportButton.setTooltip("Export Module");
		
//		ToolStripButton saveButton = new ToolStripButton();  
//		saveButton.setIcon("[SKIN]/icons/save.png");  
//		saveButton.setTooltip("Save");
		
//		moduleToolStrip.addButton(importButton);
//		moduleToolStrip.addButton(exportButton);
//		moduleToolStrip.addSeparator();
//		moduleToolStrip.addButton(saveButton);
		
		moduleListGrid.setAutoFetchData(true);
		moduleListGrid.setCanEdit(true);
		moduleListGrid.setShowFilterEditor(true);
		moduleListGrid.setSelectionType(SelectionStyle.SIMPLE);
		moduleListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		moduleListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		moduleListGrid.setShowRowNumbers(true);
		
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				moduleListGrid.startEditingNew();
			}
		});		

		moduleListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {
				moduleListGrid.saveAllEdits();
				getComboBoxData();
				refreshModuleData();				
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
							moduleListGrid.removeSelectedData();
							getComboBoxData();
							refreshModuleData();
							SC.say("Data Removed");
						}
					}
				});
			}
		});
	
		moduleConfigurationLayout.setMembers(moduleToolStrip);

		bindCustomUiHandlers();
	}
	
//	private void showImportDialog() {
//		final Window winModal = new Window();
//		winModal.setWidth(300);
//		winModal.setHeight(120);
//		winModal.setTitle("Import Module");
//		winModal.setShowMinimizeButton(false);
//		winModal.setIsModal(true);
//		winModal.setShowModalMask(true);
//		winModal.centerInPage();
//		winModal.addCloseClickHandler(new CloseClickHandler() {
//			public void onCloseClick(CloseClientEvent event) {
//				winModal.destroy();
//			}
//		});
//		DynamicForm form = new DynamicForm();
//		form.setHeight100();
//		form.setWidth100();
//		form.setPadding(5);
//	
//		FileItem attachhmentFileItem = new FileItem("Import");  
//		form.setFields(attachhmentFileItem);
//
//		HLayout btnLayout = new HLayout();
//		btnLayout.setWidth100();
//		btnLayout.setAlign(Alignment.CENTER);
//		btnLayout.setMembersMargin(5);
//		btnLayout.setPadding(10);
//		Button btnSave = new Button("Import");
//		btnSave.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				winModal.destroy();
//			}
//		});
//		Button btnCancel = new Button("Cancel");
//		btnCancel.addClickHandler(new ClickHandler() {
//			@Override
//			public void onClick(ClickEvent event) {
//				winModal.destroy();
//			}
//		});
//
//		btnLayout.setMembers(btnSave, btnCancel);
//
//		winModal.addItem(form);
//		winModal.addItem(btnLayout);
//		winModal.show();
//	}

	@Override
	public void loadModuleData(DataSource dataSource, Map<String,String> moduleTypeMap, Map<String,String> parentModuleMap) {		
		//populate combo box
		dataSource.getField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTTYPEID).setValueMap(moduleTypeMap);
		dataSource.getField(DataNameTokens.RAFAPPLICATIONOBJECT_PARENTAPPLICATIONOBJECTID).setValueMap(parentModuleMap);
		
		//populate listgrid
		moduleListGrid.setDataSource(dataSource);
		moduleListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		moduleListGrid.setSortField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTID);
		moduleListGrid.getField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTID).setCanEdit(false);		
 		
		//validation
		//required
		moduleListGrid.getField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTUUID).setRequired(true);
		moduleListGrid.getField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTCANONICALNAME).setRequired(true);
		moduleListGrid.getField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTTYPEID).setRequired(true);
				
		//length
		LengthRangeValidator lengthRangeValidator36 = new LengthRangeValidator();  
		lengthRangeValidator36.setMax(36);  
		moduleListGrid.getField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTUUID).setValidators(lengthRangeValidator36);
		
		LengthRangeValidator lengthRangeValidator1000 = new LengthRangeValidator();  
		lengthRangeValidator1000.setMax(1000);  
		moduleListGrid.getField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTCANONICALNAME).setValidators(lengthRangeValidator1000);
		
        moduleConfigurationLayout.addMember(moduleListGrid);
        bindCustomUiHandlers();
	}
	
	//get combo box to refresh after add/update
	public void getComboBoxData() {
		//Request Module Type Combo
		RPCRequest requestModuleType = new RPCRequest();
		requestModuleType.setActionURL(GWT.getHostPageBaseURL() + "ModuleConfigurationPresenterServlet?method=fetchModuleTypeComboBoxData&type=RPC");
		requestModuleType.setHttpMethod("POST");
		requestModuleType.setUseSimpleHttp(true);
		requestModuleType.setShowPrompt(false);
		
		RPCManager.sendRequest(requestModuleType, 
				new RPCCallback () {
					public void execute(RPCResponse response,
							Object rawData, RPCRequest request) {
						String rpcResponseModuleType = rawData.toString();
						String xmlDataModuleType = rpcResponseModuleType;
						final Map<String, String> moduleTypeMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataModuleType));
						
						//Request Parent Module Combo
						RPCRequest requestParentModule = new RPCRequest();
						requestParentModule.setActionURL(GWT.getHostPageBaseURL() + "ModuleConfigurationPresenterServlet?method=fetchParentModuleComboBoxData&type=RPC");
						requestParentModule.setHttpMethod("POST");
						requestParentModule.setUseSimpleHttp(true);
						requestParentModule.setShowPrompt(false);
						
						RPCManager.sendRequest(requestParentModule, 
								new RPCCallback () {
									public void execute(RPCResponse response,
											Object rawData, RPCRequest request) {
										String rpcResponseParentModule = rawData.toString();
										String xmlDataParentModule = rpcResponseParentModule;
										final Map<String, String> parentModuleMap = Util.formComboBoxMap(Util.formHashMapfromXML(xmlDataParentModule));
										//populate combo box
										moduleListGrid.getField(DataNameTokens.RAFAPPLICATIONOBJECT_APPLICATIONOBJECTTYPEID).setValueMap(moduleTypeMap);
										moduleListGrid.getField(DataNameTokens.RAFAPPLICATIONOBJECT_PARENTAPPLICATIONOBJECTID).setValueMap(parentModuleMap);
										moduleListGrid.fetchData();
									}
						});
				}
		});
	}
	
	@Override
	public Widget asWidget() {
		return moduleConfigurationLayout;
	}

	protected void bindCustomUiHandlers() {
		moduleListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshModuleData();
			}
		});
	}

	public void refreshModuleData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				moduleListGrid.setData(response.getData());
			}
		};
		getComboBoxData();
		moduleListGrid.getDataSource().fetchData(moduleListGrid.getFilterEditorCriteria(), callBack);
	}
}
