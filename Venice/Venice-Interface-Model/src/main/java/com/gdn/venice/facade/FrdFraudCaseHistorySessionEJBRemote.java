package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdFraudCaseHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdFraudCaseHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdFraudCaseHistory
	 */
	public List<FrdFraudCaseHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdFraudCaseHistory persists a country
	 * 
	 * @param frdFraudCaseHistory
	 * @return the persisted FrdFraudCaseHistory
	 */
	public FrdFraudCaseHistory persistFrdFraudCaseHistory(FrdFraudCaseHistory frdFraudCaseHistory);

	/**
	 * persistFrdFraudCaseHistoryList - persists a list of FrdFraudCaseHistory
	 * 
	 * @param frdFraudCaseHistoryList
	 * @return the list of persisted FrdFraudCaseHistory
	 */
	public ArrayList<FrdFraudCaseHistory> persistFrdFraudCaseHistoryList(
			List<FrdFraudCaseHistory> frdFraudCaseHistoryList);

	/**
	 * mergeFrdFraudCaseHistory - merges a FrdFraudCaseHistory
	 * 
	 * @param frdFraudCaseHistory
	 * @return the merged FrdFraudCaseHistory
	 */
	public FrdFraudCaseHistory mergeFrdFraudCaseHistory(FrdFraudCaseHistory frdFraudCaseHistory);

	/**
	 * mergeFrdFraudCaseHistoryList - merges a list of FrdFraudCaseHistory
	 * 
	 * @param frdFraudCaseHistoryList
	 * @return the merged list of FrdFraudCaseHistory
	 */
	public ArrayList<FrdFraudCaseHistory> mergeFrdFraudCaseHistoryList(
			List<FrdFraudCaseHistory> frdFraudCaseHistoryList);

	/**
	 * removeFrdFraudCaseHistory - removes a FrdFraudCaseHistory
	 * 
	 * @param frdFraudCaseHistory
	 */
	public void removeFrdFraudCaseHistory(FrdFraudCaseHistory frdFraudCaseHistory);

	/**
	 * removeFrdFraudCaseHistoryList - removes a list of FrdFraudCaseHistory
	 * 
	 * @param frdFraudCaseHistoryList
	 */
	public void removeFrdFraudCaseHistoryList(List<FrdFraudCaseHistory> frdFraudCaseHistoryList);

	/**
	 * findByFrdFraudCaseHistoryLike - finds a list of FrdFraudCaseHistory Like
	 * 
	 * @param frdFraudCaseHistory
	 * @return the list of FrdFraudCaseHistory found
	 */
	public List<FrdFraudCaseHistory> findByFrdFraudCaseHistoryLike(FrdFraudCaseHistory frdFraudCaseHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdFraudCaseHistory>LikeFR - finds a list of FrdFraudCaseHistory> Like with a finder return object
	 * 
	 * @param frdFraudCaseHistory
	 * @return the list of FrdFraudCaseHistory found
	 */
	public FinderReturn findByFrdFraudCaseHistoryLikeFR(FrdFraudCaseHistory frdFraudCaseHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
