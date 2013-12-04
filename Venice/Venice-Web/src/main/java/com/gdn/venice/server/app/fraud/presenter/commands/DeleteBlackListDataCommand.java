package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote;
import com.gdn.venice.persistence.FrdEntityBlacklist;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for Blacklist Maintenance
 * 
 * @author Roland
 */

public class DeleteBlackListDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteBlackListDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<FrdEntityBlacklist> blacklistList = new ArrayList<FrdEntityBlacklist>();		
		List<HashMap<String,String >> dataList = request.getData();		
		FrdEntityBlacklist entityBlacklist = new FrdEntityBlacklist();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID)) {
					entityBlacklist.setEntityBlacklistId(new Long(data.get(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID)));
				} 
			}						
			blacklistList.add(entityBlacklist);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FrdEntityBlacklistSessionEJBRemote sessionHome = (FrdEntityBlacklistSessionEJBRemote) locator.lookup(FrdEntityBlacklistSessionEJBRemote.class, "FrdEntityBlacklistSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<blacklistList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(blacklistList.get(i).getEntityBlacklistId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDENTITYBLACKLIST_ENTITYBLACKLISTID));
				criteria.add(simpleCriteria);
			}
			
			blacklistList = sessionHome.findByFrdEntityBlacklistLike(entityBlacklist, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeFrdEntityBlacklistList((ArrayList<FrdEntityBlacklist>)blacklistList);			
									
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
