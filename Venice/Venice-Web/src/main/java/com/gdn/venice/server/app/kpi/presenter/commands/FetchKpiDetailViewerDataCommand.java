package com.gdn.venice.server.app.kpi.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote;
import com.gdn.venice.facade.KpiPartyPeriodTransactionSessionEJBRemote;
import com.gdn.venice.facade.VenPartySessionEJBRemote;
import com.gdn.venice.persistence.KpiMeasurementPeriod;
import com.gdn.venice.persistence.KpiPartyPeriodTransaction;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;
public class FetchKpiDetailViewerDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	public FetchKpiDetailViewerDataCommand(RafDsRequest request){
		this.request=request;
		
	}
	//KpiPartyTarget
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
	
		Locator<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionLocator=null;
			
		try{
			kpiPartyPeriodTransactionLocator = new Locator<KpiPartyPeriodTransaction>();
			
			KpiPartyPeriodTransactionSessionEJBRemote sessionHome = (KpiPartyPeriodTransactionSessionEJBRemote) kpiPartyPeriodTransactionLocator.lookup(KpiPartyPeriodTransactionSessionEJBRemote.class, "KpiPartyPeriodTransactionSessionEJBBean");
							
			List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactionList = null;	
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			
			if (criteria == null) {
				String query = "select o from KpiPartyPeriodTransaction o";			
				kpiPartyPeriodTransactionList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				
			} else {
				KpiPartyPeriodTransaction bl = new KpiPartyPeriodTransaction();
				kpiPartyPeriodTransactionList = sessionHome.findByKpiPartyPeriodTransactionLike(bl, criteria, 0, 0);						
			}
			for(int i=0; i<kpiPartyPeriodTransactionList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				KpiPartyPeriodTransaction list = kpiPartyPeriodTransactionList.get(i);
				map.put(DataNameTokens.KPIPARTYPERIODTRANSACTION_ID, list.getKpiPartyPeriodTransactionId().toString());
				map.put(DataNameTokens.KPIPARTYPERIODTRANSACTION_KPIPARTYMEASUREMENTPERIOD_PARTYID, Util.isNull(list.getKpiPartyMeasurementPeriod().getId().getPartyId(),"").toString());	
				map.put(DataNameTokens.KPIPARTYPERIODTRANSACTION_KPIPARTYMEASUREMENTPERIOD_KPIPERIODID, Util.isNull(list.getKpiPartyMeasurementPeriod().getId().getKpiPeriodId(),"").toString());
				map.put(DataNameTokens.KPIPARTYPERIODTRANSACTION_TIMESTAMP, Util.isNull(list.getTransactionTimestamp(),"").toString());
				map.put(DataNameTokens.KPIPARTYPERIODTRANSACTION_KPIKEYPERMORMANCEINDICATOR_KPIID, Util.isNull(list.getKpiKeyPerformanceIndicator().getKpiId(),"").toString());	
				map.put(DataNameTokens.KPIPARTYPERIODTRANSACTION_TRANSACTIONVALUE, Util.isNull(list.getKpiTransactionValue(),"").toString());		
				map.put(DataNameTokens.KPIPARTYPERIODTRANSACTION_TRANSACTIONREASON, Util.isNull(list.getKpiTransactionReason(),"").toString());		
								
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
				if(kpiPartyPeriodTransactionLocator!=null){
					kpiPartyPeriodTransactionLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
	
}