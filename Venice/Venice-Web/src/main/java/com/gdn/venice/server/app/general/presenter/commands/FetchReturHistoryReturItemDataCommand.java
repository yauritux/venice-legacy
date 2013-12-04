package com.gdn.venice.server.app.general.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenReturItemStatusHistorySessionEJBRemote;
import com.gdn.venice.persistence.VenReturItemStatusHistory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for for retur item history
 * 
 * @author Roland
 */

public class FetchReturHistoryReturItemDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchReturHistoryReturItemDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;

		try{
			locator = new Locator<Object>();			
			VenReturItemStatusHistorySessionEJBRemote sessionHome = (VenReturItemStatusHistorySessionEJBRemote) locator.lookup(VenReturItemStatusHistorySessionEJBRemote.class, "VenReturItemStatusHistorySessionEJBBean");			
			List<VenReturItemStatusHistory> returItemHistoryList = null;					
			String query = "select o from VenReturItemStatusHistory o where o.venReturItem.venRetur.returId="+ request.getParams().get(DataNameTokens.VENRETUR_RETURID).toString()+ " order by o.id.historyTimestamp desc";		
			returItemHistoryList = sessionHome.queryByRange(query, 0, 0);			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0; i<returItemHistoryList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				VenReturItemStatusHistory list = returItemHistoryList.get(i);
				map.put(DataNameTokens.VENRETURITEMSTATUSHISTORY_RETURITEMID, list.getId()!=null && list.getId().getReturItemId()!=null?list.getId().getReturItemId().toString():"");
				map.put(DataNameTokens.VENRETURITEMSTATUSHISTORY_WCSRETURITEMID, list.getVenReturItem()!=null && list.getVenReturItem().getWcsReturItemId()!=null?list.getVenReturItem().getWcsReturItemId():"");
		        String s =  sdf.format(list.getId().getHistoryTimestamp());
		        StringBuilder sb = new StringBuilder(s);
				map.put(DataNameTokens.VENRETURITEMSTATUSHISTORY_TIMESTAMP, sb.toString());
				map.put(DataNameTokens.VENRETURITEMSTATUSHISTORY_VENRETURSTATUS_ORDERSTATUSCODE, list.getVenReturStatus().getOrderStatusCode()!=null && list.getVenReturStatus().getOrderStatusCode()!=null?list.getVenReturStatus().getOrderStatusCode():"");								 
				map.put(DataNameTokens.VENRETURITEMSTATUSHISTORY_STATUSCHANGEREASON, Util.isNull(list.getStatusChangeReason(), "").toString());				
				
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
