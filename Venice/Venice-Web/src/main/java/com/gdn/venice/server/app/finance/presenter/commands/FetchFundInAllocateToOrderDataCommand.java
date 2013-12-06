package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.util.VeniceConstants;

public class FetchFundInAllocateToOrderDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public FetchFundInAllocateToOrderDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
		JPQLAdvancedQueryCriteria criteria = request.getCriteria();
		
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<VenOrder> orderLocator = null;
		
		try {
			orderLocator = new Locator<VenOrder>();
			
			VenOrderSessionEJBRemote sessionHome = (VenOrderSessionEJBRemote) orderLocator
			.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			List<VenOrder> orderList = null;
			
			VenOrder order = new VenOrder();

			if (criteria!=null) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE);
				simpleCriteria.setOperator("notEquals");
				simpleCriteria.setValue(""+VeniceConstants.VEN_ORDER_STATUS_CODE);
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE));
				criteria.add(simpleCriteria);			
				orderList = sessionHome.findByVenOrderLike(order, criteria, 0, 0);
			} else {
				String select = "select o from VenOrder o where o.venOrderStatus.orderStatusCode<>'"+VeniceConstants.VEN_ORDER_STATUS_CODE+"'";
				
//				orderList = sessionHome.queryByRange(select, request.getStartRow(), request.getEndRow());
				orderList = sessionHome.queryByRange(select, 0, 0);
			}
			
			
			for (int i=0;i<orderList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				order = orderList.get(i);
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
				
				map.put(DataNameTokens.VENORDER_ORDERID, (order.getOrderId()!=null)?order.getOrderId().toString():"");
				map.put(DataNameTokens.VENORDER_WCSORDERID, order.getWcsOrderId());
				map.put(DataNameTokens.VENORDER_FULFILLMENTSTATUS, order.getFulfillmentStatus()!=null?order.getFulfillmentStatus().toString():"");
				map.put(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, order.getVenOrderStatus()!=null?order.getVenOrderStatus().getOrderStatusCode():"");
				map.put(DataNameTokens.VENORDER_ORDERDATE, order.getOrderDate()!=null?formatter.format(order.getOrderDate()):"");

				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(orderList.size());
			rafDsResponse.setEndRow(request.getStartRow()+orderList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				orderLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}
	
}
