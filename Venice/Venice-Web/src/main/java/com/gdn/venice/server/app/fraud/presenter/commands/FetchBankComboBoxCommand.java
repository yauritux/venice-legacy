package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.VenBankSessionEJBRemote;
import com.gdn.venice.persistence.VenBank;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Bank
 * 
 * @author Roland
 */

public class FetchBankComboBoxCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			VenBankSessionEJBRemote sessionHome = (VenBankSessionEJBRemote) locator.lookup(VenBankSessionEJBRemote.class, "VenBankSessionEJBBean");			
			List<VenBank> bank = null;
			String query = "select o from VenBank o";
			bank = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<bank.size();i++){
				VenBank list = bank.get(i);
				map.put("data"+Util.isNull(list.getBankId(), "").toString(), Util.isNull(list.getBankShortName(),"").toString());							
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
