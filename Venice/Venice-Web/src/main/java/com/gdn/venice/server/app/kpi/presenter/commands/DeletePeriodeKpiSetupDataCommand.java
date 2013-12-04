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
import com.gdn.venice.facade.FrdEntityBlacklistSessionEJBRemote;
import com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote;
import com.gdn.venice.persistence.KpiMeasurementPeriod;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Delete Command for Blacklist Maintenance
 * 
 * @author Roland
 */

public class DeletePeriodeKpiSetupDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeletePeriodeKpiSetupDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		//KpiMeasurementPeriod
		List<KpiMeasurementPeriod> periodKpiList = new ArrayList<KpiMeasurementPeriod>();		
		List<HashMap<String,String >> dataList = request.getData();		
		KpiMeasurementPeriod entityPeriodkpi = new KpiMeasurementPeriod();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.KPIMEASUREMENTPERIOD_PERIODID)) {
					entityPeriodkpi.setKpiPeriodId(new Long(data.get(DataNameTokens.KPIMEASUREMENTPERIOD_PERIODID)));
				} 
			}						
			periodKpiList.add(entityPeriodkpi);			
		}
				
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			KpiMeasurementPeriodSessionEJBRemote sessionHome = (KpiMeasurementPeriodSessionEJBRemote) locator.lookup(KpiMeasurementPeriodSessionEJBRemote.class, "KpiMeasurementPeriodSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<periodKpiList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.KPIMEASUREMENTPERIOD_PERIODID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(periodKpiList.get(i).getKpiPeriodId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.KPIMEASUREMENTPERIOD_PERIODID));
				criteria.add(simpleCriteria);
			}
			periodKpiList = sessionHome.findByKpiMeasurementPeriodLike(entityPeriodkpi, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeKpiMeasurementPeriodList((ArrayList<KpiMeasurementPeriod>)periodKpiList);			
									
			rafDsResponse.setStatus(0);
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
