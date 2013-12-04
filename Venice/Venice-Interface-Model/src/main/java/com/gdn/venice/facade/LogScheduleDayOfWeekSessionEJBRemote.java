package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogScheduleDayOfWeek;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogScheduleDayOfWeekSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogScheduleDayOfWeek
	 */
	public List<LogScheduleDayOfWeek> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogScheduleDayOfWeek persists a country
	 * 
	 * @param logScheduleDayOfWeek
	 * @return the persisted LogScheduleDayOfWeek
	 */
	public LogScheduleDayOfWeek persistLogScheduleDayOfWeek(LogScheduleDayOfWeek logScheduleDayOfWeek);

	/**
	 * persistLogScheduleDayOfWeekList - persists a list of LogScheduleDayOfWeek
	 * 
	 * @param logScheduleDayOfWeekList
	 * @return the list of persisted LogScheduleDayOfWeek
	 */
	public ArrayList<LogScheduleDayOfWeek> persistLogScheduleDayOfWeekList(
			List<LogScheduleDayOfWeek> logScheduleDayOfWeekList);

	/**
	 * mergeLogScheduleDayOfWeek - merges a LogScheduleDayOfWeek
	 * 
	 * @param logScheduleDayOfWeek
	 * @return the merged LogScheduleDayOfWeek
	 */
	public LogScheduleDayOfWeek mergeLogScheduleDayOfWeek(LogScheduleDayOfWeek logScheduleDayOfWeek);

	/**
	 * mergeLogScheduleDayOfWeekList - merges a list of LogScheduleDayOfWeek
	 * 
	 * @param logScheduleDayOfWeekList
	 * @return the merged list of LogScheduleDayOfWeek
	 */
	public ArrayList<LogScheduleDayOfWeek> mergeLogScheduleDayOfWeekList(
			List<LogScheduleDayOfWeek> logScheduleDayOfWeekList);

	/**
	 * removeLogScheduleDayOfWeek - removes a LogScheduleDayOfWeek
	 * 
	 * @param logScheduleDayOfWeek
	 */
	public void removeLogScheduleDayOfWeek(LogScheduleDayOfWeek logScheduleDayOfWeek);

	/**
	 * removeLogScheduleDayOfWeekList - removes a list of LogScheduleDayOfWeek
	 * 
	 * @param logScheduleDayOfWeekList
	 */
	public void removeLogScheduleDayOfWeekList(List<LogScheduleDayOfWeek> logScheduleDayOfWeekList);

	/**
	 * findByLogScheduleDayOfWeekLike - finds a list of LogScheduleDayOfWeek Like
	 * 
	 * @param logScheduleDayOfWeek
	 * @return the list of LogScheduleDayOfWeek found
	 */
	public List<LogScheduleDayOfWeek> findByLogScheduleDayOfWeekLike(LogScheduleDayOfWeek logScheduleDayOfWeek,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogScheduleDayOfWeek>LikeFR - finds a list of LogScheduleDayOfWeek> Like with a finder return object
	 * 
	 * @param logScheduleDayOfWeek
	 * @return the list of LogScheduleDayOfWeek found
	 */
	public FinderReturn findByLogScheduleDayOfWeekLikeFR(LogScheduleDayOfWeek logScheduleDayOfWeek,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
