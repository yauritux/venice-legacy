package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogActivityReconRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogActivityReconRecordSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogActivityReconRecord> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#persistLogActivityReconRecord(com
	 * .gdn.venice.persistence.LogActivityReconRecord)
	 */
	public LogActivityReconRecord persistLogActivityReconRecord(LogActivityReconRecord logActivityReconRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#persistLogActivityReconRecordList
	 * (java.util.List)
	 */
	public ArrayList<LogActivityReconRecord> persistLogActivityReconRecordList(
			List<LogActivityReconRecord> logActivityReconRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#mergeLogActivityReconRecord(com.
	 * gdn.venice.persistence.LogActivityReconRecord)
	 */
	public LogActivityReconRecord mergeLogActivityReconRecord(LogActivityReconRecord logActivityReconRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#mergeLogActivityReconRecordList(
	 * java.util.List)
	 */
	public ArrayList<LogActivityReconRecord> mergeLogActivityReconRecordList(
			List<LogActivityReconRecord> logActivityReconRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#removeLogActivityReconRecord(com
	 * .gdn.venice.persistence.LogActivityReconRecord)
	 */
	public void removeLogActivityReconRecord(LogActivityReconRecord logActivityReconRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#removeLogActivityReconRecordList
	 * (java.util.List)
	 */
	public void removeLogActivityReconRecordList(List<LogActivityReconRecord> logActivityReconRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#findByLogActivityReconRecordLike
	 * (com.gdn.venice.persistence.LogActivityReconRecord, int, int)
	 */
	public List<LogActivityReconRecord> findByLogActivityReconRecordLike(LogActivityReconRecord logActivityReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActivityReconRecordSessionEJBRemote#findByLogActivityReconRecordLikeFR
	 * (com.gdn.venice.persistence.LogActivityReconRecord, int, int)
	 */
	public FinderReturn findByLogActivityReconRecordLikeFR(LogActivityReconRecord logActivityReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
