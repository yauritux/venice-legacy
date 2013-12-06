package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdFraudCaseHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdFraudCaseHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdFraudCaseHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote#persistFrdFraudCaseHistory(com
	 * .gdn.venice.persistence.FrdFraudCaseHistory)
	 */
	public FrdFraudCaseHistory persistFrdFraudCaseHistory(FrdFraudCaseHistory frdFraudCaseHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote#persistFrdFraudCaseHistoryList
	 * (java.util.List)
	 */
	public ArrayList<FrdFraudCaseHistory> persistFrdFraudCaseHistoryList(
			List<FrdFraudCaseHistory> frdFraudCaseHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote#mergeFrdFraudCaseHistory(com.
	 * gdn.venice.persistence.FrdFraudCaseHistory)
	 */
	public FrdFraudCaseHistory mergeFrdFraudCaseHistory(FrdFraudCaseHistory frdFraudCaseHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote#mergeFrdFraudCaseHistoryList(
	 * java.util.List)
	 */
	public ArrayList<FrdFraudCaseHistory> mergeFrdFraudCaseHistoryList(
			List<FrdFraudCaseHistory> frdFraudCaseHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote#removeFrdFraudCaseHistory(com
	 * .gdn.venice.persistence.FrdFraudCaseHistory)
	 */
	public void removeFrdFraudCaseHistory(FrdFraudCaseHistory frdFraudCaseHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote#removeFrdFraudCaseHistoryList
	 * (java.util.List)
	 */
	public void removeFrdFraudCaseHistoryList(List<FrdFraudCaseHistory> frdFraudCaseHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote#findByFrdFraudCaseHistoryLike
	 * (com.gdn.venice.persistence.FrdFraudCaseHistory, int, int)
	 */
	public List<FrdFraudCaseHistory> findByFrdFraudCaseHistoryLike(FrdFraudCaseHistory frdFraudCaseHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudCaseHistorySessionEJBRemote#findByFrdFraudCaseHistoryLikeFR
	 * (com.gdn.venice.persistence.FrdFraudCaseHistory, int, int)
	 */
	public FinderReturn findByFrdFraudCaseHistoryLikeFR(FrdFraudCaseHistory frdFraudCaseHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
