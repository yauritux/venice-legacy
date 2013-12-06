package com.gdn.venice.client.app.task.view;

import com.gdn.venice.client.app.task.presenter.TaskSummaryPresenter;
import com.gdn.venice.client.app.task.view.handlers.TaskSummaryUiHandlers;
import com.gdn.venice.client.widgets.RafViewLayout;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.HTMLFlow;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

/**
 * View for Task Summary
 * 
 * @author Henry Chandra
 *
 */
public class TaskSummaryView extends ViewWithUiHandlers<TaskSummaryUiHandlers> implements TaskSummaryPresenter.MyView {
	private static final int NORTH_HEIGHT = 20;  	

	RafViewLayout taskSummaryLayout;
	VLayout taskDetailLayout;
	
	HTMLFlow toDoListDetail;

	@Inject
	public TaskSummaryView() {

		taskSummaryLayout = new RafViewLayout();

		taskSummaryLayout.setMembers(buildTaskSummaryView());

		bindCustomUiHandlers();
	}

	private SectionStack buildTaskSummaryView() {
		SectionStack taskSummaryStack = new SectionStack();
		taskSummaryStack.setWidth100();
		taskSummaryStack.setHeight100();
		taskSummaryStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		taskSummaryStack.setAnimateSections(true);	
		
		SectionStackSection toDoListSection = new SectionStackSection("<b>To Do List</b>"); 
		toDoListSection.setExpanded(true);
		
		toDoListDetail = new HTMLFlow();  
		toDoListDetail.setOverflow(Overflow.AUTO);  
		toDoListDetail.setPadding(10);  
		toDoListDetail.setHeight100();
//		toDoListDetail.setContents("<span style=\"font-size:12px\">You have <b>5</b> items in Inbox<br/>" +
//				"This is the summary from your task:" +
//				"<ol>" +
//				"<li>Fraud Module</li>" +
//				"<ul>" +
//				"<li>Suspicious Fraud <b>(5)</b></li>" +
//				"</ul>" +
//				"</ol>" +
//				"</span>");  
		
		toDoListSection.addItem(toDoListDetail);
		
//		SectionStackSection assignedTaskSection = new SectionStackSection("<b>Assigned Task</b>"); 
//		assignedTaskSection.setExpanded(true); 
//		
//		HTMLFlow assignedTaskDeatil = new HTMLFlow();  
//		assignedTaskDeatil.setOverflow(Overflow.AUTO);  
//		assignedTaskDeatil.setPadding(10);  
//		assignedTaskDeatil.setHeight100();
//		assignedTaskDeatil.setContents("<span style=\"font-size:12px\">You already submitted <b>10</b> tasks in this month<br/>" +
//					"<ul>" +
//					"<li>Task Completed <b>(5)</b></li>" +
//					"<li>Task In Progress <b>(5)</b></li>" +
//					"</ul>" +
//					"</span>");  
//		assignedTaskSection.addItem(assignedTaskDeatil);
		
		
		taskSummaryStack.addSection(toDoListSection);
//		taskSummaryStack.addSection(assignedTaskSection);

		return taskSummaryStack;
	}

	@Override
	public Widget asWidget() {
		return taskSummaryLayout;
	}
	
	protected void bindCustomUiHandlers() {

	}

	@Override
	public void updateTaskSummaryData(String taskSummaryData) {
		toDoListDetail.setContents(taskSummaryData);  
	}
	
	

}

