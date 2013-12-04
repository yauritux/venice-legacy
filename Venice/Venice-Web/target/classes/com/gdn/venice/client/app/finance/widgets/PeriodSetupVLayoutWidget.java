package com.gdn.venice.client.app.finance.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.finance.view.PeriodSetupView;
import com.gdn.venice.client.util.Util;
import com.gwtplatform.mvp.client.View;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.ListGridEditEvent;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.EditCompleteEvent;
import com.smartgwt.client.widgets.grid.events.EditCompleteHandler;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * PeriodSetupVLayoutWidget - a widget class for displaying periods
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PeriodSetupVLayoutWidget extends VLayout {
	private static final int LIST_HEIGHT = 230;
	
	/*
	 * The main list grid for the periods
	 */
	ListGrid periodListGrid;

	/*
	 * The vertical layout used to lay out the top periods list
	 */
	VLayout periodListLayout;

	/*
	 * The period view object
	 */
	View view;
	
	/**
	 * Constructor that takes the view and instantiates 
	 * it for reference as well as builds the UI
	 * @param view
	 */
	public PeriodSetupVLayoutWidget(View view) {

		this.view = view;

		setWidth100();
		setHeight100();

		periodListLayout = new VLayout();
		periodListLayout.setHeight(LIST_HEIGHT);
		periodListLayout.setShowResizeBar(true);

		periodListGrid = new ListGrid();

		periodListGrid.setShowAllRecords(true);
		periodListGrid.setSortField(0);

		periodListGrid.setSelectionType(SelectionStyle.SIMPLE);
		periodListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		periodListGrid.setShowFilterEditor(true);
		periodListGrid.setCanEdit(true);
		periodListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		periodListGrid.setAutoFetchData(false);
		periodListGrid.setCanResizeFields(true);
		periodListGrid.setShowRowNumbers(true);
		periodListGrid.addEditCompleteHandler(new EditCompleteHandler() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.widgets.grid.events.EditCompleteHandler#onEditComplete(com.smartgwt.client.widgets.grid.events.EditCompleteEvent)
			 */
			@Override
			public void onEditComplete(EditCompleteEvent event) {				
				periodListGrid.saveAllEdits();
				periodListGrid.clearCriteria();
				refreshPeriodData();
				
			}
		});
		
		periodListGrid.addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
					/* (non-Javadoc)
					 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
					 */
					@Override
					public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
						refreshPeriodData();
					}
				});
		setMembers(periodListLayout);
	}
	
	/**
	 * Refreshes the period data from the data source
	 */
	public void refreshPeriodData() {
		DSCallback callBack = new DSCallback() {
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				periodListGrid.setData(response.getData());
			}
		};
		periodListGrid.getDataSource().fetchData(periodListGrid.getFilterEditorCriteria(), callBack);
	}
	
	/**
	 * Loads the list of Period data 
	 * 
	 * @param dataSource
	 *            period data datasource
	 */
	public void loadPeriodData(DataSource dataSource) {
		periodListGrid.setDataSource(dataSource);
		periodListGrid.setCanEdit(true);
		periodListGrid.setEditEvent(ListGridEditEvent.DOUBLECLICK);
		periodListGrid.setCanSort(true);
		periodListGrid.setFields(Util.getListGridFieldsFromDataSource(dataSource));

		periodListGrid.getField(DataNameTokens.FINPERIOD_PERIODID).setWidth("5%");
		periodListGrid.getField(DataNameTokens.FINPERIOD_PERIODID).setHidden(true);
		periodListGrid.getField(DataNameTokens.FINPERIOD_PERIODDESC).setRequired(true);
		periodListGrid.getField(DataNameTokens.FINPERIOD_PERIODDESC).setWidth("10%");
		periodListGrid.getField(DataNameTokens.FINPERIOD_PERIODDESC).setHidden(false);
		periodListGrid.getField(DataNameTokens.FINPERIOD_FROMDATETIME).setRequired(true);
		periodListGrid.getField(DataNameTokens.FINPERIOD_FROMDATETIME).setWidth("10%");
		periodListGrid.getField(DataNameTokens.FINPERIOD_FROMDATETIME).setHidden(false);
		periodListGrid.getField(DataNameTokens.FINPERIOD_FROMDATETIME).setCanFilter(false);
		periodListGrid.getField(DataNameTokens.FINPERIOD_FROMDATETIME).setType(ListGridFieldType.DATE); 
//		periodListGrid.getField(DataNameTokens.FINPERIOD_FROMDATETIME).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);
		periodListGrid.getField(DataNameTokens.FINPERIOD_TODATETIME).setRequired(true);
		periodListGrid.getField(DataNameTokens.FINPERIOD_TODATETIME).setWidth("10%");
		periodListGrid.getField(DataNameTokens.FINPERIOD_TODATETIME).setHidden(false);
		periodListGrid.getField(DataNameTokens.FINPERIOD_TODATETIME).setType(ListGridFieldType.DATE); 
//		periodListGrid.getField(DataNameTokens.FINPERIOD_TODATETIME).setDateFormatter(DateDisplayFormat.TOEUROPEANSHORTDATE);

		periodListLayout.addMember(periodListGrid);
	}
	
	/**
	 * Method to bind the custom UI handlers to the view
	 */
	public void bindCustomUiHandlers() {
		periodListGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

		/* (non-Javadoc)
		 * @see com.smartgwt.client.widgets.grid.events.SelectionChangedHandler#onSelectionChanged(com.smartgwt.client.widgets.grid.events.SelectionEvent)
		 */
		@Override
		public void onSelectionChanged(SelectionEvent event) {
			ListGridRecord[] selectedRecords = periodListGrid.getSelection();		
			/*
			 * Enable the remove button if there is a selected row
			 */
				if (selectedRecords.length == 0) {
					((PeriodSetupView) view).getRemoveButton().disable();
				} else {
					((PeriodSetupView) view).getRemoveButton().enable();
				}
			}
		});
	}
	
	/**
	 * Returns the period list grid object
	 * @return the periodListGrid
	 */
	public ListGrid getPeriodListGrid() {
		return periodListGrid;
	}
}