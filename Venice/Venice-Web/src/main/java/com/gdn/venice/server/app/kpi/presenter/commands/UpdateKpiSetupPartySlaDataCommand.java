package com.gdn.venice.server.app.kpi.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote;
import com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBRemote;
import com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote;
import com.gdn.venice.facade.KpiPartySlaSessionEJBRemote;
import com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote;
import com.gdn.venice.facade.VenPartySessionEJBRemote;
import com.gdn.venice.persistence.KpiKeyPerformanceIndicator;
import com.gdn.venice.persistence.KpiPartyMeasurementPeriod;
import com.gdn.venice.persistence.KpiPartyMeasurementPeriodPK;
import com.gdn.venice.persistence.KpiPartyPeriodActual;
import com.gdn.venice.persistence.KpiPartySla;
import com.gdn.venice.persistence.KpiPartyTarget;
import com.gdn.venice.persistence.KpiTargetBaseline;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * 
 * @author Arifin
 */

public class UpdateKpiSetupPartySlaDataCommand implements RafRpcCommand {
HashMap<String, String> partySlaDataMap;
	public UpdateKpiSetupPartySlaDataCommand(String parameter){
		partySlaDataMap=Util.formHashMapfromXML(parameter);
	}
	
	@Override
	public String execute() {
		String Return="0";
		List<KpiPartyTarget> kpiPartyTargetList=new ArrayList<KpiPartyTarget>();

		Locator<KpiPartySla> kpiPartySlaLocator =null;
		Locator<KpiPartyTarget> kpiPartyTargetLocator = null;

		if(checkDuplicateData(partySlaDataMap)){
			if(IsMachField(partySlaDataMap)){
		try {
		kpiPartyTargetLocator = new Locator<KpiPartyTarget>();
		kpiPartySlaLocator = new Locator<KpiPartySla>();
			  
		KpiPartyTargetSessionEJBRemote kpiPartyTargetsessionHome = (KpiPartyTargetSessionEJBRemote) kpiPartyTargetLocator.lookup(KpiPartyTargetSessionEJBRemote.class, "KpiPartyTargetSessionEJBBean");
		KpiPartySlaSessionEJBRemote kpiPartySlasessionHome = (KpiPartySlaSessionEJBRemote) kpiPartySlaLocator.lookup(KpiPartySlaSessionEJBRemote.class, "KpiPartySlaSessionEJBBean");
		
		KpiPartyTarget binKpiPartyTarget = new KpiPartyTarget();		
		KpiPartySla binKpiPartySla = new KpiPartySla();
		for (int i=0;i<partySlaDataMap.size();i++) {
			Map<String,String> data = Util.formHashMapfromXML(partySlaDataMap.get("data" + i));
			Iterator<String> iter = data.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if(key.equals(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID)){
					try{
						binKpiPartyTarget = kpiPartyTargetsessionHome.queryByRange("select o from KpiPartyTarget o where o.kpiPartyTargetId="+new Long(data.get(key)), 0, 1).get(0); 
					}catch(IndexOutOfBoundsException e){
						binKpiPartyTarget.setKpiPartyTargetId(new Long(data.get(key)));
					}
					break;
				}
			}				
			
			iter = data.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLAID)) {						
					List <KpiPartySla>kpiPartySlaList = kpiPartySlasessionHome.queryByRange("select o from KpiPartySla o where o.venParty.partyId = "+data.get(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID), 0, 0);
					if(kpiPartySlaList.size()>0){				
						binKpiPartySla = kpiPartySlaList.get(0);			
						KpiPartySla kpiPartySla = new KpiPartySla();
						kpiPartySla.setPartySlaId(binKpiPartySla.getPartySlaId());
						binKpiPartyTarget.setKpiPartySla(kpiPartySla);					
					}else{
						VenParty venParty =new VenParty();
						venParty.setPartyId(new Long(data.get(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID)));
						binKpiPartySla.setVenParty(venParty);
						binKpiPartySla=kpiPartySlasessionHome.persistKpiPartySla(binKpiPartySla);
						
						kpiPartySlaList = kpiPartySlasessionHome.queryByRange("select o from KpiPartySla o where o.venParty.partyId = "+data.get(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID), 0, 0);
						binKpiPartySla = kpiPartySlaList.get(0);			
						KpiPartySla kpiPartySla = new KpiPartySla();
						kpiPartySla.setPartySlaId(binKpiPartySla.getPartySlaId());
						binKpiPartyTarget.setKpiPartySla(kpiPartySla);
					}
				}else if(key.equals(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID)){
					KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator = new KpiKeyPerformanceIndicator();
					kpiKeyPerformanceIndicator.setKpiId(new Long(data.get(key)));
					binKpiPartyTarget.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicator);
				}else if(key.equals(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID)){		
				}else if(key.equals(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_FULLORLEGALNAME)){												
				}else if(key.equals(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID)){
					KpiTargetBaseline kpiTargetBaseline = new KpiTargetBaseline();
					kpiTargetBaseline.setTargetBaselineId(new Long(data.get(key)));
					binKpiPartyTarget.setKpiTargetBaseline(kpiTargetBaseline);								
				}else if(key.equals(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE)){		
					binKpiPartyTarget.setKpiTargetValue(new Integer(data.get(key)));									
				} 
			}				
			
