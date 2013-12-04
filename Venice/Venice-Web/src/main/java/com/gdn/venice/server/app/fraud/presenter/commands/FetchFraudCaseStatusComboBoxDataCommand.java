package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.FrdFraudCaseStatusSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudCaseStatus;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Group
 * 
 * @author Roland
 */

public class FetchFraudCaseStatusComboBoxDataCommand implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			FrdFraudCaseStatusSessionEJBRemote sessionHome = (FrdFraudCaseStatusSessionEJBRemote) locator.lookup(FrdFraudCaseStatusSessionEJBRemote.class, "FrdFraudCaseStatusSessionEJBBean");			
			List<FrdFraudCaseStatus> status = null;
			String query = "select o from FrdFraudCaseStatus o";
			status = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<status.size();i++){
				FrdFraudCaseStatus list = status.get(i);
				map.put("data"+Util.isNull(list.getFraudCaseStatusId(), "").toString(), Util.isNull(list.getFraudCaseStatusDesc(),"").toString());
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
