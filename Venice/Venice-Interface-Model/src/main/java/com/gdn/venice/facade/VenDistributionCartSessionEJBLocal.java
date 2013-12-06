package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenDistributionCart;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenDistributionCartSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDistributionCartSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenDistributionCart> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDistributionCartSessionEJBRemote#persistVenDistributionCart(com
	 * .gdn.venice.persistence.VenDistributionCart)
	 */
	public VenDistributionCart persistVenDistributionCart(VenDistributionCart venDistributionCart);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDistributionCartSessionEJBRemote#persistVenDistributionCartList
	 * (java.util.List)
	 */
	public ArrayList<VenDistributionCart> persistVenDistributionCartList(
			List<VenDistributionCart> venDistributionCartList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDistributionCartSessionEJBRemote#mergeVenDistributionCart(com.
	 * gdn.venice.persistence.VenDistributionCart)
	 */
	public VenDistributionCart mergeVenDistributionCart(VenDistributionCart venDistributionCart);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDistributionCartSessionEJBRemote#mergeVenDistributionCartList(
	 * java.util.List)
	 */
	public ArrayList<VenDistributionCart> mergeVenDistributionCartList(
			List<VenDistributionCart> venDistributionCartList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDistributionCartSessionEJBRemote#removeVenDistributionCart(com
	 * .gdn.venice.persistence.VenDistributionCart)
	 */
	public void removeVenDistributionCart(VenDistributionCart venDistributionCart);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDistributionCartSessionEJBRemote#removeVenDistributionCartList
	 * (java.util.List)
	 */
	public void removeVenDistributionCartList(List<VenDistributionCart> venDistributionCartList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDistributionCartSessionEJBRemote#findByVenDistributionCartLike
	 * (com.gdn.venice.persistence.VenDistributionCart, int, int)
	 */
	public List<VenDistributionCart> findByVenDistributionCartLike(VenDistributionCart venDistributionCart,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenDistributionCartSessionEJBRemote#findByVenDistributionCartLikeFR
	 * (com.gdn.venice.persistence.VenDistributionCart, int, int)
	 */
	public FinderReturn findByVenDistributionCartLikeFR(VenDistributionCart venDistributionCart,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
