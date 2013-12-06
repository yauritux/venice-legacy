package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenCustomer;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseCustomerDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public FetchFraudCaseCustomerDataCommand(RafDsRequest request){
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			//Lookup into EJB ven order entity
			locator = new Locator<Object>();
			String orderId = request.getParams().get(DataNameTokens.VENORDER_ORDERID);
			//Lookup into EJB for customer entity
			VenOrderSessionEJBRemote orderSessionHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			List<VenOrder> orderList = orderSessionHome.queryByRange("select o from VenOrder o  where o.wcsOrderId = '"+orderId+"'", 0, 1);
			HashMap<String, String> map = new HashMap<String, String>();
			
			//Set result
			DateToXsdDatetimeFormatter formatter =  new DateToXsdDatetimeFormatter();
			if (orderList.size() > 0  && orderList.get(0).getVenCustomer()!=null) {
				VenCustomer customer = orderList.get(0).getVenCustomer();			
				map.put(DataNameTokens.VENCUSTOMER_WCSCUSTOMERID, Util.isNull(customer.getWcsCustomerId(), "").toString());
				map.put(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME, customer.getVenParty()!=null && customer.getVenParty().getFullOrLegalName()!=null?customer.getVenParty().getFullOrLegalName().toString():"");
				map.put(DataNameTokens.VENCUSTOMER_CUSTOMERUSERNAME, Util.isNull(customer.getCustomerUserName(), "").toString());
				map.put(DataNameTokens.VENCUSTOMER_USERTYPE, Util.isNull(customer.getUserType(), "").equals("R")?"Registered shopper":"Unregistered shopper");
				map.put(DataNameTokens.VENCUSTOMER_DATEOFBIRTH, formatter.format(customer.getDateOfBirth()));
				map.put(DataNameTokens.VENCUSTOMER_FIRSTTIMETRANSACTIONFLAG, Util.isNull(customer.getFirstTimeTransactionFlag(), "false").toString());	
				map.put(DataNameTokens.VENORDER_IPADDRESS, Util.isNull(orderList.get(0).getIpAddress(), "").toString());
				dataList.add(map);
				
				//Set DSResponse's properties
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(1);
				rafDsResponse.setEndRow(request.getStartRow()+1);
			} else {
				//Set DSResponse's properties
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(0);
				rafDsResponse.setEndRow(request.getStartRow());
			}
		} catch(Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if (locator != null) {
					locator.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		//Put and return result
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}	
}