package com.gdn.venice.facade.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gdn.venice.dao.VenOrderItemDAO;
import com.gdn.venice.facade.spring.LogAirwayBillService;
import com.gdn.venice.facade.spring.PublisherService;
import com.gdn.venice.facade.spring.VenOrderItemStatusHistoryService;
import com.gdn.venice.persistence.VenOrderItem;
import com.gdn.venice.util.VeniceConstants;

@Component("orderItemMergeProcessor")
public class VenOrderItemMergeProcessor extends MergeProcessor{
	
	@Autowired
	VenOrderItemStatusHistoryService venOrderItemStatusHistoryService;
	
	@Autowired
	LogAirwayBillService logAirwayBillService;

	@Autowired
	PublisherService publisherService;
	
	@Autowired
	VenOrderItemDAO venOrderItemDAO;
	@Override
	public boolean preMerge(Object obj) {
		VenOrderItem newVenOrderItem = (VenOrderItem) obj;

		VenOrderItem existingVenOrderItem = venOrderItemDAO
				.findWithVenOrderStatusAndLogAirwayBillByWcsOrderItemId(newVenOrderItem.getWcsOrderItemId());
		
		Long existingOrderItemStatusId = existingVenOrderItem.getVenOrderStatus().getOrderStatusId();
		Long newOrderItemStatusId = newVenOrderItem.getVenOrderStatus().getOrderStatusId();
		
		boolean isAllowedForPublish = isAllowedToPublishUpdateOrderItemStatusMessage(existingOrderItemStatusId, newOrderItemStatusId);
		boolean isAllowedToAddOrderItemStatusHistory = isAllowedToAddOrderItemStatusHistory(existingOrderItemStatusId, newOrderItemStatusId);
		boolean isAllowedToAddDummyLogAirwayBillForNewlyFPOrderItem = isAllowedToAddDummyLogAirwayBillForNewlyFPOrderItem(existingOrderItemStatusId, newOrderItemStatusId);
		
		if(isAllowedForPublish){
			publisherService.publishUpdateOrderItemStatus(newVenOrderItem);
		}
		
		if(isAllowedToAddOrderItemStatusHistory){
			venOrderItemStatusHistoryService.saveVenOrderItemStatusHistory(newVenOrderItem);
		}
		
		if(isAllowedToAddDummyLogAirwayBillForNewlyFPOrderItem){
			logAirwayBillService.addDummyLogAirwayBillForNewlyFPOrderItem(existingVenOrderItem);
		}
		
		return true;
	}

	@Override
	public boolean merge(Object obj) {
		VenOrderItem newVenOrderItem = (VenOrderItem) obj;
		
		venOrderItemDAO.save(newVenOrderItem);
		
		return true;
	}

	@Override
	public boolean postMerge(Object obj) {
		return true;
	}
	
	private boolean isAllowedToPublishUpdateOrderItemStatusMessage(long existingOrderItemStatus, long newOrderItemStatus){
		if ((existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_PU && newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES)
                || (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES && newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_PP)
                || (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES && newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX)
                || (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_PP && newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX)
                || (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES && newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_RT)
                || (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES && newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_D)
                || (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX && newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_D)
                || (existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_PF && newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_FP)) {
			
			return true;
		}
		
		return false;
	}

	private boolean isAllowedToAddOrderItemStatusHistory(long existingOrderItemStatus, long newOrderItemStatus) {
		
		if(existingOrderItemStatus == newOrderItemStatus){
			return false;
		}
		
		 if (newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_PU
                 || newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_ES
                 || newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_PP
                 || newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_CX
                 || newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_RT
                 || newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_D
                 || newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_X) {
			 
			 return true;
		 }
		
		return false;
	}
	
	private boolean isAllowedToAddDummyLogAirwayBillForNewlyFPOrderItem(long existingOrderItemStatus, long newOrderItemStatus){
		
		if ((existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_C || existingOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_SF) 
				&& newOrderItemStatus == VeniceConstants.VEN_ORDER_STATUS_FP) {
			return true;
		}
		
		return false;
	}

}
