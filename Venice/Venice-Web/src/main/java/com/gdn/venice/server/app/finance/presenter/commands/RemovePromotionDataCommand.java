/**
 * 
 */
package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenPromotionSessionEJBRemote;
import com.gdn.venice.persistence.VenPromotion;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

/**
 * This is Datasource-style presenter command use for removing promotion data.
 * <p>
 * <b>author:</b> <a href="mailto:christian.suwuh@pwsindonesia.com">Christian Suwuh</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class RemovePromotionDataCommand implements RafDsCommand {


	/*
	 * This is the data source request that is passed to 
	 * the command in the constructor. Originally it is
	 * built from the request body of the servlet using
	 * the XML parameters that are passed to the servlet
	 */
	RafDsRequest request = null;
	/**
	 * Basic Constructor for remove Promotion data
	 * @param request
	 */
	public RemovePromotionDataCommand(RafDsRequest rafDsRequest) {
		super();
		request = rafDsRequest;
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
		
		for(int i=0; i<dataList.size();i++){
			Map <String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();
			
			while (iter.hasNext()) {
				String key = iter.next();

				if (key.equals(DataNameTokens.VENPROMOTION_PROMOTIONID)) {
					entityPromotion.setPromotionId(new Long(data.get(DataNameTokens.VENPROMOTION_PROMOTIONID)));
				} 
			}						
			promotionList.add(entityPromotion);	
 
		}
		
		
		Locator<Object> promotionLocator = null;
		
		try {
			promotionLocator = new Locator<Object>();
			
			VenPromotionSessionEJBRemote sessionHome = (VenPromotionSessionEJBRemote) promotionLocator
			.lookup(VenPromotionSessionEJBRemote.class, "VenPromotionSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<promotionList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.VENPROMOTION_PROMOTIONID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(promotionList.get(i).getPromotionId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENPROMOTION_PROMOTIONID));
				criteria.add(simpleCriteria);
			}
			
			promotionList = sessionHome.findByVenPromotionLike(entityPromotion, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeVenPromotionList((ArrayList<VenPromotion>)promotionList);		
					
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(promotionList.size());
			rafDsResponse.setEndRow(request.getStartRow()+promotionList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally {
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
