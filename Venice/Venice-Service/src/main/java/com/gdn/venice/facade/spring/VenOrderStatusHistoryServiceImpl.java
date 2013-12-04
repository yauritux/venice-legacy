package com.gdn.venice.facade.spring;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gdn.venice.dao.VenOrderStatusHistoryDAO;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.gdn.venice.persistence.VenOrderStatusHistoryPK;

@Service
public class VenOrderStatusHistoryServiceImpl implements VenOrderStatusHistoryService {
	@Autowired
	VenOrderStatusHistoryDAO venOrderStatusHistoryDAO;
	
	@Override
	public void saveVenOrderStatusHistory(VenOrder venOrder) {
		String changeReason = "Updated by System";
		commonSaveVenOrderStatusHistory(venOrder, changeReason);
	}
	
	@Override
	public void savePartialPartialFulfillmentVenOrderStatusHistory(VenOrder venOrder) {
		String changeReason = "Updated by System (Partial Fulfillment)";
		commonSaveVenOrderStatusHistory(venOrder, changeReason);
	} 
	
	private void commonSaveVenOrderStatusHistory(VenOrder venOrder, String changeReason){
		VenOrderStatusHistoryPK venOrderStatusHistoryPK = new VenOrderStatusHistoryPK();
		venOrderStatusHistoryPK.setOrderId(new Long(venOrder.getOrderId()));
		venOrderStatusHistoryPK.setHistoryTimestamp(new Timestamp(System.currentTimeMillis()));
		
		VenOrderStatusHistory orderStatusHistory = new VenOrderStatusHistory();
		orderStatusHistory.setId(venOrderStatusHistoryPK);
		orderStatusHistory.setStatusChangeReason(changeReason);
		orderStatusHistory.setVenOrderStatus(venOrder.getVenOrderStatus());
		
		venOrderStatusHistoryDAO.save(orderStatusHistory);
	}
}
