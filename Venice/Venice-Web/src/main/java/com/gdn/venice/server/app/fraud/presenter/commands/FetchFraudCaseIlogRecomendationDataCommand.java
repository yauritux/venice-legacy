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
 * Fetch Command for Ilog Recomendation
 * 
 * @author Roland
 */

public class FetchFraudCaseIlogRecomendationDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseIlogRecomendationDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			String fraudCaseId = request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID);			
			locator = new Locator<Object>();
			FrdFraudSuspicionCaseSessionEJBRemote fraudCaseSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
			List<FrdFraudSuspicionCase> fraudSuspicionCaseList = fraudCaseSessionHome.queryByRange("select o from FrdFraudSuspicionCase o where o.fraudSuspicionCaseId = " + fraudCaseId, 0, 0);
		
			String status = "";			
			for (int i=0;i<fraudSuspicionCaseList.size();i++) {					
				FrdFraudSuspicionCase list = fraudSuspicionCaseList.get(i);			
				status=Util.isNull(list.getIlogFraudStatus(), "").toString();
			}	
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_ILOGRECOMENDATION, status);					
			dataList.add(map);
			rafDsResponse.setStatus(0);
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
