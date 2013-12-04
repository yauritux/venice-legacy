package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenAddressTypeSessionEJBRemote;
import com.gdn.venice.persistence.VenAddressType;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 * This is Datasource-style presenter command use for fetching Address type data combo box in Address table on Address  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchAddressTypeDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Logistic Provider Address Type combo box data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchAddressTypeDataCommand(RafDsRequest rafDsRequest) {
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
			
			VenAddressTypeSessionEJBRemote sessionHome = (VenAddressTypeSessionEJBRemote) locator.lookup(VenAddressTypeSessionEJBRemote.class, "VenAddressTypeSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the address type  data
			 */
			List<VenAddressType> addressTypeList = null;
			
			addressTypeList = sessionHome.queryByRange("select o from VenAddressType o", 0, 0);
			
			/*
			 * Map the values from the combo box (Address Type field)
			 */
			for(VenAddressType addressType:addressTypeList){
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(DataNameTokens.VENADDRESSTYPE_ADDRESSTYPEID, addressType.getAddressTypeId()!=null?addressType.getAddressTypeId().toString():"");
				map.put(DataNameTokens.VENADDRESSTYPE_ADDRESSTYPEDESC,addressType.getAddressTypeDesc() !=null? addressType.getAddressTypeDesc():"" );
		
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
