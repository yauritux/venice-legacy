package com.gdn.venice.server.app.finance.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.facade.FinPeriodSessionEJBRemote;
import com.gdn.venice.persistence.FinPeriod;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Blacklist Source
 * 
 * @author Roland
 */

public class FetchPeriodFinanceDashboardCommandComboBox implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<Integer, String> mapp = new HashMap<Integer, String>();
		HashMap<String, String> map = new HashMap<String, String>();

		try{
			locator = new Locator<Object>();			
			FinPeriodSessionEJBRemote sessionHome = (FinPeriodSessionEJBRemote) locator.lookup(FinPeriodSessionEJBRemote.class, "FinPeriodSessionEJBBean");			
			List<FinPeriod> finPeriodListSource = null;
			String query = "select o from FinPeriod o Order By o.periodId Asc";			
			finPeriodListSource = sessionHome.queryByRange(query, 0, 0);
			SimpleDateFormat ddmmyyyyDateFormat = new SimpleDateFormat("dd-MM-yyyy");
			for(int i=0; i<finPeriodListSource.size();i++){
				FinPeriod list = finPeriodListSource.get(i);
				mapp.put(new Integer(list.getPeriodId().toString()), list.getPeriodDesc() +" ( "+ddmmyyyyDateFormat.format(list.getFromDatetime())+" to "+ddmmyyyyDateFormat.format(list.getToDatetime())+" )");		
			}
			map=Util.SortedHashMap(mapp,"Asc");
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
