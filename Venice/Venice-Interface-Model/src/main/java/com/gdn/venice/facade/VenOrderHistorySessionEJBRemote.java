package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderHistory
	 */
	public List<VenOrderHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderHistory persists a country
	 * 
	 * @param venOrderHistory
	 * @return the persisted VenOrderHistory
	 */
	public VenOrderHistory persistVenOrderHistory(VenOrderHistory venOrderHistory);

	/**
	 * persistVenOrderHistoryList - persists a list of VenOrderHistory
	 * 
	 * @param venOrderHistoryList
	 * @return the list of persisted VenOrderHistory
	 */
	public ArrayList<VenOrderHistory> persistVenOrderHistoryList(
			List<VenOrderHistory> venOrderHistoryList);

	/**
	 * mergeVenOrderHistory - merges a VenOrderHistory
	 * 
	 * @param venOrderHistory
	 * @return the merged VenOrderHistory
	 */
	public VenOrderHistory mergeVenOrderHistory(VenOrderHistory venOrderHistory);

	/**
	 * mergeVenOrderHistoryList - merges a list of VenOrderHistory
	 * 
	 * @param venOrderHistoryList
	 * @return the merged list of VenOrderHistory
	 */
	public ArrayList<VenOrderHistory> mergeVenOrderHistoryList(
			List<VenOrderHistory> venOrderHistoryList);

	/**
	 * removeVenOrderHistory - removes a VenOrderHistory
	 * 
	 * @param venOrderHistory
	 */
	public void removeVenOrderHistory(VenOrderHistory venOrderHistory);

	/**
	 * removeVenOrderHistoryList - removes a list of VenOrderHistory
	 * 
	 * @param venOrderHistoryList
	 */
	public void removeVenOrderHistoryList(List<VenOrderHistory> venOrderHistoryList);

	/**
	 * findByVenOrderHistoryLike - finds a list of VenOrderHistory Like
	 * 
	 * @param venOrderHistory
	 * @return the list of VenOrderHistory found
	 */
	public List<VenOrderHistory> findByVenOrderHistoryLike(VenOrderHistory venOrderHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderHistory>LikeFR - finds a list of VenOrderHistory> Like with a finder return object
	 * 
	 * @param venOrderHistory
	 * @return the list of VenOrderHistory found
	 */
	public FinderReturn findByVenOrderHistoryLikeFR(VenOrderHistory venOrderHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
