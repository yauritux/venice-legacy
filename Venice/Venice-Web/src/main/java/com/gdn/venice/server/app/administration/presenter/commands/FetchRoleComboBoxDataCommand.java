package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.RafRoleSessionEJBRemote;
import com.gdn.venice.persistence.RafRole;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Role
 * 
 * @author Roland
 */

public class FetchRoleComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			RafRoleSessionEJBRemote sessionHome = (RafRoleSessionEJBRemote) locator.lookup(RafRoleSessionEJBRemote.class, "RafRoleSessionEJBBean");			
			List<RafRole> role = null;
			String query = "select o from RafRole o";
			role = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<role.size();i++){
				RafRole list = role.get(i);
				map.put("data"+Util.isNull(list.getRoleId(), "").toString(), Util.isNull(list.getRoleName(),"").toString());							
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return Util.formXMLfromHashMap(map);
	}	
}
