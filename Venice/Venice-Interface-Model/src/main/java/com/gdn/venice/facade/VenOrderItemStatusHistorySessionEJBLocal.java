package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderItemStatusHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderItemStatusHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderItemStatusHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote#persistVenOrderItemStatusHistory(com
	 * .gdn.venice.persistence.VenOrderItemStatusHistory)
	 */
	public VenOrderItemStatusHistory persistVenOrderItemStatusHistory(VenOrderItemStatusHistory venOrderItemStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote#persistVenOrderItemStatusHistoryList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderItemStatusHistory> persistVenOrderItemStatusHistoryList(
			List<VenOrderItemStatusHistory> venOrderItemStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote#mergeVenOrderItemStatusHistory(com.
	 * gdn.venice.persistence.VenOrderItemStatusHistory)
	 */
	public VenOrderItemStatusHistory mergeVenOrderItemStatusHistory(VenOrderItemStatusHistory venOrderItemStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote#mergeVenOrderItemStatusHistoryList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderItemStatusHistory> mergeVenOrderItemStatusHistoryList(
			List<VenOrderItemStatusHistory> venOrderItemStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote#removeVenOrderItemStatusHistory(com
	 * .gdn.venice.persistence.VenOrderItemStatusHistory)
	 */
	public void removeVenOrderItemStatusHistory(VenOrderItemStatusHistory venOrderItemStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote#removeVenOrderItemStatusHistoryList
	 * (java.util.List)
	 */
	public void removeVenOrderItemStatusHistoryList(List<VenOrderItemStatusHistory> venOrderItemStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote#findByVenOrderItemStatusHistoryLike
	 * (com.gdn.venice.persistence.VenOrderItemStatusHistory, int, int)
	 */
	public List<VenOrderItemStatusHistory> findByVenOrderItemStatusHistoryLike(VenOrderItemStatusHistory venOrderItemStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemStatusHistorySessionEJBRemote#findByVenOrderItemStatusHistoryLikeFR
	 * (com.gdn.venice.persistence.VenOrderItemStatusHistory, int, int)
	 */
	public FinderReturn findByVenOrderItemStatusHistoryLikeFR(VenOrderItemStatusHistory venOrderItemStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
