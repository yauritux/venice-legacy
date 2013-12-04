package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogPickupScheduleSessionEJBRemote;
import com.gdn.venice.persistence.LogPickupSchedule;
import com.gdn.venice.persistence.LogScheduleDayOfWeek;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter command use for fetching Logistics Provider Schedule data on Schedule  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchLogisticProviderScheduleDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	/**
	 * Basic constructor for Fetch Logistic Provider Schedule data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchLogisticProviderScheduleDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		
		/* 
		 * Locating LogisticPickupSchedule EJB
		 */
		Locator<Object> logPickUpScheduleLocator=null;
		
		try{
			logPickUpScheduleLocator = new Locator<Object>();
			LogPickupScheduleSessionEJBRemote logPickupScheduleSessionHome = (LogPickupScheduleSessionEJBRemote) logPickUpScheduleLocator.lookup(LogPickupScheduleSessionEJBRemote.class, "LogPickupScheduleSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch logistic pickup schedule
			 */
			List<LogPickupSchedule> logPickupScheduleList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		
			if (criteria == null) {
				if(request.getParams() != null && request.getParams().get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID) != null){
					String query = "select o from LogPickupSchedule o join fetch o.logScheduleDayOfWeeks where o.logLogisticsProvider.logisticsProviderId = " + request.getParams().get(DataNameTokens.LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID);	
					logPickupScheduleList = logPickupScheduleSessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				}else{

					logPickupScheduleList = logPickupScheduleSessionHome.queryByRange("select o from LogPickupSchedule o join fetch o.logScheduleDayOfWeeks", request.getStartRow(), request.getEndRow()-request.getStartRow());
				}
			} else {
				LogPickupSchedule logPickupSchedule = new LogPickupSchedule();
				logPickupScheduleList = logPickupScheduleSessionHome.findByLogPickupScheduleLike(logPickupSchedule, criteria, 0, 0);
			}
			
			/*
			 * Map the data into a map for populating the dataList
			 */
			for(LogPickupSchedule logPickup:logPickupScheduleList){
				HashMap<String, String> map = new HashMap<String, String>();
				
				String days = "";
				String daysId = "";
				for(LogScheduleDayOfWeek logPickupDay:logPickup.getLogScheduleDayOfWeeks()){
					if(days.isEmpty() ){
						days = logPickupDay.getScheduleDayDesc();
						daysId = logPickupDay.getScheduleDayId().toString();
					}else{
						daysId = daysId + "," + logPickupDay.getScheduleDayId().toString();
						days = days + "," + logPickupDay.getScheduleDayDesc();
					}
				}
				map.put(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULESID ,logPickup.getPickupSchedulesId() !=null? logPickup.getPickupSchedulesId().toString():"");
				map.put(DataNameTokens.LOGPICKUPSCHEDULE_LOGLOGISTICSPROVIDER_LOGISTICSPROVIDERID ,logPickup.getLogLogisticsProvider().getLogisticsProviderId() !=null? logPickup.getLogLogisticsProvider().getLogisticsProviderId().toString():"");
				map.put(DataNameTokens.LOGPICKUPSCHEDULE_FROMTIME ,logPickup.getFromTime().toString() !=null? logPickup.getFromTime().toString():"");
				map.put(DataNameTokens.LOGPICKUPSCHEDULE_TOTIME , logPickup.getToTime().toString()!=null? logPickup.getToTime().toString():"");
				map.put(DataNameTokens.LOGPICKUPSCHEDULE_PICKUPSCHEDULEDESC, logPickup.getPickupScheduleDesc() !=null? logPickup.getPickupScheduleDesc():"");
				map.put(DataNameTokens.LOGPICKUPSCHEDULE_INCLUDEPUBLICHOLIDAYS, logPickup.getIncludePublicHolidays().toString());
				map.put(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID, daysId);		
				map.put(DataNameTokens.LOGPICKUPSCHEDULE_LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC, days);	
				dataList.add(map);
			}

			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(logPickupScheduleList.size());
			rafDsResponse.setEndRow(request.getStartRow()+logPickupScheduleList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(logPickUpScheduleLocator!=null){
					logPickUpScheduleLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}