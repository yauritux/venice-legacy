package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenContactDetail;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenContactDetailSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenContactDetail> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailSessionEJBRemote#persistVenContactDetail(com
	 * .gdn.venice.persistence.VenContactDetail)
	 */
	public VenContactDetail persistVenContactDetail(VenContactDetail venContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailSessionEJBRemote#persistVenContactDetailList
	 * (java.util.List)
	 */
	public ArrayList<VenContactDetail> persistVenContactDetailList(
			List<VenContactDetail> venContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailSessionEJBRemote#mergeVenContactDetail(com.
	 * gdn.venice.persistence.VenContactDetail)
	 */
	public VenContactDetail mergeVenContactDetail(VenContactDetail venContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailSessionEJBRemote#mergeVenContactDetailList(
	 * java.util.List)
	 */
	public ArrayList<VenContactDetail> mergeVenContactDetailList(
			List<VenContactDetail> venContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailSessionEJBRemote#removeVenContactDetail(com
	 * .gdn.venice.persistence.VenContactDetail)
	 */
	public void removeVenContactDetail(VenContactDetail venContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailSessionEJBRemote#removeVenContactDetailList
	 * (java.util.List)
	 */
	public void removeVenContactDetailList(List<VenContactDetail> venContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailSessionEJBRemote#findByVenContactDetailLike
	 * (com.gdn.venice.persistence.VenContactDetail, int, int)
	 */
	public List<VenContactDetail> findByVenContactDetailLike(VenContactDetail venContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailSessionEJBRemote#findByVenContactDetailLikeFR
	 * (com.gdn.venice.persistence.VenContactDetail, int, int)
	 */
	public FinderReturn findByVenContactDetailLikeFR(VenContactDetail venContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
