package com.gdn.venice.server.app.kpi.presenter.commands;

import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.util.Util;
import com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote;
import com.gdn.venice.persistence.KpiMeasurementPeriod;
import com.gdn.venice.persistence.KpiTargetBaseline;
import com.gdn.venice.server.command.RafRpcCommand;

/**
 * Fetch Command for Blacklist Source
 * 
 * @author Roland
 */

public class FetchKpiSetupPartySlaDataCommandComboBoxPeriod implements RafRpcCommand{
	
	public String execute() {
		Locator<Object> locator=null;
		HashMap<Integer, String> mapp = new HashMap<Integer, String>();
		HashMap<String, String> map = new HashMap<String, String>();
		try{
			locator = new Locator<Object>();			
			KpiMeasurementPeriodSessionEJBRemote sessionHome = (KpiMeasurementPeriodSessionEJBRemote) locator.lookup(KpiMeasurementPeriodSessionEJBRemote.class, "KpiMeasurementPeriodSessionEJBBean");			
			List<KpiMeasurementPeriod> kpiPeriodListSource = null;
			String query = "select o from KpiMeasurementPeriod o";
			
			kpiPeriodListSource = sessionHome.queryByRange(query, 0, 0);
			
			for(int i=0; i<kpiPeriodListSource.size();i++){
				KpiMeasurementPeriod list = kpiPeriodListSource.get(i);
				mapp.put(new Integer(list.getKpiPeriodId().toString()), list.getDescription());						
			}
			map=Util.SortedHashMap(mapp, "asc");
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
