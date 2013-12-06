package com.gdn.venice.client.app.task.view;

import com.gdn.venice.client.app.task.data.TaskLaunchpadData;
import com.gdn.venice.client.app.task.presenter.TaskLaunchpadPresenter;
import com.gdn.venice.client.app.task.view.handlers.TaskLaunchpadUiHandlers;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.GroupStartOpen;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * @author Henry Chandra
 */
public class TaskLaunchpadView extends
ViewWithUiHandlers<TaskLaunchpadUiHandlers> implements
TaskLaunchpadPresenter.MyView {
	private static final int TITLE_HEIGHT = 20;  	

	RafViewLayout taskLaunchpadLayout;

	@Inject
	public TaskLaunchpadView() {
		
		taskLaunchpadLayout = new RafViewLayout();

	
		taskLaunchpadLayout.setMembers(buildTaskLaunchpadView());

		bindCustomUiHandlers();
	}
	
	private ListGrid buildTaskLaunchpadView() {
		ListGrid taskLaunchpadListGrid = new ListGrid() {
			 @Override
	            protected Canvas createRecordComponent(final ListGridRecord record, Integer colNum) {

	                String fieldName = this.getFieldName(colNum);

	                if (fieldName.equals("startField")) {
	                    IButton button = new IButton();
	                    button.setHeight(18);
	                    button.setWidth(65);                    
	                    button.setIcon("[SKIN]/icons/play.png");
	                    button.setTitle("Start");
	                    return button;
	                } else {
	                    return null;
	                }

	            }
		};
		taskLaunchpadListGrid.setWidth100();
		taskLaunchpadListGrid.setHeight100();
		taskLaunchpadListGrid.setShowAllRecords(true);
		taskLaunchpadListGrid.setShowRecordComponents(true);        
		taskLaunchpadListGrid.setShowRecordComponentsByCell(true);
		taskLaunchpadListGrid.setSortField(0);

		ListGridField idField = new ListGridField("id", "Id", 100);
		ListGridField typeField = new ListGridField("type", "Type");
		ListGridField nameField = new ListGridField("name", "Name");
		ListGridField startField = new ListGridField("startField", "Start", 200);
		
		taskLaunchpadListGrid.setFields(idField, typeField, nameField, startField);
		taskLaunchpadListGrid.setCanResizeFields(true);
		taskLaunchpadListGrid.setShowRowNumbers(true);
		taskLaunchpadListGrid.setDataSource(TaskLaunchpadData.getTaskLaunchpadData());
		taskLaunchpadListGrid.setAutoFetchData(true);
		taskLaunchpadListGrid.setGroupByField("type");
		taskLaunchpadListGrid.setGroupStartOpen(GroupStartOpen.ALL);
		
		taskLaunchpadListGrid.setShowFilterEditor(true);
		taskLaunchpadListGrid.setFilterOnKeypress(true);
		
		return taskLaunchpadListGrid;
	}
	
	
	
	@Override
	public Widget asWidget() {
		return taskLaunchpadLayout;
	}

	protected void bindCustomUiHandlers() {

	}
	
	

}
