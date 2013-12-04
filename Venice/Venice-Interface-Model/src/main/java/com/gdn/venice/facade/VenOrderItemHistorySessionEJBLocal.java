package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderItemHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderItemHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderItemHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemHistorySessionEJBRemote#persistVenOrderItemHistory(com
	 * .gdn.venice.persistence.VenOrderItemHistory)
	 */
	public VenOrderItemHistory persistVenOrderItemHistory(VenOrderItemHistory venOrderItemHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemHistorySessionEJBRemote#persistVenOrderItemHistoryList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderItemHistory> persistVenOrderItemHistoryList(
			List<VenOrderItemHistory> venOrderItemHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemHistorySessionEJBRemote#mergeVenOrderItemHistory(com.
	 * gdn.venice.persistence.VenOrderItemHistory)
	 */
	public VenOrderItemHistory mergeVenOrderItemHistory(VenOrderItemHistory venOrderItemHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemHistorySessionEJBRemote#mergeVenOrderItemHistoryList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderItemHistory> mergeVenOrderItemHistoryList(
			List<VenOrderItemHistory> venOrderItemHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemHistorySessionEJBRemote#removeVenOrderItemHistory(com
	 * .gdn.venice.persistence.VenOrderItemHistory)
	 */
	public void removeVenOrderItemHistory(VenOrderItemHistory venOrderItemHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemHistorySessionEJBRemote#removeVenOrderItemHistoryList
	 * (java.util.List)
	 */
	public void removeVenOrderItemHistoryList(List<VenOrderItemHistory> venOrderItemHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemHistorySessionEJBRemote#findByVenOrderItemHistoryLike
	 * (com.gdn.venice.persistence.VenOrderItemHistory, int, int)
	 */
	public List<VenOrderItemHistory> findByVenOrderItemHistoryLike(VenOrderItemHistory venOrderItemHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemHistorySessionEJBRemote#findByVenOrderItemHistoryLikeFR
	 * (com.gdn.venice.persistence.VenOrderItemHistory, int, int)
	 */
	public FinderReturn findByVenOrderItemHistoryLikeFR(VenOrderItemHistory venOrderItemHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
