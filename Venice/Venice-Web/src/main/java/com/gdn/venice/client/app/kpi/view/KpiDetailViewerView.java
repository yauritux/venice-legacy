package com.gdn.venice.client.app.kpi.view;

import java.util.HashMap;
import java.util.Map;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.kpi.presenter.KpiDetailViewerPresenter;
import com.gdn.venice.client.app.kpi.view.handlers.KpiDetailViewerUiHandlers;
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
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;

/**
 * View for KPI Detail Viewer
 * 
 * @author Henry Chandra
 */
public class KpiDetailViewerView extends
ViewWithUiHandlers<KpiDetailViewerUiHandlers> implements
KpiDetailViewerPresenter.MyView {
	private static final int TITLE_HEIGHT = 20;  	
	RafViewLayout kpiDetailViewerLayout;	
	VLayout kpiDashboardMerchantLayout;	
	ListGrid kpiDetailViewerListGrid;
	Map <String,String> mapPeriod = new HashMap<String,String>();
	Map <String,String> mapKpi= new HashMap<String,String>();
	Map <String,String> mapParty= new HashMap<String,String>();
	final TextItem partyItem = new TextItem();
	final TextItem idpartyItem = new TextItem();
	ListGrid selectPartyItemList = new ListGrid();

	@Inject
	public KpiDetailViewerView() {
		kpiDetailViewerLayout = new RafViewLayout();
		
	}	
	public void loadDataKpiDetailViewerView(DataSource dataSource){
		
		dataSource.getField(DataNameTokens.KPIPARTYPERIODTRANSACTION_KPIKEYPERMORMANCEINDICATOR_KPIID).setValueMap(mapKpi);
		dataSource.getField(DataNameTokens.KPIPARTYPERIODTRANSACTION_KPIPARTYMEASUREMENTPERIOD_KPIPERIODID).setValueMap(mapPeriod);
		dataSource.getField(DataNameTokens.KPIPARTYPERIODTRANSACTION_KPIPARTYMEASUREMENTPERIOD_PARTYID).setValueMap(mapParty);
		
		ToolStrip kpiDetailViewerToolStrip = new ToolStrip();
		kpiDetailViewerToolStrip.setWidth100();
		kpiDetailViewerToolStrip.setPadding(2);

		kpiDetailViewerListGrid = new ListGrid();
		kpiDetailViewerListGrid.setDataSource(dataSource);
		kpiDetailViewerListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));
		kpiDetailViewerListGrid.setAutoFetchData(true);
		kpiDetailViewerListGrid.setCanEdit(false);
		kpiDetailViewerListGrid.setCanResizeFields(true);
		kpiDetailViewerListGrid.setShowFilterEditor(true);
		kpiDetailViewerListGrid.setCanSort(true);
		kpiDetailViewerListGrid.setShowRowNumbers(true);
		kpiDetailViewerListGrid.setSelectionType(SelectionStyle.SIMPLE);
		kpiDetailViewerListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);		
		kpiDetailViewerListGrid.getField(DataNameTokens.KPIPARTYPERIODTRANSACTION_ID).setHidden(true);

		kpiDetailViewerListGrid.getField(DataNameTokens.KPIPARTYPERIODTRANSACTION_TRANSACTIONVALUE).setAlign(Alignment.RIGHT);
//		kpiDetailViewerListGrid.getField(DataNameTokens.KPIPARTYPERIODTRANSACTION_TIMESTAMP).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
		bindCustomUiHandlers();
	
		kpiDetailViewerLayout.setMembers(kpiDetailViewerToolStrip, kpiDetailViewerListGrid);
	}
	
	@Override
	public Widget asWidget() {
		return kpiDetailViewerLayout;
	}

	
	private void bindCustomUiHandlers() {
		kpiDetailViewerListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {			
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshKpiDetailViewerViewData();
				}
		});			

	}
	
	public void refreshKpiDetailViewerViewData() {
		DSCallback callBack = new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				kpiDetailViewerListGrid.setData(response.getData());}
		};		
		kpiDetailViewerListGrid.getDataSource().fetchData(kpiDetailViewerListGrid.getFilterEditorCriteria(), callBack);
		
	}
	
	public void setPeriodCombobox(Map<String, String> mapPeriod){
		this.mapPeriod=mapPeriod;
		 
		
	}
	public void setPartyCombobox(Map<String, String> mapParty){
		this.mapParty=mapParty;
		
	}
	
	public void setKpiCombobox(Map<String, String> mapKpi){
		this.mapKpi=mapKpi;
	}	
}
