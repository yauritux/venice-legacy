package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderStatusSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderStatus
	 */
	public List<VenOrderStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderStatus persists a country
	 * 
	 * @param venOrderStatus
	 * @return the persisted VenOrderStatus
	 */
	public VenOrderStatus persistVenOrderStatus(VenOrderStatus venOrderStatus);

	/**
	 * persistVenOrderStatusList - persists a list of VenOrderStatus
	 * 
	 * @param venOrderStatusList
	 * @return the list of persisted VenOrderStatus
	 */
	public ArrayList<VenOrderStatus> persistVenOrderStatusList(
			List<VenOrderStatus> venOrderStatusList);

	/**
	 * mergeVenOrderStatus - merges a VenOrderStatus
	 * 
	 * @param venOrderStatus
	 * @return the merged VenOrderStatus
	 */
	public VenOrderStatus mergeVenOrderStatus(VenOrderStatus venOrderStatus);

	/**
	 * mergeVenOrderStatusList - merges a list of VenOrderStatus
	 * 
	 * @param venOrderStatusList
	 * @return the merged list of VenOrderStatus
	 */
	public ArrayList<VenOrderStatus> mergeVenOrderStatusList(
			List<VenOrderStatus> venOrderStatusList);

	/**
	 * removeVenOrderStatus - removes a VenOrderStatus
	 * 
	 * @param venOrderStatus
	 */
	public void removeVenOrderStatus(VenOrderStatus venOrderStatus);

	/**
	 * removeVenOrderStatusList - removes a list of VenOrderStatus
	 * 
	 * @param venOrderStatusList
	 */
	public void removeVenOrderStatusList(List<VenOrderStatus> venOrderStatusList);

	/**
	 * findByVenOrderStatusLike - finds a list of VenOrderStatus Like
	 * 
	 * @param venOrderStatus
	 * @return the list of VenOrderStatus found
	 */
	public List<VenOrderStatus> findByVenOrderStatusLike(VenOrderStatus venOrderStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderStatus>LikeFR - finds a list of VenOrderStatus> Like with a finder return object
	 * 
	 * @param venOrderStatus
	 * @return the list of VenOrderStatus found
	 */
	public FinderReturn findByVenOrderStatusLikeFR(VenOrderStatus venOrderStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
