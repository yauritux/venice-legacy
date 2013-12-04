package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenReturSessionEJBRemote;
import com.gdn.venice.persistence.VenCustomer;
import com.gdn.venice.persistence.VenRetur;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;
import com.djarum.raf.utilities.Locator;

public class FetchReturCustomerDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchReturCustomerDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<VenReturSessionEJBRemote> returLocator = null;
		
		try {
			returLocator = new Locator<VenReturSessionEJBRemote>();
			
			VenReturSessionEJBRemote returSessionHome = (VenReturSessionEJBRemote) returLocator
			.lookup(VenReturSessionEJBRemote.class, "VenReturSessionEJBBean");
			
			String select = "select o from VenRetur o where o.returId = " + request.getParams().get(DataNameTokens.VENRETUR_RETURID);
			
			//Find Retur by Retur Id			
			List<VenRetur> returList = returSessionHome.queryByRange(select, 0, 1);
						
			if (returList.size()>0  && returList.get(0).getVenCustomer()!=null) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				VenCustomer customer = returList.get(0).getVenCustomer();
				
				DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
			
				map.put(DataNameTokens.VENCUSTOMER_CUSTOMERID, customer.getCustomerId()!=null?customer.getCustomerId().toString():"");
				map.put(DataNameTokens.VENCUSTOMER_WCSCUSTOMERID, customer.getWcsCustomerId());
				map.put(DataNameTokens.VENCUSTOMER_CUSTOMERUSERNAME, customer.getCustomerUserName());
				map.put(DataNameTokens.VENCUSTOMER_USERTYPE, Util.isNull(customer.getUserType(), "").equals("R")?"Registered shopper":"Unregistered shopper");
				map.put(DataNameTokens.VENCUSTOMER_DATEOFBIRTH, formatter.format(customer.getDateOfBirth()));
				map.put(DataNameTokens.VENCUSTOMER_FIRSTTIMETRANSACTIONFLAG, customer.getFirstTimeTransactionFlag()!=null?customer.getFirstTimeTransactionFlag().toString():"");
				map.put(DataNameTokens.VENCUSTOMER_VENPARTY_PARTYFIRSTNAME, customer.getVenParty()!=null?customer.getVenParty().getPartyFirstName():"");
				map.put(DataNameTokens.VENCUSTOMER_VENPARTY_PARTYLASTNAME, customer.getVenParty()!=null?customer.getVenParty().getPartyLastName():"");
				map.put(DataNameTokens.VENCUSTOMER_VENPARTY_FULLORLEGALNAME, customer.getVenParty()!=null?customer.getVenParty().getFullOrLegalName():"");
				
				dataList.add(map);
				
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(1);
				rafDsResponse.setEndRow(request.getStartRow()+1);
			} else {
				rafDsResponse.setStatus(0);
				rafDsResponse.setStartRow(request.getStartRow());
				rafDsResponse.setTotalRows(0);
				rafDsResponse.setEndRow(request.getStartRow());
			}

			
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				returLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);

		return rafDsResponse;
	}

}
