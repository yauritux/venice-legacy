package com.gdn.venice.server.app.finance.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchFundInAllocateToOrderPaymentDataCommand implements RafDsCommand {
	RafDsRequest request;
	String userName;
	
	public FetchFundInAllocateToOrderPaymentDataCommand(RafDsRequest request, String userName) {
		this.request = request;
		this.userName = userName;
	}

	@Override
	public RafDsResponse execute() {
		String orderId = request.getParams().get(DataNameTokens.VENORDERPAYMENT_VENORDERPAYMENTALLOCATION_VENORDER_ORDERID);
		String allocationAmount = request.getParams().get("AllocationAmount");
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		if (orderId!=null) {
			List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
			
			Locator<VenOrderPaymentAllocation> venOrderPaymentAllocationLocator = null;
			
			try {
				venOrderPaymentAllocationLocator = new Locator<VenOrderPaymentAllocation>();
				
				VenOrderPaymentAllocationSessionEJBRemote venOrderPaymentAllocationHome = (VenOrderPaymentAllocationSessionEJBRemote) venOrderPaymentAllocationLocator
				.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			
							
				VenOrderPayment orderPayment = new VenOrderPayment();
				
				String query = "Select o from VenOrderPaymentAllocation o where o.venOrder.orderId=" + orderId;
				
				List<VenOrderPaymentAllocation> orderPaymentAllocationList  = venOrderPaymentAllocationHome.queryByRange(query, 0, 0);
				
				for (int i=0;i<orderPaymentAllocationList.size();i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					
					orderPayment = orderPaymentAllocationList.get(i).getVenOrderPayment();
										
					map.put(DataNameTokens.VENORDERPAYMENT_ORDERPAYMENTID, orderPayment.getOrderPaymentId()!=null?orderPayment.getOrderPaymentId().toString():"");
					map.put(DataNameTokens.VENORDERPAYMENT_WCSPAYMENTID, orderPayment.getWcsPaymentId());
					map.put(DataNameTokens.VENORDERPAYMENT_AMOUNT, orderPayment.getAmount()!=null?orderPayment.getAmount().toString():"");
					map.put("AllocationAmount", allocationAmount!=null?allocationAmount.toString():"");
					map.put(DataNameTokens.VENORDERPAYMENT_VENPAYMENTTYPE_PAYMENTTYPEDESC, orderPayment.getVenPaymentType()!=null?orderPayment.getVenPaymentType().getPaymentTypeDesc():"");
					map.put(DataNameTokens.VENORDERPAYMENT_VENPAYMENTSTATUS_PAYMENTSTATUSDESC, orderPayment.getVenPaymentStatus()!=null?orderPayment.getVenPaymentStatus().getPaymentStatusDesc():"");
					
					
					dataList.add(map);
				}
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(orderPaymentAllocationList.size());
				rafDsResponse.setEndRow(request.getStartRow()+orderPaymentAllocationList.size());
			} catch (Exception e) {
				e.printStackTrace();
				rafDsResponse.setStatus(-1);
			} finally {
				try {
					venOrderPaymentAllocationLocator.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			

			rafDsResponse.setData(dataList);

			
		} else {
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(0);
			rafDsResponse.setEndRow(request.getStartRow());
		}
		
		return rafDsResponse;
	}
	
}
