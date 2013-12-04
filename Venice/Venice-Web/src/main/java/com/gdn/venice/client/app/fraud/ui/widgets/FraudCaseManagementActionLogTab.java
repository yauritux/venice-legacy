package com.gdn.venice.client.app.fraud.ui.widgets;

import java.util.HashMap;
import java.util.LinkedHashMap;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.fraud.data.FraudData;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.DateTimeItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * Widget for Action Log
 * 
 * @author Roland
 */

public class FraudCaseManagementActionLogTab extends Tab {
	ToolStripButton addButton;
	ToolStripButton removeButton;
	ListGrid fraudActionLogListGrid;
	
	Window actionLogDetailWindow;
	DynamicForm actionLogForm = new DynamicForm();
	final HiddenItem partyIdItem = new HiddenItem(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYID);
	final TextItem partyItem = new TextItem(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYNAME);
	ListGrid partyList;
	
	public FraudCaseManagementActionLogTab(String title, DataSource actionLogData, final String caseId) {
		super(title);		

		addButton = new ToolStripButton();
		addButton.setIcon("[SKIN]/icons/add.png");
		addButton.setTooltip("Add to Action Log");
		addButton.setTitle("Add");

		removeButton = new ToolStripButton();
		removeButton.setIcon("[SKIN]/icons/remove.png");
		removeButton.setTooltip("Remove from Action Log");
		removeButton.setTitle("Remove");

		ToolStrip actionLogToolStrip = new ToolStrip();
		actionLogToolStrip.setWidth100();
		actionLogToolStrip.addButton(addButton);
		actionLogToolStrip.addButton(removeButton);
		
		ListGridField listGridField[] = Util.getListGridFieldsFromDataSource(actionLogData);
		ListGridField editField = new ListGridField("EDITFIELD", "Edit");  
		editField.setWidth(35);
		editField.setCanFilter(false);
		editField.setCanSort(false);
		editField.setAlign(Alignment.CENTER);
        ListGridField finalListGridField[] = {editField, listGridField[0], listGridField[1], listGridField[2], listGridField[3], listGridField[4], listGridField[5]};
		
		fraudActionLogListGrid = new ListGrid()  {  
			@Override
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {
				if (this.getFieldName(colNum).equalsIgnoreCase("editfield")) {
					ImgButton editImg = new ImgButton();
					editImg.setShowDown(false);
					editImg.setShowRollOver(false);
					editImg.setLayoutAlign(Alignment.CENTER);
					editImg.setSrc("[SKIN]/icons/note_edit.png");
					editImg.setPrompt("Edit Action Log");
					editImg.setHeight(16);
					editImg.setWidth(16);
					editImg.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {
							HashMap<String, String> params = new HashMap<String, String>();
							params.put(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID, record.getAttribute(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID));
							actionLogForm.getDataSource().setDefaultParams(params);
							actionLogForm.fetchData(null, new DSCallback() {
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									actionLogDetailWindow.setTitle("Edit Action Log");
									actionLogDetailWindow.show();
								}
							});
						}
					});

					return editImg;
				} else {
					return null;
				}
			}
		};
		
		fraudActionLogListGrid.setDataSource(actionLogData);
		fraudActionLogListGrid.setFields(finalListGridField);
		fraudActionLogListGrid.setAutoFetchData(true);
		fraudActionLogListGrid.setSelectionType(SelectionStyle.SIMPLE);
		fraudActionLogListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		fraudActionLogListGrid.setShowRowNumbers(true);
		fraudActionLogListGrid.setShowFilterEditor(true);
		fraudActionLogListGrid.setSortField(1);
		fraudActionLogListGrid.setShowRecordComponents(true);          
		fraudActionLogListGrid.setShowRecordComponentsByCell(true);  
		
		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID).setHidden(true);
		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYID).setHidden(true);
		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID).setCanEdit(false);
		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYID).setCanEdit(false);
		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_DATETIME).setWidth(120);
		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_ACTIONTYPE).setWidth(100);
