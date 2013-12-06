package com.gdn.venice.server.app.fraud.presenter.commands;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseOrderDetailDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseOrderDetailDataCommand(RafDsRequest request) {
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
			String orderId= request.getParams().get(DataNameTokens.VENORDER_ORDERID);				
					
			//Find ven order id first
			//Lookup into EJB for fraud case
			locator = new Locator<Object>();
			if(!fraudCaseId.equals("")){
					FrdFraudSuspicionCaseSessionEJBRemote fraudCaseSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
					List<FrdFraudSuspicionCase> fraudCaseList = null;
					
					//Calling facade for fraud case
					fraudCaseList = fraudCaseSessionHome.queryByRange("select o from FrdFraudSuspicionCase o where o.fraudSuspicionCaseId = " + fraudCaseId, 0, 0);
					
					//Find ven order id
					
					for (int i = 0; i < fraudCaseList.size(); i++) {
						orderId = fraudCaseList.get(i).getVenOrder().getOrderId() == null ? "" : fraudCaseList.get(i).getVenOrder().getOrderId().toString();
					}
			}
			//Lookup into EJB for order
			VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			List<VenOrder> orderList = null;
			
			//Calling facade for order item
			orderList = orderSessionHome.queryByRange("select o from VenOrder o where o.orderId = " + orderId, 0, 1);
			
			//Looping through order to produce result
			HashMap<String, String> map = new HashMap<String, String>();
			DateToXsdDatetimeFormatter formatter =  new DateToXsdDatetimeFormatter();
			BigDecimal amount = new BigDecimal(0);
			for (int i = 0; i < orderList.size(); i++) {
				map.put(DataNameTokens.VENORDER_WCSORDERID, Util.isNull(orderList.get(i).getWcsOrderId(), "").toString());
				map.put(DataNameTokens.VENORDER_RMAFLAG, Util.isNull(orderList.get(i).getRmaFlag(), "false").toString());
				
				//get amount from payment allocation
				List<VenOrderPaymentAllocation> allocationList = orderPaymentAllocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId="+orderId, 0, 0);
				if(allocationList.size()>0){
					for (int j = 0; j < allocationList.size(); j++) {
						amount=amount.add(allocationList.get(j).getAllocationAmount());
						amount=amount.add(allocationList.get(j).getVenOrderPayment().getHandlingFee());
					}
									}
				map.put(DataNameTokens.VENORDER_AMOUNT, amount.toString());
				map.put(DataNameTokens.VENORDER_ORDERDATE, formatter.format(orderList.get(i).getOrderDate()));
				map.put(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSCODE, orderList.get(i).getVenOrderStatus()!=null && orderList.get(i).getVenOrderStatus().getOrderStatusCode()!=null?orderList.get(i).getVenOrderStatus().getOrderStatusCode().toString():"");
				map.put(DataNameTokens.VENORDER_IPADDRESS, Util.isNull(orderList.get(i).getIpAddress(), "").toString());
				map.put(DataNameTokens.VENORDER_FULFILLMENTSTATUS, Util.isNull(orderList.get(i).getFulfillmentStatus(), "").toString());
				map.put(DataNameTokens.VENORDER_VENCUSTOMER_FIRSTTIMETRANSCTIONFLAG, orderList.get(i).getVenCustomer()!=null && orderList.get(i).getVenCustomer().getFirstTimeTransactionFlag()!=null?orderList.get(i).getVenCustomer().getFirstTimeTransactionFlag().toString():"false");
			}
			
			//Lookup into EJB for order item
			VenOrderItemSessionEJBRemote orderItemSessionHome = (VenOrderItemSessionEJBRemote) locator.lookup(VenOrderItemSessionEJBRemote.class, "VenOrderItemSessionEJBBean");
			List<VenOrderItem> orderItemList = null;
			
			//Calling facade for order item
			orderItemList = orderItemSessionHome.queryByRange("select o from VenOrderItem o where o.venOrder.orderId = " + orderId, 0, 0);
			
			//Variable for result
			BigDecimal productPrice = new BigDecimal(0);

			//Set result on temporary variable
			for (int i = 0; i < orderItemList.size(); i++) {
				productPrice = productPrice.add(orderItemList.get(i).getTotal());
			}
			
			map.put(DataNameTokens.VENORDERITEM_PRODUCTPRICE, productPrice.toString());
			dataList.add(map);

			//Set DSResponse's properties
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(1);
			rafDsResponse.setEndRow(request.getStartRow() + 1);
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