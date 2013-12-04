package com.gdn.venice.server.app.general.presenter.commands;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataConstantNameTokens;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderBlockingSource;
import com.gdn.venice.persistence.VenOrderStatus;
import com.gdn.venice.server.command.RafRpcCommand;
import com.gdn.venice.server.util.Util;

public class UpdateOrderBlockDataCommand implements RafRpcCommand {
	
	HashMap<String, String> blockData;
	
	public UpdateOrderBlockDataCommand(String parameter) {
		blockData = Util.formHashMapfromXML(parameter);
	}

	@Override
	public String execute() {
		//Create a new order object and update the order
		Locator<VenOrder> orderLocator = null;
		VenOrderSessionEJBRemote sessionHome = null;
		
		try {
			orderLocator = new Locator<VenOrder>();
			sessionHome = (VenOrderSessionEJBRemote) orderLocator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		VenOrder order;
		try{
			order = sessionHome.queryByRange("select o from VenOrder o where o.orderId="+new Long(blockData.get("orderId")), 0, 1).get(0);
		}catch(IndexOutOfBoundsException e){
			order = new VenOrder();
			order.setOrderId(new Long(blockData.get("orderId")));
			System.out.println("order id: "+order.getOrderId());		
		}
		System.out.println("blockingSource: "+blockData.get("blockSource"));
		VenOrderBlockingSource orderBlockingSource = new VenOrderBlockingSource();
		if (blockData.get("blockSource").equals(DataConstantNameTokens.VENORDER_BLOCKINGSOURCEDESC_FRD)) {
			orderBlockingSource.setBlockingSourceId(DataConstantNameTokens.VENORDER_BLOCKINGSOURCEID_FRD);
			orderBlockingSource.setBlockingSourceDesc(DataConstantNameTokens.VENORDER_BLOCKINGSOURCEDESC_FRD);
		} else if (blockData.get("blockSource").equals(DataConstantNameTokens.VENORDER_BLOCKINGSOURCEDESC_FIN)) {
			orderBlockingSource.setBlockingSourceId(DataConstantNameTokens.VENORDER_BLOCKINGSOURCEID_FIN);
			orderBlockingSource.setBlockingSourceDesc(DataConstantNameTokens.VENORDER_BLOCKINGSOURCEDESC_FIN);
		}
		
		order.setVenOrderBlockingSource(orderBlockingSource);		
		order.setBlockedFlag(new Boolean(blockData.get("blockFlag")));
		order.setBlockedTimestamp(new Timestamp(new Date().getTime()));		
		order.setBlockedReason(blockData.get("blockReason"));
		
		VenOrderStatus status = new VenOrderStatus();
		status.setOrderStatusId(new Long(blockData.get("orderStatus")));
		order.setVenOrderStatus(status);
		
		//Update the order here		
		try {
			sessionHome.mergeVenOrder(order);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				orderLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "-1";
		} finally {
			try {
				orderLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return "0";
	}
}

