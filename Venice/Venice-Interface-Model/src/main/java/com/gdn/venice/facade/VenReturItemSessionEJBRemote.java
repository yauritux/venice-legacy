package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenReturItem;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenReturItemSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenReturItem
	 */
	public List<VenReturItem> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenReturItem persists a country
	 * 
	 * @param venReturItem
	 * @return the persisted VenReturItem
	 */
	public VenReturItem persistVenReturItem(VenReturItem venReturItem);

	/**
	 * persistVenReturItemList - persists a list of VenReturItem
	 * 
	 * @param venReturItemList
	 * @return the list of persisted VenReturItem
	 */
	public ArrayList<VenReturItem> persistVenReturItemList(
			List<VenReturItem> venReturItemList);

	/**
	 * mergeVenReturItem - merges a VenReturItem
	 * 
	 * @param venReturItem
	 * @return the merged VenReturItem
	 */
	public VenReturItem mergeVenReturItem(VenReturItem venReturItem);

	/**
	 * mergeVenReturItemList - merges a list of VenReturItem
	 * 
	 * @param venReturItemList
	 * @return the merged list of VenReturItem
	 */
	public ArrayList<VenReturItem> mergeVenReturItemList(
			List<VenReturItem> venReturItemList);

	/**
	 * removeVenReturItem - removes a VenReturItem
	 * 
	 * @param venReturItem
	 */
	public void removeVenReturItem(VenReturItem venReturItem);

	/**
	 * removeVenReturItemList - removes a list of VenReturItem
	 * 
	 * @param venReturItemList
	 */
	public void removeVenReturItemList(List<VenReturItem> venReturItemList);

	/**
	 * findByVenReturItemLike - finds a list of VenReturItem Like
	 * 
	 * @param venReturItem
	 * @return the list of VenReturItem found
	 */
	public List<VenReturItem> findByVenReturItemLike(VenReturItem venReturItem,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenReturItem>LikeFR - finds a list of VenReturItem> Like with a finder return object
	 * 
	 * @param venReturItem
	 * @return the list of VenReturItem found
	 */
	public FinderReturn findByVenReturItemLikeFR(VenReturItem venReturItem,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
