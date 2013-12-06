package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenFraudCheckStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenFraudCheckStatusSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenFraudCheckStatusSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenFraudCheckStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenFraudCheckStatusSessionEJBRemote#persistVenFraudCheckStatus(com
	 * .gdn.venice.persistence.VenFraudCheckStatus)
	 */
	public VenFraudCheckStatus persistVenFraudCheckStatus(VenFraudCheckStatus venFraudCheckStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenFraudCheckStatusSessionEJBRemote#persistVenFraudCheckStatusList
	 * (java.util.List)
	 */
	public ArrayList<VenFraudCheckStatus> persistVenFraudCheckStatusList(
			List<VenFraudCheckStatus> venFraudCheckStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenFraudCheckStatusSessionEJBRemote#mergeVenFraudCheckStatus(com.
	 * gdn.venice.persistence.VenFraudCheckStatus)
	 */
	public VenFraudCheckStatus mergeVenFraudCheckStatus(VenFraudCheckStatus venFraudCheckStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenFraudCheckStatusSessionEJBRemote#mergeVenFraudCheckStatusList(
	 * java.util.List)
	 */
	public ArrayList<VenFraudCheckStatus> mergeVenFraudCheckStatusList(
			List<VenFraudCheckStatus> venFraudCheckStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenFraudCheckStatusSessionEJBRemote#removeVenFraudCheckStatus(com
	 * .gdn.venice.persistence.VenFraudCheckStatus)
	 */
	public void removeVenFraudCheckStatus(VenFraudCheckStatus venFraudCheckStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenFraudCheckStatusSessionEJBRemote#removeVenFraudCheckStatusList
	 * (java.util.List)
	 */
	public void removeVenFraudCheckStatusList(List<VenFraudCheckStatus> venFraudCheckStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenFraudCheckStatusSessionEJBRemote#findByVenFraudCheckStatusLike
	 * (com.gdn.venice.persistence.VenFraudCheckStatus, int, int)
	 */
	public List<VenFraudCheckStatus> findByVenFraudCheckStatusLike(VenFraudCheckStatus venFraudCheckStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenFraudCheckStatusSessionEJBRemote#findByVenFraudCheckStatusLikeFR
	 * (com.gdn.venice.persistence.VenFraudCheckStatus, int, int)
	 */
	public FinderReturn findByVenFraudCheckStatusLikeFR(VenFraudCheckStatus venFraudCheckStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
