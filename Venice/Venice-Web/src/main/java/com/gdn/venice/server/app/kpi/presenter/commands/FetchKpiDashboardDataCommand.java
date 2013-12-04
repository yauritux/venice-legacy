package com.gdn.venice.server.app.kpi.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote;
import com.gdn.venice.persistence.KpiPartyPeriodActual;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;
public class FetchKpiDashboardDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchKpiDashboardDataCommand(RafDsRequest request){
		this.request=request;
		
	}
	//KpiPartyTarget
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
	
		Locator<KpiPartyPeriodActual> kpiPartyPeriodActualLocator=null;
			
		try{
			kpiPartyPeriodActualLocator = new Locator<KpiPartyPeriodActual>();
			
			KpiPartyPeriodActualSessionEJBRemote sessionHome = (KpiPartyPeriodActualSessionEJBRemote) kpiPartyPeriodActualLocator.lookup(KpiPartyPeriodActualSessionEJBRemote.class, "KpiPartyPeriodActualSessionEJBBean");
							
			List<KpiPartyPeriodActual> kpiPartyPeriodActualList = null;	
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			
			if (criteria == null) {
				if(request.getParams().get(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID)!=""){
					if(request.getParams().get(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID)!=""){
						if(request.getParams().get(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID)!=""){
								String query = "select o from KpiPartyPeriodActual o inner join o.kpiPartyMeasurementPeriod.venParty p where o.id.partyId=p.partyId and p.venPartyType.partyTypeId="+request.getParams().get(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID)+
								" and o.id.kpiPeriodId="+request.getParams().get(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID)+
								" and o.id.partyId="+request.getParams().get(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID);			
								kpiPartyPeriodActualList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());	
						}else{						
								String query = "select o from KpiPartyPeriodActual o inner join o.kpiPartyMeasurementPeriod.venParty p where o.id.partyId=p.partyId and p.venPartyType.partyTypeId="+request.getParams().get(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID)+
								" and o.id.kpiPeriodId="+request.getParams().get(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID);			
								kpiPartyPeriodActualList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());	
							}	
					}else{
							String query = "select o from KpiPartyPeriodActual o inner join o.kpiPartyMeasurementPeriod.venParty p where o.id.partyId=p.partyId and p.venPartyType.partyTypeId="+request.getParams().get(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID);		
							kpiPartyPeriodActualList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());		
					}
				}else{
					String query = "select o from KpiPartyPeriodActual o inner join o.kpiPartyMeasurementPeriod.venParty p "; 
					kpiPartyPeriodActualList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());				
					
				}
				
			} else {
				KpiPartyPeriodActual bl = new KpiPartyPeriodActual();
				kpiPartyPeriodActualList = sessionHome.findByKpiPartyPeriodActualLike(bl, criteria, 0, 0);						
			}
			
			
			for(int i=0; i<kpiPartyPeriodActualList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				KpiPartyPeriodActual list = kpiPartyPeriodActualList.get(i);
				map.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODID, Util.isNull(list.getKpiPartyMeasurementPeriod().getId().getKpiPeriodId(),"").toString());	
				map.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPERIODDESC, Util.isNull(list.getKpiPartyMeasurementPeriod().getKpiMeasurementPeriod().getDescription(),"").toString());	
				map.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_KPIPARTYID, Util.isNull(list.getKpiPartyMeasurementPeriod().getId().getPartyId().toString(),"").toString());	
				map.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIPARTYMEASUREMENTPERIOD_KPIMEASUREMENTPERIOD_VENPARTY_FULLORLEGALNAME, Util.isNull(list.getKpiPartyMeasurementPeriod().getVenParty().getFullOrLegalName(),"").toString());
				map.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIID, Util.isNull(list.getKpiKeyPerformanceIndicator().getKpiId(),"").toString());
				map.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPIKEYPERFORMANCEINDICATOR_KPIDESC, Util.isNull(list.getKpiKeyPerformanceIndicator().getKpiDesc(),"").toString());	
				map.put(DataNameTokens.KPIPARTYPERIODACTUAL_KPICALCULATEDVALUE, Util.isNull(list.getKpiCalculatedValue(),"").toString());		
											
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
				if(kpiPartyPeriodActualLocator!=null){
					kpiPartyPeriodActualLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
	
}