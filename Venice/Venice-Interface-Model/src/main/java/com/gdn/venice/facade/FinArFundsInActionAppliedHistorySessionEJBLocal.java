package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinArFundsInActionAppliedHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinArFundsInActionAppliedHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinArFundsInActionAppliedHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote#persistFinArFundsInActionAppliedHistory(com
	 * .gdn.venice.persistence.FinArFundsInActionAppliedHistory)
	 */
	public FinArFundsInActionAppliedHistory persistFinArFundsInActionAppliedHistory(FinArFundsInActionAppliedHistory finArFundsInActionAppliedHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote#persistFinArFundsInActionAppliedHistoryList
	 * (java.util.List)
	 */
	public ArrayList<FinArFundsInActionAppliedHistory> persistFinArFundsInActionAppliedHistoryList(
			List<FinArFundsInActionAppliedHistory> finArFundsInActionAppliedHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote#mergeFinArFundsInActionAppliedHistory(com.
	 * gdn.venice.persistence.FinArFundsInActionAppliedHistory)
	 */
	public FinArFundsInActionAppliedHistory mergeFinArFundsInActionAppliedHistory(FinArFundsInActionAppliedHistory finArFundsInActionAppliedHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote#mergeFinArFundsInActionAppliedHistoryList(
	 * java.util.List)
	 */
	public ArrayList<FinArFundsInActionAppliedHistory> mergeFinArFundsInActionAppliedHistoryList(
			List<FinArFundsInActionAppliedHistory> finArFundsInActionAppliedHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote#removeFinArFundsInActionAppliedHistory(com
	 * .gdn.venice.persistence.FinArFundsInActionAppliedHistory)
	 */
	public void removeFinArFundsInActionAppliedHistory(FinArFundsInActionAppliedHistory finArFundsInActionAppliedHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote#removeFinArFundsInActionAppliedHistoryList
	 * (java.util.List)
	 */
	public void removeFinArFundsInActionAppliedHistoryList(List<FinArFundsInActionAppliedHistory> finArFundsInActionAppliedHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote#findByFinArFundsInActionAppliedHistoryLike
	 * (com.gdn.venice.persistence.FinArFundsInActionAppliedHistory, int, int)
	 */
	public List<FinArFundsInActionAppliedHistory> findByFinArFundsInActionAppliedHistoryLike(FinArFundsInActionAppliedHistory finArFundsInActionAppliedHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArFundsInActionAppliedHistorySessionEJBRemote#findByFinArFundsInActionAppliedHistoryLikeFR
	 * (com.gdn.venice.persistence.FinArFundsInActionAppliedHistory, int, int)
	 */
	public FinderReturn findByFinArFundsInActionAppliedHistoryLikeFR(FinArFundsInActionAppliedHistory finArFundsInActionAppliedHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
