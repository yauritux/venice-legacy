package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.HashMap;

import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;


/**
 * This is RPC-style presenter command use for Saving Logistics Provider Schedule detail on Schedule  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class SaveScheduleDataCommand implements RafRpcCommand {

	// The map of all the parameters passed to the command
	HashMap<String, String> providerScheduleDataMap;
	
	/**
	 * Basic constructor with parameters passed in XML string
	 * @param parameter a list of the parameters for the form in XML
	 */
	public SaveScheduleDataCommand(String parameter) {
		providerScheduleDataMap = Util.formHashMapfromXML(parameter);	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
//		Locator<Object> locator=null;
//
//		try {
//			locator = new Locator<Object>();
//			LogPickupScheduleSessionEJBRemote logPickupScheduleSessionHome = (LogPickupScheduleSessionEJBRemote) locator
//					.lookup(LogPickupScheduleSessionEJBRemote.class,
//							"LogPickupScheduleSessionEJBBean");
//			
//			LogScheduleDayOfWeekSessionEJBRemote logScheduleDayOfWeekSessionHome = (LogScheduleDayOfWeekSessionEJBRemote) locator
//			.lookup(LogScheduleDayOfWeekSessionEJBRemote.class,
//					"LogScheduleDayOfWeekSessionEJBBean");
//
//
//			/*
//			 * This is the map with the whole table (all rows)
//			 */
//			HashMap<String,String> scheduleDetailMap = Util.formHashMapfromXML(providerScheduleDataMap.get("SCHEDULEDETAIL"));
//			
//			/*
//			 * This is the list that will be used to save the address later
//			 */
//			ArrayList<LogPickupSchedule> logPickupScheduleList = new ArrayList<LogPickupSchedule>();
//			
//			LogPickupSchedule logPickupSchedule = new LogPickupSchedule();
//			/*
//			 * This loop processes the rows in the request from the ListGrid
//			 */
//			for (int i=0;i<scheduleDetailMap.size();i++) {
//				
//
//				HashMap<String,String> scheduleRowMap = Util.formHashMapfromXML(scheduleDetailMap.get("SCHEDULEDETAIL" + i));
//				
//				//Check If Schedule ID not Null
//				if ( scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULESID)!=null) {
//					Long pickupSchedulesId = new Long(scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULESID));
//					logPickupSchedule.setPickupSchedulesId(pickupSchedulesId);	
//					
//					if (scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)!=null) {
//						Long logisticsProviderId = new Long(scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
//						LogLogisticsProvider logLogisticsProvider = new LogLogisticsProvider();
//						logLogisticsProvider.setLogisticsProviderId(logisticsProviderId);
//						logPickupSchedule.setLogLogisticsProvider(logLogisticsProvider);
//					}
//					
//					if (scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME)!=null) {
//						Time fromTime = new Time(new Long(scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME)));
//						logPickupSchedule.setFromTime(fromTime);
//					}
//					
//					if (scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME)!=null) {
//						Time toTime = new Time(new Long(scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME)));
//						logPickupSchedule.setToTime(toTime);
//					}
//					
//					
//					if (scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC)!=null) {
//						String pickupScheduleDesc = scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC);
//						logPickupSchedule.setPickupScheduleDesc(pickupScheduleDesc);
//					}
//					
//					if (scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS)!=null) {
//						Boolean includePublicHolidays = new Boolean(scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS));
//						logPickupSchedule.setIncludePublicHolidays(includePublicHolidays);
//					}
//					
//					if (scheduleRowMap.get(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC)!=null) {
//						Long logisticsServiceTypeId = null;
//						LogLogisticsServiceType logLogisticsServiceType = null;
//						try{
//							logisticsServiceTypeId = new Long(scheduleRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC));
//						}catch(NumberFormatException nfe){
//							// Need to lookup the primary key from the database here
//							String logisticsServiceTypeDesc = scheduleRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC);
//							List<LogLogisticsServiceType> logServiceTypeList = logServiceTypeSessionHome.queryByRange("select o from LogLogisticsServiceType o where o.logisticsServiceTypeDesc = '" + logisticsServiceTypeDesc + "'", 0, 0);
//							if(!logServiceTypeList.isEmpty()){
//								logLogisticsServiceType = logServiceTypeList.get(0); 
//							}
//						}
//						//If the promotion share is null then assume the value in desc must be the ID
//						if (logLogisticsServiceType == null) {
//							logisticsServiceTypeId = new Long(
//									scheduleRowMap
//											.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC));
//							List<LogLogisticsServiceType> logLogisticsServiceTypeList = logServiceTypeSessionHome
//									.queryByRange(
//											"select o from LogLogisticsServiceType o where o.logisticsServiceTypeId = "
//													+ logisticsServiceTypeId, 0, 0);
//							if (!logLogisticsServiceTypeList.isEmpty()) {
//								logLogisticsServiceType = logLogisticsServiceTypeList
//										.get(0);
//							}
//						}
//						logPickupSchedule
//						.setLogLogisticsServiceType(logLogisticsServiceType);
//					}
//					
//					logPickupScheduleList.add(logPickupSchedule);
//					logServiceSessionHome.mergeLogLogisticServiceList(logPickupScheduleList);
//
//				//If Service ID is Null
//				}else{
//				
//					if (scheduleRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID)!=null) {
//						Long logisticsProviderId = new Long(scheduleRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID));
//						LogLogisticsProvider logLogisticsProvider = new LogLogisticsProvider();
//						logLogisticsProvider.setLogisticsProviderId(logisticsProviderId);
//						logPickupSchedule.setLogLogisticsProvider(logLogisticsProvider);
//					}
//					
//					if (scheduleRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE)!=null) {
//						String serviceCode = scheduleRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_SERVICECODE);
//						logPickupSchedule.setServiceCode(serviceCode);
//					}
//					
//					if (scheduleRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC)!=null) {
//						String logisticsServiceDesc = scheduleRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGISTICSSERVICEDESC);
//						logPickupSchedule.setLogisticsServiceDesc(logisticsServiceDesc);
//					}
//					
//					if (scheduleRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC)!=null) {
//						Long logisticsServiceTypeId = null;
//						LogLogisticsServiceType logLogisticsServiceType = null;
//						try{
//							logisticsServiceTypeId = new Long(scheduleRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC));
//						}catch(NumberFormatException nfe){
//							// Need to lookup the primary key from the database here
//							String logisticsServiceTypeDesc = scheduleRowMap.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC);
//							List<LogLogisticsServiceType> logServiceTypeList = logServiceTypeSessionHome.queryByRange("select o from LogLogisticsServiceType o where o.logisticsServiceTypeDesc = '" + logisticsServiceTypeDesc + "'", 0, 0);
//							if(!logServiceTypeList.isEmpty()){
//								logLogisticsServiceType = logServiceTypeList.get(0); 
//							}
//						}
//						//If assume the value in desc must be the ID
//						if (logLogisticsServiceType == null) {
//							logisticsServiceTypeId = new Long(
//									scheduleRowMap
//											.get(DataNameTokens.LOGLOGISTICSERVICE_LOGLOGISTICSSERVICETYPE_LOGISTICSSERVICETYPEDESC));
//							List<LogLogisticsServiceType> logLogisticsServiceTypeList = logServiceTypeSessionHome
//									.queryByRange(
//											"select o from LogLogisticsServiceType o where o.logisticsServiceTypeId = "
//													+ logisticsServiceTypeId, 0, 0);
//							if (!logLogisticsServiceTypeList.isEmpty()) {
//								logLogisticsServiceType = logLogisticsServiceTypeList
//										.get(0);
//							}
//						}
//						logPickupSchedule
//						.setLogLogisticsServiceType(logLogisticsServiceType);
//					}
//					logPickupScheduleList.add(logPickupSchedule);
//					logServiceSessionHome.mergeLogLogisticServiceList(logPickupScheduleList);	
//
//				}
//
//			}
//
//		} catch (Exception ex) {
//			String errorMsg = Util.extractMessageFromEJBExceptionText(ex.getMessage());
//			ex.printStackTrace();
//			return "-1" + ":" + errorMsg;
//		} finally {
//			try {			
//				locator.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}	
		return null;
	}
}
