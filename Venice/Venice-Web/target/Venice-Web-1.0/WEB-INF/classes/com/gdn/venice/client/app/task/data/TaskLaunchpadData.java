package com.gdn.venice.client.app.task.data;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class TaskLaunchpadData {
	private static DataSource taskLaunchpadDs = null;
	
	public static DataSource getTaskLaunchpadData() {
		if (taskLaunchpadDs != null) {
			return taskLaunchpadDs;
		} else {
			taskLaunchpadDs = new DataSource();
			
			taskLaunchpadDs.setRecordXPath("/tasklaunchpad/tasks/task");
			DataSourceTextField idField = new DataSourceTextField("id", "Id");
			idField.setPrimaryKey(true);
	
	        DataSourceTextField typeField = new DataSourceTextField("type", "Type");
	        
	        DataSourceTextField nameField = new DataSourceTextField("name", "Name");
	        
	
	        taskLaunchpadDs.setFields(idField, typeField, nameField);
	        taskLaunchpadDs.setDataURL("ds/test_data/tasklaunchpad.data.xml");
	        
	        taskLaunchpadDs.setClientOnly(true);
	        
	        return taskLaunchpadDs;
		}    
        
		
	}
}
