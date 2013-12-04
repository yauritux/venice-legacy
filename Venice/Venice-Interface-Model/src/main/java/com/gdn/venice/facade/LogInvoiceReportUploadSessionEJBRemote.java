package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogInvoiceReportUploadSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogInvoiceReportUpload
	 */
	public List<LogInvoiceReportUpload> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogInvoiceReportUpload persists a country
	 * 
	 * @param logInvoiceReportUpload
	 * @return the persisted LogInvoiceReportUpload
	 */
	public LogInvoiceReportUpload persistLogInvoiceReportUpload(LogInvoiceReportUpload logInvoiceReportUpload);

	/**
	 * persistLogInvoiceReportUploadList - persists a list of LogInvoiceReportUpload
	 * 
	 * @param logInvoiceReportUploadList
	 * @return the list of persisted LogInvoiceReportUpload
	 */
	public ArrayList<LogInvoiceReportUpload> persistLogInvoiceReportUploadList(
			List<LogInvoiceReportUpload> logInvoiceReportUploadList);

	/**
	 * mergeLogInvoiceReportUpload - merges a LogInvoiceReportUpload
	 * 
	 * @param logInvoiceReportUpload
	 * @return the merged LogInvoiceReportUpload
	 */
	public LogInvoiceReportUpload mergeLogInvoiceReportUpload(LogInvoiceReportUpload logInvoiceReportUpload);

	/**
	 * mergeLogInvoiceReportUploadList - merges a list of LogInvoiceReportUpload
	 * 
	 * @param logInvoiceReportUploadList
	 * @return the merged list of LogInvoiceReportUpload
	 */
	public ArrayList<LogInvoiceReportUpload> mergeLogInvoiceReportUploadList(
			List<LogInvoiceReportUpload> logInvoiceReportUploadList);

	/**
	 * removeLogInvoiceReportUpload - removes a LogInvoiceReportUpload
	 * 
	 * @param logInvoiceReportUpload
	 */
	public void removeLogInvoiceReportUpload(LogInvoiceReportUpload logInvoiceReportUpload);

	/**
	 * removeLogInvoiceReportUploadList - removes a list of LogInvoiceReportUpload
	 * 
	 * @param logInvoiceReportUploadList
	 */
	public void removeLogInvoiceReportUploadList(List<LogInvoiceReportUpload> logInvoiceReportUploadList);

	/**
	 * findByLogInvoiceReportUploadLike - finds a list of LogInvoiceReportUpload Like
	 * 
	 * @param logInvoiceReportUpload
	 * @return the list of LogInvoiceReportUpload found
	 */
	public List<LogInvoiceReportUpload> findByLogInvoiceReportUploadLike(LogInvoiceReportUpload logInvoiceReportUpload,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogInvoiceReportUpload>LikeFR - finds a list of LogInvoiceReportUpload> Like with a finder return object
	 * 
	 * @param logInvoiceReportUpload
	 * @return the list of LogInvoiceReportUpload found
	 */
	public FinderReturn findByLogInvoiceReportUploadLikeFR(LogInvoiceReportUpload logInvoiceReportUpload,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
