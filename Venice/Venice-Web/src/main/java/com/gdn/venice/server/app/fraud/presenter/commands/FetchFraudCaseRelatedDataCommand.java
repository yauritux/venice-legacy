package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudRelatedOrderInfo;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

public class FetchFraudCaseRelatedDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseRelatedDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			FrdFraudRelatedOrderInfoSessionEJBRemote sessionHome = (FrdFraudRelatedOrderInfoSessionEJBRemote) locator.lookup(FrdFraudRelatedOrderInfoSessionEJBRemote.class, "FrdFraudRelatedOrderInfoSessionEJBBean");
			
			List<FrdFraudRelatedOrderInfo> relatedOrderList = sessionHome.queryByRange("select o from FrdFraudRelatedOrderInfo o where o.frdFraudSuspicionCase.fraudSuspicionCaseId = " + request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID), request.getStartRow(), request.getEndRow());
			for (int i=0;i<relatedOrderList.size();i++) {
				Long orderId = (Long) relatedOrderList.get(i).getVenOrder().getOrderId();
				FrdFraudSuspicionCaseSessionEJBRemote fraudCaseSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
				List<FrdFraudSuspicionCase> fraudCaseList = null;
				String query = "select o from FrdFraudSuspicionCase o where o.venOrder.orderId = " + orderId;
				fraudCaseList = fraudCaseSessionHome.queryByRange(query, 0, 0);
				
				for (int j=0;j<fraudCaseList.size();j++) {
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_VENORDER_WCSORDERID, fraudCaseList.get(j).getVenOrder()!=null && fraudCaseList.get(j).getVenOrder().getWcsOrderId()!=null?fraudCaseList.get(j).getVenOrder().getWcsOrderId().toString():"");
					map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID, Util.isNull(fraudCaseList.get(j).getFraudSuspicionCaseId(), "").toString());
					map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDATETIME, Util.isNull(fraudCaseList.get(j).getFraudCaseDateTime(), "").toString());
					map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEDESC, Util.isNull(fraudCaseList.get(j).getFraudCaseDesc(), "").toString());
					map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDTOTALPOINTS, Util.isNull(fraudCaseList.get(j).getFraudTotalPoints(), "").toString());
					map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRDFRAUDCASESTATUS_FRAUDCASESTATUSDESC, fraudCaseList.get(j).getFrdFraudCaseStatus()!=null && fraudCaseList.get(j).getFrdFraudCaseStatus().getFraudCaseStatusDesc()!=null?fraudCaseList.get(j).getFrdFraudCaseStatus().getFraudCaseStatusDesc().toString():"");
					map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDCASEREASON, Util.isNull(fraudCaseList.get(j).getSuspicionReason(), "").toString());
					map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONNOTES, Util.isNull(fraudCaseList.get(j).getFraudSuspicionNotes(), "").toString());
					dataList.add(map);
				}				
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
