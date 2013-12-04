package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPartySessionEJBRemote;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 *This is Datasource-style presenter command use for fetching party maintenance data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class FetchPartyMaintenanceDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	/**
	 * Basic Constructor for Fetch Party Maintenance data
	 * @param rafDsRequest
	 */
	public FetchPartyMaintenanceDataCommand(RafDsRequest rafDsRequest) {	
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> partyDataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> venPartyMaintenanceLocator=null;
		
		try{
			venPartyMaintenanceLocator = new Locator <Object>();
			
			VenPartySessionEJBRemote venPartySessionHome = (VenPartySessionEJBRemote) venPartyMaintenanceLocator.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the partys
			 */
			List<VenParty> venPartyMaintenanceList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			
			if (criteria == null) {	
				venPartyMaintenanceList = venPartySessionHome.queryByRange("select o from VenParty o where o.venPartyType.partyTypeId not in (2, 5)", 0, 50);
			} else {
				VenParty venParty = new VenParty();
				venPartyMaintenanceList = venPartySessionHome.findByVenPartyLike(venParty, criteria, 0, 0);
			}
			
			/*
			 * Map the data into a map for populating the partyDataList
			 */
			for(VenParty party:venPartyMaintenanceList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.VENPARTY_PARTYID, party.getPartyId()!=null?party.getPartyId().toString():"");
				map.put(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEID, party.getVenPartyType().getPartyTypeId()!=null?party.getVenPartyType().getPartyTypeId().toString():"");
				map.put(DataNameTokens.VENPARTY_VENPARTYTYPE_PARTYTYPEDESC, party.getVenPartyType().getPartyTypeDesc()!=null?party.getVenPartyType().getPartyTypeDesc():"");
				map.put(DataNameTokens.VENPARTY_FIRSTNAME, party.getPartyFirstName()!=null?party.getPartyFirstName():"");
				map.put(DataNameTokens.VENPARTY_MIDDLENAME, party.getPartyMiddleName()!=null?party.getPartyMiddleName():"");
				map.put(DataNameTokens.VENPARTY_LASTNAME, party.getPartyLastName()!=null?party.getPartyLastName():"");
				map.put(DataNameTokens.VENPARTY_FULLORLEGALNAME, party.getFullOrLegalName()!=null?party.getFullOrLegalName():"");
				map.put(DataNameTokens.VENPARTY_POSITION, party.getPartyPosition()!=null?party.getPartyPosition():"");
			
				partyDataList.add(map);
			}
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(venPartyMaintenanceList.size());
			rafDsResponse.setEndRow(request.getStartRow()+venPartyMaintenanceList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(venPartyMaintenanceLocator!=null){
					venPartyMaintenanceLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(partyDataList);
		return rafDsResponse;
	}
}