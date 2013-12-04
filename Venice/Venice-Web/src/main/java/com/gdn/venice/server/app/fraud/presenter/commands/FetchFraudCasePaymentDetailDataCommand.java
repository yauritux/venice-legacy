package com.gdn.venice.server.app.fraud.presenter.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchFraudCasePaymentDetailDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCasePaymentDetailDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			//Catch parameter from client
			String fraudCaseId = request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);
			
			//Find ven order id first
			//Lookup into EJB for fraud case
			locator = new Locator<Object>();
			FrdFraudSuspicionCaseSessionEJBRemote fraudCaseSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
			List<FrdFraudSuspicionCase> fraudCaseList = null;
			String query = "select o from FrdFraudSuspicionCase o " +
					       "where o.fraudSuspicionCaseId = " + fraudCaseId;
			
			//Calling facade for fraud case
			fraudCaseList = fraudCaseSessionHome.queryByRange(query, 0, 0);
			
			//Find ven order id
			String orderId = "";
			for (int i = 0; i < fraudCaseList.size(); i++) {
				orderId = fraudCaseList.get(i).getVenOrder().getOrderId() == null ? "" : fraudCaseList.get(i).getVenOrder().getOrderId().toString();
			}
			
			//Lookup into EJB for order item
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			List<VenOrderItem> orderItemList = null;
			query = "select o from VenOrderItem o " +
					"where o.venOrder.orderId = " + orderId;
			
			//Calling facade for order item
			orderItemList = orderItemSessionHome.queryByRange(query, 0, 0);
			
			//Variable for result
			BigDecimal productPrice = new BigDecimal(0);
			BigDecimal shippingCost = new BigDecimal(0);
			BigDecimal insuranceCost = new BigDecimal(0);

			//Set result on temporary variable
			for (int i = 0; i < orderItemList.size(); i++) {
				productPrice = productPrice.add(new BigDecimal(Util.isNull(orderItemList.get(i).getTotal(), 0).toString()));
				shippingCost = shippingCost.add(new BigDecimal(Util.isNull(orderItemList.get(i).getShippingCost(), 0).toString()));
				insuranceCost = insuranceCost.add(new BigDecimal(Util.isNull(orderItemList.get(i).getInsuranceCost(), 0).toString()));
			}
			
			//Lookup into EJB for order item adjustment
			VenOrderItemAdjustmentSessionEJBRemote orderItemAdjustmentSessionHome = (VenOrderItemAdjustmentSessionEJBRemote) locator.lookup(VenOrderItemAdjustmentSessionEJBRemote.class, "VenOrderItemAdjustmentSessionEJBBean");
			List<VenOrderItemAdjustment> orderItemAdjustmentList = null;
			query = "select o from VenOrderItemAdjustment o " +
					"where o.venOrderItem.venOrder.orderId = " + orderId;
			
			//Calling facade for order item
			orderItemAdjustmentList = orderItemAdjustmentSessionHome.queryByRange(query, 0, 0);
			
			//Variable for result
			BigDecimal adjustment = new BigDecimal(0);
			
			//Set result on temporary variable
			for (int i = 0; i < orderItemAdjustmentList.size(); i++) {
				adjustment = adjustment.add(new BigDecimal(Util.isNull(orderItemAdjustmentList.get(i).getAmount(), 0).toString()));
			}
			
			//Lookup into EJB for order payment
			VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			List<VenOrderPaymentAllocation> orderPaymentAllocationList = null;
			query = "select o from VenOrderPaymentAllocation o " +
					"where o.venOrder.orderId = " + orderId;
			
			//Calling facade for order payment
			orderPaymentAllocationList = orderPaymentAllocationSessionHome.queryByRange(query, 0, 0);
			
			//Variable for result
			BigDecimal handlingFee = new BigDecimal(0);
			
			//Set result on temporary variable
			for (int i = 0; i < orderPaymentAllocationList.size(); i++) {
				handlingFee = handlingFee.add(new BigDecimal(orderPaymentAllocationList.get(i).getVenOrderPayment()!=null && orderPaymentAllocationList.get(i).getVenOrderPayment().getHandlingFee()!=null?orderPaymentAllocationList.get(i).getVenOrderPayment().getHandlingFee().toString():"0"));
			}
			
			//Variable for result
			BigDecimal totalTransaction = new BigDecimal(0);
			
			//Set result on temporary variable
			totalTransaction = totalTransaction.add(productPrice).add(shippingCost).add(insuranceCost).add(adjustment).add(handlingFee);
			
			//Final result
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(DataNameTokens.VENORDERITEM_PRODUCTPRICE, productPrice.toString());
			map.put(DataNameTokens.VENORDERITEM_SHIPPINGCOST, shippingCost.toString());
			map.put(DataNameTokens.VENORDERITEM_INSURANCECOST, insuranceCost.toString());
			map.put(DataNameTokens.VENORDERITEMADJUSTMENT_AMOUNT, adjustment.toString());
			map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_HANDLINGFEE, handlingFee.toString());
			map.put(DataNameTokens.VENORDERPAYMENT_TOTALTRANSACTION, totalTransaction.toString());
			dataList.add(map);

			//Set DSResponse's properties
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setEndRow(request.getStartRow() + 1);
			rafDsResponse.setTotalRows(1);
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