package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdRelatedFraudCase;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdRelatedFraudCaseSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdRelatedFraudCase
	 */
	public List<FrdRelatedFraudCase> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdRelatedFraudCase persists a country
	 * 
	 * @param frdRelatedFraudCase
	 * @return the persisted FrdRelatedFraudCase
	 */
	public FrdRelatedFraudCase persistFrdRelatedFraudCase(FrdRelatedFraudCase frdRelatedFraudCase);

	/**
	 * persistFrdRelatedFraudCaseList - persists a list of FrdRelatedFraudCase
	 * 
	 * @param frdRelatedFraudCaseList
	 * @return the list of persisted FrdRelatedFraudCase
	 */
	public ArrayList<FrdRelatedFraudCase> persistFrdRelatedFraudCaseList(
			List<FrdRelatedFraudCase> frdRelatedFraudCaseList);

	/**
	 * mergeFrdRelatedFraudCase - merges a FrdRelatedFraudCase
	 * 
	 * @param frdRelatedFraudCase
	 * @return the merged FrdRelatedFraudCase
	 */
	public FrdRelatedFraudCase mergeFrdRelatedFraudCase(FrdRelatedFraudCase frdRelatedFraudCase);

	/**
	 * mergeFrdRelatedFraudCaseList - merges a list of FrdRelatedFraudCase
	 * 
	 * @param frdRelatedFraudCaseList
	 * @return the merged list of FrdRelatedFraudCase
	 */
	public ArrayList<FrdRelatedFraudCase> mergeFrdRelatedFraudCaseList(
			List<FrdRelatedFraudCase> frdRelatedFraudCaseList);

	/**
	 * removeFrdRelatedFraudCase - removes a FrdRelatedFraudCase
	 * 
	 * @param frdRelatedFraudCase
	 */
	public void removeFrdRelatedFraudCase(FrdRelatedFraudCase frdRelatedFraudCase);

	/**
	 * removeFrdRelatedFraudCaseList - removes a list of FrdRelatedFraudCase
	 * 
	 * @param frdRelatedFraudCaseList
	 */
	public void removeFrdRelatedFraudCaseList(List<FrdRelatedFraudCase> frdRelatedFraudCaseList);

	/**
	 * findByFrdRelatedFraudCaseLike - finds a list of FrdRelatedFraudCase Like
	 * 
	 * @param frdRelatedFraudCase
	 * @return the list of FrdRelatedFraudCase found
	 */
	public List<FrdRelatedFraudCase> findByFrdRelatedFraudCaseLike(FrdRelatedFraudCase frdRelatedFraudCase,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdRelatedFraudCase>LikeFR - finds a list of FrdRelatedFraudCase> Like with a finder return object
	 * 
	 * @param frdRelatedFraudCase
	 * @return the list of FrdRelatedFraudCase found
	 */
	public FinderReturn findByFrdRelatedFraudCaseLikeFR(FrdRelatedFraudCase frdRelatedFraudCase,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
