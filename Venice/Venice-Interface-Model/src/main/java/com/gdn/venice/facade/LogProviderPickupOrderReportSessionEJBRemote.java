package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogProviderPickupOrderReport;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogProviderPickupOrderReportSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogProviderPickupOrderReport
	 */
	public List<LogProviderPickupOrderReport> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogProviderPickupOrderReport persists a country
	 * 
	 * @param logProviderPickupOrderReport
	 * @return the persisted LogProviderPickupOrderReport
	 */
	public LogProviderPickupOrderReport persistLogProviderPickupOrderReport(LogProviderPickupOrderReport logProviderPickupOrderReport);

	/**
	 * persistLogProviderPickupOrderReportList - persists a list of LogProviderPickupOrderReport
	 * 
	 * @param logProviderPickupOrderReportList
	 * @return the list of persisted LogProviderPickupOrderReport
	 */
	public ArrayList<LogProviderPickupOrderReport> persistLogProviderPickupOrderReportList(
			List<LogProviderPickupOrderReport> logProviderPickupOrderReportList);

	/**
	 * mergeLogProviderPickupOrderReport - merges a LogProviderPickupOrderReport
	 * 
	 * @param logProviderPickupOrderReport
	 * @return the merged LogProviderPickupOrderReport
	 */
	public LogProviderPickupOrderReport mergeLogProviderPickupOrderReport(LogProviderPickupOrderReport logProviderPickupOrderReport);

	/**
	 * mergeLogProviderPickupOrderReportList - merges a list of LogProviderPickupOrderReport
	 * 
	 * @param logProviderPickupOrderReportList
	 * @return the merged list of LogProviderPickupOrderReport
	 */
	public ArrayList<LogProviderPickupOrderReport> mergeLogProviderPickupOrderReportList(
			List<LogProviderPickupOrderReport> logProviderPickupOrderReportList);

	/**
	 * removeLogProviderPickupOrderReport - removes a LogProviderPickupOrderReport
	 * 
	 * @param logProviderPickupOrderReport
	 */
	public void removeLogProviderPickupOrderReport(LogProviderPickupOrderReport logProviderPickupOrderReport);

	/**
	 * removeLogProviderPickupOrderReportList - removes a list of LogProviderPickupOrderReport
	 * 
	 * @param logProviderPickupOrderReportList
	 */
	public void removeLogProviderPickupOrderReportList(List<LogProviderPickupOrderReport> logProviderPickupOrderReportList);

	/**
	 * findByLogProviderPickupOrderReportLike - finds a list of LogProviderPickupOrderReport Like
	 * 
	 * @param logProviderPickupOrderReport
	 * @return the list of LogProviderPickupOrderReport found
	 */
	public List<LogProviderPickupOrderReport> findByLogProviderPickupOrderReportLike(LogProviderPickupOrderReport logProviderPickupOrderReport,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogProviderPickupOrderReport>LikeFR - finds a list of LogProviderPickupOrderReport> Like with a finder return object
	 * 
	 * @param logProviderPickupOrderReport
	 * @return the list of LogProviderPickupOrderReport found
	 */
	public FinderReturn findByLogProviderPickupOrderReportLikeFR(LogProviderPickupOrderReport logProviderPickupOrderReport,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
