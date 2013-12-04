package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenPaymentStatus;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenPaymentStatusSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentStatusSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenPaymentStatus> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentStatusSessionEJBRemote#persistVenPaymentStatus(com
	 * .gdn.venice.persistence.VenPaymentStatus)
	 */
	public VenPaymentStatus persistVenPaymentStatus(VenPaymentStatus venPaymentStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentStatusSessionEJBRemote#persistVenPaymentStatusList
	 * (java.util.List)
	 */
	public ArrayList<VenPaymentStatus> persistVenPaymentStatusList(
			List<VenPaymentStatus> venPaymentStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentStatusSessionEJBRemote#mergeVenPaymentStatus(com.
	 * gdn.venice.persistence.VenPaymentStatus)
	 */
	public VenPaymentStatus mergeVenPaymentStatus(VenPaymentStatus venPaymentStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentStatusSessionEJBRemote#mergeVenPaymentStatusList(
	 * java.util.List)
	 */
	public ArrayList<VenPaymentStatus> mergeVenPaymentStatusList(
			List<VenPaymentStatus> venPaymentStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentStatusSessionEJBRemote#removeVenPaymentStatus(com
	 * .gdn.venice.persistence.VenPaymentStatus)
	 */
	public void removeVenPaymentStatus(VenPaymentStatus venPaymentStatus);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentStatusSessionEJBRemote#removeVenPaymentStatusList
	 * (java.util.List)
	 */
	public void removeVenPaymentStatusList(List<VenPaymentStatus> venPaymentStatusList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentStatusSessionEJBRemote#findByVenPaymentStatusLike
	 * (com.gdn.venice.persistence.VenPaymentStatus, int, int)
	 */
	public List<VenPaymentStatus> findByVenPaymentStatusLike(VenPaymentStatus venPaymentStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentStatusSessionEJBRemote#findByVenPaymentStatusLikeFR
	 * (com.gdn.venice.persistence.VenPaymentStatus, int, int)
	 */
	public FinderReturn findByVenPaymentStatusLikeFR(VenPaymentStatus venPaymentStatus,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
