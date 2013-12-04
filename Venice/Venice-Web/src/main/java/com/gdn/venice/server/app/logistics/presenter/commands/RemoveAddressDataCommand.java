package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenAddressSessionEJBRemote;
import com.gdn.venice.facade.VenPartyAddressSessionEJBRemote;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenAddressType;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenPartyAddress;
import com.gdn.venice.persistence.VenPartyAddressPK;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * This is RPC-Style presenter  command use for removing Logistic Provider  address data on Address Tab
 *
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class RemoveAddressDataCommand implements RafRpcCommand {

	
	// The map of all the parameters passed to the command
	HashMap<String, String> providerAddressDataMap;
	
	/**
	 * Basic constructor with parameters passed in XML string
	 * @param parameter a list of the parameters for the form in XML
	 */
	public RemoveAddressDataCommand(String parameter) {
		providerAddressDataMap = Util.formHashMapfromXML(parameter);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		Locator<VenPartyAddress> locator = null;
		
		try {
			locator = new Locator<VenPartyAddress>();
			
			VenPartyAddressSessionEJBRemote venPartyAddressSessionHome = (VenPartyAddressSessionEJBRemote) locator
			.lookup(VenPartyAddressSessionEJBRemote.class, "VenPartyAddressSessionEJBBean");
			
			VenAddressSessionEJBRemote venAddressSessionHome = (VenAddressSessionEJBRemote) locator
			.lookup(VenAddressSessionEJBRemote.class, "VenAddressSessionEJBBean");
			
			/*
			 * This is the list that will be used to delete the party address later
			 */
			ArrayList<VenPartyAddress> partyAddressList = new ArrayList<VenPartyAddress>();
			
			/*
			 * This is the list that will be used to delete the address later
			 */
			ArrayList<VenAddress> venAddressList = new ArrayList<VenAddress>();
			
			/*
			 * This loop processes all fo the records to be deleted
			 */
			for (int i=0;i<providerAddressDataMap.size();i++) {
				/*
				 * This map contains a single row of data
				 */
				HashMap<String,String> addressRowMap = Util.formHashMapfromXML(providerAddressDataMap.get("REMOVEDADDRESS" + i));
				
				/*
				 * This is the VenPartyAddress object we will add to the list
				 */
				VenPartyAddress venPartyAddress = new VenPartyAddress();
				
				//This is the primary key instance we will use 
				VenPartyAddressPK id = new VenPartyAddressPK();
				
				if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID)!=null) {
					VenParty venParty = new VenParty();
					venParty.setPartyId(new Long(addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENPARTY_PARTYID)));
					venPartyAddress.setVenParty(venParty);
					id.setPartyId(venParty.getPartyId());
				}
				
				if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID)!=null) {
					VenAddress venAddress = new VenAddress();
					venAddress.setAddressId(new Long(addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESS_ADDRESSID)));
					venPartyAddress.setVenAddress(venAddress);
					venAddressList.add(venAddress);
					id.setAddressId(venAddress.getAddressId());
					
				}
				
				if (addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID)!=null) {
					VenAddressType venAddressType = new VenAddressType();
					venAddressType.setAddressTypeId(new Long(addressRowMap.get(DataNameTokens.VENPARTYADDRESS_VENADDRESSTYPE_ADDRESSTYPEID)));
					venPartyAddress.setVenAddressType(venAddressType);
					id.setAddressTypeId(venAddressType.getAddressTypeId());
					
				}
				
				venPartyAddress.setId(id);
				partyAddressList.add(venPartyAddress);			
			}
			venPartyAddressSessionHome.removeVenPartyAddressList(partyAddressList);
			venAddressSessionHome.removeVenAddressList(venAddressList);
			
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
