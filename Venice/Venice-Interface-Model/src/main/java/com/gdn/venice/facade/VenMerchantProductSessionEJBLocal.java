package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenMerchantProduct;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenMerchantProductSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantProductSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenMerchantProduct> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantProductSessionEJBRemote#persistVenMerchantProduct(com
	 * .gdn.venice.persistence.VenMerchantProduct)
	 */
	public VenMerchantProduct persistVenMerchantProduct(VenMerchantProduct venMerchantProduct);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantProductSessionEJBRemote#persistVenMerchantProductList
	 * (java.util.List)
	 */
	public ArrayList<VenMerchantProduct> persistVenMerchantProductList(
			List<VenMerchantProduct> venMerchantProductList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantProductSessionEJBRemote#mergeVenMerchantProduct(com.
	 * gdn.venice.persistence.VenMerchantProduct)
	 */
	public VenMerchantProduct mergeVenMerchantProduct(VenMerchantProduct venMerchantProduct);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantProductSessionEJBRemote#mergeVenMerchantProductList(
	 * java.util.List)
	 */
	public ArrayList<VenMerchantProduct> mergeVenMerchantProductList(
			List<VenMerchantProduct> venMerchantProductList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantProductSessionEJBRemote#removeVenMerchantProduct(com
	 * .gdn.venice.persistence.VenMerchantProduct)
	 */
	public void removeVenMerchantProduct(VenMerchantProduct venMerchantProduct);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantProductSessionEJBRemote#removeVenMerchantProductList
	 * (java.util.List)
	 */
	public void removeVenMerchantProductList(List<VenMerchantProduct> venMerchantProductList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantProductSessionEJBRemote#findByVenMerchantProductLike
	 * (com.gdn.venice.persistence.VenMerchantProduct, int, int)
	 */
	public List<VenMerchantProduct> findByVenMerchantProductLike(VenMerchantProduct venMerchantProduct,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantProductSessionEJBRemote#findByVenMerchantProductLikeFR
	 * (com.gdn.venice.persistence.VenMerchantProduct, int, int)
	 */
	public FinderReturn findByVenMerchantProductLikeFR(VenMerchantProduct venMerchantProduct,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
