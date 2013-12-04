package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenMerchantProductSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenProductCategory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseCategoryDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseCategoryDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			//Catch parameter from client
			String orderId= request.getParams().get(DataNameTokens.VENORDER_ORDERID);
			locator = new Locator<Object>();
			
			//Lookup into EJB for order item
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			VenMerchantProductSessionEJBRemote merchantProductSessionHome = (VenMerchantProductSessionEJBRemote) locator.lookup(VenMerchantProductSessionEJBRemote.class, "VenMerchantProductSessionEJBBean");
			List<VenOrderItem> orderItemList = null;
			
			//Calling facade for order item
			orderItemList = orderItemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.wcsOrderId = '" + orderId+"'", 0, 0);
			//Looping through order item to produce result
			for (int i = 0; i < orderItemList.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				VenOrderItem venOrderItem = orderItemList.get(i);	
				
				//Lookup into EJB for getting product category
				List<VenMerchantProduct> venMerchantProductList = merchantProductSessionHome.queryByRange("select o from VenMerchantProduct o join fetch o.venProductCategories where o.productId = " + venOrderItem.getVenMerchantProduct().getProductId(), 0, 0);
				
				List<VenProductCategory> productCategoryList = null;
				if(venMerchantProductList.size()>0){
					productCategoryList = venMerchantProductList.get(0).getVenProductCategories();
				}
				String category1 = "", category2="", category3="";
				if (productCategoryList != null) {
					for (int k = 0; k < productCategoryList.size(); k++) {
						if (productCategoryList.get(k).getLevel() == 1){
							category1 = productCategoryList.get(k).getProductCategory();
						}else if(productCategoryList.get(k).getLevel() == 2){
							category2 = productCategoryList.get(k).getProductCategory();
						}else if(productCategoryList.get(k).getLevel() == 3){
							category3 = productCategoryList.get(k).getProductCategory();
						}
					}
				}
				
				map.put(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY, "C1: "+category1+", C2: "+category2+", C3: "+category3);				
				map.put(DataNameTokens.VENORDERITEM_TOTAL, Util.isNull(venOrderItem.getTotal(), "").toString());
				dataList.add(map);
			}
			
			//Set DSResponse's properties
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setEndRow(request.getStartRow() + dataList.size());
			rafDsResponse.setTotalRows(dataList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if (locator != null) {
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}