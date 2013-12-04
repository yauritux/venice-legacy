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
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;

public class AddPeriodKpiSetupCommand implements RafDsCommand {

	RafDsRequest request;
	
	public AddPeriodKpiSetupCommand(RafDsRequest request){
		this.request=request;
		
	}
	//KpiMeasurementPeriod.java
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		Locator<KpiMeasurementPeriod> periodkpiLocator=null;
		List<HashMap<String, String>> dataList=new ArrayList<HashMap<String,String>>();
		try{
			periodkpiLocator = new Locator<KpiMeasurementPeriod>();
			KpiMeasurementPeriodSessionEJBRemote sessionHome = (KpiMeasurementPeriodSessionEJBRemote) periodkpiLocator.lookup(KpiMeasurementPeriodSessionEJBRemote.class, "KpiMeasurementPeriodSessionEJBBean");
			dataList=request.getData();

//			DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
			
			
			boolean cekDate=false;
			KpiMeasurementPeriod binContact = new KpiMeasurementPeriod();
			 for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				
				Date fromDate =new Date(new Long(data.get(DataNameTokens.KPIMEASUREMENTPERIOD_FROMDATETIME)));
				Date toDate =new Date(new Long(data.get(DataNameTokens.KPIMEASUREMENTPERIOD_TODATETIME)));	
				if(fromDate.before(toDate)){
					cekDate=true;
				}else{
					cekDate=false;
					break;
				}
				Iterator<String> iter=data.keySet().iterator();
     			while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.KPIMEASUREMENTPERIOD_DESCRIPTION)){
						binContact.setDescription(data.get(key).toString());
					}else if(key.equals(DataNameTokens.KPIMEASUREMENTPERIOD_FROMDATETIME)){
						Long actionLogDate =new Long (data.get(key));
						binContact.setFromDateTime(new Timestamp(actionLogDate));
					} else if(key.equals(DataNameTokens.KPIMEASUREMENTPERIOD_TODATETIME)){
						Long dateTo =new Long (data.get(key));
						binContact.setToDateTime(new Timestamp(dateTo));
						
					}
					
					
				}
			}
			if(cekDate){
			binContact=sessionHome.persistKpiMeasurementPeriod(binContact);
			rafDsResponse.setStatus(0);
			}else{
				rafDsResponse.setStatus(-1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(periodkpiLocator!=null){
					periodkpiLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
