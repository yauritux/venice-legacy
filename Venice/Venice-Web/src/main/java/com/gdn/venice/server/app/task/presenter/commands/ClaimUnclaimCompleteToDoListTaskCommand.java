package com.gdn.venice.server.app.task.presenter.commands;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.regex.Pattern;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudCaseHistory;
import com.gdn.venice.persistence.FrdFraudCaseHistoryPK;
import com.gdn.venice.persistence.FrdFraudCaseStatus;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafRpcCommand;
import com.lombardisoftware.webapi.Variable;

/**
 * Command for claiming, releasing and completing BPM tasks
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class ClaimUnclaimCompleteToDoListTaskCommand implements RafRpcCommand {
	/*
	 * A HashMap of the task ids to use during the command
	 */
	private HashMap<String, String> taskId;

	/*
	 * A long signifying the type of task that enables differentiation between
	 * the various task types handled by the command different actions such as
	 * passing task-type-specific parameters to the BPM API
	 */
	private Long taskType = null;

	/*
	 * The action to be taken by the command (claim, unclaim, complete)
	 */
	private String action = null;

	/*
	 * The user name to be used when calling teh BPM API
	 */
	private String userName = null;

	/**
	 * Copy Constructor that sets up the internal state for the command
	 * 
	 * @param parameters
	 *            is the parameters passed to the command
	 * @param action
	 *            is the action to be taken (claim, unclaim, complete)
	 * @param userName
	 *            is the user name to be used when calling the BPM API
	 */
	public ClaimUnclaimCompleteToDoListTaskCommand(String parameters, String action, String userName) {
		Pattern p = Pattern.compile("[\\{\\}\\=\\, ]++");
		String[] split = p.split(parameters);

		taskId = new HashMap<String, String>();
		for (int i = 1; i < split.length; i += 2) {
			if (split[i].startsWith(DataNameTokens.TASKID)) {
				taskId.put(split[i], split[i + 1]);
			} else if (split[i].equals(DataNameTokens.TASKTYPEID)) {
				taskType = new Long(split[i + 1]);
			}

		}

		this.action = action;
		this.userName = userName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(userName, BPMAdapter.getUserPasswordFromLDAP(userName));

		bpmAdapter.synchronize();

		/*
		 * Iterate the list of task ids and perform the requested action (claim,
		 * unclaim, complete)
		 */
		for (int i = 0; i < taskId.size(); i++) {
			try {
				if (action.equals("claim")) {
					/*
					 * Take the claim action based on the task type
					 */
					if (taskType.equals(ProcessNameTokens.TASK_TYPE_UNKNOWN)) {
						bpmAdapter.getWebAPI().assignTask( new Long(taskId.get(DataNameTokens.TASKID + (i + 1))));
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_FINANCE)) {
						bpmAdapter.getWebAPI().assignTask(new Long(taskId.get(DataNameTokens.TASKID + (i + 1))));
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_LOGISTICS)) {
						bpmAdapter.getWebAPI().assignTask(new Long(taskId.get(DataNameTokens.TASKID + (i + 1))));
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_KPI)) {
						bpmAdapter.getWebAPI().assignTask(new Long(taskId.get(DataNameTokens.TASKID + (i + 1))));
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_FRAUD)) {
						String[] params = taskId.get(DataNameTokens.TASKID + (i + 1)).split("~");
						bpmAdapter.getWebAPI().assignTask(new Long(params[0]));
						// Jika fraud management process, maka buat history
						if (params.length > 2 && (params[1] != "" && params[2] != "")) {
							FrdFraudCaseHistory entityFraudHistory = new FrdFraudCaseHistory();

							// Add fraud case history dan jalankan BPM jika
							// diperlukan
							Locator<FrdFraudCaseHistory> fraudCaseHistoryLocator = null;

							try {
								fraudCaseHistoryLocator = new Locator<FrdFraudCaseHistory>();
								FrdFraudCaseHistorySessionEJBRemote sessionHome = (FrdFraudCaseHistorySessionEJBRemote) fraudCaseHistoryLocator
									.lookup(FrdFraudCaseHistorySessionEJBRemote.class, "FrdFraudCaseHistorySessionEJBBean");

								// Buat history modified
								FrdFraudCaseHistoryPK frdFraudCaseHistoryPK = new FrdFraudCaseHistoryPK();
								frdFraudCaseHistoryPK.setFraudSuspicionCaseId(new Long(params[1]));
								frdFraudCaseHistoryPK.setFraudCaseHistoryDate(new Timestamp(System.currentTimeMillis()));
								entityFraudHistory.setId(frdFraudCaseHistoryPK);
								entityFraudHistory.setFraudCaseHistoryNotes("Claimed by " + userName);
								FrdFraudCaseStatus frdFraudCaseStatus = new FrdFraudCaseStatus();
								frdFraudCaseStatus.setFraudCaseStatusId(new Long(params[2]));
								entityFraudHistory.setFrdFraudCaseStatus(frdFraudCaseStatus);
								sessionHome.persistFrdFraudCaseHistory(entityFraudHistory);

							} catch (Exception ex) {
								ex.printStackTrace();
							} finally {
								try {
									fraudCaseHistoryLocator.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				} else if (action.equals("unclaim")) {
					/*
					 * Take the unclaim action based on the task type
					 */
					if (taskType.equals(ProcessNameTokens.TASK_TYPE_UNKNOWN)) {
						bpmAdapter.getWebAPI().reassignTask(new Long(taskId.get(DataNameTokens.TASKID + (i + 1))));
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_FINANCE)) {
						bpmAdapter.getWebAPI().reassignTask(new Long(taskId.get(DataNameTokens.TASKID + (i + 1))));
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_LOGISTICS)) {
						bpmAdapter.getWebAPI().reassignTask(new Long(taskId.get(DataNameTokens.TASKID + (i + 1))));
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_KPI)) {
						bpmAdapter.getWebAPI().reassignTask(new Long(taskId.get(DataNameTokens.TASKID+ (i + 1))));
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_FRAUD)) {
						String[] params = taskId.get(DataNameTokens.TASKID + (i + 1)).split("~");
						bpmAdapter.getWebAPI().reassignTask(new Long(params[0]));

						// Jika fraud management process, maka buat history
						if (params.length > 2 && (params[1] != "" && params[2] != "")) {
							FrdFraudCaseHistory entityFraudHistory = new FrdFraudCaseHistory();

							// Add fraud case history dan jalankan BPM jika
							// diperlukan
							Locator<FrdFraudCaseHistory> fraudCaseHistoryLocator = null;

							try {
								fraudCaseHistoryLocator = new Locator<FrdFraudCaseHistory>();
								FrdFraudCaseHistorySessionEJBRemote sessionHome = (FrdFraudCaseHistorySessionEJBRemote) fraudCaseHistoryLocator
									.lookup(FrdFraudCaseHistorySessionEJBRemote.class, "FrdFraudCaseHistorySessionEJBBean");

								// Buat history modified
								FrdFraudCaseHistoryPK frdFraudCaseHistoryPK = new FrdFraudCaseHistoryPK();
								frdFraudCaseHistoryPK.setFraudSuspicionCaseId(new Long(params[1]));
								frdFraudCaseHistoryPK.setFraudCaseHistoryDate(new Timestamp(System.currentTimeMillis()));
								entityFraudHistory.setId(frdFraudCaseHistoryPK);
								entityFraudHistory.setFraudCaseHistoryNotes("Unclaimed by " + userName);
								FrdFraudCaseStatus frdFraudCaseStatus = new FrdFraudCaseStatus();
								frdFraudCaseStatus.setFraudCaseStatusId(new Long(params[2]));
								entityFraudHistory.setFrdFraudCaseStatus(frdFraudCaseStatus);
								sessionHome.persistFrdFraudCaseHistory(entityFraudHistory);

							} catch (Exception ex) {
								ex.printStackTrace();
							} finally {
								try {
									fraudCaseHistoryLocator.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				} else if (action.equals("complete")) {
					/*
					 * Take the complete action based on the task type
					 */
					if (taskType.equals(ProcessNameTokens.TASK_TYPE_UNKNOWN)) {
						bpmAdapter.getWebAPI().completeTask(new Long(taskId.get(DataNameTokens.TASKID + (i + 1))), new Variable[] {});
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_FINANCE)) {
						bpmAdapter.getWebAPI().completeTask(new Long(taskId.get(DataNameTokens.TASKID + (i + 1))), new Variable[] {});
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_LOGISTICS)) {
						bpmAdapter.getWebAPI().completeTask(new Long(taskId.get(DataNameTokens.TASKID + (i + 1))), new Variable[] {});
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_KPI)) {
						bpmAdapter.getWebAPI().completeTask(new Long(taskId.get(DataNameTokens.TASKID + (i + 1))), new Variable[] {});
					} else if (taskType.equals(ProcessNameTokens.TASK_TYPE_FRAUD)) {
						String[] params = taskId.get(DataNameTokens.TASKID + (i + 1)).split("~");

						// Jalankan action di bpm (lombardi)
						try {
							Variable[] variables = new Variable[] { new Variable("escalate", "FALSE") };

							bpmAdapter.getWebAPI().completeTask(new Long(params[0]), variables);
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}

						// Jika fraud management process, maka buat history
						if (params.length > 2 && (params[1] != "" && params[2] != "")) {
							FrdFraudCaseHistory entityFraudHistory = new FrdFraudCaseHistory();

							// Add fraud case history dan jalankan BPM jika
							// diperlukan
							Locator<FrdFraudCaseHistory> fraudCaseHistoryLocator = null;

							try {
								fraudCaseHistoryLocator = new Locator<FrdFraudCaseHistory>();
								FrdFraudCaseHistorySessionEJBRemote sessionHome = (FrdFraudCaseHistorySessionEJBRemote) fraudCaseHistoryLocator
									.lookup(FrdFraudCaseHistorySessionEJBRemote.class, "FrdFraudCaseHistorySessionEJBBean");

								// Buat history modified
								FrdFraudCaseHistoryPK frdFraudCaseHistoryPK = new FrdFraudCaseHistoryPK();
								frdFraudCaseHistoryPK.setFraudSuspicionCaseId(new Long(params[1]));
								frdFraudCaseHistoryPK.setFraudCaseHistoryDate(new Timestamp(System.currentTimeMillis()));
								entityFraudHistory.setId(frdFraudCaseHistoryPK);
								entityFraudHistory.setFraudCaseHistoryNotes("Closed by " + userName);
								FrdFraudCaseStatus frdFraudCaseStatus = new FrdFraudCaseStatus();
								frdFraudCaseStatus.setFraudCaseStatusId(new Long(params[2]));
								entityFraudHistory.setFrdFraudCaseStatus(frdFraudCaseStatus);
								sessionHome.persistFrdFraudCaseHistory(entityFraudHistory);

							} catch (Exception ex) {
								ex.printStackTrace();
							} finally {
								try {
									fraudCaseHistoryLocator.close();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				return "-1";
			}
		}
		return "0";
	}
}
