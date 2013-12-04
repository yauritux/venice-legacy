package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinArFundsIdReportTime;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinArFundsIdReportTimeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsIdReportTimeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinArFundsIdReportTime> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsIdReportTimeSessionEJBRemote#persistFinArFundsIdReportTime(com
	 * .gdn.venice.persistence.FinArFundsIdReportTime)
	 */
	public FinArFundsIdReportTime persistFinArFundsIdReportTime(FinArFundsIdReportTime finArFundsIdReportTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsIdReportTimeSessionEJBRemote#persistFinArFundsIdReportTimeList
	 * (java.util.List)
	 */
	public ArrayList<FinArFundsIdReportTime> persistFinArFundsIdReportTimeList(
			List<FinArFundsIdReportTime> finArFundsIdReportTimeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsIdReportTimeSessionEJBRemote#mergeFinArFundsIdReportTime(com.
	 * gdn.venice.persistence.FinArFundsIdReportTime)
	 */
	public FinArFundsIdReportTime mergeFinArFundsIdReportTime(FinArFundsIdReportTime finArFundsIdReportTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsIdReportTimeSessionEJBRemote#mergeFinArFundsIdReportTimeList(
	 * java.util.List)
	 */
	public ArrayList<FinArFundsIdReportTime> mergeFinArFundsIdReportTimeList(
			List<FinArFundsIdReportTime> finArFundsIdReportTimeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsIdReportTimeSessionEJBRemote#removeFinArFundsIdReportTime(com
	 * .gdn.venice.persistence.FinArFundsIdReportTime)
	 */
	public void removeFinArFundsIdReportTime(FinArFundsIdReportTime finArFundsIdReportTime);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsIdReportTimeSessionEJBRemote#removeFinArFundsIdReportTimeList
	 * (java.util.List)
	 */
	public void removeFinArFundsIdReportTimeList(List<FinArFundsIdReportTime> finArFundsIdReportTimeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsIdReportTimeSessionEJBRemote#findByFinArFundsIdReportTimeLike
	 * (com.gdn.venice.persistence.FinArFundsIdReportTime, int, int)
	 */
	public List<FinArFundsIdReportTime> findByFinArFundsIdReportTimeLike(FinArFundsIdReportTime finArFundsIdReportTime,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsIdReportTimeSessionEJBRemote#findByFinArFundsIdReportTimeLikeFR
	 * (com.gdn.venice.persistence.FinArFundsIdReportTime, int, int)
	 */
	public FinderReturn findByFinArFundsIdReportTimeLikeFR(FinArFundsIdReportTime finArFundsIdReportTime,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
