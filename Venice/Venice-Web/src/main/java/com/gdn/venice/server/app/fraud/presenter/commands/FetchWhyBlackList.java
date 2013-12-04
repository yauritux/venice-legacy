package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdBlacklistReasonSessionEJBRemote;
import com.gdn.venice.persistence.FrdBlacklistReason;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;
/**
 * Fetch reason black list
 * 
 * @author Arifin
 */

public class FetchWhyBlackList implements RafDsCommand {

	RafDsRequest request;
	
	public FetchWhyBlackList(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			FrdBlacklistReasonSessionEJBRemote sessionHome = (FrdBlacklistReasonSessionEJBRemote) locator.lookup(FrdBlacklistReasonSessionEJBRemote.class, "FrdBlacklistReasonSessionEJBBean");		
			List<FrdBlacklistReason> partyList = null;					
			/*
			 * get reason black list untuk order tersebut
			 */
				String query = "select o from FrdBlacklistReason o where o.orderId="+request.getParams().get(DataNameTokens.VENORDER_ORDERID);	
				partyList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
				if(partyList!=null && !partyList.isEmpty()){
					for(int i=0; i<partyList.size();i++){
						HashMap<String, String> map = new HashMap<String, String>();
						FrdBlacklistReason list = partyList.get(i);
						map.put(DataNameTokens.FRDBLACKLIST_ORDERID, Util.isNull(list.getOrderId(), "").toString());
						map.put(DataNameTokens.FRDBLACKLIST_BLACKLIST_REASON, Util.isNull(list.getBlacklistReason(), "").toString());		
						dataList.add(map);				
					}
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
