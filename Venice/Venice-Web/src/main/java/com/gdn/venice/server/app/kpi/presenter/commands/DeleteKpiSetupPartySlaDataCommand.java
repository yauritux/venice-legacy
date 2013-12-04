package com.gdn.venice.server.app.kpi.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote;
import com.gdn.venice.persistence.KpiPartyTarget;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * 
 * @author Arifin
 */

public class DeleteKpiSetupPartySlaDataCommand implements RafRpcCommand {
HashMap<String, String> partySlaDataMap;
	
	public DeleteKpiSetupPartySlaDataCommand(String parameter){
		partySlaDataMap=Util.formHashMapfromXML(parameter);
	}
	
	@Override
	public String execute() {
			List<KpiPartyTarget> kpiPartyTargetList=new ArrayList<KpiPartyTarget>();
			KpiPartyTarget binKpiPartyTarget= new KpiPartyTarget();
			

		
		for (int i=0;i<partySlaDataMap.size();i++) {
			Map<String,String> listDataMap = Util.formHashMapfromXML(partySlaDataMap.get("data" + i));
			Iterator<String> iter = listDataMap.keySet().iterator();
			
			while (iter.hasNext()) {	
				String key = iter.next();
				if(key.equals(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID)){
					binKpiPartyTarget.setKpiPartyTargetId(new Long(listDataMap.get(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID)));
				}
				
			}

			kpiPartyTargetList.add(binKpiPartyTarget);		
		}			

		Locator<KpiPartyTargetSessionEJBRemote> kpiPartyTargetLocator = null;	
		
		try {
			kpiPartyTargetLocator = new Locator<KpiPartyTargetSessionEJBRemote>();
			
			KpiPartyTargetSessionEJBRemote kpiPartyTargetsessionHome = (KpiPartyTargetSessionEJBRemote) kpiPartyTargetLocator.lookup(KpiPartyTargetSessionEJBRemote.class, "KpiPartyTargetSessionEJBBean");
		
			JPQLAdvancedQueryCriteria criteriaKpiPartyTarget = new JPQLAdvancedQueryCriteria();
			criteriaKpiPartyTarget.setBooleanOperator("or");
			
			for (int i=0;i<kpiPartyTargetList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(kpiPartyTargetList.get(i).getKpiPartyTargetId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID));
				criteriaKpiPartyTarget.add(simpleCriteria);						
				
			}		
			kpiPartyTargetList = kpiPartyTargetsessionHome.findByKpiPartyTargetLike(binKpiPartyTarget, criteriaKpiPartyTarget, 0, 0);
			kpiPartyTargetsessionHome.removeKpiPartyTargetList((ArrayList<KpiPartyTarget>)kpiPartyTargetList);				
			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return "-1";
		} finally {
			try {
				if(kpiPartyTargetLocator!=null){
					kpiPartyTargetLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return "0";
	}
}
