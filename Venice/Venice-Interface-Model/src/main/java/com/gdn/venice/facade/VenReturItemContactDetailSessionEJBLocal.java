package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenReturItemContactDetail;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenReturItemContactDetailSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemContactDetailSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenReturItemContactDetail> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemContactDetailSessionEJBRemote#persistVenReturItemContactDetail(com
	 * .gdn.venice.persistence.VenReturItemContactDetail)
	 */
	public VenReturItemContactDetail persistVenReturItemContactDetail(VenReturItemContactDetail venReturItemContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemContactDetailSessionEJBRemote#persistVenReturItemContactDetailList
	 * (java.util.List)
	 */
	public ArrayList<VenReturItemContactDetail> persistVenReturItemContactDetailList(
			List<VenReturItemContactDetail> venReturItemContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemContactDetailSessionEJBRemote#mergeVenReturItemContactDetail(com.
	 * gdn.venice.persistence.VenReturItemContactDetail)
	 */
	public VenReturItemContactDetail mergeVenReturItemContactDetail(VenReturItemContactDetail venReturItemContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemContactDetailSessionEJBRemote#mergeVenReturItemContactDetailList(
	 * java.util.List)
	 */
	public ArrayList<VenReturItemContactDetail> mergeVenReturItemContactDetailList(
			List<VenReturItemContactDetail> venReturItemContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemContactDetailSessionEJBRemote#removeVenReturItemContactDetail(com
	 * .gdn.venice.persistence.VenReturItemContactDetail)
	 */
	public void removeVenReturItemContactDetail(VenReturItemContactDetail venReturItemContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemContactDetailSessionEJBRemote#removeVenReturItemContactDetailList
	 * (java.util.List)
	 */
	public void removeVenReturItemContactDetailList(List<VenReturItemContactDetail> venReturItemContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemContactDetailSessionEJBRemote#findByVenReturItemContactDetailLike
	 * (com.gdn.venice.persistence.VenReturItemContactDetail, int, int)
	 */
	public List<VenReturItemContactDetail> findByVenReturItemContactDetailLike(VenReturItemContactDetail venReturItemContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemContactDetailSessionEJBRemote#findByVenReturItemContactDetailLikeFR
	 * (com.gdn.venice.persistence.VenReturItemContactDetail, int, int)
	 */
	public FinderReturn findByVenReturItemContactDetailLikeFR(VenReturItemContactDetail venReturItemContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
