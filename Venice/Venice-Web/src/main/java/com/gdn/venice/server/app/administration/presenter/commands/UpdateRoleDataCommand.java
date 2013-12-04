package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.RafRoleSessionEJBRemote;
import com.gdn.venice.persistence.RafRole;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for Role
 * 
 * @author Roland
 */

public class UpdateRoleDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdateRoleDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<RafRole> rafRoleList = new ArrayList<RafRole>();		
		List<HashMap<String,String >> dataList = request.getData();		
		RafRole rafRole = new RafRole();
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			RafRoleSessionEJBRemote sessionHome = (RafRoleSessionEJBRemote) locator.lookup(RafRoleSessionEJBRemote.class, "RafRoleSessionEJBBean");

			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.RAFROLE_ROLEID)) {
						try
						{
							rafRole = sessionHome.queryByRange("select o from RafRole o where o.roleId="+new Long(data.get(key)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							rafRole.setRoleId(new Long(data.get(key)));
						}
						break;
					}
				}						
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.RAFROLE_ROLENAME)) {
						rafRole.setRoleName(data.get(key));
					} else if (key.equals(DataNameTokens.RAFROLE_ROLEDESC)) {
						rafRole.setRoleDesc(data.get(key));	
					} else if (key.equals(DataNameTokens.RAFROLE_PARENTROLE)) {
						RafRole parent = new RafRole();
						parent.setRoleId(new Long(data.get(key)));
						rafRole.setRafRole(parent);
					} 
				}						
				rafRoleList.add(rafRole);			
			}
			
			sessionHome.mergeRafRoleList((ArrayList<RafRole>)rafRoleList);
			
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
