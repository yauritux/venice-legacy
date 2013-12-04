package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderItemAdjustmentSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderItemAdjustment
	 */
	public List<VenOrderItemAdjustment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderItemAdjustment persists a country
	 * 
	 * @param venOrderItemAdjustment
	 * @return the persisted VenOrderItemAdjustment
	 */
	public VenOrderItemAdjustment persistVenOrderItemAdjustment(VenOrderItemAdjustment venOrderItemAdjustment);

	/**
	 * persistVenOrderItemAdjustmentList - persists a list of VenOrderItemAdjustment
	 * 
	 * @param venOrderItemAdjustmentList
	 * @return the list of persisted VenOrderItemAdjustment
	 */
	public ArrayList<VenOrderItemAdjustment> persistVenOrderItemAdjustmentList(
			List<VenOrderItemAdjustment> venOrderItemAdjustmentList);

	/**
	 * mergeVenOrderItemAdjustment - merges a VenOrderItemAdjustment
	 * 
	 * @param venOrderItemAdjustment
	 * @return the merged VenOrderItemAdjustment
	 */
	public VenOrderItemAdjustment mergeVenOrderItemAdjustment(VenOrderItemAdjustment venOrderItemAdjustment);

	/**
	 * mergeVenOrderItemAdjustmentList - merges a list of VenOrderItemAdjustment
	 * 
	 * @param venOrderItemAdjustmentList
	 * @return the merged list of VenOrderItemAdjustment
	 */
	public ArrayList<VenOrderItemAdjustment> mergeVenOrderItemAdjustmentList(
			List<VenOrderItemAdjustment> venOrderItemAdjustmentList);

	/**
	 * removeVenOrderItemAdjustment - removes a VenOrderItemAdjustment
	 * 
	 * @param venOrderItemAdjustment
	 */
	public void removeVenOrderItemAdjustment(VenOrderItemAdjustment venOrderItemAdjustment);

	/**
	 * removeVenOrderItemAdjustmentList - removes a list of VenOrderItemAdjustment
	 * 
	 * @param venOrderItemAdjustmentList
	 */
	public void removeVenOrderItemAdjustmentList(List<VenOrderItemAdjustment> venOrderItemAdjustmentList);

	/**
	 * findByVenOrderItemAdjustmentLike - finds a list of VenOrderItemAdjustment Like
	 * 
	 * @param venOrderItemAdjustment
	 * @return the list of VenOrderItemAdjustment found
	 */
	public List<VenOrderItemAdjustment> findByVenOrderItemAdjustmentLike(VenOrderItemAdjustment venOrderItemAdjustment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderItemAdjustment>LikeFR - finds a list of VenOrderItemAdjustment> Like with a finder return object
	 * 
	 * @param venOrderItemAdjustment
	 * @return the list of VenOrderItemAdjustment found
	 */
	public FinderReturn findByVenOrderItemAdjustmentLikeFR(VenOrderItemAdjustment venOrderItemAdjustment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
