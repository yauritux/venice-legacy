package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.facade.VenDistributionCartSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchOrderLogisticsCartDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchOrderLogisticsCartDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<VenOrderItemSessionEJBRemote> orderItemLocator = null;
		Locator<VenDistributionCartSessionEJBRemote> distributionCartLocator = null;
		
		try {
//			orderItemLocator = new Locator<VenOrderItemSessionEJBRemote>();
//			
//			VenOrderItemSessionEJBRemote sessionHome = (VenOrderItemSessionEJBRemote) orderItemLocator
//			.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			
			//Find Order Item by the Order Id of the Order Item
//			String select = "select oi from VenOrderItem oi where oi.venOrder.orderId = " + request.getParams().get(DataNameTokens.VENORDER_ORDERID);
//			
//			List<VenOrderItem> orderItemList = sessionHome.queryByRange(select, 0, 0);
//			
//			distributionCartLocator = new Locator<VenDistributionCartSessionEJBRemote>();
			
//			VenDistributionCartSessionEJBRemote distributionCartSessionHome = (VenDistributionCartSessionEJBRemote) distributionCartLocator
//			.lookup(VenDistributionCartSessionEJBRemote.class, "VenDistributionCartSessionEJBBean");
//			
//			int numOfRecords=0;
//			
//			for (int i=0;i<orderItemList.size();i++) {
//				VenOrderItem orderItem = orderItemList.get(i);
//				
//				JPQLAdvancedQueryCriteria criteria = request.getCriteria()!=null?request.getCriteria():new JPQLAdvancedQueryCriteria();
//				criteria.setBooleanOperator("and");
//				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
//				simpleCriteria.setFieldName(DataNameTokens.VENDISTRIBUTIONCART_VENORDERITEM_ORDERITEMID);
//				simpleCriteria.setOperator("equals");
//				simpleCriteria.setValue(orderItem.getOrderItemId().toString());
//				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENDISTRIBUTIONCART_VENORDERITEM_ORDERITEMID));
//				criteria.add(simpleCriteria);
				
//				VenDistributionCart distributionCart = new VenDistributionCart();
					
//				List<VenDistributionCart>	distributionCartList = distributionCartSessionHome.findByVenDistributionCartLike(distributionCart, criteria, request.getStartRow(), request.getEndRow());
//				List<VenDistributionCart>	distributionCartList = distributionCartSessionHome.findByVenDistributionCartLike(distributionCart, criteria, 0, 0);

//				for (int j=0;j<distributionCartList.size();j++) {
//					HashMap<String, String> map = new HashMap<String, String>();
//					
//					distributionCart = distributionCartList.get(j);
//		
//					map.put(DataNameTokens.VENDISTRIBUTIONCART_DISTRIBUTIONCARTID, distributionCart.getDistributionCartId()!=null?distributionCart.getDistributionCartId().toString():"");
//					map.put(DataNameTokens.VENDISTRIBUTIONCART_VENORDERITEM_WCSORDERITEMID, distributionCart.getVenOrderItem()!=null?distributionCart.getVenOrderItem().getWcsOrderItemId():"");
//					map.put(DataNameTokens.VENDISTRIBUTIONCART_DCSEQUENCE, distributionCart.getDcSequence()!=null?distributionCart.getDcSequence().toString():"");
//					map.put(DataNameTokens.VENDISTRIBUTIONCART_PACKAGEWEIGHT, distributionCart.getPackageWeight()!=null?distributionCart.getPackageWeight().toString():"");
//					map.put(DataNameTokens.VENDISTRIBUTIONCART_QUANTITY, distributionCart.getQuantity()!=null?distributionCart.getQuantity().toString():"");
//					
//					dataList.add(map);
//					numOfRecords++;
//				}
//			}
			
			
//			rafDsResponse.setStatus(0);
//			rafDsResponse.setStartRow(request.getStartRow());
//			rafDsResponse.setTotalRows(numOfRecords);
//			rafDsResponse.setEndRow(request.getStartRow()+numOfRecords);
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(orderItemLocator != null)
					orderItemLocator.close();
				if(distributionCartLocator != null)
					distributionCartLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}

}
