package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogLogisticServiceSessionEJBRemote;
import com.gdn.venice.persistence.LogLogisticService;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * This is RPC-Style presenter  command use for removing Logistic Provider  Service data on Service Tab
 *
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class RemoveServiceDataCommand implements RafRpcCommand {

	// The map of all the parameters passed to the command
	HashMap<String, String> providerServiceDataMap;
	
	/**
	 * Basic constructor with parameters passed in XML string
	 * @param parameter a list of the parameters for the form in XML
	 */
	public RemoveServiceDataCommand(String parameter) {
		providerServiceDataMap = Util.formHashMapfromXML(parameter);	
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			LogLogisticServiceSessionEJBRemote logServiceSessionHome = (LogLogisticServiceSessionEJBRemote) locator
			.lookup(LogLogisticServiceSessionEJBRemote.class,
					"LogLogisticServiceSessionEJBBean");
			
			/*
			 * This is the list that will be used to remove the service later
			 */
			ArrayList<LogLogisticService> logLogisticServiceList = new ArrayList<LogLogisticService>();
			
			/*
			 * This loop processes all fo the records to be deleted
			 */
			for (int i=0;i<providerServiceDataMap.size();i++) {
				/*
				 * This map contains a single row of data
				 */
				HashMap<String,String> serviceRowMap = Util.formHashMapfromXML(providerServiceDataMap.get("REMOVEDSERVICE" + i));
				
				/*
				 * This is the LogLogisticService object we will add to the list
				 */
				LogLogisticService logLogisticService = new LogLogisticService();
				
				
				if (serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID)!=null) {
					logLogisticService.setLogisticsServiceId(new Long(serviceRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEID)));
				}			
				
				logLogisticServiceList.add(logLogisticService);			
			}
			logServiceSessionHome.removeLogLogisticServiceList(logLogisticServiceList);
			
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
