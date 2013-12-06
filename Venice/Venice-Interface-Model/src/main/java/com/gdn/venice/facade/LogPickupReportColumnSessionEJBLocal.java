package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogPickupReportColumn;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogPickupReportColumnSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportColumnSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogPickupReportColumn> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportColumnSessionEJBRemote#persistLogPickupReportColumn(com
	 * .gdn.venice.persistence.LogPickupReportColumn)
	 */
	public LogPickupReportColumn persistLogPickupReportColumn(LogPickupReportColumn logPickupReportColumn);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportColumnSessionEJBRemote#persistLogPickupReportColumnList
	 * (java.util.List)
	 */
	public ArrayList<LogPickupReportColumn> persistLogPickupReportColumnList(
			List<LogPickupReportColumn> logPickupReportColumnList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportColumnSessionEJBRemote#mergeLogPickupReportColumn(com.
	 * gdn.venice.persistence.LogPickupReportColumn)
	 */
	public LogPickupReportColumn mergeLogPickupReportColumn(LogPickupReportColumn logPickupReportColumn);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportColumnSessionEJBRemote#mergeLogPickupReportColumnList(
	 * java.util.List)
	 */
	public ArrayList<LogPickupReportColumn> mergeLogPickupReportColumnList(
			List<LogPickupReportColumn> logPickupReportColumnList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportColumnSessionEJBRemote#removeLogPickupReportColumn(com
	 * .gdn.venice.persistence.LogPickupReportColumn)
	 */
	public void removeLogPickupReportColumn(LogPickupReportColumn logPickupReportColumn);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportColumnSessionEJBRemote#removeLogPickupReportColumnList
	 * (java.util.List)
	 */
	public void removeLogPickupReportColumnList(List<LogPickupReportColumn> logPickupReportColumnList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportColumnSessionEJBRemote#findByLogPickupReportColumnLike
	 * (com.gdn.venice.persistence.LogPickupReportColumn, int, int)
	 */
	public List<LogPickupReportColumn> findByLogPickupReportColumnLike(LogPickupReportColumn logPickupReportColumn,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupReportColumnSessionEJBRemote#findByLogPickupReportColumnLikeFR
	 * (com.gdn.venice.persistence.LogPickupReportColumn, int, int)
	 */
	public FinderReturn findByLogPickupReportColumnLikeFR(LogPickupReportColumn logPickupReportColumn,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
