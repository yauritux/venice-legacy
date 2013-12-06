package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogInvoiceReconRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogInvoiceReconRecordSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogInvoiceReconRecord> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote#persistLogInvoiceReconRecord(com
	 * .gdn.venice.persistence.LogInvoiceReconRecord)
	 */
	public LogInvoiceReconRecord persistLogInvoiceReconRecord(LogInvoiceReconRecord logInvoiceReconRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote#persistLogInvoiceReconRecordList
	 * (java.util.List)
	 */
	public ArrayList<LogInvoiceReconRecord> persistLogInvoiceReconRecordList(
			List<LogInvoiceReconRecord> logInvoiceReconRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote#mergeLogInvoiceReconRecord(com.
	 * gdn.venice.persistence.LogInvoiceReconRecord)
	 */
	public LogInvoiceReconRecord mergeLogInvoiceReconRecord(LogInvoiceReconRecord logInvoiceReconRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote#mergeLogInvoiceReconRecordList(
	 * java.util.List)
	 */
	public ArrayList<LogInvoiceReconRecord> mergeLogInvoiceReconRecordList(
			List<LogInvoiceReconRecord> logInvoiceReconRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote#removeLogInvoiceReconRecord(com
	 * .gdn.venice.persistence.LogInvoiceReconRecord)
	 */
	public void removeLogInvoiceReconRecord(LogInvoiceReconRecord logInvoiceReconRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote#removeLogInvoiceReconRecordList
	 * (java.util.List)
	 */
	public void removeLogInvoiceReconRecordList(List<LogInvoiceReconRecord> logInvoiceReconRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote#findByLogInvoiceReconRecordLike
	 * (com.gdn.venice.persistence.LogInvoiceReconRecord, int, int)
	 */
	public List<LogInvoiceReconRecord> findByLogInvoiceReconRecordLike(LogInvoiceReconRecord logInvoiceReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceReconRecordSessionEJBRemote#findByLogInvoiceReconRecordLikeFR
	 * (com.gdn.venice.persistence.LogInvoiceReconRecord, int, int)
	 */
	public FinderReturn findByLogInvoiceReconRecordLikeFR(LogInvoiceReconRecord logInvoiceReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
