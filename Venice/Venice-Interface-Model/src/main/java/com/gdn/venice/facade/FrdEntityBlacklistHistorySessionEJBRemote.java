package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdEntityBlacklistHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdEntityBlacklistHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdEntityBlacklistHistory
	 */
	public List<FrdEntityBlacklistHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdEntityBlacklistHistory persists a country
	 * 
	 * @param frdEntityBlacklistHistory
	 * @return the persisted FrdEntityBlacklistHistory
	 */
	public FrdEntityBlacklistHistory persistFrdEntityBlacklistHistory(FrdEntityBlacklistHistory frdEntityBlacklistHistory);

	/**
	 * persistFrdEntityBlacklistHistoryList - persists a list of FrdEntityBlacklistHistory
	 * 
	 * @param frdEntityBlacklistHistoryList
	 * @return the list of persisted FrdEntityBlacklistHistory
	 */
	public ArrayList<FrdEntityBlacklistHistory> persistFrdEntityBlacklistHistoryList(
			List<FrdEntityBlacklistHistory> frdEntityBlacklistHistoryList);

	/**
	 * mergeFrdEntityBlacklistHistory - merges a FrdEntityBlacklistHistory
	 * 
	 * @param frdEntityBlacklistHistory
	 * @return the merged FrdEntityBlacklistHistory
	 */
	public FrdEntityBlacklistHistory mergeFrdEntityBlacklistHistory(FrdEntityBlacklistHistory frdEntityBlacklistHistory);

	/**
	 * mergeFrdEntityBlacklistHistoryList - merges a list of FrdEntityBlacklistHistory
	 * 
	 * @param frdEntityBlacklistHistoryList
	 * @return the merged list of FrdEntityBlacklistHistory
	 */
	public ArrayList<FrdEntityBlacklistHistory> mergeFrdEntityBlacklistHistoryList(
			List<FrdEntityBlacklistHistory> frdEntityBlacklistHistoryList);

	/**
	 * removeFrdEntityBlacklistHistory - removes a FrdEntityBlacklistHistory
	 * 
	 * @param frdEntityBlacklistHistory
	 */
	public void removeFrdEntityBlacklistHistory(FrdEntityBlacklistHistory frdEntityBlacklistHistory);

	/**
	 * removeFrdEntityBlacklistHistoryList - removes a list of FrdEntityBlacklistHistory
	 * 
	 * @param frdEntityBlacklistHistoryList
	 */
	public void removeFrdEntityBlacklistHistoryList(List<FrdEntityBlacklistHistory> frdEntityBlacklistHistoryList);

	/**
	 * findByFrdEntityBlacklistHistoryLike - finds a list of FrdEntityBlacklistHistory Like
	 * 
	 * @param frdEntityBlacklistHistory
	 * @return the list of FrdEntityBlacklistHistory found
	 */
	public List<FrdEntityBlacklistHistory> findByFrdEntityBlacklistHistoryLike(FrdEntityBlacklistHistory frdEntityBlacklistHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdEntityBlacklistHistory>LikeFR - finds a list of FrdEntityBlacklistHistory> Like with a finder return object
	 * 
	 * @param frdEntityBlacklistHistory
	 * @return the list of FrdEntityBlacklistHistory found
	 */
	public FinderReturn findByFrdEntityBlacklistHistoryLikeFR(FrdEntityBlacklistHistory frdEntityBlacklistHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
