package com.gdn.venice.client.ui.data;

import com.gdn.venice.client.app.DataWidgetNameTokens;
import com.gdn.venice.client.app.NameTokens;
/**
 * This class contains the menu data definition for the task management stack section
 *
 */
public class TaskManagementNavigationPaneSectionData {

	private static NavigationPaneTreeNodeRecord[] records;

	public static NavigationPaneTreeNodeRecord[] getRecords() {
		if (records == null) {
			records = getNewRecords();
		}
		return records;
	}

	public static NavigationPaneTreeNodeRecord[] getNewRecords() {
	return new NavigationPaneTreeNodeRecord[]{
	  new NavigationPaneTreeNodeRecord("2", "1", "Task Management", null, "TM1", DataWidgetNameTokens.TASK_TASKMANAGEMENTTREENODE),
	  new NavigationPaneTreeNodeRecord("3", "2", "Task Summary", NameTokens.taskSummaryPage, "TM2", DataWidgetNameTokens.TASK_TASKSUMMARYTREENODE),
	  new NavigationPaneTreeNodeRecord("4", "2", "To Do List", NameTokens.toDoListPage, "TM3", DataWidgetNameTokens.TASK_TODOLISTTREENODE)
//	  new NavigationPaneTreeNodeRecord("5", "2", "Assigned Task", NameTokens.getAssignedTaskPage(), "TM4"),
//	  new NavigationPaneTreeNodeRecord("6", "2", "Task Launchpad", NameTokens.getTaskLaunchpadPage(), "TM5"),
//	  new NavigationPaneTreeNodeRecord("7", "2", "Test Rest Data Source", NameTokens.getTestRestDataSource(), "TM6")
	};
  }
}
