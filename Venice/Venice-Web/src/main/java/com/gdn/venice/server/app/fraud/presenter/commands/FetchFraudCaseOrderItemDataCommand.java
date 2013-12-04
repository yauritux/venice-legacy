package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenMerchantProductSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAddress;
import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.gdn.venice.persistence.VenOrderItemContactDetail;
import com.gdn.venice.persistence.VenProductCategory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseOrderItemDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseOrderItemDataCommand(RafDsRequest request) {
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
			VenOrderItemAdjustmentSessionEJBRemote orderItemAdjustmentSessionHome = (VenOrderItemAdjustmentSessionEJBRemote) locator.lookup(VenOrderItemAdjustmentSessionEJBRemote.class, "VenOrderItemAdjustmentSessionEJBBean");
			VenOrderItemAddressSessionEJBRemote orderItemAddressSessionHome = (VenOrderItemAddressSessionEJBRemote) locator.lookup(VenOrderItemAddressSessionEJBRemote.class, "VenOrderItemAddressSessionEJBBean");
			VenOrderItemContactDetailSessionEJBRemote recipientContactDetailSessionHome = (VenOrderItemContactDetailSessionEJBRemote) locator.lookup(VenOrderItemContactDetailSessionEJBRemote.class, "VenOrderItemContactDetailSessionEJBBean");
			VenMerchantProductSessionEJBRemote merchantProductSessionHome = (VenMerchantProductSessionEJBRemote) locator.lookup(VenMerchantProductSessionEJBRemote.class, "VenMerchantProductSessionEJBBean");
			
			List<VenOrderItem> orderItemList = null;
			
			//Calling facade for order item
			orderItemList = orderItemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.wcsOrderId = '" + orderId+"'", 0, 0);
			//Looping through order item to produce result
			for (int i = 0; i < orderItemList.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				VenOrderItem venOrderItem = orderItemList.get(i);					
				List<VenOrderItemAdjustment> orderItemAdjustmentList = null;
				orderItemAdjustmentList = orderItemAdjustmentSessionHome.queryByRange("select o from VenOrderItemAdjustment o where o.venOrderItem.orderItemId = " + venOrderItem.getOrderItemId(), 0, 0);
				
				//Lookup into EJB for getting customer's phone				
				List<VenOrderItemContactDetail> contactDetailList = recipientContactDetailSessionHome.queryByRange("select o from VenOrderItemContactDetail o where o.venOrderItem.orderItemId="+venOrderItem.getOrderItemId(), 0, 0);
				String recipientMobile = "", recipientPhone="", recipientEmail="";
				for (int j = 0; j < contactDetailList.size(); j++) {
					if (contactDetailList.get(j).getVenContactDetail().getVenContactDetailType().getContactDetailTypeId() == 1){
						recipientMobile = contactDetailList.get(j).getVenContactDetail().getContactDetail();
					}else if(contactDetailList.get(j).getVenContactDetail().getVenContactDetailType().getContactDetailTypeId() == 0){
						recipientPhone = contactDetailList.get(j).getVenContactDetail().getContactDetail();
					}else if(contactDetailList.get(j).getVenContactDetail().getVenContactDetailType().getContactDetailTypeId() == 3){
						recipientEmail = contactDetailList.get(j).getVenContactDetail().getContactDetail();
					}
				}
				
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
				//Set all data into map and then put on data list
				map.put(DataNameTokens.VENORDERITEM_ORDERITEMID, Util.isNull(venOrderItem.getOrderItemId(), "").toString());				
				map.put(DataNameTokens.VENORDERITEM_WCSORDERITEMID, Util.isNull(venOrderItem.getWcsOrderItemId(), "").toString());
				map.put(DataNameTokens.VENORDERITEM_VENORDER_ORDERID, venOrderItem.getVenOrder()!=null && venOrderItem.getVenOrder().getOrderId()!=null?venOrderItem.getVenOrder().getOrderId().toString():"");
				map.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_VENPRODUCTTYPE_PRODUCTTYPEDESC, venOrderItem.getVenMerchantProduct()!=null && venOrderItem.getVenMerchantProduct().getVenProductType()!=null &&venOrderItem.getVenMerchantProduct().getVenProductType().getProductTypeDesc()!=null?venOrderItem.getVenMerchantProduct().getVenProductType().getProductTypeDesc().toString():"");
				map.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, venOrderItem.getVenMerchantProduct()!=null && venOrderItem.getVenMerchantProduct().getWcsProductName()!=null?venOrderItem.getVenMerchantProduct().getWcsProductName().toString():"");
				map.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU, venOrderItem.getVenMerchantProduct()!=null && venOrderItem.getVenMerchantProduct().getWcsProductSku()!=null?venOrderItem.getVenMerchantProduct().getWcsProductSku().toString():"");
				map.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_SUMMARY, 
						("<div style=\"float:left;width:100px\">Product Type</div>:&nbsp;" + venOrderItem.getVenMerchantProduct()!=null && venOrderItem.getVenMerchantProduct().getVenProductType()!=null && venOrderItem.getVenMerchantProduct().getVenProductType().getProductTypeDesc()!=null?venOrderItem.getVenMerchantProduct().getVenProductType().getProductTypeDesc().toString():"") + "<br />" +
						("<div style=\"float:left;width:100px\">Logistic Service</div>:&nbsp;" + venOrderItem.getLogLogisticService()!=null && venOrderItem.getLogLogisticService().getLogisticsServiceDesc()!=null?venOrderItem.getLogLogisticService().getLogisticsServiceDesc().toString():"") + "<br />" +
						("<div style=\"float:left;width:100px\">Product Name</div>:&nbsp;" + venOrderItem.getVenMerchantProduct()!=null && venOrderItem.getVenMerchantProduct().getWcsProductName()!=null?venOrderItem.getVenMerchantProduct().getWcsProductName().toString():"") + "<br />" +
						("<div style=\"float:left;width:100px\">Product SKU</div>:&nbsp;" + venOrderItem.getVenMerchantProduct()!=null && venOrderItem.getVenMerchantProduct().getWcsProductSku()!=null?venOrderItem.getVenMerchantProduct().getWcsProductSku().toString():"") + "<br />" +
						("<div style=\"float:left;width:100px\">Category C1:</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + category1) + "<br />" +
						("<div style=\"float:left;width:100px\">Category C2:</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + category2) + "<br />" +
						("<div style=\"float:left;width:100px\">Category C3:</div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + category3));
				map.put(DataNameTokens.VENORDERITEM_QUANTITY, Util.isNull(venOrderItem.getQuantity(), "").toString());
				map.put(DataNameTokens.VENORDERITEM_PRICE, Util.isNull(venOrderItem.getPrice(), "").toString());
				map.put(DataNameTokens.VENORDERITEM_TOTAL, Util.isNull(venOrderItem.getTotal(), "").toString());
				map.put(DataNameTokens.VENORDERITEM_SHIPPINGCOST, Util.isNull(venOrderItem.getShippingCost(), "").toString());
				map.put(DataNameTokens.VENORDERITEM_INSURANCECOST, Util.isNull(venOrderItem.getInsuranceCost(), "").toString());
				map.put(DataNameTokens.VENORDERITEMADJUSTMENT_AMOUNT, orderItemAdjustmentList.size() == 1 ? orderItemAdjustmentList.get(0).getAmount().toString() : "");
				map.put(DataNameTokens.VENORDERITEMADJUSTMENT_VENPROMOTION_PROMOCODE,  orderItemAdjustmentList.size() == 1 ? orderItemAdjustmentList.get(0).getVenPromotion().getPromotionCode().toString() : "");
				
				List<VenOrderItemAddress> orderItemAddressList = orderItemAddressSessionHome.queryByRange("select o from VenOrderItemAddress o where o.venOrderItem.orderItemId = "+venOrderItem.getOrderItemId(), 0, 1);
				VenOrderItemAddress venOrderItemAddressList = new VenOrderItemAddress();
				if(orderItemAddressList.size()>0){
					venOrderItemAddressList = orderItemAddressList.get(0);
				
					map.put(DataNameTokens.VENORDERITEM_VENADDRESS,
							"<b>" + (venOrderItem.getVenRecipient()!=null && venOrderItem.getVenRecipient().getVenParty()!=null && venOrderItem.getVenRecipient().getVenParty().getFullOrLegalName()!=null?venOrderItem.getVenRecipient().getVenParty().getFullOrLegalName().toString():"") + "</b><br />"+
							(recipientPhone !="" ? " Phone: " + recipientPhone + "</b><br />" : "") +
							(recipientMobile !="" ? " Mobile: " + recipientMobile + "</b><br />" : "") +
							(recipientEmail !="" ? " Email: " + recipientEmail + "</b><br />" : "") +
							(venOrderItemAddressList.getVenAddress()!=null && venOrderItemAddressList.getVenAddress().getStreetAddress1()!=null?venOrderItemAddressList.getVenAddress().getStreetAddress1().toString():"") +
							(" "+ venOrderItemAddressList.getVenAddress()!=null && venOrderItemAddressList.getVenAddress().getStreetAddress2()!=null?venOrderItemAddressList.getVenAddress().getStreetAddress2().toString():"") + "<br />" +
							(venOrderItemAddressList.getVenAddress()!=null && venOrderItemAddressList.getVenAddress().getKelurahan()!=null?venOrderItemAddressList.getVenAddress().getKelurahan().toString():"") + ", " +
							(venOrderItemAddressList.getVenAddress()!=null && venOrderItemAddressList.getVenAddress().getKecamatan()!=null?venOrderItemAddressList.getVenAddress().getKecamatan().toString():"") + "<br />" +
							(venOrderItemAddressList.getVenAddress()!=null && venOrderItemAddressList.getVenAddress().getVenCity()!=null?venOrderItemAddressList.getVenAddress().getVenCity().getCityName():"") + " " +
							(venOrderItemAddressList.getVenAddress()!=null && venOrderItemAddressList.getVenAddress().getPostalCode()!=null?venOrderItemAddressList.getVenAddress().getPostalCode().toString():"") + "<br />" +
							(venOrderItemAddressList.getVenAddress()!=null && venOrderItemAddressList.getVenAddress().getVenState()!=null ?venOrderItemAddressList.getVenAddress().getVenState().getStateName():"") + ", " +
							(venOrderItemAddressList.getVenAddress()!=null && venOrderItemAddressList.getVenAddress().getVenCountry()!=null && venOrderItemAddressList.getVenAddress().getVenCountry().getCountryName()!=null?venOrderItemAddressList.getVenAddress().getVenCountry().getCountryName().toString():""));
				}else{
					map.put(DataNameTokens.VENORDERITEM_VENADDRESS,
							"<b>" + (venOrderItem.getVenRecipient()!=null && venOrderItem.getVenRecipient().getVenParty()!=null && venOrderItem.getVenRecipient().getVenParty().getFullOrLegalName()!=null?venOrderItem.getVenRecipient().getVenParty().getFullOrLegalName().toString():"") +
							(recipientMobile !="" ? " (" + recipientMobile + ")" : ""));
				}
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