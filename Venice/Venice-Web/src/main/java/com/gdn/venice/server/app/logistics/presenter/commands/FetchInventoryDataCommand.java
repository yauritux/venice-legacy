package com.gdn.venice.server.app.logistics.presenter.commands;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenMerchantProductSessionEJBRemote;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchInventoryDataCommand implements RafDsCommand {

	RafDsRequest request;	
	
	public FetchInventoryDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();		
		Locator<VenMerchantProductSessionEJBRemote> merchantProductLocator = null;
		
		try {
			merchantProductLocator = new Locator<VenMerchantProductSessionEJBRemote>();
			
			VenMerchantProductSessionEJBRemote venMerchantProductHome = (VenMerchantProductSessionEJBRemote) merchantProductLocator
				.lookup(VenMerchantProductSessionEJBRemote.class, "VenMerchantProductSessionEJBBean");
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			
			List<VenMerchantProduct> merchantProductList = null;
			
			if(criteria == null){
				String query = "select o from VenMerchantProduct o where o.venMerchant.merchantId is null";
				
				merchantProductList = venMerchantProductHome.queryByRange(query, 0, 50);
			}else{
				VenMerchantProduct venMerchantProduct = new VenMerchantProduct();
				
				JPQLSimpleQueryCriteria andCriteria = new JPQLSimpleQueryCriteria();
				andCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENMERCHANTPRODUCT_COSTOFGOODSSOLD));
				andCriteria.setFieldName(DataNameTokens.VENMERCHANTPRODUCT_COSTOFGOODSSOLD);
				andCriteria.setValue("0");
				andCriteria.setOperator("greater");
				criteria.add(andCriteria);
				
				merchantProductList = venMerchantProductHome.findByVenMerchantProductLike(venMerchantProduct, criteria, 0, 0);
			}
			
			for (int i=0;i<merchantProductList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();				
				VenMerchantProduct venMerchantProduct = merchantProductList.get(i);				
								
				map.put(DataNameTokens.VENMERCHANTPRODUCT_PRODUCTID, venMerchantProduct.getProductId().toString());
				map.put(DataNameTokens.VENMERCHANTPRODUCT_WCSPRODUCTSKU, venMerchantProduct.getWcsProductSku());
				map.put(DataNameTokens.VENMERCHANTPRODUCT_WCSPRODUCTNAME, venMerchantProduct.getWcsProductName());
				map.put(DataNameTokens.VENMERCHANTPRODUCT_COSTOFGOODSSOLD, formatDouble(venMerchantProduct.getCostOfGoodsSold().doubleValue()));
				
				dataList.add(map);
			}
			
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(merchantProductList.size());
			rafDsResponse.setEndRow(request.getStartRow()+merchantProductList.size());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				merchantProductLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
	
	private String formatDouble(Double value) {
		NumberFormat nf = new DecimalFormat("#,###,###,###,###");
		return "Rp " + nf.format(value.doubleValue()).replace(',', '.');
	}

}
