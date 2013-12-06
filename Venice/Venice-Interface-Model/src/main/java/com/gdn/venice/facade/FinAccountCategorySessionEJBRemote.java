package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinAccountCategory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinAccountCategorySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinAccountCategory
	 */
	public List<FinAccountCategory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinAccountCategory persists a country
	 * 
	 * @param finAccountCategory
	 * @return the persisted FinAccountCategory
	 */
	public FinAccountCategory persistFinAccountCategory(FinAccountCategory finAccountCategory);

	/**
	 * persistFinAccountCategoryList - persists a list of FinAccountCategory
	 * 
	 * @param finAccountCategoryList
	 * @return the list of persisted FinAccountCategory
	 */
	public ArrayList<FinAccountCategory> persistFinAccountCategoryList(
			List<FinAccountCategory> finAccountCategoryList);

	/**
	 * mergeFinAccountCategory - merges a FinAccountCategory
	 * 
	 * @param finAccountCategory
	 * @return the merged FinAccountCategory
	 */
	public FinAccountCategory mergeFinAccountCategory(FinAccountCategory finAccountCategory);

	/**
	 * mergeFinAccountCategoryList - merges a list of FinAccountCategory
	 * 
	 * @param finAccountCategoryList
	 * @return the merged list of FinAccountCategory
	 */
	public ArrayList<FinAccountCategory> mergeFinAccountCategoryList(
			List<FinAccountCategory> finAccountCategoryList);

	/**
	 * removeFinAccountCategory - removes a FinAccountCategory
	 * 
	 * @param finAccountCategory
	 */
	public void removeFinAccountCategory(FinAccountCategory finAccountCategory);

	/**
	 * removeFinAccountCategoryList - removes a list of FinAccountCategory
	 * 
	 * @param finAccountCategoryList
	 */
	public void removeFinAccountCategoryList(List<FinAccountCategory> finAccountCategoryList);

	/**
	 * findByFinAccountCategoryLike - finds a list of FinAccountCategory Like
	 * 
	 * @param finAccountCategory
	 * @return the list of FinAccountCategory found
	 */
	public List<FinAccountCategory> findByFinAccountCategoryLike(FinAccountCategory finAccountCategory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinAccountCategory>LikeFR - finds a list of FinAccountCategory> Like with a finder return object
	 * 
	 * @param finAccountCategory
	 * @return the list of FinAccountCategory found
	 */
	public FinderReturn findByFinAccountCategoryLikeFR(FinAccountCategory finAccountCategory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
