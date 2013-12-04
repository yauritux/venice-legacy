package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogInvoiceReconCommentHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogInvoiceReconCommentHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogInvoiceReconCommentHistory
	 */
	public List<LogInvoiceReconCommentHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogInvoiceReconCommentHistory persists a country
	 * 
	 * @param logInvoiceReconCommentHistory
	 * @return the persisted LogInvoiceReconCommentHistory
	 */
	public LogInvoiceReconCommentHistory persistLogInvoiceReconCommentHistory(LogInvoiceReconCommentHistory logInvoiceReconCommentHistory);

	/**
	 * persistLogInvoiceReconCommentHistoryList - persists a list of LogInvoiceReconCommentHistory
	 * 
	 * @param logInvoiceReconCommentHistoryList
	 * @return the list of persisted LogInvoiceReconCommentHistory
	 */
	public ArrayList<LogInvoiceReconCommentHistory> persistLogInvoiceReconCommentHistoryList(
			List<LogInvoiceReconCommentHistory> logInvoiceReconCommentHistoryList);

	/**
	 * mergeLogInvoiceReconCommentHistory - merges a LogInvoiceReconCommentHistory
	 * 
	 * @param logInvoiceReconCommentHistory
	 * @return the merged LogInvoiceReconCommentHistory
	 */
	public LogInvoiceReconCommentHistory mergeLogInvoiceReconCommentHistory(LogInvoiceReconCommentHistory logInvoiceReconCommentHistory);

	/**
	 * mergeLogInvoiceReconCommentHistoryList - merges a list of LogInvoiceReconCommentHistory
	 * 
	 * @param logInvoiceReconCommentHistoryList
	 * @return the merged list of LogInvoiceReconCommentHistory
	 */
	public ArrayList<LogInvoiceReconCommentHistory> mergeLogInvoiceReconCommentHistoryList(
			List<LogInvoiceReconCommentHistory> logInvoiceReconCommentHistoryList);

	/**
	 * removeLogInvoiceReconCommentHistory - removes a LogInvoiceReconCommentHistory
	 * 
	 * @param logInvoiceReconCommentHistory
	 */
	public void removeLogInvoiceReconCommentHistory(LogInvoiceReconCommentHistory logInvoiceReconCommentHistory);

	/**
	 * removeLogInvoiceReconCommentHistoryList - removes a list of LogInvoiceReconCommentHistory
	 * 
	 * @param logInvoiceReconCommentHistoryList
	 */
	public void removeLogInvoiceReconCommentHistoryList(List<LogInvoiceReconCommentHistory> logInvoiceReconCommentHistoryList);

	/**
	 * findByLogInvoiceReconCommentHistoryLike - finds a list of LogInvoiceReconCommentHistory Like
	 * 
	 * @param logInvoiceReconCommentHistory
	 * @return the list of LogInvoiceReconCommentHistory found
	 */
	public List<LogInvoiceReconCommentHistory> findByLogInvoiceReconCommentHistoryLike(LogInvoiceReconCommentHistory logInvoiceReconCommentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogInvoiceReconCommentHistory>LikeFR - finds a list of LogInvoiceReconCommentHistory> Like with a finder return object
	 * 
	 * @param logInvoiceReconCommentHistory
	 * @return the list of LogInvoiceReconCommentHistory found
	 */
	public FinderReturn findByLogInvoiceReconCommentHistoryLikeFR(LogInvoiceReconCommentHistory logInvoiceReconCommentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
