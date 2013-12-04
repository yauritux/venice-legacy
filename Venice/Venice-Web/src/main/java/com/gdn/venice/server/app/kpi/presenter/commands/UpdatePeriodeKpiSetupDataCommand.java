package com.gdn.venice.server.app.kpi.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote;
import com.gdn.venice.persistence.KpiMeasurementPeriod;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * Update Command for Blacklist Maintenance
 * 
 * @author Roland
 */

public class UpdatePeriodeKpiSetupDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public UpdatePeriodeKpiSetupDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();

		List<KpiMeasurementPeriod> periodKpiList = new ArrayList<KpiMeasurementPeriod>();		
		List<HashMap<String,String >> dataList = request.getData();		
		KpiMeasurementPeriod entityPeriodKpi = new KpiMeasurementPeriod();
		boolean cekDate=true;
		boolean cek=true;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			KpiMeasurementPeriodSessionEJBRemote sessionHome = (KpiMeasurementPeriodSessionEJBRemote) locator.lookup(KpiMeasurementPeriodSessionEJBRemote.class, "KpiMeasurementPeriodSessionEJBBean");
	
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();
			
			if(data.get(DataNameTokens.KPIMEASUREMENTPERIOD_FROMDATETIME)!=null&&data.get(DataNameTokens.KPIMEASUREMENTPERIOD_TODATETIME)!=null){
			Date fromDate =new Date(new Long(data.get(DataNameTokens.KPIMEASUREMENTPERIOD_FROMDATETIME)));
			Date toDate =new Date(new Long(data.get(DataNameTokens.KPIMEASUREMENTPERIOD_TODATETIME)));	
			if(fromDate.before(toDate)){
				cekDate=true;
				cek=false;
			}else{
				cekDate=false;
				break;
			}
			}else
				cek=true;	
			
			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.KPIMEASUREMENTPERIOD_PERIODID)) {
					try{
						entityPeriodKpi = sessionHome.queryByRange("select o from KpiMeasurementPeriod o where o.kpiPeriodId="+new Long(data.get(key)), 0, 1).get(0);
					}catch(IndexOutOfBoundsException e){
						entityPeriodKpi.setKpiPeriodId(new Long(data.get(key)));
					}
					break;
				}
			}		
						
			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.KPIMEASUREMENTPERIOD_FROMDATETIME)) {
					Long actionLogDate = new Long(data.get(key));
					entityPeriodKpi.setFromDateTime(new Timestamp(actionLogDate));
					if(cek){
						List<KpiMeasurementPeriod> kpiMeasurementPeriodList= sessionHome.queryByRange("select o from KpiMeasurementPeriod o where o.kpiPeriodId="+entityPeriodKpi.getKpiPeriodId(), 0, 0);
						KpiMeasurementPeriod listSla= kpiMeasurementPeriodList.get(0);
						if(new Date(new Long(data.get(key))).before(listSla.getToDateTime())){
							cekDate=true;
						}else{
							cekDate=false;
							break;
						}						
					}					
				} else if (key.equals(DataNameTokens.KPIMEASUREMENTPERIOD_TODATETIME)) {
					Long dateTo = new Long(data.get(key));
					entityPeriodKpi.setToDateTime(new Timestamp(dateTo));
					if(cek){
						List<KpiMeasurementPeriod> kpiMeasurementPeriodList= sessionHome.queryByRange("select o from KpiMeasurementPeriod o where o.kpiPeriodId="+entityPeriodKpi.getKpiPeriodId(), 0, 0);
						KpiMeasurementPeriod listSla= kpiMeasurementPeriodList.get(0);
						if(new Date(new Long(data.get(key))).after(listSla.getFromDateTime())){
							cekDate=true;
						}else{
							cekDate=false;
							break;
						}						
					}				
				}else if(key.equals(DataNameTokens.KPIMEASUREMENTPERIOD_DESCRIPTION)){
					entityPeriodKpi.setDescription(data.get(key).toString());
				}
			}				
			
			periodKpiList.add(entityPeriodKpi);			
		}
	
		if(cekDate){
			sessionHome.mergeKpiMeasurementPeriodList((ArrayList<KpiMeasurementPeriod>)periodKpiList);
			rafDsResponse.setStatus(0);
			}else{
				rafDsResponse.setStatus(-1);
			}
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
