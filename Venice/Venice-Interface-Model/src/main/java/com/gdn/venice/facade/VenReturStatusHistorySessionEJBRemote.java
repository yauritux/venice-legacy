package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenReturStatusHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenReturStatusHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenReturStatusHistory
	 */
	public List<VenReturStatusHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenReturStatusHistory persists a country
	 * 
	 * @param venReturStatusHistory
	 * @return the persisted VenReturStatusHistory
	 */
	public VenReturStatusHistory persistVenReturStatusHistory(VenReturStatusHistory venReturStatusHistory);

	/**
	 * persistVenReturStatusHistoryList - persists a list of VenReturStatusHistory
	 * 
	 * @param venReturStatusHistoryList
	 * @return the list of persisted VenReturStatusHistory
	 */
	public ArrayList<VenReturStatusHistory> persistVenReturStatusHistoryList(
			List<VenReturStatusHistory> venReturStatusHistoryList);

	/**
	 * mergeVenReturStatusHistory - merges a VenReturStatusHistory
	 * 
	 * @param venReturStatusHistory
	 * @return the merged VenReturStatusHistory
	 */
	public VenReturStatusHistory mergeVenReturStatusHistory(VenReturStatusHistory venReturStatusHistory);

	/**
	 * mergeVenReturStatusHistoryList - merges a list of VenReturStatusHistory
	 * 
	 * @param venReturStatusHistoryList
	 * @return the merged list of VenReturStatusHistory
	 */
	public ArrayList<VenReturStatusHistory> mergeVenReturStatusHistoryList(
			List<VenReturStatusHistory> venReturStatusHistoryList);

	/**
	 * removeVenReturStatusHistory - removes a VenReturStatusHistory
	 * 
	 * @param venReturStatusHistory
	 */
	public void removeVenReturStatusHistory(VenReturStatusHistory venReturStatusHistory);

	/**
	 * removeVenReturStatusHistoryList - removes a list of VenReturStatusHistory
	 * 
	 * @param venReturStatusHistoryList
	 */
	public void removeVenReturStatusHistoryList(List<VenReturStatusHistory> venReturStatusHistoryList);

	/**
	 * findByVenReturStatusHistoryLike - finds a list of VenReturStatusHistory Like
	 * 
	 * @param venReturStatusHistory
	 * @return the list of VenReturStatusHistory found
	 */
	public List<VenReturStatusHistory> findByVenReturStatusHistoryLike(VenReturStatusHistory venReturStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenReturStatusHistory>LikeFR - finds a list of VenReturStatusHistory> Like with a finder return object
	 * 
	 * @param venReturStatusHistory
	 * @return the list of VenReturStatusHistory found
	 */
	public FinderReturn findByVenReturStatusHistoryLikeFR(VenReturStatusHistory venReturStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
