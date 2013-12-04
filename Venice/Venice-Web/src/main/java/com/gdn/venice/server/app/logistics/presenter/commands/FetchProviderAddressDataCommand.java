package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPartyAddressSessionEJBRemote;
import com.gdn.venice.persistence.VenPartyAddress;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 * This Datasource-style presenter servlet command use for fetching Logistics Provider  Addresss data
 *
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchProviderAddressDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Logistics Provider Address data
	 * @param rafDsRequest is the request parameters passed to the command 
	 */
	public FetchProviderAddressDataCommand(RafDsRequest rafDsRequest) {
		this.request = rafDsRequest;
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> venPartyAddressLocator=null;
		
		try{
			venPartyAddressLocator = new Locator<Object>();
			
			VenPartyAddressSessionEJBRemote venPartyAddressSessionHome = (VenPartyAddressSessionEJBRemote) venPartyAddressLocator.lookup(VenPartyAddressSessionEJBRemote.class, "VenPartyAddressSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the provider address
			 */
			List<VenPartyAddress> venPartyAddressList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			
			if (criteria == null) {
				if(request.getParams() != null && request.getParams().get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID) != null){
					String query = "select o from VenPartyAddress o where o.venParty.partyId = " + request.getParams().get(DataNameTokens.LOGLOGISTICSPROVIDER_VENPARTY_PARTYID);	
					venPartyAddressList = venPartyAddressSessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				}else{
					String query = "select o from VenPartyAddress o ";	
					venPartyAddressList = venPartyAddressSessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				}
			} else {
				VenPartyAddress venPartyAddress = new VenPartyAddress();
				venPartyAddressList = venPartyAddressSessionHome.findByVenPartyAddressLike(venPartyAddress, criteria, 0, 0);
			}
			
			/*
			 * Map the data into a map for populating the dataList
			 */
			for(VenPartyAddress partyAddress:venPartyAddressList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID, partyAddress.getVenParty().getPartyId()!=null? partyAddress.getVenParty().getPartyId().toString():"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID, partyAddress.getVenAddress().getAddressId()!=null?partyAddress.getVenAddress().getAddressId().toString():"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1, partyAddress.getVenAddress().getStreetAddress1() !=null? partyAddress.getVenAddress().getStreetAddress1():"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2, partyAddress.getVenAddress().getStreetAddress2() !=null? partyAddress.getVenAddress().getStreetAddress2():"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN, partyAddress.getVenAddress().getKecamatan() !=null? partyAddress.getVenAddress().getKecamatan():"" );
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN, partyAddress.getVenAddress().getKelurahan() !=null? partyAddress.getVenAddress().getKelurahan():"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID, partyAddress.getVenAddress().getVenCity()!=null?partyAddress.getVenAddress().getVenCity().getCityId()+"":"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME, partyAddress.getVenAddress().getVenCity()!=null?partyAddress.getVenAddress().getVenCity().getCityName():"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATEID, partyAddress.getVenAddress().getVenState()!=null?partyAddress.getVenAddress().getVenState().getStateId()+"":"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME, partyAddress.getVenAddress().getVenState()!=null?partyAddress.getVenAddress().getVenState().getStateName():"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE, partyAddress.getVenAddress().getPostalCode());
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYID, partyAddress.getVenAddress().getVenCountry().getCountryId()!=null?partyAddress.getVenAddress().getVenCountry().getCountryId().toString():"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME, partyAddress.getVenAddress().getVenCountry().getCountryCode()!=null?partyAddress.getVenAddress().getVenCountry().getCountryCode():"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID, partyAddress.getVenAddressType().getAddressTypeId() !=null? partyAddress.getVenAddressType().getAddressTypeId().toString():"");
				map.put(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC, partyAddress.getVenAddressType().getAddressTypeDesc() !=null? partyAddress.getVenAddressType().getAddressTypeDesc():"");
				
				dataList.add(map);
			}

			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(venPartyAddressList.size());
			rafDsResponse.setEndRow(request.getStartRow()+venPartyAddressList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(venPartyAddressLocator!=null){
					venPartyAddressLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}
