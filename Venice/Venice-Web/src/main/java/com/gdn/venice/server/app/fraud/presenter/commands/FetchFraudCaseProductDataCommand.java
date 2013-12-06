package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenMerchantProductSessionEJBRemote;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseProductDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseProductDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			VenMerchantProductSessionEJBRemote sessionHome = (VenMerchantProductSessionEJBRemote) locator.lookup(VenMerchantProductSessionEJBRemote.class, "VenMerchantProductSessionEJBBean");
			List<VenMerchantProduct> venMerchantProductList = null;
			if (request.getParams().get(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY) == "") {
				venMerchantProductList = sessionHome.queryByRange("select o from VenMerchantProduct o", request.getStartRow(), request.getEndRow());
			} else {
				venMerchantProductList = sessionHome.queryByRange("select o from VenMerchantProduct o inner join o.venProductCategories p where p.productCategory = '" + request.getParams().get(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY) + "'", request.getStartRow(), request.getEndRow());
			}			
			VenMerchantProduct venMerchantProduct = new VenMerchantProduct();
			for (int i=0;i<venMerchantProductList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				venMerchantProduct = venMerchantProductList.get(i);
				
				map.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_PRODUCTID, Util.isNull(venMerchantProduct.getProductId(), "").toString());
				map.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, Util.isNull(venMerchantProduct.getWcsProductName(), "").toString());				
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(locator!=null){
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
