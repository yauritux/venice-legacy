package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogInvoiceAirwaybillRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogInvoiceAirwaybillRecordSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogInvoiceAirwaybillRecord
	 */
	public List<LogInvoiceAirwaybillRecord> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogInvoiceAirwaybillRecord persists a country
	 * 
	 * @param logInvoiceAirwaybillRecord
	 * @return the persisted LogInvoiceAirwaybillRecord
	 */
	public LogInvoiceAirwaybillRecord persistLogInvoiceAirwaybillRecord(LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord);

	/**
	 * persistLogInvoiceAirwaybillRecordList - persists a list of LogInvoiceAirwaybillRecord
	 * 
	 * @param logInvoiceAirwaybillRecordList
	 * @return the list of persisted LogInvoiceAirwaybillRecord
	 */
	public ArrayList<LogInvoiceAirwaybillRecord> persistLogInvoiceAirwaybillRecordList(
			List<LogInvoiceAirwaybillRecord> logInvoiceAirwaybillRecordList);

	/**
	 * mergeLogInvoiceAirwaybillRecord - merges a LogInvoiceAirwaybillRecord
	 * 
	 * @param logInvoiceAirwaybillRecord
	 * @return the merged LogInvoiceAirwaybillRecord
	 */
	public LogInvoiceAirwaybillRecord mergeLogInvoiceAirwaybillRecord(LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord);

	/**
	 * mergeLogInvoiceAirwaybillRecordList - merges a list of LogInvoiceAirwaybillRecord
	 * 
	 * @param logInvoiceAirwaybillRecordList
	 * @return the merged list of LogInvoiceAirwaybillRecord
	 */
	public ArrayList<LogInvoiceAirwaybillRecord> mergeLogInvoiceAirwaybillRecordList(
			List<LogInvoiceAirwaybillRecord> logInvoiceAirwaybillRecordList);

	/**
	 * removeLogInvoiceAirwaybillRecord - removes a LogInvoiceAirwaybillRecord
	 * 
	 * @param logInvoiceAirwaybillRecord
	 */
	public void removeLogInvoiceAirwaybillRecord(LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord);

	/**
	 * removeLogInvoiceAirwaybillRecordList - removes a list of LogInvoiceAirwaybillRecord
	 * 
	 * @param logInvoiceAirwaybillRecordList
	 */
	public void removeLogInvoiceAirwaybillRecordList(List<LogInvoiceAirwaybillRecord> logInvoiceAirwaybillRecordList);

	/**
	 * findByLogInvoiceAirwaybillRecordLike - finds a list of LogInvoiceAirwaybillRecord Like
	 * 
	 * @param logInvoiceAirwaybillRecord
	 * @return the list of LogInvoiceAirwaybillRecord found
	 */
	public List<LogInvoiceAirwaybillRecord> findByLogInvoiceAirwaybillRecordLike(LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogInvoiceAirwaybillRecord>LikeFR - finds a list of LogInvoiceAirwaybillRecord> Like with a finder return object
	 * 
	 * @param logInvoiceAirwaybillRecord
	 * @return the list of LogInvoiceAirwaybillRecord found
	 */
	public FinderReturn findByLogInvoiceAirwaybillRecordLikeFR(LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
