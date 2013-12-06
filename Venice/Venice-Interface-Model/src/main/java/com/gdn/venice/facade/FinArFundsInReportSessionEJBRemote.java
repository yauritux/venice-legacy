package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArFundsInReport;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinArFundsInReportSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinArFundsInReport
	 */
	public List<FinArFundsInReport> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinArFundsInReport persists a country
	 * 
	 * @param finArFundsInReport
	 * @return the persisted FinArFundsInReport
	 */
	public FinArFundsInReport persistFinArFundsInReport(FinArFundsInReport finArFundsInReport);

	/**
	 * persistFinArFundsInReportList - persists a list of FinArFundsInReport
	 * 
	 * @param finArFundsInReportList
	 * @return the list of persisted FinArFundsInReport
	 */
	public ArrayList<FinArFundsInReport> persistFinArFundsInReportList(
			List<FinArFundsInReport> finArFundsInReportList);

	/**
	 * mergeFinArFundsInReport - merges a FinArFundsInReport
	 * 
	 * @param finArFundsInReport
	 * @return the merged FinArFundsInReport
	 */
	public FinArFundsInReport mergeFinArFundsInReport(FinArFundsInReport finArFundsInReport);

	/**
	 * mergeFinArFundsInReportList - merges a list of FinArFundsInReport
	 * 
	 * @param finArFundsInReportList
	 * @return the merged list of FinArFundsInReport
	 */
	public ArrayList<FinArFundsInReport> mergeFinArFundsInReportList(
			List<FinArFundsInReport> finArFundsInReportList);

	/**
	 * removeFinArFundsInReport - removes a FinArFundsInReport
	 * 
	 * @param finArFundsInReport
	 */
	public void removeFinArFundsInReport(FinArFundsInReport finArFundsInReport);

	/**
	 * removeFinArFundsInReportList - removes a list of FinArFundsInReport
	 * 
	 * @param finArFundsInReportList
	 */
	public void removeFinArFundsInReportList(List<FinArFundsInReport> finArFundsInReportList);

	/**
	 * findByFinArFundsInReportLike - finds a list of FinArFundsInReport Like
	 * 
	 * @param finArFundsInReport
	 * @return the list of FinArFundsInReport found
	 */
	public List<FinArFundsInReport> findByFinArFundsInReportLike(FinArFundsInReport finArFundsInReport,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinArFundsInReport>LikeFR - finds a list of FinArFundsInReport> Like with a finder return object
	 * 
	 * @param finArFundsInReport
	 * @return the list of FinArFundsInReport found
	 */
	public FinderReturn findByFinArFundsInReportLikeFR(FinArFundsInReport finArFundsInReport,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
