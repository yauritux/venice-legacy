package com.gdn.venice.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;
import com.gdn.venice.persistence.FinSalesRecord;

@Remote
public interface FinSalesRecordSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinSalesRecord
	 */
	public List<FinSalesRecord> queryByRange(String jpqlStmt, int firstResult,	int maxResults);
	/**
	 * persistFinSalesRecord persists a country
	 * 
	 * @param finSalesRecord
	 * @return the persisted FinSalesRecord
	 */
	public FinSalesRecord persistFinSalesRecord(FinSalesRecord finSalesRecord);

	/**
	 * persistFinSalesRecordList - persists a list of FinSalesRecord
	 * 
	 * @param finSalesRecordList
	 * @return the list of persisted FinSalesRecord
	 */
	public ArrayList<FinSalesRecord> persistFinSalesRecordList(
			List<FinSalesRecord> finSalesRecordList);

	/**
	 * mergeFinSalesRecord - merges a FinSalesRecord
	 * 
	 * @param finSalesRecord
	 * @return the merged FinSalesRecord
	 */
	public FinSalesRecord mergeFinSalesRecord(FinSalesRecord finSalesRecord);

	/**
	 * mergeFinSalesRecordList - merges a list of FinSalesRecord
	 * 
	 * @param finSalesRecordList
	 * @return the merged list of FinSalesRecord
	 */
	public ArrayList<FinSalesRecord> mergeFinSalesRecordList(
			List<FinSalesRecord> finSalesRecordList);

	/**
	 * removeFinSalesRecord - removes a FinSalesRecord
	 * 
	 * @param finSalesRecord
	 */
	public void removeFinSalesRecord(FinSalesRecord finSalesRecord);

	/**
	 * removeFinSalesRecordList - removes a list of FinSalesRecord
	 * 
	 * @param finSalesRecordList
	 */
	public void removeFinSalesRecordList(List<FinSalesRecord> finSalesRecordList);

	/**
	 * findByFinSalesRecordLike - finds a list of FinSalesRecord Like
	 * 
	 * @param finSalesRecord
	 * @return the list of FinSalesRecord found
	 */
	public List<FinSalesRecord> findByFinSalesRecordLike(FinSalesRecord finSalesRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinSalesRecord>LikeFR - finds a list of FinSalesRecord> Like with a finder return object
	 * 
	 * @param finSalesRecord
	 * @return the list of FinSalesRecord found
	 */
	public FinderReturn findByFinSalesRecordLikeFR(FinSalesRecord finSalesRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	
	public List<FinSalesRecord> queryByRangeWithNativeQueryUseToFinanceDashboard(String jpqlStmt);
}
