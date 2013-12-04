package com.gdn.venice.server.app.kpi.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote;
import com.gdn.venice.persistence.KpiKeyPerformanceIndicator;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Blacklist Source
 * 
 * @author Roland
 */

public class FetchKpiSetupPartySlaDataCommandComboBoxKpi implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;
		HashMap<Integer, String> mapSorted = new HashMap<Integer, String>();
		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			KpiKeyPerformanceIndicatorSessionEJBRemote sessionHome = (KpiKeyPerformanceIndicatorSessionEJBRemote) locator.lookup(KpiKeyPerformanceIndicatorSessionEJBRemote.class, "KpiKeyPerformanceIndicatorSessionEJBBean");			
			List<KpiKeyPerformanceIndicator> kpiPerformanceListSource = null;
			String query = "select o from KpiKeyPerformanceIndicator o";
			
			kpiPerformanceListSource = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<kpiPerformanceListSource.size();i++){
				KpiKeyPerformanceIndicator list = kpiPerformanceListSource.get(i);
				mapSorted.put(new Integer(list.getKpiId().toString()), list.getKpiDesc());		
			}
			map=Util.SortedHashMap(mapSorted, "asc");
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
