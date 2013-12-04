package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenPaymentStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenPaymentStatusSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenPaymentStatus
	 */
	public List<VenPaymentStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenPaymentStatus persists a country
	 * 
	 * @param venPaymentStatus
	 * @return the persisted VenPaymentStatus
	 */
	public VenPaymentStatus persistVenPaymentStatus(VenPaymentStatus venPaymentStatus);

	/**
	 * persistVenPaymentStatusList - persists a list of VenPaymentStatus
	 * 
	 * @param venPaymentStatusList
	 * @return the list of persisted VenPaymentStatus
	 */
	public ArrayList<VenPaymentStatus> persistVenPaymentStatusList(
			List<VenPaymentStatus> venPaymentStatusList);

	/**
	 * mergeVenPaymentStatus - merges a VenPaymentStatus
	 * 
	 * @param venPaymentStatus
	 * @return the merged VenPaymentStatus
	 */
	public VenPaymentStatus mergeVenPaymentStatus(VenPaymentStatus venPaymentStatus);

	/**
	 * mergeVenPaymentStatusList - merges a list of VenPaymentStatus
	 * 
	 * @param venPaymentStatusList
	 * @return the merged list of VenPaymentStatus
	 */
	public ArrayList<VenPaymentStatus> mergeVenPaymentStatusList(
			List<VenPaymentStatus> venPaymentStatusList);

	/**
	 * removeVenPaymentStatus - removes a VenPaymentStatus
	 * 
	 * @param venPaymentStatus
	 */
	public void removeVenPaymentStatus(VenPaymentStatus venPaymentStatus);

	/**
	 * removeVenPaymentStatusList - removes a list of VenPaymentStatus
	 * 
	 * @param venPaymentStatusList
	 */
	public void removeVenPaymentStatusList(List<VenPaymentStatus> venPaymentStatusList);

	/**
	 * findByVenPaymentStatusLike - finds a list of VenPaymentStatus Like
	 * 
	 * @param venPaymentStatus
	 * @return the list of VenPaymentStatus found
	 */
	public List<VenPaymentStatus> findByVenPaymentStatusLike(VenPaymentStatus venPaymentStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenPaymentStatus>LikeFR - finds a list of VenPaymentStatus> Like with a finder return object
	 * 
	 * @param venPaymentStatus
	 * @return the list of VenPaymentStatus found
	 */
	public FinderReturn findByVenPaymentStatusLikeFR(VenPaymentStatus venPaymentStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
