package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinTransactionStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinTransactionStatusSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinTransactionStatus
	 */
	public List<FinTransactionStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinTransactionStatus persists a country
	 * 
	 * @param finTransactionStatus
	 * @return the persisted FinTransactionStatus
	 */
	public FinTransactionStatus persistFinTransactionStatus(FinTransactionStatus finTransactionStatus);

	/**
	 * persistFinTransactionStatusList - persists a list of FinTransactionStatus
	 * 
	 * @param finTransactionStatusList
	 * @return the list of persisted FinTransactionStatus
	 */
	public ArrayList<FinTransactionStatus> persistFinTransactionStatusList(
			List<FinTransactionStatus> finTransactionStatusList);

	/**
	 * mergeFinTransactionStatus - merges a FinTransactionStatus
	 * 
	 * @param finTransactionStatus
	 * @return the merged FinTransactionStatus
	 */
	public FinTransactionStatus mergeFinTransactionStatus(FinTransactionStatus finTransactionStatus);

	/**
	 * mergeFinTransactionStatusList - merges a list of FinTransactionStatus
	 * 
	 * @param finTransactionStatusList
	 * @return the merged list of FinTransactionStatus
	 */
	public ArrayList<FinTransactionStatus> mergeFinTransactionStatusList(
			List<FinTransactionStatus> finTransactionStatusList);

	/**
	 * removeFinTransactionStatus - removes a FinTransactionStatus
	 * 
	 * @param finTransactionStatus
	 */
	public void removeFinTransactionStatus(FinTransactionStatus finTransactionStatus);

	/**
	 * removeFinTransactionStatusList - removes a list of FinTransactionStatus
	 * 
	 * @param finTransactionStatusList
	 */
	public void removeFinTransactionStatusList(List<FinTransactionStatus> finTransactionStatusList);

	/**
	 * findByFinTransactionStatusLike - finds a list of FinTransactionStatus Like
	 * 
	 * @param finTransactionStatus
	 * @return the list of FinTransactionStatus found
	 */
	public List<FinTransactionStatus> findByFinTransactionStatusLike(FinTransactionStatus finTransactionStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinTransactionStatus>LikeFR - finds a list of FinTransactionStatus> Like with a finder return object
	 * 
	 * @param finTransactionStatus
	 * @return the list of FinTransactionStatus found
	 */
	public FinderReturn findByFinTransactionStatusLikeFR(FinTransactionStatus finTransactionStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
