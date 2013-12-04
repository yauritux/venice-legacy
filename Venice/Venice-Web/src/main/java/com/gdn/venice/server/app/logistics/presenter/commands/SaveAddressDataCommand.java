package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.client.app.logistic.view.ProviderManagementView;
import com.gdn.venice.facade.VenAddressSessionEJBRemote;
import com.gdn.venice.facade.VenAddressTypeSessionEJBRemote;
import com.gdn.venice.facade.VenCountrySessionEJBRemote;
import com.gdn.venice.facade.VenPartyAddressSessionEJBRemote;
import com.gdn.venice.facade.VenStateSessionEJBRemote;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenAddressType;
import com.gdn.venice.persistence.VenCity;
import com.gdn.venice.persistence.VenCountry;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenPartyAddress;
import com.gdn.venice.persistence.VenPartyAddressPK;
import com.gdn.venice.persistence.VenState;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;


/**
 * This is RPC-style presenter command use for Saving Logistics Provider Address data on Address  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class SaveAddressDataCommand implements RafRpcCommand {

	ProviderManagementView proview;
	
	// The map of all the parameters passed to the command
	HashMap<String, String> providerAddressDataMap;
	
	/**
	 * Basic constructor with parameters passed in XML string
	 * @param parameter a list of the parameters for the form in XML
	 */
	public SaveAddressDataCommand(String parameter) {
		providerAddressDataMap = Util.formHashMapfromXML(parameter);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		Locator<Object> locator=null;

		try {
			locator = new Locator<Object>();
			VenPartyAddressSessionEJBRemote venPartyAddressSessionHome = (VenPartyAddressSessionEJBRemote) locator
					.lookup(VenPartyAddressSessionEJBRemote.class,
							"VenPartyAddressSessionEJBBean");
			
			VenAddressSessionEJBRemote venAddressSessionHome = (VenAddressSessionEJBRemote) locator
			.lookup(VenAddressSessionEJBRemote.class,
					"VenAddressSessionEJBBean");
			
			VenStateSessionEJBRemote venStateSessionHome = (VenStateSessionEJBRemote) locator
			.lookup(VenStateSessionEJBRemote.class,
					"VenStateSessionEJBBean");
			
			VenCountrySessionEJBRemote venCountrySessionHome = (VenCountrySessionEJBRemote) locator
			.lookup(VenCountrySessionEJBRemote.class,
					"VenCountrySessionEJBBean");
			
			VenAddressTypeSessionEJBRemote venAddressTypeSessionHome = (VenAddressTypeSessionEJBRemote) locator
			.lookup(VenAddressTypeSessionEJBRemote.class,
					"VenAddressTypeSessionEJBBean");

			/*
			 * This is the map with the whole table (all rows)
			 */
			HashMap<String,String> addressDetailMap = Util.formHashMapfromXML(providerAddressDataMap.get("ADDRESS"));
			
			/*
			 * This is the list that will be used to save the party address later
			 */
			ArrayList<VenPartyAddress> venPartyAddressList = new ArrayList<VenPartyAddress>();
			
			/*
			 * This is the list that will be used to save the address later
			 */
			ArrayList<VenAddress> venAddressList = new ArrayList<VenAddress>();
			
			/*
			 * This loop processes the rows in the request from the ListGrid
			 */
			for (int i=0;i<addressDetailMap.size();i++) {
				VenPartyAddress venPartyAddress = new VenPartyAddress();

				HashMap<String,String> addressRowMap = Util.formHashMapfromXML(addressDetailMap.get("ADDRESS" + i));

				VenPartyAddressPK id = new VenPartyAddressPK();
				
				VenAddress venAddress = new VenAddress();
				//Check If Party ID not Null
				if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID)!=null) {
					Long partyId = new Long(addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID));							
					VenParty  venParty = new VenParty();
					venParty.setPartyId(partyId);

					id.setPartyId(venParty.getPartyId());
					venPartyAddress.setVenParty(venParty);
				}
				//Check If Address ID not Null
				if ( addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID)!=null) {	
					Long addressId = new Long(addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID));							
					try
					{
						venAddress = venAddressSessionHome.queryByRange("select o from VenAddress o where o.addressId="+addressId, 0, 1).get(0);
					}catch(IndexOutOfBoundsException e){
						venAddress.setAddressId(addressId);
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1)!=null) {
						String streetAddress1 = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1);
						venAddress.setStreetAddress1(streetAddress1);
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2)!=null) {
						String streetAddress2 = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2);
				
						venAddress.setStreetAddress2(streetAddress2);
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN)!=null) {
						String kelurahan = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN);
						
						venAddress.setKelurahan(kelurahan);
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN)!=null) {

						String kecamatan = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN);
						
						venAddress.setKecamatan(kecamatan);
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME)!=null) {

						Long cityId = new Long(addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID));
						String cityName = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME);
						VenCity venCity = new VenCity();
						venCity.setCityId(cityId);
						venCity.setCityName(cityName);
						
						venAddress.setVenCity(venCity);
						
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE)!=null) {

						String postalCode = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE);			
						venAddress.setPostalCode(postalCode);		
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME)!=null) {
						Long stateId = null;
						VenState vState = null;
						try{
							stateId = new Long(addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME));
						}catch(NumberFormatException nfe){
							// Need to lookup the primary key from the database here
							String stateName = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME);
							List<VenState> venStateList = venStateSessionHome.queryByRange("select o from VenState o where o.stateName = '" + stateName + "'", 0, 0);
							if(!venStateList.isEmpty()){
								vState = venStateList.get(0); 
							}
						}
						//If the promotion share is null then assume the value in desc must be the ID
						if (vState == null) {
							stateId = new Long(
									addressRowMap
											.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME));
							List<VenState> venStateList = venStateSessionHome
									.queryByRange(
											"select o from VenState o where o.stateId = "
													+ stateId, 0, 0);
							if (!venStateList.isEmpty()) {
								vState = venStateList
										.get(0);
							}
						}
						venAddress
						.setVenState(vState);
						
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME) !=null) {
						Long countryId = new Long( addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYID));
						String countryName = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME);
						String countryCode = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME);
						VenCountry venCountry = new VenCountry();
						venCountry.setCountryId(countryId);	
						venCountry.setCountryCode(countryCode);
						venCountry.setCountryName(countryName);	
						
						venAddress.setVenCountry(venCountry);

					}
					venAddressList.add(venAddress);
					id.setAddressId(venAddress.getAddressId());
					venPartyAddress.setVenAddress(venAddress);
					venAddressSessionHome.mergeVenAddressList(venAddressList);
					
					//If Address ID is Null
				}else{
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1)!=null) {
						String streetAddress1 = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS1);
						venAddress.setStreetAddress1(streetAddress1);
					}					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2)!=null) {
						String streetAddress2 = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_STREETADDRESS2);				
						venAddress.setStreetAddress2(streetAddress2);
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN)!=null) {
						String kelurahan = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KELURAHAN);						
						venAddress.setKelurahan(kelurahan);
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN)!=null) {
						String kecamatan = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_KECAMATAN);						
						venAddress.setKecamatan(kecamatan);
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME)!=null) {

						Long cityId = new Long(addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYID));
						String cityName = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCITY_CITYNAME);
						VenCity venCity = new VenCity();
						venCity.setCityId(cityId);
						venCity.setCityName(cityName);
						
						venAddress.setVenCity(venCity);
						
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE)!=null) {

						String postalCode = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_POSTALCODE);			
						venAddress.setPostalCode(postalCode);		
					}
					
					if ( addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME)!=null) {
						Long stateId = null;
						VenState vState = null;
						try{
							stateId = new Long(addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME));
						}catch(NumberFormatException nfe){
							// Need to lookup the primary key from the database here
							String stateName = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME);
							List<VenState> venStateList = venStateSessionHome.queryByRange("select o from VenState o where o.stateName = '" + stateName + "'", 0, 0);
							if(!venStateList.isEmpty()){
								vState = venStateList.get(0); 
							}
						}

						if (vState == null) {
							stateId = new Long(
									addressRowMap
											.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENSTATE_STATENAME));
							List<VenState> venStateList = venStateSessionHome
									.queryByRange(
											"select o from VenState o where o.stateId = "
													+ stateId, 0, 0);
							if (!venStateList.isEmpty()) {
								vState = venStateList
										.get(0);
							}
						}
						venAddress
						.setVenState(vState);
					}
					
					if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME) !=null) {
						Long countryId = null;
						VenCountry country = null;
						try{
							countryId = new Long(addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME));
						}catch(NumberFormatException nfe){
							// Need to lookup the primary key from the database here
							String countryCode = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME);
							List<VenCountry> venCountryList = venCountrySessionHome.queryByRange("select o from VenCountry o where o.countryCode = '" + countryCode + "'", 0, 0);
							if(!venCountryList.isEmpty()){
								country = venCountryList.get(0); 
							}
						}
						if (country == null) {
							countryId = new Long(
									addressRowMap
											.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_VENCOUNTRY_COUNTRYNAME));
							List<VenCountry> venCountryList = venCountrySessionHome
									.queryByRange(
											"select o from VenCountry o where o.countryId = "
													+ countryId, 0, 0);
							if (!venCountryList.isEmpty()) {
								country = venCountryList
										.get(0);
							}
						}
						venAddress
						.setVenCountry(country);


					}
					
					venAddressList.add(venAddress);
					venAddress = venAddressSessionHome.persistVenAddress(venAddress);
					id.setAddressId(venAddress.getAddressId());
					venPartyAddress.setVenAddress(venAddress);
					
				}
				
				//If Address Type Desc not Null		
				if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC)!=null ) {
					Long addressTypeId = null;
					VenAddressType venAddressType = null;
					try{
						addressTypeId = new Long(addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC));
					}catch(NumberFormatException nfe){
						// Need to lookup the primary key from the database here
						String addressTypeDesc = addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC);
						List<VenAddressType> venAddressTypeList = venAddressTypeSessionHome.queryByRange("select o from VenAddressType o where o.addressTypeDesc = '" + addressTypeDesc + "'", 0, 0);
						if(!venAddressTypeList.isEmpty()){
							venAddressType = venAddressTypeList.get(0); 
						}
					}
					// assume the value in desc must be the ID
					if (venAddressType == null) {
						addressTypeId = new Long(
								addressRowMap
										.get(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEDESC));
						List<VenAddressType> venAddressTypeList = venAddressTypeSessionHome
								.queryByRange(
										"select o from VenAddressType o where o.addressTypeId = "
												+ addressTypeId, 0, 0);
						if (!venAddressTypeList.isEmpty()) {
							venAddressType = venAddressTypeList
									.get(0);
						}
					}

					id.setAddressTypeId(venAddressType.getAddressTypeId());
					venPartyAddress.setVenAddressType(venAddressType);
				}else {
					return "-2";
				}
											
				venPartyAddress.setId(id);
				venPartyAddressList.add(venPartyAddress);			
			}
			venPartyAddressSessionHome.mergeVenPartyAddressList(venPartyAddressList);
		
		} catch (Exception ex) {
			String errorMsg = Util.extractMessageFromEJBExceptionText(ex.getMessage());
			ex.printStackTrace();
			return "-1" + ":" + errorMsg;
		} finally {
			try {			
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		return "0";
	}
}