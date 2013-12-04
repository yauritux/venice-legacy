package com.gdn.venice.server.app.fraud.presenter.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudCaseHistory;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.server.bpmenablement.BPMAdapter;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;
import com.lombardisoftware.webapi.Role;

public class FetchFraudCaseDataCommand implements RafDsCommand {
	RafDsRequest request;
	String username;
	String password;
	
	public FetchFraudCaseDataCommand(RafDsRequest request, String username, String password) {
		this.request = request;
		this.username = username;
		this.password = password;
	}
	
	@Override
	public RafDsResponse execute() {
		//Dapatkan role tertinggi dari config file
		String highestRole = "";
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(System.getenv("VENICE_HOME") +  "/lib/WebAPIFactory.properties"));
			String[] roles = properties.getProperty("fraud.bpm.role.hierarchy").split("~");
			highestRole = roles.length > 0 ? roles[roles.length - 1] : "";
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Variable yang digunakan untuk menentukan apakah fraud case masih bisa diedit setelah terclose
		Boolean isHighestRole = false;
		if (highestRole != "") {
			//Dapatkan dari lombardi, apakah current user adalah role tertinggi
			//Buka koneksi ke lombardi
			BPMAdapter bpmAdapter = BPMAdapter.getBPMAdapter(username, password);
			bpmAdapter.synchronize();
			
			try {
				Role role[] = bpmAdapter.getWebAPI().getCurrentUser().getRoleMemberships();
				for (int i = 0; i < role.length; i++) {
					if (role[i].getDisplayName().equalsIgnoreCase(highestRole)) {
						isHighestRole = true;
						break;
					}
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			FrdFraudSuspicionCaseSessionEJBRemote sessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
			List<FrdFraudSuspicionCase> fraudCaseList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			if (criteria == null) {
				String query = "select o from FrdFraudSuspicionCase o";
				fraudCaseList = sessionHome.queryByRange(query, 0, 50);
			} else {
				FrdFraudSuspicionCase fraudCase = new FrdFraudSuspicionCase();
				fraudCaseList = sessionHome.findByFrdFraudSuspicionCaseLike(fraudCase, criteria, 0, 0);
			}
			
			FrdFraudCaseHistorySessionEJBRemote historySessionHome = (FrdFraudCaseHistorySessionEJBRemote) locator.lookup(FrdFraudCaseHistorySessionEJBRemote.class, "FrdFraudCaseHistorySessionEJBBean");
			List<FrdFraudCaseHistory> fraudCaseHistoryList = null;
			
			for(int i = 0; i < fraudCaseList.size(); i++){
				HashMap<String, String> map = new HashMap<String, String>();
				FrdFraudSuspicionCase list = fraudCaseList.get(i);
				
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID, list.getVenOrder()!=null && list.getVenOrder().getWcsOrderId()!=null?list.getVenOrder().getWcsOrderId().toString():"");
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, Util.isNull(list.getFraudSuspicionCaseId(), "").toString());
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME, Util.isNull(list.getFraudCaseDateTime(), "").toString());
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC, Util.isNull(list.getFraudCaseDesc(), "").toString());
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES, Util.isNull(list.getFraudSuspicionNotes(), "").toString());
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC, list.getFrdFraudCaseStatus()!=null && list.getFrdFraudCaseStatus().getFraudCaseStatusDesc()!=null?list.getFrdFraudCaseStatus().getFraudCaseStatusDesc().toString():"");
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_ORDERDATE, list.getVenOrder()!=null && list.getVenOrder().getOrderDate()!=null?list.getVenOrder().getOrderDate().toString():"");
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS, Util.isNull(list.getFraudTotalPoints(), "").toString());
				
				fraudCaseHistoryList = historySessionHome.queryByRange("select o from FrdFraudCaseHistory o join fetch o.frdFraudSuspicionCase where o.frdFraudSuspicionCase.fraudSuspicionCaseId = " + list.getFraudSuspicionCaseId(), 0, 0);
				String taskStatus = "Open";
				String lastAction = "Calculated by System";
				Boolean isAbleToModifyAfterCompleted = false;
				
				if (fraudCaseHistoryList.size() > 0) {
					lastAction=fraudCaseHistoryList.get(fraudCaseHistoryList.size() - 1).getFraudCaseHistoryNotes();
					String historyNotes = fraudCaseHistoryList.get(fraudCaseHistoryList.size() - 1).getFraudCaseHistoryNotes().toLowerCase();
					if (historyNotes.contains("claimed")) {
						taskStatus = "In Process";
					}
					else if (historyNotes.contains("closed")) {
						taskStatus = "Completed";
						isAbleToModifyAfterCompleted = isHighestRole ? true : false;
					}
					else if (historyNotes.contains("modified")) {
						taskStatus = "In Process";
						for(int j = 0; j < fraudCaseHistoryList.size(); j++) {
							historyNotes = fraudCaseHistoryList.get(j).getFraudCaseHistoryNotes().toLowerCase();
							
							if (historyNotes.contains("closed")) {
								taskStatus = "Completed";
								isAbleToModifyAfterCompleted = isHighestRole ? true : false;
							}
						}
					}
				}
				map.put(DataNameTokens.TASKSTATUS, taskStatus);
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_ENABLEMODIFYAFTERCOMPLETED, isAbleToModifyAfterCompleted.toString());
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_LASTACTION, lastAction);
				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		} catch (Exception e) {
			e.printStackTrace();
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
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
