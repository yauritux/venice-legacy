package com.gdn.venice.server.app.kpi.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote;
import com.gdn.venice.persistence.KpiMeasurementPeriod;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;
public class FetchKpiPeriodDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchKpiPeriodDataCommand(RafDsRequest request){
		this.request=request;
		
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<KpiMeasurementPeriod> KpiPeriodLocator=null;
		
		try{
			KpiPeriodLocator = new Locator<KpiMeasurementPeriod>();
			KpiMeasurementPeriodSessionEJBRemote sessionHome = (KpiMeasurementPeriodSessionEJBRemote) KpiPeriodLocator.lookup(KpiMeasurementPeriodSessionEJBRemote.class, "KpiMeasurementPeriodSessionEJBBean");
			List<KpiMeasurementPeriod> kpiPeriodList = null;			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			

			
			if (criteria == null) {
				String query = "select o from KpiMeasurementPeriod o";			
				kpiPeriodList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				KpiMeasurementPeriod bl = new KpiMeasurementPeriod();
				kpiPeriodList = sessionHome.findByKpiMeasurementPeriodLike(bl, criteria, 0, 0);
			}

			for(int i=0; i<kpiPeriodList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				KpiMeasurementPeriod list = kpiPeriodList.get(i);
				map.put(DataNameTokens.KPIMEASUREMENTPERIOD_PERIODID, Util.isNull(list.getKpiPeriodId(),"").toString());
				map.put(DataNameTokens.KPIMEASUREMENTPERIOD_DESCRIPTION,Util.isNull(list.getDescription(),"").toString());
				map.put(DataNameTokens.KPIMEASUREMENTPERIOD_FROMDATETIME,Util.isNull(list.getFromDateTime(),"").toString());
				map.put(DataNameTokens.KPIMEASUREMENTPERIOD_TODATETIME, Util.isNull(list.getToDateTime(),"").toString());				
								
				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(KpiPeriodLocator!=null){
					KpiPeriodLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
	
}