package com.gdn.venice.server.app.general.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote;
import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for for order item history
 * 
 * @author Roland
 */

public class FetchOrderItemHistoryDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchOrderItemHistoryDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;

		try{
			locator = new Locator<Object>();	
			VenOrderItemStatusHistorySessionEJBRemote sessionHome = (VenOrderItemStatusHistorySessionEJBRemote) locator.lookup(VenOrderItemStatusHistorySessionEJBRemote.class, "VenOrderItemStatusHistorySessionEJBBean");			
			List<VenOrderItemStatusHistory> orderItemHistoryList = null;					
			String query = "select o from VenOrderItemStatusHistory o where o.venOrderItem.orderItemId="+ request.getParams().get(DataNameTokens.VENORDERITEM_ORDERITEMID).toString()+ " order by o.id.historyTimestamp desc";		
			orderItemHistoryList = sessionHome.queryByRange(query, 0, 0);			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0; i<orderItemHistoryList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				VenOrderItemStatusHistory list = orderItemHistoryList.get(i);
				map.put(DataNameTokens.VENORDERITEMSTATUSHISTORY_ORDERITEMID, list.getId()!=null && list.getId().getOrderItemId()!=null?list.getId().getOrderItemId().toString():"");
				map.put(DataNameTokens.VENORDERITEMSTATUSHISTORY_WCSORDERITEMID, list.getVenOrderItem()!=null && list.getVenOrderItem().getWcsOrderItemId()!=null?list.getVenOrderItem().getWcsOrderItemId():"");
		        String s =  sdf.format(list.getId().getHistoryTimestamp());
		        StringBuilder sb = new StringBuilder(s);
				map.put(DataNameTokens.VENORDERITEMSTATUSHISTORY_TIMESTAMP, sb.toString());
				map.put(DataNameTokens.VENORDERITEMSTATUSHISTORY_VENORDERSTATUS_ORDERSTATUSCODE, list.getVenOrderStatus().getOrderStatusCode()!=null && list.getVenOrderStatus().getOrderStatusCode()!=null?list.getVenOrderStatus().getOrderStatusCode():"");								 
				map.put(DataNameTokens.VENORDERITEMSTATUSHISTORY_STATUSCHANGEREASON, Util.isNull(list.getStatusChangeReason(), "").toString());				
				
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
