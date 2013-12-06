package com.gdn.venice.client.app.task.view;

import com.gdn.venice.client.app.task.data.AssignedTaskData;
import com.gdn.venice.client.app.task.presenter.AssignedTaskPresenter;
import com.gdn.venice.client.app.task.view.handlers.AssignedTaskUiHandlers;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

/**
 * View for Assigned Task
 * 
 * @author Henry Chandra
 */
public class AssignedTaskView extends
ViewWithUiHandlers<AssignedTaskUiHandlers> implements
AssignedTaskPresenter.MyView {
	private static final int TITLE_HEIGHT = 20;  	

	RafViewLayout assignedTaskLayout;

	@Inject
	public AssignedTaskView() {
		
		assignedTaskLayout = new RafViewLayout();
		
		assignedTaskLayout.setMembers(buildAssignedTaskListGrid());

		bindCustomUiHandlers();
	}
	
	private ListGrid buildAssignedTaskListGrid() {
		ListGrid assignedTaskListGrid = new ListGrid();
		assignedTaskListGrid.setWidth100();
		assignedTaskListGrid.setHeight100();
		assignedTaskListGrid.setShowAllRecords(true);
		assignedTaskListGrid.setSortField(0);

		ListGridField idField = new ListGridField("taskid", "Id", 100);
		ListGridField typeField = new ListGridField("type", "Type", 200);
		typeField.setValueMap("Human Resources - Recruitment", "Human Resources - Travel Management");
		ListGridField descField = new ListGridField("taskdesc", "Description", 400);
		ListGridField dateCreatedField = new ListGridField("taskdate", "Date Created", 100);
		ListGridField dueDateField = new ListGridField("taskduedate", "Due Date", 100);        
		ListGridField statusField = new ListGridField("status", "Status");

		assignedTaskListGrid.setFields(idField, typeField, descField, dateCreatedField, dueDateField, statusField);
		assignedTaskListGrid.setCanResizeFields(true);
		assignedTaskListGrid.setShowRowNumbers(true);
		assignedTaskListGrid.setDataSource(AssignedTaskData.getAssignedTaskData());
		assignedTaskListGrid.setAutoFetchData(true);
		assignedTaskListGrid.setGroupByField("type");
		assignedTaskListGrid.setGroupStartOpen(GroupStartOpen.ALL);
		
		assignedTaskListGrid.setShowFilterEditor(true);
		assignedTaskListGrid.setFilterOnKeypress(true);
		
		return assignedTaskListGrid;
	}
	
	@Override
	public Widget asWidget() {
		return assignedTaskLayout;
	}

	protected void bindCustomUiHandlers() {

	}

	
	

}
