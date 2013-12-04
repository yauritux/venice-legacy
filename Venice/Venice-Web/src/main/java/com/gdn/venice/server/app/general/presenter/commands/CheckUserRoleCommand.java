package com.gdn.venice.server.app.general.presenter.commands;

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

public class CheckUserRoleCommand implements RafRpcCommand {
	String username;
	String roleNeeded;
	
	public CheckUserRoleCommand(String parameter, String username, String roleNeeded) {
		this.username=username;
		this.roleNeeded=roleNeeded;
	}

	@Override
	public String execute() {				
		//split the role if the action can be done by more than one role
		String[] roleNeededSplit = roleNeeded.split(",");
		Locator<Object> locator = null;		
		try {
			locator = new Locator<Object>();			
			RafUserRoleSessionEJBRemote sessionHome = (RafUserRoleSessionEJBRemote) locator
			.lookup(RafUserRoleSessionEJBRemote.class, "RafUserRoleSessionEJBBean");
			
			//get user role based on username
			System.out.println("get user role based on username for username: "+username);
			String select = "select o from RafUserRole o where o.rafUser.loginName='"+username+ "'";							
			List<RafUserRole> permissionList = sessionHome.queryByRange(select, 0,0);	
			
			Boolean roleFound=false;
			if(permissionList.size()>0){
				long roleId=0;
				for (int j=0;j<roleNeededSplit.length;j++){
					for (int i=0;i<permissionList.size();i++) {						
						roleId = permissionList.get(i).getRafRole().getRoleId();
						
						//check for role needed here
						if(roleNeededSplit[j].trim().equals(VeniceConstants.VEN_ROLE_NAME_FINANCE)){
							if(roleId==VeniceConstants.VEN_ROLE_FINANCE){
								System.out.println("finance role found");
								roleFound=true;
								break;
							}
						}
						
						if(roleNeededSplit[j].trim().equals(VeniceConstants.VEN_ROLE_NAME_FRAUD)){
							if(roleId==VeniceConstants.VEN_ROLE_FRAUD){
								System.out.println("fraud role found");
								roleFound=true;
								break;
							}
						}
					}	
				}
			}			
			if(roleFound==true){
				return "1";
			}else{
				return "0";
			}
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

