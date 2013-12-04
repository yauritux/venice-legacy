package com.gdn.venice.server.app.kpi.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.facade.VenPartySessionEJBRemote;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Blacklist Source
 * 
 * @author Roland
 */

public class FetchKpiSetupPartySlaDataCommandComboBoxParty implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			VenPartySessionEJBRemote sessionHome = (VenPartySessionEJBRemote) locator.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");			
			List<VenParty> venPartySource = null;
			String query = "select o from VenParty o where o.venPartyType.partyTypeId=1 or  o.venPartyType.partyTypeId=2";
			
			venPartySource = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<venPartySource.size();i++){
				VenParty list = venPartySource.get(i);
				map.put("data"+list.getPartyId().toString(), list.getFullOrLegalName());						
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
