package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;
import com.gdn.venice.persistence.LogInvoiceUploadLog;

@Remote
public interface LogInvoiceUploadLogSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogInvoiceUploadLog
	 */
	public List<LogInvoiceUploadLog> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogInvoiceUploadLog persists a country
	 * 
	 * @param logInvoiceUploadLog
	 * @return the persisted LogInvoiceUploadLog
	 */
	public LogInvoiceUploadLog persistLogInvoiceUploadLog(LogInvoiceUploadLog logInvoiceUploadLog);

	/**
	 * persistLogInvoiceUploadLogList - persists a list of LogInvoiceUploadLog
	 * 
	 * @param logInvoiceUploadLogList
	 * @return the list of persisted LogInvoiceUploadLog
	 */
	public ArrayList<LogInvoiceUploadLog> persistLogInvoiceUploadLogList(
			List<LogInvoiceUploadLog> logInvoiceUploadLogList);

	/**
	 * mergeLogInvoiceUploadLog - merges a LogInvoiceUploadLog
	 * 
	 * @param logInvoiceUploadLog
	 * @return the merged LogInvoiceUploadLog
	 */
	public LogInvoiceUploadLog mergeLogInvoiceUploadLog(LogInvoiceUploadLog logInvoiceUploadLog);

	/**
	 * mergeLogInvoiceUploadLogList - merges a list of LogInvoiceUploadLog
	 * 
	 * @param logInvoiceUploadLogList
	 * @return the merged list of LogInvoiceUploadLog
	 */
	public ArrayList<LogInvoiceUploadLog> mergeLogInvoiceUploadLogList(
			List<LogInvoiceUploadLog> logInvoiceUploadLogList);

	/**
	 * removeLogInvoiceUploadLog - removes a LogInvoiceUploadLog
	 * 
	 * @param logInvoiceUploadLog
	 */
	public void removeLogInvoiceUploadLog(LogInvoiceUploadLog logInvoiceUploadLog);

	/**
	 * removeLogInvoiceUploadLogList - removes a list of LogInvoiceUploadLog
	 * 
	 * @param logInvoiceUploadLogList
	 */
	public void removeLogInvoiceUploadLogList(List<LogInvoiceUploadLog> logInvoiceUploadLogList);

	/**
	 * findByLogInvoiceUploadLogLike - finds a list of LogInvoiceUploadLog Like
	 * 
	 * @param logInvoiceUploadLog
	 * @return the list of LogInvoiceUploadLog found
	 */
	public List<LogInvoiceUploadLog> findByLogInvoiceUploadLogLike(LogInvoiceUploadLog logInvoiceUploadLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogInvoiceUploadLog>LikeFR - finds a list of LogInvoiceUploadLog> Like with a finder return object
	 * 
	 * @param logInvoiceUploadLog
	 * @return the list of LogInvoiceUploadLog found
	 */
	public FinderReturn findByLogInvoiceUploadLogLikeFR(LogInvoiceUploadLog logInvoiceUploadLog,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
