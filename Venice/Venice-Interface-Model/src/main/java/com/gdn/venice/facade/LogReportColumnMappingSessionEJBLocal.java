package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogReportColumnMapping;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogReportColumnMappingSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportColumnMappingSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogReportColumnMapping> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportColumnMappingSessionEJBRemote#persistLogReportColumnMapping(com
	 * .gdn.venice.persistence.LogReportColumnMapping)
	 */
	public LogReportColumnMapping persistLogReportColumnMapping(LogReportColumnMapping logReportColumnMapping);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportColumnMappingSessionEJBRemote#persistLogReportColumnMappingList
	 * (java.util.List)
	 */
	public ArrayList<LogReportColumnMapping> persistLogReportColumnMappingList(
			List<LogReportColumnMapping> logReportColumnMappingList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportColumnMappingSessionEJBRemote#mergeLogReportColumnMapping(com.
	 * gdn.venice.persistence.LogReportColumnMapping)
	 */
	public LogReportColumnMapping mergeLogReportColumnMapping(LogReportColumnMapping logReportColumnMapping);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportColumnMappingSessionEJBRemote#mergeLogReportColumnMappingList(
	 * java.util.List)
	 */
	public ArrayList<LogReportColumnMapping> mergeLogReportColumnMappingList(
			List<LogReportColumnMapping> logReportColumnMappingList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportColumnMappingSessionEJBRemote#removeLogReportColumnMapping(com
	 * .gdn.venice.persistence.LogReportColumnMapping)
	 */
	public void removeLogReportColumnMapping(LogReportColumnMapping logReportColumnMapping);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportColumnMappingSessionEJBRemote#removeLogReportColumnMappingList
	 * (java.util.List)
	 */
	public void removeLogReportColumnMappingList(List<LogReportColumnMapping> logReportColumnMappingList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportColumnMappingSessionEJBRemote#findByLogReportColumnMappingLike
	 * (com.gdn.venice.persistence.LogReportColumnMapping, int, int)
	 */
	public List<LogReportColumnMapping> findByLogReportColumnMappingLike(LogReportColumnMapping logReportColumnMapping,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReportColumnMappingSessionEJBRemote#findByLogReportColumnMappingLikeFR
	 * (com.gdn.venice.persistence.LogReportColumnMapping, int, int)
	 */
	public FinderReturn findByLogReportColumnMappingLikeFR(LogReportColumnMapping logReportColumnMapping,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
