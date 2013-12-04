package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;
import com.gdn.venice.persistence.LogInvoiceUploadLog;

@Local
public interface LogInvoiceUploadLogSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogInvoiceUploadLog> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote#persistLogInvoiceUploadLog(com
	 * .gdn.venice.persistence.LogInvoiceUploadLog)
	 */
	public LogInvoiceUploadLog persistLogInvoiceUploadLog(LogInvoiceUploadLog logInvoiceUploadLog);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote#persistLogInvoiceUploadLogList
	 * (java.util.List)
	 */
	public ArrayList<LogInvoiceUploadLog> persistLogInvoiceUploadLogList(
			List<LogInvoiceUploadLog> logInvoiceUploadLogList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote#mergeLogInvoiceUploadLog(com.
	 * gdn.venice.persistence.LogInvoiceUploadLog)
	 */
	public LogInvoiceUploadLog mergeLogInvoiceUploadLog(LogInvoiceUploadLog logInvoiceUploadLog);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote#mergeLogInvoiceUploadLogList(
	 * java.util.List)
	 */
	public ArrayList<LogInvoiceUploadLog> mergeLogInvoiceUploadLogList(
			List<LogInvoiceUploadLog> logInvoiceUploadLogList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote#removeLogInvoiceUploadLog(com
	 * .gdn.venice.persistence.LogInvoiceUploadLog)
	 */
	public void removeLogInvoiceUploadLog(LogInvoiceUploadLog logInvoiceUploadLog);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote#removeLogInvoiceUploadLogList
	 * (java.util.List)
	 */
	public void removeLogInvoiceUploadLogList(List<LogInvoiceUploadLog> logInvoiceUploadLogList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote#findByLogInvoiceUploadLogLike
	 * (com.gdn.venice.persistence.LogInvoiceUploadLog, int, int)
	 */
	public List<LogInvoiceUploadLog> findByLogInvoiceUploadLogLike(LogInvoiceUploadLog logInvoiceUploadLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceUploadLogSessionEJBRemote#findByLogInvoiceUploadLogLikeFR
	 * (com.gdn.venice.persistence.LogInvoiceUploadLog, int, int)
	 */
	public FinderReturn findByLogInvoiceUploadLogLikeFR(LogInvoiceUploadLog logInvoiceUploadLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
