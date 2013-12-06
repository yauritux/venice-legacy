package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogReportStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogReportStatusSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogReportStatus
	 */
	public List<LogReportStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogReportStatus persists a country
	 * 
	 * @param logReportStatus
	 * @return the persisted LogReportStatus
	 */
	public LogReportStatus persistLogReportStatus(LogReportStatus logReportStatus);

	/**
	 * persistLogReportStatusList - persists a list of LogReportStatus
	 * 
	 * @param logReportStatusList
	 * @return the list of persisted LogReportStatus
	 */
	public ArrayList<LogReportStatus> persistLogReportStatusList(
			List<LogReportStatus> logReportStatusList);

	/**
	 * mergeLogReportStatus - merges a LogReportStatus
	 * 
	 * @param logReportStatus
	 * @return the merged LogReportStatus
	 */
	public LogReportStatus mergeLogReportStatus(LogReportStatus logReportStatus);

	/**
	 * mergeLogReportStatusList - merges a list of LogReportStatus
	 * 
	 * @param logReportStatusList
	 * @return the merged list of LogReportStatus
	 */
	public ArrayList<LogReportStatus> mergeLogReportStatusList(
			List<LogReportStatus> logReportStatusList);

	/**
	 * removeLogReportStatus - removes a LogReportStatus
	 * 
	 * @param logReportStatus
	 */
	public void removeLogReportStatus(LogReportStatus logReportStatus);

	/**
	 * removeLogReportStatusList - removes a list of LogReportStatus
	 * 
	 * @param logReportStatusList
	 */
	public void removeLogReportStatusList(List<LogReportStatus> logReportStatusList);

	/**
	 * findByLogReportStatusLike - finds a list of LogReportStatus Like
	 * 
	 * @param logReportStatus
	 * @return the list of LogReportStatus found
	 */
	public List<LogReportStatus> findByLogReportStatusLike(LogReportStatus logReportStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogReportStatus>LikeFR - finds a list of LogReportStatus> Like with a finder return object
	 * 
	 * @param logReportStatus
	 * @return the list of LogReportStatus found
	 */
	public FinderReturn findByLogReportStatusLikeFR(LogReportStatus logReportStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
