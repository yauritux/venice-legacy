package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote;
import com.gdn.venice.facade.LogLogisticsServiceTypeSessionEJBRemote;
import com.gdn.venice.persistence.LogLogisticService;
import com.gdn.venice.persistence.LogLogisticsProvider;
import com.gdn.venice.persistence.LogLogisticsServiceType;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * This is RPC-style presenter command use for Saving Logistics Provider Service detail on Service  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class SaveServiceDataCommand implements RafRpcCommand {

	// The map of all the parameters passed to the command
	HashMap<String, String> providerServiceDataMap;
	
	/**
	 * Basic constructor with parameters passed in XML string
	 * @param parameter a list of the parameters for the form in XML
	 */
	
	public SaveServiceDataCommand(String parameter) {
		providerServiceDataMap = Util.formHashMapfromXML(parameter);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		Locator<Object> locator=null;

		try {
			locator = new Locator<Object>();
			LogLogisticsServiceTypeSessionEJBRemote logServiceTypeSessionHome = (LogLogisticsServiceTypeSessionEJBRemote) locator
					.lookup(LogLogisticsServiceTypeSessionEJBRemote.class, "LogLogisticsServiceTypeSessionEJBBean");
			
			LogLogisticServiceSessionEJBRemote logServiceSessionHome = (LogLogisticServiceSessionEJBRemote) locator
			.lookup(LogLogisticServiceSessionEJBRemote.class, "LogLogisticServiceSessionEJBBean");

			/*
			 * This is the map with the whole table (all rows)
			 */
			HashMap<String,String> serviceDetailMap = Util.formHashMapfromXML(providerServiceDataMap.get("SERVICEDETAIL"));
			
			/*
			 * This is the list that will be used to save the address later
			 */
			ArrayList<LogLogisticService> logLogisticServiceList = new ArrayList<LogLogisticService>();
			
			LogLogisticService logLogisticService = new LogLogisticService();
			/*
			 * This loop processes the rows in the request from the ListGrid
			 */
			for (int i=0;i<serviceDetailMap.size();i++) {
				HashMap<String,String> serviceRowMap = Util.formHashMapfromXML(serviceDetailMap.get("SERVICEDETAIL" + i));
				
		
				//Check If Service ID not Null
				if ( serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID)!=null) {
					Long logisticsServiceId = new Long(serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID));
					try{
						logLogisticService = logServiceSessionHome.queryByRange("select o from LogLogisticService o where o.logisticsServiceId="+logisticsServiceId, 0, 1).get(0);
					}catch(IndexOutOfBoundsException e){
						logLogisticService.setLogisticsServiceId(logisticsServiceId);
					}
					
					if (serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)!=null) {
						Long logisticsProviderId = new Long(serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
						LogLogisticsProvider logLogisticsProvider = new LogLogisticsProvider();
						logLogisticsProvider.setLogisticsProviderId(logisticsProviderId);
						logLogisticService.setLogLogisticsProvider(logLogisticsProvider);
					}
					
					if (serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE)!=null) {
						String serviceCode = serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE);
						logLogisticService.setServiceCode(serviceCode);
					}
					
					if (serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC)!=null) {
						String logisticsServiceDesc = serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC);
						logLogisticService.setLogisticsServiceDesc(logisticsServiceDesc);
					}
					
					if (serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC)!=null) {
						Long logisticsServiceTypeId = null;
						LogLogisticsServiceType logLogisticsServiceType = null;
						try{
							logisticsServiceTypeId = new Long(serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC));
						}catch(NumberFormatException nfe){
							// Need to lookup the primary key from the database here
							String logisticsServiceTypeDesc = serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC);
							List<LogLogisticsServiceType> logServiceTypeList = logServiceTypeSessionHome.queryByRange("select o from LogLogisticsServiceType o where o.logisticsServiceTypeDesc = '" + logisticsServiceTypeDesc + "'", 0, 0);
							if(!logServiceTypeList.isEmpty()){
								logLogisticsServiceType = logServiceTypeList.get(0); 
							}
						}
						//If the promotion share is null then assume the value in desc must be the ID
						if (logLogisticsServiceType == null) {
							logisticsServiceTypeId = new Long(serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC));
							List<LogLogisticsServiceType> logLogisticsServiceTypeList = logServiceTypeSessionHome.queryByRange("select o from LogLogisticsServiceType o where o.logisticsServiceTypeId = " + logisticsServiceTypeId, 0, 0);
							if (!logLogisticsServiceTypeList.isEmpty()) {
								logLogisticsServiceType = logLogisticsServiceTypeList.get(0);
							}
						}
						logLogisticService.setLogLogisticsServiceType(logLogisticsServiceType);
					}
					
					logLogisticServiceList.add(logLogisticService);
					logServiceSessionHome.mergeLogLogisticServiceList(logLogisticServiceList);

					//If Service ID is Null
				}else{
					if (serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)!=null) {
						Long logisticsProviderId = new Long(serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
						LogLogisticsProvider logLogisticsProvider = new LogLogisticsProvider();
						logLogisticsProvider.setLogisticsProviderId(logisticsProviderId);
						logLogisticService.setLogLogisticsProvider(logLogisticsProvider);
					}
					
					if (serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE)!=null) {
						String serviceCode = serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE);
						logLogisticService.setServiceCode(serviceCode);
					}
					
					if (serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC)!=null) {
						String logisticsServiceDesc = serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC);
						logLogisticService.setLogisticsServiceDesc(logisticsServiceDesc);
					}
					
					if (serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC)!=null) {
						Long logisticsServiceTypeId = null;
						LogLogisticsServiceType logLogisticsServiceType = null;
						try{
							logisticsServiceTypeId = new Long(serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC));
						}catch(NumberFormatException nfe){
							// Need to lookup the primary key from the database here
							String logisticsServiceTypeDesc = serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC);
							List<LogLogisticsServiceType> logServiceTypeList = logServiceTypeSessionHome.queryByRange("select o from LogLogisticsServiceType o where o.logisticsServiceTypeDesc = '" + logisticsServiceTypeDesc + "'", 0, 0);
							if(!logServiceTypeList.isEmpty()){
								logLogisticsServiceType = logServiceTypeList.get(0); 
							}
						}
						//If assume the value in desc must be the ID
						if (logLogisticsServiceType == null) {
							logisticsServiceTypeId = new Long(serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC));
							List<LogLogisticsServiceType> logLogisticsServiceTypeList = logServiceTypeSessionHome.queryByRange("select o from LogLogisticsServiceType o where o.logisticsServiceTypeId = " + logisticsServiceTypeId, 0, 0);
							if (!logLogisticsServiceTypeList.isEmpty()) {
								logLogisticsServiceType = logLogisticsServiceTypeList.get(0);
							}
						}
						logLogisticService.setLogLogisticsServiceType(logLogisticsServiceType);
					}
					logLogisticServiceList.add(logLogisticService);
					logServiceSessionHome.mergeLogLogisticServiceList(logLogisticServiceList);	
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
