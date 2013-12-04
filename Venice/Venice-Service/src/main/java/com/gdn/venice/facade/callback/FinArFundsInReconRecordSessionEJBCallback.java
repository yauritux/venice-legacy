package com.gdn.venice.facade.callback;

import java.math.BigDecimal;
import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote;
import com.gdn.venice.facade.VenOrderPaymentSessionEJBRemote;
import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.gdn.venice.persistence.VenOrderPayment;
import com.gdn.venice.persistence.VenPaymentStatus;
import com.gdn.venice.util.VeniceConstants;

/**
 * FinArFundsInReconRecordSessionEJBCallback.java - a callback class for trapping 
 * approval of the funds in records and setting the approval status of AR payments.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class FinArFundsInReconRecordSessionEJBCallback implements
		SessionCallback {

	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public FinArFundsInReconRecordSessionEJBCallback() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.callback.FinArFundsInReconRecordSessionEJBCallback");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPrePersist(java.lang
	 * .Object)
	 */
	@Override
	public Boolean onPrePersist(Object businessObject) {
		_log.debug("onPrePersist");
		FinArFundsInReconRecord finArFundsInReconRecord = (FinArFundsInReconRecord) businessObject;
		try {		
			if(finArFundsInReconRecord.getRefundAmount()==null){
				finArFundsInReconRecord.setRefundAmount(new BigDecimal(0));
			}
			_log.debug("refund Amount = "+finArFundsInReconRecord.getRefundAmount());
		} catch (Exception e) {
			String errMsg = "An exception occured when processing the callback for FinArFundsInReconRecordSessionEJBCallback:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			return Boolean.FALSE;
		} finally{			
		}
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPostPersist(java.lang
	 * .Object)
	 */
	@Override
	public Boolean onPostPersist(Object businessObject) {
		_log.debug("onPostPersist");
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPreMerge(java.lang.Object
	 * )
	 */
	@Override
	public Boolean onPreMerge(Object businessObject) {
		_log.debug("onPreMerge");
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPostMerge(java.lang.
	 * Object)
	 */
	@Override
	public Boolean onPostMerge(Object businessObject) {
		_log.debug("onPostMerge");
		FinArFundsInReconRecord finArFundsInReconRecord = (FinArFundsInReconRecord) businessObject;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FinArFundsInReconRecordSessionEJBRemote finArFundsInReconRecordHome = (FinArFundsInReconRecordSessionEJBRemote) locator
			.lookup(FinArFundsInReconRecordSessionEJBRemote.class, "FinArFundsInReconRecordSessionEJBBean");
			
			VenOrderPaymentSessionEJBRemote venOrderPaymentHome = (VenOrderPaymentSessionEJBRemote) locator
			.lookup(VenOrderPaymentSessionEJBRemote.class, "VenOrderPaymentSessionEJBBean");

			List<FinArFundsInReconRecord> finArFundsInReconRecordList = finArFundsInReconRecordHome.queryByRange("select o from FinArFundsInReconRecord o where o.reconciliationRecordId = " + finArFundsInReconRecord.getReconciliationRecordId()+" and o.finArFundsInActionApplied.actionAppliedId<>"+VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_REMOVED, 0, 0);
			
			FinArFundsInReconRecord existingFinArFundsInReconRecord = new FinArFundsInReconRecord();
			if(finArFundsInReconRecordList.size()>0){
				existingFinArFundsInReconRecord = finArFundsInReconRecordList.get(0);
			}
			
			/*
			 * If the status of the funds in record is approved for the first time then approve the payment
			 */
			if(finArFundsInReconRecord.getFinApprovalStatus().getApprovalStatusId() == VeniceConstants.FIN_APPROVAL_STATUS_APPROVED
					&& (existingFinArFundsInReconRecord.getFinApprovalStatus()!=null?existingFinArFundsInReconRecord.getFinApprovalStatus().getApprovalStatusId() != VeniceConstants.FIN_APPROVAL_STATUS_APPROVED : false)){
				/**
				 * order VA menjadi C jika Status All Fund dan Tidak di action taken apapun Venice228
				 */
				boolean trueorfalse = existingFinArFundsInReconRecord.getFinArFundsInActionApplied().getActionAppliedId().equals(VeniceConstants.FIN_AR_FUNDS_IN_ACTION_APPLIED_NONE)
				&& existingFinArFundsInReconRecord.getFinArReconResult().getReconResultId().equals(VeniceConstants.FIN_AR_RECON_RESULT_ALL);
				
				if (existingFinArFundsInReconRecord.getVenOrderPayment()!=null && trueorfalse) {	
						//Set the status of the payment for VA payments to approved
						List<VenOrderPayment> venOrderPaymentList = venOrderPaymentHome.queryByRange("select o from VenOrderPayment o where o.orderPaymentId = " + existingFinArFundsInReconRecord.getVenOrderPayment().getOrderPaymentId(), 0, 0);
						VenOrderPayment venOrderPayment = venOrderPaymentList.get(0);
						if(venOrderPayment.getVenPaymentType().getPaymentTypeId() == VeniceConstants.VEN_PAYMENT_TYPE_ID_VA){
							VenPaymentStatus venPaymentStatus = new VenPaymentStatus();
							venPaymentStatus.setPaymentStatusId(VeniceConstants.VEN_VA_PAYMENT_STATUS_ID_APPROVED);
							venOrderPayment.setVenPaymentStatus(venPaymentStatus);
							//Merge the payment - this will trigger the sending of status change etc. 
					    	venOrderPaymentHome.mergeVenOrderPayment(venOrderPayment);
						}
				}
			}
		} catch (Exception e) {
			String errMsg = "An exception occured when processing the callback for FinArFundsInReconRecordSessionEJBCallback:" + e.getMessage();
			_log.error(errMsg);
			e.printStackTrace();
			return Boolean.FALSE;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPreRemove(java.lang.
	 * Object)
	 */
	@Override
	public Boolean onPreRemove(Object businessObject) {
		_log.debug("onPreRemove");
		return Boolean.TRUE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.callback.SessionCallback#onPostRemove(java.lang
	 * .Object)
	 */
	@Override
	public Boolean onPostRemove(Object businessObject) {
		_log.debug("onPostRemove");
		return Boolean.TRUE;
	}
}
