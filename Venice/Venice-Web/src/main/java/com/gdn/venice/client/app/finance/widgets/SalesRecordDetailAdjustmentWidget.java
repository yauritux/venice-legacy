package com.gdn.venice.client.app.finance.widgets;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.util.Util;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * Widget for Sales Record Detail Adjustment
 * 
 * @author Roland
 */

public class SalesRecordDetailAdjustmentWidget extends VLayout {
	
	public SalesRecordDetailAdjustmentWidget(DataSource dataSource) {
		
		setPadding(5);
		setHeight(150);

		final ListGrid adjustmentListGrid = new ListGrid();
		adjustmentListGrid.setSelectionType(SelectionStyle.SIMPLE);
		adjustmentListGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		adjustmentListGrid.setDataSource(dataSource);
		adjustmentListGrid.setAutoFetchData(true);
		ListGridField[] fields = Util.getListGridFieldsFromDataSource(dataSource);
		adjustmentListGrid.setFields(fields);
		adjustmentListGrid.getField(DataNameTokens.VENORDERITEMADJUSTMENT_VENORDERITEM_ORDERITEMID).setHidden(true);
		Util.formatListGridFieldAsCurrency(adjustmentListGrid.getField(DataNameTokens.VENORDERITEMADJUSTMENT_AMOUNT));
		setMembers(adjustmentListGrid);
	}
}
