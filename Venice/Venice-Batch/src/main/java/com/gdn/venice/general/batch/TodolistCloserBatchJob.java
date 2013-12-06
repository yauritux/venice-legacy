package com.gdn.venice.general.batch;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import com.gdn.venice.bpmenablement.BPMAdapter;
import com.lombardisoftware.webapi.Task;
import com.lombardisoftware.webapi.Variable;

/**
 * TodolistCloserBatchJob.java
 * 
 * This batch job identifies the todo list that's need to be closed 
 * 
 * <p>
 * <b>author: roland
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2013
 */
public class TodolistCloserBatchJob {
	String username = "";
	String password = "";
	String processOrTask = "";
	String processOrTaskName = "";
	
	public TodolistCloserBatchJob() {
		super();
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(System.getenv("VENICE_HOME") +  "/admin/config.properties"));
			username = properties.getProperty("username");
			password = properties.getProperty("password");
			processOrTask = properties.getProperty("processOrTask");
			processOrTaskName = properties.getProperty("processOrTaskName");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Boolean findAndCloseTodolistTask() {		
		System.out.println("Start TodolistCloserBatchJob.");
		System.out.println("getting task for username: "+username);
		BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(username, password);
		bpmAdapter.synchronize();

		List<Long> taskIds = null;		
		try {
			taskIds = bpmAdapter.getClientRepository().loadTaskIdsForSavedSearch(1);
			System.out.println("task total size: "+taskIds.size());
						
			System.out.println("finding "+processOrTask+" type: "+processOrTaskName);
			int taskSize = taskIds.size();
			for (int i=0;i<taskSize;i++) {
				Task task = bpmAdapter.getClientRepository().loadTask(taskIds.get(i));
				
				//untuk close process
				if(processOrTask.equals("process")){
					if(!task.getProcessInstance().getProcess().getName().equals(processOrTaskName)){
						taskIds.remove(taskIds.get(i));
						--i;
						--taskSize;
						continue;
					}
				}else if(processOrTask.equals("task")){				
					//untuk close task
					if(!task.getActivityName().equals(processOrTaskName)){
						taskIds.remove(taskIds.get(i));
						--i;
						--taskSize;
						continue;
					}
				}
			}
			
			System.out.println("size of "+processOrTask+" "+processOrTaskName+" to close is: "+taskSize);
			for (int i=0;i<taskIds.size();i++) {
				System.out.println("closing "+processOrTask+" ke "+i);
				Task task = bpmAdapter.getClientRepository().loadTask(taskIds.get(i));				
				bpmAdapter.getWebAPI().completeTask(new Long(task.getId()), new Variable[] {});
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		System.out.println("TodolistCloserBatchJob completed.");
		return Boolean.TRUE;		
	}
	
	public static void main(String[] args) {
		TodolistCloserBatchJob todolistCloserBatchJob = new TodolistCloserBatchJob();
		todolistCloserBatchJob.findAndCloseTodolistTask();
	}
}
