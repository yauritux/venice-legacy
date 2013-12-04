package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenCountrySessionEJBRemote;
import com.gdn.venice.persistence.VenCountry;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter command use for fetching Country combo box data in Address table on Address  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchCountryDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic Constructor for Fetch country combo box data
	 * 
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchCountryDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> countryDataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator <Object>();
			
			VenCountrySessionEJBRemote countrySessionHome = (VenCountrySessionEJBRemote) locator.lookup(VenCountrySessionEJBRemote.class, "VenCountrySessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the country data
			 */
			List<VenCountry> countryList = null;
			
			countryList = countrySessionHome.queryByRange("select o from VenCountry o", 0, 0);
			
			/*
			 * Map the values from the combo box (Country field)
			 */
			for(VenCountry country:countryList){
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(DataNameTokens.VENCOUNTRY_COUNTRYID, country.getCountryId()!=null?country.getCountryId().toString():"");
				map.put(DataNameTokens.VENCOUNTRY_COUNTRYCODE,country.getCountryCode() !=null? country.getCountryCode():"" );
				map.put(DataNameTokens.VENCOUNTRY_COUNTRYNAME,country.getCountryName() !=null? country.getCountryName():"" );
		
				countryDataList.add(map);
			}
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(countryDataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+countryDataList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(countryDataList);
		return rafDsResponse;
	}
}