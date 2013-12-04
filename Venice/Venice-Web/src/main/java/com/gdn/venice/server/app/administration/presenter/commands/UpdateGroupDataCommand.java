package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafGroupSessionEJBRemote;
import com.gdn.venice.persistence.RafGroup;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for Group Maintenance
 * 
 * @author Roland
 */

public class UpdateGroupDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateGroupDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafGroup> rafGroupList = new ArrayList<RafGroup>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafGroup rafGroup = new RafGroup();
		
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafGroupSessionEJBRemote sessionHome = (RafGroupSessionEJBRemote) locator.lookup(RafGroupSessionEJBRemote.class, "RafGroupSessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();
				String key;
				while (iter.hasNext()) {
					key = iter.next();
					if (key.equals(DataNameTokens.RAFGROUP_GROUPID)) {
						try{
							rafGroup = sessionHome.queryByRange("select o from RafGroup o where o.groupId="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							rafGroup.setGroupId(new Long(data.get(key)));
						}
						break;	
					}
				}
				
				iter = data.keySet().iterator();
				while (iter.hasNext()) {
					key = iter.next();
					if (key.equals(DataNameTokens.RAFGROUP_GROUPNAME)) {
						rafGroup.setGroupName(data.get(key));
					} else if (key.equals(DataNameTokens.RAFGROUP_GROUPDESC)) {
						rafGroup.setGroupDesc(data.get(key));	
					} 
				}						
				rafGroupList.add(rafGroup);			
			}
			
			sessionHome.mergeRafGroupList((ArrayList<RafGroup>)rafGroupList);
			
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
