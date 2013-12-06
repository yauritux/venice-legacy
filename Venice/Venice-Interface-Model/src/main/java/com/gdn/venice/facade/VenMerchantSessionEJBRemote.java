package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenMerchant;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenMerchantSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenMerchant
	 */
	public List<VenMerchant> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenMerchant persists a country
	 * 
	 * @param venMerchant
	 * @return the persisted VenMerchant
	 */
	public VenMerchant persistVenMerchant(VenMerchant venMerchant);

	/**
	 * persistVenMerchantList - persists a list of VenMerchant
	 * 
	 * @param venMerchantList
	 * @return the list of persisted VenMerchant
	 */
	public ArrayList<VenMerchant> persistVenMerchantList(
			List<VenMerchant> venMerchantList);

	/**
	 * mergeVenMerchant - merges a VenMerchant
	 * 
	 * @param venMerchant
	 * @return the merged VenMerchant
	 */
	public VenMerchant mergeVenMerchant(VenMerchant venMerchant);

	/**
	 * mergeVenMerchantList - merges a list of VenMerchant
	 * 
	 * @param venMerchantList
	 * @return the merged list of VenMerchant
	 */
	public ArrayList<VenMerchant> mergeVenMerchantList(
			List<VenMerchant> venMerchantList);

	/**
	 * removeVenMerchant - removes a VenMerchant
	 * 
	 * @param venMerchant
	 */
	public void removeVenMerchant(VenMerchant venMerchant);

	/**
	 * removeVenMerchantList - removes a list of VenMerchant
	 * 
	 * @param venMerchantList
	 */
	public void removeVenMerchantList(List<VenMerchant> venMerchantList);

	/**
	 * findByVenMerchantLike - finds a list of VenMerchant Like
	 * 
	 * @param venMerchant
	 * @return the list of VenMerchant found
	 */
	public List<VenMerchant> findByVenMerchantLike(VenMerchant venMerchant,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenMerchant>LikeFR - finds a list of VenMerchant> Like with a finder return object
	 * 
	 * @param venMerchant
	 * @return the list of VenMerchant found
	 */
	public FinderReturn findByVenMerchantLikeFR(VenMerchant venMerchant,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
