package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenCitySessionEJBRemote;
import com.gdn.venice.persistence.VenCity;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter command use for adding city data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class AddCityDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for Add Logistic Provider Address City/Region data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public AddCityDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		/*
		 * This is the list that will be used to add the city data later
		 */
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<VenCity> venCityLocator=null;
		
		try{
			venCityLocator = new Locator<VenCity>();
			VenCitySessionEJBRemote venPromotionSessionHome = (VenCitySessionEJBRemote) venCityLocator.lookup(VenCitySessionEJBRemote.class, "VenCitySessionEJBBean");
			dataList=request.getData();
			
			VenCity venCity = new VenCity();
			 
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.VENCITY_CITYID)){
						venCity.setCityId(new Long (data.get(key)));
					} else if(key.equals(DataNameTokens.VENCITY_CITYNAME )){
						venCity.setCityName(data.get(key));
					} else if(key.equals(DataNameTokens.VENCITY_CITYCODE )){
						venCity.setCityCode(data.get(key));
					}
				}
			}
					
			venCity=venPromotionSessionHome.persistVenCity(venCity);
			dataList=new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map=new HashMap<String, String>();
			
			/*
			 * Map the data into a map for populating the dataList
			 */
			map.put(DataNameTokens.VENCITY_CITYID, venCity.getCityId()!=null?venCity.getCityId().toString():"");
			map.put(DataNameTokens.VENCITY_CITYCODE, venCity.getCityCode()!=null?venCity.getCityCode():"");
			map.put(DataNameTokens.VENCITY_CITYNAME, venCity.getCityName()!=null?venCity.getCityName():"");

			dataList.add(map);
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(0);
			rafDsResponse.setTotalRows(1);			
			rafDsResponse.setEndRow(1);
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(venCityLocator!=null){
					venCityLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}