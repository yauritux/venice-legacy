package com.gdn.venice.client.app.task.data;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class AssignedTaskData {
	private static DataSource assignedTaskDs = null;
	
	public static DataSource getAssignedTaskData() {
		if (assignedTaskDs != null) {
			return assignedTaskDs;
		} else {
			assignedTaskDs = new DataSource();
			
			assignedTaskDs.setRecordXPath("/assignedtask/tasks/task");
			DataSourceTextField idField = new DataSourceTextField("taskid");
	        idField.setPrimaryKey(true);
	
	        DataSourceTextField typeField = new DataSourceTextField("type", "Type");
	        
	        DataSourceTextField taskDescField = new DataSourceTextField("taskdesc", "Description");
	        
	        DataSourceTextField assigneeField = new DataSourceTextField("assignee", "Assignee");
	        
	        DataSourceDateTimeField taskDateField = new DataSourceDateTimeField("taskdate", "Date Created");
	        
	        DataSourceDateTimeField taskDueDateField = new DataSourceDateTimeField("taskduedate", "Due Date");
	        
	        DataSourceTextField statusField = new DataSourceTextField("status", "Status");
	
	        assignedTaskDs.setFields(idField, typeField, taskDescField, assigneeField, taskDateField, taskDueDateField, statusField);
	        assignedTaskDs.setDataURL("ds/test_data/assignedtask.data.xml");
	        
	        assignedTaskDs.setClientOnly(true);
	        
	        return assignedTaskDs;
		}    
        
		
	}
}
