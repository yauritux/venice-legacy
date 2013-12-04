package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote;
import com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote;
import com.gdn.venice.persistence.VenParty;
import com.gdn.venice.persistence.VenPartyPromotionShare;
import com.gdn.venice.persistence.VenPartyPromotionSharePK;
import com.gdn.venice.persistence.VenPromotion;
import com.gdn.venice.persistence.VenPromotionShareCalcMethod;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

/**
 * This is RPC-style presenter command use for saving and updating the Promotion share detail
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">ChristianSuwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class SavePromotionShareCommand implements RafRpcCommand {

	// The map of all the parameters passed to the command
	private HashMap<String, String> promotionShareDataMap;
	
	/**
	 * Basic constructor with parameters passed in XML string
	 * @param parameter a list of the parameters for the form in XML
	 */
	public SavePromotionShareCommand(String parameter) {
		promotionShareDataMap = Util.formHashMapfromXML(parameter);
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafRpcCommand#execute()
	 */
	@Override
	public String execute() {

		Locator<Object> locator=null;
		try {
			locator = new Locator<Object>();
			VenPartyPromotionShareSessionEJBRemote venPartyPromotionShareSessionHome = (VenPartyPromotionShareSessionEJBRemote) locator
					.lookup(VenPartyPromotionShareSessionEJBRemote.class,
							"VenPartyPromotionShareSessionEJBBean");
			VenPromotionShareCalcMethodSessionEJBRemote venPromotionShareCalcMethodSessionHome = (VenPromotionShareCalcMethodSessionEJBRemote) locator
					.lookup(VenPromotionShareCalcMethodSessionEJBRemote.class,
							"VenPromotionShareCalcMethodSessionEJBBean");

			/*
			 * This is the map with the whole table (all rows)
			 */
			HashMap<String,String> promotionShareDetailMap = Util.formHashMapfromXML(promotionShareDataMap.get("PROMOTIONSHARE"));
			
			/*
			 * This is the list that will be used to save the promotion shares later
			 */
			ArrayList<VenPartyPromotionShare> promotionShareList = new ArrayList<VenPartyPromotionShare>();
			
			/*
			 * This loop processes the rows in the request from the ListGrid
			 */
			for (int i=0;i<promotionShareDetailMap.size();i++) {
				VenPartyPromotionShare venPartyPromotionShare = new VenPartyPromotionShare();

				HashMap<String,String> promotionShareRowMap = Util.formHashMapfromXML(promotionShareDetailMap.get("PROMOTIONSHARE" + i));

				VenPartyPromotionSharePK id = new VenPartyPromotionSharePK();
				/*
				 * Extract the promotion ID and lookup the promotion
				 * 	o Don't really need to do the lookup		
				 */
				VenPromotion venPromotion = null;
				VenParty venParty = null;
				
				if (promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID)!=null) {
					Long promotionId = new Long(promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID));
					venPromotion = new VenPromotion();
					venPromotion.setPromotionId(promotionId);
					id.setPromotionId(venPromotion.getPromotionId());
				}
				
				if (promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID)!=null) {
					Long partyId = new Long(promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID));
					venParty = new VenParty();
					venParty.setPartyId(partyId);
					id.setPartyId(partyId);
				}
				
				try{
					venPartyPromotionShare = venPartyPromotionShareSessionHome.queryByRange("select o from VenPartyPromotionShare o where o.id.promotionId="
							+id.getPromotionId()+" and o.id.partyId="+id.getPartyId(), 0, 1).get(0);
				}catch(IndexOutOfBoundsException e){
					venPartyPromotionShare.setId(id);
				}
				venPartyPromotionShare.setVenPromotion(venPromotion);
				venPartyPromotionShare.setVenParty(venParty);
				
				if (promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC)!=null) {	
					/*
					 * This workaround is required because we are using setData. There seems to be a bug in smart GWT where
					 * when existing row values are retrieved from the list grid item with the combo box the description is
					 * returned but for new rows the ID is returned. 
					 * 	o We firstly try to stuff the value into a long and trap the exception to determine
					 * 	  if it is a Long or String value
					 *  o If it is a string then assume it is the desc and lookup the ID
					 */
					Long calcMethodId = null;
					VenPromotionShareCalcMethod venPromotionShareCalcMethod = null;
					try{
						calcMethodId = new Long(promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC));
					}catch(NumberFormatException nfe){
						// Need to lookup the primary key from the database here
						String calcMethodDesc = promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC);
						List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList = venPromotionShareCalcMethodSessionHome.queryByRange("select o from VenPromotionShareCalcMethod o where o.promotionCalcMethodDesc = '" + calcMethodDesc + "'", 0, 0);
						if(!venPromotionShareCalcMethodList.isEmpty()){
							venPromotionShareCalcMethod = venPromotionShareCalcMethodList.get(0); 
						}
					}
					//If the promotion share is null then assume the value in desc must be the ID
					if (venPromotionShareCalcMethod == null) {
						calcMethodId = new Long(
								promotionShareRowMap
										.get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC));
						List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList = venPromotionShareCalcMethodSessionHome
								.queryByRange(
										"select o from VenPromotionShareCalcMethod o where o.promotionCalcMethodId = "
												+ calcMethodId, 0, 0);
						if (!venPromotionShareCalcMethodList.isEmpty()) {
							venPromotionShareCalcMethod = venPromotionShareCalcMethodList
									.get(0);
						}
					}
					venPartyPromotionShare
					.setVenPromotionShareCalcMethod(venPromotionShareCalcMethod);
				} 
				
				if (promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE)!=null) {
					venPartyPromotionShare.setPromotionCalcValue(promotionShareRowMap.get(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE));
				}

				promotionShareList.add(venPartyPromotionShare);			
			}
			venPartyPromotionShareSessionHome.mergeVenPartyPromotionShareList(promotionShareList);
		
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