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
 * Update Command for User Role Detail
 * 
 * @author Roland
 */

public class UpdateUserDetailRoleDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateUserDetailRoleDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<RafUserRole> rafUserRoleList = new ArrayList<RafUserRole>();
		List<RafUserRole> rafUserRoleListCheck = new ArrayList<RafUserRole>();
		List<HashMap<String,String >> dataList = request.getData();		
		RafUserRole rafUserRole = new RafUserRole();
		Boolean status=false;
		String roleId="";
		
		//because only role id can be changed in screen, so only role id sent from servlet, user id must be sent as parameter.
		String userId=request.getParams().get(DataNameTokens.RAFUSER_USERID).toString();
						
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafUserRoleSessionEJBRemote sessionHome = (RafUserRoleSessionEJBRemote) locator.lookup(RafUserRoleSessionEJBRemote.class, "RafUserRoleSessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if(key.equals(DataNameTokens.RAFUSERROLE_RAFUSERROLEID)){
						try{
							rafUserRole = sessionHome.queryByRange("select o from RafUserRole o where o.rafUserRoleId="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							rafUserRole.setRafUserRoleId(new Long(data.get(key)));
						}
						break;
					}
				}
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if(key.equals(DataNameTokens.RAFUSER_RAFUSERROLES_USERID)){
						RafUser user = new RafUser();
						user.setUserId(new Long(data.get(key)));
						rafUserRole.setRafUser(user);					
					} else if(key.equals(DataNameTokens.RAFUSER_RAFUSERROLES_ROLEID)){
						RafRole role =  new RafRole();
						role.setRoleId(new Long(data.get(key)));
						rafUserRole.setRafRole(role);
						roleId=new Long(data.get(DataNameTokens.RAFUSER_RAFUSERROLES_ROLEID)).toString();
					}
				}
				
				rafUserRoleList.add(rafUserRole);			
			}

			//check first if the user and role already exist in database
			String query = "select o from RafUserRole o where o.rafUser.userId="+userId+" and o.rafRole.roleId="+roleId;
			rafUserRoleListCheck = sessionHome.queryByRange(query, 0, 0);
			if(rafUserRoleListCheck.size()>0){
				status=true;
			}else{
				status=false;
			}
			
			if(status==false){
				//data is unique so update the database
				sessionHome.mergeRafUserRoleList((ArrayList<RafUserRole>)rafUserRoleList);
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