//		fraudActionLogListGrid.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_DATETIME).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
		
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();  
		map.put("CALL", "Call");
		map.put("MEET", "Meet");
		map.put("SENDDOCUMENT", "Send Document");
		map.put("OTHER", "Other");
		actionLogData.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_ACTIONTYPE).setValueMap(map);
		
		VLayout actionLogLayout = new VLayout();
		actionLogLayout.setMembers(actionLogToolStrip, fraudActionLogListGrid);
		setPane(actionLogLayout);
		bindCustomUiHandlers();
		
		actionLogDetailWindow = buildAddEditWindow(caseId);
	}
	
	protected void bindCustomUiHandlers() {
		addButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				actionLogDetailWindow.setTitle("Add Action Log");
				actionLogDetailWindow.show();
			}
		});
		
		removeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value) {
							fraudActionLogListGrid.removeSelectedData(new DSCallback() {
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									fraudActionLogListGrid.clearCriteria();
									refreshFraudCaseActionLogData();
									SC.say("Data Removed");
								}
							}, null);
						}
					}
				});
			}
		});
		
		fraudActionLogListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshFraudCaseActionLogData();
			}
		});
	}
	
	private Window buildAddEditWindow(final String caseId) {
		final Window addEditActionLogWindow = new Window();
		addEditActionLogWindow.setWidth(450);
		addEditActionLogWindow.setHeight(250);
		addEditActionLogWindow.setShowMinimizeButton(false);
		addEditActionLogWindow.setIsModal(true);
		addEditActionLogWindow.setShowModalMask(true);
		addEditActionLogWindow.centerInPage();
		
		ToolStripButton saveButton = new ToolStripButton();
		saveButton.setIcon("[SKIN]/icons/save.png");
		saveButton.setTooltip("Save Action Log");
		saveButton.setTitle("Save");
		
		ToolStripButton cancelButton = new ToolStripButton();
		cancelButton.setIcon("[SKIN]/icons/delete.png");
		cancelButton.setTooltip("Cancel Action Log");
		cancelButton.setTitle("Cancel");

		ToolStrip actionLogDetailToolStrip = new ToolStrip();
		actionLogDetailToolStrip.setWidth100();
		actionLogDetailToolStrip.addButton(saveButton);
		actionLogDetailToolStrip.addButton(cancelButton);

		DataSource actionLogDetailData = FraudData.getFraudCaseActionLogDetailData();
		actionLogForm.setDataSource(actionLogDetailData);
		actionLogForm.setUseAllDataSourceFields(false);
		actionLogForm.setNumCols(2);
		
		HiddenItem caseIdItem = new HiddenItem(DataNameTokens.FRDFRAUDCASEACTIONLOG_SUSPICIONCASEID);
		HiddenItem actionLogIdItem = new HiddenItem(DataNameTokens.FRDFRAUDCASEACTIONLOG_FRAUDACTIONID);
		
		ComboBoxItem actionType = new ComboBoxItem(DataNameTokens.FRDFRAUDCASEACTIONLOG_ACTIONTYPE);
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();  
		map.put("CALL", "Call");
		map.put("MEET", "Meet");
		map.put("SENDDOCUMENT", "Send Document");
		map.put("OTHER", "Other");
		actionType.setWidth("140");
		actionType.setValueMap(map);

		partyItem.setTitle("Party");
		partyItem.setWidth("200");
        PickerIcon searchPartytPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {  
			public void onFormItemClick(FormItemIconClickEvent event) {  
				buildSelectPartyWindow().show();
			}  
		});  
        partyItem.setIcons(searchPartytPicker);
        
        DateTimeItem actionLogDate = new DateTimeItem(DataNameTokens.FRDFRAUDCASEACTIONLOG_DATETIME);
