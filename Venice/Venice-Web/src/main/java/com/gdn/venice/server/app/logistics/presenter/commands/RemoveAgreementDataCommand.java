package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJBException;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.KpiPartySlaSessionEJBRemote;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote;
import com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote;
import com.gdn.venice.persistence.KpiPartySla;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.LogProviderAgreement;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * This is RPC-Style presenter command use for removing Logistic Provider
 * Agreement data on Agreement Tab
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian
 * Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class RemoveAgreementDataCommand implements RafRpcCommand {

	// The map of all the parameters passed to the command
	HashMap<String, String> providerAgreementDataMap;

	/**
	 * Basic constructor with parameters passed in XML string
	 * 
	 * @param parameter
	 *            a list of the parameters for the form in XML
	 */
	public RemoveAgreementDataCommand(String parameter) {
		providerAgreementDataMap = Util.formHashMapfromXML(parameter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		Locator<Object> locator = null;

		try {
			locator = new Locator<Object>();

			LogProviderAgreementSessionEJBRemote sessionHome = (LogProviderAgreementSessionEJBRemote) locator.lookup(LogProviderAgreementSessionEJBRemote.class,"LogProviderAgreementSessionEJBBean");

			KpiPartySlaSessionEJBRemote kpiPartySlaHome = (KpiPartySlaSessionEJBRemote) locator.lookup(KpiPartySlaSessionEJBRemote.class,"KpiPartySlaSessionEJBBean");

			LogLogisticsProviderSessionEJBRemote logLogisticsProviderHome = (LogLogisticsProviderSessionEJBRemote) locator.lookup(LogLogisticsProviderSessionEJBRemote.class,"LogLogisticsProviderSessionEJBBean");
			
			/*
			 * This is the list that will be used to remove the agreement later
			 */
			ArrayList<LogProviderAgreement> logProviderAgreementList = new ArrayList<LogProviderAgreement>();

			List<KpiPartySla> kpiPartySlaList = null;
			/*
			 * This loop processes all fo the records to be deleted
			 */
			for (int i = 0; i < providerAgreementDataMap.size(); i++) {
				/*
				 * This map contains a single row of data
				 */
				HashMap<String, String> agreementRowMap = Util.formHashMapfromXML(providerAgreementDataMap.get("REMOVEDAGREEMENT" + i));

				/*
				 * This is the LogProviderAgreement object we will add to the
				 * list
				 */
				LogProviderAgreement logProviderAgreement = new LogProviderAgreement();

				if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID) != null) {
					logProviderAgreement.setProviderAgreementId(new Long(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID)));
				}

				logProviderAgreementList.add(logProviderAgreement);
				sessionHome.removeLogProviderAgreementList(logProviderAgreementList);
				
			}
			
			List<LogProviderAgreement> providerAgreementList=null;
			for (int i = 0; i < providerAgreementDataMap.size(); i++) {
			
				/*
				 * This map contains a single row of data
				 */
				HashMap<String, String> agreementRowMap = Util.formHashMapfromXML(providerAgreementDataMap.get("REMOVEDAGREEMENT" + i));
				
				Long logisticsProviderId = new Long(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));

				List<LogLogisticsProvider> logLogisticsProviderList = logLogisticsProviderHome.queryByRange("select o from LogLogisticsProvider o where o.logisticsProviderId = "+ logisticsProviderId, 0, 0);
					if (logLogisticsProviderList.isEmpty()) {
						throw new EJBException("Logistics provider id:"+ logisticsProviderId+ " is invalid. Logistics provider not found!");
					}

				LogLogisticsProvider logLogisticsProvider = logLogisticsProviderList.get(0);
				Long partyId = logLogisticsProvider.getVenParty().getPartyId();
				kpiPartySlaList = kpiPartySlaHome.queryByRange("select o from KpiPartySla o where o.venParty.partyId = "+ partyId, 0, 0);
				
				
				if(!kpiPartySlaList.isEmpty()){
					KpiPartySla kpiParySla = kpiPartySlaList.get(0);
					Long kpiPartySla = kpiParySla.getPartySlaId();
					providerAgreementList = sessionHome.queryByRange("select o from LogProviderAgreement o where o.kpiPartySla.partySlaId = "+ kpiPartySla, 0, 0);		
					
					if (!providerAgreementList.isEmpty()){
						
						if(providerAgreementList.size() != providerAgreementDataMap.size() ){
							kpiPartySlaList.get(0);				
						}
					}else if(!kpiPartySlaList.isEmpty()) {
						kpiPartySlaHome.removeKpiPartySlaList(kpiPartySlaList);	
					}else{
						
					}
				}
				
				
			}

		} catch (Exception ex) {
			String errorMsg = Util.extractMessageFromEJBExceptionText(ex.getMessage());
			ex.printStackTrace();
			return "-1" + ":" + errorMsg;
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "0";
	}

}
