package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogActionApplied;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogActionAppliedSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActionAppliedSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogActionApplied> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActionAppliedSessionEJBRemote#persistLogActionApplied(com
	 * .gdn.venice.persistence.LogActionApplied)
	 */
	public LogActionApplied persistLogActionApplied(LogActionApplied logActionApplied);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActionAppliedSessionEJBRemote#persistLogActionAppliedList
	 * (java.util.List)
	 */
	public ArrayList<LogActionApplied> persistLogActionAppliedList(
			List<LogActionApplied> logActionAppliedList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActionAppliedSessionEJBRemote#mergeLogActionApplied(com.
	 * gdn.venice.persistence.LogActionApplied)
	 */
	public LogActionApplied mergeLogActionApplied(LogActionApplied logActionApplied);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActionAppliedSessionEJBRemote#mergeLogActionAppliedList(
	 * java.util.List)
	 */
	public ArrayList<LogActionApplied> mergeLogActionAppliedList(
			List<LogActionApplied> logActionAppliedList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActionAppliedSessionEJBRemote#removeLogActionApplied(com
	 * .gdn.venice.persistence.LogActionApplied)
	 */
	public void removeLogActionApplied(LogActionApplied logActionApplied);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActionAppliedSessionEJBRemote#removeLogActionAppliedList
	 * (java.util.List)
	 */
	public void removeLogActionAppliedList(List<LogActionApplied> logActionAppliedList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActionAppliedSessionEJBRemote#findByLogActionAppliedLike
	 * (com.gdn.venice.persistence.LogActionApplied, int, int)
	 */
	public List<LogActionApplied> findByLogActionAppliedLike(LogActionApplied logActionApplied,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogActionAppliedSessionEJBRemote#findByLogActionAppliedLikeFR
	 * (com.gdn.venice.persistence.LogActionApplied, int, int)
	 */
	public FinderReturn findByLogActionAppliedLikeFR(LogActionApplied logActionApplied,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
