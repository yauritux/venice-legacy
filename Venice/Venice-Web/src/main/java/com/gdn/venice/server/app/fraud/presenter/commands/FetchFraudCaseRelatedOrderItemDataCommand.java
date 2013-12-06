package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote;
import com.gdn.venice.facade.VenContactDetailSessionEJBRemote;
import com.gdn.venice.facade.VenMerchantProductSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudRelatedOrderInfo;
import com.gdn.venice.persistence.VenContactDetail;
import com.gdn.venice.persistence.VenMerchantProduct;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenProductCategory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseRelatedOrderItemDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseRelatedOrderItemDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			FrdFraudRelatedOrderInfoSessionEJBRemote sessionHome = (FrdFraudRelatedOrderInfoSessionEJBRemote) locator.lookup(FrdFraudRelatedOrderInfoSessionEJBRemote.class, "FrdFraudRelatedOrderInfoSessionEJBBean");
			
			List<FrdFraudRelatedOrderInfo> relatedOrderList = sessionHome.queryByRange("select o from FrdFraudRelatedOrderInfo o where o.frdFraudSuspicionCase.fraudSuspicionCaseId = " + request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID), request.getStartRow(), request.getEndRow());
			for (int i=0;i<relatedOrderList.size();i++) {
				Long orderId = (Long) relatedOrderList.get(i).getVenOrder().getOrderId();
				
				VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
				
				JPQLAdvancedQueryCriteria orderItemCriteria = new JPQLAdvancedQueryCriteria();
				orderItemCriteria.setBooleanOperator("and");
				JPQLSimpleQueryCriteria orderIdCriteria = new JPQLSimpleQueryCriteria();
				orderIdCriteria.setFieldName(DataNameTokens.VENORDERITEM_VENORDER_ORDERID);
				orderIdCriteria.setOperator("equals");
				orderIdCriteria.setValue(orderId.toString());
				orderIdCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENORDERITEM_VENORDER_ORDERID));
				orderItemCriteria.add(orderIdCriteria);
				
				VenOrderItem venOrderItem = new VenOrderItem();
				List<VenOrderItem> orderItemList = orderItemSessionHome.findByVenOrderItemLike(venOrderItem, orderItemCriteria, 0, 0);
				
				
				for (int j=0;j<orderItemList.size();j++) {
					HashMap<String, String> orderItemMap = new HashMap<String, String>();
					venOrderItem = orderItemList.get(j);
					
					Long partyId = venOrderItem.getVenRecipient().getVenParty().getPartyId();
					VenContactDetailSessionEJBRemote contactDetailSessionHome = (VenContactDetailSessionEJBRemote) locator.lookup(VenContactDetailSessionEJBRemote.class, "VenContactDetailSessionEJBBean");
					
					JPQLAdvancedQueryCriteria criteria = request.getCriteria()!=null?request.getCriteria():new JPQLAdvancedQueryCriteria();
					criteria.setBooleanOperator("and");
					JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
					simpleCriteria.setFieldName(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID);
					simpleCriteria.setOperator("equals");
					simpleCriteria.setValue(partyId.toString());
					simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENCONTACTDETAIL_VENPARTY_PARTYID));
					criteria.add(simpleCriteria);				
		
					VenContactDetail contactDetail = new VenContactDetail();
					List<VenContactDetail> contactDetailList = contactDetailSessionHome.findByVenContactDetailLike(contactDetail, criteria, request.getStartRow(), request.getEndRow());
					String customerMobile = "";
					for (int k=0;k<contactDetailList.size();k++) {
						contactDetail = contactDetailList.get(k);
						
						if (contactDetail.getVenContactDetailType().getContactDetailTypeId() == 1)
							customerMobile = contactDetail.getContactDetail();
					}
					
					VenMerchantProductSessionEJBRemote merchantProductSessionHome = (VenMerchantProductSessionEJBRemote) locator.lookup(VenMerchantProductSessionEJBRemote.class, "VenMerchantProductSessionEJBBean");
					List<VenMerchantProduct> venMerchantProductList = merchantProductSessionHome.queryByRange("select o from VenMerchantProduct o join fetch o.venProductCategories where o.productId = " + venOrderItem.getVenMerchantProduct().getProductId(), 0, 0);
					List<VenProductCategory> productCategoryList = venMerchantProductList.get(0).getVenProductCategories();
					String category = "";
					if (productCategoryList != null) {
						for (int l = 0; l < productCategoryList.size(); l++) {
							if (productCategoryList.get(l).getLevel() == 3)
								category = productCategoryList.get(l).getProductCategory().toString();
						}
					}
					
					orderItemMap.put(DataNameTokens.VENORDERITEM_ORDERITEMID, Util.isNull(venOrderItem.getOrderItemId(), "").toString());				
					orderItemMap.put(DataNameTokens.VENORDERITEM_WCSORDERITEMID, Util.isNull(venOrderItem.getWcsOrderItemId(), "").toString());
					orderItemMap.put(DataNameTokens.VENORDER_WCSORDERID, Util.isNull(venOrderItem.getVenOrder().getWcsOrderId(), "").toString());
					orderItemMap.put(DataNameTokens.VENORDERITEM_VENORDER_ORDERID, venOrderItem.getVenOrder()!=null && venOrderItem.getVenOrder().getOrderId()!=null?venOrderItem.getVenOrder().getOrderId().toString():"");
					orderItemMap.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_VENPRODUCTTYPE_PRODUCTTYPEDESC, venOrderItem.getVenMerchantProduct()!=null && venOrderItem.getVenMerchantProduct().getVenProductType()!=null && venOrderItem.getVenMerchantProduct().getVenProductType().getProductTypeDesc()!=null?venOrderItem.getVenMerchantProduct().getVenProductType().getProductTypeDesc().toString():"");
					orderItemMap.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, venOrderItem.getVenMerchantProduct()!=null && venOrderItem.getVenMerchantProduct().getWcsProductName()!=null?venOrderItem.getVenMerchantProduct().getWcsProductName().toString():"");
					orderItemMap.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTSKU, venOrderItem.getVenMerchantProduct()!=null && venOrderItem.getVenMerchantProduct().getWcsProductSku()!=null?venOrderItem.getVenMerchantProduct().getWcsProductSku().toString():"");
					orderItemMap.put(DataNameTokens.VENPRODUCTCATEGORY_PRODUCTCATEGORY, category);
					orderItemMap.put(DataNameTokens.VENORDERITEM_QUANTITY, Util.isNull(venOrderItem.getQuantity(), "").toString());
					orderItemMap.put(DataNameTokens.VENORDERITEM_PRICE, Util.isNull(venOrderItem.getPrice(), "").toString());
					orderItemMap.put(DataNameTokens.VENORDERITEM_TOTAL, Util.isNull(venOrderItem.getTotal(), "").toString());
					orderItemMap.put(DataNameTokens.VENORDERITEM_VENADDRESS,
							"<b>" + venOrderItem.getVenRecipient()!=null && venOrderItem.getVenRecipient().getVenParty()!=null && venOrderItem.getVenRecipient().getVenParty().getFullOrLegalName()!=null?venOrderItem.getVenRecipient().getVenParty().getFullOrLegalName().toString():"" +
							(customerMobile != "" ? " (" + customerMobile + ")" : "") + "</b><br />" +
							(venOrderItem.getVenAddress()!=null && venOrderItem.getVenAddress().getStreetAddress1()!=null?venOrderItem.getVenAddress().getStreetAddress1().toString():"") +
							(" "+ venOrderItem.getVenAddress()!=null && venOrderItem.getVenAddress().getStreetAddress2()!=null?venOrderItem.getVenAddress().getStreetAddress2().toString():"") + "<br />" +
							(venOrderItem.getVenAddress()!=null && venOrderItem.getVenAddress().getKelurahan()!=null?venOrderItem.getVenAddress().getKelurahan().toString():"") + ", " +
							(venOrderItem.getVenAddress()!=null && venOrderItem.getVenAddress().getKecamatan()!=null?venOrderItem.getVenAddress().getKecamatan().toString():"") + "<br />" +
							(venOrderItem.getVenAddress()!=null && venOrderItem.getVenAddress().getVenCity()!=null ?venOrderItem.getVenAddress().getVenCity().getCityName():"") + " " +
							(venOrderItem.getVenAddress()!=null && venOrderItem.getVenAddress().getPostalCode()!=null?venOrderItem.getVenAddress().getPostalCode().toString():"") + "<br />" +
							(venOrderItem.getVenAddress()!=null && venOrderItem.getVenAddress().getVenState()!=null ?venOrderItem.getVenAddress().getVenState().getStateName():"") + ", " +
							(venOrderItem.getVenAddress()!=null && venOrderItem.getVenAddress().getVenCountry()!=null && venOrderItem.getVenAddress().getVenCountry().getCountryName()!=null?venOrderItem.getVenAddress().getVenCountry().getCountryName().toString():""));
												
					dataList.add(orderItemMap);
				}
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
