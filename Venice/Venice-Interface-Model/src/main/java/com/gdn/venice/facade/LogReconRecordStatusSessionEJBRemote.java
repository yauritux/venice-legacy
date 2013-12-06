package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogReconRecordStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogReconRecordStatusSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogReconRecordStatus
	 */
	public List<LogReconRecordStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogReconRecordStatus persists a country
	 * 
	 * @param logReconRecordStatus
	 * @return the persisted LogReconRecordStatus
	 */
	public LogReconRecordStatus persistLogReconRecordStatus(LogReconRecordStatus logReconRecordStatus);

	/**
	 * persistLogReconRecordStatusList - persists a list of LogReconRecordStatus
	 * 
	 * @param logReconRecordStatusList
	 * @return the list of persisted LogReconRecordStatus
	 */
	public ArrayList<LogReconRecordStatus> persistLogReconRecordStatusList(
			List<LogReconRecordStatus> logReconRecordStatusList);

	/**
	 * mergeLogReconRecordStatus - merges a LogReconRecordStatus
	 * 
	 * @param logReconRecordStatus
	 * @return the merged LogReconRecordStatus
	 */
	public LogReconRecordStatus mergeLogReconRecordStatus(LogReconRecordStatus logReconRecordStatus);

	/**
	 * mergeLogReconRecordStatusList - merges a list of LogReconRecordStatus
	 * 
	 * @param logReconRecordStatusList
	 * @return the merged list of LogReconRecordStatus
	 */
	public ArrayList<LogReconRecordStatus> mergeLogReconRecordStatusList(
			List<LogReconRecordStatus> logReconRecordStatusList);

	/**
	 * removeLogReconRecordStatus - removes a LogReconRecordStatus
	 * 
	 * @param logReconRecordStatus
	 */
	public void removeLogReconRecordStatus(LogReconRecordStatus logReconRecordStatus);

	/**
	 * removeLogReconRecordStatusList - removes a list of LogReconRecordStatus
	 * 
	 * @param logReconRecordStatusList
	 */
	public void removeLogReconRecordStatusList(List<LogReconRecordStatus> logReconRecordStatusList);

	/**
	 * findByLogReconRecordStatusLike - finds a list of LogReconRecordStatus Like
	 * 
	 * @param logReconRecordStatus
	 * @return the list of LogReconRecordStatus found
	 */
	public List<LogReconRecordStatus> findByLogReconRecordStatusLike(LogReconRecordStatus logReconRecordStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogReconRecordStatus>LikeFR - finds a list of LogReconRecordStatus> Like with a finder return object
	 * 
	 * @param logReconRecordStatus
	 * @return the list of LogReconRecordStatus found
	 */
	public FinderReturn findByLogReconRecordStatusLikeFR(LogReconRecordStatus logReconRecordStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
