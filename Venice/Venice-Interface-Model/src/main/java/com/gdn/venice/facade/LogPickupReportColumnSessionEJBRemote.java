package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogPickupReportColumn;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogPickupReportColumnSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogPickupReportColumn
	 */
	public List<LogPickupReportColumn> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogPickupReportColumn persists a country
	 * 
	 * @param logPickupReportColumn
	 * @return the persisted LogPickupReportColumn
	 */
	public LogPickupReportColumn persistLogPickupReportColumn(LogPickupReportColumn logPickupReportColumn);

	/**
	 * persistLogPickupReportColumnList - persists a list of LogPickupReportColumn
	 * 
	 * @param logPickupReportColumnList
	 * @return the list of persisted LogPickupReportColumn
	 */
	public ArrayList<LogPickupReportColumn> persistLogPickupReportColumnList(
			List<LogPickupReportColumn> logPickupReportColumnList);

	/**
	 * mergeLogPickupReportColumn - merges a LogPickupReportColumn
	 * 
	 * @param logPickupReportColumn
	 * @return the merged LogPickupReportColumn
	 */
	public LogPickupReportColumn mergeLogPickupReportColumn(LogPickupReportColumn logPickupReportColumn);

	/**
	 * mergeLogPickupReportColumnList - merges a list of LogPickupReportColumn
	 * 
	 * @param logPickupReportColumnList
	 * @return the merged list of LogPickupReportColumn
	 */
	public ArrayList<LogPickupReportColumn> mergeLogPickupReportColumnList(
			List<LogPickupReportColumn> logPickupReportColumnList);

	/**
	 * removeLogPickupReportColumn - removes a LogPickupReportColumn
	 * 
	 * @param logPickupReportColumn
	 */
	public void removeLogPickupReportColumn(LogPickupReportColumn logPickupReportColumn);

	/**
	 * removeLogPickupReportColumnList - removes a list of LogPickupReportColumn
	 * 
	 * @param logPickupReportColumnList
	 */
	public void removeLogPickupReportColumnList(List<LogPickupReportColumn> logPickupReportColumnList);

	/**
	 * findByLogPickupReportColumnLike - finds a list of LogPickupReportColumn Like
	 * 
	 * @param logPickupReportColumn
	 * @return the list of LogPickupReportColumn found
	 */
	public List<LogPickupReportColumn> findByLogPickupReportColumnLike(LogPickupReportColumn logPickupReportColumn,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogPickupReportColumn>LikeFR - finds a list of LogPickupReportColumn> Like with a finder return object
	 * 
	 * @param logPickupReportColumn
	 * @return the list of LogPickupReportColumn found
	 */
	public FinderReturn findByLogPickupReportColumnLikeFR(LogPickupReportColumn logPickupReportColumn,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
