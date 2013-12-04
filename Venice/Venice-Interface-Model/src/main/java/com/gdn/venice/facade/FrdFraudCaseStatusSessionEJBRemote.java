package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdFraudCaseStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdFraudCaseStatusSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdFraudCaseStatus
	 */
	public List<FrdFraudCaseStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdFraudCaseStatus persists a country
	 * 
	 * @param frdFraudCaseStatus
	 * @return the persisted FrdFraudCaseStatus
	 */
	public FrdFraudCaseStatus persistFrdFraudCaseStatus(FrdFraudCaseStatus frdFraudCaseStatus);

	/**
	 * persistFrdFraudCaseStatusList - persists a list of FrdFraudCaseStatus
	 * 
	 * @param frdFraudCaseStatusList
	 * @return the list of persisted FrdFraudCaseStatus
	 */
	public ArrayList<FrdFraudCaseStatus> persistFrdFraudCaseStatusList(
			List<FrdFraudCaseStatus> frdFraudCaseStatusList);

	/**
	 * mergeFrdFraudCaseStatus - merges a FrdFraudCaseStatus
	 * 
	 * @param frdFraudCaseStatus
	 * @return the merged FrdFraudCaseStatus
	 */
	public FrdFraudCaseStatus mergeFrdFraudCaseStatus(FrdFraudCaseStatus frdFraudCaseStatus);

	/**
	 * mergeFrdFraudCaseStatusList - merges a list of FrdFraudCaseStatus
	 * 
	 * @param frdFraudCaseStatusList
	 * @return the merged list of FrdFraudCaseStatus
	 */
	public ArrayList<FrdFraudCaseStatus> mergeFrdFraudCaseStatusList(
			List<FrdFraudCaseStatus> frdFraudCaseStatusList);

	/**
	 * removeFrdFraudCaseStatus - removes a FrdFraudCaseStatus
	 * 
	 * @param frdFraudCaseStatus
	 */
	public void removeFrdFraudCaseStatus(FrdFraudCaseStatus frdFraudCaseStatus);

	/**
	 * removeFrdFraudCaseStatusList - removes a list of FrdFraudCaseStatus
	 * 
	 * @param frdFraudCaseStatusList
	 */
	public void removeFrdFraudCaseStatusList(List<FrdFraudCaseStatus> frdFraudCaseStatusList);

	/**
	 * findByFrdFraudCaseStatusLike - finds a list of FrdFraudCaseStatus Like
	 * 
	 * @param frdFraudCaseStatus
	 * @return the list of FrdFraudCaseStatus found
	 */
	public List<FrdFraudCaseStatus> findByFrdFraudCaseStatusLike(FrdFraudCaseStatus frdFraudCaseStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdFraudCaseStatus>LikeFR - finds a list of FrdFraudCaseStatus> Like with a finder return object
	 * 
	 * @param frdFraudCaseStatus
	 * @return the list of FrdFraudCaseStatus found
	 */
	public FinderReturn findByFrdFraudCaseStatusLikeFR(FrdFraudCaseStatus frdFraudCaseStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
