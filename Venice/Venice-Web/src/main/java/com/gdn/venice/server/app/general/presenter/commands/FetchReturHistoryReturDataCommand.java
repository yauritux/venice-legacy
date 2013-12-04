package com.gdn.venice.server.app.general.presenter.commands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.VenReturStatusHistorySessionEJBRemote;
import com.gdn.venice.persistence.VenReturStatusHistory;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for for retur history
 * 
 * @author Roland
 */

public class FetchReturHistoryReturDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchReturHistoryReturDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			VenReturStatusHistorySessionEJBRemote sessionHome = (VenReturStatusHistorySessionEJBRemote) locator.lookup(VenReturStatusHistorySessionEJBRemote.class, "VenReturStatusHistorySessionEJBBean");			
			List<VenReturStatusHistory> returHistoryList = null;					
			String query = "select o from VenReturStatusHistory o where o.id.returId="+ request.getParams().get(DataNameTokens.VENRETUR_RETURID).toString()+" order by o.id.historyTimestamp desc";			
			returHistoryList = sessionHome.queryByRange(query, 0, 0);			
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=0; i<returHistoryList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				VenReturStatusHistory list = returHistoryList.get(i);
				map.put(DataNameTokens.VENRETURSTATUSHISTORY_RETURID, list.getId()!=null && list.getId().getReturId()!=null?list.getId().getReturId().toString():"");
				map.put(DataNameTokens.VENRETURSTATUSHISTORY_WCSRETURID, list.getVenRetur()!=null && list.getVenRetur().getWcsReturId()!=null?list.getVenRetur().getWcsReturId():"");
		        String s =  sdf.format(list.getId().getHistoryTimestamp());
		        StringBuilder sb = new StringBuilder(s);
				map.put(DataNameTokens.VENRETURSTATUSHISTORY_TIMESTAMP, sb.toString());
				map.put(DataNameTokens.VENRETURSTATUSHISTORY_VENRETURSTATUS_ORDERSTATUSCODE, list.getVenReturStatus().getOrderStatusCode()!=null && list.getVenReturStatus().getOrderStatusCode()!=null?list.getVenReturStatus().getOrderStatusCode():"");								 
				map.put(DataNameTokens.VENRETURSTATUSHISTORY_STATUSCHANGEREASON, Util.isNull(list.getStatusChangeReason(), "").toString());				
				
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