			kpiPartyTargetList.add(binKpiPartyTarget);						
		}	
		
		kpiPartyTargetsessionHome.mergeKpiPartyTargetList((ArrayList<KpiPartyTarget>)kpiPartyTargetList);
		} catch (Exception ex) {
			ex.printStackTrace();
			return "-1";
		} finally {
			try {
			
				if(kpiPartySlaLocator!=null){
					kpiPartySlaLocator.close();
				}
				if(kpiPartyTargetLocator!=null){
					kpiPartyTargetLocator.close();
				}
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		Return="0";
			}else{
				Return="3";
			}
		}else{
			Return="2";
		}
		return Return;
	}

	public boolean checkDuplicateData(HashMap<String, String> partySlaDataMap){
		Locator<KpiPartyTarget> kpiPartyTargetLocator=null;	
		boolean set=true;
		try{
			kpiPartyTargetLocator=new Locator<KpiPartyTarget>();
			KpiPartyTargetSessionEJBRemote kpiPartyTargetsessionHome = (KpiPartyTargetSessionEJBRemote) kpiPartyTargetLocator.lookup(KpiPartyTargetSessionEJBRemote.class, "KpiPartyTargetSessionEJBBean");
			for(int i=0;i< partySlaDataMap.size();i++){
				HashMap<String,String> listDataMap = Util.formHashMapfromXML(partySlaDataMap.get("data" + i));				
				List <KpiPartyTarget> kpiPartyTargetList = kpiPartyTargetsessionHome.queryByRange("select o from KpiPartyTarget o inner join o.kpiPartySla p where o.kpiPartySla.partySlaId=p.partySlaId and o.kpiKeyPerformanceIndicator.kpiId="+listDataMap.get(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID)+
						" and o.kpiTargetBaseline.targetBaselineId="+listDataMap.get(DataNameTokens.KPIPARTYTARGET_KPITARGETBASELINE_TARGETBASELINEID)+
						" and p.venParty.partyId="+listDataMap.get(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID), 0, 0);	
				if(kpiPartyTargetList.size()>0){
					for(int j=0;j<kpiPartyTargetList.size();j++){
					KpiPartyTarget binKpiPartyTarget =	kpiPartyTargetList.get(j);	
					if(new Long(listDataMap.get(DataNameTokens.KPIPARTYTARGET_KPIPARTYTARGETID))-new Long(binKpiPartyTarget.getKpiPartyTargetId().toString())==0){					
							if(new Long(binKpiPartyTarget.getKpiTargetValue())-new Long(listDataMap.get(DataNameTokens.KPIPARTYTARGET_KPITARGETVALUE))==0){
									set=false;
										break;
							}else
								set=true;
					}else{
						set=false;
						break;
					}
				}
			}					
		}						
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				kpiPartyTargetLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		
		return set;
	}
	public boolean IsMachField(HashMap<String, String> partySlaDataMap){
		Locator<VenParty> venPartyLocator=null;
		Locator<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorLocator=null;	

		boolean set=true;
		try{
			venPartyLocator = new Locator<VenParty>();
			kpiKeyPerformanceIndicatorLocator = new Locator<KpiKeyPerformanceIndicator>();

			VenPartySessionEJBRemote venPartysessionHome = (VenPartySessionEJBRemote) venPartyLocator.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");
			KpiKeyPerformanceIndicatorSessionEJBRemote kpiKeyPerformanceIndicatorSessionHome = (KpiKeyPerformanceIndicatorSessionEJBRemote) kpiKeyPerformanceIndicatorLocator.lookup(KpiKeyPerformanceIndicatorSessionEJBRemote.class, "KpiKeyPerformanceIndicatorSessionEJBBean");

			for(int i=0;i< partySlaDataMap.size();i++){
				HashMap<String,String> listDataMap = Util.formHashMapfromXML(partySlaDataMap.get("data" + i));
				
				List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList = kpiKeyPerformanceIndicatorSessionHome.queryByRange("select b.calculationMethod from KpiKeyPerformanceIndicator b where b.kpiId="+listDataMap.get(DataNameTokens.KPIPARTYTARGET_KPIPERFORMANCEINDICATOR_KPIID),0, 0);
				if(kpiKeyPerformanceIndicatorList.size()!=0){
					String temp="";
					if((kpiKeyPerformanceIndicatorList.get(0)+"").equals("0")){
						temp="2";
					}else temp="1";
								List <VenParty> venPartyList = venPartysessionHome.queryByRange("select o from VenParty o where o.partyId="+listDataMap.get(DataNameTokens.KPIPARTYTARGET_KPIPARTYSLA_VENPARTY_PARTYID)+" and o.venPartyType.partyTypeId="+temp, 0, 0);	
							if(venPartyList.size()==0){
									set=false;
									break;
								}else{
									set=true;
								}	
					}else{
							set=false;
							break;
					}
						
			}			
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				venPartyLocator.close();
				kpiKeyPerformanceIndicatorLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}			
		return set;
	}

}
