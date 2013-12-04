package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenPartyPromotionShare;
import com.gdn.venice.persistence.VenPartyPromotionSharePK;
import com.gdn.venice.persistence.VenPromotion;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * This is RPC-Style presenter  command use for deleting promotion share data
 *
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class DeletePromotionShareCommand implements RafRpcCommand {

	// The map of all the parameters passed to the command
	HashMap<String, String> promotionShareDataMap;
	
	/**
	 * Constructor with the form parameters
	 * @param parameter
	 */
	public DeletePromotionShareCommand(String parameter) {
		promotionShareDataMap = Util.formHashMapfromXML(parameter);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {
		Locator<VenPartyPromotionShare> venPartyPromotionShareLocator = null;
		
		try {
			venPartyPromotionShareLocator = new Locator<VenPartyPromotionShare>();
			
			VenPartyPromotionShareSessionEJBRemote venPartyPromotionShareSessionHome = (VenPartyPromotionShareSessionEJBRemote) venPartyPromotionShareLocator
			.lookup(VenPartyPromotionShareSessionEJBRemote.class, "VenPartyPromotionShareSessionEJBBean");
			
			/*
			 * This is the list that will be used to delete the promotion shares later
			 */
			ArrayList<VenPartyPromotionShare> promotionShareList = new ArrayList<VenPartyPromotionShare>();
			
			/*
			 * This loop processes all fo the records to be deleted
			 */
			for (int i=0;i<promotionShareDataMap.size();i++) {
				/*
				 * This map contains a single row of data
				 */
				HashMap<String,String> promotionShareRowMap = Util.formHashMapfromXML(promotionShareDataMap.get("DELETEDPROMOTIONSHARE" + i));
				
				/*
				 * This is the VenPartyPromotionShare object we will add to the list
				 */
				VenPartyPromotionShare venPartyPromotionShare = new VenPartyPromotionShare();
				
				//This is the primary key instance we will use 
				VenPartyPromotionSharePK id = new VenPartyPromotionSharePK();
				
				if (promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID)!=null) {
					VenPromotion venPromotion = new VenPromotion();
					venPromotion.setPromotionId(new Long(promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID)));
					venPartyPromotionShare.setVenPromotion(venPromotion);
					id.setPromotionId(venPromotion.getPromotionId());
				}
				
				if (promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID)!=null) {
					VenParty venParty = new VenParty();
					venParty.setPartyId(new Long(promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID)));
					venPartyPromotionShare.setVenParty(venParty);
					id.setPartyId(venParty.getPartyId());
					
				}
				venPartyPromotionShare.setId(id);
				promotionShareList.add(venPartyPromotionShare);			
			}
			venPartyPromotionShareSessionHome.removeVenPartyPromotionShareList(promotionShareList);
			
		} catch (Exception ex) {
			String errorMsg = Util.extractMessageFromEJBExceptionText(ex.getMessage());
			ex.printStackTrace();
			return "-1" + ":" + errorMsg;
		} finally {
			try {
				venPartyPromotionShareLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "0";
	}	
}
