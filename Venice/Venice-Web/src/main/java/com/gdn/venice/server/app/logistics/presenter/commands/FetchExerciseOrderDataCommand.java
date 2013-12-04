package com.gdn.venice.server.app.logistics.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.djarum.raf.utilities.Locator;

public class FetchExerciseOrderDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public FetchExerciseOrderDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		
		Locator<VenOrder> orderLocator = null;
		
		try {
			orderLocator = new Locator<VenOrder>();
			
			VenOrderSessionEJBRemote sessionHome = (VenOrderSessionEJBRemote) orderLocator
			.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
						
			List<VenOrder> orderList = null;
			
			String query = "Select o from VenOrder o";
			
//			orderList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
			orderList = sessionHome.queryByRange(query, 0, 0);

			
			for (int i=0;i<orderList.size();i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				
				VenOrder order = orderList.get(i);
				
				map.put(DataNameTokens.VENORDER_ORDERID, order.getOrderId()!=null?order.getOrderId().toString():"");
				map.put(DataNameTokens.VENORDER_WCSORDERID, order.getWcsOrderId());
				map.put(DataNameTokens.VENORDER_IPADDRESS, order.getIpAddress());
				map.put(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME, order.getVenCustomer()!=null?order.getVenCustomer().getCustomerUserName():"");
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(orderList.size());
			rafDsResponse.setEndRow(request.getStartRow()+orderList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				orderLocator.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		rafDsResponse.setData(dataList);
		
		return rafDsResponse;
	}

}
