package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogProviderPickupOrderReport;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogProviderPickupOrderReportSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogProviderPickupOrderReport> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#persistLogProviderPickupOrderReport(com
	 * .gdn.venice.persistence.LogProviderPickupOrderReport)
	 */
	public LogProviderPickupOrderReport persistLogProviderPickupOrderReport(LogProviderPickupOrderReport logProviderPickupOrderReport);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#persistLogProviderPickupOrderReportList
	 * (java.util.List)
	 */
	public ArrayList<LogProviderPickupOrderReport> persistLogProviderPickupOrderReportList(
			List<LogProviderPickupOrderReport> logProviderPickupOrderReportList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#mergeLogProviderPickupOrderReport(com.
	 * gdn.venice.persistence.LogProviderPickupOrderReport)
	 */
	public LogProviderPickupOrderReport mergeLogProviderPickupOrderReport(LogProviderPickupOrderReport logProviderPickupOrderReport);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#mergeLogProviderPickupOrderReportList(
	 * java.util.List)
	 */
	public ArrayList<LogProviderPickupOrderReport> mergeLogProviderPickupOrderReportList(
			List<LogProviderPickupOrderReport> logProviderPickupOrderReportList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#removeLogProviderPickupOrderReport(com
	 * .gdn.venice.persistence.LogProviderPickupOrderReport)
	 */
	public void removeLogProviderPickupOrderReport(LogProviderPickupOrderReport logProviderPickupOrderReport);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#removeLogProviderPickupOrderReportList
	 * (java.util.List)
	 */
	public void removeLogProviderPickupOrderReportList(List<LogProviderPickupOrderReport> logProviderPickupOrderReportList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#findByLogProviderPickupOrderReportLike
	 * (com.gdn.venice.persistence.LogProviderPickupOrderReport, int, int)
	 */
	public List<LogProviderPickupOrderReport> findByLogProviderPickupOrderReportLike(LogProviderPickupOrderReport logProviderPickupOrderReport,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogProviderPickupOrderReportSessionEJBRemote#findByLogProviderPickupOrderReportLikeFR
	 * (com.gdn.venice.persistence.LogProviderPickupOrderReport, int, int)
	 */
	public FinderReturn findByLogProviderPickupOrderReportLikeFR(LogProviderPickupOrderReport logProviderPickupOrderReport,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
