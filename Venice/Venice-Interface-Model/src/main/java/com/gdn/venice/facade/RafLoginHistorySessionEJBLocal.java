package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafLoginHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafLoginHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafLoginHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafLoginHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafLoginHistorySessionEJBRemote#persistRafLoginHistory(com
	 * .gdn.venice.persistence.RafLoginHistory)
	 */
	public RafLoginHistory persistRafLoginHistory(RafLoginHistory rafLoginHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafLoginHistorySessionEJBRemote#persistRafLoginHistoryList
	 * (java.util.List)
	 */
	public ArrayList<RafLoginHistory> persistRafLoginHistoryList(
			List<RafLoginHistory> rafLoginHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafLoginHistorySessionEJBRemote#mergeRafLoginHistory(com.
	 * gdn.venice.persistence.RafLoginHistory)
	 */
	public RafLoginHistory mergeRafLoginHistory(RafLoginHistory rafLoginHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafLoginHistorySessionEJBRemote#mergeRafLoginHistoryList(
	 * java.util.List)
	 */
	public ArrayList<RafLoginHistory> mergeRafLoginHistoryList(
			List<RafLoginHistory> rafLoginHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafLoginHistorySessionEJBRemote#removeRafLoginHistory(com
	 * .gdn.venice.persistence.RafLoginHistory)
	 */
	public void removeRafLoginHistory(RafLoginHistory rafLoginHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafLoginHistorySessionEJBRemote#removeRafLoginHistoryList
	 * (java.util.List)
	 */
	public void removeRafLoginHistoryList(List<RafLoginHistory> rafLoginHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafLoginHistorySessionEJBRemote#findByRafLoginHistoryLike
	 * (com.gdn.venice.persistence.RafLoginHistory, int, int)
	 */
	public List<RafLoginHistory> findByRafLoginHistoryLike(RafLoginHistory rafLoginHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafLoginHistorySessionEJBRemote#findByRafLoginHistoryLikeFR
	 * (com.gdn.venice.persistence.RafLoginHistory, int, int)
	 */
	public FinderReturn findByRafLoginHistoryLikeFR(RafLoginHistory rafLoginHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
