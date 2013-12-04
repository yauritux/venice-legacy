package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenCitySessionEJBRemote;
import com.gdn.venice.persistence.VenCity;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter command use for removing city data.
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class RemoveCityDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request = null;
	/**
	 * Basic constructor for Remove Logistics Provider Address data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public RemoveCityDataCommand(RafDsRequest rafDsRequest) {
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
		
		for(int i=0; i<dataList.size();i++){
			Map <String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();
			
			while (iter.hasNext()) {
				String key = iter.next();

				if (key.equals(DataNameTokens.VENCITY_CITYID)) {
					venCity.setCityId(new Long(data.get(DataNameTokens.VENCITY_CITYID)));
				} 
			}						
			cityList.add(venCity);	
 
		}
		
		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			VenCitySessionEJBRemote sessionHome = (VenCitySessionEJBRemote) locator
			.lookup(VenCitySessionEJBRemote.class, "VenCitySessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<cityList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.VENCITY_CITYID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(cityList.get(i).getCityId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENCITY_CITYID));
				criteria.add(simpleCriteria);
			}
			
			cityList = sessionHome.findByVenCityLike(venCity, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeVenCityList((ArrayList<VenCity>)cityList);		
					
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(cityList.size());
			rafDsResponse.setEndRow(request.getStartRow()+cityList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally {
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
