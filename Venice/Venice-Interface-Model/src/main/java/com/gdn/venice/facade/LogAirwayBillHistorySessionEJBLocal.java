package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogAirwayBillHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogAirwayBillHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogAirwayBillHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillHistorySessionEJBRemote#persistLogAirwayBillHistory(com
	 * .gdn.venice.persistence.LogAirwayBillHistory)
	 */
	public LogAirwayBillHistory persistLogAirwayBillHistory(LogAirwayBillHistory logAirwayBillHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillHistorySessionEJBRemote#persistLogAirwayBillHistoryList
	 * (java.util.List)
	 */
	public ArrayList<LogAirwayBillHistory> persistLogAirwayBillHistoryList(
			List<LogAirwayBillHistory> logAirwayBillHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillHistorySessionEJBRemote#mergeLogAirwayBillHistory(com.
	 * gdn.venice.persistence.LogAirwayBillHistory)
	 */
	public LogAirwayBillHistory mergeLogAirwayBillHistory(LogAirwayBillHistory logAirwayBillHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillHistorySessionEJBRemote#mergeLogAirwayBillHistoryList(
	 * java.util.List)
	 */
	public ArrayList<LogAirwayBillHistory> mergeLogAirwayBillHistoryList(
			List<LogAirwayBillHistory> logAirwayBillHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillHistorySessionEJBRemote#removeLogAirwayBillHistory(com
	 * .gdn.venice.persistence.LogAirwayBillHistory)
	 */
	public void removeLogAirwayBillHistory(LogAirwayBillHistory logAirwayBillHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillHistorySessionEJBRemote#removeLogAirwayBillHistoryList
	 * (java.util.List)
	 */
	public void removeLogAirwayBillHistoryList(List<LogAirwayBillHistory> logAirwayBillHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillHistorySessionEJBRemote#findByLogAirwayBillHistoryLike
	 * (com.gdn.venice.persistence.LogAirwayBillHistory, int, int)
	 */
	public List<LogAirwayBillHistory> findByLogAirwayBillHistoryLike(LogAirwayBillHistory logAirwayBillHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillHistorySessionEJBRemote#findByLogAirwayBillHistoryLikeFR
	 * (com.gdn.venice.persistence.LogAirwayBillHistory, int, int)
	 */
	public FinderReturn findByLogAirwayBillHistoryLikeFR(LogAirwayBillHistory logAirwayBillHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
