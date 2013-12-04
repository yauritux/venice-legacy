package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogReconActivityRecordResult;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogReconActivityRecordResultSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogReconActivityRecordResult
	 */
	public List<LogReconActivityRecordResult> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogReconActivityRecordResult persists a country
	 * 
	 * @param logReconActivityRecordResult
	 * @return the persisted LogReconActivityRecordResult
	 */
	public LogReconActivityRecordResult persistLogReconActivityRecordResult(LogReconActivityRecordResult logReconActivityRecordResult);

	/**
	 * persistLogReconActivityRecordResultList - persists a list of LogReconActivityRecordResult
	 * 
	 * @param logReconActivityRecordResultList
	 * @return the list of persisted LogReconActivityRecordResult
	 */
	public ArrayList<LogReconActivityRecordResult> persistLogReconActivityRecordResultList(
			List<LogReconActivityRecordResult> logReconActivityRecordResultList);

	/**
	 * mergeLogReconActivityRecordResult - merges a LogReconActivityRecordResult
	 * 
	 * @param logReconActivityRecordResult
	 * @return the merged LogReconActivityRecordResult
	 */
	public LogReconActivityRecordResult mergeLogReconActivityRecordResult(LogReconActivityRecordResult logReconActivityRecordResult);

	/**
	 * mergeLogReconActivityRecordResultList - merges a list of LogReconActivityRecordResult
	 * 
	 * @param logReconActivityRecordResultList
	 * @return the merged list of LogReconActivityRecordResult
	 */
	public ArrayList<LogReconActivityRecordResult> mergeLogReconActivityRecordResultList(
			List<LogReconActivityRecordResult> logReconActivityRecordResultList);

	/**
	 * removeLogReconActivityRecordResult - removes a LogReconActivityRecordResult
	 * 
	 * @param logReconActivityRecordResult
	 */
	public void removeLogReconActivityRecordResult(LogReconActivityRecordResult logReconActivityRecordResult);

	/**
	 * removeLogReconActivityRecordResultList - removes a list of LogReconActivityRecordResult
	 * 
	 * @param logReconActivityRecordResultList
	 */
	public void removeLogReconActivityRecordResultList(List<LogReconActivityRecordResult> logReconActivityRecordResultList);

	/**
	 * findByLogReconActivityRecordResultLike - finds a list of LogReconActivityRecordResult Like
	 * 
	 * @param logReconActivityRecordResult
	 * @return the list of LogReconActivityRecordResult found
	 */
	public List<LogReconActivityRecordResult> findByLogReconActivityRecordResultLike(LogReconActivityRecordResult logReconActivityRecordResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogReconActivityRecordResult>LikeFR - finds a list of LogReconActivityRecordResult> Like with a finder return object
	 * 
	 * @param logReconActivityRecordResult
	 * @return the list of LogReconActivityRecordResult found
	 */
	public FinderReturn findByLogReconActivityRecordResultLikeFR(LogReconActivityRecordResult logReconActivityRecordResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
