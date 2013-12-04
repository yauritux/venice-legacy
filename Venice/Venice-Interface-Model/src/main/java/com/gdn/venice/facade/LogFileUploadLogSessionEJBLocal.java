package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogFileUploadLog;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogFileUploadLogSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogFileUploadLogSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogFileUploadLog> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogFileUploadLogSessionEJBRemote#persistLogFileUploadLog(com
	 * .gdn.venice.persistence.LogFileUploadLog)
	 */
	public LogFileUploadLog persistLogFileUploadLog(LogFileUploadLog logFileUploadLog);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogFileUploadLogSessionEJBRemote#persistLogFileUploadLogList
	 * (java.util.List)
	 */
	public ArrayList<LogFileUploadLog> persistLogFileUploadLogList(
			List<LogFileUploadLog> logFileUploadLogList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogFileUploadLogSessionEJBRemote#mergeLogFileUploadLog(com.
	 * gdn.venice.persistence.LogFileUploadLog)
	 */
	public LogFileUploadLog mergeLogFileUploadLog(LogFileUploadLog logFileUploadLog);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogFileUploadLogSessionEJBRemote#mergeLogFileUploadLogList(
	 * java.util.List)
	 */
	public ArrayList<LogFileUploadLog> mergeLogFileUploadLogList(
			List<LogFileUploadLog> logFileUploadLogList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogFileUploadLogSessionEJBRemote#removeLogFileUploadLog(com
	 * .gdn.venice.persistence.LogFileUploadLog)
	 */
	public void removeLogFileUploadLog(LogFileUploadLog logFileUploadLog);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogFileUploadLogSessionEJBRemote#removeLogFileUploadLogList
	 * (java.util.List)
	 */
	public void removeLogFileUploadLogList(List<LogFileUploadLog> logFileUploadLogList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogFileUploadLogSessionEJBRemote#findByLogFileUploadLogLike
	 * (com.gdn.venice.persistence.LogFileUploadLog, int, int)
	 */
	public List<LogFileUploadLog> findByLogFileUploadLogLike(LogFileUploadLog logFileUploadLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogFileUploadLogSessionEJBRemote#findByLogFileUploadLogLikeFR
	 * (com.gdn.venice.persistence.LogFileUploadLog, int, int)
	 */
	public FinderReturn findByLogFileUploadLogLikeFR(LogFileUploadLog logFileUploadLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
