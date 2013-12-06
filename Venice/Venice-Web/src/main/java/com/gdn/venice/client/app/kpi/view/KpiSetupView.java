package com.gdn.venice.client.app.kpi.view;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.kpi.data.KpiData;
import com.gdn.venice.client.app.kpi.presenter.KpiSetupPresenter;
import com.gdn.venice.client.app.kpi.view.handlers.KpiSetupUiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.form.validator.IntegerRangeValidator;
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
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for KPI Setup
 * 
 * @author Henry Chandra
 */
public class KpiSetupView extends
ViewWithUiHandlers<KpiSetupUiHandlers> implements
KpiSetupPresenter.MyView {
	RafViewLayout kpiSetupLayout;
	ListGrid kpiPeriodListGrid = new ListGrid();
	ListGrid kpiPartiSlaListGrid;
	final TextItem partyItem = new TextItem();
	final TextItem partyItemId = new TextItem();
	ListGrid selectPartyItemList;	
    ListGrid selectPartyListGrid;	
	public Map<String, String> tempMapKpi = new HashMap<String, String>();
	public Map<String, String> tempMapPeriod = new HashMap<String, String>();
	public Map<String, String> tempMapBaseline = new HashMap<String, String>();	

	@Inject
	public KpiSetupView() {		
		kpiSetupLayout = new RafViewLayout();	
		
		}	

	public void setComboBoxKpi(Map <String,String> map){
		this.tempMapKpi=map;
	}	
	public void setComboBoxPeriod(Map <String,String> map){
		this.tempMapPeriod=map;
	}	
	public void setComboBoxBaseline(Map <String,String> map){
		this.tempMapBaseline=map;
		setTab();
	}	
	
	public void setTab(){	
	TabSet kpiSetupTabSet = new TabSet();
	kpiSetupTabSet.addTab(buildKpiPeriodsTab(KpiData.getKpiSetupPeriodData()));
	kpiSetupTabSet.addTab(buildKpiPartySlaTab(KpiData.getKpiSetupPartySlaData("","")));
	kpiSetupTabSet.addTabSelectedHandler(new TabSelectedHandler() {		
		@Override
		public void onTabSelected(TabSelectedEvent event) {
		}
	});
	kpiSetupLayout.setMembers(kpiSetupTabSet);	
	
	}

	private Tab buildKpiPeriodsTab(DataSource dataSource) {
		Tab kpiPeriodsTab = new Tab("Periods");		
		VLayout kpiPeriodsLayout = new VLayout();		
		
		ToolStrip kpiPeriodsToolStrip = new ToolStrip();
		kpiPeriodsToolStrip.setWidth100();
		
		ToolStripButton btnAdd = new ToolStripButton();
		btnAdd.setIcon("[SKIN]/icons/add.png");
		btnAdd.setTooltip("Add to Periods");
		btnAdd.setTitle("Add");
		
		ToolStripButton btnRemove = new ToolStripButton();
		btnRemove.setIcon("[SKIN]/icons/remove.png");
		btnRemove.setTooltip("Remove from Periods");
		btnRemove.setTitle("Remove");

		kpiPeriodListGrid.setDataSource(dataSource);
		kpiPeriodListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		kpiPeriodListGrid.setAutoFetchData(true);
		kpiPeriodListGrid.setCanEdit(true);
		kpiPeriodListGrid.setCanResizeFields(true);
		kpiPeriodListGrid.setShowFilterEditor(true);
		kpiPeriodListGrid.setCanSort(true);
		kpiPeriodListGrid.setSelectionType(SelectionStyle.SIMPLE);
		kpiPeriodListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		kpiPeriodListGrid.setShowRowNumbers(true);
		kpiPeriodListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);		
		kpiPeriodListGrid.getField(DataNameTokens.KPIMEASUREMENTPERIOD_PERIODID).setCanEdit(false);
		kpiPeriodListGrid.getField(DataNameTokens.KPIMEASUREMENTPERIOD_PERIODID).setWidth(120);
//		kpiPeriodListGrid.getField(DataNameTokens.KPIMEASUREMENTPERIOD_FROMDATETIME).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
//		kpiPeriodListGrid.getField(DataNameTokens.KPIMEASUREMENTPERIOD_TODATETIME).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
		
		//validasi
		kpiPeriodListGrid.getField(DataNameTokens.KPIMEASUREMENTPERIOD_DESCRIPTION).setRequired(true);
		kpiPeriodListGrid.getField(DataNameTokens.KPIMEASUREMENTPERIOD_FROMDATETIME).setRequired(true);
		kpiPeriodListGrid.getField(DataNameTokens.KPIMEASUREMENTPERIOD_TODATETIME).setRequired(true);
		kpiPeriodListGrid.getField(DataNameTokens.KPIMEASUREMENTPERIOD_FROMDATETIME).setType(ListGridFieldType.DATE);   
		kpiPeriodListGrid.getField(DataNameTokens.KPIMEASUREMENTPERIOD_TODATETIME).setType(ListGridFieldType.DATE);  
		
		LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();  
		lengthRangeValidator.setMax(100);  
		kpiPeriodListGrid.getField(DataNameTokens.KPIMEASUREMENTPERIOD_DESCRIPTION).setValidators(lengthRangeValidator);
		btnAdd.addClickHandler(new ClickHandler() {				
			@Override
			public void onClick(ClickEvent event) {
				kpiPeriodListGrid.startEditingNew();		
				
			}
		});
	
		
		kpiPeriodListGrid.addEditCompleteHandler(new EditCompleteHandler() {			
			@Override
			public void onEditComplete(EditCompleteEvent event) {				
					kpiPeriodListGrid.saveAllEdits();		
					SC.say("Data Added/Updated");							
					refreshKpiSetupPeriodData(); 
				}
		});
				
		btnRemove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(kpiPeriodListGrid.getSelection().length!=0){
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {
						if(value != null && value){
							kpiPeriodListGrid.removeSelectedData();
							SC.say("Data Removed");							
							refreshKpiSetupPeriodData(); 
							}
						}
					});
				}else
					SC.say("Please select the data to be Removed");
				}
			});
			
		
		kpiPeriodsToolStrip.addButton(btnAdd);
		kpiPeriodsToolStrip.addButton(btnRemove);
		
		kpiPeriodListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshKpiSetupPeriodData();
				}
		});	

		kpiPeriodsLayout.setMembers(kpiPeriodsToolStrip, kpiPeriodListGrid);
		
		kpiPeriodsTab.setPane(kpiPeriodsLayout);
		return kpiPeriodsTab;
	}

	
	private Tab buildKpiPartySlaTab(DataSource dataSource) {
		Tab kpiPartySlaTab = new Tab("Party SLA");		
		VLayout kpiPartySlaLayout = new VLayout();
		
		ToolStrip partySlaToolStrip = new ToolStrip();
		partySlaToolStrip.setWidth100();
		
		ToolStripButton btnAdd = new ToolStripButton();
		btnAdd.setIcon("[SKIN]/icons/add.png");
		btnAdd.setTooltip("Add to Party Sla");
		btnAdd.setTitle("Add");
			
		ToolStripButton btnRemove = new ToolStripButton();
		btnRemove.setIcon("[SKIN]/icons/remove.png");
		btnRemove.setTooltip("Remove from Party Sla");
		btnRemove.setTitle("Remove");
		
		ToolStripButton saveButton = new ToolStripButton();  
		saveButton.setIcon("[SKIN]/icons/save.png");  
		saveButton.setTooltip("Save Current Party Sla");
		saveButton.setTitle("Save");
		
		
		
		final SelectItem selectItem = new SelectItem(); 
		LinkedHashMap<String, String> valueMapKPI = new LinkedHashMap<String, String>(); 
		valueMapKPI.put("false", "");
		valueMapKPI.put("1", "Merchant");
		valueMapKPI.put("2", "Logistic Provider");
		valueMapKPI.put("", "Show All");
		selectItem.setTitle("Party&nbsp;Type");
		selectItem.setValueMap(valueMapKPI);
		selectItem.setDefaultValue("false");
		selectItem.addChangedHandler(new ChangedHandler(){
			@Override
			public void onChanged(ChangedEvent event) {
				if(!selectItem.getValueAsString().equals("false")){
					ShowRecordFromFilter(selectItem.getValueAsString(),"");				
					partyItem.setValue("");
					partyItemId.setValue("");
				}
			}
				
			
		});		
		
		// SEARCH for party
		partyItem.setTitle("Party");
       PickerIcon searchPartyItemPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {  
			public void onFormItemClick(FormItemIconClickEvent event) {
				buildSelectPartyItemWindow(selectItem.getValueAsString()).show();
			}  
		});  
       partyItem.setIcons(searchPartyItemPicker);
 	    
       ToolStripButton btnRm = new ToolStripButton();
		btnRm.setIcon("[SKIN]/icons/delete.png");
		btnRm.setTooltip("Remove Criteria");
		btnRm.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				partyItem.setValue("");
				partyItemId.setValue("");
				selectItem.setValue("false");
			}
		});
              
      kpiPartiSlaListGrid= new ListGrid(){    	  
			@Override  
			protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {  
				
				final String fieldName = this.getFieldName(colNum);  
				
				if (fieldName.equals(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME)) {  		
		
					HLayout layout = new HLayout();
					layout.setWidth100();
					layout.setHeight(18);
					
					Label label  = new Label();
					label.setHeight(18);
					label.setWidth100();
					
					IButton button = new IButton();  
					button.setHeight(18);
					button.setWidth(18);
					button.setIcon("[SKIN]/icons/search.png");  
					
					button.addClickHandler(new ClickHandler() {
						
						@Override
						public void onClick(ClickEvent event) { 
							if (fieldName.equals(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME)) {
								if(selectItem.getValueAsString().equals("false")){
									buildSelectPartyListWindow(record,"").show();		
								}else{
									buildSelectPartyListWindow(record,selectItem.getValueAsString()).show();		

								}
							}
						}
					});
					
					layout.setMembers(label, button);
					return layout; 
					
				} else
					return null;
			}
		};             
		
		kpiPartiSlaListGrid.setSaveLocally(true);
		kpiPartiSlaListGrid.setShowAllRecords(true);
		kpiPartiSlaListGrid.setSortField(0);
		kpiPartiSlaListGrid.setCanResizeFields(true);
		kpiPartiSlaListGrid.setDataSource(dataSource);	
		kpiPartiSlaListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));			
		kpiPartiSlaListGrid.setCanEdit(true);				
	    kpiPartiSlaListGrid.setShowFilterEditor(true);
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID).setHidden(true);
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID).setCanEdit(false);
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID).setTitle("ID Party Target");
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLAID).setHidden(true);
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLAID).setCanEdit(false);
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLAID).setTitle("ID Party Sla");
    	kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID).setTitle("KPI");
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID).setValueMap(tempMapKpi);	
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID).setTitle("Id Party");
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID).setCanEdit(false);
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID).setHidden(true);
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME).setTitle("Party");
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME).setCanEdit(false);	
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID).setTitle("Baseline");
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID).setValueMap(tempMapBaseline);
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE).setTitle("Target");
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE).setAlign(Alignment.RIGHT);

		//Util.formatListGridFieldAsPercent(kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE));

		
		kpiPartiSlaListGrid.setShowRecordComponents(true);          
		kpiPartiSlaListGrid.setShowRecordComponentsByCell(true);  
		kpiPartiSlaListGrid.setSelectionType(SelectionStyle.SIMPLE);
		kpiPartiSlaListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		kpiPartiSlaListGrid.setShowRowNumbers(true);
		
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE).setType(ListGridFieldType.INTEGER); 
		//validasi
		  IntegerRangeValidator integerRangeValidator = new IntegerRangeValidator();  
	      integerRangeValidator.setMin(1);  
	      kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE).setValidators(integerRangeValidator);
		
		btnAdd.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {					
					kpiPartiSlaListGrid.startEditingNew();	
					
			}
		});
		btnRemove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if(kpiPartiSlaListGrid.getSelection().length!=0){
				SC.ask("Are you sure you want to delete this data?", new BooleanCallback() {					
					@Override
					public void execute(Boolean value) {		
						if(value != null && value){
					ListGridRecord[] pastySlaDataMapDetailRecord = kpiPartiSlaListGrid.getSelection();
						HashMap<String, String> pastySlaDataMap = new HashMap<String, String>();
						HashMap<String, String> pastySlaDataMapList = new HashMap<String, String>();
						int count=0;
						for (int i=0;i<pastySlaDataMapDetailRecord.length;i++) {	
							//count=0;
							if(pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID)!=null){
							pastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID));
								pastySlaDataMapList.put("data"+count, Util.formXMLfromHashMap(pastySlaDataMap));	
							count++;			
							}		
							if(count!=0){
								getUiHandlers().deleteKpiSetupPartySlaDataCommand(pastySlaDataMapList);	
							}							
						}																						
								kpiPartiSlaListGrid.removeSelectedData();			
					}
						}
					});
				}else
					SC.say("Please select the data to be Removed");
				}
			});
		
			saveButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {						
				HashMap<String, String> addNewPastySlaDataMap = new HashMap<String, String>();
				HashMap<String, String> addNewPastySlaDataMapList = new HashMap<String, String>();
				HashMap<String, String> updatePastySlaDataMap = new HashMap<String, String>();
				HashMap<String, String> updatePastySlaDataMapList = new HashMap<String, String>();
				
				ListGridRecord[] pastySlaDataMapDetailRecord = kpiPartiSlaListGrid.getSelection();
				int indexAddError=0;
				int indexAdd=0;
				int indexUpdate=0;
				if(pastySlaDataMapDetailRecord.length!=0){
				for (int i=0;i<pastySlaDataMapDetailRecord.length;i++) {
					
					if(pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID)==null&&
							pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID)!=null&&
							pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID)!=null){	
						
						addNewPastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLAID, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLAID));
						addNewPastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID));
						addNewPastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID));
						addNewPastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME));
						addNewPastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID));
						addNewPastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE));
						addNewPastySlaDataMapList.put("data"+indexAdd, Util.formXMLfromHashMap(addNewPastySlaDataMap));
						indexAdd++;						
		
					}else if(pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID)!=null){	
						updatePastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID));
						updatePastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLAID, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLAID));
						updatePastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID));
						updatePastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID));
						updatePastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME));
						updatePastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID, pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID));
						updatePastySlaDataMap.put(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE,pastySlaDataMapDetailRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE).toString().replace("%","").trim());
						updatePastySlaDataMapList.put("data"+indexUpdate, Util.formXMLfromHashMap(updatePastySlaDataMap));
						indexUpdate++;					
					}else{
						indexAddError++;						
					}				
				}	
				if(indexAdd!=0&&indexAddError==0){
					getUiHandlers().addKpiSetupPartySlaDataCommand(addNewPastySlaDataMapList);
				}
				if(indexUpdate!=0&&indexAddError==0){
					getUiHandlers().updateKpiSetupPartySlaDataCommand(updatePastySlaDataMapList);
				}
				if(indexAddError!=0){
					SC.say("Please check again of data is selected to be saved/Updated");	
				}
			}else
				SC.say("Please select the data to be Saved/Updated");
			 }				
			
			});
	       
			partySlaToolStrip.addButton(btnAdd);
			partySlaToolStrip.addButton(btnRemove);
			partySlaToolStrip.addButton(saveButton);
			partySlaToolStrip.addSeparator();
			partySlaToolStrip.addFormItem(selectItem);
			partySlaToolStrip.addFormItem(partyItem);
			partySlaToolStrip.addButton(btnRm);
			
			kpiPartiSlaListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
				@Override
				public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {					
					refreshKpiSetupPartySlaData();
					}
			});		
			
		kpiPartySlaLayout.setMembers(partySlaToolStrip, kpiPartiSlaListGrid);	
		kpiPartySlaTab.setPane(kpiPartySlaLayout);
		
		return kpiPartySlaTab;
	}
	
	@Override
	public Widget asWidget() {
		return kpiSetupLayout;
	}
	public void refreshKpiSetupPeriodData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				kpiPeriodListGrid.setData(response.getData());}
		};		
		kpiPeriodListGrid.getDataSource().fetchData(kpiPeriodListGrid.getFilterEditorCriteria(), callBack);
		
	}	
	public void refreshKpiSetupPartySlaData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				kpiPartiSlaListGrid.setData(response.getData());}
		};		
		kpiPartiSlaListGrid.getDataSource().fetchData(kpiPartiSlaListGrid.getFilterEditorCriteria(), callBack);
		
	}
	
		
	private Window buildSelectPartyItemWindow(String IdTypeVenParty) {
		final Window selectProdCatWindow = new Window();
		selectProdCatWindow.setWidth(500);
		selectProdCatWindow.setHeight(350);
		selectProdCatWindow.setTitle("Select Party Category");
		selectProdCatWindow.setShowMinimizeButton(false);
		selectProdCatWindow.setIsModal(true);
		selectProdCatWindow.setShowModalMask(true);
		selectProdCatWindow.centerInPage();
		selectProdCatWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				selectProdCatWindow.destroy();
			}
		});

		VLayout selectPartyLayout = new VLayout();
		selectPartyLayout.setHeight100();
		selectPartyLayout.setWidth100();

		selectPartyItemList = new ListGrid();
		selectPartyItemList.setHeight(300);
		selectPartyItemList.setSelectionType(SelectionStyle.SIMPLE);
		selectPartyItemList.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		selectPartyItemList.setShowFilterEditor(true);
		selectPartyItemList.setAutoFetchData(true);

		selectPartyItemList.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {

			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshSelectPartyData();
			}
		});
		if(IdTypeVenParty.equals("false")){
			IdTypeVenParty="";
		}
		DataSource selectPartyDataSource = KpiData.getItemParty(IdTypeVenParty,"");
		selectPartyItemList.setDataSource(selectPartyDataSource);
		selectPartyItemList.setFields(Util.getListGridFieldsFromDataSource(selectPartyDataSource));
		selectPartyItemList.getField(DataNameTokens.VENPARTY_PARTYID).setHidden(true);				
		HLayout selectProdCatButtons = new HLayout(5);
		final IButton buttonSelectProdCat = new IButton("Select");
		IButton buttonCancel = new IButton("Cancel");
		buttonSelectProdCat.setDisabled(true);
		buttonSelectProdCat.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				partyItemId.setValue(selectPartyItemList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_PARTYID));
				partyItem.setValue(selectPartyItemList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_FULLORLEGALNAME));
				ShowRecordFromFilter("",partyItemId.getValueAsString());		
				selectProdCatWindow.destroy();
			}
		});
		buttonCancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				selectProdCatWindow.destroy();
			}
		});
		selectProdCatButtons.setAlign(Alignment.CENTER);
		selectProdCatButtons.setMembers(buttonSelectProdCat, buttonCancel);		
		selectPartyLayout.setMembers(selectPartyItemList, selectProdCatButtons);
		selectProdCatWindow.addItem(selectPartyLayout);
		selectPartyItemList.addSelectionChangedHandler(new SelectionChangedHandler() {
				@Override
			public void onSelectionChanged(SelectionEvent event) {
				buttonSelectProdCat.setDisabled(selectPartyItemList.getSelection().length!=1);
			}
		});
		
		return selectProdCatWindow;
	}
	
	private void ShowRecordFromFilter(String idTypeParty,String idParty){
		DataSource datasource =KpiData.getKpiSetupPartySlaData(idTypeParty,idParty);
		datasource.getField(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID).setValueMap(tempMapKpi);	
		datasource.getField(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID).setValueMap(tempMapBaseline);	
		
		kpiPartiSlaListGrid.setDataSource(datasource);				
		kpiPartiSlaListGrid.setShowAllRecords(true);
		kpiPartiSlaListGrid.setSortField(0);
		kpiPartiSlaListGrid.setCanResizeFields(true);
		kpiPartiSlaListGrid.setFields(Util.getListGridFieldsFromDataSource(datasource));
		kpiPartiSlaListGrid.setFields(
				new ListGridField(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID),
				new ListGridField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME),
				new ListGridField(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID),
				new ListGridField(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE)
		);
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE).setAlign(Alignment.RIGHT);
	   kpiPartiSlaListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);	
		kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME).setCanEdit(false);
		//validasi
		  IntegerRangeValidator integerRangeValidator = new IntegerRangeValidator();  
	      integerRangeValidator.setMin(1);  
	      kpiPartiSlaListGrid.getField(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE).setValidators(integerRangeValidator);		
		refreshKpiSetupPartySlaData();	
	}
	
	private void refreshSelectPartyData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				selectPartyItemList.setData(response.getData());
			}
		};
		selectPartyItemList.getDataSource().fetchData(selectPartyItemList.getFilterEditorCriteria(), callBack);
	}
	
	private Window buildSelectPartyListWindow(final ListGridRecord record,String idTypeParty) {
		final Window selectPartyListWindow = new Window();
		selectPartyListWindow.setWidth(500);
		selectPartyListWindow.setHeight(350);
		selectPartyListWindow.setTitle("Select Party Category");
		selectPartyListWindow.setShowMinimizeButton(false);
		selectPartyListWindow.setIsModal(true);
		selectPartyListWindow.setShowModalMask(true);
		selectPartyListWindow.centerInPage();
		selectPartyListWindow.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClientEvent event) {
				selectPartyListWindow.destroy();
			}
		});
		VLayout selectPartyLayout = new VLayout();
		selectPartyLayout.setHeight100();
		selectPartyLayout.setWidth100();
		
		selectPartyListGrid = new ListGrid();
		selectPartyListGrid.setHeight(300);
		selectPartyListGrid.setSelectionType(SelectionStyle.SIMPLE);
		selectPartyListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		selectPartyListGrid.setShowFilterEditor(true);
		selectPartyListGrid.setAutoFetchData(true);
		selectPartyListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshSelectPartyListData();
			}
		});
		DataSource selectPartyDataSource = KpiData.getItemParty(idTypeParty,partyItemId.getValueAsString());
		selectPartyListGrid.setDataSource(selectPartyDataSource);
		selectPartyListGrid.setFields(Util.getListGridFieldsFromDataSource(selectPartyDataSource));
		selectPartyListGrid.getField(DataNameTokens.VENPARTY_PARTYID).setHidden(true);
		HLayout selectProdCatButtons = new HLayout(5);
		final IButton buttonSelectProdCat = new IButton("Select");
		IButton buttonCancel = new IButton("Cancel");
		buttonSelectProdCat.setDisabled(true);
		buttonSelectProdCat.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String partyId = selectPartyListGrid.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_PARTYID);
				String party = selectPartyListGrid.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_FULLORLEGALNAME);
				record.setAttribute(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID, partyId);
				record.setAttribute(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME, party);
				kpiPartiSlaListGrid.redraw();
				selectPartyListWindow.destroy();
			}
		});
		
		
		buttonCancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				selectPartyListWindow.destroy();
			}
		});
		selectProdCatButtons.setAlign(Alignment.CENTER);
		selectProdCatButtons.setMembers(buttonSelectProdCat, buttonCancel);		
		selectPartyLayout.setMembers(selectPartyListGrid, selectProdCatButtons);
		selectPartyListWindow.addItem(selectPartyLayout);
		selectPartyListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {			
			@Override
			public void onSelectionChanged(SelectionEvent event) {
				buttonSelectProdCat.setDisabled(selectPartyListGrid.getSelection().length!=1);
			}
		});		
		return selectPartyListWindow;
	}
	
	private void refreshSelectPartyListData() {		
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				selectPartyListGrid.setData(response.getData());
			}
		};
		selectPartyListGrid.getDataSource().fetchData(selectPartyListGrid.getFilterEditorCriteria(), callBack);
	}
}