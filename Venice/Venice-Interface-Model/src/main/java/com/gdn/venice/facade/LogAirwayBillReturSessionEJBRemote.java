package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogAirwayBillRetur;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogAirwayBillReturSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogAirwayBillRetur
	 */
	public List<LogAirwayBillRetur> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogAirwayBillRetur persists a country
	 * 
	 * @param logAirwayBillRetur
	 * @return the persisted LogAirwayBillRetur
	 */
	public LogAirwayBillRetur persistLogAirwayBillRetur(LogAirwayBillRetur logAirwayBillRetur);

	/**
	 * persistLogAirwayBillReturList - persists a list of LogAirwayBillRetur
	 * 
	 * @param logAirwayBillReturList
	 * @return the list of persisted LogAirwayBillRetur
	 */
	public ArrayList<LogAirwayBillRetur> persistLogAirwayBillReturList(
			List<LogAirwayBillRetur> logAirwayBillReturList);

	/**
	 * mergeLogAirwayBillRetur - merges a LogAirwayBillRetur
	 * 
	 * @param logAirwayBillRetur
	 * @return the merged LogAirwayBillRetur
	 */
	public LogAirwayBillRetur mergeLogAirwayBillRetur(LogAirwayBillRetur logAirwayBillRetur);

	/**
	 * mergeLogAirwayBillReturList - merges a list of LogAirwayBillRetur
	 * 
	 * @param logAirwayBillReturList
	 * @return the merged list of LogAirwayBillRetur
	 */
	public ArrayList<LogAirwayBillRetur> mergeLogAirwayBillReturList(
			List<LogAirwayBillRetur> logAirwayBillReturList);

	/**
	 * removeLogAirwayBillRetur - removes a LogAirwayBillRetur
	 * 
	 * @param logAirwayBillRetur
	 */
	public void removeLogAirwayBillRetur(LogAirwayBillRetur logAirwayBillRetur);

	/**
	 * removeLogAirwayBillReturList - removes a list of LogAirwayBillRetur
	 * 
	 * @param logAirwayBillReturList
	 */
	public void removeLogAirwayBillReturList(List<LogAirwayBillRetur> logAirwayBillReturList);

	/**
	 * findByLogAirwayBillReturLike - finds a list of LogAirwayBillRetur Like
	 * 
	 * @param logAirwayBillRetur
	 * @return the list of LogAirwayBillRetur found
	 */
	public List<LogAirwayBillRetur> findByLogAirwayBillReturLike(LogAirwayBillRetur logAirwayBillRetur,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogAirwayBillRetur>LikeFR - finds a list of LogAirwayBillRetur> Like with a finder return object
	 * 
	 * @param logAirwayBillRetur
	 * @return the list of LogAirwayBillRetur found
	 */
	public FinderReturn findByLogAirwayBillReturLikeFR(LogAirwayBillRetur logAirwayBillRetur,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
