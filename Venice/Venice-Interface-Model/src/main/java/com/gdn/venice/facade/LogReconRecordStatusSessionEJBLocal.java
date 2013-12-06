package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogReconRecordStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogReconRecordStatusSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconRecordStatusSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogReconRecordStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconRecordStatusSessionEJBRemote#persistLogReconRecordStatus(com
	 * .gdn.venice.persistence.LogReconRecordStatus)
	 */
	public LogReconRecordStatus persistLogReconRecordStatus(LogReconRecordStatus logReconRecordStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconRecordStatusSessionEJBRemote#persistLogReconRecordStatusList
	 * (java.util.List)
	 */
	public ArrayList<LogReconRecordStatus> persistLogReconRecordStatusList(
			List<LogReconRecordStatus> logReconRecordStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconRecordStatusSessionEJBRemote#mergeLogReconRecordStatus(com.
	 * gdn.venice.persistence.LogReconRecordStatus)
	 */
	public LogReconRecordStatus mergeLogReconRecordStatus(LogReconRecordStatus logReconRecordStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconRecordStatusSessionEJBRemote#mergeLogReconRecordStatusList(
	 * java.util.List)
	 */
	public ArrayList<LogReconRecordStatus> mergeLogReconRecordStatusList(
			List<LogReconRecordStatus> logReconRecordStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconRecordStatusSessionEJBRemote#removeLogReconRecordStatus(com
	 * .gdn.venice.persistence.LogReconRecordStatus)
	 */
	public void removeLogReconRecordStatus(LogReconRecordStatus logReconRecordStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconRecordStatusSessionEJBRemote#removeLogReconRecordStatusList
	 * (java.util.List)
	 */
	public void removeLogReconRecordStatusList(List<LogReconRecordStatus> logReconRecordStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconRecordStatusSessionEJBRemote#findByLogReconRecordStatusLike
	 * (com.gdn.venice.persistence.LogReconRecordStatus, int, int)
	 */
	public List<LogReconRecordStatus> findByLogReconRecordStatusLike(LogReconRecordStatus logReconRecordStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconRecordStatusSessionEJBRemote#findByLogReconRecordStatusLikeFR
	 * (com.gdn.venice.persistence.LogReconRecordStatus, int, int)
	 */
	public FinderReturn findByLogReconRecordStatusLikeFR(LogReconRecordStatus logReconRecordStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
