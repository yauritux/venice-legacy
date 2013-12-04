package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogLogisticsProviderSessionEJBRemote;
import com.gdn.venice.facade.LogReportTemplateSessionEJBRemote;
import com.gdn.venice.facade.VenPartySessionEJBRemote;
import com.gdn.venice.facade.VenPartyTypeSessionEJBRemote;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.LogReportTemplate;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenPartyType;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * This is RPC-style presenter command use for Saving Logistics Provider  data.
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class SaveProviderDataCommand implements RafRpcCommand {

	// The map of all the parameters passed to the command
	HashMap<String, String> providerDataMap;
	
	/**
	 * Basic constructor with parameters passed in XML string
	 * @param parameter a list of the parameters for the form in XML
	 */
	public SaveProviderDataCommand(String parameter) {
		providerDataMap = Util.formHashMapfromXML(parameter);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		Locator<Object> locator = null;


		try{
			locator = new Locator<Object>();
			
			LogLogisticsProviderSessionEJBRemote logProviderSessionHome = (LogLogisticsProviderSessionEJBRemote) locator
			.lookup(LogLogisticsProviderSessionEJBRemote.class, "LogLogisticsProviderSessionEJBBean");
			
			VenPartySessionEJBRemote venPartySessionHome = (VenPartySessionEJBRemote) locator
			.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");
			
			VenPartyTypeSessionEJBRemote partyTypeSessionHome = (VenPartyTypeSessionEJBRemote) locator
			.lookup(VenPartyTypeSessionEJBRemote.class, "VenPartyTypeSessionEJBBean");
			
			LogReportTemplateSessionEJBRemote templateSessionHome = (LogReportTemplateSessionEJBRemote) locator.lookup(LogReportTemplateSessionEJBRemote.class, "LogReportTemplateSessionEJBBean");

			/*
			 * This is the map with the whole table (all rows)
			 */
			HashMap<String,String> providerDetailMap = Util.formHashMapfromXML(providerDataMap.get("LOGPROVIDER"));
			
			/*
			 * This is the list that will be used to save the address later
			 */
			ArrayList<VenParty> venPartyList = new ArrayList<VenParty>();
			
			LogLogisticsProvider logLogisticsProvider= new LogLogisticsProvider();

			VenParty venParty = new VenParty();
				
			//Check If Log Provider ID not Null		
			if (providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)!=null) {
				try{
					logLogisticsProvider = logProviderSessionHome.queryByRange("select o from LogLogisticsProvider o where o.logisticsProviderId="+new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)), 0, 1).get(0);
				}catch(IndexOutOfBoundsException e){
					logLogisticsProvider.setLogisticsProviderId(new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)));
				}

				if (  providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID) !=null) {
						
					Long partyId = new Long( providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID));							
					try{
						venParty = venPartySessionHome.queryByRange("select o from VenParty o where o.partyId="+partyId, 0, 1).get(0);
					}catch(IndexOutOfBoundsException e){
						venParty.setPartyId(partyId);
					}
					
					if (providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME)!=null) {
						venParty.setFullOrLegalName(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME));
					}
					
					venPartyList.add(venParty);
					venPartySessionHome.mergeVenPartyList(venPartyList);
					logLogisticsProvider.setVenParty(venParty);
				}
					
				if (providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE)!=null) {
					String logisticsProviderCode = providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE);
					logLogisticsProvider.setLogisticsProviderCode(logisticsProviderCode);		

				} 
					
				if (providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC) !=null) {
					Long reportTemplateId = null;
					LogReportTemplate logReportTemplate2 = null;
						try{
							reportTemplateId = new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC));
						}catch(NumberFormatException nfe){
							// Need to lookup the primary key from the database here
							String reportTemplateDesc = providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC);
							List<LogReportTemplate> reportTemplateList = templateSessionHome.queryByRange("select o from LogReportTemplate o where o.templateDesc = '" + reportTemplateDesc + "'", 0, 0);
							if(!reportTemplateList.isEmpty()){
								logReportTemplate2 = reportTemplateList.get(0); 
							}
						}
					
						if (logReportTemplate2 == null) {
							reportTemplateId = new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC));
							List<LogReportTemplate> reportTemplateList = templateSessionHome.queryByRange("select o from LogReportTemplate o where o.templateId = "+ reportTemplateId, 0, 0);
							if (!reportTemplateList.isEmpty()) {
								logReportTemplate2 = reportTemplateList.get(0);
							}
						}
						logLogisticsProvider.setLogReportTemplate2(logReportTemplate2);
				}
					
				if (providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC) !=null) {
					Long reportTemplateId = null;
					LogReportTemplate logReportTemplate1 = null;
						try{
							reportTemplateId = new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC));
						}catch(NumberFormatException nfe){
							// Need to lookup the primary key from the database here
							String reportTemplateDesc = providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC);
							List<LogReportTemplate> reportTemplateList = templateSessionHome.queryByRange("select o from LogReportTemplate o where o.templateDesc = '" + reportTemplateDesc + "'", 0, 0);
							if(!reportTemplateList.isEmpty()){
								logReportTemplate1 = reportTemplateList.get(0); 
							}
						}
					
						if (logReportTemplate1 == null) {
							reportTemplateId = new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC));
							List<LogReportTemplate> reportTemplateList = templateSessionHome.queryByRange("select o from LogReportTemplate o where o.templateId = "+ reportTemplateId, 0, 0);
							if (!reportTemplateList.isEmpty()) {
								logReportTemplate1 = reportTemplateList.get(0);
							}
						}
						logLogisticsProvider.setLogReportTemplate1(logReportTemplate1);
				}
					
				logProviderSessionHome.mergeLogLogisticsProvider(logLogisticsProvider);
					
				}  else {
					//Check If Party ID is  Null
					if(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID)==null){
						if (providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME)!=null) {
							venParty.setFullOrLegalName(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_FULLORLEGALNAME));
						}

						if (providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC) !=null) {
							Long partyTypeId = null;
							VenPartyType venPartyType = null;
							try{
								partyTypeId = new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC));
							}catch(NumberFormatException nfe){
							// Need to lookup the primary key from the database here
									String partyTypeDesc = providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC);
									List<VenPartyType> venPartyTypeList = partyTypeSessionHome.queryByRange("select o from VenPartyType o where o.partyTypeDesc = '" + partyTypeDesc + "'", 0, 0);
									if(!venPartyTypeList.isEmpty()){
										venPartyType = venPartyTypeList.get(0); 
									}
							}

							if (venPartyType == null) {
								partyTypeId = new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_VENPARTYTYPE_PARTYTYPEDESC));
								List<VenPartyType> venPartyTypeList = partyTypeSessionHome.queryByRange("select o from VenPartyType o where o.partyTypeId = "+ partyTypeId, 0, 0);
								if (!venPartyTypeList.isEmpty()) {
									venPartyType = venPartyTypeList.get(0);
								}
							}
							venParty.setVenPartyType(venPartyType);							
				
						}
						venPartyList.add(venParty);
						venParty = venPartySessionHome.persistVenParty(venParty);
						logLogisticsProvider.setVenParty(venParty);
				}		
						if (providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC) !=null) {
							Long reportTemplateId = null;
							LogReportTemplate logReportTemplate2 = null;
								try{
									reportTemplateId = new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC));
								}catch(NumberFormatException nfe){
									// Need to lookup the primary key from the database here
									String reportTemplateDesc = providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC);
									List<LogReportTemplate> reportTemplateList = templateSessionHome.queryByRange("select o from LogReportTemplate o where o.templateDesc = '" + reportTemplateDesc + "'", 0, 0);
									if(!reportTemplateList.isEmpty()){
										logReportTemplate2 = reportTemplateList.get(0); 
									}
								}

								if (logReportTemplate2 == null) {
									reportTemplateId = new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGREPORTTEMPLATE_TEMPLATEDESC));
									List<LogReportTemplate> reportTemplateList = templateSessionHome.queryByRange("select o from LogReportTemplate o where o.templateId = "+ reportTemplateId, 0, 0);
									if (!reportTemplateList.isEmpty()) {
										logReportTemplate2 = reportTemplateList.get(0);
									}
								}
								logLogisticsProvider.setLogReportTemplate2(logReportTemplate2);
						}
							
						if (providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC) !=null) {
							Long reportTemplateId = null;
							LogReportTemplate logReportTemplate1 = null;
								try{
									reportTemplateId = new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC));
								}catch(NumberFormatException nfe){
									// Need to lookup the primary key from the database here
									String reportTemplateDesc = providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC);
									List<LogReportTemplate> reportTemplateList = templateSessionHome.queryByRange("select o from LogReportTemplate o where o.templateDesc = '" + reportTemplateDesc + "'", 0, 0);
									if(!reportTemplateList.isEmpty()){
										logReportTemplate1 = reportTemplateList.get(0); 
									}
								}
							
								if (logReportTemplate1 == null) {
									reportTemplateId = new Long(providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGINVOICETEMPLATE_TEMPLATEDESC));
									List<LogReportTemplate> reportTemplateList = templateSessionHome.queryByRange("select o from LogReportTemplate o where o.templateId = "+ reportTemplateId, 0, 0);
									if (!reportTemplateList.isEmpty()) {
										logReportTemplate1 = reportTemplateList.get(0);
									}
								}
								logLogisticsProvider.setLogReportTemplate1(logReportTemplate1);
						}
							
						if (providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE)!=null) {
								String logisticsProviderCode = providerDetailMap.get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERCODE);
								logLogisticsProvider.setLogisticsProviderCode(logisticsProviderCode);		

						}
						logProviderSessionHome.persistLogLogisticsProvider(logLogisticsProvider);
					} //else end

			
		}catch (Exception ex) {
			String errorMsg = Util.extractMessageFromEJBExceptionText(ex.getMessage());
			ex.printStackTrace();
			return "-1" + ":" + errorMsg;
		}finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "0";
	}
}
