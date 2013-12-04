package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenPromotion;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenPromotionSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenPromotion> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionSessionEJBRemote#persistVenPromotion(com
	 * .gdn.venice.persistence.VenPromotion)
	 */
	public VenPromotion persistVenPromotion(VenPromotion venPromotion);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionSessionEJBRemote#persistVenPromotionList
	 * (java.util.List)
	 */
	public ArrayList<VenPromotion> persistVenPromotionList(
			List<VenPromotion> venPromotionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionSessionEJBRemote#mergeVenPromotion(com.
	 * gdn.venice.persistence.VenPromotion)
	 */
	public VenPromotion mergeVenPromotion(VenPromotion venPromotion);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionSessionEJBRemote#mergeVenPromotionList(
	 * java.util.List)
	 */
	public ArrayList<VenPromotion> mergeVenPromotionList(
			List<VenPromotion> venPromotionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionSessionEJBRemote#removeVenPromotion(com
	 * .gdn.venice.persistence.VenPromotion)
	 */
	public void removeVenPromotion(VenPromotion venPromotion);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionSessionEJBRemote#removeVenPromotionList
	 * (java.util.List)
	 */
	public void removeVenPromotionList(List<VenPromotion> venPromotionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionSessionEJBRemote#findByVenPromotionLike
	 * (com.gdn.venice.persistence.VenPromotion, int, int)
	 */
	public List<VenPromotion> findByVenPromotionLike(VenPromotion venPromotion,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionSessionEJBRemote#findByVenPromotionLikeFR
	 * (com.gdn.venice.persistence.VenPromotion, int, int)
	 */
	public FinderReturn findByVenPromotionLikeFR(VenPromotion venPromotion,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
