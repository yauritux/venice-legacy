package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenProductCategory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenProductCategorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductCategorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenProductCategory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductCategorySessionEJBRemote#persistVenProductCategory(com
	 * .gdn.venice.persistence.VenProductCategory)
	 */
	public VenProductCategory persistVenProductCategory(VenProductCategory venProductCategory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductCategorySessionEJBRemote#persistVenProductCategoryList
	 * (java.util.List)
	 */
	public ArrayList<VenProductCategory> persistVenProductCategoryList(
			List<VenProductCategory> venProductCategoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductCategorySessionEJBRemote#mergeVenProductCategory(com.
	 * gdn.venice.persistence.VenProductCategory)
	 */
	public VenProductCategory mergeVenProductCategory(VenProductCategory venProductCategory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductCategorySessionEJBRemote#mergeVenProductCategoryList(
	 * java.util.List)
	 */
	public ArrayList<VenProductCategory> mergeVenProductCategoryList(
			List<VenProductCategory> venProductCategoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductCategorySessionEJBRemote#removeVenProductCategory(com
	 * .gdn.venice.persistence.VenProductCategory)
	 */
	public void removeVenProductCategory(VenProductCategory venProductCategory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductCategorySessionEJBRemote#removeVenProductCategoryList
	 * (java.util.List)
	 */
	public void removeVenProductCategoryList(List<VenProductCategory> venProductCategoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductCategorySessionEJBRemote#findByVenProductCategoryLike
	 * (com.gdn.venice.persistence.VenProductCategory, int, int)
	 */
	public List<VenProductCategory> findByVenProductCategoryLike(VenProductCategory venProductCategory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductCategorySessionEJBRemote#findByVenProductCategoryLikeFR
	 * (com.gdn.venice.persistence.VenProductCategory, int, int)
	 */
	public FinderReturn findByVenProductCategoryLikeFR(VenProductCategory venProductCategory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
