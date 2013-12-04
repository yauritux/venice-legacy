package com.gdn.venice.client.app.testrestdatasource.data;

import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.OperationBinding;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DSProtocol;

public class TestRestDataSourceData {
	private static RestDataSource testRestDataSourceDs = null;
	
	public static DataSource getTestRestDataSourceData() {
		if (testRestDataSourceDs != null) {
			return testRestDataSourceDs;
		} else {
			testRestDataSourceDs = new RestDataSource() {
	            @Override
	            protected Object transformRequest(DSRequest dsRequest) {
	                return super.transformRequest(dsRequest);
	            }
	            @Override
	            protected void transformResponse(DSResponse response, DSRequest request, Object data) {
	                super.transformResponse(response, request, data);
	            }
	        };
			
	        testRestDataSourceDs.setXmlRecordXPath("/response/data/recordlist/*");
			
	        OperationBinding fetch = new OperationBinding();
	        fetch.setOperationType(DSOperationType.FETCH);
	        fetch.setDataProtocol(DSProtocol.POSTMESSAGE);
	        OperationBinding add = new OperationBinding();
	        add.setOperationType(DSOperationType.ADD);
	        add.setDataProtocol(DSProtocol.POSTMESSAGE);
	        OperationBinding update = new OperationBinding();
	        update.setOperationType(DSOperationType.UPDATE);
	        update.setDataProtocol(DSProtocol.POSTMESSAGE);
	        OperationBinding remove = new OperationBinding();
	        remove.setOperationType(DSOperationType.REMOVE);
	        remove.setDataProtocol(DSProtocol.POSTMESSAGE);
	        testRestDataSourceDs.setOperationBindings(fetch, add, update, remove);
	        
	        DataSourceTextField data1Field = new DataSourceTextField("data1", "Data 1");
	        data1Field.setPrimaryKey(true);
	        	        
	        DataSourceTextField data2Field = new DataSourceTextField("data2", "Data 2");
	        data2Field.setValueXPath("data2/name");
	        
	        DataSourceTextField data3Field = new DataSourceTextField("data3", "Data 3");
	        
	        DataSourceTextField data4Field = new DataSourceTextField("data4", "Data 4");
	
	        testRestDataSourceDs.setFields(data1Field, data2Field, data3Field,  data4Field);
	        testRestDataSourceDs.setFetchDataURL(GWT.getHostPageBaseURL() + "TestRestDataSourceServlet?method=fetch");
	        testRestDataSourceDs.setAddDataURL(GWT.getHostPageBaseURL() + "TestRestDataSourceServlet?method=add");
//	        testRestDataSourceDs.setUpdateDataURL("data/dataIntegration/xml/responses/country_update_rest.xml");
//	        testRestDataSourceDs.setRemoveDataURL("data/dataIntegration/xml/responses/country_remove_rest.xml");

	        
	        
	        return testRestDataSourceDs;
		}    
        
		
	}
}
