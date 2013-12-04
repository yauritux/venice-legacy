package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafUserGroupMembershipSessionEJBRemote;
import com.gdn.venice.persistence.RafGroup;
import com.gdn.venice.persistence.RafUser;
import com.gdn.venice.persistence.RafUserGroupMembership;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for User Group Detail
 * 
 * @author Roland
 */

public class DeleteUserDetailGroupDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteUserDetailGroupDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafUserGroupMembership> rafUserGroupMembershipList = new ArrayList<RafUserGroupMembership>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafUserGroupMembership rafUserGroupMembership = new RafUserGroupMembership();
		String groupId="";
		String userId="";
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.RAFUSER_RAFUSERGROUP_USERID)) {
					RafUser user = new RafUser();
					user.setUserId(new Long(data.get(DataNameTokens.RAFUSER_RAFUSERGROUP_USERID)));
					rafUserGroupMembership.setRafUser(user);
					userId=new Long(data.get(DataNameTokens.RAFUSER_RAFUSERGROUP_USERID)).toString();
				} else if (key.equals(DataNameTokens.RAFUSER_RAFUSERGROUP_GROUPID)) {
					RafGroup group = new RafGroup();
					group.setGroupId(new Long(data.get(DataNameTokens.RAFUSER_RAFUSERGROUP_GROUPID)));
					rafUserGroupMembership.setRafGroup(group);
					groupId=new Long(data.get(DataNameTokens.RAFUSER_RAFUSERGROUP_GROUPID)).toString();
				} 
			}						
			rafUserGroupMembershipList.add(rafUserGroupMembership);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafUserGroupMembershipSessionEJBRemote sessionHome = (RafUserGroupMembershipSessionEJBRemote) locator.lookup(RafUserGroupMembershipSessionEJBRemote.class, "RafUserGroupMembershipSessionEJBBean");
			String query="";
			
			for (int i=0;i<rafUserGroupMembershipList.size();i++) {
				query = "select o from RafUserGroupMembership o  where o.rafUser.userId="+userId+" and o.rafGroup.groupId="+groupId;

				rafUserGroupMembershipList=sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				sessionHome.removeRafUserGroupMembershipList((ArrayList<RafUserGroupMembership>)rafUserGroupMembershipList);
			}									
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
