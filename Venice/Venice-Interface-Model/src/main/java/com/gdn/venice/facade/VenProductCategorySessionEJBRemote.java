package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenProductCategory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenProductCategorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenProductCategory
	 */
	public List<VenProductCategory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenProductCategory persists a country
	 * 
	 * @param venProductCategory
	 * @return the persisted VenProductCategory
	 */
	public VenProductCategory persistVenProductCategory(VenProductCategory venProductCategory);

	/**
	 * persistVenProductCategoryList - persists a list of VenProductCategory
	 * 
	 * @param venProductCategoryList
	 * @return the list of persisted VenProductCategory
	 */
	public ArrayList<VenProductCategory> persistVenProductCategoryList(
			List<VenProductCategory> venProductCategoryList);

	/**
	 * mergeVenProductCategory - merges a VenProductCategory
	 * 
	 * @param venProductCategory
	 * @return the merged VenProductCategory
	 */
	public VenProductCategory mergeVenProductCategory(VenProductCategory venProductCategory);

	/**
	 * mergeVenProductCategoryList - merges a list of VenProductCategory
	 * 
	 * @param venProductCategoryList
	 * @return the merged list of VenProductCategory
	 */
	public ArrayList<VenProductCategory> mergeVenProductCategoryList(
			List<VenProductCategory> venProductCategoryList);

	/**
	 * removeVenProductCategory - removes a VenProductCategory
	 * 
	 * @param venProductCategory
	 */
	public void removeVenProductCategory(VenProductCategory venProductCategory);

	/**
	 * removeVenProductCategoryList - removes a list of VenProductCategory
	 * 
	 * @param venProductCategoryList
	 */
	public void removeVenProductCategoryList(List<VenProductCategory> venProductCategoryList);

	/**
	 * findByVenProductCategoryLike - finds a list of VenProductCategory Like
	 * 
	 * @param venProductCategory
	 * @return the list of VenProductCategory found
	 */
	public List<VenProductCategory> findByVenProductCategoryLike(VenProductCategory venProductCategory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenProductCategory>LikeFR - finds a list of VenProductCategory> Like with a finder return object
	 * 
	 * @param venProductCategory
	 * @return the list of VenProductCategory found
	 */
	public FinderReturn findByVenProductCategoryLikeFR(VenProductCategory venProductCategory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
