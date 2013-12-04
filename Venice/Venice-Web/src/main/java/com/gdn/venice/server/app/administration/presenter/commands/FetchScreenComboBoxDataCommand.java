package com.gdn.venice.server.app.administration.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.RafApplicationObjectSessionEJBRemote;
import com.gdn.venice.persistence.RafApplicationObject;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Screen
 * 
 * @author Roland
 */

public class FetchScreenComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			RafApplicationObjectSessionEJBRemote sessionHome = (RafApplicationObjectSessionEJBRemote) locator.lookup(RafApplicationObjectSessionEJBRemote.class, "RafApplicationObjectSessionEJBBean");			
			List<RafApplicationObject> screen = null;
			String query = "select o from RafApplicationObject o";
			screen = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<screen.size();i++){
				RafApplicationObject list = screen.get(i);
				map.put("data"+Util.isNull(list.getApplicationObjectId(), "").toString(), Util.isNull(list.getApplicationObjectCanonicalName(),"").toString()+" - "+Util.isNull(list.getApplicationObjectUuid(),"").toString());							
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
