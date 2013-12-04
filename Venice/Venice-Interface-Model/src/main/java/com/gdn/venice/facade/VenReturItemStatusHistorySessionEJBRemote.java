package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenReturItemStatusHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenReturItemStatusHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenReturItemStatusHistory
	 */
	public List<VenReturItemStatusHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenReturItemStatusHistory persists a country
	 * 
	 * @param venReturItemStatusHistory
	 * @return the persisted VenReturItemStatusHistory
	 */
	public VenReturItemStatusHistory persistVenReturItemStatusHistory(VenReturItemStatusHistory venReturItemStatusHistory);

	/**
	 * persistVenReturItemStatusHistoryList - persists a list of VenReturItemStatusHistory
	 * 
	 * @param venReturItemStatusHistoryList
	 * @return the list of persisted VenReturItemStatusHistory
	 */
	public ArrayList<VenReturItemStatusHistory> persistVenReturItemStatusHistoryList(
			List<VenReturItemStatusHistory> venReturItemStatusHistoryList);

	/**
	 * mergeVenReturItemStatusHistory - merges a VenReturItemStatusHistory
	 * 
	 * @param venReturItemStatusHistory
	 * @return the merged VenReturItemStatusHistory
	 */
	public VenReturItemStatusHistory mergeVenReturItemStatusHistory(VenReturItemStatusHistory venReturItemStatusHistory);

	/**
	 * mergeVenReturItemStatusHistoryList - merges a list of VenReturItemStatusHistory
	 * 
	 * @param venReturItemStatusHistoryList
	 * @return the merged list of VenReturItemStatusHistory
	 */
	public ArrayList<VenReturItemStatusHistory> mergeVenReturItemStatusHistoryList(
			List<VenReturItemStatusHistory> venReturItemStatusHistoryList);

	/**
	 * removeVenReturItemStatusHistory - removes a VenReturItemStatusHistory
	 * 
	 * @param venReturItemStatusHistory
	 */
	public void removeVenReturItemStatusHistory(VenReturItemStatusHistory venReturItemStatusHistory);

	/**
	 * removeVenReturItemStatusHistoryList - removes a list of VenReturItemStatusHistory
	 * 
	 * @param venReturItemStatusHistoryList
	 */
	public void removeVenReturItemStatusHistoryList(List<VenReturItemStatusHistory> venReturItemStatusHistoryList);

	/**
	 * findByVenReturItemStatusHistoryLike - finds a list of VenReturItemStatusHistory Like
	 * 
	 * @param venReturItemStatusHistory
	 * @return the list of VenReturItemStatusHistory found
	 */
	public List<VenReturItemStatusHistory> findByVenReturItemStatusHistoryLike(VenReturItemStatusHistory venReturItemStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenReturItemStatusHistory>LikeFR - finds a list of VenReturItemStatusHistory> Like with a finder return object
	 * 
	 * @param venReturItemStatusHistory
	 * @return the list of VenReturItemStatusHistory found
	 */
	public FinderReturn findByVenReturItemStatusHistoryLikeFR(VenReturItemStatusHistory venReturItemStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
