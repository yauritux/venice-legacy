package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogReportColumnMapping;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogReportColumnMappingSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogReportColumnMapping
	 */
	public List<LogReportColumnMapping> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogReportColumnMapping persists a country
	 * 
	 * @param logReportColumnMapping
	 * @return the persisted LogReportColumnMapping
	 */
	public LogReportColumnMapping persistLogReportColumnMapping(LogReportColumnMapping logReportColumnMapping);

	/**
	 * persistLogReportColumnMappingList - persists a list of LogReportColumnMapping
	 * 
	 * @param logReportColumnMappingList
	 * @return the list of persisted LogReportColumnMapping
	 */
	public ArrayList<LogReportColumnMapping> persistLogReportColumnMappingList(
			List<LogReportColumnMapping> logReportColumnMappingList);

	/**
	 * mergeLogReportColumnMapping - merges a LogReportColumnMapping
	 * 
	 * @param logReportColumnMapping
	 * @return the merged LogReportColumnMapping
	 */
	public LogReportColumnMapping mergeLogReportColumnMapping(LogReportColumnMapping logReportColumnMapping);

	/**
	 * mergeLogReportColumnMappingList - merges a list of LogReportColumnMapping
	 * 
	 * @param logReportColumnMappingList
	 * @return the merged list of LogReportColumnMapping
	 */
	public ArrayList<LogReportColumnMapping> mergeLogReportColumnMappingList(
			List<LogReportColumnMapping> logReportColumnMappingList);

	/**
	 * removeLogReportColumnMapping - removes a LogReportColumnMapping
	 * 
	 * @param logReportColumnMapping
	 */
	public void removeLogReportColumnMapping(LogReportColumnMapping logReportColumnMapping);

	/**
	 * removeLogReportColumnMappingList - removes a list of LogReportColumnMapping
	 * 
	 * @param logReportColumnMappingList
	 */
	public void removeLogReportColumnMappingList(List<LogReportColumnMapping> logReportColumnMappingList);

	/**
	 * findByLogReportColumnMappingLike - finds a list of LogReportColumnMapping Like
	 * 
	 * @param logReportColumnMapping
	 * @return the list of LogReportColumnMapping found
	 */
	public List<LogReportColumnMapping> findByLogReportColumnMappingLike(LogReportColumnMapping logReportColumnMapping,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogReportColumnMapping>LikeFR - finds a list of LogReportColumnMapping> Like with a finder return object
	 * 
	 * @param logReportColumnMapping
	 * @return the list of LogReportColumnMapping found
	 */
	public FinderReturn findByLogReportColumnMappingLikeFR(LogReportColumnMapping logReportColumnMapping,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
