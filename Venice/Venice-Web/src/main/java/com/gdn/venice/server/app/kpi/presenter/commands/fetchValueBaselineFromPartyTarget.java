package com.gdn.venice.server.app.kpi.presenter.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote;
import com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote;
import com.gdn.venice.persistence.KpiKeyPerformanceIndicator;
import com.gdn.venice.persistence.KpiPartyTarget;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * 
 * @author Arifin
 */

public class fetchValueBaselineFromPartyTarget implements RafRpcCommand {
	HashMap<String, String> recordListDataMap;

	public fetchValueBaselineFromPartyTarget(String parameter) {
		recordListDataMap = Util.formHashMapfromXML(parameter);
	}

	@Override
	public String execute() {
		Locator<KpiPartyTargetSessionEJBRemote> kpiPartyTargetLocator = null;
		Locator<KpiKeyPerformanceIndicatorSessionEJBRemote> kpiKeyPerformanceIndicatortLocator = null;

		HashMap<String, String> map = new HashMap<String, String>();
		try {
				Map<String, String> listDataMap = Util.formHashMapfromXML(recordListDataMap.get("data0"));
					kpiPartyTargetLocator = new Locator<KpiPartyTargetSessionEJBRemote>();
			KpiPartyTargetSessionEJBRemote kpiPartyTargetsessionHome = (KpiPartyTargetSessionEJBRemote) kpiPartyTargetLocator.lookup(KpiPartyTargetSessionEJBRemote.class,"KpiPartyTargetSessionEJBBean");
			List<KpiPartyTarget> kpiPartyTargetList = null;
			String query = "select o from KpiPartyTarget o inner join o.kpiPartySla p where o.kpiPartySla.partySlaId=p.partySlaId  and  p.venParty.partyId="+listDataMap.get(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID)+
			" ORDER BY o.kpiKeyPerformanceIndicator.kpiId, o.kpiTargetBaseline.targetBaselineId ASC ";
			kpiPartyTargetList = kpiPartyTargetsessionHome.queryByRange(query, 0, 0);
			if(kpiPartyTargetList.size()!=0){
					for(int i=0; i<kpiPartyTargetList.size();i++){
						KpiPartyTarget list = kpiPartyTargetList.get(i);
						map.put("data"+i,"{"+list.getKpiKeyPerformanceIndicator().getKpiId()+"/"+list.getKpiKeyPerformanceIndicator().getCalculationMethod()+"/"+list.getKpiTargetBaseline().getTargetBaselineDesc()+"/"+list.getKpiTargetValue().toString()+"}");			
						
					}
			}else{
				kpiKeyPerformanceIndicatortLocator = new Locator<KpiKeyPerformanceIndicatorSessionEJBRemote>();
				KpiKeyPerformanceIndicatorSessionEJBRemote kpiKeyPerformanceIndicatorSession = (KpiKeyPerformanceIndicatorSessionEJBRemote) kpiKeyPerformanceIndicatortLocator.lookup(KpiKeyPerformanceIndicatorSessionEJBRemote.class,"KpiKeyPerformanceIndicatorSessionEJBBean");

				List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList = null;
				query = "select o from KpiKeyPerformanceIndicator o where o.kpiId="+listDataMap.get(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID);
				kpiKeyPerformanceIndicatorList = kpiKeyPerformanceIndicatorSession.queryByRange(query, 0, 0);
				for(int i=0; i<kpiKeyPerformanceIndicatorList.size();i++){
					KpiKeyPerformanceIndicator list2 = kpiKeyPerformanceIndicatorList.get(i);
					map.put("data"+i,"{null/"+list2.getCalculationMethod()+"/0/0}");								
				}
						
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return "-1";
		} finally {
			try {
				if (kpiPartyTargetLocator != null) {
					kpiPartyTargetLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Util.formXMLfromHashMap(map);
	}
}
