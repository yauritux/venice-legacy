package com.gdn.venice.server.app.fraud.presenter.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.task.ProcessNameTokens;
import com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudCaseHistory;
import com.gdn.venice.persistence.FrdFraudCaseHistoryPK;
import com.gdn.venice.persistence.FrdFraudCaseStatus;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.lombardisoftware.webapi.Variable;

public class UpdateFraudCaseFraudManagementDataCommand implements RafDsCommand {
	String action;
	String username;
	String password;
	RafDsRequest request;
	
	public UpdateFraudCaseFraudManagementDataCommand(String action,
													 String username,
													 String password,
													 RafDsRequest request) {

		this.action = action;
		this.username = username;
		this.password = password;
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		FrdFraudSuspicionCase entityFraudManagement = new FrdFraudSuspicionCase();
		List<FrdFraudSuspicionCase> fraudManagementList = new ArrayList<FrdFraudSuspicionCase>();
		FrdFraudCaseHistory entityFraudHistory = new FrdFraudCaseHistory();
		
		List<HashMap<String, String>> dataList = request.getData();
		
		//TODO Tambahkan transaksi
		//Update tabel fraud suspicion case
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			//Update fraud case
			FrdFraudSuspicionCaseSessionEJBRemote fraudSuspicionSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
			
			//Siapkan java bean baik untuk history maupun fraud suspicion case
			Long fraudCaseId = new Long("-1");
			Long taskId = new Long("-1");
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID)) {
						fraudCaseId = new Long(data.get(key));
						try{
							entityFraudManagement = fraudSuspicionSessionHome.queryByRange("select o from FrdFraudSuspicionCase o where o.fraudSuspicionCaseId="+fraudCaseId, 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							entityFraudManagement.setFraudSuspicionCaseId(fraudCaseId);
						}
						break;
					}
				}
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					
					String key = iter.next();					
					if (key.equals(DataNameTokens.TASKID)) {
						taskId = new Long(data.get(key));
					} else if (key.equals(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC)) {
						entityFraudManagement.setFraudCaseDesc(data.get(key));
					} else if (key.equals(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON)) {
						entityFraudManagement.setSuspicionReason(data.get(key));	
					} else if (key.equals(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES)) {
						entityFraudManagement.setFraudSuspicionNotes(data.get(key));
					} else if (key.equals(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSID)) {
						FrdFraudCaseStatus frdFraudCaseStatus = new FrdFraudCaseStatus();
						frdFraudCaseStatus.setFraudCaseStatusId(new Long(data.get(key)));
						entityFraudManagement.setFrdFraudCaseStatus(frdFraudCaseStatus);
						entityFraudHistory.setFrdFraudCaseStatus(frdFraudCaseStatus);
					}
				}
				
				entityFraudHistory.setFrdFraudSuspicionCase(entityFraudManagement);
				fraudManagementList.add(entityFraudManagement);			
			}
			
			fraudSuspicionSessionHome.mergeFrdFraudSuspicionCaseList((ArrayList<FrdFraudSuspicionCase>)fraudManagementList);
			
			//Buat history modified
			FrdFraudCaseHistorySessionEJBRemote fraudHistorySessionHome = (FrdFraudCaseHistorySessionEJBRemote) locator.lookup(FrdFraudCaseHistorySessionEJBRemote.class, "FrdFraudCaseHistorySessionEJBBean");
			FrdFraudCaseHistoryPK frdFraudCaseHistoryPK = new FrdFraudCaseHistoryPK();
			frdFraudCaseHistoryPK.setFraudSuspicionCaseId(fraudCaseId);
			frdFraudCaseHistoryPK.setFraudCaseHistoryDate(new Timestamp(System.currentTimeMillis()));
			entityFraudHistory.setId(frdFraudCaseHistoryPK);
			entityFraudHistory.setFraudCaseHistoryNotes("Modified by " + username);
			fraudHistorySessionHome.persistFrdFraudCaseHistory(entityFraudHistory);
			
			//Add fraud case history dan jalankan BPM jika diperlukan
			//Jika escalate, buat history untuk escalate dan jalankan fungsi escalate di bpm
			//Jika close, buat history untuk close case dan jalankan fungsi complete task di bpm
			if (!action.equalsIgnoreCase("")) {
				//Buka koneksi ke lombardi
				BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(username, password);
				bpmAdapter.synchronize();

				//Ambil role saat ini dari bpm (lombardi)
				bpmAdapter.getWebAPI().reassignTask(taskId);
				String role = bpmAdapter.getWebAPI().getTask(taskId).getParticipantDisplayName();
				bpmAdapter.getWebAPI().assignTask(taskId);
				
				//Siapkan PK untuk history
				frdFraudCaseHistoryPK = new FrdFraudCaseHistoryPK();
				frdFraudCaseHistoryPK.setFraudSuspicionCaseId(fraudCaseId);
				frdFraudCaseHistoryPK.setFraudCaseHistoryDate(new Timestamp(System.currentTimeMillis()));
				entityFraudHistory.setId(frdFraudCaseHistoryPK);
				
				if (action.equalsIgnoreCase("escalate")) {
					//Ambil dari config file untuk superior role
					String superiorRole = "";
					Properties properties = new Properties();
					try {
						properties.load(new FileInputStream(System.getenv("VENICE_HOME") +  "/lib/WebAPIFactory.properties"));
						String[] roles = properties.getProperty("fraud.bpm.role.hierarchy").split("~");
						
						for (int i = 0; i < roles.length; i++) {
							if (roles[i].equalsIgnoreCase(role) && i < roles.length - 1) {
								superiorRole = roles[i + 1];
								break;
							}
						}
					} catch (FileNotFoundException e) {
						rafDsResponse.setStatus(-1);
						e.printStackTrace();
					} catch (IOException e) {
						rafDsResponse.setStatus(-1);
						e.printStackTrace();
					}
					
					//Buat history escalate jika memang masih ada superior
					if (!superiorRole.equalsIgnoreCase("")) {
						entityFraudHistory.setFraudCaseHistoryNotes("Escalated to " + superiorRole + " by " + username + " (" + role + ")");
						fraudHistorySessionHome.persistFrdFraudCaseHistory(entityFraudHistory);
						
						//Dapatkan semua variable dari lombardi
						String orderId = bpmAdapter.getExternalDataVariableAsString(taskId, ProcessNameTokens.ORDERID);
						String wcsOrderId = bpmAdapter.getExternalDataVariableAsString(taskId, ProcessNameTokens.WCSORDERID);
						String fraudStatusId = bpmAdapter.getExternalDataVariableAsString(taskId, ProcessNameTokens.FRAUDSTATUSID);
						
						//Jalankan action di bpm (lombardi)
						try {
							Variable[] variables = new Variable[] {
								new Variable("escalate", "TRUE"),
								new Variable(ProcessNameTokens.FRAUDCASEID, fraudCaseId.toString()),
								new Variable(ProcessNameTokens.ORDERID, orderId),
								new Variable(ProcessNameTokens.WCSORDERID, wcsOrderId),
								new Variable(ProcessNameTokens.FRAUDSTATUSID, fraudStatusId)
							};
							
							bpmAdapter.getWebAPI().completeTask(taskId, variables);
						} catch (NumberFormatException e) {
							rafDsResponse.setStatus(-1);
							e.printStackTrace();
						} catch (Exception e) {
							rafDsResponse.setStatus(-1);
							e.printStackTrace();
						}
					}
				}
				else if (action.equalsIgnoreCase("closecase")) {
					//Buat history close case
					entityFraudHistory.setFraudCaseHistoryNotes("Closed by " + username + " (" + role + ")");
					fraudHistorySessionHome.persistFrdFraudCaseHistory(entityFraudHistory);
					
					//Jalankan action di bpm (lombardi)
					try {
						Variable[] variables = new Variable[] {
							new Variable("escalate", "FALSE")
						};
						
						bpmAdapter.getWebAPI().completeTask(taskId, variables);
						System.out.println("\n Case closed by: "+ username + " (" + role + ")");
					} catch (NumberFormatException e) {
						rafDsResponse.setStatus(-1);
						e.printStackTrace();
					} catch (Exception e) {
						rafDsResponse.setStatus(-1);
						e.printStackTrace();
					}
				}
			}
			
			//Jika berhasil set status = 0
			rafDsResponse.setData(new ArrayList<HashMap<String, String>>());
			rafDsResponse.setStatus(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if (locator != null) {
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		//Return RafDSResponse
		rafDsResponse.setData(new ArrayList<HashMap<String, String>>());
		return rafDsResponse;
	}
}