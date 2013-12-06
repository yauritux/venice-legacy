package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogPickupReportCell;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogPickupReportCellSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportCellSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogPickupReportCell> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportCellSessionEJBRemote#persistLogPickupReportCell(com
	 * .gdn.venice.persistence.LogPickupReportCell)
	 */
	public LogPickupReportCell persistLogPickupReportCell(LogPickupReportCell logPickupReportCell);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportCellSessionEJBRemote#persistLogPickupReportCellList
	 * (java.util.List)
	 */
	public ArrayList<LogPickupReportCell> persistLogPickupReportCellList(
			List<LogPickupReportCell> logPickupReportCellList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportCellSessionEJBRemote#mergeLogPickupReportCell(com.
	 * gdn.venice.persistence.LogPickupReportCell)
	 */
	public LogPickupReportCell mergeLogPickupReportCell(LogPickupReportCell logPickupReportCell);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportCellSessionEJBRemote#mergeLogPickupReportCellList(
	 * java.util.List)
	 */
	public ArrayList<LogPickupReportCell> mergeLogPickupReportCellList(
			List<LogPickupReportCell> logPickupReportCellList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportCellSessionEJBRemote#removeLogPickupReportCell(com
	 * .gdn.venice.persistence.LogPickupReportCell)
	 */
	public void removeLogPickupReportCell(LogPickupReportCell logPickupReportCell);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportCellSessionEJBRemote#removeLogPickupReportCellList
	 * (java.util.List)
	 */
	public void removeLogPickupReportCellList(List<LogPickupReportCell> logPickupReportCellList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportCellSessionEJBRemote#findByLogPickupReportCellLike
	 * (com.gdn.venice.persistence.LogPickupReportCell, int, int)
	 */
	public List<LogPickupReportCell> findByLogPickupReportCellLike(LogPickupReportCell logPickupReportCell,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportCellSessionEJBRemote#findByLogPickupReportCellLikeFR
	 * (com.gdn.venice.persistence.LogPickupReportCell, int, int)
	 */
	public FinderReturn findByLogPickupReportCellLikeFR(LogPickupReportCell logPickupReportCell,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
