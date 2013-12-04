package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogScheduleDayOfWeek;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogScheduleDayOfWeekSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogScheduleDayOfWeekSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogScheduleDayOfWeek> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogScheduleDayOfWeekSessionEJBRemote#persistLogScheduleDayOfWeek(com
	 * .gdn.venice.persistence.LogScheduleDayOfWeek)
	 */
	public LogScheduleDayOfWeek persistLogScheduleDayOfWeek(LogScheduleDayOfWeek logScheduleDayOfWeek);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogScheduleDayOfWeekSessionEJBRemote#persistLogScheduleDayOfWeekList
	 * (java.util.List)
	 */
	public ArrayList<LogScheduleDayOfWeek> persistLogScheduleDayOfWeekList(
			List<LogScheduleDayOfWeek> logScheduleDayOfWeekList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogScheduleDayOfWeekSessionEJBRemote#mergeLogScheduleDayOfWeek(com.
	 * gdn.venice.persistence.LogScheduleDayOfWeek)
	 */
	public LogScheduleDayOfWeek mergeLogScheduleDayOfWeek(LogScheduleDayOfWeek logScheduleDayOfWeek);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogScheduleDayOfWeekSessionEJBRemote#mergeLogScheduleDayOfWeekList(
	 * java.util.List)
	 */
	public ArrayList<LogScheduleDayOfWeek> mergeLogScheduleDayOfWeekList(
			List<LogScheduleDayOfWeek> logScheduleDayOfWeekList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogScheduleDayOfWeekSessionEJBRemote#removeLogScheduleDayOfWeek(com
	 * .gdn.venice.persistence.LogScheduleDayOfWeek)
	 */
	public void removeLogScheduleDayOfWeek(LogScheduleDayOfWeek logScheduleDayOfWeek);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogScheduleDayOfWeekSessionEJBRemote#removeLogScheduleDayOfWeekList
	 * (java.util.List)
	 */
	public void removeLogScheduleDayOfWeekList(List<LogScheduleDayOfWeek> logScheduleDayOfWeekList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogScheduleDayOfWeekSessionEJBRemote#findByLogScheduleDayOfWeekLike
	 * (com.gdn.venice.persistence.LogScheduleDayOfWeek, int, int)
	 */
	public List<LogScheduleDayOfWeek> findByLogScheduleDayOfWeekLike(LogScheduleDayOfWeek logScheduleDayOfWeek,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogScheduleDayOfWeekSessionEJBRemote#findByLogScheduleDayOfWeekLikeFR
	 * (com.gdn.venice.persistence.LogScheduleDayOfWeek, int, int)
	 */
	public FinderReturn findByLogScheduleDayOfWeekLikeFR(LogScheduleDayOfWeek logScheduleDayOfWeek,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
