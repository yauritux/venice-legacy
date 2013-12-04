package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.LogScheduleDayOfWeekSessionEJBRemote;
import com.gdn.venice.persistence.LogScheduleDayOfWeek;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter command use for fetching days data  on Schedule  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchDaysDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Logistic Provider Schedule Days data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchDaysDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		//Extract the query criteria from the request
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
				
		//This is a list of rows sent in the response (returned by query)
		List<HashMap<String, String>> responseDataList = new ArrayList<HashMap<String, String>>();
		
		Locator<Object> locator = null;
		
		//This is the RAF data source response we will build
		RafDsResponse rafDsResponse = new RafDsResponse();
		try {
			locator = new Locator<Object>();
			
			LogScheduleDayOfWeekSessionEJBRemote sessionHome = (LogScheduleDayOfWeekSessionEJBRemote) locator
			.lookup(LogScheduleDayOfWeekSessionEJBRemote.class, "LogScheduleDayOfWeekSessionEJBBean");
			
			List<LogScheduleDayOfWeek> daysList = null;
			
			LogScheduleDayOfWeek logScheduleDayOfWeek = new LogScheduleDayOfWeek();

			if (criteria!=null) {
				daysList = sessionHome.findByLogScheduleDayOfWeekLike(logScheduleDayOfWeek, criteria, 0, 0);
			} else {
				daysList = sessionHome.queryByRange("select o from LogScheduleDayOfWeek o ", 0, 0);
			}
			
			/*
			 * Map the data into a map for populating the dataList
			 */			
			for (LogScheduleDayOfWeek day:daysList) {
				//Each row is stored in a HashMap
				HashMap<String, String> rowMap = new HashMap<String, String>();
				
				rowMap.put(DataNameTokens.LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYID, day.getScheduleDayId().toString()!=null ? day.getScheduleDayId().toString():"");
				rowMap.put(DataNameTokens.LOGSCHEDULEDAYOFWEEK_SCHEDULEDAYDESC, day.getScheduleDayDesc() !=null? day.getScheduleDayDesc():"");
			
				responseDataList.add(rowMap);
			}
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(responseDataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+responseDataList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(responseDataList);
		return rafDsResponse;
	}

}
