package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenReturItemStatusHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenReturItemStatusHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemStatusHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenReturItemStatusHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemStatusHistorySessionEJBRemote#persistVenReturItemStatusHistory(com
	 * .gdn.venice.persistence.VenReturItemStatusHistory)
	 */
	public VenReturItemStatusHistory persistVenReturItemStatusHistory(VenReturItemStatusHistory venReturItemStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemStatusHistorySessionEJBRemote#persistVenReturItemStatusHistoryList
	 * (java.util.List)
	 */
	public ArrayList<VenReturItemStatusHistory> persistVenReturItemStatusHistoryList(
			List<VenReturItemStatusHistory> venReturItemStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemStatusHistorySessionEJBRemote#mergeVenReturItemStatusHistory(com.
	 * gdn.venice.persistence.VenReturItemStatusHistory)
	 */
	public VenReturItemStatusHistory mergeVenReturItemStatusHistory(VenReturItemStatusHistory venReturItemStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemStatusHistorySessionEJBRemote#mergeVenReturItemStatusHistoryList(
	 * java.util.List)
	 */
	public ArrayList<VenReturItemStatusHistory> mergeVenReturItemStatusHistoryList(
			List<VenReturItemStatusHistory> venReturItemStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemStatusHistorySessionEJBRemote#removeVenReturItemStatusHistory(com
	 * .gdn.venice.persistence.VenReturItemStatusHistory)
	 */
	public void removeVenReturItemStatusHistory(VenReturItemStatusHistory venReturItemStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemStatusHistorySessionEJBRemote#removeVenReturItemStatusHistoryList
	 * (java.util.List)
	 */
	public void removeVenReturItemStatusHistoryList(List<VenReturItemStatusHistory> venReturItemStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemStatusHistorySessionEJBRemote#findByVenReturItemStatusHistoryLike
	 * (com.gdn.venice.persistence.VenReturItemStatusHistory, int, int)
	 */
	public List<VenReturItemStatusHistory> findByVenReturItemStatusHistoryLike(VenReturItemStatusHistory venReturItemStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemStatusHistorySessionEJBRemote#findByVenReturItemStatusHistoryLikeFR
	 * (com.gdn.venice.persistence.VenReturItemStatusHistory, int, int)
	 */
	public FinderReturn findByVenReturItemStatusHistoryLikeFR(VenReturItemStatusHistory venReturItemStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
