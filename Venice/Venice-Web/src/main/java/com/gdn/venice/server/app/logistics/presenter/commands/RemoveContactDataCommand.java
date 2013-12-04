package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * This is RPC-Style presenter  command use for removing Logistic Provider  Contact data on Contact Tab
 *
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class RemoveContactDataCommand implements RafRpcCommand {

	
	// The map of all the parameters passed to the command
	HashMap<String, String> providerContactDataMap;
	
	/**
	 * Basic constructor with parameters passed in XML string
	 * @param parameter a list of the parameters for the form in XML
	 */
	public RemoveContactDataCommand(String parameter) {
		providerContactDataMap = Util.formHashMapfromXML(parameter);	
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			VenContactDetailSessionEJBRemote venContactDetailSessionHome = (VenContactDetailSessionEJBRemote) locator
			.lookup(VenContactDetailSessionEJBRemote.class,
					"VenContactDetailSessionEJBBean");
			
			/*
			 * This is the list that will be used to delete the promotion shares later
			 */
			ArrayList<VenContactDetail> contactDetailList = new ArrayList<VenContactDetail>();
			
			/*
			 * This loop processes all fo the records to be deleted
			 */
			for (int i=0;i<providerContactDataMap.size();i++) {
				/*
				 * This map contains a single row of data
				 */
				HashMap<String,String> contactRowMap = Util.formHashMapfromXML(providerContactDataMap.get("REMOVEDCONTACT" + i));
				
				/*
				 * This is the VenPartyPromotionShare object we will add to the list
				 */
				VenContactDetail venContactDetail = new VenContactDetail();
				
				
				if (contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID)!=null) {
					venContactDetail.setContactDetailId(new Long(contactRowMap.get(DataNameTokens.VENCONTACTDETAIL_CONTACTDETAILID)));
				}			
				
				contactDetailList.add(venContactDetail);			
			}
			venContactDetailSessionHome.removeVenContactDetailList(contactDetailList);
			
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