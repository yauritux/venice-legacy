package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogActivityReconRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogActivityReconRecordSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogActivityReconRecord
	 */
	public List<LogActivityReconRecord> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogActivityReconRecord persists a country
	 * 
	 * @param logActivityReconRecord
	 * @return the persisted LogActivityReconRecord
	 */
	public LogActivityReconRecord persistLogActivityReconRecord(LogActivityReconRecord logActivityReconRecord);

	/**
	 * persistLogActivityReconRecordList - persists a list of LogActivityReconRecord
	 * 
	 * @param logActivityReconRecordList
	 * @return the list of persisted LogActivityReconRecord
	 */
	public ArrayList<LogActivityReconRecord> persistLogActivityReconRecordList(
			List<LogActivityReconRecord> logActivityReconRecordList);

	/**
	 * mergeLogActivityReconRecord - merges a LogActivityReconRecord
	 * 
	 * @param logActivityReconRecord
	 * @return the merged LogActivityReconRecord
	 */
	public LogActivityReconRecord mergeLogActivityReconRecord(LogActivityReconRecord logActivityReconRecord);

	/**
	 * mergeLogActivityReconRecordList - merges a list of LogActivityReconRecord
	 * 
	 * @param logActivityReconRecordList
	 * @return the merged list of LogActivityReconRecord
	 */
	public ArrayList<LogActivityReconRecord> mergeLogActivityReconRecordList(
			List<LogActivityReconRecord> logActivityReconRecordList);

	/**
	 * removeLogActivityReconRecord - removes a LogActivityReconRecord
	 * 
	 * @param logActivityReconRecord
	 */
	public void removeLogActivityReconRecord(LogActivityReconRecord logActivityReconRecord);

	/**
	 * removeLogActivityReconRecordList - removes a list of LogActivityReconRecord
	 * 
	 * @param logActivityReconRecordList
	 */
	public void removeLogActivityReconRecordList(List<LogActivityReconRecord> logActivityReconRecordList);

	/**
	 * findByLogActivityReconRecordLike - finds a list of LogActivityReconRecord Like
	 * 
	 * @param logActivityReconRecord
	 * @return the list of LogActivityReconRecord found
	 */
	public List<LogActivityReconRecord> findByLogActivityReconRecordLike(LogActivityReconRecord logActivityReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogActivityReconRecord>LikeFR - finds a list of LogActivityReconRecord> Like with a finder return object
	 * 
	 * @param logActivityReconRecord
	 * @return the list of LogActivityReconRecord found
	 */
	public FinderReturn findByLogActivityReconRecordLikeFR(LogActivityReconRecord logActivityReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
