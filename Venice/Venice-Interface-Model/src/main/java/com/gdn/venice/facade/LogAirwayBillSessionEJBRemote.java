package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogAirwayBill;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogAirwayBillSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogAirwayBill
	 */
	public List<LogAirwayBill> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	public String countQueryByRange(String jpqlStmt, int firstResult,
			int maxResults);
	
	public  List<LogAirwayBill> queryByRangeWithNativeQuery(String jpqlStmt, int firstResult,
			int maxResults);
	/**
	 * persistLogAirwayBill persists a country
	 * 
	 * @param logAirwayBill
	 * @return the persisted LogAirwayBill
	 */
	public LogAirwayBill persistLogAirwayBill(LogAirwayBill logAirwayBill);

	/**
	 * persistLogAirwayBillList - persists a list of LogAirwayBill
	 * 
	 * @param logAirwayBillList
	 * @return the list of persisted LogAirwayBill
	 */
	public ArrayList<LogAirwayBill> persistLogAirwayBillList(
			List<LogAirwayBill> logAirwayBillList);

	/**
	 * mergeLogAirwayBill - merges a LogAirwayBill
	 * 
	 * @param logAirwayBill
	 * @return the merged LogAirwayBill
	 */
	public LogAirwayBill mergeLogAirwayBill(LogAirwayBill logAirwayBill);

	/**
	 * mergeLogAirwayBillList - merges a list of LogAirwayBill
	 * 
	 * @param logAirwayBillList
	 * @return the merged list of LogAirwayBill
	 */
	public ArrayList<LogAirwayBill> mergeLogAirwayBillList(
			List<LogAirwayBill> logAirwayBillList);

	/**
	 * removeLogAirwayBill - removes a LogAirwayBill
	 * 
	 * @param logAirwayBill
	 */
	public void removeLogAirwayBill(LogAirwayBill logAirwayBill);

	/**
	 * removeLogAirwayBillList - removes a list of LogAirwayBill
	 * 
	 * @param logAirwayBillList
	 */
	public void removeLogAirwayBillList(List<LogAirwayBill> logAirwayBillList);

	/**
	 * findByLogAirwayBillLike - finds a list of LogAirwayBill Like
	 * 
	 * @param logAirwayBill
	 * @return the list of LogAirwayBill found
	 */
	public List<LogAirwayBill> findByLogAirwayBillLike(LogAirwayBill logAirwayBill,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogAirwayBill>LikeFR - finds a list of LogAirwayBill> Like with a finder return object
	 * 
	 * @param logAirwayBill
	 * @return the list of LogAirwayBill found
	 */
	public FinderReturn findByLogAirwayBillLikeFR(LogAirwayBill logAirwayBill,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
