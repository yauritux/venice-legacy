package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogPickupSchedule;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogPickupScheduleSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupScheduleSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogPickupSchedule> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupScheduleSessionEJBRemote#persistLogPickupSchedule(com
	 * .gdn.venice.persistence.LogPickupSchedule)
	 */
	public LogPickupSchedule persistLogPickupSchedule(LogPickupSchedule logPickupSchedule);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupScheduleSessionEJBRemote#persistLogPickupScheduleList
	 * (java.util.List)
	 */
	public ArrayList<LogPickupSchedule> persistLogPickupScheduleList(
			List<LogPickupSchedule> logPickupScheduleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupScheduleSessionEJBRemote#mergeLogPickupSchedule(com.
	 * gdn.venice.persistence.LogPickupSchedule)
	 */
	public LogPickupSchedule mergeLogPickupSchedule(LogPickupSchedule logPickupSchedule);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupScheduleSessionEJBRemote#mergeLogPickupScheduleList(
	 * java.util.List)
	 */
	public ArrayList<LogPickupSchedule> mergeLogPickupScheduleList(
			List<LogPickupSchedule> logPickupScheduleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupScheduleSessionEJBRemote#removeLogPickupSchedule(com
	 * .gdn.venice.persistence.LogPickupSchedule)
	 */
	public void removeLogPickupSchedule(LogPickupSchedule logPickupSchedule);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupScheduleSessionEJBRemote#removeLogPickupScheduleList
	 * (java.util.List)
	 */
	public void removeLogPickupScheduleList(List<LogPickupSchedule> logPickupScheduleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupScheduleSessionEJBRemote#findByLogPickupScheduleLike
	 * (com.gdn.venice.persistence.LogPickupSchedule, int, int)
	 */
	public List<LogPickupSchedule> findByLogPickupScheduleLike(LogPickupSchedule logPickupSchedule,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogPickupScheduleSessionEJBRemote#findByLogPickupScheduleLikeFR
	 * (com.gdn.venice.persistence.LogPickupSchedule, int, int)
	 */
	public FinderReturn findByLogPickupScheduleLikeFR(LogPickupSchedule logPickupSchedule,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
