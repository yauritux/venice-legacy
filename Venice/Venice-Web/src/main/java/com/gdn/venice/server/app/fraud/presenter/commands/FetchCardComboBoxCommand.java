package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.VenCardTypeSessionEJBRemote;
import com.gdn.venice.persistence.VenCardType;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Card Type
 * 
 * @author Roland
 */

public class FetchCardComboBoxCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			VenCardTypeSessionEJBRemote sessionHome = (VenCardTypeSessionEJBRemote) locator.lookup(VenCardTypeSessionEJBRemote.class, "VenCardTypeSessionEJBBean");			
			List<VenCardType> card = null;
			String query = "select o from VenCardType o";
			card = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<card.size();i++){
				VenCardType list = card.get(i);
				map.put("data"+Util.isNull(list.getCardTypeId(), "").toString(), Util.isNull(list.getCardTypeDesc(), "").toString());							
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
