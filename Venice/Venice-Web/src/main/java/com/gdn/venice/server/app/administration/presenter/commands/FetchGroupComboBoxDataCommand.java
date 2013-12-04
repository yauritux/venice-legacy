package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.RafGroupSessionEJBRemote;
import com.gdn.venice.persistence.RafGroup;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Group
 * 
 * @author Roland
 */

public class FetchGroupComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			RafGroupSessionEJBRemote sessionHome = (RafGroupSessionEJBRemote) locator.lookup(RafGroupSessionEJBRemote.class, "RafGroupSessionEJBBean");			
			List<RafGroup> group = null;
			String query = "select o from RafGroup o";
			group = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<group.size();i++){
				RafGroup list = group.get(i);
				map.put("data"+Util.isNull(list.getGroupId(), "").toString(), Util.isNull(list.getGroupName(),"").toString());							
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
