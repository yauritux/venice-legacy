package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote;
import com.gdn.venice.persistence.VenPartyPromotionShare;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter command use for fetching Promotion Share data on bottom table
 *  
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">ChristianSuwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class FetchPromotionShareDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;

	/**
	 * Basic constructor for Fetch Promotion Share data
	 * @param request
	 */
	public FetchPromotionShareDataCommand(RafDsRequest request) {

		this.request = request;

	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<VenPartyPromotionShare> venPartyPromotionShareLocator=null;
		
		try{
			venPartyPromotionShareLocator = new Locator<VenPartyPromotionShare>();
			
			VenPartyPromotionShareSessionEJBRemote venPartyPromotionShareSessionHome = (VenPartyPromotionShareSessionEJBRemote) venPartyPromotionShareLocator.lookup(VenPartyPromotionShareSessionEJBRemote.class, "VenPartyPromotionShareSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the promotion shares
			 */
			List<VenPartyPromotionShare> venPartyPromotionShareList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();
			
			if (criteria == null) {
				if(request.getParams() != null && request.getParams().get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID) != null){
					String query = "select o from VenPartyPromotionShare o where o.venPromotion.promotionId = " + request.getParams().get(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID);	
					venPartyPromotionShareList = venPartyPromotionShareSessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				}else{
					String query = "select o from VenPartyPromotionShare o ";	
					venPartyPromotionShareList = venPartyPromotionShareSessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				}
			} else {
				VenPartyPromotionShare venPartyPromotionShare = new VenPartyPromotionShare();
				venPartyPromotionShareList = venPartyPromotionShareSessionHome.findByVenPartyPromotionShareLike(venPartyPromotionShare, criteria, 0, 0);
			}
			
			/*
			 * Map the data into a map for populating the dataList
			 */
			for(VenPartyPromotionShare partyPromotionShare:venPartyPromotionShareList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTION_PROMOTIONID , partyPromotionShare.getId().getPromotionId().toString()!=null?partyPromotionShare.getId().getPromotionId().toString():"");
				map.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_PARTYID, partyPromotionShare.getId().getPartyId().toString()!=null?partyPromotionShare.getId().getPartyId().toString():"");
				map.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPARTY_FULLORLEGALNAME, partyPromotionShare.getVenParty().getFullOrLegalName()!=null?partyPromotionShare.getVenParty().getFullOrLegalName():"");
				map.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODID , partyPromotionShare.getVenPromotionShareCalcMethod().getPromotionCalcMethodId().toString()!=null?partyPromotionShare.getVenPromotionShareCalcMethod().getPromotionCalcMethodId().toString():"");
				map.put(DataNameTokens.VENPARTYPROMOTIONSHARE_VENPROMOTIONSHARECALCMETHOD_PROMOTIONCALCMETHODDESC , partyPromotionShare.getVenPromotionShareCalcMethod().getPromotionCalcMethodDesc()!=null?partyPromotionShare.getVenPromotionShareCalcMethod().getPromotionCalcMethodDesc():"");
				map.put(DataNameTokens.VENPARTYPROMOTIONSHARE_PROMOTIONCALCVALUE , partyPromotionShare.getPromotionCalcValue()!=null?partyPromotionShare.getPromotionCalcValue():"");
				dataList.add(map);
			}

			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(venPartyPromotionShareList.size());
			rafDsResponse.setEndRow(request.getStartRow()+venPartyPromotionShareList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(venPartyPromotionShareLocator!=null){
					venPartyPromotionShareLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}			
}
