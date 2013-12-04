package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.LinkedHashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.facade.FinAccountSessionEJBRemote;
import com.gdn.venice.persistence.FinAccount;
import com.gdn.venice.server.command.RafRpcCommand;

public class FetchManualJournalAccountComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<FinAccount> finAccountLocator=null;

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		try{
			finAccountLocator = new Locator<FinAccount>();			
			FinAccountSessionEJBRemote sessionHome = (FinAccountSessionEJBRemote) finAccountLocator.lookup(FinAccountSessionEJBRemote.class, "FinAccountSessionEJBBean");			
			List<FinAccount> finAccountList = null;
			String query = "select o from FinAccount o order by o.accountDesc asc";
			
			finAccountList = sessionHome.queryByRange(query, 0, 50);
			
			for(int i=0; i<finAccountList.size();i++){
				FinAccount finAccount = finAccountList.get(i);
				map.put("data"+finAccount.getAccountId().toString(), finAccount.getAccountNumber().toString() + "-" + finAccount.getAccountDesc());							
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				finAccountLocator.close();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
//		return "0:" + Util.formXMLfromHashMap(map);
		return Util.formXMLfromHashMap(map);
	}	
}
