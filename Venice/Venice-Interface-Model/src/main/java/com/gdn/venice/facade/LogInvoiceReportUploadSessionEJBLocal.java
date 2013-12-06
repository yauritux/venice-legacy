package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogInvoiceReportUpload;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogInvoiceReportUploadSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogInvoiceReportUpload> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#persistLogInvoiceReportUpload(com
	 * .gdn.venice.persistence.LogInvoiceReportUpload)
	 */
	public LogInvoiceReportUpload persistLogInvoiceReportUpload(LogInvoiceReportUpload logInvoiceReportUpload);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#persistLogInvoiceReportUploadList
	 * (java.util.List)
	 */
	public ArrayList<LogInvoiceReportUpload> persistLogInvoiceReportUploadList(
			List<LogInvoiceReportUpload> logInvoiceReportUploadList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#mergeLogInvoiceReportUpload(com.
	 * gdn.venice.persistence.LogInvoiceReportUpload)
	 */
	public LogInvoiceReportUpload mergeLogInvoiceReportUpload(LogInvoiceReportUpload logInvoiceReportUpload);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#mergeLogInvoiceReportUploadList(
	 * java.util.List)
	 */
	public ArrayList<LogInvoiceReportUpload> mergeLogInvoiceReportUploadList(
			List<LogInvoiceReportUpload> logInvoiceReportUploadList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#removeLogInvoiceReportUpload(com
	 * .gdn.venice.persistence.LogInvoiceReportUpload)
	 */
	public void removeLogInvoiceReportUpload(LogInvoiceReportUpload logInvoiceReportUpload);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#removeLogInvoiceReportUploadList
	 * (java.util.List)
	 */
	public void removeLogInvoiceReportUploadList(List<LogInvoiceReportUpload> logInvoiceReportUploadList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#findByLogInvoiceReportUploadLike
	 * (com.gdn.venice.persistence.LogInvoiceReportUpload, int, int)
	 */
	public List<LogInvoiceReportUpload> findByLogInvoiceReportUploadLike(LogInvoiceReportUpload logInvoiceReportUpload,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReportUploadSessionEJBRemote#findByLogInvoiceReportUploadLikeFR
	 * (com.gdn.venice.persistence.LogInvoiceReportUpload, int, int)
	 */
	public FinderReturn findByLogInvoiceReportUploadLikeFR(LogInvoiceReportUpload logInvoiceReportUpload,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
