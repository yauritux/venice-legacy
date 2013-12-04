package com.gdn.venice.facade.callback;

import java.util.List;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote;
import com.gdn.venice.facade.VenOrderSessionEJBRemote;
import com.gdn.venice.integration.outbound.Publisher;
import com.gdn.venice.persistence.VenOrder;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.gdn.venice.util.VeniceConstants;

/**
 * VenOrderPaymentSessionEJBCallback.java
 * 
 * This callback object is used to implement the publishing of order payment
 * status for VA payments changes
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class VenOrderPaymentSessionEJBCallback implements SessionCallback {

	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public VenOrderPaymentSessionEJBCallback() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.callback.VenOrderPaymentSessionEJBCallback");
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
		VenOrderPayment venOrderPayment = (VenOrderPayment) businessObject;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();

			VenOrderPaymentSessionEJBRemote orderPaymentHome = (VenOrderPaymentSessionEJBRemote) locator
					.lookup(VenOrderPaymentSessionEJBRemote.class, "VenOrderPaymentSessionEJBBean");

			List<VenOrderPayment> venOrderPaymentList = orderPaymentHome
					.queryByRange("select o from VenOrderPayment o where o.orderPaymentId =" + venOrderPayment.getOrderPaymentId(), 0, 1);
			VenOrderPayment existingVenOrderPayment = venOrderPaymentList.get(0);

			// If the payment type is not a VA payment then return because nothing
			// to publish
			if (existingVenOrderPayment.getVenPaymentType().getPaymentTypeId() != VeniceConstants.VEN_PAYMENT_TYPE_ID_VA 
					&& existingVenOrderPayment.getVenPaymentType().getPaymentTypeId() != VeniceConstants.VEN_PAYMENT_TYPE_ID_CS) {
				return Boolean.TRUE;
			}	

			VenOrderPaymentAllocationSessionEJBRemote orderPaymentAllocationHome = (VenOrderPaymentAllocationSessionEJBRemote) locator
					.lookup(VenOrderPaymentAllocationSessionEJBRemote.class, "VenOrderPaymentAllocationSessionEJBBean");

			//Fetch the allocation and the order for publishing
			List<VenOrderPaymentAllocation> paymentAllocationList = orderPaymentAllocationHome
					.queryByRange("select o from VenOrderPaymentAllocation o where o.venOrderPayment.orderPaymentId =" + venOrderPayment.getOrderPaymentId(), 0, 0);
			
			VenOrderSessionEJBRemote orderHome = (VenOrderSessionEJBRemote) locator
			.lookup(VenOrderSessionEJBRemote.class, "VenOrderSessionEJBBean");
			if(paymentAllocationList.size()>0 && !paymentAllocationList.isEmpty() && paymentAllocationList!=null){
					List<VenOrder> venOrderList = orderHome.queryByRange("select o from VenOrder o where o.orderId =" + paymentAllocationList.get(0).getVenOrder().getOrderId(), 0, 0);
					VenOrder venOrder = venOrderList.get(0);
					
					String wcsOrderId = venOrder.getWcsOrderId();
					venOrderPayment.setWcsPaymentId(existingVenOrderPayment.getWcsPaymentId());
		
					
					Long existingStatus = existingVenOrderPayment.getVenPaymentStatus().getPaymentStatusId();
		
					Long newStatus = venOrderPayment.getVenPaymentStatus().getPaymentStatusId();
					
					/**
					 * VA Payment
					 */
					if(existingVenOrderPayment.getVenPaymentType().getPaymentTypeId() == VeniceConstants.VEN_PAYMENT_TYPE_ID_VA){
						/*
						 * If there is a status change then publish it: 
						 * 	o APPROVING -> APPROVED
						 */
						if (existingStatus == VeniceConstants.VEN_VA_PAYMENT_STATUS_ID_APPROVING && newStatus == VeniceConstants.VEN_VA_PAYMENT_STATUS_ID_APPROVED) {
			
							Publisher publisher = new Publisher();
							publisher.publishUpdateOrderVAPaymentStatus(wcsOrderId, venOrderPayment);
							
						}
					}
					
					/**
					 * CS Payment
					 */
					if(existingVenOrderPayment.getVenPaymentType().getPaymentTypeId() == VeniceConstants.VEN_PAYMENT_TYPE_ID_CS){
						/*
						 * If there is a status change then publish it: 
						 * 	o APPROVING -> APPROVED
						 *     o PENDING -> APPROVED
						 */
						if ((existingStatus == VeniceConstants.VENORDERPAYMENT_PAYMENTSTATUSID_NOT_APPROVED 
								|| existingStatus == VeniceConstants.VENORDERPAYMENT_PAYMENTSTATUSID_PENDING) 
								&& newStatus == VeniceConstants.VENORDERPAYMENT_PAYMENTSTATUSID_APPROVED) {
			
							Publisher publisher = new Publisher();
							publisher.publishUpdateOrderCSPaymentStatus(wcsOrderId, venOrderPayment);
							
						}
						
						/*
						 * If there is a status change then publish it: 
						 * 	o APPROVING -> REJECTED
						 *     o PENDING -> REJECTED
						 */
						if ((existingStatus == VeniceConstants.VENORDERPAYMENT_PAYMENTSTATUSID_NOT_APPROVED 
								|| existingStatus == VeniceConstants.VENORDERPAYMENT_PAYMENTSTATUSID_PENDING) 
								&& newStatus == VeniceConstants.VENORDERPAYMENT_PAYMENTSTATUSID_REJECTED) {
			
							Publisher publisher = new Publisher();
							publisher.publishRejectOrderCSPaymentStatus(wcsOrderId, existingVenOrderPayment);
							
						}
						
						/*
						 * If there is a status change then publish it: 
						 * 	o APPROVING -> PENDING
						 */
						if (existingStatus == VeniceConstants.VENORDERPAYMENT_PAYMENTSTATUSID_NOT_APPROVED && newStatus == VeniceConstants.VENORDERPAYMENT_PAYMENTSTATUSID_PENDING) {
			
							Publisher publisher = new Publisher();
							publisher.publishPendingOrderCSPaymentStatus(wcsOrderId, existingVenOrderPayment);
							
						}
					}
					
			}else{
				_log.debug("Payment Allocation is null");
			}
		} catch (Exception e) {
			String errMsg = "An exception occured when processing a preMerge callback for VenOrderPaymentSessionEJBCallback:" + e.getMessage();
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
