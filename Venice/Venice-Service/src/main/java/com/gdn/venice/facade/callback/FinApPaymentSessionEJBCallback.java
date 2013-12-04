package com.gdn.venice.facade.callback;

import java.util.List;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.FinApInvoiceSessionEJBRemote;
import com.gdn.venice.facade.FinApPaymentSessionEJBRemote;
import com.gdn.venice.facade.finance.journal.FinanceJournalPosterSessionEJBRemote;
import com.gdn.venice.persistence.FinApInvoice;
import com.gdn.venice.persistence.FinApPayment;
import com.gdn.venice.util.VeniceConstants;

/**
 * FinApPaymentSessionEJBCallback.java - a callback class for trapping 
 * approval of AP payments.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class FinApPaymentSessionEJBCallback implements
		SessionCallback {

	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public FinApPaymentSessionEJBCallback() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.callback.FinApPaymentSessionEJBCallback");
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
		FinApPayment finApPayment = (FinApPayment) businessObject;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			FinApPaymentSessionEJBRemote finApPaymentHome = (FinApPaymentSessionEJBRemote) locator
					.lookup(FinApPaymentSessionEJBRemote.class, "FinApPaymentSessionEJBBean");
			
			FinApInvoiceSessionEJBRemote finApInvoiceHome = (FinApInvoiceSessionEJBRemote) locator
			.lookup(FinApInvoiceSessionEJBRemote.class, "FinApInvoiceSessionEJBBean");

			FinApPayment finApPaymentExisting = finApPaymentHome.queryByRange("select o from FinApPayment o where o.apPaymentId = " + finApPayment.getApPaymentId(), 0, 0).get(0);
	
			//If approved for the first time then call the journal poster 
			if(finApPaymentExisting.getFinApprovalStatus() != null && finApPaymentExisting.getFinApprovalStatus().getApprovalStatusId() != VeniceConstants.FIN_APPROVAL_STATUS_APPROVED 
					&& finApPayment.getFinApprovalStatus() != null && finApPayment.getFinApprovalStatus().getApprovalStatusId() != null && finApPayment.getFinApprovalStatus().getApprovalStatusId() == VeniceConstants.FIN_APPROVAL_STATUS_APPROVED){
				
				/*
				 * Set the AR amount for the logistics invoice if any 
				 * penalty is applied by finance on the payment
				 * 
				 *  Note that the penalty amount originates from a manual
				 *  journal entry in piutang usaha
				 *  
				 *  Note that there will only be one invoice for a payment
				 *  so this is safe
				 */
				List<FinApInvoice> finApInvoiceList = finApInvoiceHome.queryByRange("select o from FinApInvoice o where o.finApPayment.apPaymentId = " + finApPayment.getApPaymentId(), 0, 0);
				
				for(FinApInvoice finApInvoice:finApInvoiceList){
					finApInvoice.setArAmount(finApPayment.getPenaltyAmount());
					finApInvoice = finApInvoiceHome.mergeFinApInvoice(finApInvoice);
				}
			}

		} catch (Exception e) {
			String errMsg = "An exception occured when processing the callback for FinApPaymentSessionEJBCallback:" + e.getMessage();
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
	 * com.gdn.venice.facade.callback.SessionCallback#onPostMerge(java.lang.
	 * Object)
	 */
	@Override
	public Boolean onPostMerge(Object businessObject) {
		_log.debug("onPostMerge");
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
