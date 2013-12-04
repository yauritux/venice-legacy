package com.gdn.venice.server.app.general.presenter.commands;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.persistence.VenOrderItemStatusHistoryPK;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.gdn.venice.persistence.VenOrderStatusHistoryPK;
import com.gdn.venice.server.command.RafRpcCommand;

public class UpdateOrderStatusDataCommand implements RafRpcCommand {
	
	String orderId;
	String method;
	String username;
	
	public UpdateOrderStatusDataCommand(String orderId, String method, String username) {
		this.orderId = orderId;
		this.method = method;
		this.username = username;
	}

	@Override
	public String execute() {
		//Create a new order object and update the order				
		VenOrder order;
		
		VenOrderStatus venOrderStatus = new VenOrderStatus();
		if (method.equals("updateOrderStatusToSF")) {
			venOrderStatus.setOrderStatusId(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_SF);
		} else if (method.equals("updateOrderStatusToFP")) {
			venOrderStatus.setOrderStatusId(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FP);
		} else if (method.equals("updateOrderStatusToFC")) {
			venOrderStatus.setOrderStatusId(DataConstantNameTokens.VENORDERSTATUS_ORDERSTATUSID_FC);
		}
				
		//Update the order here
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			VenOrderSessionEJBRemote sessionHome = (VenOrderSessionEJBRemote) locator
				.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			order = sessionHome.queryByRange("select o from VenOrder o where o.orderId = " + orderId, 0, 1).get(0);
			order.setVenOrderStatus(venOrderStatus);
			
			sessionHome.mergeVenOrder(order);			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "-1";
		} finally {
			try {
				locator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//Now update the order items based on originalOrderItemList
		Locator<VenOrderItem> orderItemLocator = null;
		
		try {
			orderItemLocator = new Locator<VenOrderItem>();			
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) orderItemLocator
			.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			
			String select = "select oi from VenOrderItem oi where oi.venOrder.orderId=" + orderId;
			
			List<VenOrderItem> originalOrderItemList = orderItemSessionHome.queryByRange(select, 0,0);			
			ArrayList<VenOrderItem> orderItemList = new ArrayList<VenOrderItem>();
			
			for (int i=0;i<originalOrderItemList.size();i++) {
				VenOrderItem orderItem = new VenOrderItem();
				orderItem = originalOrderItemList.get(i);
				
				orderItem.setVenOrderStatus(venOrderStatus);				
				orderItemList.add(orderItem);
			}
			
			orderItemSessionHome.mergeVenOrderItemList(orderItemList);			
			
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				orderItemLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "-1";
		} finally {
			try {
				orderItemLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		
		//now add history order and order item
		Locator<Object> historyLocator = null;
		
		try {
			System.out.println("start add order history");
			historyLocator = new Locator<Object>();
			VenOrderStatusHistorySessionEJBRemote orderHistorySessionHome = (VenOrderStatusHistorySessionEJBRemote) historyLocator
			.lookup(VenOrderStatusHistorySessionEJBRemote.class, "VenOrderStatusHistorySessionEJBBean");
			
			VenOrderItemStatusHistorySessionEJBRemote orderItemHistorySessionHome = (VenOrderItemStatusHistorySessionEJBRemote) historyLocator
			.lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");
			
			VenOrderStatusHistoryPK venOrderStatusHistoryPK = new VenOrderStatusHistoryPK();
			venOrderStatusHistoryPK.setOrderId(new Long(orderId));
			venOrderStatusHistoryPK.setHistoryTimestamp(new Timestamp(System.currentTimeMillis()));
			
			VenOrderStatusHistory orderStatusHistory = new VenOrderStatusHistory();
			orderStatusHistory.setId(venOrderStatusHistoryPK);
			orderStatusHistory.setStatusChangeReason("Updated by " + username);
			orderStatusHistory.setVenOrderStatus(venOrderStatus);
			
//			System.out.println("order id: "+new Long(orderId));
//			System.out.println("username: "+username);
//			System.out.println("status: "+venOrderStatus.getOrderStatusId());
			
			orderHistorySessionHome.persistVenOrderStatusHistory(orderStatusHistory);	
						
			System.out.println("start add order item history");
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) historyLocator
			.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			
			String select = "select oi from VenOrderItem oi where oi.venOrder.orderId=" + orderId;
			
			List<VenOrderItem> originalOrderItemList = orderItemSessionHome.queryByRange(select, 0,0);					
			for (int i=0;i<originalOrderItemList.size();i++) {				
				VenOrderItemStatusHistoryPK venOrderItemStatusHistoryPK = new VenOrderItemStatusHistoryPK();
				venOrderItemStatusHistoryPK.setOrderItemId(originalOrderItemList.get(i).getOrderItemId());
				venOrderItemStatusHistoryPK.setHistoryTimestamp(new Timestamp(System.currentTimeMillis()));
				
				VenOrderItemStatusHistory orderItemStatusHistory = new VenOrderItemStatusHistory();
				orderItemStatusHistory.setId(venOrderItemStatusHistoryPK);
				orderItemStatusHistory.setStatusChangeReason("Updated by " + username);
				orderItemStatusHistory.setVenOrderStatus(venOrderStatus);
				
//				System.out.println("order item id: "+originalOrderItemList.get(i).getOrderItemId());
//				System.out.println("username: "+username);
//				System.out.println("status: "+venOrderStatus.getOrderStatusId());
				
				orderItemHistorySessionHome.persistVenOrderItemStatusHistory(orderItemStatusHistory);	
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				historyLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "-1";
		} finally {
			try {
				historyLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
		return "0";
	}
}

