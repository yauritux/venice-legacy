package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogReconInvoiceRecordResult;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogReconInvoiceRecordResultSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconInvoiceRecordResultSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogReconInvoiceRecordResult> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconInvoiceRecordResultSessionEJBRemote#persistLogReconInvoiceRecordResult(com
	 * .gdn.venice.persistence.LogReconInvoiceRecordResult)
	 */
	public LogReconInvoiceRecordResult persistLogReconInvoiceRecordResult(LogReconInvoiceRecordResult logReconInvoiceRecordResult);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconInvoiceRecordResultSessionEJBRemote#persistLogReconInvoiceRecordResultList
	 * (java.util.List)
	 */
	public ArrayList<LogReconInvoiceRecordResult> persistLogReconInvoiceRecordResultList(
			List<LogReconInvoiceRecordResult> logReconInvoiceRecordResultList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconInvoiceRecordResultSessionEJBRemote#mergeLogReconInvoiceRecordResult(com.
	 * gdn.venice.persistence.LogReconInvoiceRecordResult)
	 */
	public LogReconInvoiceRecordResult mergeLogReconInvoiceRecordResult(LogReconInvoiceRecordResult logReconInvoiceRecordResult);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconInvoiceRecordResultSessionEJBRemote#mergeLogReconInvoiceRecordResultList(
	 * java.util.List)
	 */
	public ArrayList<LogReconInvoiceRecordResult> mergeLogReconInvoiceRecordResultList(
			List<LogReconInvoiceRecordResult> logReconInvoiceRecordResultList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconInvoiceRecordResultSessionEJBRemote#removeLogReconInvoiceRecordResult(com
	 * .gdn.venice.persistence.LogReconInvoiceRecordResult)
	 */
	public void removeLogReconInvoiceRecordResult(LogReconInvoiceRecordResult logReconInvoiceRecordResult);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconInvoiceRecordResultSessionEJBRemote#removeLogReconInvoiceRecordResultList
	 * (java.util.List)
	 */
	public void removeLogReconInvoiceRecordResultList(List<LogReconInvoiceRecordResult> logReconInvoiceRecordResultList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconInvoiceRecordResultSessionEJBRemote#findByLogReconInvoiceRecordResultLike
	 * (com.gdn.venice.persistence.LogReconInvoiceRecordResult, int, int)
	 */
	public List<LogReconInvoiceRecordResult> findByLogReconInvoiceRecordResultLike(LogReconInvoiceRecordResult logReconInvoiceRecordResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconInvoiceRecordResultSessionEJBRemote#findByLogReconInvoiceRecordResultLikeFR
	 * (com.gdn.venice.persistence.LogReconInvoiceRecordResult, int, int)
	 */
	public FinderReturn findByLogReconInvoiceRecordResultLikeFR(LogReconInvoiceRecordResult logReconInvoiceRecordResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
