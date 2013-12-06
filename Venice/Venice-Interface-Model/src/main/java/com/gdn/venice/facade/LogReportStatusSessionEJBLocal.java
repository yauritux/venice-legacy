package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogReportStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogReportStatusSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportStatusSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogReportStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportStatusSessionEJBRemote#persistLogReportStatus(com
	 * .gdn.venice.persistence.LogReportStatus)
	 */
	public LogReportStatus persistLogReportStatus(LogReportStatus logReportStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportStatusSessionEJBRemote#persistLogReportStatusList
	 * (java.util.List)
	 */
	public ArrayList<LogReportStatus> persistLogReportStatusList(
			List<LogReportStatus> logReportStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportStatusSessionEJBRemote#mergeLogReportStatus(com.
	 * gdn.venice.persistence.LogReportStatus)
	 */
	public LogReportStatus mergeLogReportStatus(LogReportStatus logReportStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportStatusSessionEJBRemote#mergeLogReportStatusList(
	 * java.util.List)
	 */
	public ArrayList<LogReportStatus> mergeLogReportStatusList(
			List<LogReportStatus> logReportStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportStatusSessionEJBRemote#removeLogReportStatus(com
	 * .gdn.venice.persistence.LogReportStatus)
	 */
	public void removeLogReportStatus(LogReportStatus logReportStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportStatusSessionEJBRemote#removeLogReportStatusList
	 * (java.util.List)
	 */
	public void removeLogReportStatusList(List<LogReportStatus> logReportStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportStatusSessionEJBRemote#findByLogReportStatusLike
	 * (com.gdn.venice.persistence.LogReportStatus, int, int)
	 */
	public List<LogReportStatus> findByLogReportStatusLike(LogReportStatus logReportStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportStatusSessionEJBRemote#findByLogReportStatusLikeFR
	 * (com.gdn.venice.persistence.LogReportStatus, int, int)
	 */
	public FinderReturn findByLogReportStatusLikeFR(LogReportStatus logReportStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
