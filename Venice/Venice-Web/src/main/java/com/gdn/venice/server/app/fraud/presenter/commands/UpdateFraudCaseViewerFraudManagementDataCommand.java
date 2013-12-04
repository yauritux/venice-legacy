package com.gdn.venice.server.app.fraud.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudCaseHistory;
import com.gdn.venice.persistence.FrdFraudCaseHistoryPK;
import com.gdn.venice.persistence.FrdFraudCaseStatus;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for update notes in Fraud case viewer
 * 
 * @author Roland
 */

public class UpdateFraudCaseViewerFraudManagementDataCommand implements RafDsCommand {
	String action;
	String username;
	String password;
	RafDsRequest request;
	
	public UpdateFraudCaseViewerFraudManagementDataCommand(
													 String username,
													 String password,
													 RafDsRequest request) {

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
				
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			//Update fraud case
			FrdFraudSuspicionCaseSessionEJBRemote fraudSuspicionSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
			
			//Siapkan java bean baik untuk history maupun fraud suspicion case
			Long fraudCaseId = new Long("-1");
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
					if (key.equals(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES)) {
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
			entityFraudHistory.setFraudCaseHistoryNotes("Notes modified by " + username);
			fraudHistorySessionHome.persistFrdFraudCaseHistory(entityFraudHistory);
					
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

		rafDsResponse.setData(new ArrayList<HashMap<String, String>>());
		return rafDsResponse;
	}
}