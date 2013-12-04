package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogActivityReconCommentHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogActivityReconCommentHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogActivityReconCommentHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBRemote#persistLogActivityReconCommentHistory(com
	 * .gdn.venice.persistence.LogActivityReconCommentHistory)
	 */
	public LogActivityReconCommentHistory persistLogActivityReconCommentHistory(LogActivityReconCommentHistory logActivityReconCommentHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBRemote#persistLogActivityReconCommentHistoryList
	 * (java.util.List)
	 */
	public ArrayList<LogActivityReconCommentHistory> persistLogActivityReconCommentHistoryList(
			List<LogActivityReconCommentHistory> logActivityReconCommentHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBRemote#mergeLogActivityReconCommentHistory(com.
	 * gdn.venice.persistence.LogActivityReconCommentHistory)
	 */
	public LogActivityReconCommentHistory mergeLogActivityReconCommentHistory(LogActivityReconCommentHistory logActivityReconCommentHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBRemote#mergeLogActivityReconCommentHistoryList(
	 * java.util.List)
	 */
	public ArrayList<LogActivityReconCommentHistory> mergeLogActivityReconCommentHistoryList(
			List<LogActivityReconCommentHistory> logActivityReconCommentHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBRemote#removeLogActivityReconCommentHistory(com
	 * .gdn.venice.persistence.LogActivityReconCommentHistory)
	 */
	public void removeLogActivityReconCommentHistory(LogActivityReconCommentHistory logActivityReconCommentHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBRemote#removeLogActivityReconCommentHistoryList
	 * (java.util.List)
	 */
	public void removeLogActivityReconCommentHistoryList(List<LogActivityReconCommentHistory> logActivityReconCommentHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBRemote#findByLogActivityReconCommentHistoryLike
	 * (com.gdn.venice.persistence.LogActivityReconCommentHistory, int, int)
	 */
	public List<LogActivityReconCommentHistory> findByLogActivityReconCommentHistoryLike(LogActivityReconCommentHistory logActivityReconCommentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconCommentHistorySessionEJBRemote#findByLogActivityReconCommentHistoryLikeFR
	 * (com.gdn.venice.persistence.LogActivityReconCommentHistory, int, int)
	 */
	public FinderReturn findByLogActivityReconCommentHistoryLikeFR(LogActivityReconCommentHistory logActivityReconCommentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
