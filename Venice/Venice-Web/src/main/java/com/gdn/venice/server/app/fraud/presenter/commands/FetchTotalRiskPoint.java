package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Order Item Info
 * 
 * @author Arifin
 */

public class FetchTotalRiskPoint implements RafDsCommand {

	RafDsRequest request;
	
	public FetchTotalRiskPoint(RafDsRequest request){
		this.request=request;
	}
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		List<HashMap<String, String>> dataList= new ArrayList<HashMap<String, String>>();
		Locator<Object> locator=null;
		
		try{
			locator = new Locator<Object>();			
			FrdFraudSuspicionCaseSessionEJBRemote sessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");		
			List<FrdFraudSuspicionCase> partyList = null;			
				/*
				 * get risk point untuk orderid tersebut
				 */
				String query = "select o from FrdFraudSuspicionCase o where o.fraudSuspicionCaseId="+request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);			
				partyList = sessionHome.queryByRange(query, request.getStartRow(), request.getEndRow()-request.getStartRow());
		
			for(int i=0; i<partyList.size();i++){
				HashMap<String, String> map = new HashMap<String, String>();
				FrdFraudSuspicionCase list = partyList.get(i);
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, Util.isNull(list.getFraudSuspicionCaseId(), "").toString());
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS, Util.isNull(list.getFraudTotalPoints(), "").toString());				
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
