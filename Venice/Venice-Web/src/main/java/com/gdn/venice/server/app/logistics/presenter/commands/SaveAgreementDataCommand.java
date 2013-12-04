package com.gdn.venice.server.app.logistics.presenter.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ejb.EJBException;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.KpiPartySlaSessionEJBRemote;
import com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote;
import com.gdn.venice.facade.LogProviderAgreementSessionEJBRemote;
import com.gdn.venice.persistence.KpiKeyPerformanceIndicator;
import com.gdn.venice.persistence.KpiPartySla;
import com.gdn.venice.persistence.KpiPartyTarget;
import com.gdn.venice.persistence.KpiTargetBaseline;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.LogProviderAgreement;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.GwtDateFormatter;
import com.gdn.venice.server.util.Util;
import com.gdn.venice.util.VeniceConstants;

/**
 * This is RPC-style presenter command use for Saving Logistics Provider Agreement detail on Agreement  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class SaveAgreementDataCommand implements RafRpcCommand {

	// The map of all the parameters passed to the command
	HashMap<String, String> providerAgreementDataMap;
	
	/**
	 * Basic constructor with parameters passed in XML string
	 * @param parameter a list of the parameters for the form in XML
	 */
	public SaveAgreementDataCommand(String parameter) {
		providerAgreementDataMap = Util.formHashMapfromXML(parameter);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		
		Locator<Object> locator=null;

		try {
			locator = new Locator<Object>();
			LogProviderAgreementSessionEJBRemote sessionHome = (LogProviderAgreementSessionEJBRemote) locator
			.lookup(LogProviderAgreementSessionEJBRemote.class,
					"LogProviderAgreementSessionEJBBean");

			/*
			 * This is the map with the whole table (all rows)
			 */
			HashMap<String,String> agreementDetailMap = Util.formHashMapfromXML(providerAgreementDataMap.get("AGREEMENTDETAIL"));
			
			/*
			 * This is the list that will be used to save the agreement later
			 */
			ArrayList<LogProviderAgreement> agreementDataList = new ArrayList<LogProviderAgreement>();
			
			LogProviderAgreement logProviderAgreement = new LogProviderAgreement();
			/*
			 * This loop processes the rows in the request from the ListGrid
			 */
			for (int i=0;i<agreementDetailMap.size();i++) {

				HashMap<String,String> agreementRowMap = Util.formHashMapfromXML(agreementDetailMap.get("AGREEMENTDETAIL" + i));
			
		
				// Check If Provider Agreement ID not Null
				if ( agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID)!=null) {
					Long providerAgreementId = new Long(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PROVIDERAGREEMENTID));
					try
					{
						logProviderAgreement = sessionHome.queryByRange("select o from LogProviderAgreement o where o.providerAgreementId="+providerAgreementId, 0, 1).get(0);
					}catch(IndexOutOfBoundsException e){
						logProviderAgreement.setProviderAgreementId(providerAgreementId);
					}					
					
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)!=null) {
						Long logisticsProviderId = new Long(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
						LogLogisticsProvider logLogisticsProvider = new LogLogisticsProvider();
						logLogisticsProvider.setLogisticsProviderId(logisticsProviderId);
						logProviderAgreement.setLogLogisticsProvider(logLogisticsProvider);
					}
					
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDESC)!=null) {
						String agreementDesc = agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDESC);
						logProviderAgreement.setAgreementDesc(agreementDesc);
					}
					
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE)!=null) {
						Date agreementDate =  GwtDateFormatter.parse(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE));
						logProviderAgreement.setAgreementDate(agreementDate);
					}
					
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE)!=null) {
						Date expiryDate =  GwtDateFormatter.parse(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE));
						logProviderAgreement.setExpiryDate(expiryDate);
					}

					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT)!=null) {
						Integer pickupTimeCommitment =  new Integer(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT));
						logProviderAgreement.setPickupTimeCommitment(pickupTimeCommitment);
					}

					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT)!=null) {
						Integer deliveryTimeCommitment =  new Integer(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT));
						logProviderAgreement.setDeliveryTimeCommitment(deliveryTimeCommitment);
					}
					
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT)!=null) {
						BigDecimal discountLevelPct = new BigDecimal(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT));
						logProviderAgreement.setDiscountLevelPct(discountLevelPct);
					}
					
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE)!=null) {
						BigDecimal ppnPercentage = new BigDecimal(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE));
						logProviderAgreement.setPpnPercentage(ppnPercentage);
					}
					
					//Create the default KPIPartySla and associated default KPI targets for the provider
					logProviderAgreement.setKpiPartySla(this.createDefaultProviderKPIPartyTargetRecords(locator, logProviderAgreement.getLogLogisticsProvider().getLogisticsProviderId()));
					
					agreementDataList.add(logProviderAgreement);
					

					Date fromDate =logProviderAgreement.getAgreementDate();
					Date toDate =logProviderAgreement.getExpiryDate();	
					if(fromDate.before(toDate)){
						sessionHome.mergeLogProviderAgreement(logProviderAgreement);
					}else{
						return "-1" ;
					}
					
				}else{

					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)!=null) {
						Long logisticsProviderId = new Long(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
						LogLogisticsProvider logLogisticsProvider = new LogLogisticsProvider();
						logLogisticsProvider.setLogisticsProviderId(logisticsProviderId);
						logProviderAgreement.setLogLogisticsProvider(logLogisticsProvider);
					}
					
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDESC)!=null) {
						String agreementDesc = agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDESC);
						logProviderAgreement.setAgreementDesc(agreementDesc);
					}
					
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT)!=null) {
						Integer pickupTimeCommitment =  new Integer(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PICKUPTIMECOMMITMENT));
						logProviderAgreement.setPickupTimeCommitment(pickupTimeCommitment);
					}

					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT)!=null) {
						Integer deliveryTimeCommitment =  new Integer(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_DELIVERYTIMECOMMITMENT));
						logProviderAgreement.setDeliveryTimeCommitment(deliveryTimeCommitment);
					}

					
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE)!=null) {
						Date agreementDate =  GwtDateFormatter.parse(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_AGREEMENTDATE));
						logProviderAgreement.setAgreementDate(agreementDate);
					}
				
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE)!=null) {

						Date expiryDate = GwtDateFormatter.parse(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_EXPIRYDATE));
						logProviderAgreement.setExpiryDate(expiryDate);
					}
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT)!=null) {
						BigDecimal discountLevelPct = new BigDecimal(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_DISCOUNTLEVELPCT));
						logProviderAgreement.setDiscountLevelPct(discountLevelPct);
					}
					
					if (agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE)!=null) {
						BigDecimal ppnPercentage = new BigDecimal(agreementRowMap.get(DataNameTokens.LOGPROVIDERAGREEMENT_PPNPERCENTAGE));
						logProviderAgreement.setPpnPercentage(ppnPercentage);
					}
					
					//Create the default KPIPartySla and associated default KPI targets for the provider
					logProviderAgreement.setKpiPartySla(this.createDefaultProviderKPIPartyTargetRecords(locator, logProviderAgreement.getLogLogisticsProvider().getLogisticsProviderId()));
					
					agreementDataList.add(logProviderAgreement);
					
					Date fromDate =logProviderAgreement.getAgreementDate();
					Date toDate =logProviderAgreement.getExpiryDate();	
					if(fromDate.before(toDate)){
						sessionHome.persistLogProviderAgreement(logProviderAgreement);
					}else{
						return "-2" ;
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
	
	/**
	 * Creates a default party SLA and targets for the logistics provider
	 * @param locator the locator for EJBs
	 * @param venMerchant the merchant in question
	 * @return the KpiPartySla record for the logistics provider
	 */
	private KpiPartySla createDefaultProviderKPIPartyTargetRecords(Locator<Object> locator, Long logisticsProviderId){
		KpiPartySla kpiPartySla = null;
		try{
			KpiPartyTargetSessionEJBRemote kpiPartyTargetHome = (KpiPartyTargetSessionEJBRemote) locator
			.lookup(KpiPartyTargetSessionEJBRemote.class,
					"KpiPartyTargetSessionEJBBean");
			
			KpiPartySlaSessionEJBRemote kpiPartySlaHome = (KpiPartySlaSessionEJBRemote) locator
			.lookup(KpiPartySlaSessionEJBRemote.class,
					"KpiPartySlaSessionEJBBean");

			LogLogisticsProviderSessionEJBRemote logLogisticsProviderHome = (LogLogisticsProviderSessionEJBRemote) locator
			.lookup(LogLogisticsProviderSessionEJBRemote.class,
					"LogLogisticsProviderSessionEJBBean");
			
			List<LogLogisticsProvider> logLogisticsProviderList = logLogisticsProviderHome.queryByRange("select o from LogLogisticsProvider o where o.logisticsProviderId = " + logisticsProviderId, 0, 0);
			if(logLogisticsProviderList.isEmpty()){
				throw new EJBException("Logistics provider id:" + logisticsProviderId + " is invalid. Logistics provider not found!");
			}
			
			LogLogisticsProvider logLogisticsProvider = logLogisticsProviderList.get(0);
			
			Long partyId = logLogisticsProvider.getVenParty().getPartyId();
			
			/*
			 * Here we need to create a new party SLA if none exists
			 */
			List<KpiPartySla> kpiPartySlaList = kpiPartySlaHome.queryByRange("select o from KpiPartySla o where o.venParty.partyId = " + partyId, 0, 0);
			
			if(kpiPartySlaList.isEmpty()){
				kpiPartySla = new KpiPartySla();
				kpiPartySla.setVenParty(logLogisticsProvider.getVenParty());
				kpiPartySla = kpiPartySlaHome.persistKpiPartySla(kpiPartySla);
			}else{
				kpiPartySla = kpiPartySlaList.get(0);
			}
			
			List<KpiPartyTarget> kpiPartyTargetList = kpiPartyTargetHome.queryByRange("select o from KpiPartyTarget o where o.kpiPartySla.partySlaId = " + kpiPartySla.getPartySlaId(), 0, 0);
			
			/*
			 * If there are no party targets then create the defaults
			 * for the logistics provider targets of:
			 * 	o Pickup Performance
			 *  o Delivery Performance
			 *  o Invoice Accuracy
			 */
			if(kpiPartyTargetList.isEmpty()){
				
				KpiTargetBaseline kpiTargetBaselineWorst = new KpiTargetBaseline();
				kpiTargetBaselineWorst.setTargetBaselineId(VeniceConstants.KPI_TARGET_BASELINE_WORST);

				KpiTargetBaseline kpiTargetBaselineAverage = new KpiTargetBaseline();
				kpiTargetBaselineAverage.setTargetBaselineId(VeniceConstants.KPI_TARGET_BASELINE_AVERAGE);

				KpiTargetBaseline kpiTargetBaselineBest = new KpiTargetBaseline();
				kpiTargetBaselineBest.setTargetBaselineId(VeniceConstants.KPI_TARGET_BASELINE_BEST);
				
				KpiKeyPerformanceIndicator kpiKeyPerformanceIndicatorPickupPerformance = new KpiKeyPerformanceIndicator();
				kpiKeyPerformanceIndicatorPickupPerformance.setKpiId(VeniceConstants.KPI_LOGISTICS_PICKUP_PERFORMANCE);
				
				KpiKeyPerformanceIndicator kpiKeyPerformanceIndicatorDeliveryPerformance = new KpiKeyPerformanceIndicator();
				kpiKeyPerformanceIndicatorDeliveryPerformance.setKpiId(VeniceConstants.KPI_LOGISTICS_DELIVERY_PERFORMANCE);

				KpiKeyPerformanceIndicator kpiKeyPerformanceIndicatorInvoiceAccuracy = new KpiKeyPerformanceIndicator();
				kpiKeyPerformanceIndicatorInvoiceAccuracy.setKpiId(VeniceConstants.KPI_LOGISTICS_INVOICE_ACCURACY);

				List<KpiPartyTarget> newKpiPartyTargetList = new ArrayList<KpiPartyTarget>();
				
				/*
				 * For pickup performance 
				 */
				KpiPartyTarget worstTargetPickupPerformanceDefault = new KpiPartyTarget();
				worstTargetPickupPerformanceDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorPickupPerformance);
				worstTargetPickupPerformanceDefault.setKpiPartySla(kpiPartySla);
				worstTargetPickupPerformanceDefault.setKpiTargetBaseline(kpiTargetBaselineWorst);
				worstTargetPickupPerformanceDefault.setKpiTargetValue(60);
				newKpiPartyTargetList.add(worstTargetPickupPerformanceDefault);
				
				KpiPartyTarget avgTargetPickupPerformanceDefault = new KpiPartyTarget();
				avgTargetPickupPerformanceDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorPickupPerformance);
				avgTargetPickupPerformanceDefault.setKpiPartySla(kpiPartySla);
				avgTargetPickupPerformanceDefault.setKpiTargetBaseline(kpiTargetBaselineAverage);
				avgTargetPickupPerformanceDefault.setKpiTargetValue(80);
				newKpiPartyTargetList.add(avgTargetPickupPerformanceDefault);
				
				KpiPartyTarget bestTargetPickupPerformanceDefault = new KpiPartyTarget();
				bestTargetPickupPerformanceDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorPickupPerformance);
				bestTargetPickupPerformanceDefault.setKpiPartySla(kpiPartySla);
				bestTargetPickupPerformanceDefault.setKpiTargetBaseline(kpiTargetBaselineBest);
				bestTargetPickupPerformanceDefault.setKpiTargetValue(100);
				newKpiPartyTargetList.add(bestTargetPickupPerformanceDefault);
				
				/*
				 * For delivery performance
				 */
				KpiPartyTarget worstTargetDeliveryPerformanceDefault = new KpiPartyTarget();
				worstTargetDeliveryPerformanceDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorDeliveryPerformance);
				worstTargetDeliveryPerformanceDefault.setKpiPartySla(kpiPartySla);
				worstTargetDeliveryPerformanceDefault.setKpiTargetBaseline(kpiTargetBaselineWorst);
				worstTargetDeliveryPerformanceDefault.setKpiTargetValue(60);
				newKpiPartyTargetList.add(worstTargetDeliveryPerformanceDefault);

				KpiPartyTarget avgTargetDeliveryPerformanceDefault = new KpiPartyTarget();
				avgTargetDeliveryPerformanceDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorDeliveryPerformance);
				avgTargetDeliveryPerformanceDefault.setKpiPartySla(kpiPartySla);
				avgTargetDeliveryPerformanceDefault.setKpiTargetBaseline(kpiTargetBaselineAverage);
				avgTargetDeliveryPerformanceDefault.setKpiTargetValue(80);
				newKpiPartyTargetList.add(avgTargetDeliveryPerformanceDefault);

				KpiPartyTarget bestTargetDeliveryPerformanceDefault = new KpiPartyTarget();
				bestTargetDeliveryPerformanceDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorDeliveryPerformance);
				bestTargetDeliveryPerformanceDefault.setKpiPartySla(kpiPartySla);
				bestTargetDeliveryPerformanceDefault.setKpiTargetBaseline(kpiTargetBaselineBest);
				bestTargetDeliveryPerformanceDefault.setKpiTargetValue(100);
				newKpiPartyTargetList.add(bestTargetDeliveryPerformanceDefault);
				
				
				/*
				 * For invoice accuracy
				 */
				KpiPartyTarget worstTargetInvoiceAccuracyDefault = new KpiPartyTarget();
				worstTargetInvoiceAccuracyDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorInvoiceAccuracy);
				worstTargetInvoiceAccuracyDefault.setKpiPartySla(kpiPartySla);
				worstTargetInvoiceAccuracyDefault.setKpiTargetBaseline(kpiTargetBaselineWorst);
				worstTargetInvoiceAccuracyDefault.setKpiTargetValue(60);
				newKpiPartyTargetList.add(worstTargetInvoiceAccuracyDefault);

				KpiPartyTarget avgTargetInvoiceAccuracyDefault = new KpiPartyTarget();
				avgTargetInvoiceAccuracyDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorInvoiceAccuracy);
				avgTargetInvoiceAccuracyDefault.setKpiPartySla(kpiPartySla);
				avgTargetInvoiceAccuracyDefault.setKpiTargetBaseline(kpiTargetBaselineAverage);
				avgTargetInvoiceAccuracyDefault.setKpiTargetValue(80);
				newKpiPartyTargetList.add(avgTargetInvoiceAccuracyDefault);

				KpiPartyTarget bestTargetInvoiceAccuracyDefault = new KpiPartyTarget();
				bestTargetInvoiceAccuracyDefault.setKpiKeyPerformanceIndicator(kpiKeyPerformanceIndicatorInvoiceAccuracy);
				bestTargetInvoiceAccuracyDefault.setKpiPartySla(kpiPartySla);
				bestTargetInvoiceAccuracyDefault.setKpiTargetBaseline(kpiTargetBaselineBest);
				bestTargetInvoiceAccuracyDefault.setKpiTargetValue(100);
				newKpiPartyTargetList.add(bestTargetInvoiceAccuracyDefault);


				/*
				 * Persist the new values
				 */
				newKpiPartyTargetList = kpiPartyTargetHome.persistKpiPartyTargetList(newKpiPartyTargetList);
				
			}
		}catch (Exception e) {
			String errMsg = "An exception occured when creating default logistics provider KPI party target records:";
			e.printStackTrace();
			throw new EJBException(errMsg + e.getMessage());
		}
		return kpiPartySla;	
	}

}
