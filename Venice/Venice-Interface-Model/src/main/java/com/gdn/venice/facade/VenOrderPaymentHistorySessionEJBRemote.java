package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderPaymentHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderPaymentHistorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderPaymentHistory
	 */
	public List<VenOrderPaymentHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderPaymentHistory persists a country
	 * 
	 * @param venOrderPaymentHistory
	 * @return the persisted VenOrderPaymentHistory
	 */
	public VenOrderPaymentHistory persistVenOrderPaymentHistory(VenOrderPaymentHistory venOrderPaymentHistory);

	/**
	 * persistVenOrderPaymentHistoryList - persists a list of VenOrderPaymentHistory
	 * 
	 * @param venOrderPaymentHistoryList
	 * @return the list of persisted VenOrderPaymentHistory
	 */
	public ArrayList<VenOrderPaymentHistory> persistVenOrderPaymentHistoryList(
			List<VenOrderPaymentHistory> venOrderPaymentHistoryList);

	/**
	 * mergeVenOrderPaymentHistory - merges a VenOrderPaymentHistory
	 * 
	 * @param venOrderPaymentHistory
	 * @return the merged VenOrderPaymentHistory
	 */
	public VenOrderPaymentHistory mergeVenOrderPaymentHistory(VenOrderPaymentHistory venOrderPaymentHistory);

	/**
	 * mergeVenOrderPaymentHistoryList - merges a list of VenOrderPaymentHistory
	 * 
	 * @param venOrderPaymentHistoryList
	 * @return the merged list of VenOrderPaymentHistory
	 */
	public ArrayList<VenOrderPaymentHistory> mergeVenOrderPaymentHistoryList(
			List<VenOrderPaymentHistory> venOrderPaymentHistoryList);

	/**
	 * removeVenOrderPaymentHistory - removes a VenOrderPaymentHistory
	 * 
	 * @param venOrderPaymentHistory
	 */
	public void removeVenOrderPaymentHistory(VenOrderPaymentHistory venOrderPaymentHistory);

	/**
	 * removeVenOrderPaymentHistoryList - removes a list of VenOrderPaymentHistory
	 * 
	 * @param venOrderPaymentHistoryList
	 */
	public void removeVenOrderPaymentHistoryList(List<VenOrderPaymentHistory> venOrderPaymentHistoryList);

	/**
	 * findByVenOrderPaymentHistoryLike - finds a list of VenOrderPaymentHistory Like
	 * 
	 * @param venOrderPaymentHistory
	 * @return the list of VenOrderPaymentHistory found
	 */
	public List<VenOrderPaymentHistory> findByVenOrderPaymentHistoryLike(VenOrderPaymentHistory venOrderPaymentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderPaymentHistory>LikeFR - finds a list of VenOrderPaymentHistory> Like with a finder return object
	 * 
	 * @param venOrderPaymentHistory
	 * @return the list of VenOrderPaymentHistory found
	 */
	public FinderReturn findByVenOrderPaymentHistoryLikeFR(VenOrderPaymentHistory venOrderPaymentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
