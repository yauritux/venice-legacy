package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.LogAirwayBillRetur;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface LogAirwayBillReturSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<LogAirwayBillRetur> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote#persistLogAirwayBillRetur(com
	 * .gdn.venice.persistence.LogAirwayBillRetur)
	 */
	public LogAirwayBillRetur persistLogAirwayBillRetur(LogAirwayBillRetur logAirwayBillRetur);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote#persistLogAirwayBillReturList
	 * (java.util.List)
	 */
	public ArrayList<LogAirwayBillRetur> persistLogAirwayBillReturList(
			List<LogAirwayBillRetur> logAirwayBillReturList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote#mergeLogAirwayBillRetur(com.
	 * gdn.venice.persistence.LogAirwayBillRetur)
	 */
	public LogAirwayBillRetur mergeLogAirwayBillRetur(LogAirwayBillRetur logAirwayBillRetur);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote#mergeLogAirwayBillReturList(
	 * java.util.List)
	 */
	public ArrayList<LogAirwayBillRetur> mergeLogAirwayBillReturList(
			List<LogAirwayBillRetur> logAirwayBillReturList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote#removeLogAirwayBillRetur(com
	 * .gdn.venice.persistence.LogAirwayBillRetur)
	 */
	public void removeLogAirwayBillRetur(LogAirwayBillRetur logAirwayBillRetur);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote#removeLogAirwayBillReturList
	 * (java.util.List)
	 */
	public void removeLogAirwayBillReturList(List<LogAirwayBillRetur> logAirwayBillReturList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote#findByLogAirwayBillReturLike
	 * (com.gdn.venice.persistence.LogAirwayBillRetur, int, int)
	 */
	public List<LogAirwayBillRetur> findByLogAirwayBillReturLike(LogAirwayBillRetur logAirwayBillRetur,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.LogAirwayBillReturSessionEJBRemote#findByLogAirwayBillReturLikeFR
	 * (com.gdn.venice.persistence.LogAirwayBillRetur, int, int)
	 */
	public FinderReturn findByLogAirwayBillReturLikeFR(LogAirwayBillRetur logAirwayBillRetur,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
