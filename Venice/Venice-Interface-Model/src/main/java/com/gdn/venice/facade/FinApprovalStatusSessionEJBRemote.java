package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinApprovalStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinApprovalStatusSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinApprovalStatus
	 */
	public List<FinApprovalStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinApprovalStatus persists a country
	 * 
	 * @param finApprovalStatus
	 * @return the persisted FinApprovalStatus
	 */
	public FinApprovalStatus persistFinApprovalStatus(FinApprovalStatus finApprovalStatus);

	/**
	 * persistFinApprovalStatusList - persists a list of FinApprovalStatus
	 * 
	 * @param finApprovalStatusList
	 * @return the list of persisted FinApprovalStatus
	 */
	public ArrayList<FinApprovalStatus> persistFinApprovalStatusList(
			List<FinApprovalStatus> finApprovalStatusList);

	/**
	 * mergeFinApprovalStatus - merges a FinApprovalStatus
	 * 
	 * @param finApprovalStatus
	 * @return the merged FinApprovalStatus
	 */
	public FinApprovalStatus mergeFinApprovalStatus(FinApprovalStatus finApprovalStatus);

	/**
	 * mergeFinApprovalStatusList - merges a list of FinApprovalStatus
	 * 
	 * @param finApprovalStatusList
	 * @return the merged list of FinApprovalStatus
	 */
	public ArrayList<FinApprovalStatus> mergeFinApprovalStatusList(
			List<FinApprovalStatus> finApprovalStatusList);

	/**
	 * removeFinApprovalStatus - removes a FinApprovalStatus
	 * 
	 * @param finApprovalStatus
	 */
	public void removeFinApprovalStatus(FinApprovalStatus finApprovalStatus);

	/**
	 * removeFinApprovalStatusList - removes a list of FinApprovalStatus
	 * 
	 * @param finApprovalStatusList
	 */
	public void removeFinApprovalStatusList(List<FinApprovalStatus> finApprovalStatusList);

	/**
	 * findByFinApprovalStatusLike - finds a list of FinApprovalStatus Like
	 * 
	 * @param finApprovalStatus
	 * @return the list of FinApprovalStatus found
	 */
	public List<FinApprovalStatus> findByFinApprovalStatusLike(FinApprovalStatus finApprovalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinApprovalStatus>LikeFR - finds a list of FinApprovalStatus> Like with a finder return object
	 * 
	 * @param finApprovalStatus
	 * @return the list of FinApprovalStatus found
	 */
	public FinderReturn findByFinApprovalStatusLikeFR(FinApprovalStatus finApprovalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
