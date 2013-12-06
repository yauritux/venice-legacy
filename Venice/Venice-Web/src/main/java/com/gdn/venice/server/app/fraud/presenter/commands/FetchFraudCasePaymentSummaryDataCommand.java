package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.facade.VenOrderItemSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.VenAddress;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class FetchFraudCasePaymentSummaryDataCommand implements RafDsCommand {
	RafDsRequest request;	

	public FetchFraudCasePaymentSummaryDataCommand(RafDsRequest request) {
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
			String query = "select o from FrdFraudSuspicionCase o where o.fraudSuspicionCaseId = " + fraudCaseId;
			
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
			query = "select o from VenOrderItem o where o.venOrder.orderId = " + orderId;
			
			//Calling facade for order item
			orderItemList = orderItemSessionHome.queryByRange(query, 0, 0);
			
			//Find multiple shipping address by looping through order item
			//Only if order item more than one
			boolean isMultipleShippingAddresses = false;
			if (orderItemList.size() > 1) {
				VenAddress refAddress = orderItemList.get(0).getVenAddress();
				for (int i = 1; i < orderItemList.size(); i++) {
					VenAddress currAddress = orderItemList.get(i).getVenAddress();
					if (!isNull(refAddress.getStreetAddress1(), "").equalsIgnoreCase(isNull(currAddress.getStreetAddress1(), ""))) {
						isMultipleShippingAddresses = true;
						break;
					}
				}
			}
			
			//Lookup into EJB for order payment allocation
			VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			List<VenOrderPaymentAllocation> orderPaymentAllocationList = null;
			query = "select o from VenOrderPaymentAllocation o where o.venOrder.orderId = " + orderId;
			
			//Calling facade for order payment allocation
			orderPaymentAllocationList = orderPaymentAllocationSessionHome.queryByRange(query, 0, 0);
			
			//Find at least one shipping address and billing address are match
			boolean isShipppingAddressAndBillingAddressAreMatch = false;
			for (int i = 0; i < orderItemList.size(); i++) {
				VenAddress shippingAddress = orderItemList.get(i).getVenAddress();
				for (int j = 0; j < orderPaymentAllocationList.size(); j++) {
					VenAddress billingAddress = orderPaymentAllocationList.get(j).getVenOrderPayment().getVenAddress();
					if (isNull(shippingAddress.getStreetAddress1(), "").equalsIgnoreCase(isNull(billingAddress.getStreetAddress1(), "")) &&
							isNull(shippingAddress.getPostalCode(), "").equalsIgnoreCase(isNull(billingAddress.getPostalCode(), ""))) {
						
						isShipppingAddressAndBillingAddressAreMatch = true;
						break;
					}
				}
			}
			
			//Variable for result
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_PAYMENTADDRESSSIMILARITY, isShipppingAddressAndBillingAddressAreMatch==true? "Yes" : "No");
			map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_MULTIPLESHIPMENT, isMultipleShippingAddresses==true? "Yes" : "No");
			dataList.add(map);

			//Set DSResponse's properties
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(0);
			rafDsResponse.setTotalRows(1);
			rafDsResponse.setEndRow(1);
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
		
		//Set data and return
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
	
	//General function for replacing null object with string replacement (usually "")
	private String isNull(Object object, String replacement) {
		return object == null ? replacement : object.toString();
	}
}