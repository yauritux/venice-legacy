package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderContactDetail;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderContactDetailSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderContactDetail> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote#persistVenOrderContactDetail(com
	 * .gdn.venice.persistence.VenOrderContactDetail)
	 */
	public VenOrderContactDetail persistVenOrderContactDetail(VenOrderContactDetail venOrderContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote#persistVenOrderContactDetailList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderContactDetail> persistVenOrderContactDetailList(
			List<VenOrderContactDetail> venOrderContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote#mergeVenOrderContactDetail(com.
	 * gdn.venice.persistence.VenOrderContactDetail)
	 */
	public VenOrderContactDetail mergeVenOrderContactDetail(VenOrderContactDetail venOrderContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote#mergeVenOrderContactDetailList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderContactDetail> mergeVenOrderContactDetailList(
			List<VenOrderContactDetail> venOrderContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote#removeVenOrderContactDetail(com
	 * .gdn.venice.persistence.VenOrderContactDetail)
	 */
	public void removeVenOrderContactDetail(VenOrderContactDetail venOrderContactDetail);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote#removeVenOrderContactDetailList
	 * (java.util.List)
	 */
	public void removeVenOrderContactDetailList(List<VenOrderContactDetail> venOrderContactDetailList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote#findByVenOrderContactDetailLike
	 * (com.gdn.venice.persistence.VenOrderContactDetail, int, int)
	 */
	public List<VenOrderContactDetail> findByVenOrderContactDetailLike(VenOrderContactDetail venOrderContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderContactDetailSessionEJBRemote#findByVenOrderContactDetailLikeFR
	 * (com.gdn.venice.persistence.VenOrderContactDetail, int, int)
	 */
	public FinderReturn findByVenOrderContactDetailLikeFR(VenOrderContactDetail venOrderContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
