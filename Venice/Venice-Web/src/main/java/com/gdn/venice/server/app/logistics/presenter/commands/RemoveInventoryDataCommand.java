package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenMerchantProductSessionEJBRemote;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class RemoveInventoryDataCommand implements RafDsCommand {

	RafDsRequest request = null;
	
	
	public RemoveInventoryDataCommand(RafDsRequest rafDsRequest) {
		super();
		request = rafDsRequest;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		rafDsResponse.setStatus(0);
		rafDsResponse.setStartRow(request.getStartRow());
		rafDsResponse.setTotalRows(0);
		rafDsResponse.setEndRow(request.getStartRow());
		
		List<VenMerchantProduct> merchantProductList = new ArrayList<VenMerchantProduct>();		
		List<HashMap<String,String >> dataList = request.getData();		
		VenMerchantProduct entityMerchantProduct = new VenMerchantProduct();
		
		for(int i=0; i<dataList.size();i++){
			Map <String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();
			
			while (iter.hasNext()) {
				String key = iter.next();

				if (key.equals(DataNameTokens.VENMERCHANTPRODUCT_PRODUCTID)) {
					entityMerchantProduct.setProductId(new Long(data.get(DataNameTokens.VENMERCHANTPRODUCT_PRODUCTID)));
				} 
			}						
			merchantProductList.add(entityMerchantProduct);	
 
		}
		
		
		Locator<Object> merchantProductLocator = null;
		
		try {
			merchantProductLocator = new Locator<Object>();
			
			VenMerchantProductSessionEJBRemote sessionHome = (VenMerchantProductSessionEJBRemote) merchantProductLocator
			.lookup(VenMerchantProductSessionEJBRemote.class, "VenMerchantProductSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<merchantProductList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.VENMERCHANTPRODUCT_PRODUCTID);
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(merchantProductList.get(i).getProductId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENMERCHANTPRODUCT_PRODUCTID));
				criteria.add(simpleCriteria);
			}
			
			merchantProductList = sessionHome.findByVenMerchantProductLike(entityMerchantProduct, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeVenMerchantProductList((ArrayList<VenMerchantProduct>)merchantProductList);		
					
			/*
			 * Initialize the data source response settings
			 */
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(merchantProductList.size());
			rafDsResponse.setEndRow(request.getStartRow()+merchantProductList.size());
		} catch (Exception ex) {
			ex.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally {
			try {
				if(merchantProductLocator!=null){
					merchantProductLocator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}

}
