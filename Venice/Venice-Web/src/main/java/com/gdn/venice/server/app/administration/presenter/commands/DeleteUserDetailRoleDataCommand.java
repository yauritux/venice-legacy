package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafUserRoleSessionEJBRemote;
import com.gdn.venice.persistence.RafRole;
import com.gdn.venice.persistence.RafUser;
import com.gdn.venice.persistence.RafUserRole;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for User Role Detail
 * 
 * @author Roland
 */

public class DeleteUserDetailRoleDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteUserDetailRoleDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafUserRole> rafUserRoleList = new ArrayList<RafUserRole>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafUserRole rafUserRole = new RafUserRole();
		String roleId="";
		String userId="";
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.RAFUSER_RAFUSERROLES_USERID)) {
					RafUser user = new RafUser();
					user.setUserId(new Long(data.get(DataNameTokens.RAFUSER_RAFUSERROLES_USERID)));
					rafUserRole.setRafUser(user);
					userId=new Long(data.get(DataNameTokens.RAFUSER_RAFUSERROLES_USERID)).toString();
				} else if (key.equals(DataNameTokens.RAFUSER_RAFUSERROLES_ROLEID)) {
					RafRole role = new RafRole();
					role.setRoleId(new Long(data.get(DataNameTokens.RAFUSER_RAFUSERROLES_ROLEID)));
					rafUserRole.setRafRole(role);
					roleId=new Long(data.get(DataNameTokens.RAFUSER_RAFUSERROLES_ROLEID)).toString();
				} 
			}						
			rafUserRoleList.add(rafUserRole);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafUserRoleSessionEJBRemote sessionHome = (RafUserRoleSessionEJBRemote) locator.lookup(RafUserRoleSessionEJBRemote.class, "RafUserRoleSessionEJBBean");
			String query="";
			
			for (int i=0;i<rafUserRoleList.size();i++) {
				query = "select o from RafUserRole o  where o.rafUser.userId="+userId+" and o.rafRole.roleId="+roleId;

				rafUserRoleList=sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				sessionHome.removeRafUserRoleList((ArrayList<RafUserRole>)rafUserRoleList);
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
