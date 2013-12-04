package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderHistorySessionEJBRemote#persistVenOrderHistory(com
	 * .gdn.venice.persistence.VenOrderHistory)
	 */
	public VenOrderHistory persistVenOrderHistory(VenOrderHistory venOrderHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderHistorySessionEJBRemote#persistVenOrderHistoryList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderHistory> persistVenOrderHistoryList(
			List<VenOrderHistory> venOrderHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderHistorySessionEJBRemote#mergeVenOrderHistory(com.
	 * gdn.venice.persistence.VenOrderHistory)
	 */
	public VenOrderHistory mergeVenOrderHistory(VenOrderHistory venOrderHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderHistorySessionEJBRemote#mergeVenOrderHistoryList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderHistory> mergeVenOrderHistoryList(
			List<VenOrderHistory> venOrderHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderHistorySessionEJBRemote#removeVenOrderHistory(com
	 * .gdn.venice.persistence.VenOrderHistory)
	 */
	public void removeVenOrderHistory(VenOrderHistory venOrderHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderHistorySessionEJBRemote#removeVenOrderHistoryList
	 * (java.util.List)
	 */
	public void removeVenOrderHistoryList(List<VenOrderHistory> venOrderHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderHistorySessionEJBRemote#findByVenOrderHistoryLike
	 * (com.gdn.venice.persistence.VenOrderHistory, int, int)
	 */
	public List<VenOrderHistory> findByVenOrderHistoryLike(VenOrderHistory venOrderHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderHistorySessionEJBRemote#findByVenOrderHistoryLikeFR
	 * (com.gdn.venice.persistence.VenOrderHistory, int, int)
	 */
	public FinderReturn findByVenOrderHistoryLikeFR(VenOrderHistory venOrderHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
