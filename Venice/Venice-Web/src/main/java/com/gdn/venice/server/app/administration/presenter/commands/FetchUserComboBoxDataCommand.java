package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.RafUserSessionEJBRemote;
import com.gdn.venice.persistence.RafUser;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for User
 * 
 * @author Roland
 */

public class FetchUserComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			RafUserSessionEJBRemote sessionHome = (RafUserSessionEJBRemote) locator.lookup(RafUserSessionEJBRemote.class, "RafUserSessionEJBBean");			
			List<RafUser> user = null;
			String query = "select o from RafUser o";
			user = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<user.size();i++){
				RafUser list = user.get(i);
				map.put("data"+Util.isNull(list.getUserId(), "").toString(), Util.isNull(list.getLoginName(),"").toString());							
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
