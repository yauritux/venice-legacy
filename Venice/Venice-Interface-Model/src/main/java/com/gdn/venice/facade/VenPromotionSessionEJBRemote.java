package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenPromotion;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenPromotionSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenPromotion
	 */
	public List<VenPromotion> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenPromotion persists a country
	 * 
	 * @param venPromotion
	 * @return the persisted VenPromotion
	 */
	public VenPromotion persistVenPromotion(VenPromotion venPromotion);

	/**
	 * persistVenPromotionList - persists a list of VenPromotion
	 * 
	 * @param venPromotionList
	 * @return the list of persisted VenPromotion
	 */
	public ArrayList<VenPromotion> persistVenPromotionList(
			List<VenPromotion> venPromotionList);

	/**
	 * mergeVenPromotion - merges a VenPromotion
	 * 
	 * @param venPromotion
	 * @return the merged VenPromotion
	 */
	public VenPromotion mergeVenPromotion(VenPromotion venPromotion);

	/**
	 * mergeVenPromotionList - merges a list of VenPromotion
	 * 
	 * @param venPromotionList
	 * @return the merged list of VenPromotion
	 */
	public ArrayList<VenPromotion> mergeVenPromotionList(
			List<VenPromotion> venPromotionList);

	/**
	 * removeVenPromotion - removes a VenPromotion
	 * 
	 * @param venPromotion
	 */
	public void removeVenPromotion(VenPromotion venPromotion);

	/**
	 * removeVenPromotionList - removes a list of VenPromotion
	 * 
	 * @param venPromotionList
	 */
	public void removeVenPromotionList(List<VenPromotion> venPromotionList);

	/**
	 * findByVenPromotionLike - finds a list of VenPromotion Like
	 * 
	 * @param venPromotion
	 * @return the list of VenPromotion found
	 */
	public List<VenPromotion> findByVenPromotionLike(VenPromotion venPromotion,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenPromotion>LikeFR - finds a list of VenPromotion> Like with a finder return object
	 * 
	 * @param venPromotion
	 * @return the list of VenPromotion found
	 */
	public FinderReturn findByVenPromotionLikeFR(VenPromotion venPromotion,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
