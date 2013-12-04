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
 * Update Command for User Group Detail
 * 
 * @author Roland
 */

public class UpdateUserDetailGroupDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateUserDetailGroupDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<RafUserGroupMembership> rafUserGroupMembershipList = new ArrayList<RafUserGroupMembership>();
		List<RafUserGroupMembership> rafUserGroupMembershipListCheck = new ArrayList<RafUserGroupMembership>();
		List<HashMap<String,String >> dataList = request.getData();		
		RafUserGroupMembership rafUserGroupMembership = new RafUserGroupMembership();
		Boolean status=false;
		String groupId="";
		
		//because only group id can be changed in screen, so only group id sent from servlet, user id must be sent as parameter.
		String userId=request.getParams().get(DataNameTokens.RAFUSER_USERID).toString();
		
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafUserGroupMembershipSessionEJBRemote sessionHome = (RafUserGroupMembershipSessionEJBRemote) locator.lookup(RafUserGroupMembershipSessionEJBRemote.class, "RafUserGroupMembershipSessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if(key.equals(DataNameTokens.RAFUSERGROUP_RAFUSERGROUPID)){
						try{
							rafUserGroupMembership = sessionHome.queryByRange("select o from RafUserGroupMembership o where o.rafUserGroupMembershipId="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							rafUserGroupMembership.setRafUserGroupMembershipId(new Long(data.get(key)));
						}
						break;
					}
				}						
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if(key.equals(DataNameTokens.RAFUSER_RAFUSERGROUP_USERID)){
						RafUser user = new RafUser();
						user.setUserId(new Long(data.get(key)));
						rafUserGroupMembership.setRafUser(user);					
					} else if(key.equals(DataNameTokens.RAFUSER_RAFUSERGROUP_GROUPID)){
						RafGroup group =  new RafGroup();
						group.setGroupId(new Long(data.get(key)));
						rafUserGroupMembership.setRafGroup(group);
						groupId=new Long(data.get(DataNameTokens.RAFUSER_RAFUSERGROUP_GROUPID)).toString();
					}
				}						
				
				rafUserGroupMembershipList.add(rafUserGroupMembership);			
			}
					
			//check first if the user and group already exist in database
			String query = "select o from RafUserGroupMembership o where o.rafUser.userId="+userId+" and o.rafGroup.groupId="+groupId;
			rafUserGroupMembershipListCheck = sessionHome.queryByRange(query, 0, 0);
			if(rafUserGroupMembershipListCheck.size()>0){
				status=true;
			}else{
				status=false;
			}
			
			if(status==false){
				//data is unique so update the database
				sessionHome.mergeRafUserGroupMembershipList((ArrayList<RafUserGroupMembership>)rafUserGroupMembershipList);
				rafDsResponse.setStatus(0);
			}else{
				//data already exist
				rafDsResponse.setStatus(2);
			}
			
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
