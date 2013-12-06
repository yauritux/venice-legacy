package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogPickupReportCell;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogPickupReportCellSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogPickupReportCell
	 */
	public List<LogPickupReportCell> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogPickupReportCell persists a country
	 * 
	 * @param logPickupReportCell
	 * @return the persisted LogPickupReportCell
	 */
	public LogPickupReportCell persistLogPickupReportCell(LogPickupReportCell logPickupReportCell);

	/**
	 * persistLogPickupReportCellList - persists a list of LogPickupReportCell
	 * 
	 * @param logPickupReportCellList
	 * @return the list of persisted LogPickupReportCell
	 */
	public ArrayList<LogPickupReportCell> persistLogPickupReportCellList(
			List<LogPickupReportCell> logPickupReportCellList);

	/**
	 * mergeLogPickupReportCell - merges a LogPickupReportCell
	 * 
	 * @param logPickupReportCell
	 * @return the merged LogPickupReportCell
	 */
	public LogPickupReportCell mergeLogPickupReportCell(LogPickupReportCell logPickupReportCell);

	/**
	 * mergeLogPickupReportCellList - merges a list of LogPickupReportCell
	 * 
	 * @param logPickupReportCellList
	 * @return the merged list of LogPickupReportCell
	 */
	public ArrayList<LogPickupReportCell> mergeLogPickupReportCellList(
			List<LogPickupReportCell> logPickupReportCellList);

	/**
	 * removeLogPickupReportCell - removes a LogPickupReportCell
	 * 
	 * @param logPickupReportCell
	 */
	public void removeLogPickupReportCell(LogPickupReportCell logPickupReportCell);

	/**
	 * removeLogPickupReportCellList - removes a list of LogPickupReportCell
	 * 
	 * @param logPickupReportCellList
	 */
	public void removeLogPickupReportCellList(List<LogPickupReportCell> logPickupReportCellList);

	/**
	 * findByLogPickupReportCellLike - finds a list of LogPickupReportCell Like
	 * 
	 * @param logPickupReportCell
	 * @return the list of LogPickupReportCell found
	 */
	public List<LogPickupReportCell> findByLogPickupReportCellLike(LogPickupReportCell logPickupReportCell,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogPickupReportCell>LikeFR - finds a list of LogPickupReportCell> Like with a finder return object
	 * 
	 * @param logPickupReportCell
	 * @return the list of LogPickupReportCell found
	 */
	public FinderReturn findByLogPickupReportCellLikeFR(LogPickupReportCell logPickupReportCell,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
