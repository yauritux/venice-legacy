package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenCitySessionEJBRemote;
import com.gdn.venice.persistence.VenCity;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter  command for Updating/Editing City Data 
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class UpdateCityDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for update/edit Logistics Provider City data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public UpdateCityDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		rafDsResponse.setStatus(0);
		rafDsResponse.setStartRow(request.getStartRow());
		rafDsResponse.setTotalRows(0);
		rafDsResponse.setEndRow(request.getStartRow());
		
		List<VenCity> cityList = new ArrayList<VenCity>();		
		List<HashMap<String,String >> dataList = request.getData();		
		VenCity venCity = new VenCity();
		
		Locator<VenCitySessionEJBRemote> locator = null;
		
		try {
			locator = new Locator<VenCitySessionEJBRemote>();
			
			VenCitySessionEJBRemote sessionHome = (VenCitySessionEJBRemote) locator
			.lookup(VenCitySessionEJBRemote.class, "VenCitySessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();

					if (key.equals(DataNameTokens.VENCITY_CITYID)) {
						venCity.setCityId(new Long(data.get(DataNameTokens.VENCITY_CITYID)));
					} else if (key.equals(DataNameTokens.VENCITY_CITYCODE)) {
						venCity.setCityCode(data.get(key));
					} else if (key.equals(DataNameTokens.VENCITY_CITYNAME)) {
						venCity.setCityName(data.get(key));	
					} 
				}						
				cityList.add(venCity);			
			}
					
			sessionHome.mergeVenCityList((ArrayList<VenCity>)cityList);
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			
			if (criteria == null) {
				String query = "select o from VenCity o";			
				cityList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				VenCity vCity = new VenCity();
				cityList = sessionHome.findByVenCityLike(vCity, criteria, 0, 0);
			}
			
			
			for(int i=0; i<cityList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				VenCity list = cityList.get(i);
				
				map.put(DataNameTokens.VENCITY_CITYID, list.getCityId()!=null?list.getCityId().toString():"");
				map.put(DataNameTokens.VENCITY_CITYCODE, list.getCityCode());
				map.put(DataNameTokens.VENCITY_CITYNAME, list.getCityName());
				
				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(cityList.size());
			rafDsResponse.setEndRow(request.getStartRow()+cityList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(locator!=null){
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}