package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenPartyPromotionShare;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenPartyPromotionShareSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenPartyPromotionShare> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote#persistVenPartyPromotionShare(com
	 * .gdn.venice.persistence.VenPartyPromotionShare)
	 */
	public VenPartyPromotionShare persistVenPartyPromotionShare(VenPartyPromotionShare venPartyPromotionShare);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote#persistVenPartyPromotionShareList
	 * (java.util.List)
	 */
	public ArrayList<VenPartyPromotionShare> persistVenPartyPromotionShareList(
			List<VenPartyPromotionShare> venPartyPromotionShareList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote#mergeVenPartyPromotionShare(com.
	 * gdn.venice.persistence.VenPartyPromotionShare)
	 */
	public VenPartyPromotionShare mergeVenPartyPromotionShare(VenPartyPromotionShare venPartyPromotionShare);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote#mergeVenPartyPromotionShareList(
	 * java.util.List)
	 */
	public ArrayList<VenPartyPromotionShare> mergeVenPartyPromotionShareList(
			List<VenPartyPromotionShare> venPartyPromotionShareList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote#removeVenPartyPromotionShare(com
	 * .gdn.venice.persistence.VenPartyPromotionShare)
	 */
	public void removeVenPartyPromotionShare(VenPartyPromotionShare venPartyPromotionShare);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote#removeVenPartyPromotionShareList
	 * (java.util.List)
	 */
	public void removeVenPartyPromotionShareList(List<VenPartyPromotionShare> venPartyPromotionShareList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote#findByVenPartyPromotionShareLike
	 * (com.gdn.venice.persistence.VenPartyPromotionShare, int, int)
	 */
	public List<VenPartyPromotionShare> findByVenPartyPromotionShareLike(VenPartyPromotionShare venPartyPromotionShare,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyPromotionShareSessionEJBRemote#findByVenPartyPromotionShareLikeFR
	 * (com.gdn.venice.persistence.VenPartyPromotionShare, int, int)
	 */
	public FinderReturn findByVenPartyPromotionShareLikeFR(VenPartyPromotionShare venPartyPromotionShare,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
