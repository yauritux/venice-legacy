package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafLoginHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafLoginHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafLoginHistory
	 */
	public List<RafLoginHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafLoginHistory persists a country
	 * 
	 * @param rafLoginHistory
	 * @return the persisted RafLoginHistory
	 */
	public RafLoginHistory persistRafLoginHistory(RafLoginHistory rafLoginHistory);

	/**
	 * persistRafLoginHistoryList - persists a list of RafLoginHistory
	 * 
	 * @param rafLoginHistoryList
	 * @return the list of persisted RafLoginHistory
	 */
	public ArrayList<RafLoginHistory> persistRafLoginHistoryList(
			List<RafLoginHistory> rafLoginHistoryList);

	/**
	 * mergeRafLoginHistory - merges a RafLoginHistory
	 * 
	 * @param rafLoginHistory
	 * @return the merged RafLoginHistory
	 */
	public RafLoginHistory mergeRafLoginHistory(RafLoginHistory rafLoginHistory);

	/**
	 * mergeRafLoginHistoryList - merges a list of RafLoginHistory
	 * 
	 * @param rafLoginHistoryList
	 * @return the merged list of RafLoginHistory
	 */
	public ArrayList<RafLoginHistory> mergeRafLoginHistoryList(
			List<RafLoginHistory> rafLoginHistoryList);

	/**
	 * removeRafLoginHistory - removes a RafLoginHistory
	 * 
	 * @param rafLoginHistory
	 */
	public void removeRafLoginHistory(RafLoginHistory rafLoginHistory);

	/**
	 * removeRafLoginHistoryList - removes a list of RafLoginHistory
	 * 
	 * @param rafLoginHistoryList
	 */
	public void removeRafLoginHistoryList(List<RafLoginHistory> rafLoginHistoryList);

	/**
	 * findByRafLoginHistoryLike - finds a list of RafLoginHistory Like
	 * 
	 * @param rafLoginHistory
	 * @return the list of RafLoginHistory found
	 */
	public List<RafLoginHistory> findByRafLoginHistoryLike(RafLoginHistory rafLoginHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafLoginHistory>LikeFR - finds a list of RafLoginHistory> Like with a finder return object
	 * 
	 * @param rafLoginHistory
	 * @return the list of RafLoginHistory found
	 */
	public FinderReturn findByRafLoginHistoryLikeFR(RafLoginHistory rafLoginHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
