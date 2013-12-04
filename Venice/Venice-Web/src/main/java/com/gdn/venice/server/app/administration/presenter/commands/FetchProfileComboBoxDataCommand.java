package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.RafProfileSessionEJBRemote;
import com.gdn.venice.persistence.RafProfile;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Profile
 * 
 * @author Roland
 */

public class FetchProfileComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			RafProfileSessionEJBRemote sessionHome = (RafProfileSessionEJBRemote) locator.lookup(RafProfileSessionEJBRemote.class, "RafProfileSessionEJBBean");			
			List<RafProfile> profile = null;
			String query = "select o from RafProfile o";
			profile = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<profile.size();i++){
				RafProfile list = profile.get(i);
				map.put("data"+Util.isNull(list.getProfileId(), "").toString(), Util.isNull(list.getProfileName(),"").toString());							
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
