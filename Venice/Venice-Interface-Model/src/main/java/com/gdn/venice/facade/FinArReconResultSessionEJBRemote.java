package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArReconResult;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinArReconResultSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinArReconResult
	 */
	public List<FinArReconResult> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinArReconResult persists a country
	 * 
	 * @param finArReconResult
	 * @return the persisted FinArReconResult
	 */
	public FinArReconResult persistFinArReconResult(FinArReconResult finArReconResult);

	/**
	 * persistFinArReconResultList - persists a list of FinArReconResult
	 * 
	 * @param finArReconResultList
	 * @return the list of persisted FinArReconResult
	 */
	public ArrayList<FinArReconResult> persistFinArReconResultList(
			List<FinArReconResult> finArReconResultList);

	/**
	 * mergeFinArReconResult - merges a FinArReconResult
	 * 
	 * @param finArReconResult
	 * @return the merged FinArReconResult
	 */
	public FinArReconResult mergeFinArReconResult(FinArReconResult finArReconResult);

	/**
	 * mergeFinArReconResultList - merges a list of FinArReconResult
	 * 
	 * @param finArReconResultList
	 * @return the merged list of FinArReconResult
	 */
	public ArrayList<FinArReconResult> mergeFinArReconResultList(
			List<FinArReconResult> finArReconResultList);

	/**
	 * removeFinArReconResult - removes a FinArReconResult
	 * 
	 * @param finArReconResult
	 */
	public void removeFinArReconResult(FinArReconResult finArReconResult);

	/**
	 * removeFinArReconResultList - removes a list of FinArReconResult
	 * 
	 * @param finArReconResultList
	 */
	public void removeFinArReconResultList(List<FinArReconResult> finArReconResultList);

	/**
	 * findByFinArReconResultLike - finds a list of FinArReconResult Like
	 * 
	 * @param finArReconResult
	 * @return the list of FinArReconResult found
	 */
	public List<FinArReconResult> findByFinArReconResultLike(FinArReconResult finArReconResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinArReconResult>LikeFR - finds a list of FinArReconResult> Like with a finder return object
	 * 
	 * @param finArReconResult
	 * @return the list of FinArReconResult found
	 */
	public FinderReturn findByFinArReconResultLikeFR(FinArReconResult finArReconResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
