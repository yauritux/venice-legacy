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
 * Add Command for Blacklist Maintenance
 * 
 * @author Roland
 */

public class AddBlackListDataCommand implements RafDsCommand {

	RafDsRequest request;
	String username;
	
	public AddBlackListDataCommand(RafDsRequest request, String username){
		this.request=request;
		this.username = username;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<Object> locator=null;
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		try{
			locator = new Locator<Object>();
			FrdEntityBlacklistSessionEJBRemote sessionHome = (FrdEntityBlacklistSessionEJBRemote) locator.lookup(FrdEntityBlacklistSessionEJBRemote.class, "FrdEntityBlacklistSessionEJBBean");
			dataList=request.getData();
			
			FrdEntityBlacklist blacklist = new FrdEntityBlacklist();
			blacklist.setCreatedby(username);
			blacklist.setBlacklistTimestamp(new Timestamp(System.currentTimeMillis()));	
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID)){
						blacklist.setEntityBlacklistId(new Long(data.get(key)));
					} else if(key.equals(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTSTRING)){
						blacklist.setBlacklistString(data.get(key));
					} else if(key.equals(DataNameTokens.FRDENTITYBLACKLIST_BLACKORWHITELIST)){
						blacklist.setBlackOrWhiteList(data.get(key));
					} else if(key.equals(DataNameTokens.FRDENTITYBLACKLIST_DESCRIPTION)){
						blacklist.setDescription(data.get(key));
					}
				}
			}
					
			blacklist=sessionHome.persistFrdEntityBlacklist(blacklist);
			rafDsResponse.setStatus(0);
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
