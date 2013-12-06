package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderItem;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderItemSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderItem
	 */
	public List<VenOrderItem> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderItem persists a country
	 * 
	 * @param venOrderItem
	 * @return the persisted VenOrderItem
	 */
	public VenOrderItem persistVenOrderItem(VenOrderItem venOrderItem);

	/**
	 * persistVenOrderItemList - persists a list of VenOrderItem
	 * 
	 * @param venOrderItemList
	 * @return the list of persisted VenOrderItem
	 */
	public ArrayList<VenOrderItem> persistVenOrderItemList(
			List<VenOrderItem> venOrderItemList);

	/**
	 * mergeVenOrderItem - merges a VenOrderItem
	 * 
	 * @param venOrderItem
	 * @return the merged VenOrderItem
	 */
	public VenOrderItem mergeVenOrderItem(VenOrderItem venOrderItem);

	/**
	 * mergeVenOrderItemList - merges a list of VenOrderItem
	 * 
	 * @param venOrderItemList
	 * @return the merged list of VenOrderItem
	 */
	public ArrayList<VenOrderItem> mergeVenOrderItemList(
			List<VenOrderItem> venOrderItemList);

	/**
	 * removeVenOrderItem - removes a VenOrderItem
	 * 
	 * @param venOrderItem
	 */
	public void removeVenOrderItem(VenOrderItem venOrderItem);

	/**
	 * removeVenOrderItemList - removes a list of VenOrderItem
	 * 
	 * @param venOrderItemList
	 */
	public void removeVenOrderItemList(List<VenOrderItem> venOrderItemList);

	/**
	 * findByVenOrderItemLike - finds a list of VenOrderItem Like
	 * 
	 * @param venOrderItem
	 * @return the list of VenOrderItem found
	 */
	public List<VenOrderItem> findByVenOrderItemLike(VenOrderItem venOrderItem,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderItem>LikeFR - finds a list of VenOrderItem> Like with a finder return object
	 * 
	 * @param venOrderItem
	 * @return the list of VenOrderItem found
	 */
	public FinderReturn findByVenOrderItemLikeFR(VenOrderItem venOrderItem,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
        
        public Boolean republish(VenOrderItem venOrderItem);
}
