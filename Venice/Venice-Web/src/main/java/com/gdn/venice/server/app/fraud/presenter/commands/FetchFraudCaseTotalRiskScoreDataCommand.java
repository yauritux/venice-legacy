package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudSuspicionCaseSessionEJBRemote;
import com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.FrdParameterRule31;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;
import com.gdn.venice.server.util.Util;

/**
 * Fetch Command for Total Risk Score
 * 
 * @author Roland
 */

public class FetchFraudCaseTotalRiskScoreDataCommand implements RafDsCommand {
	RafDsRequest request;	
	
	public FetchFraudCaseTotalRiskScoreDataCommand(RafDsRequest request) {
		this.request = request;
	}

	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();		
		List<HashMap<String, String>> dataList = new ArrayList<HashMap<String, String>>();
		Locator<Object> locator = null;
		
		try {
			String fraudCaseId = request.getParams().get(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_FRAUDSUSPICIONCASEID);
			
			//get total risk score from table
			locator = new Locator<Object>();
			FrdFraudSuspicionCaseSessionEJBRemote fraudCaseSessionHome = (FrdFraudSuspicionCaseSessionEJBRemote) locator.lookup(FrdFraudSuspicionCaseSessionEJBRemote.class, "FrdFraudSuspicionCaseSessionEJBBean");
			FrdParameterRule31SessionEJBRemote genuineSessionHome = (FrdParameterRule31SessionEJBRemote) locator.lookup(FrdParameterRule31SessionEJBRemote.class, "FrdParameterRule31SessionEJBBean");
			VenOrderPaymentAllocationSessionEJBRemote allocationSessionHome = (VenOrderPaymentAllocationSessionEJBRemote) locator.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");
			
			List<FrdFraudSuspicionCase> fraudSuspicionCaseList = fraudCaseSessionHome.queryByRange("select o from FrdFraudSuspicionCase o where o.fraudSuspicionCaseId = " + fraudCaseId, 0, 0);
		
			String totalScore = "";			
			for (int i=0;i<fraudSuspicionCaseList.size();i++) {					
				FrdFraudSuspicionCase list = fraudSuspicionCaseList.get(i);			
				totalScore=Util.isNull(list.getFraudTotalPoints(), "").toString();

			}	
			
			List<VenOrderPaymentAllocation> itemAllocation = allocationSessionHome.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrder.orderId = (select u.venOrder.orderId from FrdFraudSuspicionCase u where u.fraudSuspicionCaseId =  "+ fraudCaseId+")", 0, 0);
			
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_SUMMARY_TOTALRISKSCORE, totalScore);				
			
			if(itemAllocation.get(0)!=null?(itemAllocation.get(0).getVenOrderPayment().getMaskedCreditCardNumber()!=null?true:false):false){
					List<FrdParameterRule31> genuineList = genuineSessionHome.queryByRange("select o from FrdParameterRule31 o where o.email ='"+itemAllocation.get(0).getVenOrder().getVenCustomer().getCustomerUserName()+"' and o.noCc ='"+itemAllocation.get(0).getVenOrderPayment().getMaskedCreditCardNumber()+"' ", 0, 0);
					if(!genuineList.isEmpty()){
						map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_GENUINE,"true");
					}else{
						map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_GENUINE,null);
					}
			}else{					
				map.put(DataNameTokens.FRDFRAUDSUSPICIONCASE_GENUINE,null);
			}			
			
			dataList.add(map);
			rafDsResponse.setStatus(0);
		} catch (Exception e) {
			e.printStackTrace();
			rafDsResponse.setStatus(-1);
		} finally {
			try {
				if (locator != null) {
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
