package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderItemContactDetail;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderItemContactDetailSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderItemContactDetail> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote#persistVenOrderItemContactDetail(com
	 * .gdn.venice.persistence.VenOrderItemContactDetail)
	 */
	public VenOrderItemContactDetail persistVenOrderItemContactDetail(VenOrderItemContactDetail venOrderItemContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote#persistVenOrderItemContactDetailList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderItemContactDetail> persistVenOrderItemContactDetailList(
			List<VenOrderItemContactDetail> venOrderItemContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote#mergeVenOrderItemContactDetail(com.
	 * gdn.venice.persistence.VenOrderItemContactDetail)
	 */
	public VenOrderItemContactDetail mergeVenOrderItemContactDetail(VenOrderItemContactDetail venOrderItemContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote#mergeVenOrderItemContactDetailList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderItemContactDetail> mergeVenOrderItemContactDetailList(
			List<VenOrderItemContactDetail> venOrderItemContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote#removeVenOrderItemContactDetail(com
	 * .gdn.venice.persistence.VenOrderItemContactDetail)
	 */
	public void removeVenOrderItemContactDetail(VenOrderItemContactDetail venOrderItemContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote#removeVenOrderItemContactDetailList
	 * (java.util.List)
	 */
	public void removeVenOrderItemContactDetailList(List<VenOrderItemContactDetail> venOrderItemContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote#findByVenOrderItemContactDetailLike
	 * (com.gdn.venice.persistence.VenOrderItemContactDetail, int, int)
	 */
	public List<VenOrderItemContactDetail> findByVenOrderItemContactDetailLike(VenOrderItemContactDetail venOrderItemContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemContactDetailSessionEJBRemote#findByVenOrderItemContactDetailLikeFR
	 * (com.gdn.venice.persistence.VenOrderItemContactDetail, int, int)
	 */
	public FinderReturn findByVenOrderItemContactDetailLikeFR(VenOrderItemContactDetail venOrderItemContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
