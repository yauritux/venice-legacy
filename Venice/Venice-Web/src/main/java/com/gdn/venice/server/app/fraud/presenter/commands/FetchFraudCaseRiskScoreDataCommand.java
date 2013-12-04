package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionPoint;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseRiskScoreDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseRiskScoreDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			//Catch parameter from client
			String fraudCaseId = request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONCASEID);
			
			//Lookup into EJB
			locator = new Locator<Object>();
			FrdFraudSuspicionPointSessionEJBRemote fraudPointSessionHome = (FrdFraudSuspicionPointSessionEJBRemote) locator.lookup(FrdFraudSuspicionPointSessionEJBRemote.class, "FrdFraudSuspicionPointSessionEJBBean");
			List<FrdFraudSuspicionPoint> fraudSuspicionPointList = null;
			String query = "select o from FrdFraudSuspicionPoint o " +
					       "where o.frdFraudSuspicionCase.fraudSuspicionCaseId = " + fraudCaseId;

			//Calling facade
			fraudSuspicionPointList = fraudPointSessionHome.queryByRange(query, request.getStartRow(), request.getEndRow());
			
			//Fill result
			for (int i = 0; i < fraudSuspicionPointList.size(); i++) {	
				HashMap<String, String> map = new HashMap<String, String>();
				FrdFraudSuspicionPoint point = fraudSuspicionPointList.get(i);
				
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONPOINTSID, Util.isNull(point.getFraudSuspicionPointsId(), "").toString());
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDRULENAME, Util.isNull(point.getFraudRuleName(), "").toString());
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_RISKPOINTS, Util.isNull(point.getRiskPoints(), "").toString());
				dataList.add(map);
			}
			
			rafDsResponse.setStatus(0);
			rafDsResponse.setTotalRows(dataList.size());
			rafDsResponse.setStartRow(request.getStartRow());
			rafDsResponse.setEndRow(request.getStartRow() + dataList.size());
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