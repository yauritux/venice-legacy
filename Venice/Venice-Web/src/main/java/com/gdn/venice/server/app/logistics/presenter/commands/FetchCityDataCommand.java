package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenCitySessionEJBRemote;
import com.gdn.venice.persistence.VenCity;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 * This is Datasource-style presenter command use for fetching City/Region data in Address table on Address  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchCityDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Logistic Provider Address City/Region data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchCityDataCommand(RafDsRequest rafDsRequest) {
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
			
			VenCitySessionEJBRemote sessionHome = (VenCitySessionEJBRemote) locator
			.lookup(VenCitySessionEJBRemote.class, "VenCitySessionEJBBean");
			
			List<VenCity> cityList = null;
			
			VenCity city = new VenCity();

			if (criteria!=null) {
				cityList = sessionHome.findByVenCityLike(city, criteria, 0, 0);
			} else {
				cityList = sessionHome.queryByRange("select o from VenCity o ", 0, 0);
			}
			
			/*
			 * Map the data into a map for populating the dataList
			 */			
			for (VenCity venCity:cityList) {
				//Each row is stored in a HashMap
				HashMap<String, String> rowMap = new HashMap<String, String>();
				
				rowMap.put(DataNameTokens.VENCITY_CITYID, (venCity.getCityId()!=null)?venCity.getCityId().toString():"");
				rowMap.put(DataNameTokens.VENCITY_CITYCODE, venCity.getCityCode() !=null? venCity.getCityCode():"");
				rowMap.put(DataNameTokens.VENCITY_CITYNAME, venCity.getCityName() !=null? venCity.getCityName():"");
			
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
