package com.gdn.venice.client.data;

import java.util.HashMap;
import java.util.Map;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.OperationBinding;
import com.smartgwt.client.data.RestDataSource;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.DSProtocol;

public class RafDataSource extends RestDataSource{
	
	public RafDataSource(String xmlRecordXPath, String fetchURL, String addURL, String updateURL, String removeURL, DataSourceField[] dataSourceFields) {
		setXmlRecordXPath(xmlRecordXPath);

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
        setOperationBindings(fetch, add, update, remove);
        
		
        setFields(dataSourceFields);
        if (fetchURL!=null && !fetchURL.isEmpty()) {
        	setFetchDataURL(fetchURL);
        }
        if (addURL!=null && !addURL.isEmpty()) {
        	setAddDataURL(addURL);
        }
        if (updateURL!=null && !updateURL.isEmpty()) {
        	setUpdateDataURL(updateURL);
        }
        if (removeURL!=null && !removeURL.isEmpty()) {
        	setRemoveDataURL(removeURL);
        }
	}
	
	public void fetchDataWithParam(Map<String, String> params) {
        getOperationBinding(DSOperationType.FETCH).setDefaultParams(params);
        fetchData();
	}
	
	public OperationBinding getOperationBinding(DSOperationType operationType) {
		OperationBinding[] operationBindings = getOperationBindings();
		for (int i=0;i<operationBindings.length;i++) {
			if (operationBindings[i].getOperationType().getValue().equals(operationType.getValue()))
				return operationBindings[i];
		}
		return null;
	}
	
}
