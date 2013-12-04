package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrder;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrder
	 */
	public List<VenOrder> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);
	
	public String countQueryByRange(String jpqlStmt, int firstResult,
			int maxResults);
	/**
	 * persistVenOrder persists a country
	 * 
	 * @param venOrder
	 * @return the persisted VenOrder
	 */
	public VenOrder persistVenOrder(VenOrder venOrder);

	/**
	 * persistVenOrderList - persists a list of VenOrder
	 * 
	 * @param venOrderList
	 * @return the list of persisted VenOrder
	 */
	public ArrayList<VenOrder> persistVenOrderList(
			List<VenOrder> venOrderList);

	/**
	 * mergeVenOrder - merges a VenOrder
	 * 
	 * @param venOrder
	 * @return the merged VenOrder
	 */
	public VenOrder mergeVenOrder(VenOrder venOrder);

	/**
	 * mergeVenOrderList - merges a list of VenOrder
	 * 
	 * @param venOrderList
	 * @return the merged list of VenOrder
	 */
	public ArrayList<VenOrder> mergeVenOrderList(
			List<VenOrder> venOrderList);

	/**
	 * removeVenOrder - removes a VenOrder
	 * 
	 * @param venOrder
	 */
	public void removeVenOrder(VenOrder venOrder);

	/**
	 * removeVenOrderList - removes a list of VenOrder
	 * 
	 * @param venOrderList
	 */
	public void removeVenOrderList(List<VenOrder> venOrderList);

	/**
	 * findByVenOrderLike - finds a list of VenOrder Like
	 * 
	 * @param venOrder
	 * @return the list of VenOrder found
	 */
	public List<VenOrder> findByVenOrderLike(VenOrder venOrder,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	
	public String countFindByVenOrderLike(VenOrder venOrder,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	/**
	 * findByVenOrder>LikeFR - finds a list of VenOrder> Like with a finder return object
	 * 
	 * @param venOrder
	 * @return the list of VenOrder found
	 */
	public FinderReturn findByVenOrderLikeFR(VenOrder venOrder,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
