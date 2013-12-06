package com.gdn.venice.client.ui.data;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;

/**
 * This is the data source for the combo box in the search form of the menu
 * It will need to be replaced with a bound data source later when the menus
 * are to be read from the database/server.
 *
 */
public class RAFDataSource extends DataSource {
	 private static RAFDataSource instance = null;

	    public static RAFDataSource getInstance() {
	        if (instance == null) {
	            instance = new RAFDataSource("rafNavigation");
	        }
	        return instance;
	    }

	    public RAFDataSource(String id) {

	        setID(id);
	        setRecordXPath("/navigation/item");
	        DataSourceIntegerField pkField = new DataSourceIntegerField("id");
	        pkField.setHidden(true);
	        pkField.setPrimaryKey(true);

	        DataSourceTextField shortCutField = new DataSourceTextField("shortcut", "Shortcut");
	        DataSourceTextField descriptionField = new DataSourceTextField("description", "Description");
	        
	        setFields(pkField, shortCutField, descriptionField);

	        setDataURL("ds/test_data/rafnavigation.data.xml");
	        setClientOnly(true);        
	    }
}
