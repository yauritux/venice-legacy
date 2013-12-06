package com.gdn.venice.client.app.task.data;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.data.RafDataSource;
import com.google.gwt.core.client.GWT;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.fields.DataSourceDateTimeField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class TaskManagementData {
	
	public static RafDataSource getToDoListData() {
		DataSourceField[] dataSourceFields = {
				new DataSourceTextField(DataNameTokens.TASKID, "Task ID"),
				new DataSourceTextField(DataNameTokens.TASKTYPE, "Type"), 
				new DataSourceTextField(DataNameTokens.TASKDESCRIPTION, "Description"),
				new DataSourceTextField(DataNameTokens.TASKASSIGNEE, "Assignee"),
				new DataSourceDateTimeField(DataNameTokens.TASKCREATEDDATE, "Date Created"), 
				new DataSourceDateTimeField(DataNameTokens.TASKDUEDATE, "Due Date"),
				new DataSourceTextField(DataNameTokens.TASKSTATUS, "Status"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, "Fraud Case Id"),
				new DataSourceTextField(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID, "Fraud Status Id")
		};
		dataSourceFields[0].setPrimaryKey(true);
		
		RafDataSource dataSource = new RafDataSource(
				"/response/data/*",
				GWT.getHostPageBaseURL() + "ToDoListPresenterServlet?method=fetchToDoListData&type=DataSource",
				null,
				null,
				null,
				dataSourceFields); 
		
		return dataSource;
				
	}
}