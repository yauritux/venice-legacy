package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderPaymentAllocation;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderPaymentAllocationSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderPaymentAllocation> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote#persistVenOrderPaymentAllocation(com
	 * .gdn.venice.persistence.VenOrderPaymentAllocation)
	 */
	public VenOrderPaymentAllocation persistVenOrderPaymentAllocation(VenOrderPaymentAllocation venOrderPaymentAllocation);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote#persistVenOrderPaymentAllocationList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderPaymentAllocation> persistVenOrderPaymentAllocationList(
			List<VenOrderPaymentAllocation> venOrderPaymentAllocationList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote#mergeVenOrderPaymentAllocation(com.
	 * gdn.venice.persistence.VenOrderPaymentAllocation)
	 */
	public VenOrderPaymentAllocation mergeVenOrderPaymentAllocation(VenOrderPaymentAllocation venOrderPaymentAllocation);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote#mergeVenOrderPaymentAllocationList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderPaymentAllocation> mergeVenOrderPaymentAllocationList(
			List<VenOrderPaymentAllocation> venOrderPaymentAllocationList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote#removeVenOrderPaymentAllocation(com
	 * .gdn.venice.persistence.VenOrderPaymentAllocation)
	 */
	public void removeVenOrderPaymentAllocation(VenOrderPaymentAllocation venOrderPaymentAllocation);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote#removeVenOrderPaymentAllocationList
	 * (java.util.List)
	 */
	public void removeVenOrderPaymentAllocationList(List<VenOrderPaymentAllocation> venOrderPaymentAllocationList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote#findByVenOrderPaymentAllocationLike
	 * (com.gdn.venice.persistence.VenOrderPaymentAllocation, int, int)
	 */
	public List<VenOrderPaymentAllocation> findByVenOrderPaymentAllocationLike(VenOrderPaymentAllocation venOrderPaymentAllocation,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAllocationSessionEJBRemote#findByVenOrderPaymentAllocationLikeFR
	 * (com.gdn.venice.persistence.VenOrderPaymentAllocation, int, int)
	 */
	public FinderReturn findByVenOrderPaymentAllocationLikeFR(VenOrderPaymentAllocation venOrderPaymentAllocation,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
