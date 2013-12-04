package com.gdn.venice.client.app.finance.widgets;

import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent;
import com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler;

public class OrderItemListGridWidget extends ListGrid {
	public OrderItemListGridWidget(){
		setWidth100();
		setHeight100();
		setShowAllRecords(true);
		setSortField(0);
		
		setSelectionType(SelectionStyle.SIMPLE);
		setSelectionAppearance(SelectionAppearance.CHECKBOX);
		setShowFilterEditor(true);
		
		setAutoFetchData(true);
		
		setCanResizeFields(true);		
		
//		addFilterEditorSubmitHandler(new FilterEditorSubmitHandler() {
//			
//			/* (non-Javadoc)
//			 * @see com.smartgwt.client.widgets.grid.events.FilterEditorSubmitHandler#onFilterEditorSubmit(com.smartgwt.client.widgets.grid.events.FilterEditorSubmitEvent)
//			 */
//			@Override
//			public void onFilterEditorSubmit(FilterEditorSubmitEvent event) {
//				refreshOrderItemData();
//			}
//		});
		
		setGroupStartOpen(GroupStartOpen.ALL);
		setShowGroupSummary(true);  
	}
	
	/**
	 * Refreshes the funds in reconciliation data
	 */
	public void refreshOrderItemData() {
		DSCallback callBack = new DSCallback() {
			
			/* (non-Javadoc)
			 * @see com.smartgwt.client.data.DSCallback#execute(com.smartgwt.client.data.DSResponse, java.lang.Object, com.smartgwt.client.data.DSRequest)
			 */
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				setData(response.getData());
				setGroupStartOpen(GroupStartOpen.ALL);
			}
		};
		
		getDataSource().fetchData(getFilterEditorCriteria(), callBack);
		
	}
}
