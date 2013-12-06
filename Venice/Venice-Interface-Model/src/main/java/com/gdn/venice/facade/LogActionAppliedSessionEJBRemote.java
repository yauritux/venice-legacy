package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogActionApplied;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogActionAppliedSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogActionApplied
	 */
	public List<LogActionApplied> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogActionApplied persists a country
	 * 
	 * @param logActionApplied
	 * @return the persisted LogActionApplied
	 */
	public LogActionApplied persistLogActionApplied(LogActionApplied logActionApplied);

	/**
	 * persistLogActionAppliedList - persists a list of LogActionApplied
	 * 
	 * @param logActionAppliedList
	 * @return the list of persisted LogActionApplied
	 */
	public ArrayList<LogActionApplied> persistLogActionAppliedList(
			List<LogActionApplied> logActionAppliedList);

	/**
	 * mergeLogActionApplied - merges a LogActionApplied
	 * 
	 * @param logActionApplied
	 * @return the merged LogActionApplied
	 */
	public LogActionApplied mergeLogActionApplied(LogActionApplied logActionApplied);

	/**
	 * mergeLogActionAppliedList - merges a list of LogActionApplied
	 * 
	 * @param logActionAppliedList
	 * @return the merged list of LogActionApplied
	 */
	public ArrayList<LogActionApplied> mergeLogActionAppliedList(
			List<LogActionApplied> logActionAppliedList);

	/**
	 * removeLogActionApplied - removes a LogActionApplied
	 * 
	 * @param logActionApplied
	 */
	public void removeLogActionApplied(LogActionApplied logActionApplied);

	/**
	 * removeLogActionAppliedList - removes a list of LogActionApplied
	 * 
	 * @param logActionAppliedList
	 */
	public void removeLogActionAppliedList(List<LogActionApplied> logActionAppliedList);

	/**
	 * findByLogActionAppliedLike - finds a list of LogActionApplied Like
	 * 
	 * @param logActionApplied
	 * @return the list of LogActionApplied found
	 */
	public List<LogActionApplied> findByLogActionAppliedLike(LogActionApplied logActionApplied,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogActionApplied>LikeFR - finds a list of LogActionApplied> Like with a finder return object
	 * 
	 * @param logActionApplied
	 * @return the list of LogActionApplied found
	 */
	public FinderReturn findByLogActionAppliedLikeFR(LogActionApplied logActionApplied,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
