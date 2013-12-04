package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.RafPermissionTypeSessionEJBRemote;
import com.gdn.venice.persistence.RafPermissionType;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for PermissionType
 * 
 * @author Roland
 */

public class FetchPermissionComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			RafPermissionTypeSessionEJBRemote sessionHome = (RafPermissionTypeSessionEJBRemote) locator.lookup(RafPermissionTypeSessionEJBRemote.class, "RafPermissionTypeSessionEJBBean");			
			List<RafPermissionType> permission = null;
			String query = "select o from RafPermissionType o";
			permission = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<permission.size();i++){
				RafPermissionType list = permission.get(i);
				map.put("data"+Util.isNull(list.getPermissionTypeId(), "").toString(), Util.isNull(list.getPermissionTypeDesc(),"").toString());							
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
