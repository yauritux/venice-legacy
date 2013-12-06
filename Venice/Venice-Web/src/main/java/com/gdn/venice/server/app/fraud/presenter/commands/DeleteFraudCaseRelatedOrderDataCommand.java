package com.gdn.venice.server.app.fraud.presenter.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.djarum.raf.utilities.JPQLSimpleQueryCriteria;
import com.djarum.raf.utilities.Locator;
import com.gdn.venice.client.app.DataNameTokens;
import com.gdn.venice.facade.FrdFraudRelatedOrderInfoSessionEJBRemote;
import com.gdn.venice.persistence.FrdFraudRelatedOrderInfo;
import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.server.command.RafDsCommand;
import com.gdn.venice.server.data.RafDsRequest;
import com.gdn.venice.server.data.RafDsResponse;

public class DeleteFraudCaseRelatedOrderDataCommand implements RafDsCommand {
	RafDsRequest request;
	
	public DeleteFraudCaseRelatedOrderDataCommand(RafDsRequest request) {
		this.request = request;
	}
	
	@Override
	public RafDsResponse execute() {
		RafDsResponse rafDsResponse = new RafDsResponse();
		
		List<FrdFraudRelatedOrderInfo> frdFraudRelatedOrderInfoList = new ArrayList<FrdFraudRelatedOrderInfo>();		
		List<HashMap<String,String >> dataList = request.getData();		
		FrdFraudRelatedOrderInfo frdFraudRelatedOrderInfo = new FrdFraudRelatedOrderInfo();
		
		for (int i=0;i<dataList.size();i++) {
			Map<String, String> data = dataList.get(i);
			Iterator<String> iter = data.keySet().iterator();

			while (iter.hasNext()) {
				String key = iter.next();
				if (key.equals(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID)) {
					FrdFraudSuspicionCase frdFraudSuspicionCase = new FrdFraudSuspicionCase();
					frdFraudSuspicionCase.setFraudSuspicionCaseId(new Long(data.get(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID)));
					frdFraudRelatedOrderInfo.setFrdFraudSuspicionCase(frdFraudSuspicionCase);					
				} else if (key.equals(DataNameTokens.VENORDER_ORDERID)) {
					VenOrder venOrder = new VenOrder();
					venOrder.setOrderId(new Long(data.get(DataNameTokens.VENORDER_ORDERID)));
					frdFraudRelatedOrderInfo.setVenOrder(venOrder);
				} 
			}						
			frdFraudRelatedOrderInfoList.add(frdFraudRelatedOrderInfo);			
		}
				
		Locator<Object> locator = null;
		
		try {
			locator = new Locator<Object>();
			FrdFraudRelatedOrderInfoSessionEJBRemote sessionHome = (FrdFraudRelatedOrderInfoSessionEJBRemote) locator
			.lookup(FrdFraudRelatedOrderInfoSessionEJBRemote.class, "FrdFraudRelatedOrderInfoSessionEJBBean");
						
			JPQLAdvancedQueryCriteria criteria = new JPQLAdvancedQueryCriteria();
			criteria.setBooleanOperator("or");
			for (int i=0;i<frdFraudRelatedOrderInfoList.size();i++) {
				JPQLSimpleQueryCriteria simpleCriteria = new JPQLSimpleQueryCriteria();
				simpleCriteria.setFieldName("o.frdFraudSuspicionCase.fraudSuspicionCaseId");
				simpleCriteria.setOperator("equals");
				simpleCriteria.setValue(frdFraudRelatedOrderInfoList.get(i).getFrdFraudSuspicionCase().getFraudSuspicionCaseId().toString());
				simpleCriteria.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.FRDFRAUDSUSPICIONCASE_FRAUDSUSPICIONCASEID));
				criteria.add(simpleCriteria);
				criteria.setBooleanOperator("and");
				JPQLSimpleQueryCriteria simpleCriteria2 = new JPQLSimpleQueryCriteria();
				simpleCriteria2.setFieldName("o.venOrder.orderId");
				simpleCriteria2.setOperator("equals");
				simpleCriteria2.setValue(frdFraudRelatedOrderInfoList.get(i).getVenOrder().getOrderId().toString());
				simpleCriteria2.setFieldClass(DataNameTokens.getDataNameToken().getFieldClass(DataNameTokens.VENORDER_ORDERID));
				criteria.add(simpleCriteria2);
			}
			
			frdFraudRelatedOrderInfoList = sessionHome.findByFrdFraudRelatedOrderInfoLike(frdFraudRelatedOrderInfo, criteria, request.getStartRow(), request.getEndRow());
			sessionHome.removeFrdFraudRelatedOrderInfoList((ArrayList<FrdFraudRelatedOrderInfo>)frdFraudRelatedOrderInfoList);			
									
			rafDsResponse.setStatus(0);
		} catch (Exception ex) {
			ex.printStackTrace();
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
