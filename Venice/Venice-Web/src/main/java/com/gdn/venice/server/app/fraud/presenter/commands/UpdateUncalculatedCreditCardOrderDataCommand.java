package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class UpdateUncalculatedCreditCardOrderDataCommand implements RafDsCommand  {
	RafDsRequest request;
	
	public UpdateUncalculatedCreditCardOrderDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList = request.getData();
		
		VenOrderPayment entityVenOrderPayment = new VenOrderPayment();
		String venOrderId = "";
		
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			
			VenOrderPaymentSessionEJBRemote orderPaymentSessionHome = (VenOrderPaymentSessionEJBRemote) locator.lookup(VenOrderPaymentSessionEJBRemote.class, "VenOrderPaymentSessionEJBBean");
			for (int i = 0; i < dataList.size(); i++) {
				Map<String, String> data = dataList.get(i);
				Iterator<String> iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();
					if (key.equals(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID)) {
						try{
							entityVenOrderPayment = orderPaymentSessionHome.queryByRange("select o from VenOrderPayment o where o.orderPaymentId="+new Long(data.get(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID)), 0, 1).get(0);
						}catch(IndexOutOfBoundsException e){
							entityVenOrderPayment.setOrderPaymentId(new Long(data.get(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID)));
						}
						break;
					}
				}
				
				iter = data.keySet().iterator();

				while (iter.hasNext()) {
					String key = iter.next();

					if (key.equals(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERID)) {
						venOrderId = data.get(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERID);
					}
					else if (key.equals(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_PAYMENTCONFIRMATIONNUMBER)) {
						entityVenOrderPayment.setPaymentConfirmationNumber(data.get(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_PAYMENTCONFIRMATIONNUMBER));
					}
				}
			}
			
			//Check whether data is available for edit (Data is not available for editing anymore if already reconciled with MIGS report)
			List<VenOrderPayment> venOrderPaymentList = null;
			String query = "select o from VenOrderPayment o " +
					       "where o.orderPaymentId = " + entityVenOrderPayment.getOrderPaymentId().toString() + " " +
					       		 "and (o.maskedCreditCardNumber is null or o.maskedCreditCardNumber = '')";

			venOrderPaymentList = orderPaymentSessionHome.queryByRange(query, 0, 0);
			
			if (venOrderPaymentList.size() == 1) {
				orderPaymentSessionHome.mergeVenOrderPayment(entityVenOrderPayment);
				
				VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
				List<VenOrderPaymentAllocation> venOrderPaymentAllocationList = null;
				query = "select o from VenOrderPaymentAllocation o " +
	                    "where o.venOrder.orderId = " + venOrderId + " " +
						      "and o.venOrderPayment.orderPaymentId = " + entityVenOrderPayment.getOrderPaymentId().toString();
				
				venOrderPaymentAllocationList = orderPaymentAllocationSessionHome.queryByRange(query, 0, 0);
				List<HashMap<String, String>> dataListResult = new ArrayList<HashMap<String, String>>();
				
				for (int i = 0; i < venOrderPaymentAllocationList.size(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					VenOrderPaymentAllocation venOrderPaymentAllocation = venOrderPaymentAllocationList.get(i);
					
					map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID, venOrderPaymentAllocation.getVenOrderPayment().getOrderPaymentId().toString());
					map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERID, venOrderPaymentAllocation.getVenOrder().getOrderId().toString());
					map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_WCSORDERID, venOrderPaymentAllocation.getVenOrder().getWcsOrderId());
					map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID, venOrderPaymentAllocation.getVenOrderPayment().getWcsPaymentId());
					map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT, venOrderPaymentAllocation.getVenOrderPayment().getAmount() != null ? venOrderPaymentAllocation.getVenOrderPayment().getAmount().toString() : "");
					map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_PAYMENTCONFIRMATIONNUMBER, venOrderPaymentAllocation.getVenOrderPayment().getPaymentConfirmationNumber());
					dataListResult.add(map);
				}

				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(dataListResult.size());
				rafDsResponse.setEndRow(request.getStartRow() + dataListResult.size());
				rafDsResponse.setData(dataListResult);
			}
			else {
				//Unable to edit auth. code that is already matched with MIGS report.
				rafDsResponse.setStatus(-4);
			}
		} catch (Exception e) {
			rafDsResponse.setStatus(-1);
			e.printStackTrace();
		} finally {
			try {
				if (locator != null) {
					locator.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return rafDsResponse;
	}
}