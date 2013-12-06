package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinSalesRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinSalesRecordSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinSalesRecordSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinSalesRecord> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinSalesRecordSessionEJBRemote#persistFinSalesRecord(com
	 * .gdn.venice.persistence.FinSalesRecord)
	 */
	public FinSalesRecord persistFinSalesRecord(FinSalesRecord finSalesRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinSalesRecordSessionEJBRemote#persistFinSalesRecordList
	 * (java.util.List)
	 */
	public ArrayList<FinSalesRecord> persistFinSalesRecordList(
			List<FinSalesRecord> finSalesRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinSalesRecordSessionEJBRemote#mergeFinSalesRecord(com.
	 * gdn.venice.persistence.FinSalesRecord)
	 */
	public FinSalesRecord mergeFinSalesRecord(FinSalesRecord finSalesRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinSalesRecordSessionEJBRemote#mergeFinSalesRecordList(
	 * java.util.List)
	 */
	public ArrayList<FinSalesRecord> mergeFinSalesRecordList(
			List<FinSalesRecord> finSalesRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinSalesRecordSessionEJBRemote#removeFinSalesRecord(com
	 * .gdn.venice.persistence.FinSalesRecord)
	 */
	public void removeFinSalesRecord(FinSalesRecord finSalesRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinSalesRecordSessionEJBRemote#removeFinSalesRecordList
	 * (java.util.List)
	 */
	public void removeFinSalesRecordList(List<FinSalesRecord> finSalesRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinSalesRecordSessionEJBRemote#findByFinSalesRecordLike
	 * (com.gdn.venice.persistence.FinSalesRecord, int, int)
	 */
	public List<FinSalesRecord> findByFinSalesRecordLike(FinSalesRecord finSalesRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinSalesRecordSessionEJBRemote#findByFinSalesRecordLikeFR
	 * (com.gdn.venice.persistence.FinSalesRecord, int, int)
	 */
	public FinderReturn findByFinSalesRecordLikeFR(FinSalesRecord finSalesRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
