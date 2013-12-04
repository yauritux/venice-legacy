package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogApprovalStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogApprovalStatusSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogApprovalStatus
	 */
	public List<LogApprovalStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogApprovalStatus persists a country
	 * 
	 * @param logApprovalStatus
	 * @return the persisted LogApprovalStatus
	 */
	public LogApprovalStatus persistLogApprovalStatus(LogApprovalStatus logApprovalStatus);

	/**
	 * persistLogApprovalStatusList - persists a list of LogApprovalStatus
	 * 
	 * @param logApprovalStatusList
	 * @return the list of persisted LogApprovalStatus
	 */
	public ArrayList<LogApprovalStatus> persistLogApprovalStatusList(
			List<LogApprovalStatus> logApprovalStatusList);

	/**
	 * mergeLogApprovalStatus - merges a LogApprovalStatus
	 * 
	 * @param logApprovalStatus
	 * @return the merged LogApprovalStatus
	 */
	public LogApprovalStatus mergeLogApprovalStatus(LogApprovalStatus logApprovalStatus);

	/**
	 * mergeLogApprovalStatusList - merges a list of LogApprovalStatus
	 * 
	 * @param logApprovalStatusList
	 * @return the merged list of LogApprovalStatus
	 */
	public ArrayList<LogApprovalStatus> mergeLogApprovalStatusList(
			List<LogApprovalStatus> logApprovalStatusList);

	/**
	 * removeLogApprovalStatus - removes a LogApprovalStatus
	 * 
	 * @param logApprovalStatus
	 */
	public void removeLogApprovalStatus(LogApprovalStatus logApprovalStatus);

	/**
	 * removeLogApprovalStatusList - removes a list of LogApprovalStatus
	 * 
	 * @param logApprovalStatusList
	 */
	public void removeLogApprovalStatusList(List<LogApprovalStatus> logApprovalStatusList);

	/**
	 * findByLogApprovalStatusLike - finds a list of LogApprovalStatus Like
	 * 
	 * @param logApprovalStatus
	 * @return the list of LogApprovalStatus found
	 */
	public List<LogApprovalStatus> findByLogApprovalStatusLike(LogApprovalStatus logApprovalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogApprovalStatus>LikeFR - finds a list of LogApprovalStatus> Like with a finder return object
	 * 
	 * @param logApprovalStatus
	 * @return the list of LogApprovalStatus found
	 */
	public FinderReturn findByLogApprovalStatusLikeFR(LogApprovalStatus logApprovalStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
