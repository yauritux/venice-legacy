package com.gdn.venice.server.app.general.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote;
import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for for order history
 * 
 * @author Roland
 */

public class FetchOrderHistoryOrderDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchOrderHistoryOrderDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			VenOrderStatusHistorySessionEJBRemote sessionHome = (VenOrderStatusHistorySessionEJBRemote) locator.lookup(VenOrderStatusHistorySessionEJBRemote.class, "VenOrderStatusHistorySessionEJBBean");			
			List<VenOrderStatusHistory> orderHistoryList = null;					
			String query = "select o from VenOrderStatusHistory o where o.id.orderId="+ request.getParams().get(DataNameTokens.VENORDER_ORDERID).toString()+" order by o.id.historyTimestamp desc";			
			orderHistoryList = sessionHome.queryByRange(query, 0, 0);			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0; i<orderHistoryList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				VenOrderStatusHistory list = orderHistoryList.get(i);
				map.put(DataNameTokens.VENORDERSTATUSHISTORY_ORDERID, list.getId()!=null && list.getId().getOrderId()!=null?list.getId().getOrderId().toString():"");
				map.put(DataNameTokens.VENORDERSTATUSHISTORY_WCSORDERID, list.getVenOrder()!=null && list.getVenOrder().getWcsOrderId()!=null?list.getVenOrder().getWcsOrderId():"");
		        String s =  sdf.format(list.getId().getHistoryTimestamp());
		        StringBuilder sb = new StringBuilder(s);
				map.put(DataNameTokens.VENORDERSTATUSHISTORY_TIMESTAMP, sb.toString());
				map.put(DataNameTokens.VENORDERSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE, list.getVenOrderStatus().getOrderStatusCode()!=null && list.getVenOrderStatus().getOrderStatusCode()!=null?list.getVenOrderStatus().getOrderStatusCode():"");								 
				map.put(DataNameTokens.VENORDERSTATUSHISTORY_STATUSCHANGEREASON, Util.isNull(list.getStatusChangeReason(), "").toString());				
				
				dataList.add(map);				
			}
			rafDsResponse.setStatus(0);
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setEndRow(request.getStartRow()+dataList.size());
		}catch(Exception e){
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		}finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		rafDsResponse.setData(dataList);
		return rafDsResponse;
	}
}
