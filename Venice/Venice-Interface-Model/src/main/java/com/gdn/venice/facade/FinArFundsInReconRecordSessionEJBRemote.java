package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinArFundsInReconRecordSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinArFundsInReconRecord
	 */
	public List<FinArFundsInReconRecord> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinArFundsInReconRecord persists a country
	 * 
	 * @param finArFundsInReconRecord
	 * @return the persisted FinArFundsInReconRecord
	 */
	public FinArFundsInReconRecord persistFinArFundsInReconRecord(FinArFundsInReconRecord finArFundsInReconRecord);

	/**
	 * persistFinArFundsInReconRecordList - persists a list of FinArFundsInReconRecord
	 * 
	 * @param finArFundsInReconRecordList
	 * @return the list of persisted FinArFundsInReconRecord
	 */
	public ArrayList<FinArFundsInReconRecord> persistFinArFundsInReconRecordList(
			List<FinArFundsInReconRecord> finArFundsInReconRecordList);

	/**
	 * mergeFinArFundsInReconRecord - merges a FinArFundsInReconRecord
	 * 
	 * @param finArFundsInReconRecord
	 * @return the merged FinArFundsInReconRecord
	 */
	public FinArFundsInReconRecord mergeFinArFundsInReconRecord(FinArFundsInReconRecord finArFundsInReconRecord);

	/**
	 * mergeFinArFundsInReconRecordList - merges a list of FinArFundsInReconRecord
	 * 
	 * @param finArFundsInReconRecordList
	 * @return the merged list of FinArFundsInReconRecord
	 */
	public ArrayList<FinArFundsInReconRecord> mergeFinArFundsInReconRecordList(
			List<FinArFundsInReconRecord> finArFundsInReconRecordList);

	/**
	 * removeFinArFundsInReconRecord - removes a FinArFundsInReconRecord
	 * 
	 * @param finArFundsInReconRecord
	 */
	public void removeFinArFundsInReconRecord(FinArFundsInReconRecord finArFundsInReconRecord);

	/**
	 * removeFinArFundsInReconRecordList - removes a list of FinArFundsInReconRecord
	 * 
	 * @param finArFundsInReconRecordList
	 */
	public void removeFinArFundsInReconRecordList(List<FinArFundsInReconRecord> finArFundsInReconRecordList);

	/**
	 * findByFinArFundsInReconRecordLike - finds a list of FinArFundsInReconRecord Like
	 * 
	 * @param finArFundsInReconRecord
	 * @return the list of FinArFundsInReconRecord found
	 */
	public List<FinArFundsInReconRecord> findByFinArFundsInReconRecordLike(FinArFundsInReconRecord finArFundsInReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinArFundsInReconRecord>LikeFR - finds a list of FinArFundsInReconRecord> Like with a finder return object
	 * 
	 * @param finArFundsInReconRecord
	 * @return the list of FinArFundsInReconRecord found
	 */
	public FinderReturn findByFinArFundsInReconRecordLikeFR(FinArFundsInReconRecord finArFundsInReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
