package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.facade.VenPartyTypeSessionEJBRemote;
import com.gdn.venice.persistence.VenPartyType;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Action Log Type
 * 
 * @author Roland
 */

public class FetchFraudCaseActionLogTypeComboBoxCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			VenPartyTypeSessionEJBRemote sessionHome = (VenPartyTypeSessionEJBRemote) locator.lookup(VenPartyTypeSessionEJBRemote.class, "VenPartyTypeSessionEJBBean");			
			List<VenPartyType> partyType = null;
			String query = "select o from VenPartyType o";
			
			partyType = sessionHome.queryByRange(query, 0, 100);
			
			for(int i=0; i<partyType.size();i++){
				VenPartyType list = partyType.get(i);
				map.put("data"+com.gdn.venice.server.util.Util.isNull(list.getPartyTypeId(), "").toString(), com.gdn.venice.server.util.Util.isNull(list.getPartyTypeDesc(), "").toString());							
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
