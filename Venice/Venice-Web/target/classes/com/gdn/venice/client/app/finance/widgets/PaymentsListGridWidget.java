package com.gdn.venice.client.app.finance.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;

/**
 * This is the payment list grid that appears in the payments tab of payment processing
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class PaymentsListGridWidget extends ListGrid {
	
	/**
	 * Basic constructor to setup the list grid
	 */
	public PaymentsListGridWidget() {
		setWidth100();
		setHeight100();
		setShowAllRecords(true);
		setSortField(0);
		
		setSelectionType(SelectionStyle.SIMPLE);
		setSelectionAppearance(SelectionAppearance.CHECKBOX);
		setShowFilterEditor(true);
		
		setAutoFetchData(true);
		
		setCanResizeFields(true);
		
		/*
		 * Attach a filter editor submit handler
		 */
		addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
			
			/**
			 * Called on submit for the filter to refresh the data filtered.
			 */
			@Override
			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
				refreshData();
			}
		});
	}
	
	/**
	 * Refreshes the data for the list grid by going to the server again
	 */
	public void refreshData() {
		DSCallback callBack = new DSCallback() {
			
			/**
			 * Overidden execute method for the callback handler
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				setData(response.getData());
				setGroupStartOpen(GroupStartOpen.ALL);
			}
		};
		
		getDataSource().fetchData(getFilterEditorCriteria(), callBack);
		
		Util.formatListGridFieldAsCurrency(this.getField(DataNameTokens.FINAPPAYMENT_AMOUNT));
		Util.formatListGridFieldAsCurrency(this.getField(DataNameTokens.FINAPPAYMENT_PENALTYAMOUNT));

		
	}
}
