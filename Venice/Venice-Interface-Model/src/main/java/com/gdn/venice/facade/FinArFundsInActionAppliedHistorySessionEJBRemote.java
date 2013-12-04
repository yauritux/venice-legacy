package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArFundsInActionAppliedHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinArFundsInActionAppliedHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinArFundsInActionAppliedHistory
	 */
	public List<FinArFundsInActionAppliedHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinArFundsInActionAppliedHistory persists a country
	 * 
	 * @param finArFundsInActionAppliedHistory
	 * @return the persisted FinArFundsInActionAppliedHistory
	 */
	public FinArFundsInActionAppliedHistory persistFinArFundsInActionAppliedHistory(FinArFundsInActionAppliedHistory finArFundsInActionAppliedHistory);

	/**
	 * persistFinArFundsInActionAppliedHistoryList - persists a list of FinArFundsInActionAppliedHistory
	 * 
	 * @param finArFundsInActionAppliedHistoryList
	 * @return the list of persisted FinArFundsInActionAppliedHistory
	 */
	public ArrayList<FinArFundsInActionAppliedHistory> persistFinArFundsInActionAppliedHistoryList(
			List<FinArFundsInActionAppliedHistory> finArFundsInActionAppliedHistoryList);

	/**
	 * mergeFinArFundsInActionAppliedHistory - merges a FinArFundsInActionAppliedHistory
	 * 
	 * @param finArFundsInActionAppliedHistory
	 * @return the merged FinArFundsInActionAppliedHistory
	 */
	public FinArFundsInActionAppliedHistory mergeFinArFundsInActionAppliedHistory(FinArFundsInActionAppliedHistory finArFundsInActionAppliedHistory);

	/**
	 * mergeFinArFundsInActionAppliedHistoryList - merges a list of FinArFundsInActionAppliedHistory
	 * 
	 * @param finArFundsInActionAppliedHistoryList
	 * @return the merged list of FinArFundsInActionAppliedHistory
	 */
	public ArrayList<FinArFundsInActionAppliedHistory> mergeFinArFundsInActionAppliedHistoryList(
			List<FinArFundsInActionAppliedHistory> finArFundsInActionAppliedHistoryList);

	/**
	 * removeFinArFundsInActionAppliedHistory - removes a FinArFundsInActionAppliedHistory
	 * 
	 * @param finArFundsInActionAppliedHistory
	 */
	public void removeFinArFundsInActionAppliedHistory(FinArFundsInActionAppliedHistory finArFundsInActionAppliedHistory);

	/**
	 * removeFinArFundsInActionAppliedHistoryList - removes a list of FinArFundsInActionAppliedHistory
	 * 
	 * @param finArFundsInActionAppliedHistoryList
	 */
	public void removeFinArFundsInActionAppliedHistoryList(List<FinArFundsInActionAppliedHistory> finArFundsInActionAppliedHistoryList);

	/**
	 * findByFinArFundsInActionAppliedHistoryLike - finds a list of FinArFundsInActionAppliedHistory Like
	 * 
	 * @param finArFundsInActionAppliedHistory
	 * @return the list of FinArFundsInActionAppliedHistory found
	 */
	public List<FinArFundsInActionAppliedHistory> findByFinArFundsInActionAppliedHistoryLike(FinArFundsInActionAppliedHistory finArFundsInActionAppliedHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinArFundsInActionAppliedHistory>LikeFR - finds a list of FinArFundsInActionAppliedHistory> Like with a finder return object
	 * 
	 * @param finArFundsInActionAppliedHistory
	 * @return the list of FinArFundsInActionAppliedHistory found
	 */
	public FinderReturn findByFinArFundsInActionAppliedHistoryLikeFR(FinArFundsInActionAppliedHistory finArFundsInActionAppliedHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
