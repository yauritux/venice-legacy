package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArFundsInActionApplied;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinArFundsInActionAppliedSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinArFundsInActionApplied
	 */
	public List<FinArFundsInActionApplied> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinArFundsInActionApplied persists a country
	 * 
	 * @param finArFundsInActionApplied
	 * @return the persisted FinArFundsInActionApplied
	 */
	public FinArFundsInActionApplied persistFinArFundsInActionApplied(FinArFundsInActionApplied finArFundsInActionApplied);

	/**
	 * persistFinArFundsInActionAppliedList - persists a list of FinArFundsInActionApplied
	 * 
	 * @param finArFundsInActionAppliedList
	 * @return the list of persisted FinArFundsInActionApplied
	 */
	public ArrayList<FinArFundsInActionApplied> persistFinArFundsInActionAppliedList(
			List<FinArFundsInActionApplied> finArFundsInActionAppliedList);

	/**
	 * mergeFinArFundsInActionApplied - merges a FinArFundsInActionApplied
	 * 
	 * @param finArFundsInActionApplied
	 * @return the merged FinArFundsInActionApplied
	 */
	public FinArFundsInActionApplied mergeFinArFundsInActionApplied(FinArFundsInActionApplied finArFundsInActionApplied);

	/**
	 * mergeFinArFundsInActionAppliedList - merges a list of FinArFundsInActionApplied
	 * 
	 * @param finArFundsInActionAppliedList
	 * @return the merged list of FinArFundsInActionApplied
	 */
	public ArrayList<FinArFundsInActionApplied> mergeFinArFundsInActionAppliedList(
			List<FinArFundsInActionApplied> finArFundsInActionAppliedList);

	/**
	 * removeFinArFundsInActionApplied - removes a FinArFundsInActionApplied
	 * 
	 * @param finArFundsInActionApplied
	 */
	public void removeFinArFundsInActionApplied(FinArFundsInActionApplied finArFundsInActionApplied);

	/**
	 * removeFinArFundsInActionAppliedList - removes a list of FinArFundsInActionApplied
	 * 
	 * @param finArFundsInActionAppliedList
	 */
	public void removeFinArFundsInActionAppliedList(List<FinArFundsInActionApplied> finArFundsInActionAppliedList);

	/**
	 * findByFinArFundsInActionAppliedLike - finds a list of FinArFundsInActionApplied Like
	 * 
	 * @param finArFundsInActionApplied
	 * @return the list of FinArFundsInActionApplied found
	 */
	public List<FinArFundsInActionApplied> findByFinArFundsInActionAppliedLike(FinArFundsInActionApplied finArFundsInActionApplied,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinArFundsInActionApplied>LikeFR - finds a list of FinArFundsInActionApplied> Like with a finder return object
	 * 
	 * @param finArFundsInActionApplied
	 * @return the list of FinArFundsInActionApplied found
	 */
	public FinderReturn findByFinArFundsInActionAppliedLikeFR(FinArFundsInActionApplied finArFundsInActionApplied,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
