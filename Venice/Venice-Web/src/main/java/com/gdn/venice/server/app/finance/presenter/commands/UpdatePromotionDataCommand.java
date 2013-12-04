package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPromotionSessionEJBRemote;
import com.gdn.venice.persistence.VenPromotion;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter  command for Updating/Editing Promotion Data 
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class UpdatePromotionDataCommand implements RafDsCommand {

	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request;
	/**
	 * Basic Constructor for Update Promotion data
	 * @param request
	 */
	public UpdatePromotionDataCommand(RafDsRequest request) {
		this.request = request;
	}
	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
	
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		rafDsResponse.setStatus(0);
		rafDsResponse.setStartRow(request.getStartRow());
		rafDsResponse.setTotalRows(0);
		rafDsResponse.setEndRow(request.getStartRow());
		
		List<VenPromotion> promotionList = new ArrayList<VenPromotion>();		
		List<HashMap<String,String >> dataList = request.getData();		
		VenPromotion entityPromotion = new VenPromotion();
				
		Locator<VenPromotionSessionEJBRemote> promotionLocator = null;
		
		try {
			promotionLocator = new Locator<VenPromotionSessionEJBRemote>();
			
			VenPromotionSessionEJBRemote sessionHome = (VenPromotionSessionEJBRemote) promotionLocator
			.lookup(VenPromotionSessionEJBRemote.class, "VenPromotionSessionEJBBean");
			
			for (int i=0;i<dataList.size();i++) {
				Map<String, String> data = dataList.get(i);
				
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();

					if (key.equals(DataNameTokens.VENPROMOTION_PROMOTIONID)) {
						try{
							entityPromotion = sessionHome.queryByRange("select o from VenPromotion o where o.promotionId="+new Long(data.get(DataNameTokens.VENPROMOTION_PROMOTIONID)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							entityPromotion.setPromotionId(new Long(data.get(DataNameTokens.VENPROMOTION_PROMOTIONID)));
						}
						break;
					}
				}						
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();

					if (key.equals(DataNameTokens.VENPROMOTION_PROMOTIONCODE)) {
						entityPromotion.setPromotionCode(data.get(key));
					} else if (key.equals(DataNameTokens.VENPROMOTION_PROMOTIONNAME)) {
						entityPromotion.setPromotionName(data.get(key));	
					} 
				}						
				
				promotionList.add(entityPromotion);			
			}
					
			sessionHome.mergeVenPromotionList((ArrayList<VenPromotion>)promotionList);
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			
			if (criteria == null) {
				String query = "select o from VenPromotion o";			
				promotionList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			} else {
				VenPromotion vp = new VenPromotion();
				promotionList = sessionHome.findByVenPromotionLike(vp, criteria, 0, 0);
			}
			
			
			for(int i=0; i<promotionList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				VenPromotion list = promotionList.get(i);
				
				map.put(DataNameTokens.VENPROMOTION_PROMOTIONID, list.getPromotionId()!=null?list.getPromotionId().toString():"");
				map.put(DataNameTokens.VENPROMOTION_PROMOTIONCODE, list.getPromotionCode());
				map.put(DataNameTokens.VENPROMOTION_PROMOTIONNAME, list.getPromotionName());
				
				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(promotionList.size());
			rafDsResponse.setEndRow(request.getStartRow()+promotionList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(promotionLocator!=null){
					promotionLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}
