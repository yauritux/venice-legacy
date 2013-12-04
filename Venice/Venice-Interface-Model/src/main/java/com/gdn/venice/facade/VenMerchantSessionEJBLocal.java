package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenMerchant;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenMerchantSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenMerchant> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantSessionEJBRemote#persistVenMerchant(com
	 * .gdn.venice.persistence.VenMerchant)
	 */
	public VenMerchant persistVenMerchant(VenMerchant venMerchant);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantSessionEJBRemote#persistVenMerchantList
	 * (java.util.List)
	 */
	public ArrayList<VenMerchant> persistVenMerchantList(
			List<VenMerchant> venMerchantList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantSessionEJBRemote#mergeVenMerchant(com.
	 * gdn.venice.persistence.VenMerchant)
	 */
	public VenMerchant mergeVenMerchant(VenMerchant venMerchant);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantSessionEJBRemote#mergeVenMerchantList(
	 * java.util.List)
	 */
	public ArrayList<VenMerchant> mergeVenMerchantList(
			List<VenMerchant> venMerchantList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantSessionEJBRemote#removeVenMerchant(com
	 * .gdn.venice.persistence.VenMerchant)
	 */
	public void removeVenMerchant(VenMerchant venMerchant);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantSessionEJBRemote#removeVenMerchantList
	 * (java.util.List)
	 */
	public void removeVenMerchantList(List<VenMerchant> venMerchantList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantSessionEJBRemote#findByVenMerchantLike
	 * (com.gdn.venice.persistence.VenMerchant, int, int)
	 */
	public List<VenMerchant> findByVenMerchantLike(VenMerchant venMerchant,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantSessionEJBRemote#findByVenMerchantLikeFR
	 * (com.gdn.venice.persistence.VenMerchant, int, int)
	 */
	public FinderReturn findByVenMerchantLikeFR(VenMerchant venMerchant,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
