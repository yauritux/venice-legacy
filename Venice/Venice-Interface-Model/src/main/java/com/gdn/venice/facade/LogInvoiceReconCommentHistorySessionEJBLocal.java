package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogInvoiceReconCommentHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogInvoiceReconCommentHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogInvoiceReconCommentHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBRemote#persistLogInvoiceReconCommentHistory(com
	 * .gdn.venice.persistence.LogInvoiceReconCommentHistory)
	 */
	public LogInvoiceReconCommentHistory persistLogInvoiceReconCommentHistory(LogInvoiceReconCommentHistory logInvoiceReconCommentHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBRemote#persistLogInvoiceReconCommentHistoryList
	 * (java.util.List)
	 */
	public ArrayList<LogInvoiceReconCommentHistory> persistLogInvoiceReconCommentHistoryList(
			List<LogInvoiceReconCommentHistory> logInvoiceReconCommentHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBRemote#mergeLogInvoiceReconCommentHistory(com.
	 * gdn.venice.persistence.LogInvoiceReconCommentHistory)
	 */
	public LogInvoiceReconCommentHistory mergeLogInvoiceReconCommentHistory(LogInvoiceReconCommentHistory logInvoiceReconCommentHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBRemote#mergeLogInvoiceReconCommentHistoryList(
	 * java.util.List)
	 */
	public ArrayList<LogInvoiceReconCommentHistory> mergeLogInvoiceReconCommentHistoryList(
			List<LogInvoiceReconCommentHistory> logInvoiceReconCommentHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBRemote#removeLogInvoiceReconCommentHistory(com
	 * .gdn.venice.persistence.LogInvoiceReconCommentHistory)
	 */
	public void removeLogInvoiceReconCommentHistory(LogInvoiceReconCommentHistory logInvoiceReconCommentHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBRemote#removeLogInvoiceReconCommentHistoryList
	 * (java.util.List)
	 */
	public void removeLogInvoiceReconCommentHistoryList(List<LogInvoiceReconCommentHistory> logInvoiceReconCommentHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBRemote#findByLogInvoiceReconCommentHistoryLike
	 * (com.gdn.venice.persistence.LogInvoiceReconCommentHistory, int, int)
	 */
	public List<LogInvoiceReconCommentHistory> findByLogInvoiceReconCommentHistoryLike(LogInvoiceReconCommentHistory logInvoiceReconCommentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconCommentHistorySessionEJBRemote#findByLogInvoiceReconCommentHistoryLikeFR
	 * (com.gdn.venice.persistence.LogInvoiceReconCommentHistory, int, int)
	 */
	public FinderReturn findByLogInvoiceReconCommentHistoryLikeFR(LogInvoiceReconCommentHistory logInvoiceReconCommentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
