package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinAccountCategory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinAccountCategorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountCategorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinAccountCategory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountCategorySessionEJBRemote#persistFinAccountCategory(com
	 * .gdn.venice.persistence.FinAccountCategory)
	 */
	public FinAccountCategory persistFinAccountCategory(FinAccountCategory finAccountCategory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountCategorySessionEJBRemote#persistFinAccountCategoryList
	 * (java.util.List)
	 */
	public ArrayList<FinAccountCategory> persistFinAccountCategoryList(
			List<FinAccountCategory> finAccountCategoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountCategorySessionEJBRemote#mergeFinAccountCategory(com.
	 * gdn.venice.persistence.FinAccountCategory)
	 */
	public FinAccountCategory mergeFinAccountCategory(FinAccountCategory finAccountCategory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountCategorySessionEJBRemote#mergeFinAccountCategoryList(
	 * java.util.List)
	 */
	public ArrayList<FinAccountCategory> mergeFinAccountCategoryList(
			List<FinAccountCategory> finAccountCategoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountCategorySessionEJBRemote#removeFinAccountCategory(com
	 * .gdn.venice.persistence.FinAccountCategory)
	 */
	public void removeFinAccountCategory(FinAccountCategory finAccountCategory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountCategorySessionEJBRemote#removeFinAccountCategoryList
	 * (java.util.List)
	 */
	public void removeFinAccountCategoryList(List<FinAccountCategory> finAccountCategoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountCategorySessionEJBRemote#findByFinAccountCategoryLike
	 * (com.gdn.venice.persistence.FinAccountCategory, int, int)
	 */
	public List<FinAccountCategory> findByFinAccountCategoryLike(FinAccountCategory finAccountCategory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinAccountCategorySessionEJBRemote#findByFinAccountCategoryLikeFR
	 * (com.gdn.venice.persistence.FinAccountCategory, int, int)
	 */
	public FinderReturn findByFinAccountCategoryLikeFR(FinAccountCategory finAccountCategory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
