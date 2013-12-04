package com.gdn.venice.server.app.finance.presenter.commands;

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
 * This Datasource-style presenter command use for fetching Party data from database
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011 
 *
 */
public class FetchPromotionPartyDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	private RafDsRequest request;
	
	/**
	 * Basic Constructor for Fetch Promotion Party data
	 * 
	 * @param request is the request parameters passed to the command 
	 */
	public FetchPromotionPartyDataCommand(RafDsRequest request) {
		this.request = request;
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
			
			VenPartySessionEJBRemote sessionHome = (VenPartySessionEJBRemote) locator
			.lookup(VenPartySessionEJBRemote.class, "VenPartySessionEJBBean");
			
			List<VenParty> partyList = null;
			
			VenParty party = new VenParty();

			if (criteria!=null) {
				partyList = sessionHome.findByVenPartyLike(party, criteria, 0, 50);
			} else {
				partyList = sessionHome.queryByRange("select o from VenParty o where o.venPartyType.partyTypeId not in (2, 5 ) ", 0, 0);
			}
			
			/*
			 * Map the data into a map for populating the dataList
			 */			
			for (VenParty venParty:partyList) {
				//Each row is stored in a HashMap
				HashMap<String, String> rowMap = new HashMap<String, String>();
				
				rowMap.put(DataNameTokens.VENPARTY_PARTYID, (venParty.getPartyId()!=null)?venParty.getPartyId().toString():"");
				rowMap.put(DataNameTokens.VENPARTY_FULLORLEGALNAME, venParty.getFullOrLegalName());
			
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
