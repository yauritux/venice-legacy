package com.gdn.venice.server.app.task.presenter.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafRpcCommand;
import com.lombardisoftware.webapi.Task;

public class RetrieveTaskSummaryDataCommand implements RafRpcCommand {
	boolean isSynchronizerCompleted = false;
	String userName;
	
	
	public RetrieveTaskSummaryDataCommand(String userName) {
		this.userName = userName;
	}

	@Override
	public String execute() {
		String retVal = "";
		
		int totalInboxItem=0;
		
		Map<String, TaskSummaryProcessData> taskSummaryData = new HashMap<String, TaskSummaryProcessData>();
		
		try {
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));

			bpmAdapter.synchronize();

			List<Long> taskIds = bpmAdapter.getClientRepository().loadTaskIdsForSavedSearch(1);
			for (int i=0;i<taskIds.size();i++) {
				Task task = bpmAdapter.getClientRepository().loadTask(taskIds.get(i));
				if (task.getProcessInstance()!=null) {
					String processName = task.getProcessInstance().getProcess().getName();
					String activityName = task.getActivityName();
					
					//excluded from current user is set when the currently logged in user
					//is excluded within the task inbox.
					//Example for this would be peer-approving process for Logistics Activity Report Approval 
					boolean excludedFromCurrentUser = false;
					
					if (processName.equals(ProcessNameTokens.LOGISTICSACTIVITYREPORTAPPROVAL) &&
							activityName.equals(ProcessNameTokens.LOGISTICSACTIVITYREPORTAPPROVAL_APPROVALACTIVITYNAME)) {
						String submittedBy = bpmAdapter.getExternalDataVariableAsString(new Long(task.getId()), ProcessNameTokens.SUBMITTEDBY);
						excludedFromCurrentUser = userName.equals(submittedBy);
					}
					
					if (!excludedFromCurrentUser) {
						if (!taskSummaryData.containsKey(processName)) {
							TaskSummaryProcessData processData = new TaskSummaryProcessData();
							processData.setProcessName(processName);
							
							Map<String, TaskSummaryTaskData> taskDataMap = new HashMap<String, TaskSummaryTaskData>();
							TaskSummaryTaskData taskData = new TaskSummaryTaskData();
							taskData.setActivityName(activityName);
							taskData.setNumActivity(taskData.getNumActivity()+1);
							totalInboxItem += 1;
							taskDataMap.put(activityName, taskData);
							processData.setTaskData(taskDataMap);
							
							taskSummaryData.put(processName, processData);
						} else {
							TaskSummaryProcessData processData = taskSummaryData.get(processName);
							Map<String, TaskSummaryTaskData> taskDataMap = processData.getTaskData();
							if (!taskDataMap.containsKey(activityName)) {
								TaskSummaryTaskData taskData = new TaskSummaryTaskData();
								taskData.setActivityName(activityName);
								taskData.setNumActivity(taskData.getNumActivity()+1);
								totalInboxItem += 1;
								taskDataMap.put(activityName, taskData);
							} else {
								TaskSummaryTaskData taskData = taskDataMap.get(activityName);
								taskData.setNumActivity(taskData.getNumActivity()+1);
								totalInboxItem += 1;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (taskSummaryData.size()>0) {
			if (totalInboxItem > 1) {
				retVal += "<span style=\"font-size:12px\">You have <b>" + totalInboxItem + "</b> items in Inbox<br/>";
			} else {
				retVal += "<span style=\"font-size:12px\">You have <b>" + totalInboxItem + "</b> item in Inbox<br/>";
			}
			retVal = retVal + "This is the summary from your task:" +
				"<ol>";
			Iterator<String> iterProcess = taskSummaryData.keySet().iterator();
			while (iterProcess.hasNext()) {
				String processName = iterProcess.next();
				TaskSummaryProcessData taskSummaryProcessData = taskSummaryData.get(processName);
				
				retVal = retVal + "<li>" +  processName + "</li>";
				
				Map<String, TaskSummaryTaskData> taskDataMap = taskSummaryProcessData.getTaskData();
				if (taskDataMap!=null && taskDataMap.size()>0) {
					retVal += "<ul>";
					
					Iterator<String> iterTask = taskDataMap.keySet().iterator();
					while (iterTask.hasNext()) {
						String taskName = iterTask.next();
						TaskSummaryTaskData taskSummaryTaskData = taskDataMap.get(taskName);
						
						retVal = retVal + "<li>" +  taskName + " (<b>"+ taskSummaryTaskData.getNumActivity() +"</b>)</li>";
					}
					
					retVal += "</ul>";
				}
			}
			retVal += "</ol></span>";
		} else {
			retVal += "<span style=\"font-size:12px\">You have no item in Inbox<br/></span>";
		}

		return retVal;
	}
	
	private class TaskSummaryProcessData {
		String processName;
		Map<String, TaskSummaryTaskData> taskData;

		public Map<String, TaskSummaryTaskData> getTaskData() {
			return taskData;
		}

		public void setTaskData(Map<String, TaskSummaryTaskData> taskData) {
			this.taskData = taskData;
		}

		public String getProcessName() {
			return processName;
		}

		public void setProcessName(String processName) {
			this.processName = processName;
		}
	}
	
	private class TaskSummaryTaskData {
		String activityName;
		int numActivity = 0;

		public int getNumActivity() {
			return numActivity;
		}

		public void setNumActivity(int numActivity) {
			this.numActivity = numActivity;
		}

		public String getActivityName() {
			return activityName;
		}

		public void setActivityName(String activityName) {
			this.activityName = activityName;
		}
		
		
	}
	
}


