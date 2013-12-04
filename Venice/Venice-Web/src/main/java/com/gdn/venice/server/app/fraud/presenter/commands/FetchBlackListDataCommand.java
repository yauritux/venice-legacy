package com.gdn.venice.server.app.fraud.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote;
import com.gdn.venice.persistence.FrdEntityBlacklist;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Blacklist Maintenance
 * 
 * @author Roland
 */

public class FetchBlackListDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchBlackListDataCommand(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			FrdEntityBlacklistSessionEJBRemote sessionHome = (FrdEntityBlacklistSessionEJBRemote) locator.lookup(FrdEntityBlacklistSessionEJBRemote.class, "FrdEntityBlacklistSessionEJBBean");			
			List<FrdEntityBlacklist> blackList = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from FrdEntityBlacklist o";			
				blackList = sessionHome.queryByRange(query, 0, 50);
			} else {
				FrdEntityBlacklist bl = new FrdEntityBlacklist();
				blackList = sessionHome.findByFrdEntityBlacklistLike(bl, criteria, 0, 0);
			}
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0; i<blackList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				FrdEntityBlacklist list = blackList.get(i);
				map.put(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID, Util.isNull(list.getEntityBlacklistId(), "").toString());
				map.put(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTSTRING, Util.isNull(list.getBlacklistString(), "").toString());
				map.put(DataNameTokens.FRDENTITYBLACKLIST_BLACKORWHITELIST, Util.isNull(list.getBlackOrWhiteList(), "").toString());
				map.put(DataNameTokens.FRDENTITYBLACKLIST_DESCRIPTION, Util.isNull(list.getDescription(), "").toString());
				map.put(DataNameTokens.FRDENTITYBLACKLIST_CREATEDBY, Util.isNull(list.getCreatedby(), "").toString());
				
		        String s =  sdf.format(list.getBlacklistTimestamp());
		        StringBuilder sb = new StringBuilder(s);
				map.put(DataNameTokens.FRDENTITYBLACKLIST_BLACKLISTTIMESTAMP, sb.toString());
				
				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
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
