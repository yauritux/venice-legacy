package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderStatusSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusSessionEJBRemote#persistVenOrderStatus(com
	 * .gdn.venice.persistence.VenOrderStatus)
	 */
	public VenOrderStatus persistVenOrderStatus(VenOrderStatus venOrderStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusSessionEJBRemote#persistVenOrderStatusList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderStatus> persistVenOrderStatusList(
			List<VenOrderStatus> venOrderStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusSessionEJBRemote#mergeVenOrderStatus(com.
	 * gdn.venice.persistence.VenOrderStatus)
	 */
	public VenOrderStatus mergeVenOrderStatus(VenOrderStatus venOrderStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusSessionEJBRemote#mergeVenOrderStatusList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderStatus> mergeVenOrderStatusList(
			List<VenOrderStatus> venOrderStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusSessionEJBRemote#removeVenOrderStatus(com
	 * .gdn.venice.persistence.VenOrderStatus)
	 */
	public void removeVenOrderStatus(VenOrderStatus venOrderStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusSessionEJBRemote#removeVenOrderStatusList
	 * (java.util.List)
	 */
	public void removeVenOrderStatusList(List<VenOrderStatus> venOrderStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusSessionEJBRemote#findByVenOrderStatusLike
	 * (com.gdn.venice.persistence.VenOrderStatus, int, int)
	 */
	public List<VenOrderStatus> findByVenOrderStatusLike(VenOrderStatus venOrderStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderStatusSessionEJBRemote#findByVenOrderStatusLikeFR
	 * (com.gdn.venice.persistence.VenOrderStatus, int, int)
	 */
	public FinderReturn findByVenOrderStatusLikeFR(VenOrderStatus venOrderStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
