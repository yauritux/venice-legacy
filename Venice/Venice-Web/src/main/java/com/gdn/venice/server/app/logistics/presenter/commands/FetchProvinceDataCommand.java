package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenStateSessionEJBRemote;
import com.gdn.venice.persistence.VenState;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This Datasource-style presenter servlet command use for fetching province combo box data
 *
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchProvinceDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic Constructor for Fetch Province data
	 * 
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchProvinceDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> provinceDataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator <Object>();
			
			VenStateSessionEJBRemote stateSessionHome = (VenStateSessionEJBRemote) locator.lookup(VenStateSessionEJBRemote.class, "VenStateSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the province data
			 */
			List<VenState> provinceList = null;
			
			provinceList = stateSessionHome.queryByRange("select o from VenState o", 0, 0);

			
			/*
			 * Map the values from the combo box (Province field)
			 */
			for(VenState state:provinceList){
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(DataNameTokens.VENSTATE_STATEID, state.getStateId()!=null?state.getStateId().toString():"");
				map.put(DataNameTokens.VENSTATE_STATECODE,state.getStateCode() !=null? state.getStateCode():"" );
				map.put(DataNameTokens.VENSTATE_STATENAME,state.getStateName() !=null? state.getStateName():"" );
		
				provinceDataList.add(map);
			}
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(provinceDataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+provinceDataList.size());
			
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
		rafDsResponse.setData(provinceDataList);
		return rafDsResponse;
	}

}
