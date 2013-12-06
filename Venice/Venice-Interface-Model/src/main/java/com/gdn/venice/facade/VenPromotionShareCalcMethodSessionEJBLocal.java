package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenPromotionShareCalcMethod;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenPromotionShareCalcMethodSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenPromotionShareCalcMethod> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#persistVenPromotionShareCalcMethod(com
	 * .gdn.venice.persistence.VenPromotionShareCalcMethod)
	 */
	public VenPromotionShareCalcMethod persistVenPromotionShareCalcMethod(VenPromotionShareCalcMethod venPromotionShareCalcMethod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#persistVenPromotionShareCalcMethodList
	 * (java.util.List)
	 */
	public ArrayList<VenPromotionShareCalcMethod> persistVenPromotionShareCalcMethodList(
			List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#mergeVenPromotionShareCalcMethod(com.
	 * gdn.venice.persistence.VenPromotionShareCalcMethod)
	 */
	public VenPromotionShareCalcMethod mergeVenPromotionShareCalcMethod(VenPromotionShareCalcMethod venPromotionShareCalcMethod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#mergeVenPromotionShareCalcMethodList(
	 * java.util.List)
	 */
	public ArrayList<VenPromotionShareCalcMethod> mergeVenPromotionShareCalcMethodList(
			List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#removeVenPromotionShareCalcMethod(com
	 * .gdn.venice.persistence.VenPromotionShareCalcMethod)
	 */
	public void removeVenPromotionShareCalcMethod(VenPromotionShareCalcMethod venPromotionShareCalcMethod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#removeVenPromotionShareCalcMethodList
	 * (java.util.List)
	 */
	public void removeVenPromotionShareCalcMethodList(List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#findByVenPromotionShareCalcMethodLike
	 * (com.gdn.venice.persistence.VenPromotionShareCalcMethod, int, int)
	 */
	public List<VenPromotionShareCalcMethod> findByVenPromotionShareCalcMethodLike(VenPromotionShareCalcMethod venPromotionShareCalcMethod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPromotionShareCalcMethodSessionEJBRemote#findByVenPromotionShareCalcMethodLikeFR
	 * (com.gdn.venice.persistence.VenPromotionShareCalcMethod, int, int)
	 */
	public FinderReturn findByVenPromotionShareCalcMethodLikeFR(VenPromotionShareCalcMethod venPromotionShareCalcMethod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
