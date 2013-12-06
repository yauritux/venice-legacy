package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLQueryStringBuilder;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.util.VeniceConstants;

public class FetchUncalculatedCreditCardOrderDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public FetchUncalculatedCreditCardOrderDataCommand(RafDsRequest request){
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			//Lookup into EJB
			locator = new Locator<Object>();
			VenOrderPaymentAllocationSessionEJBRemote sessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			List<VenOrderPaymentAllocation> venOrderPaymentAllocationList = null;
			
			//Build query
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();			
			
			if (criteria == null) {
				String query = "select o from VenOrderPaymentAllocation o where o.venOrderPayment.venPaymentType.paymentTypeCode = '"+ VeniceConstants.VEN_PAYMENT_TYPE_CC +"' and (o.venOrderPayment.maskedCreditCardNumber is null or o.venOrderPayment.maskedCreditCardNumber = '')";			
				venOrderPaymentAllocationList = sessionHome.queryByRange(query, 0, 0);
			} else {
				VenOrderPaymentAllocation venOrderPaymentAllocation = new VenOrderPaymentAllocation();
				JPQLQueryStringBuilder<VenOrderPaymentAllocation> qb = new JPQLQueryStringBuilder<VenOrderPaymentAllocation>(venOrderPaymentAllocation);
				
				@SuppressWarnings("rawtypes")				
				String query = qb.buildQueryString(new HashMap(), criteria);
				for (int i = 0; i < qb.getBindingArray().length; i++) {
					if (qb.getBindingArray()[i] != null) {
						String bindingValue = qb.getBindingArray()[i].toString();
						if (qb.getBindingArray()[i].getClass().getCanonicalName().equals("java.lang.String")) {
							bindingValue = "'" + bindingValue + "'";
						}
						
						CharSequence cherSeq = new String("?" + (i + 1));
						query = query.replace(cherSeq, bindingValue);
					}
				}

				query += " and o.venOrderPayment.venPaymentType.paymentTypeCode = '"+ VeniceConstants.VEN_PAYMENT_TYPE_CC +"' and (o.venOrderPayment.maskedCreditCardNumber is null or o.venOrderPayment.maskedCreditCardNumber = '')";
				venOrderPaymentAllocationList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow() - request.getStartRow());
			}
			
			//Put result
			for (int i = 0; i < venOrderPaymentAllocationList.size(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				VenOrderPaymentAllocation venOrderPaymentAllocation = venOrderPaymentAllocationList.get(i);

				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_ORDERPAYMENTID, venOrderPaymentAllocation.getVenOrderPayment()!=null && venOrderPaymentAllocation.getVenOrderPayment().getOrderPaymentId()!=null?venOrderPaymentAllocation.getVenOrderPayment().getOrderPaymentId().toString():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_ORDERID, venOrderPaymentAllocation.getVenOrder()!=null && venOrderPaymentAllocation.getVenOrder().getOrderId()!=null?venOrderPaymentAllocation.getVenOrder().getOrderId().toString():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDER_WCSORDERID, venOrderPaymentAllocation.getVenOrder()!=null && venOrderPaymentAllocation.getVenOrder().getWcsOrderId()!=null?venOrderPaymentAllocation.getVenOrder().getWcsOrderId().toString():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_WCSPAYMENTID, venOrderPaymentAllocation.getVenOrderPayment()!=null && venOrderPaymentAllocation.getVenOrderPayment().getWcsPaymentId()!=null?venOrderPaymentAllocation.getVenOrderPayment().getWcsPaymentId().toString():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_AMOUNT, venOrderPaymentAllocation.getVenOrderPayment()!=null && venOrderPaymentAllocation.getVenOrderPayment().getAmount()!=null?venOrderPaymentAllocation.getVenOrderPayment().getAmount().toString():"");
				map.put(DataNameTokens.VENORDERPAYMENTALLOCATION_VENORDERPAYMENT_PAYMENTCONFIRMATIONNUMBER, venOrderPaymentAllocation.getVenOrderPayment()!=null && venOrderPaymentAllocation.getVenOrderPayment().getPaymentConfirmationNumber()!=null?venOrderPaymentAllocation.getVenOrderPayment().getPaymentConfirmationNumber().toString():"");
				dataList.add(map);
			}
			
			//Set DSResponse's properties
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(venOrderPaymentAllocationList.size());
			rafDsResponse.setEndRow(request.getStartRow() + dataList.size());
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
}