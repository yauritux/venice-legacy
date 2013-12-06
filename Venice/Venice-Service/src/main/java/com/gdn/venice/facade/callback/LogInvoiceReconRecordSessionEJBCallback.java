package com.gdn.venice.facade.callback;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBLocal;
import com.gdn.venice.persistence.LogInvoiceReconCommentHistory;
import com.gdn.venice.persistence.LogInvoiceReconCommentHistoryPK;
import com.gdn.venice.persistence.LogInvoiceReconRecord;

/**
 * LogInvoiceReconRecordSessionEJBCallback.java
 * 
 * This callback object is used to history for comments on activity reconciliation
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 */
public class LogInvoiceReconRecordSessionEJBCallback implements SessionCallback {

	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public LogInvoiceReconRecordSessionEJBCallback() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.callback.LogInvoiceReconRecordSessionEJBCallback");
	}

	@Override
	public Boolean onPrePersist(Object businessObject) {
		_log.debug("onPrePersist");
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPostPersist(Object businessObject) {
		_log.debug("onPostPersist");
		
		/*
		 * This will not work because the transaction has not yet been committed
		 * and this EJB is entering the container in a new transaction context!!
		 * 
		 * It will result in a FK violation
		 */
//		LogInvoiceReconRecord logInvoiceReconRecord = (LogInvoiceReconRecord) businessObject;
//		Locator<Object> locator = null;
//		try {
//			locator = new Locator<Object>();
//
//			LogInvoiceReconCommentHistorySessionEJBRemote commentHistorybean = (LogInvoiceReconCommentHistorySessionEJBRemote) locator
//			.lookup(
//					LogInvoiceReconCommentHistorySessionEJBRemote.class,
//					"LogInvoiceReconCommentHistorySessionEJBBean");	
//			
//			LogInvoiceReconCommentHistory history = new LogInvoiceReconCommentHistory();
//			LogInvoiceReconCommentHistoryPK id = new LogInvoiceReconCommentHistoryPK();
//			id.setHistoryTimestamp(new java.util.Date());
//			id.setInvoiceReconRecordId(logInvoiceReconRecord.getInvoiceReconRecordId());
//			history.setId(id);
//			history.setComment(logInvoiceReconRecord.getComment());
//			history.setLogInvoiceReconRecord(logInvoiceReconRecord);
//			history.setUserLogonName(logInvoiceReconRecord.getUserLogonName() != null?logInvoiceReconRecord.getUserLogonName(): "System");
//	
//			
//			commentHistorybean.persistLogInvoiceReconCommentHistory(history);
//		} catch (Exception e) {
//			String errMsg = "An exception occured when processing a preMerge callback for LogInvoiceReconRecordSessionEJBCallback:"
//					+ e.getMessage();
//			_log.error(errMsg);
//			e.printStackTrace();
//			throw new EJBException(errMsg);
//		} finally{
//		try{
//			if(locator!=null){
//				locator.close();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//	}
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPreMerge(Object businessObject) {
		_log.debug("onPreMerge");
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPostMerge(Object businessObject) {
		_log.debug("onPostMerge");
		LogInvoiceReconRecord logInvoiceReconRecord = (LogInvoiceReconRecord) businessObject;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();

			LogInvoiceReconCommentHistorySessionEJBLocal commentHistorybean = (LogInvoiceReconCommentHistorySessionEJBLocal) locator
			.lookupLocal(LogInvoiceReconCommentHistorySessionEJBLocal.class, "LogInvoiceReconCommentHistorySessionEJBBeanLocal");	
			
			LogInvoiceReconCommentHistory history = new LogInvoiceReconCommentHistory();
			LogInvoiceReconCommentHistoryPK id = new LogInvoiceReconCommentHistoryPK();
			id.setHistoryTimestamp(new java.util.Date());
			id.setInvoiceReconRecordId(logInvoiceReconRecord.getInvoiceReconRecordId());
			history.setId(id);
			history.setComment(logInvoiceReconRecord.getComment());
			history.setLogInvoiceReconRecord(logInvoiceReconRecord);
			history.setUserLogonName(logInvoiceReconRecord.getUserLogonName() != null?logInvoiceReconRecord.getUserLogonName(): "System");
			
			commentHistorybean.persistLogInvoiceReconCommentHistory(history);
		} catch (Exception e) {
			String errMsg = "An exception occured when processing a preMerge callback for LogInvoiceReconRecordSessionEJBCallback:" + e.getMessage();
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
