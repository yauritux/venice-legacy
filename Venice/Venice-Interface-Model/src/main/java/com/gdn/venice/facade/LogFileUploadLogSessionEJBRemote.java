package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogFileUploadLog;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogFileUploadLogSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogFileUploadLog
	 */
	public List<LogFileUploadLog> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogFileUploadLog persists a country
	 * 
	 * @param logFileUploadLog
	 * @return the persisted LogFileUploadLog
	 */
	public LogFileUploadLog persistLogFileUploadLog(LogFileUploadLog logFileUploadLog);

	/**
	 * persistLogFileUploadLogList - persists a list of LogFileUploadLog
	 * 
	 * @param logFileUploadLogList
	 * @return the list of persisted LogFileUploadLog
	 */
	public ArrayList<LogFileUploadLog> persistLogFileUploadLogList(
			List<LogFileUploadLog> logFileUploadLogList);

	/**
	 * mergeLogFileUploadLog - merges a LogFileUploadLog
	 * 
	 * @param logFileUploadLog
	 * @return the merged LogFileUploadLog
	 */
	public LogFileUploadLog mergeLogFileUploadLog(LogFileUploadLog logFileUploadLog);

	/**
	 * mergeLogFileUploadLogList - merges a list of LogFileUploadLog
	 * 
	 * @param logFileUploadLogList
	 * @return the merged list of LogFileUploadLog
	 */
	public ArrayList<LogFileUploadLog> mergeLogFileUploadLogList(
			List<LogFileUploadLog> logFileUploadLogList);

	/**
	 * removeLogFileUploadLog - removes a LogFileUploadLog
	 * 
	 * @param logFileUploadLog
	 */
	public void removeLogFileUploadLog(LogFileUploadLog logFileUploadLog);

	/**
	 * removeLogFileUploadLogList - removes a list of LogFileUploadLog
	 * 
	 * @param logFileUploadLogList
	 */
	public void removeLogFileUploadLogList(List<LogFileUploadLog> logFileUploadLogList);

	/**
	 * findByLogFileUploadLogLike - finds a list of LogFileUploadLog Like
	 * 
	 * @param logFileUploadLog
	 * @return the list of LogFileUploadLog found
	 */
	public List<LogFileUploadLog> findByLogFileUploadLogLike(LogFileUploadLog logFileUploadLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogFileUploadLog>LikeFR - finds a list of LogFileUploadLog> Like with a finder return object
	 * 
	 * @param logFileUploadLog
	 * @return the list of LogFileUploadLog found
	 */
	public FinderReturn findByLogFileUploadLogLikeFR(LogFileUploadLog logFileUploadLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
