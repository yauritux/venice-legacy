package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogReconInvoiceRecordResult;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogReconInvoiceRecordResultSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogReconInvoiceRecordResult
	 */
	public List<LogReconInvoiceRecordResult> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogReconInvoiceRecordResult persists a country
	 * 
	 * @param logReconInvoiceRecordResult
	 * @return the persisted LogReconInvoiceRecordResult
	 */
	public LogReconInvoiceRecordResult persistLogReconInvoiceRecordResult(LogReconInvoiceRecordResult logReconInvoiceRecordResult);

	/**
	 * persistLogReconInvoiceRecordResultList - persists a list of LogReconInvoiceRecordResult
	 * 
	 * @param logReconInvoiceRecordResultList
	 * @return the list of persisted LogReconInvoiceRecordResult
	 */
	public ArrayList<LogReconInvoiceRecordResult> persistLogReconInvoiceRecordResultList(
			List<LogReconInvoiceRecordResult> logReconInvoiceRecordResultList);

	/**
	 * mergeLogReconInvoiceRecordResult - merges a LogReconInvoiceRecordResult
	 * 
	 * @param logReconInvoiceRecordResult
	 * @return the merged LogReconInvoiceRecordResult
	 */
	public LogReconInvoiceRecordResult mergeLogReconInvoiceRecordResult(LogReconInvoiceRecordResult logReconInvoiceRecordResult);

	/**
	 * mergeLogReconInvoiceRecordResultList - merges a list of LogReconInvoiceRecordResult
	 * 
	 * @param logReconInvoiceRecordResultList
	 * @return the merged list of LogReconInvoiceRecordResult
	 */
	public ArrayList<LogReconInvoiceRecordResult> mergeLogReconInvoiceRecordResultList(
			List<LogReconInvoiceRecordResult> logReconInvoiceRecordResultList);

	/**
	 * removeLogReconInvoiceRecordResult - removes a LogReconInvoiceRecordResult
	 * 
	 * @param logReconInvoiceRecordResult
	 */
	public void removeLogReconInvoiceRecordResult(LogReconInvoiceRecordResult logReconInvoiceRecordResult);

	/**
	 * removeLogReconInvoiceRecordResultList - removes a list of LogReconInvoiceRecordResult
	 * 
	 * @param logReconInvoiceRecordResultList
	 */
	public void removeLogReconInvoiceRecordResultList(List<LogReconInvoiceRecordResult> logReconInvoiceRecordResultList);

	/**
	 * findByLogReconInvoiceRecordResultLike - finds a list of LogReconInvoiceRecordResult Like
	 * 
	 * @param logReconInvoiceRecordResult
	 * @return the list of LogReconInvoiceRecordResult found
	 */
	public List<LogReconInvoiceRecordResult> findByLogReconInvoiceRecordResultLike(LogReconInvoiceRecordResult logReconInvoiceRecordResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogReconInvoiceRecordResult>LikeFR - finds a list of LogReconInvoiceRecordResult> Like with a finder return object
	 * 
	 * @param logReconInvoiceRecordResult
	 * @return the list of LogReconInvoiceRecordResult found
	 */
	public FinderReturn findByLogReconInvoiceRecordResultLikeFR(LogReconInvoiceRecordResult logReconInvoiceRecordResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
