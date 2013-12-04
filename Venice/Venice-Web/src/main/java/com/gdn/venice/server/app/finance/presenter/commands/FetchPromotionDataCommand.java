package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPromotionSessionEJBRemote;
import com.gdn.venice.persistence.VenPromotion;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;


/**
 *This is Datasource-style presenter command use for fetching promotion data
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">ChristianSuwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 *
 */
public class FetchPromotionDataCommand implements RafDsCommand {
	
	RafDsRequest request;
	
	/**
	 * Basic constructor for Fetch Promotion data
	 * @param request
	 */
	public FetchPromotionDataCommand(RafDsRequest request) {
		
		this.request = request;	
	}

	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<VenPromotion> venPromotionLocator=null;
		
		try{
			venPromotionLocator = new Locator<VenPromotion>();
			
			VenPromotionSessionEJBRemote venPromotionSessionHome = (VenPromotionSessionEJBRemote) venPromotionLocator.lookup(VenPromotionSessionEJBRemote.class, "VenPromotionSessionEJBBean");
			
			/*
			 * This is the list that will be used to fetch the promotions
			 */
			List<VenPromotion> venPromotionList = null;
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
		
			if (criteria == null) {
				String query = "select o from VenPromotion o left join fetch o.venPromotionType";			
				venPromotionList = venPromotionSessionHome.queryByRange(query, 0, 50);
			} else {
				VenPromotion venPromotion = new VenPromotion();
				venPromotionList = venPromotionSessionHome.findByVenPromotionLike(venPromotion, criteria, 0, 0);
			}
				
			/*
			 * Map the data into a map for populating the dataList
			 */
			for(VenPromotion promotion:venPromotionList){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.VENPROMOTION_PROMOTIONID, promotion.getPromotionId()!=null?promotion.getPromotionId().toString():"");
				map.put(DataNameTokens.VENPROMOTION_PROMOTIONCODE, promotion.getPromotionCode()!=null?promotion.getPromotionCode():"");
				map.put(DataNameTokens.VENPROMOTION_PROMOTIONNAME, promotion.getPromotionName()!=null?promotion.getPromotionName():"");
				map.put(DataNameTokens.VENPROMOTION_VENPROMOTIONTYPE_PROMOTIONTYPEDESC, promotion.getVenPromotionType()!=null?promotion.getVenPromotionType().getPromotionTypeDesc():"");				
				map.put(DataNameTokens.VENPROMOTION_GDNMARGIN, promotion.getGdnMargin()!=null?promotion.getGdnMargin().toString():"");
				map.put(DataNameTokens.VENPROMOTION_MERCHANTMARGIN, promotion.getMerchantMargin()!=null?promotion.getMerchantMargin().toString():"");
				map.put(DataNameTokens.VENPROMOTION_OTHERSMARGIN, promotion.getOthersMargin()!=null?promotion.getOthersMargin().toString():"");
				
				dataList.add(map);
			}

			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(venPromotionList.size());
			rafDsResponse.setEndRow(request.getStartRow()+venPromotionList.size());
			
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(venPromotionLocator!=null){
					venPromotionLocator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
	
}
