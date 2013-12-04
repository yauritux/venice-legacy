package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdEntityBlacklistHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdEntityBlacklistHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdEntityBlacklistHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistHistorySessionEJBRemote#persistFrdEntityBlacklistHistory(com
	 * .gdn.venice.persistence.FrdEntityBlacklistHistory)
	 */
	public FrdEntityBlacklistHistory persistFrdEntityBlacklistHistory(FrdEntityBlacklistHistory frdEntityBlacklistHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistHistorySessionEJBRemote#persistFrdEntityBlacklistHistoryList
	 * (java.util.List)
	 */
	public ArrayList<FrdEntityBlacklistHistory> persistFrdEntityBlacklistHistoryList(
			List<FrdEntityBlacklistHistory> frdEntityBlacklistHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistHistorySessionEJBRemote#mergeFrdEntityBlacklistHistory(com.
	 * gdn.venice.persistence.FrdEntityBlacklistHistory)
	 */
	public FrdEntityBlacklistHistory mergeFrdEntityBlacklistHistory(FrdEntityBlacklistHistory frdEntityBlacklistHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistHistorySessionEJBRemote#mergeFrdEntityBlacklistHistoryList(
	 * java.util.List)
	 */
	public ArrayList<FrdEntityBlacklistHistory> mergeFrdEntityBlacklistHistoryList(
			List<FrdEntityBlacklistHistory> frdEntityBlacklistHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistHistorySessionEJBRemote#removeFrdEntityBlacklistHistory(com
	 * .gdn.venice.persistence.FrdEntityBlacklistHistory)
	 */
	public void removeFrdEntityBlacklistHistory(FrdEntityBlacklistHistory frdEntityBlacklistHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistHistorySessionEJBRemote#removeFrdEntityBlacklistHistoryList
	 * (java.util.List)
	 */
	public void removeFrdEntityBlacklistHistoryList(List<FrdEntityBlacklistHistory> frdEntityBlacklistHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistHistorySessionEJBRemote#findByFrdEntityBlacklistHistoryLike
	 * (com.gdn.venice.persistence.FrdEntityBlacklistHistory, int, int)
	 */
	public List<FrdEntityBlacklistHistory> findByFrdEntityBlacklistHistoryLike(FrdEntityBlacklistHistory frdEntityBlacklistHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEntityBlacklistHistorySessionEJBRemote#findByFrdEntityBlacklistHistoryLikeFR
	 * (com.gdn.venice.persistence.FrdEntityBlacklistHistory, int, int)
	 */
	public FinderReturn findByFrdEntityBlacklistHistoryLikeFR(FrdEntityBlacklistHistory frdEntityBlacklistHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
