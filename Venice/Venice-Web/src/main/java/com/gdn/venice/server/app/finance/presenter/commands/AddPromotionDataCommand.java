package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPromotionSessionEJBRemote;
import com.gdn.venice.persistence.VenPromotion;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter command use for adding promotion
 * 
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class AddPromotionDataCommand implements RafDsCommand {

	RafDsRequest request;
	
	/**
	 * Basic Constructor for Add Promotion data
	 * @param request
	 */
	public AddPromotionDataCommand(RafDsRequest request){
		this.request=request;
	}
	
	/* (non-Javadoc)
	 * @see com.gdn.venice.server.command.RafDsCommand#execute()
	 */
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		/*
		 * This is the list that will be used to add the promotion data later
		 */
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<VenPromotion> venPromotionLocator=null;
		
		try{
			venPromotionLocator = new Locator<VenPromotion>();
			VenPromotionSessionEJBRemote venPromotionSessionHome = (VenPromotionSessionEJBRemote) venPromotionLocator.lookup(VenPromotionSessionEJBRemote.class, "VenPromotionSessionEJBBean");
			dataList=request.getData();
			
			VenPromotion venPromotion = new VenPromotion();
			 
			for(int i=0;i< dataList.size();i++){
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter=data.keySet().iterator();
				
				while(iter.hasNext()){
					String key=iter.next();
					if(key.equals(DataNameTokens.VENPROMOTION_PROMOTIONID)){
						venPromotion.setPromotionId(new Long (data.get(key)));
					} else if(key.equals(DataNameTokens.VENPROMOTION_PROMOTIONCODE )){
						venPromotion.setPromotionCode(data.get(key));
					} else if(key.equals(DataNameTokens.VENPROMOTION_PROMOTIONNAME)){
						venPromotion.setPromotionName(data.get(key));
					}
				}
			}
					
			venPromotion=venPromotionSessionHome.persistVenPromotion(venPromotion);
			dataList=new ArrayList<HashMap<String, String>>();
			HashMap<String, String> map=new HashMap<String, String>();
			
			/*
			 * Map the data into a map for populating the dataList
			 */
			map.put(DataNameTokens.VENPROMOTION_PROMOTIONID, venPromotion.getPromotionId()!=null?venPromotion.getPromotionId().toString():"");
			map.put(DataNameTokens.VENPROMOTION_PROMOTIONCODE, venPromotion.getPromotionCode()!=null?venPromotion.getPromotionCode():"");
			map.put(DataNameTokens.VENPROMOTION_PROMOTIONNAME, venPromotion.getPromotionName()!=null?venPromotion.getPromotionName():"");
			dataList.add(map);
			
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(0);
			rafDsResponse.setTotalRows(1);			
			rafDsResponse.setEndRow(1);
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