package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogInvoiceAirwaybillRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogInvoiceAirwaybillRecordSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogInvoiceAirwaybillRecord> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote#persistLogInvoiceAirwaybillRecord(com
	 * .gdn.venice.persistence.LogInvoiceAirwaybillRecord)
	 */
	public LogInvoiceAirwaybillRecord persistLogInvoiceAirwaybillRecord(LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote#persistLogInvoiceAirwaybillRecordList
	 * (java.util.List)
	 */
	public ArrayList<LogInvoiceAirwaybillRecord> persistLogInvoiceAirwaybillRecordList(
			List<LogInvoiceAirwaybillRecord> logInvoiceAirwaybillRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote#mergeLogInvoiceAirwaybillRecord(com.
	 * gdn.venice.persistence.LogInvoiceAirwaybillRecord)
	 */
	public LogInvoiceAirwaybillRecord mergeLogInvoiceAirwaybillRecord(LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote#mergeLogInvoiceAirwaybillRecordList(
	 * java.util.List)
	 */
	public ArrayList<LogInvoiceAirwaybillRecord> mergeLogInvoiceAirwaybillRecordList(
			List<LogInvoiceAirwaybillRecord> logInvoiceAirwaybillRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote#removeLogInvoiceAirwaybillRecord(com
	 * .gdn.venice.persistence.LogInvoiceAirwaybillRecord)
	 */
	public void removeLogInvoiceAirwaybillRecord(LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote#removeLogInvoiceAirwaybillRecordList
	 * (java.util.List)
	 */
	public void removeLogInvoiceAirwaybillRecordList(List<LogInvoiceAirwaybillRecord> logInvoiceAirwaybillRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote#findByLogInvoiceAirwaybillRecordLike
	 * (com.gdn.venice.persistence.LogInvoiceAirwaybillRecord, int, int)
	 */
	public List<LogInvoiceAirwaybillRecord> findByLogInvoiceAirwaybillRecordLike(LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogInvoiceAirwaybillRecordSessionEJBRemote#findByLogInvoiceAirwaybillRecordLikeFR
	 * (com.gdn.venice.persistence.LogInvoiceAirwaybillRecord, int, int)
	 */
	public FinderReturn findByLogInvoiceAirwaybillRecordLikeFR(LogInvoiceAirwaybillRecord logInvoiceAirwaybillRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
