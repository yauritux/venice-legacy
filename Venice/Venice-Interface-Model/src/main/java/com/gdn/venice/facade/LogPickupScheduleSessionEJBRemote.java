package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.LogPickupSchedule;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface LogPickupScheduleSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of LogPickupSchedule
	 */
	public List<LogPickupSchedule> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistLogPickupSchedule persists a country
	 * 
	 * @param logPickupSchedule
	 * @return the persisted LogPickupSchedule
	 */
	public LogPickupSchedule persistLogPickupSchedule(LogPickupSchedule logPickupSchedule);

	/**
	 * persistLogPickupScheduleList - persists a list of LogPickupSchedule
	 * 
	 * @param logPickupScheduleList
	 * @return the list of persisted LogPickupSchedule
	 */
	public ArrayList<LogPickupSchedule> persistLogPickupScheduleList(
			List<LogPickupSchedule> logPickupScheduleList);

	/**
	 * mergeLogPickupSchedule - merges a LogPickupSchedule
	 * 
	 * @param logPickupSchedule
	 * @return the merged LogPickupSchedule
	 */
	public LogPickupSchedule mergeLogPickupSchedule(LogPickupSchedule logPickupSchedule);

	/**
	 * mergeLogPickupScheduleList - merges a list of LogPickupSchedule
	 * 
	 * @param logPickupScheduleList
	 * @return the merged list of LogPickupSchedule
	 */
	public ArrayList<LogPickupSchedule> mergeLogPickupScheduleList(
			List<LogPickupSchedule> logPickupScheduleList);

	/**
	 * removeLogPickupSchedule - removes a LogPickupSchedule
	 * 
	 * @param logPickupSchedule
	 */
	public void removeLogPickupSchedule(LogPickupSchedule logPickupSchedule);

	/**
	 * removeLogPickupScheduleList - removes a list of LogPickupSchedule
	 * 
	 * @param logPickupScheduleList
	 */
	public void removeLogPickupScheduleList(List<LogPickupSchedule> logPickupScheduleList);

	/**
	 * findByLogPickupScheduleLike - finds a list of LogPickupSchedule Like
	 * 
	 * @param logPickupSchedule
	 * @return the list of LogPickupSchedule found
	 */
	public List<LogPickupSchedule> findByLogPickupScheduleLike(LogPickupSchedule logPickupSchedule,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByLogPickupSchedule>LikeFR - finds a list of LogPickupSchedule> Like with a finder return object
	 * 
	 * @param logPickupSchedule
	 * @return the list of LogPickupSchedule found
	 */
	public FinderReturn findByLogPickupScheduleLikeFR(LogPickupSchedule logPickupSchedule,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
