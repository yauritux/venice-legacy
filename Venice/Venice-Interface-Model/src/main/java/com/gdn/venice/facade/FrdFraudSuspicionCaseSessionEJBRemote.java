package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdFraudSuspicionCase;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdFraudSuspicionCaseSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdFraudSuspicionCase
	 */
	public List<FrdFraudSuspicionCase> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdFraudSuspicionCase persists a country
	 * 
	 * @param frdFraudSuspicionCase
	 * @return the persisted FrdFraudSuspicionCase
	 */
	public FrdFraudSuspicionCase persistFrdFraudSuspicionCase(FrdFraudSuspicionCase frdFraudSuspicionCase);

	/**
	 * persistFrdFraudSuspicionCaseList - persists a list of FrdFraudSuspicionCase
	 * 
	 * @param frdFraudSuspicionCaseList
	 * @return the list of persisted FrdFraudSuspicionCase
	 */
	public ArrayList<FrdFraudSuspicionCase> persistFrdFraudSuspicionCaseList(
			List<FrdFraudSuspicionCase> frdFraudSuspicionCaseList);

	/**
	 * mergeFrdFraudSuspicionCase - merges a FrdFraudSuspicionCase
	 * 
	 * @param frdFraudSuspicionCase
	 * @return the merged FrdFraudSuspicionCase
	 */
	public FrdFraudSuspicionCase mergeFrdFraudSuspicionCase(FrdFraudSuspicionCase frdFraudSuspicionCase);

	/**
	 * mergeFrdFraudSuspicionCaseList - merges a list of FrdFraudSuspicionCase
	 * 
	 * @param frdFraudSuspicionCaseList
	 * @return the merged list of FrdFraudSuspicionCase
	 */
	public ArrayList<FrdFraudSuspicionCase> mergeFrdFraudSuspicionCaseList(
			List<FrdFraudSuspicionCase> frdFraudSuspicionCaseList);

	/**
	 * removeFrdFraudSuspicionCase - removes a FrdFraudSuspicionCase
	 * 
	 * @param frdFraudSuspicionCase
	 */
	public void removeFrdFraudSuspicionCase(FrdFraudSuspicionCase frdFraudSuspicionCase);

	/**
	 * removeFrdFraudSuspicionCaseList - removes a list of FrdFraudSuspicionCase
	 * 
	 * @param frdFraudSuspicionCaseList
	 */
	public void removeFrdFraudSuspicionCaseList(List<FrdFraudSuspicionCase> frdFraudSuspicionCaseList);

	/**
	 * findByFrdFraudSuspicionCaseLike - finds a list of FrdFraudSuspicionCase Like
	 * 
	 * @param frdFraudSuspicionCase
	 * @return the list of FrdFraudSuspicionCase found
	 */
	public List<FrdFraudSuspicionCase> findByFrdFraudSuspicionCaseLike(FrdFraudSuspicionCase frdFraudSuspicionCase,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdFraudSuspicionCase>LikeFR - finds a list of FrdFraudSuspicionCase> Like with a finder return object
	 * 
	 * @param frdFraudSuspicionCase
	 * @return the list of FrdFraudSuspicionCase found
	 */
	public FinderReturn findByFrdFraudSuspicionCaseLikeFR(FrdFraudSuspicionCase frdFraudSuspicionCase,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
