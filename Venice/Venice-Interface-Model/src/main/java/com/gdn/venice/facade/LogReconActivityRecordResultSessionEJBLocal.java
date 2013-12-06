package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogReconActivityRecordResult;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogReconActivityRecordResultSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconActivityRecordResultSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogReconActivityRecordResult> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconActivityRecordResultSessionEJBRemote#persistLogReconActivityRecordResult(com
	 * .gdn.venice.persistence.LogReconActivityRecordResult)
	 */
	public LogReconActivityRecordResult persistLogReconActivityRecordResult(LogReconActivityRecordResult logReconActivityRecordResult);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconActivityRecordResultSessionEJBRemote#persistLogReconActivityRecordResultList
	 * (java.util.List)
	 */
	public ArrayList<LogReconActivityRecordResult> persistLogReconActivityRecordResultList(
			List<LogReconActivityRecordResult> logReconActivityRecordResultList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconActivityRecordResultSessionEJBRemote#mergeLogReconActivityRecordResult(com.
	 * gdn.venice.persistence.LogReconActivityRecordResult)
	 */
	public LogReconActivityRecordResult mergeLogReconActivityRecordResult(LogReconActivityRecordResult logReconActivityRecordResult);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconActivityRecordResultSessionEJBRemote#mergeLogReconActivityRecordResultList(
	 * java.util.List)
	 */
	public ArrayList<LogReconActivityRecordResult> mergeLogReconActivityRecordResultList(
			List<LogReconActivityRecordResult> logReconActivityRecordResultList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconActivityRecordResultSessionEJBRemote#removeLogReconActivityRecordResult(com
	 * .gdn.venice.persistence.LogReconActivityRecordResult)
	 */
	public void removeLogReconActivityRecordResult(LogReconActivityRecordResult logReconActivityRecordResult);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconActivityRecordResultSessionEJBRemote#removeLogReconActivityRecordResultList
	 * (java.util.List)
	 */
	public void removeLogReconActivityRecordResultList(List<LogReconActivityRecordResult> logReconActivityRecordResultList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconActivityRecordResultSessionEJBRemote#findByLogReconActivityRecordResultLike
	 * (com.gdn.venice.persistence.LogReconActivityRecordResult, int, int)
	 */
	public List<LogReconActivityRecordResult> findByLogReconActivityRecordResultLike(LogReconActivityRecordResult logReconActivityRecordResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogReconActivityRecordResultSessionEJBRemote#findByLogReconActivityRecordResultLikeFR
	 * (com.gdn.venice.persistence.LogReconActivityRecordResult, int, int)
	 */
	public FinderReturn findByLogReconActivityRecordResultLikeFR(LogReconActivityRecordResult logReconActivityRecordResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
