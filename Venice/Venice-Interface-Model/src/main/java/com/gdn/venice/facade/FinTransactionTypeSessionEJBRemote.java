package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinTransactionType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinTransactionTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinTransactionType
	 */
	public List<FinTransactionType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinTransactionType persists a country
	 * 
	 * @param finTransactionType
	 * @return the persisted FinTransactionType
	 */
	public FinTransactionType persistFinTransactionType(FinTransactionType finTransactionType);

	/**
	 * persistFinTransactionTypeList - persists a list of FinTransactionType
	 * 
	 * @param finTransactionTypeList
	 * @return the list of persisted FinTransactionType
	 */
	public ArrayList<FinTransactionType> persistFinTransactionTypeList(
			List<FinTransactionType> finTransactionTypeList);

	/**
	 * mergeFinTransactionType - merges a FinTransactionType
	 * 
	 * @param finTransactionType
	 * @return the merged FinTransactionType
	 */
	public FinTransactionType mergeFinTransactionType(FinTransactionType finTransactionType);

	/**
	 * mergeFinTransactionTypeList - merges a list of FinTransactionType
	 * 
	 * @param finTransactionTypeList
	 * @return the merged list of FinTransactionType
	 */
	public ArrayList<FinTransactionType> mergeFinTransactionTypeList(
			List<FinTransactionType> finTransactionTypeList);

	/**
	 * removeFinTransactionType - removes a FinTransactionType
	 * 
	 * @param finTransactionType
	 */
	public void removeFinTransactionType(FinTransactionType finTransactionType);

	/**
	 * removeFinTransactionTypeList - removes a list of FinTransactionType
	 * 
	 * @param finTransactionTypeList
	 */
	public void removeFinTransactionTypeList(List<FinTransactionType> finTransactionTypeList);

	/**
	 * findByFinTransactionTypeLike - finds a list of FinTransactionType Like
	 * 
	 * @param finTransactionType
	 * @return the list of FinTransactionType found
	 */
	public List<FinTransactionType> findByFinTransactionTypeLike(FinTransactionType finTransactionType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinTransactionType>LikeFR - finds a list of FinTransactionType> Like with a finder return object
	 * 
	 * @param finTransactionType
	 * @return the list of FinTransactionType found
	 */
	public FinderReturn findByFinTransactionTypeLikeFR(FinTransactionType finTransactionType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