//        actionLogDate.setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATETIME);
        actionLogDate.setWidth("140");
		TextAreaItem notesItem = new TextAreaItem(DataNameTokens.FRDFRAUDCASEACTIONLOG_NOTES);
		notesItem.setWidth("300");
		notesItem.setHeight(70);

		actionLogForm.setFields(
				caseIdItem,
				actionLogIdItem,
				actionType,
				partyIdItem,
				partyItem,
				actionLogDate,
				notesItem
			);
		
		//validation
		//length
		LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();  
		lengthRangeValidator.setMax(1000);  
		actionLogForm.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_NOTES).setValidators(lengthRangeValidator);
		 
		//required
		actionLogForm.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_ACTIONTYPE).setRequired(true);
		actionLogForm.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_PARTYNAME).setRequired(true);
		actionLogForm.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_DATETIME).setRequired(true);
		actionLogForm.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_NOTES).setRequired(true);
		 
		VLayout actionLogLayout = new VLayout();
		actionLogLayout.setHeight100();
		actionLogLayout.setWidth100();
		actionLogLayout.setMembers(actionLogDetailToolStrip, actionLogForm);
		addEditActionLogWindow.addItem(actionLogLayout);

		saveButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				SC.ask("Are you sure you want to save this data?", new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						if(value != null && value ==  true) {
							actionLogForm.getField(DataNameTokens.FRDFRAUDCASEACTIONLOG_SUSPICIONCASEID).setValue(caseId);							
							if(addEditActionLogWindow.getTitle().toString().toLowerCase().trim().startsWith("add")){
								actionLogForm.setSaveOperationType(DSOperationType.ADD);
							} else {
								actionLogForm.setSaveOperationType(DSOperationType.UPDATE);
							}
							
							actionLogForm.saveData(new DSCallback() {
								@Override
								public void execute(DSResponse response, Object rawData, DSRequest request) {
									//Refresh list gird dan hilangkan detail nya
									DSCallback callBack = new DSCallback() {
										@Override
										public void execute(DSResponse response, Object rawData, DSRequest request) {
											fraudActionLogListGrid.setData(response.getData());
											actionLogForm.clearValues();
											addEditActionLogWindow.hide();
											refreshFraudCaseActionLogData();
											if(response.getStatus()==0){
												SC.say("Data Added/Edited");
											}	
										}
									};								
									fraudActionLogListGrid.getDataSource().fetchData(fraudActionLogListGrid.getFilterEditorCriteria(), callBack);
								}
							});
						}
					}
				});
			}
		});
		
		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				actionLogForm.clearValues();
				addEditActionLogWindow.hide();
			}
		});
		
		addEditActionLogWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				actionLogForm.clearValues();
				addEditActionLogWindow.hide();
			}
		});

		return addEditActionLogWindow;
	}
	
	private Window buildSelectPartyWindow() {
		final Window selectPartyWindow = new Window();
		selectPartyWindow.setWidth(500);
		selectPartyWindow.setHeight(350);
		selectPartyWindow.setTitle("Select Party");
		selectPartyWindow.setShowMinimizeButton(false);
		selectPartyWindow.setIsModal(true);
		selectPartyWindow.setShowModalMask(true);
		selectPartyWindow.centerInPage();
		selectPartyWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				selectPartyWindow.destroy();
			}
		});

		VLayout partyLayout = new VLayout();
		partyLayout.setHeight100();
		partyLayout.setWidth100();

		partyList = new ListGrid();
		partyList.setHeight(300);
		partyList.setSelectionType(SelectionStyle.SIMPLE);
		partyList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		partyList.setShowFilterEditor(true);
		partyList.setAutoFetchData(true);

		partyList.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				DSCallback callBack = new DSCallback() {
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						partyList.setData(response.getData());
					}
				};

				partyList.getDataSource().fetchData(partyList.getFilterEditorCriteria(), callBack);
			}
		});

		DataSource partyDataSource = FraudData.getPartyData();
		partyList.setDataSource(partyDataSource);
		partyList.setFields(Util.getListGridFieldsFromDataSource(partyDataSource));
		partyList.getField(DataNameTokens.VENPARTY_PARTYID).setHidden(true);
				
		HLayout partyButtons = new HLayout(5);
		final IButton buttonSelectParty = new IButton("Select");
		IButton buttonCancel = new IButton("Cancel");

		buttonSelectParty.setDisabled(true);
		buttonSelectParty.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String partyId = partyList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_PARTYID);
				String partyName = partyList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_FULLORLEGALNAME);
				
				partyIdItem.setValue(partyId);
				partyItem.setValue(partyName);
				
				selectPartyWindow.destroy();
			}
		});
		buttonCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectPartyWindow.destroy();
			}
		});
		partyButtons.setAlign(Alignment.CENTER);
		partyButtons.setMembers(buttonSelectParty, buttonCancel);
		
		partyLayout.setMembers(partyList, partyButtons);
		selectPartyWindow.addItem(partyLayout);

		partyList.addSelectionChangedHandler(new SelectionChangedHandler() {			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				buttonSelectParty.setDisabled(partyList.getSelection().length!=1);
			}
		});
		
		return selectPartyWindow;
	}
	
	public void refreshFraudCaseActionLogData() {
		DSCallback callBack = new DSCallback() {			
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				fraudActionLogListGrid.setData(response.getData());
			}
		};
		
		fraudActionLogListGrid.getDataSource().fetchData(fraudActionLogListGrid.getFilterEditorCriteria(), callBack);
	}		
}
