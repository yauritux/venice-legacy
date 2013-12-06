package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderItemAdjustment;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderItemAdjustmentSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderItemAdjustment> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote#persistVenOrderItemAdjustment(com
	 * .gdn.venice.persistence.VenOrderItemAdjustment)
	 */
	public VenOrderItemAdjustment persistVenOrderItemAdjustment(VenOrderItemAdjustment venOrderItemAdjustment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote#persistVenOrderItemAdjustmentList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderItemAdjustment> persistVenOrderItemAdjustmentList(
			List<VenOrderItemAdjustment> venOrderItemAdjustmentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote#mergeVenOrderItemAdjustment(com.
	 * gdn.venice.persistence.VenOrderItemAdjustment)
	 */
	public VenOrderItemAdjustment mergeVenOrderItemAdjustment(VenOrderItemAdjustment venOrderItemAdjustment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote#mergeVenOrderItemAdjustmentList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderItemAdjustment> mergeVenOrderItemAdjustmentList(
			List<VenOrderItemAdjustment> venOrderItemAdjustmentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote#removeVenOrderItemAdjustment(com
	 * .gdn.venice.persistence.VenOrderItemAdjustment)
	 */
	public void removeVenOrderItemAdjustment(VenOrderItemAdjustment venOrderItemAdjustment);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote#removeVenOrderItemAdjustmentList
	 * (java.util.List)
	 */
	public void removeVenOrderItemAdjustmentList(List<VenOrderItemAdjustment> venOrderItemAdjustmentList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote#findByVenOrderItemAdjustmentLike
	 * (com.gdn.venice.persistence.VenOrderItemAdjustment, int, int)
	 */
	public List<VenOrderItemAdjustment> findByVenOrderItemAdjustmentLike(VenOrderItemAdjustment venOrderItemAdjustment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAdjustmentSessionEJBRemote#findByVenOrderItemAdjustmentLikeFR
	 * (com.gdn.venice.persistence.VenOrderItemAdjustment, int, int)
	 */
	public FinderReturn findByVenOrderItemAdjustmentLikeFR(VenOrderItemAdjustment venOrderItemAdjustment,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
