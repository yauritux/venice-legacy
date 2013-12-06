package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogAirwayBillHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogAirwayBillHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogAirwayBillHistory
	 */
	public List<LogAirwayBillHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogAirwayBillHistory persists a country
	 * 
	 * @param logAirwayBillHistory
	 * @return the persisted LogAirwayBillHistory
	 */
	public LogAirwayBillHistory persistLogAirwayBillHistory(LogAirwayBillHistory logAirwayBillHistory);

	/**
	 * persistLogAirwayBillHistoryList - persists a list of LogAirwayBillHistory
	 * 
	 * @param logAirwayBillHistoryList
	 * @return the list of persisted LogAirwayBillHistory
	 */
	public ArrayList<LogAirwayBillHistory> persistLogAirwayBillHistoryList(
			List<LogAirwayBillHistory> logAirwayBillHistoryList);

	/**
	 * mergeLogAirwayBillHistory - merges a LogAirwayBillHistory
	 * 
	 * @param logAirwayBillHistory
	 * @return the merged LogAirwayBillHistory
	 */
	public LogAirwayBillHistory mergeLogAirwayBillHistory(LogAirwayBillHistory logAirwayBillHistory);

	/**
	 * mergeLogAirwayBillHistoryList - merges a list of LogAirwayBillHistory
	 * 
	 * @param logAirwayBillHistoryList
	 * @return the merged list of LogAirwayBillHistory
	 */
	public ArrayList<LogAirwayBillHistory> mergeLogAirwayBillHistoryList(
			List<LogAirwayBillHistory> logAirwayBillHistoryList);

	/**
	 * removeLogAirwayBillHistory - removes a LogAirwayBillHistory
	 * 
	 * @param logAirwayBillHistory
	 */
	public void removeLogAirwayBillHistory(LogAirwayBillHistory logAirwayBillHistory);

	/**
	 * removeLogAirwayBillHistoryList - removes a list of LogAirwayBillHistory
	 * 
	 * @param logAirwayBillHistoryList
	 */
	public void removeLogAirwayBillHistoryList(List<LogAirwayBillHistory> logAirwayBillHistoryList);

	/**
	 * findByLogAirwayBillHistoryLike - finds a list of LogAirwayBillHistory Like
	 * 
	 * @param logAirwayBillHistory
	 * @return the list of LogAirwayBillHistory found
	 */
	public List<LogAirwayBillHistory> findByLogAirwayBillHistoryLike(LogAirwayBillHistory logAirwayBillHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogAirwayBillHistory>LikeFR - finds a list of LogAirwayBillHistory> Like with a finder return object
	 * 
	 * @param logAirwayBillHistory
	 * @return the list of LogAirwayBillHistory found
	 */
	public FinderReturn findByLogAirwayBillHistoryLikeFR(LogAirwayBillHistory logAirwayBillHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
