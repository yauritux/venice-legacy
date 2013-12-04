package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenContactDetailType;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;


/**
 * This is RPC-style presenter command use for Saving Logistics Provider Contact detail on Address  Tab
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class SaveContactDataCommand implements RafRpcCommand {

	// The map of all the parameters passed to the command
	HashMap<String, String> providerContactDataMap;
	
	/**
	 * Basic constructor with parameters passed in XML string
	 * @param parameter a list of the parameters for the form in XML
	 */
	public SaveContactDataCommand(String parameter) {
		providerContactDataMap = Util.formHashMapfromXML(parameter);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		Locator<Object> locator=null;

		try {
			locator = new Locator<Object>();
			VenContactDetailSessionEJBRemote venContactDetailSessionHome = (VenContactDetailSessionEJBRemote) locator
					.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");
			
			VenContactDetailTypeSessionEJBRemote venContactDetailTypeSessionHome = (VenContactDetailTypeSessionEJBRemote) locator
					.lookup(VenContactDetailTypeSessionEJBRemote.class, "VenContactDetailTypeSessionEJBBean");


			/*
			 * This is the map with the whole table (all rows)
			 */
			HashMap<String,String> contactDetailMap = Util.formHashMapfromXML(providerContactDataMap.get("CONTACTDETAIL"));
			
			/*
			 * This is the list that will be used to save the address later
			 */
			ArrayList<VenContactDetail> contactDataList = new ArrayList<VenContactDetail>();
			
			/*
			 * This loop processes the rows in the request from the ListGrid
			 */
			for (int i=0;i<contactDetailMap.size();i++) {
				VenContactDetail venContactDetail = new VenContactDetail();

				HashMap<String,String> contactRowMap = Util.formHashMapfromXML(contactDetailMap.get("CONTACTDETAIL" + i));
				
				//Check If contact detail ID not Null
				if ( contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID)!=null) {
					Long contactId = new Long(contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID));
					try
					{
						venContactDetail = venContactDetailSessionHome.queryByRange("select o from VenContactDetail o where o.contactDetailId="+contactId, 0, 1).get(0);
					}catch(IndexOutOfBoundsException e){
						venContactDetail.setContactDetailId(contactId);
					}
					
					if (contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL)!=null) {
						String contactDetail = contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL);
						venContactDetail.setContactDetail(contactDetail);
					}
					
					if (contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID)!=null) {
						Long partyId = new Long(contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID));
						VenParty venParty = new VenParty();
						venParty.setPartyId(partyId);
						venContactDetail.setVenParty(venParty);
					}
					
					if (contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC)!=null) {
						Long contactDetailTypeId = null;
						VenContactDetailType venContactDetailType = null;
						try{
							contactDetailTypeId = new Long(contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC));
						}catch(NumberFormatException nfe){
							// Need to lookup the primary key from the database here
							String contactDetailTypeDesc = contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC);
							List<VenContactDetailType> venContactDetailTypeList = venContactDetailTypeSessionHome.queryByRange("select o from VenContactDetailType o where o.contactDetailTypeDesc = '" + contactDetailTypeDesc + "'", 0, 0);
							if(!venContactDetailTypeList.isEmpty()){
								venContactDetailType = venContactDetailTypeList.get(0); 
							}
						}
						//If the promotion share is null then assume the value in desc must be the ID
						if (venContactDetailType == null) {
							contactDetailTypeId = new Long(contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC));
							List<VenContactDetailType> venContactDetailTypeList = venContactDetailTypeSessionHome.queryByRange("select o from VenContactDetailType o where o.contactDetailTypeId = " + contactDetailTypeId, 0, 0);
							if (!venContactDetailTypeList.isEmpty()) {
								venContactDetailType = venContactDetailTypeList.get(0);
							}
						}
						venContactDetail.setVenContactDetailType(venContactDetailType);
					}
					
					contactDataList.add(venContactDetail);
					venContactDetailSessionHome.mergeVenContactDetailList(contactDataList);

					//If Address ID is Null
				}else{
					if (contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL)!=null) {
						String contactDetail = contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAIL);
						venContactDetail.setContactDetail(contactDetail);
					}
					
					if (contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID)!=null) {
						Long partyId = new Long(contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID));
						VenParty venParty = new VenParty();
						venParty.setPartyId(partyId);
						venContactDetail.setVenParty(venParty);
					}
					
					if (contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC)!=null) {
						Long contactDetailTypeId = null;
						VenContactDetailType venContactDetailType = null;
						try{
							contactDetailTypeId = new Long(contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC));
						}catch(NumberFormatException nfe){
							// Need to lookup the primary key from the database here
							String contactDetailTypeDesc = contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC);
							List<VenContactDetailType> venContactDetailTypeList = venContactDetailTypeSessionHome.queryByRange("select o from VenContactDetailType o where o.contactDetailTypeDesc = '" + contactDetailTypeDesc + "'", 0, 0);
							if(!venContactDetailTypeList.isEmpty()){
								venContactDetailType = venContactDetailTypeList.get(0); 
							}
						}
						//If assume the value in desc must be the ID
						if (venContactDetailType == null) {
							contactDetailTypeId = new Long(contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_VENCONTACTDETAILTYPE_CONTACTDETAILTYPEDESC));
							List<VenContactDetailType> venContactDetailTypeList = venContactDetailTypeSessionHome.queryByRange("select o from VenContactDetailType o where o.contactDetailTypeId = " + contactDetailTypeId, 0, 0);
							if (!venContactDetailTypeList.isEmpty()) {
								venContactDetailType = venContactDetailTypeList.get(0);
							}
						}
						venContactDetail.setVenContactDetailType(venContactDetailType);
					}					
					venContactDetailSessionHome.persistVenContactDetail(venContactDetail);
				}						
			}		
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
