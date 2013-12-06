package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderItemHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderItemHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderItemHistory
	 */
	public List<VenOrderItemHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderItemHistory persists a country
	 * 
	 * @param venOrderItemHistory
	 * @return the persisted VenOrderItemHistory
	 */
	public VenOrderItemHistory persistVenOrderItemHistory(VenOrderItemHistory venOrderItemHistory);

	/**
	 * persistVenOrderItemHistoryList - persists a list of VenOrderItemHistory
	 * 
	 * @param venOrderItemHistoryList
	 * @return the list of persisted VenOrderItemHistory
	 */
	public ArrayList<VenOrderItemHistory> persistVenOrderItemHistoryList(
			List<VenOrderItemHistory> venOrderItemHistoryList);

	/**
	 * mergeVenOrderItemHistory - merges a VenOrderItemHistory
	 * 
	 * @param venOrderItemHistory
	 * @return the merged VenOrderItemHistory
	 */
	public VenOrderItemHistory mergeVenOrderItemHistory(VenOrderItemHistory venOrderItemHistory);

	/**
	 * mergeVenOrderItemHistoryList - merges a list of VenOrderItemHistory
	 * 
	 * @param venOrderItemHistoryList
	 * @return the merged list of VenOrderItemHistory
	 */
	public ArrayList<VenOrderItemHistory> mergeVenOrderItemHistoryList(
			List<VenOrderItemHistory> venOrderItemHistoryList);

	/**
	 * removeVenOrderItemHistory - removes a VenOrderItemHistory
	 * 
	 * @param venOrderItemHistory
	 */
	public void removeVenOrderItemHistory(VenOrderItemHistory venOrderItemHistory);

	/**
	 * removeVenOrderItemHistoryList - removes a list of VenOrderItemHistory
	 * 
	 * @param venOrderItemHistoryList
	 */
	public void removeVenOrderItemHistoryList(List<VenOrderItemHistory> venOrderItemHistoryList);

	/**
	 * findByVenOrderItemHistoryLike - finds a list of VenOrderItemHistory Like
	 * 
	 * @param venOrderItemHistory
	 * @return the list of VenOrderItemHistory found
	 */
	public List<VenOrderItemHistory> findByVenOrderItemHistoryLike(VenOrderItemHistory venOrderItemHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderItemHistory>LikeFR - finds a list of VenOrderItemHistory> Like with a finder return object
	 * 
	 * @param venOrderItemHistory
	 * @return the list of VenOrderItemHistory found
	 */
	public FinderReturn findByVenOrderItemHistoryLikeFR(VenOrderItemHistory venOrderItemHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
