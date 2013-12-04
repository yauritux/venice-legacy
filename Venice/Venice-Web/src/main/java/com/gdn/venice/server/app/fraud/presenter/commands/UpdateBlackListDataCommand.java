package com.gdn.venice.server.app.fraud.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote;
import com.gdn.venice.persistence.FrdEntityBlacklist;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for Blacklist Maintenance
 * 
 * @author Roland
 */

public class UpdateBlackListDataCommand implements RafDsCommand {
	RafDsRequest request;
	String username;
	
	public UpdateBlackListDataCommand(RafDsRequest request, String username) {
		this.request = request;
		this.username = username;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<FrdEntityBlacklist> blacklistList = new ArrayList<FrdEntityBlacklist>();		
		List<HashMap<String,String >> dataList = request.getData();		
		FrdEntityBlacklist entityBlacklist = new FrdEntityBlacklist();
		entityBlacklist.setCreatedby(username);
		entityBlacklist.setBlacklistTimestamp(new Timestamp(System.currentTimeMillis()));
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FrdEntityBlacklistSessionEJBRemote sessionHome = (FrdEntityBlacklistSessionEJBRemote) locator.lookup(FrdEntityBlacklistSessionEJBRemote.class, "FrdEntityBlacklistSessionEJBBean");
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID)) {
						try{
							entityBlacklist = sessionHome.queryByRange("select o from FrdEntityBlacklist o where o.entityBlacklistId="+new Long(data.get(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							entityBlacklist.setEntityBlacklistId(new Long(data.get(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID)));
						}
						break;
					}
				}						
				
				iter = data.keySet().iterator();
				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTSTRING)) {
						entityBlacklist.setBlacklistString(data.get(key));
					} else if(key.equals(DataNameTokens.FRDENTITYBLACKLIST_BLACKORWHITELIST)){
						entityBlacklist.setBlackOrWhiteList(data.get(key));
					} else if(key.equals(DataNameTokens.FRDENTITYBLACKLIST_DESCRIPTION)){
						entityBlacklist.setDescription(data.get(key));
					}
				}						
				
				blacklistList.add(entityBlacklist);			
			}
			
			sessionHome.mergeFrdEntityBlacklistList((ArrayList<FrdEntityBlacklist>)blacklistList);
			
			rafDsResponse.setStatus(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(locator!=null){
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
