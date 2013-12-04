package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogActivityReconCommentHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogActivityReconCommentHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogActivityReconCommentHistory
	 */
	public List<LogActivityReconCommentHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogActivityReconCommentHistory persists a country
	 * 
	 * @param logActivityReconCommentHistory
	 * @return the persisted LogActivityReconCommentHistory
	 */
	public LogActivityReconCommentHistory persistLogActivityReconCommentHistory(LogActivityReconCommentHistory logActivityReconCommentHistory);

	/**
	 * persistLogActivityReconCommentHistoryList - persists a list of LogActivityReconCommentHistory
	 * 
	 * @param logActivityReconCommentHistoryList
	 * @return the list of persisted LogActivityReconCommentHistory
	 */
	public ArrayList<LogActivityReconCommentHistory> persistLogActivityReconCommentHistoryList(
			List<LogActivityReconCommentHistory> logActivityReconCommentHistoryList);

	/**
	 * mergeLogActivityReconCommentHistory - merges a LogActivityReconCommentHistory
	 * 
	 * @param logActivityReconCommentHistory
	 * @return the merged LogActivityReconCommentHistory
	 */
	public LogActivityReconCommentHistory mergeLogActivityReconCommentHistory(LogActivityReconCommentHistory logActivityReconCommentHistory);

	/**
	 * mergeLogActivityReconCommentHistoryList - merges a list of LogActivityReconCommentHistory
	 * 
	 * @param logActivityReconCommentHistoryList
	 * @return the merged list of LogActivityReconCommentHistory
	 */
	public ArrayList<LogActivityReconCommentHistory> mergeLogActivityReconCommentHistoryList(
			List<LogActivityReconCommentHistory> logActivityReconCommentHistoryList);

	/**
	 * removeLogActivityReconCommentHistory - removes a LogActivityReconCommentHistory
	 * 
	 * @param logActivityReconCommentHistory
	 */
	public void removeLogActivityReconCommentHistory(LogActivityReconCommentHistory logActivityReconCommentHistory);

	/**
	 * removeLogActivityReconCommentHistoryList - removes a list of LogActivityReconCommentHistory
	 * 
	 * @param logActivityReconCommentHistoryList
	 */
	public void removeLogActivityReconCommentHistoryList(List<LogActivityReconCommentHistory> logActivityReconCommentHistoryList);

	/**
	 * findByLogActivityReconCommentHistoryLike - finds a list of LogActivityReconCommentHistory Like
	 * 
	 * @param logActivityReconCommentHistory
	 * @return the list of LogActivityReconCommentHistory found
	 */
	public List<LogActivityReconCommentHistory> findByLogActivityReconCommentHistoryLike(LogActivityReconCommentHistory logActivityReconCommentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogActivityReconCommentHistory>LikeFR - finds a list of LogActivityReconCommentHistory> Like with a finder return object
	 * 
	 * @param logActivityReconCommentHistory
	 * @return the list of LogActivityReconCommentHistory found
	 */
	public FinderReturn findByLogActivityReconCommentHistoryLikeFR(LogActivityReconCommentHistory logActivityReconCommentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
