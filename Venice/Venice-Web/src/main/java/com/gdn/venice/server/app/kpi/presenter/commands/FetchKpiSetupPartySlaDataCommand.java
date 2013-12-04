package com.gdn.venice.server.app.kpi.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote;
import com.gdn.venice.facade.KpiPartySlaSessionEJBRemote;
import com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote;
import com.gdn.venice.persistence.KpiPartyPeriodActual;
import com.gdn.venice.persistence.KpiPartySla;
import com.gdn.venice.persistence.KpiPartyTarget;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
public class FetchKpiSetupPartySlaDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchKpiSetupPartySlaDataCommand(RafDsRequest request){
		this.request=request;
		
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<KpiPartyTarget> kpiPartySlaTargetLocator=null;
		Locator<KpiPartySla> kpiPartySlaLocator=null;
	
		try{
			kpiPartySlaTargetLocator = new Locator<KpiPartyTarget>();
			kpiPartySlaLocator = new Locator<KpiPartySla>();
			
		KpiPartyTargetSessionEJBRemote sessionHome = (KpiPartyTargetSessionEJBRemote) kpiPartySlaTargetLocator.lookup(KpiPartyTargetSessionEJBRemote.class, "KpiPartyTargetSessionEJBBean");
		KpiPartySlaSessionEJBRemote sessionHomeSla = (KpiPartySlaSessionEJBRemote) kpiPartySlaLocator.lookup(KpiPartySlaSessionEJBRemote.class, "KpiPartySlaSessionEJBBean");
		
			List<KpiPartyTarget> kpiPartyTargetList = null;			
			List<KpiPartySla> kpiPartySlaList= null;
		
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();		

			if (criteria == null) {
				if(request.getParams().get(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID)!=""){
					kpiPartySlaList= sessionHomeSla.queryByRange("select o from KpiPartySla o where o.venParty.partyId="+request.getParams().get(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID), 0, 0);
					if(!kpiPartySlaList.isEmpty()){
						KpiPartySla listSla= kpiPartySlaList.get(0);
						String query2 = "select o from KpiPartyTarget o where o.kpiPartySla.partySlaId="+listSla.getPartySlaId();			
						kpiPartyTargetList = sessionHome.queryByRange(query2, request.getStartRow(), request.getEndRow()-request.getStartRow());
					}					
				}else if(request.getParams().get(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID)!=""){
					kpiPartyTargetList = sessionHome.queryByRange("select o from KpiPartyTarget o where o.kpiPartySla.partySlaId in (select a.partySlaId from KpiPartySla a where a.venParty.partyId in (select b.partyId from VenParty b where b.venPartyType.partyTypeId="+request.getParams().get(DataNameTokens.VENPARTY_VENPARTYTYPE_VENPARTYTYPEID)+"))", request.getStartRow(), request.getEndRow()-request.getStartRow());
				}else{
					String query = "select o from KpiPartyTarget o";			
					kpiPartyTargetList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
					}
			} else {
				KpiPartyTarget bl = new KpiPartyTarget();
				kpiPartyTargetList = sessionHome.findByKpiPartyTargetLike(bl, criteria, 0, 0);
		
			}
			if(kpiPartyTargetList!=null){
				for(int i=0; i<kpiPartyTargetList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				KpiPartyTarget list = kpiPartyTargetList.get(i);
				map.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID, list.getKpiPartyTargetId().toString());
				map.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLAID, list.getKpiPartySla().getPartySlaId().toString());
				map.put(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID, list.getKpiKeyPerformanceIndicator().getKpiId().toString());
				map.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID, list.getKpiPartySla().getVenParty().getPartyId().toString());
				map.put(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME, list.getKpiPartySla().getVenParty().getFullOrLegalName());
				map.put(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID, list.getKpiTargetBaseline().getTargetBaselineId().toString());
				if(list.getKpiKeyPerformanceIndicator().getCalculationMethod().toString().equals("0")){
					map.put(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE, list.getKpiTargetValue().toString()+" %");		
				}else{
					map.put(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE, list.getKpiTargetValue().toString());		

				}
								
				dataList.add(map);	
				}
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
				if(kpiPartySlaTargetLocator!=null){
					kpiPartySlaTargetLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
	
}