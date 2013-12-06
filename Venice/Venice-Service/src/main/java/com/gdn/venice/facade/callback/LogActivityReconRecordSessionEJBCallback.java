package com.gdn.venice.facade.callback;

import javax.ejb.EJBException;

import org.apache.log4j.Logger;

import com.djarum.raf.utilities.Locator;
import com.djarum.raf.utilities.Log4jLoggerFactory;
import com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBLocal;
import com.gdn.venice.persistence.LogActivityReconCommentHistory;
import com.gdn.venice.persistence.LogActivityReconCommentHistoryPK;
import com.gdn.venice.persistence.LogActivityReconRecord;

/**
 * LogActivityReconRecordSessionEJBCallback.java
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
public class LogActivityReconRecordSessionEJBCallback implements SessionCallback {

	protected static Logger _log = null;

	/**
	 * Default constructor.
	 */
	public LogActivityReconRecordSessionEJBCallback() {
		super();
		Log4jLoggerFactory loggerFactory = new Log4jLoggerFactory();
		_log = loggerFactory.getLog4JLogger("com.gdn.venice.facade.callback.LogActivityReconRecordSessionEJBCallback");
	}

	@Override
	public Boolean onPrePersist(Object businessObject) {
		_log.debug("onPrePersist");
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPostPersist(Object businessObject) {
		_log.debug("onPostPersist");
		LogActivityReconRecord logActivityReconRecord = (LogActivityReconRecord) businessObject;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			
			LogActivityReconCommentHistorySessionEJBLocal commentHistorybean = (LogActivityReconCommentHistorySessionEJBLocal) locator
					.lookupLocal(LogActivityReconCommentHistorySessionEJBLocal.class, "LogActivityReconCommentHistorySessionEJBBeanLocal");	
			
			LogActivityReconCommentHistory history = new LogActivityReconCommentHistory();
			LogActivityReconCommentHistoryPK id = new LogActivityReconCommentHistoryPK();
			id.setHistoryTimestamp(new java.util.Date());
			id.setActivityReconRecordId(logActivityReconRecord.getActivityReconRecordId());
			history.setId(id);
			history.setComment(logActivityReconRecord.getComment());
			history.setLogActivityReconRecord(logActivityReconRecord);
			history.setUserLogonName(logActivityReconRecord.getUserLogonName()!=null?logActivityReconRecord.getUserLogonName():"System");
			
			commentHistorybean.persistLogActivityReconCommentHistory(history);
		} catch (Exception e) {
			String errMsg = "An exception occured when processing a preMerge callback for LogActivityReconRecordSessionEJBCallback:" + e.getMessage();
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
	public Boolean onPreMerge(Object businessObject) {
		_log.debug("onPreMerge");
		return Boolean.TRUE;
	}

	@Override
	public Boolean onPostMerge(Object businessObject) {
		_log.debug("onPostMerge");
		LogActivityReconRecord logActivityReconRecord = (LogActivityReconRecord) businessObject;
		Locator<Object> locator = null;
		try {
			locator = new Locator<Object>();
			
			LogActivityReconCommentHistorySessionEJBLocal commentHistorybean = (LogActivityReconCommentHistorySessionEJBLocal) locator
					.lookupLocal(LogActivityReconCommentHistorySessionEJBLocal.class, "LogActivityReconCommentHistorySessionEJBBeanLocal");	
			
			LogActivityReconCommentHistory history = new LogActivityReconCommentHistory();
			LogActivityReconCommentHistoryPK id = new LogActivityReconCommentHistoryPK();
			id.setHistoryTimestamp(new java.util.Date());
			id.setActivityReconRecordId(logActivityReconRecord.getActivityReconRecordId());
			history.setId(id);
			history.setComment(logActivityReconRecord.getComment());
			history.setLogActivityReconRecord(logActivityReconRecord);
			history.setUserLogonName(logActivityReconRecord.getUserLogonName()!=null?logActivityReconRecord.getUserLogonName():"System");
			
			commentHistorybean.persistLogActivityReconCommentHistory(history);
		} catch (Exception e) {
			String errMsg = "An exception occured when processing a preMerge callback for LogActivityReconRecordSessionEJBCallback:" + e.getMessage();
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
