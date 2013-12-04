package com.gdn.venice.client.app.kpi.view;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.kpi.data.KpiData;
import com.gdn.venice.client.app.kpi.presenter.KpiDashboardPresenter;
import com.gdn.venice.client.app.kpi.view.handlers.KpiDashboardUiHandlers;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.client.widgets.ExternalDashboardPortlet;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.IButton;
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
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.PortalLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

/**
 * View for KPI Dashboard
 * 
 * @author Henry Chandra
 */
public class KpiDashboardView extends
ViewWithUiHandlers<KpiDashboardUiHandlers> implements
KpiDashboardPresenter.MyView {
	RafViewLayout kpiDashboardLayout;
	VLayout kpiDashboardMerchantLayout;
	VLayout kpiDashboardLogisticLayout;
	PortalLayout kpiPortalLayout=null;
	ListGrid selectPartyItemList;	
	final TextItem merchantPicker = new TextItem();
	final TextItem merchantPickerID = new TextItem();
	final TextItem logisticsPicker = new TextItem();
	final TextItem logisticsPickerID = new TextItem();
	ListGrid merchantsListGrid = new ListGrid();
	ListGrid listLogisticData = new ListGrid();
	// id of type party
	String typePartyMerchant="1";
	String typePartyLogistics="2";

	@Inject
	public KpiDashboardView() {		
		kpiDashboardLayout = new RafViewLayout();			
	}	
	public void SetUpTabDashboardView(Map <String,String> map, DataSource dataSourceMerchants,DataSource dataSourceLogistics){
		TabSet kpiDashboardTabSet = new TabSet();		
		kpiDashboardTabSet.addTab(buildKpiDashboardMerchantsTab(map,dataSourceMerchants));
		kpiDashboardTabSet.addTab(buildKpiDashboardLogisticsTab(map,dataSourceLogistics));		
		kpiDashboardLayout.setMembers(kpiDashboardTabSet);
		bindCustomUiHandlers();
	}
	private Tab buildKpiDashboardMerchantsTab(Map <String,String> map, DataSource dataSourceMerchants) {
		Tab kpiMerchantsTab = new Tab("Merchants");		
		VLayout kpiMerchantsLayout = new VLayout();		
		ToolStrip kpiMerchantsToolStrip = new ToolStrip();
		kpiMerchantsToolStrip.setWidth100();
		
		final SelectItem slctPeriod = new SelectItem();
		final SelectItem slctMerchants = new SelectItem();
		final SelectItem slctMerchantsKpi = new SelectItem();
		final ToolStripButton btnRemove = new ToolStripButton();

	
		LinkedHashMap<String, String> valueMapTop = new LinkedHashMap<String, String>(); 
		valueMapTop.put("0", "");	
		valueMapTop.put("10", "Top 10 Merchants");
		valueMapTop.put("20", "Top 20 Merchants");	
		valueMapTop.put("", "All Merchants");	
		valueMapTop.values();
		slctMerchants.setTitle("Show");
		slctMerchants.setValueMap(valueMapTop);
		slctMerchants.setDisabled(true);		
		
		slctMerchants.addChangedHandler(new ChangedHandler(){
			@Override
			public void onChanged(ChangedEvent event) {
				if(slctMerchants.getValueAsString().equals("")){
					showCriteriaMerchant(KpiData.getKpiTopMerchantData(slctMerchants.getValueAsString(),"",slctPeriod.getValueAsString(), typePartyMerchant));
				}
				if(kpiPortalLayout!=null){
					kpiPortalLayout.destroy();
				}
				merchantPicker.setValue("");
				merchantPickerID.setValue("");
				if(slctMerchants.getValueAsString().equals("0")||slctMerchants.getValueAsString().equals("")){
					slctMerchantsKpi.setDisabled(true);		
					slctMerchantsKpi.setValue("");
				}else{
					slctMerchantsKpi.setDisabled(false);		
					slctMerchantsKpi.setValue("");
				}
			}		
			
		});		
		
		LinkedHashMap<String, String> valueMapTopKpi = new LinkedHashMap<String, String>(); 
		valueMapTopKpi.put("", "");
		valueMapTopKpi.put("4", "Partial Fulfillment Frequency");	
		valueMapTopKpi.put("5", "Fulfillment Respons Time");
		slctMerchantsKpi.setValueMap(valueMapTopKpi);
		slctMerchantsKpi.setTitle("KPI");
		slctMerchantsKpi.setDisabled(true);		
		slctMerchantsKpi.addChangedHandler(new ChangedHandler(){
			@Override
			public void onChanged(ChangedEvent event) {		
					showCriteriaMerchant(KpiData.getKpiTopMerchantData(slctMerchants.getValueAsString(), slctMerchantsKpi.getValueAsString(),slctPeriod.getValueAsString(), typePartyMerchant));	
			}
		});		
		
		merchantPicker.setTitle("Merchant");
		merchantPicker.setDisabled(true);
	       PickerIcon searchPartyItemPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {  
				public void onFormItemClick(FormItemIconClickEvent event) {
					buildSelectPartyItemWindow(typePartyMerchant,slctPeriod.getValueAsString()).show();
					if(kpiPortalLayout!=null){
						kpiPortalLayout.destroy();
					}
					slctMerchants.setValue("0");
					slctMerchantsKpi.setValue("");
					slctMerchantsKpi.setDisabled(true);					

				}  
			});  
	       merchantPicker.setIcons(searchPartyItemPicker);
		LinkedHashMap<String, String> valueMapPeriod = new LinkedHashMap<String, String>(); 
		valueMapPeriod.putAll(map);
		slctPeriod.setTitle("Period");	
		slctPeriod.setValueMap(valueMapPeriod);

		slctPeriod.addChangedHandler(new ChangedHandler(){
			@Override
			public void onChanged(ChangedEvent event) {
				slctMerchants.setValue("0");
				slctMerchantsKpi.setValue("");
				merchantPicker.setValue("");
				merchantPickerID.setValue("");
				slctMerchants.setDisabled(false);
				merchantPicker.setDisabled(false);
				btnRemove.setDisabled(false);
				showCriteriaMerchant(KpiData.getKpiDashboardData(typePartyMerchant,slctPeriod.getValueAsString(),merchantPickerID.getValueAsString()));
				if(kpiPortalLayout!=null){
					kpiPortalLayout.destroy();
				}
								
			}			
		});		
		
		btnRemove.setIcon("[SKIN]/icons/delete.png");
		btnRemove.setTooltip("Remove Criteria");
		btnRemove.setDisabled(true);
		btnRemove.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				slctPeriod.setValue("");
				slctMerchants.setValue("0");
				slctMerchantsKpi.setValue("");
				merchantPicker.setValue("");
				merchantPickerID.setValue("");
				slctMerchants.setDisabled(true);
				slctMerchantsKpi.setDisabled(true);	
				merchantPicker.setDisabled(true);
				btnRemove.setDisabled(true);
				
			}
		});
		
		kpiMerchantsToolStrip.addFormItem(slctPeriod);
		kpiMerchantsToolStrip.addSeparator();
		kpiMerchantsToolStrip.addFormItem(slctMerchants);
		kpiMerchantsToolStrip.addFormItem(slctMerchantsKpi);
		kpiMerchantsToolStrip.addSeparator();
		kpiMerchantsToolStrip.addFormItem(merchantPicker);
		kpiMerchantsToolStrip.addButton(btnRemove);
		
		merchantsListGrid.setDataSource(dataSourceMerchants);
		
		merchantsListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSourceMerchants));		
		merchantsListGrid.setFields(
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME),
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIDESC),
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE)
		);		
		merchantsListGrid.groupBy(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME);
		merchantsListGrid.getField(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE).setAlign(Alignment.RIGHT);

		merchantsListGrid.setShowRowNumbers(true);
		merchantsListGrid.setShowFilterEditor(true);
		merchantsListGrid.setFilterOnKeypress(true);
		merchantsListGrid.setCanResizeFields(true);
		merchantsListGrid.setCanSort(false);
		kpiDashboardMerchantLayout = new VLayout();
		kpiDashboardMerchantLayout.setHeight("60%");
		
		
		merchantsListGrid.addCellClickHandler(new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				ListGridRecord recordList = merchantsListGrid.getSelectedRecord();
				HashMap<String, String> recordDataMap = new HashMap<String, String>();
				HashMap<String, String> recordDataMapList = new HashMap<String, String>();
				if(recordList!=null){
				recordDataMap.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID, recordList.getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID));
				recordDataMap.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID, recordList.getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID));
				recordDataMapList.put("data0", Util.formXMLfromHashMap(recordDataMap));
				getUiHandlers().onFetchValueBaselineFromPartyTarget(recordDataMapList, merchantsListGrid.getSelectedRecord(),merchantsListGrid.getRecords(),true);
				}
			}
		});
		merchantsListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshMerchantDashboardData();
				}
		});	
		
		kpiMerchantsLayout.setMembers(kpiMerchantsToolStrip, merchantsListGrid, kpiDashboardMerchantLayout);		
		kpiMerchantsTab.setPane(kpiMerchantsLayout);		
		return kpiMerchantsTab;
	}	
	private Tab buildKpiDashboardLogisticsTab(Map <String,String> map, DataSource dataSourceLogistics) {
		Tab kpiLogisticsTab = new Tab("Logistics");		
		VLayout kpiLogisticsLayout = new VLayout();		
		ToolStrip kpiLogisticsToolStrip = new ToolStrip();
		kpiLogisticsToolStrip.setWidth100();
		
		final ToolStripButton btnRemove = new ToolStripButton();
		final SelectItem slctPeriod = new SelectItem();
		LinkedHashMap<String, String> valueMapPeriod = new LinkedHashMap<String, String>(); 
		valueMapPeriod.putAll(map);
		slctPeriod.setTitle("Period");
		slctPeriod.setValueMap(valueMapPeriod);
		slctPeriod.addChangedHandler(new ChangedHandler(){
			@Override
			public void onChanged(ChangedEvent event) {
				logisticsPicker.setDisabled(false);
				btnRemove.setDisabled(false);
				showCriteriaLogistics(KpiData.getKpiDashboardData(typePartyLogistics,slctPeriod.getValueAsString(),logisticsPickerID.getValueAsString()));

				if(kpiPortalLayout!=null){
					kpiPortalLayout.destroy();
				}
								
			}			
		});

		logisticsPicker.setTitle("Logistics");
		logisticsPicker.setDisabled(true);
	       PickerIcon searchPartyItemPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {  
				public void onFormItemClick(FormItemIconClickEvent event) {
					buildSelectPartyItemWindow(typePartyLogistics,slctPeriod.getValueAsString()).show();
					if(kpiPortalLayout!=null){
						kpiPortalLayout.destroy();
					}
				}  
			});  
	       logisticsPicker.setIcons(searchPartyItemPicker);
	       
	       btnRemove.setIcon("[SKIN]/icons/delete.png");
			btnRemove.setTooltip("Remove Criteria");
			btnRemove.setDisabled(true);
			btnRemove.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					slctPeriod.setValue("");
					logisticsPicker.setValue("");
					logisticsPickerID.setValue("");
					logisticsPicker.setDisabled(true);
					btnRemove.setDisabled(true);
					
				}
			});
		listLogisticData.setDataSource(dataSourceLogistics);
		listLogisticData.setFields(Util.getListGridFieldsFromDataSource(dataSourceLogistics));
		listLogisticData.setFields(
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME),
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIDESC),
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE)
		);
		listLogisticData.groupBy(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME);
		Util.formatListGridFieldAsPercent(listLogisticData.getField(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE));
		listLogisticData.setShowRowNumbers(true);
		listLogisticData.setShowFilterEditor(true);
		listLogisticData.setFilterOnKeypress(true);
		listLogisticData.setCanResizeFields(true);	
		
		
		kpiLogisticsToolStrip.addFormItem(slctPeriod);
		kpiLogisticsToolStrip.addSeparator();
		kpiLogisticsToolStrip.addFormItem(logisticsPicker);
		kpiLogisticsToolStrip.addButton(btnRemove);

		kpiDashboardLogisticLayout = new VLayout();
		kpiDashboardLogisticLayout.setHeight("60%");
		
		
		listLogisticData.addCellClickHandler(new CellClickHandler() {			
			@Override
			public void onCellClick(CellClickEvent event) {
				ListGridRecord recordList = listLogisticData.getSelectedRecord();
				HashMap<String, String> recordDataMap = new HashMap<String, String>();
				HashMap<String, String> recordDataMapList = new HashMap<String, String>();
				if(recordList!=null){
				recordDataMap.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID, recordList.getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID));
				recordDataMap.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID, recordList.getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID));
				recordDataMapList.put("data0", Util.formXMLfromHashMap(recordDataMap));
				getUiHandlers().onFetchValueBaselineFromPartyTarget(recordDataMapList, listLogisticData.getSelectedRecord(),listLogisticData.getRecords(),false);
				}
			}
		});
		listLogisticData.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshLogisticstDashboardData();
			}
		});		
		
		
		kpiLogisticsLayout.setMembers(kpiLogisticsToolStrip,listLogisticData,kpiDashboardLogisticLayout);		
		kpiLogisticsTab.setPane(kpiLogisticsLayout);		
		return kpiLogisticsTab;
	}
	
	
	private void showCriteriaMerchant(DataSource dataSourceMerchants){
		merchantsListGrid.setDataSource(dataSourceMerchants);
		merchantsListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSourceMerchants));
		merchantsListGrid.setFields(
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME),
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIDESC),
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE)
		);
		merchantsListGrid.groupBy(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME);
		merchantsListGrid.getField(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE).setAlign(Alignment.RIGHT);

		merchantsListGrid.setShowRowNumbers(true);
		merchantsListGrid.setShowFilterEditor(true);
		merchantsListGrid.setFilterOnKeypress(true);
		merchantsListGrid.setCanResizeFields(true);
		refreshMerchantDashboardData();
	}
	
	private void showCriteriaLogistics(DataSource dataSourceMerchants){
		listLogisticData.setDataSource(dataSourceMerchants);
		listLogisticData.setFields(Util.getListGridFieldsFromDataSource(dataSourceMerchants));
		listLogisticData.setFields(
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME),
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIDESC),
				new ListGridField(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE)
		);
		Util.formatListGridFieldAsPercent(listLogisticData.getField(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE));
		listLogisticData.groupBy(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME);
		listLogisticData.setShowRowNumbers(true);
		listLogisticData.setShowFilterEditor(true);
		listLogisticData.setFilterOnKeypress(true);
		listLogisticData.setCanResizeFields(true);
		refreshLogisticstDashboardData();
	}
	
	
	public void refreshMerchantDashboardData(){
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				merchantsListGrid.setData(response.getData());}
		};		
		merchantsListGrid.getDataSource().fetchData(merchantsListGrid.getFilterEditorCriteria(), callBack);
	}
	
	public void refreshLogisticstDashboardData(){
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				listLogisticData.setData(response.getData());}
		};		
		listLogisticData.getDataSource().fetchData(listLogisticData.getFilterEditorCriteria(), callBack);
		
	}
	
	public void showGauges(Map<String,String> map,ListGridRecord listGridRecord,ListGridRecord[] gridRecord,boolean bol) {
		kpiPortalLayout = new PortalLayout();
		kpiPortalLayout.setShowColumnMenus(false);
		kpiPortalLayout.setWidth100();
		kpiPortalLayout.setHeight100();
		kpiPortalLayout.setNumColumns(1);
		String recParam="{";
		String tempMap="";
		String type="";
		boolean selected=true;
		int count=0;
		// create parameter http
		for(int i=0;i<gridRecord.length;i++){
			if(gridRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID).equals(listGridRecord.getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID))){
				String recKpiID = gridRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID);
				String recKpiValue = gridRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE);	
				String recKpiDesc = gridRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIDESC);	
				String recKpiPeriod= gridRecord[i].getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODDESC);	
				if(count!=0){
					recParam=recParam+"&{";
				}
				tempMap="";
				if(recKpiID.equals(listGridRecord.getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID))){					
					selected=true;
				}else selected=false;
				tempMap="";
				int del=0;
				for(int j=0;j<map.size();j++){
					String[] valueMap=map.get("data"+j).toString().replace("{","").replace("}", "").split("/");						
					if(valueMap[0].equals(recKpiID)){
						if(del!=0){
							tempMap=tempMap+"/";
						}
						tempMap=tempMap+valueMap[2]+":"+valueMap[3];
						del++;
					}else if(valueMap[0].equals("null")){
						type=valueMap[1];
					}
					type=valueMap[1];
				}
				tempMap+="}";
				recParam=recParam+""+recKpiDesc+","+recKpiID+","+recKpiValue+","+recKpiPeriod+","+selected+",Baseline={"+tempMap+"}";				
				count++;
			}
		}
	
		kpiPortalLayout.addPortlet(new ExternalDashboardPortlet(listGridRecord.getAttributeAsString(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME)+" Key Performance Indicators", 
			GWT.getHostPageBaseURL() + "bindows/kpidashboard/DashBoard/ViewDashboard.html?"+recParam+"&type="+type),0,0);
		if(bol){
			kpiDashboardMerchantLayout.setMembers(kpiPortalLayout);
		}else{
			kpiDashboardLogisticLayout.setMembers(kpiPortalLayout);
		}
		
	}	
	
	@Override
	public Widget asWidget() {
		return kpiDashboardLayout;
	}

	protected void bindCustomUiHandlers() {

	}

	private Window buildSelectPartyItemWindow(final String typePerty,final String srtPeriod) {
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

		DataSource selectPartyDataSource = KpiData.getItemParty(typePerty,"");
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
				if(typePartyMerchant.equals(typePerty)){
				merchantPicker.setValue(selectPartyItemList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_FULLORLEGALNAME));
				merchantPickerID.setValue(selectPartyItemList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_PARTYID));
				showCriteriaMerchant(KpiData.getKpiDashboardData(typePartyMerchant,srtPeriod,selectPartyItemList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_PARTYID)));
				}else if(typePartyLogistics.equals(typePerty)){
					logisticsPicker.setValue(selectPartyItemList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_FULLORLEGALNAME));
					logisticsPickerID.setValue(selectPartyItemList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_PARTYID));
					showCriteriaLogistics(KpiData.getKpiDashboardData(typePartyLogistics,srtPeriod,selectPartyItemList.getSelectedRecord().getAttributeAsString(DataNameTokens.VENPARTY_PARTYID)));
				}
							
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
	
	private void refreshSelectPartyData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				selectPartyItemList.setData(response.getData());
			}
		};
		selectPartyItemList.getDataSource().fetchData(selectPartyItemList.getFilterEditorCriteria(), callBack);
	}
	

}
