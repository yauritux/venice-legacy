package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPartyTypeSessionEJBRemote;
import com.gdn.venice.persistence.VenPartyType;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 *This is Datasource-style presenter command use for fetching party type data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class FetchPartyTypeComboBoxDataCommand implements RafDsCommand {


	RafDsRequest request;
	
	/**
	 * Basic Constructor for Fetch party type combo box data
	 * @param rafDsRequest
	 */
	public FetchPartyTypeComboBoxDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> partyTypeDataList= new ArrayList<HashMap<String, String>>();
		Locator<VenPartyType> venPartyTypeLocator=null;
		
		try{
			venPartyTypeLocator = new Locator <VenPartyType>();
			
			VenPartyTypeSessionEJBRemote venPartyTypeSessionHome = (VenPartyTypeSessionEJBRemote) venPartyTypeLocator.lookup(VenPartyTypeSessionEJBRemote.class, "VenPartyTypeSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the party's type
			 */
			List<VenPartyType> venPartyTypeList = null;
			
			venPartyTypeList = venPartyTypeSessionHome.queryByRange("select o from VenPartyType o where o.partyTypeId = 2", 0, 0);

			
			/*
			 * Map the values from the combo box (Party Type Desc)
			 */
			for(VenPartyType partyType:venPartyTypeList){
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(DataNameTokens.VENPARTYTYPE_PARTYTYPEID, partyType.getPartyTypeId()!=null?partyType.getPartyTypeId().toString():"");
				map.put(DataNameTokens.VENPARTYTYPE_PARTYTYPEDESC,partyType.getPartyTypeDesc());
		
				partyTypeDataList.add(map);
			}
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(partyTypeDataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+partyTypeDataList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(venPartyTypeLocator!=null){
					venPartyTypeLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(partyTypeDataList);
		return rafDsResponse;
	}

}
