package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenDistributionCart;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenDistributionCartSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenDistributionCart
	 */
	public List<VenDistributionCart> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenDistributionCart persists a country
	 * 
	 * @param venDistributionCart
	 * @return the persisted VenDistributionCart
	 */
	public VenDistributionCart persistVenDistributionCart(VenDistributionCart venDistributionCart);

	/**
	 * persistVenDistributionCartList - persists a list of VenDistributionCart
	 * 
	 * @param venDistributionCartList
	 * @return the list of persisted VenDistributionCart
	 */
	public ArrayList<VenDistributionCart> persistVenDistributionCartList(
			List<VenDistributionCart> venDistributionCartList);

	/**
	 * mergeVenDistributionCart - merges a VenDistributionCart
	 * 
	 * @param venDistributionCart
	 * @return the merged VenDistributionCart
	 */
	public VenDistributionCart mergeVenDistributionCart(VenDistributionCart venDistributionCart);

	/**
	 * mergeVenDistributionCartList - merges a list of VenDistributionCart
	 * 
	 * @param venDistributionCartList
	 * @return the merged list of VenDistributionCart
	 */
	public ArrayList<VenDistributionCart> mergeVenDistributionCartList(
			List<VenDistributionCart> venDistributionCartList);

	/**
	 * removeVenDistributionCart - removes a VenDistributionCart
	 * 
	 * @param venDistributionCart
	 */
	public void removeVenDistributionCart(VenDistributionCart venDistributionCart);

	/**
	 * removeVenDistributionCartList - removes a list of VenDistributionCart
	 * 
	 * @param venDistributionCartList
	 */
	public void removeVenDistributionCartList(List<VenDistributionCart> venDistributionCartList);

	/**
	 * findByVenDistributionCartLike - finds a list of VenDistributionCart Like
	 * 
	 * @param venDistributionCart
	 * @return the list of VenDistributionCart found
	 */
	public List<VenDistributionCart> findByVenDistributionCartLike(VenDistributionCart venDistributionCart,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenDistributionCart>LikeFR - finds a list of VenDistributionCart> Like with a finder return object
	 * 
	 * @param venDistributionCart
	 * @return the list of VenDistributionCart found
	 */
	public FinderReturn findByVenDistributionCartLikeFR(VenDistributionCart venDistributionCart,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
