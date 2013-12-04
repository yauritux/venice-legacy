package com.gdn.venice.server.app.kpi.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.facade.KpiTargetBaselineSessionEJBRemote;
import com.gdn.venice.persistence.KpiTargetBaseline;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Blacklist Source
 * 
 * @author Roland
 */

public class FetchKpiSetupPartySlaDataCommandComboBoxBaseline implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;

		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			KpiTargetBaselineSessionEJBRemote sessionHome = (KpiTargetBaselineSessionEJBRemote) locator.lookup(KpiTargetBaselineSessionEJBRemote.class, "KpiTargetBaselineSessionEJBBean");			
			List<KpiTargetBaseline> kpiBaselineListSource = null;
			String query = "select o from KpiTargetBaseline o";
			
			kpiBaselineListSource = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<kpiBaselineListSource.size();i++){
				KpiTargetBaseline list = kpiBaselineListSource.get(i);
				map.put("data"+list.getTargetBaselineId().toString(), list.getTargetBaselineDesc());		
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
