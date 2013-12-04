package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderPaymentAllocationSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderPaymentAllocation
	 */
	public List<VenOrderPaymentAllocation> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderPaymentAllocation persists a country
	 * 
	 * @param venOrderPaymentAllocation
	 * @return the persisted VenOrderPaymentAllocation
	 */
	public VenOrderPaymentAllocation persistVenOrderPaymentAllocation(VenOrderPaymentAllocation venOrderPaymentAllocation);

	/**
	 * persistVenOrderPaymentAllocationList - persists a list of VenOrderPaymentAllocation
	 * 
	 * @param venOrderPaymentAllocationList
	 * @return the list of persisted VenOrderPaymentAllocation
	 */
	public ArrayList<VenOrderPaymentAllocation> persistVenOrderPaymentAllocationList(
			List<VenOrderPaymentAllocation> venOrderPaymentAllocationList);

	/**
	 * mergeVenOrderPaymentAllocation - merges a VenOrderPaymentAllocation
	 * 
	 * @param venOrderPaymentAllocation
	 * @return the merged VenOrderPaymentAllocation
	 */
	public VenOrderPaymentAllocation mergeVenOrderPaymentAllocation(VenOrderPaymentAllocation venOrderPaymentAllocation);

	/**
	 * mergeVenOrderPaymentAllocationList - merges a list of VenOrderPaymentAllocation
	 * 
	 * @param venOrderPaymentAllocationList
	 * @return the merged list of VenOrderPaymentAllocation
	 */
	public ArrayList<VenOrderPaymentAllocation> mergeVenOrderPaymentAllocationList(
			List<VenOrderPaymentAllocation> venOrderPaymentAllocationList);

	/**
	 * removeVenOrderPaymentAllocation - removes a VenOrderPaymentAllocation
	 * 
	 * @param venOrderPaymentAllocation
	 */
	public void removeVenOrderPaymentAllocation(VenOrderPaymentAllocation venOrderPaymentAllocation);

	/**
	 * removeVenOrderPaymentAllocationList - removes a list of VenOrderPaymentAllocation
	 * 
	 * @param venOrderPaymentAllocationList
	 */
	public void removeVenOrderPaymentAllocationList(List<VenOrderPaymentAllocation> venOrderPaymentAllocationList);

	/**
	 * findByVenOrderPaymentAllocationLike - finds a list of VenOrderPaymentAllocation Like
	 * 
	 * @param venOrderPaymentAllocation
	 * @return the list of VenOrderPaymentAllocation found
	 */
	public List<VenOrderPaymentAllocation> findByVenOrderPaymentAllocationLike(VenOrderPaymentAllocation venOrderPaymentAllocation,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderPaymentAllocation>LikeFR - finds a list of VenOrderPaymentAllocation> Like with a finder return object
	 * 
	 * @param venOrderPaymentAllocation
	 * @return the list of VenOrderPaymentAllocation found
	 */
	public FinderReturn findByVenOrderPaymentAllocationLikeFR(VenOrderPaymentAllocation venOrderPaymentAllocation,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
