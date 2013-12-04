package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinAccountType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinAccountTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinAccountType
	 */
	public List<FinAccountType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinAccountType persists a country
	 * 
	 * @param finAccountType
	 * @return the persisted FinAccountType
	 */
	public FinAccountType persistFinAccountType(FinAccountType finAccountType);

	/**
	 * persistFinAccountTypeList - persists a list of FinAccountType
	 * 
	 * @param finAccountTypeList
	 * @return the list of persisted FinAccountType
	 */
	public ArrayList<FinAccountType> persistFinAccountTypeList(
			List<FinAccountType> finAccountTypeList);

	/**
	 * mergeFinAccountType - merges a FinAccountType
	 * 
	 * @param finAccountType
	 * @return the merged FinAccountType
	 */
	public FinAccountType mergeFinAccountType(FinAccountType finAccountType);

	/**
	 * mergeFinAccountTypeList - merges a list of FinAccountType
	 * 
	 * @param finAccountTypeList
	 * @return the merged list of FinAccountType
	 */
	public ArrayList<FinAccountType> mergeFinAccountTypeList(
			List<FinAccountType> finAccountTypeList);

	/**
	 * removeFinAccountType - removes a FinAccountType
	 * 
	 * @param finAccountType
	 */
	public void removeFinAccountType(FinAccountType finAccountType);

	/**
	 * removeFinAccountTypeList - removes a list of FinAccountType
	 * 
	 * @param finAccountTypeList
	 */
	public void removeFinAccountTypeList(List<FinAccountType> finAccountTypeList);

	/**
	 * findByFinAccountTypeLike - finds a list of FinAccountType Like
	 * 
	 * @param finAccountType
	 * @return the list of FinAccountType found
	 */
	public List<FinAccountType> findByFinAccountTypeLike(FinAccountType finAccountType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinAccountType>LikeFR - finds a list of FinAccountType> Like with a finder return object
	 * 
	 * @param finAccountType
	 * @return the list of FinAccountType found
	 */
	public FinderReturn findByFinAccountTypeLikeFR(FinAccountType finAccountType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
