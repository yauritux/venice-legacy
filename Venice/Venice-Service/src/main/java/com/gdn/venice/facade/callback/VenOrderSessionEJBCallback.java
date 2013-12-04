package com.gdn.venice.facade.callback;

import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote;
import com.gdn.venice.integration.outbound.Publisher;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.gdn.venice.persistence.VenOrderStatusHistoryPK;
import com.gdn.venice.util.VeniceConstants;

/**
 * VenOrderSessionEJBCallback.java
 * 
 * This callback object is used to implement the publishing of order status
 * changes
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class VenOrderSessionEJBCallback implements SessionCallback {

	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public VenOrderSessionEJBCallback() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.callback.VenOrderSessionEJBCallback");
	}

	@Override
	public Boolean onPrePersist(Object businessObject) {
		_log.debug("onPrePersist");
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPostPersist(Object businessObject) {
		_log.debug("onPostPersist");
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPreMerge(Object businessObject) {
		_log.debug("onPreMerge");
		VenOrder venOrder = (VenOrder) businessObject;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();	
				
			Long newStatus = null;
			if(venOrder.getVenOrderStatus() != null){
				newStatus = venOrder.getVenOrderStatus().getOrderStatusId();
				_log.debug("masuk status tidak null, newStatus: "+newStatus);				
			}
			
			/*
			 * If the status is null in the incoming update then just set it to the existing
			 * order status so that we don't get an NPE below. This has zero impact.
			 */
			if(venOrder.getVenOrderStatus() == null){				
				newStatus = venOrder.getVenOrderStatus().getOrderStatusId();
				_log.debug("masuk status null, newStatus: "+newStatus);
			}
			
			_log.debug("newStatus: "+newStatus);
			/*
			 * If the order is cancelled for whatever reason 
			 * then delete the payment allocations so that
			 * the payment can be allocated to another order
			 */
			if(newStatus == VeniceConstants.VEN_ORDER_STATUS_X){
				/*VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationHome = (VenOrderPaymentAllocationSessionEJBRemote) locator
				.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");

				_log.debug("condition for cancel order is true, remove payment allocation");
				orderPaymentAllocationHome.removeVenOrderPaymentAllocationList(venOrder.getVenOrderPaymentAllocations());
				venOrder.setVenOrderPaymentAllocations(null);
				_log.debug("done remove payment allocation");*/
				
				/*
				 * payment tetap dijalankan walaupun order di cancel
				 * payment masuk dan direfund to customer or bank
				 */
				_log.debug("done remove payment allocation");
				
				//add order status history
				_log.debug("add order status history for order cancelled");
				_log.debug("\n wcs order id: "+venOrder.getWcsOrderId());
				VenOrderStatusHistorySessionEJBRemote orderStatusHistoryHome = (VenOrderStatusHistorySessionEJBRemote) locator
				.lookupLocal(VenOrderStatusHistorySessionEJBRemote.class, "VenOrderStatusHistorySessionEJBBean");
				
				VenOrderStatusHistory venOrderStatusHistory = new VenOrderStatusHistory();
				
				VenOrderStatusHistoryPK venOrderStatusHistoryPK = new VenOrderStatusHistoryPK();
				venOrderStatusHistoryPK.setHistoryTimestamp(new Date(System.currentTimeMillis()));
				venOrderStatusHistoryPK.setOrderId(venOrder.getOrderId());
				
				venOrderStatusHistory.setId(venOrderStatusHistoryPK);
				venOrderStatusHistory.setVenOrder(venOrder);
				venOrderStatusHistory.setStatusChangeReason("Updated by System");
				venOrderStatusHistory.setVenOrderStatus(venOrder.getVenOrderStatus());
				
				venOrderStatusHistory = orderStatusHistoryHome.persistVenOrderStatusHistory(venOrderStatusHistory);
				_log.debug("done add order status history for order cancelled");				
			}
			
		} catch (Exception e) {
			String errMsg = "An exception occured when processing a preMerge callback for VenOrderSessionEJBCallback:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			throw new EJBException(errMsg);
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPostMerge(Object businessObject) {
		_log.debug("onPostMerge");
		VenOrder venOrder = (VenOrder) businessObject;
		Locator<Object> locator = null;
		try {
			_log.debug("\n wcs order id: "+venOrder.getWcsOrderId());
			locator = new Locator<Object>();

			VenOrderSessionEJBRemote orderHome = (VenOrderSessionEJBRemote) locator.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
					
			List<VenOrder> venOrderList = orderHome.queryByRange("select o from VenOrder o where o.wcsOrderId ='" + venOrder.getWcsOrderId() +"'", 0, 0);
			_log.debug("\n initial venOrderList size: "+venOrderList.size());				
			
			VenOrder existingVenOrder = venOrderList.get(0);
			/*
			 *  If there is a status change then publish it (SF, FC, FP, BlockInfo)
			 *  	- note that BlockInfo can happen in any status but the others must transition from status C, except PF can change to FP
			 */
			Long existingStatus = existingVenOrder.getVenOrderStatus().getOrderStatusId();
			venOrder.setWcsOrderId(existingVenOrder.getWcsOrderId());
			
			Long newStatus = null;
			if(venOrder.getVenOrderStatus() != null){
				newStatus = venOrder.getVenOrderStatus().getOrderStatusId();
				_log.debug("existingStatus: "+existingVenOrder.getVenOrderStatus().getOrderStatusId());
				_log.debug("newStatus: "+venOrder.getVenOrderStatus().getOrderStatusId());
			}
			/*
			 * If the status is null in the incoming update then just set it to the existing
			 * order status so that we don't get an NPE below. This has zero impact.
			 */
			if(venOrder.getVenOrderStatus() == null){
				venOrder.setVenOrderStatus(existingVenOrder.getVenOrderStatus());
				newStatus = venOrder.getVenOrderStatus().getOrderStatusId();
			}
			//If there is no new status then don't test the status conditions just test the blockingInfo
			if ((((existingStatus == VeniceConstants.VEN_ORDER_STATUS_C && newStatus == VeniceConstants.VEN_ORDER_STATUS_SF) 
					|| (existingStatus == VeniceConstants.VEN_ORDER_STATUS_C && newStatus == VeniceConstants.VEN_ORDER_STATUS_FP) 
					|| (existingStatus == VeniceConstants.VEN_ORDER_STATUS_SF && newStatus == VeniceConstants.VEN_ORDER_STATUS_FP) 
					|| (existingStatus == VeniceConstants.VEN_ORDER_STATUS_SF && newStatus == VeniceConstants.VEN_ORDER_STATUS_FC)))
				|| (venOrder.getBlockedFlag() != null && existingStatus != VeniceConstants.VEN_ORDER_STATUS_VA && existingStatus != VeniceConstants.VEN_ORDER_STATUS_X && newStatus != VeniceConstants.VEN_ORDER_STATUS_X)) {
				
				_log.debug("condition for publish order status is true");
				String blockingSource = null;
				if (venOrder.getVenOrderBlockingSource() != null && venOrder.getVenOrderBlockingSource().getBlockingSourceDesc() != null) {					
					blockingSource = venOrder.getVenOrderBlockingSource().getBlockingSourceDesc();
					_log.debug("blockingSource is exist: "+blockingSource);
				}
				_log.debug("start publish order status");
				Publisher publisher = new Publisher();
				publisher.publishUpdateOrderStatus(venOrder, blockingSource);
				_log.debug("done publish order status");
			}			
		} catch (Exception e) {
			String errMsg = "An exception occured when processing a postMerge callback for VenOrderSessionEJBCallback:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			throw new EJBException(errMsg);
		} finally{
			try{
				if(locator!=null){
					locator.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPreRemove(Object businessObject) {
		_log.debug("onPreRemove");
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPostRemove(Object businessObject) {
		_log.debug("onPostRemove");
		return Boolean.TRUE;
	}
}
