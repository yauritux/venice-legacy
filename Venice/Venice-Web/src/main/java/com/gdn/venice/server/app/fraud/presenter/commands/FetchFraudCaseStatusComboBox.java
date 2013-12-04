package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.facade.FrdFraudCaseStatusSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudCaseStatus;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Fraud Case Status
 * 
 * @author Roland
 */

public class FetchFraudCaseStatusComboBox implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			FrdFraudCaseStatusSessionEJBRemote sessionHome = (FrdFraudCaseStatusSessionEJBRemote) locator.lookup(FrdFraudCaseStatusSessionEJBRemote.class, "FrdFraudCaseStatusSessionEJBBean");			
			List<FrdFraudCaseStatus> FraudCaseStatus = null;
			String query = "select o from FrdFraudCaseStatus o";
			
			FraudCaseStatus = sessionHome.queryByRange(query, 0, 100);
			
			for(int i=0; i<FraudCaseStatus.size();i++){
				FrdFraudCaseStatus list = FraudCaseStatus.get(i);
				map.put("data"+Util.isNull(list.getFraudCaseStatusId(), "").toString(), Util.isNull(list.getFraudCaseStatusDesc(), "").toString());							
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
