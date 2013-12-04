package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenMerchantProduct;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenMerchantProductSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenMerchantProduct
	 */
	public List<VenMerchantProduct> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenMerchantProduct persists a country
	 * 
	 * @param venMerchantProduct
	 * @return the persisted VenMerchantProduct
	 */
	public VenMerchantProduct persistVenMerchantProduct(VenMerchantProduct venMerchantProduct);

	/**
	 * persistVenMerchantProductList - persists a list of VenMerchantProduct
	 * 
	 * @param venMerchantProductList
	 * @return the list of persisted VenMerchantProduct
	 */
	public ArrayList<VenMerchantProduct> persistVenMerchantProductList(
			List<VenMerchantProduct> venMerchantProductList);

	/**
	 * mergeVenMerchantProduct - merges a VenMerchantProduct
	 * 
	 * @param venMerchantProduct
	 * @return the merged VenMerchantProduct
	 */
	public VenMerchantProduct mergeVenMerchantProduct(VenMerchantProduct venMerchantProduct);

	/**
	 * mergeVenMerchantProductList - merges a list of VenMerchantProduct
	 * 
	 * @param venMerchantProductList
	 * @return the merged list of VenMerchantProduct
	 */
	public ArrayList<VenMerchantProduct> mergeVenMerchantProductList(
			List<VenMerchantProduct> venMerchantProductList);

	/**
	 * removeVenMerchantProduct - removes a VenMerchantProduct
	 * 
	 * @param venMerchantProduct
	 */
	public void removeVenMerchantProduct(VenMerchantProduct venMerchantProduct);

	/**
	 * removeVenMerchantProductList - removes a list of VenMerchantProduct
	 * 
	 * @param venMerchantProductList
	 */
	public void removeVenMerchantProductList(List<VenMerchantProduct> venMerchantProductList);

	/**
	 * findByVenMerchantProductLike - finds a list of VenMerchantProduct Like
	 * 
	 * @param venMerchantProduct
	 * @return the list of VenMerchantProduct found
	 */
	public List<VenMerchantProduct> findByVenMerchantProductLike(VenMerchantProduct venMerchantProduct,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenMerchantProduct>LikeFR - finds a list of VenMerchantProduct> Like with a finder return object
	 * 
	 * @param venMerchantProduct
	 * @return the list of VenMerchantProduct found
	 */
	public FinderReturn findByVenMerchantProductLikeFR(VenMerchantProduct venMerchantProduct,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
