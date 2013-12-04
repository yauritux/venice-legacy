package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenReturContactDetail;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenReturContactDetailSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturContactDetailSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenReturContactDetail> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturContactDetailSessionEJBRemote#persistVenReturContactDetail(com
	 * .gdn.venice.persistence.VenReturContactDetail)
	 */
	public VenReturContactDetail persistVenReturContactDetail(VenReturContactDetail venReturContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturContactDetailSessionEJBRemote#persistVenReturContactDetailList
	 * (java.util.List)
	 */
	public ArrayList<VenReturContactDetail> persistVenReturContactDetailList(
			List<VenReturContactDetail> venReturContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturContactDetailSessionEJBRemote#mergeVenReturContactDetail(com.
	 * gdn.venice.persistence.VenReturContactDetail)
	 */
	public VenReturContactDetail mergeVenReturContactDetail(VenReturContactDetail venReturContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturContactDetailSessionEJBRemote#mergeVenReturContactDetailList(
	 * java.util.List)
	 */
	public ArrayList<VenReturContactDetail> mergeVenReturContactDetailList(
			List<VenReturContactDetail> venReturContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturContactDetailSessionEJBRemote#removeVenReturContactDetail(com
	 * .gdn.venice.persistence.VenReturContactDetail)
	 */
	public void removeVenReturContactDetail(VenReturContactDetail venReturContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturContactDetailSessionEJBRemote#removeVenReturContactDetailList
	 * (java.util.List)
	 */
	public void removeVenReturContactDetailList(List<VenReturContactDetail> venReturContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturContactDetailSessionEJBRemote#findByVenReturContactDetailLike
	 * (com.gdn.venice.persistence.VenReturContactDetail, int, int)
	 */
	public List<VenReturContactDetail> findByVenReturContactDetailLike(VenReturContactDetail venReturContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturContactDetailSessionEJBRemote#findByVenReturContactDetailLikeFR
	 * (com.gdn.venice.persistence.VenReturContactDetail, int, int)
	 */
	public FinderReturn findByVenReturContactDetailLikeFR(VenReturContactDetail venReturContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
