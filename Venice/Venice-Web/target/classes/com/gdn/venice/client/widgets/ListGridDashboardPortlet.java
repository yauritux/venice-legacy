package com.gdn.venice.client.widgets;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Hilite;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.Portlet;

public class ListGridDashboardPortlet extends Portlet{
	public ListGridDashboardPortlet(String title, ListGridField[] fields, Hilite[] hilites, DataSource dataSource) {
        setTitle(title);
        setShowMaximizeButton(false);
        setShowMinimizeButton(false);
        setShowCloseButton(false);

        setHeight100();
        setWidth100();

		ListGrid portletListGrid = new ListGrid();
		
		portletListGrid.setDataSource(dataSource);
		portletListGrid.setAutoFetchData(true);
		
		portletListGrid.setFields(fields);
		portletListGrid.setHilites(hilites);
		
		
		addItem(portletListGrid);
	}
}
