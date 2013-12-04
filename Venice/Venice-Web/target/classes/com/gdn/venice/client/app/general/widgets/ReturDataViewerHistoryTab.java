package com.gdn.venice.client.app.general.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;

public class ReturDataViewerHistoryTab extends Tab {
	ListGrid returHistoryReturListGrid;
	ListGrid returHistoryReturItemListGrid;

	public ReturDataViewerHistoryTab(String title, DataSource historyReturData, DataSource historyReturItemData) {
		super(title);
		
		HLayout returHistoryLayout  = new HLayout();
		
		Label returHistoryReturLabel = new Label("<b>Retur:</b>");
		returHistoryReturLabel.setHeight(10);
		
		returHistoryReturListGrid = new ListGrid();		
		returHistoryReturListGrid.setWidth100();
		returHistoryReturListGrid.setHeight100();
		returHistoryReturListGrid.setShowAllRecords(true);
		returHistoryReturListGrid.setCanResizeFields(true);
		returHistoryReturListGrid.setShowRowNumbers(true);
		returHistoryReturListGrid.setAutoFetchData(true);
			
		returHistoryReturListGrid.setDataSource(historyReturData);
		returHistoryReturListGrid.setFields(Util.getListGridFieldsFromDataSource(historyReturData));
		
		returHistoryReturListGrid.getField(DataNameTokens.VENRETURSTATUSHISTORY_RETURID).setWidth(100);
		returHistoryReturListGrid.getField(DataNameTokens.VENRETURSTATUSHISTORY_WCSRETURID).setWidth(100);
		returHistoryReturListGrid.getField(DataNameTokens.VENRETURSTATUSHISTORY_TIMESTAMP).setWidth(120);
		returHistoryReturListGrid.getField(DataNameTokens.VENRETURSTATUSHISTORY_VENRETURSTATUS_ORDERSTATUSCODE).setWidth(100);		
		returHistoryReturListGrid.getField(DataNameTokens.VENRETURSTATUSHISTORY_STATUSCHANGEREASON).setWidth(200);
		
		returHistoryReturListGrid.getField(DataNameTokens.VENRETURSTATUSHISTORY_RETURID).setHidden(true);
		returHistoryReturListGrid.getField(DataNameTokens.VENRETURSTATUSHISTORY_WCSRETURID).setHidden(true);
		returHistoryReturListGrid.groupBy(DataNameTokens.VENRETURSTATUSHISTORY_WCSRETURID);
		
		VLayout returHistoryReturLayout = new VLayout();
		returHistoryReturLayout.setMembers(returHistoryReturLabel, returHistoryReturListGrid);
		
		Label returHistoryReturItemLabel = new Label("<b>Retur Item:</b>");
		returHistoryReturItemLabel.setHeight(10);
		
		returHistoryReturItemListGrid = new ListGrid();		
		returHistoryReturItemListGrid.setWidth100();
		returHistoryReturItemListGrid.setHeight100();
		returHistoryReturItemListGrid.setShowAllRecords(true);

		returHistoryReturItemListGrid.setCanResizeFields(true);
		returHistoryReturItemListGrid.setShowRowNumbers(true);
		returHistoryReturItemListGrid.setAutoFetchData(true);			
		returHistoryReturItemListGrid.setDataSource(historyReturItemData);
		returHistoryReturItemListGrid.setFields(Util.getListGridFieldsFromDataSource(historyReturItemData));
		
		returHistoryReturItemListGrid.getField(DataNameTokens.VENRETURITEMSTATUSHISTORY_RETURITEMID).setWidth(100);
		returHistoryReturItemListGrid.getField(DataNameTokens.VENRETURITEMSTATUSHISTORY_WCSRETURITEMID).setWidth(100);
		returHistoryReturItemListGrid.getField(DataNameTokens.VENRETURITEMSTATUSHISTORY_TIMESTAMP).setWidth(120);
		returHistoryReturItemListGrid.getField(DataNameTokens.VENRETURITEMSTATUSHISTORY_VENRETURSTATUS_ORDERSTATUSCODE).setWidth(100);		
		returHistoryReturItemListGrid.getField(DataNameTokens.VENRETURITEMSTATUSHISTORY_STATUSCHANGEREASON).setWidth(200);
		
		returHistoryReturItemListGrid.getField(DataNameTokens.VENRETURITEMSTATUSHISTORY_RETURITEMID).setHidden(true);
		returHistoryReturItemListGrid.getField(DataNameTokens.VENRETURITEMSTATUSHISTORY_WCSRETURITEMID).setHidden(true);
		returHistoryReturItemListGrid.groupBy(DataNameTokens.VENRETURITEMSTATUSHISTORY_WCSRETURITEMID);
						
		VLayout returHistoryReturItemLayout = new VLayout();
		returHistoryReturItemLayout.setMembers(returHistoryReturItemLabel, returHistoryReturItemListGrid);
		
		returHistoryLayout.setPadding(5);
		returHistoryLayout.setMembersMargin(5);
		returHistoryLayout.setMembers(returHistoryReturLayout, returHistoryReturItemLayout);
		
		setPane(returHistoryLayout);
	}
}
