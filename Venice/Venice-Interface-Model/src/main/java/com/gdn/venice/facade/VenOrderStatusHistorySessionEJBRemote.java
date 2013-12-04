package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderStatusHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderStatusHistory
	 */
	public List<VenOrderStatusHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderStatusHistory persists a country
	 * 
	 * @param venOrderStatusHistory
	 * @return the persisted VenOrderStatusHistory
	 */
	public VenOrderStatusHistory persistVenOrderStatusHistory(VenOrderStatusHistory venOrderStatusHistory);

	/**
	 * persistVenOrderStatusHistoryList - persists a list of VenOrderStatusHistory
	 * 
	 * @param venOrderStatusHistoryList
	 * @return the list of persisted VenOrderStatusHistory
	 */
	public ArrayList<VenOrderStatusHistory> persistVenOrderStatusHistoryList(
			List<VenOrderStatusHistory> venOrderStatusHistoryList);

	/**
	 * mergeVenOrderStatusHistory - merges a VenOrderStatusHistory
	 * 
	 * @param venOrderStatusHistory
	 * @return the merged VenOrderStatusHistory
	 */
	public VenOrderStatusHistory mergeVenOrderStatusHistory(VenOrderStatusHistory venOrderStatusHistory);

	/**
	 * mergeVenOrderStatusHistoryList - merges a list of VenOrderStatusHistory
	 * 
	 * @param venOrderStatusHistoryList
	 * @return the merged list of VenOrderStatusHistory
	 */
	public ArrayList<VenOrderStatusHistory> mergeVenOrderStatusHistoryList(
			List<VenOrderStatusHistory> venOrderStatusHistoryList);

	/**
	 * removeVenOrderStatusHistory - removes a VenOrderStatusHistory
	 * 
	 * @param venOrderStatusHistory
	 */
	public void removeVenOrderStatusHistory(VenOrderStatusHistory venOrderStatusHistory);

	/**
	 * removeVenOrderStatusHistoryList - removes a list of VenOrderStatusHistory
	 * 
	 * @param venOrderStatusHistoryList
	 */
	public void removeVenOrderStatusHistoryList(List<VenOrderStatusHistory> venOrderStatusHistoryList);

	/**
	 * findByVenOrderStatusHistoryLike - finds a list of VenOrderStatusHistory Like
	 * 
	 * @param venOrderStatusHistory
	 * @return the list of VenOrderStatusHistory found
	 */
	public List<VenOrderStatusHistory> findByVenOrderStatusHistoryLike(VenOrderStatusHistory venOrderStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderStatusHistory>LikeFR - finds a list of VenOrderStatusHistory> Like with a finder return object
	 * 
	 * @param venOrderStatusHistory
	 * @return the list of VenOrderStatusHistory found
	 */
	public FinderReturn findByVenOrderStatusHistoryLikeFR(VenOrderStatusHistory venOrderStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
