package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenReturStatusHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenReturStatusHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturStatusHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenReturStatusHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturStatusHistorySessionEJBRemote#persistVenReturStatusHistory(com
	 * .gdn.venice.persistence.VenReturStatusHistory)
	 */
	public VenReturStatusHistory persistVenReturStatusHistory(VenReturStatusHistory venReturStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturStatusHistorySessionEJBRemote#persistVenReturStatusHistoryList
	 * (java.util.List)
	 */
	public ArrayList<VenReturStatusHistory> persistVenReturStatusHistoryList(
			List<VenReturStatusHistory> venReturStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturStatusHistorySessionEJBRemote#mergeVenReturStatusHistory(com.
	 * gdn.venice.persistence.VenReturStatusHistory)
	 */
	public VenReturStatusHistory mergeVenReturStatusHistory(VenReturStatusHistory venReturStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturStatusHistorySessionEJBRemote#mergeVenReturStatusHistoryList(
	 * java.util.List)
	 */
	public ArrayList<VenReturStatusHistory> mergeVenReturStatusHistoryList(
			List<VenReturStatusHistory> venReturStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturStatusHistorySessionEJBRemote#removeVenReturStatusHistory(com
	 * .gdn.venice.persistence.VenReturStatusHistory)
	 */
	public void removeVenReturStatusHistory(VenReturStatusHistory venReturStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturStatusHistorySessionEJBRemote#removeVenReturStatusHistoryList
	 * (java.util.List)
	 */
	public void removeVenReturStatusHistoryList(List<VenReturStatusHistory> venReturStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturStatusHistorySessionEJBRemote#findByVenReturStatusHistoryLike
	 * (com.gdn.venice.persistence.VenReturStatusHistory, int, int)
	 */
	public List<VenReturStatusHistory> findByVenReturStatusHistoryLike(VenReturStatusHistory venReturStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturStatusHistorySessionEJBRemote#findByVenReturStatusHistoryLikeFR
	 * (com.gdn.venice.persistence.VenReturStatusHistory, int, int)
	 */
	public FinderReturn findByVenReturStatusHistoryLikeFR(VenReturStatusHistory venReturStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
