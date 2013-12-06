package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenFraudCheckStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenFraudCheckStatusSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenFraudCheckStatus
	 */
	public List<VenFraudCheckStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenFraudCheckStatus persists a country
	 * 
	 * @param venFraudCheckStatus
	 * @return the persisted VenFraudCheckStatus
	 */
	public VenFraudCheckStatus persistVenFraudCheckStatus(VenFraudCheckStatus venFraudCheckStatus);

	/**
	 * persistVenFraudCheckStatusList - persists a list of VenFraudCheckStatus
	 * 
	 * @param venFraudCheckStatusList
	 * @return the list of persisted VenFraudCheckStatus
	 */
	public ArrayList<VenFraudCheckStatus> persistVenFraudCheckStatusList(
			List<VenFraudCheckStatus> venFraudCheckStatusList);

	/**
	 * mergeVenFraudCheckStatus - merges a VenFraudCheckStatus
	 * 
	 * @param venFraudCheckStatus
	 * @return the merged VenFraudCheckStatus
	 */
	public VenFraudCheckStatus mergeVenFraudCheckStatus(VenFraudCheckStatus venFraudCheckStatus);

	/**
	 * mergeVenFraudCheckStatusList - merges a list of VenFraudCheckStatus
	 * 
	 * @param venFraudCheckStatusList
	 * @return the merged list of VenFraudCheckStatus
	 */
	public ArrayList<VenFraudCheckStatus> mergeVenFraudCheckStatusList(
			List<VenFraudCheckStatus> venFraudCheckStatusList);

	/**
	 * removeVenFraudCheckStatus - removes a VenFraudCheckStatus
	 * 
	 * @param venFraudCheckStatus
	 */
	public void removeVenFraudCheckStatus(VenFraudCheckStatus venFraudCheckStatus);

	/**
	 * removeVenFraudCheckStatusList - removes a list of VenFraudCheckStatus
	 * 
	 * @param venFraudCheckStatusList
	 */
	public void removeVenFraudCheckStatusList(List<VenFraudCheckStatus> venFraudCheckStatusList);

	/**
	 * findByVenFraudCheckStatusLike - finds a list of VenFraudCheckStatus Like
	 * 
	 * @param venFraudCheckStatus
	 * @return the list of VenFraudCheckStatus found
	 */
	public List<VenFraudCheckStatus> findByVenFraudCheckStatusLike(VenFraudCheckStatus venFraudCheckStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenFraudCheckStatus>LikeFR - finds a list of VenFraudCheckStatus> Like with a finder return object
	 * 
	 * @param venFraudCheckStatus
	 * @return the list of VenFraudCheckStatus found
	 */
	public FinderReturn findByVenFraudCheckStatusLikeFR(VenFraudCheckStatus venFraudCheckStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
