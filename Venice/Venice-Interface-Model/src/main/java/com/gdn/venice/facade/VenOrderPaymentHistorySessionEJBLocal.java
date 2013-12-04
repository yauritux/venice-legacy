package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderPaymentHistory;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderPaymentHistorySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentHistorySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderPaymentHistory> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentHistorySessionEJBRemote#persistVenOrderPaymentHistory(com
	 * .gdn.venice.persistence.VenOrderPaymentHistory)
	 */
	public VenOrderPaymentHistory persistVenOrderPaymentHistory(VenOrderPaymentHistory venOrderPaymentHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentHistorySessionEJBRemote#persistVenOrderPaymentHistoryList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderPaymentHistory> persistVenOrderPaymentHistoryList(
			List<VenOrderPaymentHistory> venOrderPaymentHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentHistorySessionEJBRemote#mergeVenOrderPaymentHistory(com.
	 * gdn.venice.persistence.VenOrderPaymentHistory)
	 */
	public VenOrderPaymentHistory mergeVenOrderPaymentHistory(VenOrderPaymentHistory venOrderPaymentHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentHistorySessionEJBRemote#mergeVenOrderPaymentHistoryList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderPaymentHistory> mergeVenOrderPaymentHistoryList(
			List<VenOrderPaymentHistory> venOrderPaymentHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentHistorySessionEJBRemote#removeVenOrderPaymentHistory(com
	 * .gdn.venice.persistence.VenOrderPaymentHistory)
	 */
	public void removeVenOrderPaymentHistory(VenOrderPaymentHistory venOrderPaymentHistory);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentHistorySessionEJBRemote#removeVenOrderPaymentHistoryList
	 * (java.util.List)
	 */
	public void removeVenOrderPaymentHistoryList(List<VenOrderPaymentHistory> venOrderPaymentHistoryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentHistorySessionEJBRemote#findByVenOrderPaymentHistoryLike
	 * (com.gdn.venice.persistence.VenOrderPaymentHistory, int, int)
	 */
	public List<VenOrderPaymentHistory> findByVenOrderPaymentHistoryLike(VenOrderPaymentHistory venOrderPaymentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentHistorySessionEJBRemote#findByVenOrderPaymentHistoryLikeFR
	 * (com.gdn.venice.persistence.VenOrderPaymentHistory, int, int)
	 */
	public FinderReturn findByVenOrderPaymentHistoryLikeFR(VenOrderPaymentHistory venOrderPaymentHistory,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
