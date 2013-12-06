package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogInvoiceReconRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogInvoiceReconRecordSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogInvoiceReconRecord
	 */
	public List<LogInvoiceReconRecord> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogInvoiceReconRecord persists a country
	 * 
	 * @param logInvoiceReconRecord
	 * @return the persisted LogInvoiceReconRecord
	 */
	public LogInvoiceReconRecord persistLogInvoiceReconRecord(LogInvoiceReconRecord logInvoiceReconRecord);

	/**
	 * persistLogInvoiceReconRecordList - persists a list of LogInvoiceReconRecord
	 * 
	 * @param logInvoiceReconRecordList
	 * @return the list of persisted LogInvoiceReconRecord
	 */
	public ArrayList<LogInvoiceReconRecord> persistLogInvoiceReconRecordList(
			List<LogInvoiceReconRecord> logInvoiceReconRecordList);

	/**
	 * mergeLogInvoiceReconRecord - merges a LogInvoiceReconRecord
	 * 
	 * @param logInvoiceReconRecord
	 * @return the merged LogInvoiceReconRecord
	 */
	public LogInvoiceReconRecord mergeLogInvoiceReconRecord(LogInvoiceReconRecord logInvoiceReconRecord);

	/**
	 * mergeLogInvoiceReconRecordList - merges a list of LogInvoiceReconRecord
	 * 
	 * @param logInvoiceReconRecordList
	 * @return the merged list of LogInvoiceReconRecord
	 */
	public ArrayList<LogInvoiceReconRecord> mergeLogInvoiceReconRecordList(
			List<LogInvoiceReconRecord> logInvoiceReconRecordList);

	/**
	 * removeLogInvoiceReconRecord - removes a LogInvoiceReconRecord
	 * 
	 * @param logInvoiceReconRecord
	 */
	public void removeLogInvoiceReconRecord(LogInvoiceReconRecord logInvoiceReconRecord);

	/**
	 * removeLogInvoiceReconRecordList - removes a list of LogInvoiceReconRecord
	 * 
	 * @param logInvoiceReconRecordList
	 */
	public void removeLogInvoiceReconRecordList(List<LogInvoiceReconRecord> logInvoiceReconRecordList);

	/**
	 * findByLogInvoiceReconRecordLike - finds a list of LogInvoiceReconRecord Like
	 * 
	 * @param logInvoiceReconRecord
	 * @return the list of LogInvoiceReconRecord found
	 */
	public List<LogInvoiceReconRecord> findByLogInvoiceReconRecordLike(LogInvoiceReconRecord logInvoiceReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogInvoiceReconRecord>LikeFR - finds a list of LogInvoiceReconRecord> Like with a finder return object
	 * 
	 * @param logInvoiceReconRecord
	 * @return the list of LogInvoiceReconRecord found
	 */
	public FinderReturn findByLogInvoiceReconRecordLikeFR(LogInvoiceReconRecord logInvoiceReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
