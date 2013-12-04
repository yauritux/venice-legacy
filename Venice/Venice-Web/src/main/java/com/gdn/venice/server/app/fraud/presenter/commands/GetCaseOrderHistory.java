package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * Fetch case fraud Id
 * 
 * @author Arifin
 */

public class GetCaseOrderHistory implements RafRpcCommand{
	String orderId;
	public GetCaseOrderHistory(String parameter){
		this.orderId=parameter;
	}
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			FrdFraudSuspicionCaseSessionEJBRemote sessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");			
			List<FrdFraudSuspicionCase> status = null;
			/*
			 * get caseid/id risk point untuk order tersebut
			 */
			String query = "select o from FrdFraudSuspicionCase o where o.venOrder.wcsOrderId = '"+orderId+"'";
			status = sessionHome.queryByRange(query, 0, 0);
			if(status!=null && !status.isEmpty()){
				for(int i=0; i<status.size();i++){
					FrdFraudSuspicionCase list = status.get(i);
					map.put("data"+i, Util.isNull(list.getFraudSuspicionCaseId(),"").toString());
				}
			}else{
				map.put("data"+0,"null");
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
