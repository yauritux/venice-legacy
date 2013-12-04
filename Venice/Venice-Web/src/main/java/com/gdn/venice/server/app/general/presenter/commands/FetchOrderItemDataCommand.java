package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceTextField;

public class FetchOrderItemDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchOrderItemDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<VenOrderItemSessionEJBRemote> orderItemLocator = null;
		
		try {
			orderItemLocator = new Locator<VenOrderItemSessionEJBRemote>();
			
			VenOrderItemSessionEJBRemote sessionHome = (VenOrderItemSessionEJBRemote) orderItemLocator
			.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria()!=null?request.getCriteria():new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("and");
			JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
			simpleCriteria.setFieldName(DataNameTokens.VENORDERITEM_VENORDER_ORDERID);
			simpleCriteria.setOperator("equals");
			simpleCriteria.setValue(request.getParams().get(DataNameTokens.VENORDER_ORDERID));
			simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENORDERITEM_VENORDER_ORDERID));
			criteria.add(simpleCriteria);
			
			VenOrderItem orderItem = new VenOrderItem();
			
//			List<VenOrderItem> orderItemList  = sessionHome.findByVenOrderItemLike(orderItem, criteria, request.getStartRow(), request.getEndRow());
			List<VenOrderItem> orderItemList  = sessionHome.findByVenOrderItemLike(orderItem, criteria, 0, 0);
			
			for (int i=0;i<orderItemList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				orderItem = orderItemList.get(i);
				
				map.put(DataNameTokens.VENORDER_WCSORDERID, orderItem.getVenOrder()!=null?orderItem.getVenOrder().getWcsOrderId():"");
				map.put(DataNameTokens.VENORDER_ORDERID, orderItem.getVenOrder()!=null?orderItem.getVenOrder().getOrderId().toString():"");
				map.put(DataNameTokens.VENORDERITEM_ORDERITEMID, orderItem.getOrderItemId()!=null?orderItem.getOrderItemId().toString():"");
				map.put(DataNameTokens.VENORDERITEM_WCSORDERITEMID, orderItem.getWcsOrderItemId());
				map.put(DataNameTokens.VENORDERITEM_VENORDERSTATUS_ORDERSTATUSCODE, orderItem.getVenOrderStatus()!=null?orderItem.getVenOrderStatus().getOrderStatusCode():"");
				map.put(DataNameTokens.VENORDERITEM_VENMERCHANTPRODUCT_WCSPRODUCTNAME, orderItem.getVenMerchantProduct()!=null?orderItem.getVenMerchantProduct().getWcsProductName():"");
				map.put(DataNameTokens.VENORDERITEM_QUANTITY, orderItem.getQuantity()!=null?orderItem.getQuantity().toString():"");
				map.put(DataNameTokens.VENORDERITEM_TOTAL, orderItem.getTotal()!=null?orderItem.getTotal().toString():"");
				
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(orderItemList.size());
			rafDsResponse.setEndRow(request.getStartRow()+orderItemList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				orderItemLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}

}
