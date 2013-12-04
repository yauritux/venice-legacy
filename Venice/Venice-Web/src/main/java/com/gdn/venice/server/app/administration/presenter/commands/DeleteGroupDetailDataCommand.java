package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafGroupRoleSessionEJBRemote;
import com.gdn.venice.persistence.RafGroup;
import com.gdn.venice.persistence.RafGroupRole;
import com.gdn.venice.persistence.RafRole;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for Group Detail
 * 
 * @author Roland
 */

public class DeleteGroupDetailDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteGroupDetailDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafGroupRole> rafGroupRoleList = new ArrayList<RafGroupRole>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafGroupRole rafGroupRole = new RafGroupRole();
		String roleId="";
		String groupId="";
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID)) {
					RafGroup group = new RafGroup();
					group.setGroupId(new Long(data.get(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID)));
					rafGroupRole.setRafGroup(group);
					groupId=new Long(data.get(DataNameTokens.RAFGROUP_RAFGROUPROLES_GROUPID)).toString();
				} else if (key.equals(DataNameTokens.RAFGROUP_RAFGROUPROLES_ROLEID)) {
					RafRole role = new RafRole();
					role.setRoleId(new Long(data.get(DataNameTokens.RAFGROUP_RAFGROUPROLES_ROLEID)));
					rafGroupRole.setRafRole(role);
					roleId=new Long(data.get(DataNameTokens.RAFGROUP_RAFGROUPROLES_ROLEID)).toString();
				} 
			}						
			rafGroupRoleList.add(rafGroupRole);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafGroupRoleSessionEJBRemote sessionHome = (RafGroupRoleSessionEJBRemote) locator.lookup(RafGroupRoleSessionEJBRemote.class, "RafGroupRoleSessionEJBBean");
			String query="";
			
			for (int i=0;i<rafGroupRoleList.size();i++) {
				query = "select o from RafGroupRole o  where o.rafGroup.groupId="+groupId+" and o.rafRole.roleId="+roleId;

				rafGroupRoleList=sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				sessionHome.removeRafGroupRoleList((ArrayList<RafGroupRole>)rafGroupRoleList);
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
