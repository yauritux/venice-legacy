package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderStatusHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderStatusHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderStatusHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote#persistVenOrderStatusHistory(com
	 * .gdn.venice.persistence.VenOrderStatusHistory)
	 */
	public VenOrderStatusHistory persistVenOrderStatusHistory(VenOrderStatusHistory venOrderStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote#persistVenOrderStatusHistoryList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderStatusHistory> persistVenOrderStatusHistoryList(
			List<VenOrderStatusHistory> venOrderStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote#mergeVenOrderStatusHistory(com.
	 * gdn.venice.persistence.VenOrderStatusHistory)
	 */
	public VenOrderStatusHistory mergeVenOrderStatusHistory(VenOrderStatusHistory venOrderStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote#mergeVenOrderStatusHistoryList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderStatusHistory> mergeVenOrderStatusHistoryList(
			List<VenOrderStatusHistory> venOrderStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote#removeVenOrderStatusHistory(com
	 * .gdn.venice.persistence.VenOrderStatusHistory)
	 */
	public void removeVenOrderStatusHistory(VenOrderStatusHistory venOrderStatusHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote#removeVenOrderStatusHistoryList
	 * (java.util.List)
	 */
	public void removeVenOrderStatusHistoryList(List<VenOrderStatusHistory> venOrderStatusHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote#findByVenOrderStatusHistoryLike
	 * (com.gdn.venice.persistence.VenOrderStatusHistory, int, int)
	 */
	public List<VenOrderStatusHistory> findByVenOrderStatusHistoryLike(VenOrderStatusHistory venOrderStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusHistorySessionEJBRemote#findByVenOrderStatusHistoryLikeFR
	 * (com.gdn.venice.persistence.VenOrderStatusHistory, int, int)
	 */
	public FinderReturn findByVenOrderStatusHistoryLikeFR(VenOrderStatusHistory venOrderStatusHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
