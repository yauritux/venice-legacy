package com.gdn.venice.client.widgets;

import com.gdn.venice.client.ui.data.RAFDataSource;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;


public class NavigationPaneForm extends DynamicForm {
	
	public NavigationPaneForm() {
		ComboBoxItem navigationComboBox = new ComboBoxItem("description", "description");
		
		navigationComboBox.setOptionDataSource(RAFDataSource.getInstance());
		navigationComboBox.setShowTitle(false);
		navigationComboBox.setWidth("100%");

        ListGrid pickListProperties = new ListGrid();
        pickListProperties.setCanHover(true);
        pickListProperties.setShowHover(true);
        pickListProperties.setCellFormatter(new CellFormatter() {
            @Override
            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {
                String navigationName = record.getAttribute("description");
                String menuShortcut = record.getAttribute("shortcut");

                String styleStr = "font-family:arial;font-size:11px;white-space:nowrap;overflow:hidden;";
                String retStr = "<table>" +
                        "<tr><td ><span style='" + styleStr + "float:left'>" + menuShortcut + ": <span></td>" +
                        "<td align='right'><span style='" + styleStr + "float:right;font-weight:bold'>" + navigationName + "<span></td></tr></table>";
                return retStr;

            }
        });
        

        navigationComboBox.setPickListProperties(pickListProperties);
        
        setWidth100();
        setNumCols(1);
        setItems(navigationComboBox);
	}
	
}
