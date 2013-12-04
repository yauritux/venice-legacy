package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.KpiPartySlaSessionEJBRemote;
import com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote;
import com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote;
import com.gdn.venice.facade.VenAddressSessionEJBRemote;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenPartyAddressSessionEJBRemote;
import com.gdn.venice.facade.VenPartySessionEJBRemote;
import com.gdn.venice.persistence.KpiPartySla;
import com.gdn.venice.persistence.LogLogisticService;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.LogProviderAgreement;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenPartyAddress;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;


/**
 * This is RPC-Style presenter  command use for deleting Logistic Provider data
 *
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class DeleteProviderDataCommand implements RafRpcCommand {

	// The map of all the parameters passed to the command
	HashMap<String, String> providerDataMap;
	
	/**
	 * Basic constructor with parameters passed in XML string
	 * @param parameter a list of the parameters for the form in XML
	 */
	public DeleteProviderDataCommand(String parameter) {
		providerDataMap = Util.formHashMapfromXML(parameter);	
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			LogLogisticsProviderSessionEJBRemote logProviderSessionHome = (LogLogisticsProviderSessionEJBRemote) locator
					.lookup(LogLogisticsProviderSessionEJBRemote.class, "LogLogisticsProviderSessionEJBBean");
			
			LogLogisticServiceSessionEJBRemote logServiceSessionHome = (LogLogisticServiceSessionEJBRemote) locator
			.lookup(LogLogisticServiceSessionEJBRemote.class, "LogLogisticServiceSessionEJBBean");
			
			VenPartySessionEJBRemote venPartySessionHome = (VenPartySessionEJBRemote) locator
					.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");
			
			VenContactDetailSessionEJBRemote venContactDetailSessionHome = (VenContactDetailSessionEJBRemote) locator
					.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");
			
			VenAddressSessionEJBRemote venAddressSessionHome = (VenAddressSessionEJBRemote) locator
					.lookup(VenAddressSessionEJBRemote.class, "VenAddressSessionEJBBean");
			
			VenPartyAddressSessionEJBRemote venPartyAddressSessionHome = (VenPartyAddressSessionEJBRemote) locator
					.lookup(VenPartyAddressSessionEJBRemote.class, "VenPartyAddressSessionEJBBean");
			
			KpiPartySlaSessionEJBRemote kpiPartySlaHome = (KpiPartySlaSessionEJBRemote) locator
					.lookup(KpiPartySlaSessionEJBRemote.class, "KpiPartySlaSessionEJBBean");

			LogProviderAgreementSessionEJBRemote agreementSessionHome = (LogProviderAgreementSessionEJBRemote) locator
					.lookup(LogProviderAgreementSessionEJBRemote.class, "LogProviderAgreementSessionEJBBean");

			/*
			 * This is the list that will be used to delete the provider later
			 */
			ArrayList<LogLogisticsProvider> logLogisticsProviderList = new ArrayList<LogLogisticsProvider>();
			
			/*
			 * This is the list that will be used to delete the party later
			 */
			ArrayList<VenParty> venPartyList = new ArrayList<VenParty>();
			
			/*
			 * This loop processes all fo the records to be deleted
			 */
			for (int i=0;i<providerDataMap.size();i++) {
				/*
				 * This map contains a single row of data
				 */
				HashMap<String,String> providerRowMap = Util.formHashMapfromXML(providerDataMap.get("DELETEDLOGPROVIDERID" + i));
				
				/*
				 * This is the LogLogisticsProvider object we will add to the list
				 */
				LogLogisticsProvider logLogisticsProvider = new LogLogisticsProvider();
				
				/*
				 * This is the VenParty object we will add to the list
				 */
				VenParty venParty = new VenParty();
				
				if (providerRowMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)!=null) {
					logLogisticsProvider.setLogisticsProviderId(new Long(providerRowMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)));
					Long providerId = new Long(providerRowMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
					
					/*
					 * Populating Logistic Provider Agreement data (according to Logistic Provider ID)
					 */
					List<LogProviderAgreement> logProviderAgreementList = agreementSessionHome.queryByRange("select o from LogProviderAgreement o where o.logLogisticsProvider.logisticsProviderId = "+ providerId, 0, 0);

					//Check if Logistic Provider Agreement not Empty, then remove all agreements record
					if(!logProviderAgreementList.isEmpty()){
						agreementSessionHome.removeLogProviderAgreementList(logProviderAgreementList);
					}
					
					/*
					 * Populating Logistic Provider Service data (according to Logistic Provider ID)
					 */
					List<LogLogisticService> logLogisticServiceList = logServiceSessionHome.queryByRange("select o from LogLogisticService o where o.logLogisticsProvider.logisticsProviderId = "+ providerId, 0, 0);
					
					//Check if Logistic Provider Service  not Empty, then remove all services record
					if(!logLogisticServiceList.isEmpty()){
						logServiceSessionHome.removeLogLogisticServiceList(logLogisticServiceList);
					}
				}	
				

				
				if (providerRowMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID)!=null) {
					Long partyId = new Long(providerRowMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID));
	
					/*
					 * Populating KPI Party SLA data (according to Party ID)
					 */
					List<KpiPartySla> kpiPartySlaList = kpiPartySlaHome.queryByRange("select o from KpiPartySla o where o.venParty.partyId = "+ partyId, 0, 0);
					
					//Check if KPI Party SLA not Empty, then remove all records
					if(!kpiPartySlaList.isEmpty()){
						kpiPartySlaHome.removeKpiPartySlaList(kpiPartySlaList);
					}
					
					/*
					 * Populating VenPartyAddress data (according to Party ID)
					 */
					List<VenPartyAddress> venPartyAddressList = venPartyAddressSessionHome.queryByRange("select o from VenPartyAddress o where o.venParty.partyId = "+ partyId, 0, 0);
					
					for (int n=0; n < venPartyAddressList.size(); n++){
						//Check if VenPartyAddress not Empty, then remove all records
						if(!venPartyAddressList.isEmpty()){
							venPartyAddressSessionHome.removeVenPartyAddressList(venPartyAddressList);		
							VenPartyAddress venPartyAddress = venPartyAddressList.get(n);
							
							/*
							 * get AddressId value from VenPartyAddress
							 */
							Long addressId = venPartyAddress.getVenAddress().getAddressId();					
							
							/*
							 * Populating VenAddress data (according to Address ID)
							 */
							List<VenAddress>  venAddressList = venAddressSessionHome.queryByRange("select o from VenAddress o where o.addressId = "+ addressId, 0, 0);
							
							//Check if VenAddress not Empty, then remove all records				
							if (!venAddressList.isEmpty()){
								venAddressSessionHome.removeVenAddressList(venAddressList);
							}
						}
					}
					
					/*
					 * Populating VenContactDetail data (according to Party ID)
					 */
					List<VenContactDetail> venContactDetailList = venContactDetailSessionHome.queryByRange("select o from VenContactDetail o where o.venParty.partyId = "+ partyId, 0, 0);
					
					//Check if VenContactDetail not Empty, then remove all records				
					if(!venContactDetailList.isEmpty()){
						venContactDetailSessionHome.removeVenContactDetailList(venContactDetailList);
					}
					
					venParty.setPartyId(new Long(providerRowMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID)));
					logLogisticsProvider.setVenParty(venParty);
					venPartyList.add(venParty);
				}	
				logLogisticsProviderList.add(logLogisticsProvider);
			}
			logProviderSessionHome.removeLogLogisticsProviderList(logLogisticsProviderList);
			venPartySessionHome.removeVenPartyList(venPartyList);	
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
