package com.gdn.venice.server.app.general.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.DateToXsdDatetimeFormatter;
import com.gdn.venice.server.util.Util;

public class FetchOrderDataCommand implements RafDsCommand {
	
	RafDsRequest request;	
	
	public FetchOrderDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			VenOrderSessionEJBRemote sessionHome = (VenOrderSessionEJBRemote) locator
			.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			
			JPQLAdvancedQueryCriteria criteria = request.getCriteria();		
			List<VenOrder> orderList = null;
			int firstResult = request.getStartRow();
			if(criteria == null){
				String select = "select o from VenOrder o";
				orderList = sessionHome.queryByRange(select, firstResult, 50);
			}else{
				VenOrder order = new VenOrder();
				orderList = sessionHome.findByVenOrderLike(order, criteria, 0, 0);
			}
			
			DateToXsdDatetimeFormatter formatter = new DateToXsdDatetimeFormatter();
			for (VenOrder order : orderList) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(DataNameTokens.VENORDER_ORDERID,  Util.isNull(order.getOrderId(), "").toString());
				map.put(DataNameTokens.VENORDER_WCSORDERID,  Util.isNull(order.getWcsOrderId(), "").toString());
				map.put(DataNameTokens.VENORDER_ORDERDATE, formatter.format(order.getOrderDate()));
				map.put(DataNameTokens.VENORDER_VENCUSTOMER_CUSTOMERUSERNAME, order.getVenCustomer() != null && order.getVenCustomer().getCustomerUserName() != null?order.getVenCustomer().getCustomerUserName():"");
				map.put(DataNameTokens.VENORDER_VENORDERSTATUS_ORDERSTATUSID, order.getVenOrderStatus() != null && order.getVenOrderStatus().getOrderStatusId() != null?order.getVenOrderStatus().getOrderStatusId().toString(): "");
				GetFPDateCommand getFpDate = new GetFPDateCommand();
				map.put(DataNameTokens.VENORDER_FPDATE, getFpDate.getFPDateAsString(order.getOrderId()));		
				map.put(DataNameTokens.VENORDER_BLOCKEDFLAG,  Util.isNull(order.getBlockedFlag(), "false").toString());
				
				dataList.add(map);
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if(locator!=null){
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
