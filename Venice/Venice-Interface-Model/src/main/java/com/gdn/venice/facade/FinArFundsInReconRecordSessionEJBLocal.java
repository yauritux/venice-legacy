package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinArFundsInReconRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinArFundsInReconRecordSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinArFundsInReconRecord> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote#persistFinArFundsInReconRecord(com
	 * .gdn.venice.persistence.FinArFundsInReconRecord)
	 */
	public FinArFundsInReconRecord persistFinArFundsInReconRecord(FinArFundsInReconRecord finArFundsInReconRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote#persistFinArFundsInReconRecordList
	 * (java.util.List)
	 */
	public ArrayList<FinArFundsInReconRecord> persistFinArFundsInReconRecordList(
			List<FinArFundsInReconRecord> finArFundsInReconRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote#mergeFinArFundsInReconRecord(com.
	 * gdn.venice.persistence.FinArFundsInReconRecord)
	 */
	public FinArFundsInReconRecord mergeFinArFundsInReconRecord(FinArFundsInReconRecord finArFundsInReconRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote#mergeFinArFundsInReconRecordList(
	 * java.util.List)
	 */
	public ArrayList<FinArFundsInReconRecord> mergeFinArFundsInReconRecordList(
			List<FinArFundsInReconRecord> finArFundsInReconRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote#removeFinArFundsInReconRecord(com
	 * .gdn.venice.persistence.FinArFundsInReconRecord)
	 */
	public void removeFinArFundsInReconRecord(FinArFundsInReconRecord finArFundsInReconRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote#removeFinArFundsInReconRecordList
	 * (java.util.List)
	 */
	public void removeFinArFundsInReconRecordList(List<FinArFundsInReconRecord> finArFundsInReconRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote#findByFinArFundsInReconRecordLike
	 * (com.gdn.venice.persistence.FinArFundsInReconRecord, int, int)
	 */
	public List<FinArFundsInReconRecord> findByFinArFundsInReconRecordLike(FinArFundsInReconRecord finArFundsInReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInReconRecordSessionEJBRemote#findByFinArFundsInReconRecordLikeFR
	 * (com.gdn.venice.persistence.FinArFundsInReconRecord, int, int)
	 */
	public FinderReturn findByFinArFundsInReconRecordLikeFR(FinArFundsInReconRecord finArFundsInReconRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
