package com.gdn.venice.server.app.task.presenter.commands;

import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.RafUserRoleSessionEJBRemote;
import com.gdn.venice.persistence.RafUserRole;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.util.VeniceConstants;

/**
 * Class for checking user role
 * 
 * @author Roland
 */

public class GetUserRoleCommand implements RafRpcCommand {
	String username;
	
	public GetUserRoleCommand(String parameter, String username) {
		this.username=username;
	}

	@Override
	public String execute() {
		Locator<Object> locator = null;		
		try {
			locator = new Locator<Object>();			
			RafUserRoleSessionEJBRemote sessionHome = (RafUserRoleSessionEJBRemote) locator
			.lookup(RafUserRoleSessionEJBRemote.class, "RafUserRoleSessionEJBBean");
			
			//get user role based on username
			System.out.println("get user role based on username for username: "+username);
			String select = "select o from RafUserRole o where o.rafUser.loginName='"+username+ "'";							
			List<RafUserRole> permissionList = sessionHome.queryByRange(select, 0,0);	
			
			String roles="";
			if(permissionList.size()>0){
				long roleId=0;
				for (int i=0;i<permissionList.size();i++) {						
					roleId = permissionList.get(i).getRafRole().getRoleId();
					
					if(roleId==VeniceConstants.VEN_ROLE_FINANCE){
						System.out.println("finance role found");
						roles+=VeniceConstants.VEN_ROLE_NAME_FINANCE;
						roles+="#";
					} else if(roleId==VeniceConstants.VEN_ROLE_FRAUD){
						System.out.println("fraud role found");
						roles+=VeniceConstants.VEN_ROLE_NAME_FRAUD;
						roles+="#";
					} else if(roleId==VeniceConstants.VEN_ROLE_LOGISTIC){
						System.out.println("logistic role found");
						roles+=VeniceConstants.VEN_ROLE_NAME_LOGISTIC;
						roles+="#";
					}
				}	
			}			
			
			return roles;
			
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "-1";
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
}

