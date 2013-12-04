package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.RafApplicationObjectTypeSessionEJBRemote;
import com.gdn.venice.persistence.RafApplicationObjectType;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Module Type
 * 
 * @author Roland
 */

public class FetchModuleTypeComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			RafApplicationObjectTypeSessionEJBRemote sessionHome = (RafApplicationObjectTypeSessionEJBRemote) locator.lookup(RafApplicationObjectTypeSessionEJBRemote.class, "RafApplicationObjectTypeSessionEJBBean");			
			List<RafApplicationObjectType> moduleType = null;
			String query = "select o from RafApplicationObjectType o";
			moduleType = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<moduleType.size();i++){
				RafApplicationObjectType list = moduleType.get(i);
				map.put("data"+Util.isNull(list.getApplicationObjectTypeId(), "").toString(), Util.isNull(list.getApplicationObjectTypeDesc(),"").toString());							
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
