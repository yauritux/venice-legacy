package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenPartyPromotionShare;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenPartyPromotionShareSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenPartyPromotionShare
	 */
	public List<VenPartyPromotionShare> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenPartyPromotionShare persists a country
	 * 
	 * @param venPartyPromotionShare
	 * @return the persisted VenPartyPromotionShare
	 */
	public VenPartyPromotionShare persistVenPartyPromotionShare(VenPartyPromotionShare venPartyPromotionShare);

	/**
	 * persistVenPartyPromotionShareList - persists a list of VenPartyPromotionShare
	 * 
	 * @param venPartyPromotionShareList
	 * @return the list of persisted VenPartyPromotionShare
	 */
	public ArrayList<VenPartyPromotionShare> persistVenPartyPromotionShareList(
			List<VenPartyPromotionShare> venPartyPromotionShareList);

	/**
	 * mergeVenPartyPromotionShare - merges a VenPartyPromotionShare
	 * 
	 * @param venPartyPromotionShare
	 * @return the merged VenPartyPromotionShare
	 */
	public VenPartyPromotionShare mergeVenPartyPromotionShare(VenPartyPromotionShare venPartyPromotionShare);

	/**
	 * mergeVenPartyPromotionShareList - merges a list of VenPartyPromotionShare
	 * 
	 * @param venPartyPromotionShareList
	 * @return the merged list of VenPartyPromotionShare
	 */
	public ArrayList<VenPartyPromotionShare> mergeVenPartyPromotionShareList(
			List<VenPartyPromotionShare> venPartyPromotionShareList);

	/**
	 * removeVenPartyPromotionShare - removes a VenPartyPromotionShare
	 * 
	 * @param venPartyPromotionShare
	 */
	public void removeVenPartyPromotionShare(VenPartyPromotionShare venPartyPromotionShare);

	/**
	 * removeVenPartyPromotionShareList - removes a list of VenPartyPromotionShare
	 * 
	 * @param venPartyPromotionShareList
	 */
	public void removeVenPartyPromotionShareList(List<VenPartyPromotionShare> venPartyPromotionShareList);

	/**
	 * findByVenPartyPromotionShareLike - finds a list of VenPartyPromotionShare Like
	 * 
	 * @param venPartyPromotionShare
	 * @return the list of VenPartyPromotionShare found
	 */
	public List<VenPartyPromotionShare> findByVenPartyPromotionShareLike(VenPartyPromotionShare venPartyPromotionShare,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenPartyPromotionShare>LikeFR - finds a list of VenPartyPromotionShare> Like with a finder return object
	 * 
	 * @param venPartyPromotionShare
	 * @return the list of VenPartyPromotionShare found
	 */
	public FinderReturn findByVenPartyPromotionShareLikeFR(VenPartyPromotionShare venPartyPromotionShare,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
