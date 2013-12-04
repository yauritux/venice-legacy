package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderItemStatusHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderItemStatusHistory
	 */
	public List<VenOrderItemStatusHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderItemStatusHistory persists a country
	 * 
	 * @param venOrderItemStatusHistory
	 * @return the persisted VenOrderItemStatusHistory
	 */
	public VenOrderItemStatusHistory persistVenOrderItemStatusHistory(VenOrderItemStatusHistory venOrderItemStatusHistory);

	/**
	 * persistVenOrderItemStatusHistoryList - persists a list of VenOrderItemStatusHistory
	 * 
	 * @param venOrderItemStatusHistoryList
	 * @return the list of persisted VenOrderItemStatusHistory
	 */
	public ArrayList<VenOrderItemStatusHistory> persistVenOrderItemStatusHistoryList(
			List<VenOrderItemStatusHistory> venOrderItemStatusHistoryList);

	/**
	 * mergeVenOrderItemStatusHistory - merges a VenOrderItemStatusHistory
	 * 
	 * @param venOrderItemStatusHistory
	 * @return the merged VenOrderItemStatusHistory
	 */
	public VenOrderItemStatusHistory mergeVenOrderItemStatusHistory(VenOrderItemStatusHistory venOrderItemStatusHistory);

	/**
	 * mergeVenOrderItemStatusHistoryList - merges a list of VenOrderItemStatusHistory
	 * 
	 * @param venOrderItemStatusHistoryList
	 * @return the merged list of VenOrderItemStatusHistory
	 */
	public ArrayList<VenOrderItemStatusHistory> mergeVenOrderItemStatusHistoryList(
			List<VenOrderItemStatusHistory> venOrderItemStatusHistoryList);

	/**
	 * removeVenOrderItemStatusHistory - removes a VenOrderItemStatusHistory
	 * 
	 * @param venOrderItemStatusHistory
	 */
	public void removeVenOrderItemStatusHistory(VenOrderItemStatusHistory venOrderItemStatusHistory);

	/**
	 * removeVenOrderItemStatusHistoryList - removes a list of VenOrderItemStatusHistory
	 * 
	 * @param venOrderItemStatusHistoryList
	 */
	public void removeVenOrderItemStatusHistoryList(List<VenOrderItemStatusHistory> venOrderItemStatusHistoryList);

	/**
	 * findByVenOrderItemStatusHistoryLike - finds a list of VenOrderItemStatusHistory Like
	 * 
	 * @param venOrderItemStatusHistory
	 * @return the list of VenOrderItemStatusHistory found
	 */
	public List<VenOrderItemStatusHistory> findByVenOrderItemStatusHistoryLike(VenOrderItemStatusHistory venOrderItemStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderItemStatusHistory>LikeFR - finds a list of VenOrderItemStatusHistory> Like with a finder return object
	 * 
	 * @param venOrderItemStatusHistory
	 * @return the list of VenOrderItemStatusHistory found
	 */
	public FinderReturn findByVenOrderItemStatusHistoryLikeFR(VenOrderItemStatusHistory venOrderItemStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
