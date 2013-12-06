package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenCustomer;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenCustomerSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCustomerSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenCustomer> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCustomerSessionEJBRemote#persistVenCustomer(com
	 * .gdn.venice.persistence.VenCustomer)
	 */
	public VenCustomer persistVenCustomer(VenCustomer venCustomer);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCustomerSessionEJBRemote#persistVenCustomerList
	 * (java.util.List)
	 */
	public ArrayList<VenCustomer> persistVenCustomerList(
			List<VenCustomer> venCustomerList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCustomerSessionEJBRemote#mergeVenCustomer(com.
	 * gdn.venice.persistence.VenCustomer)
	 */
	public VenCustomer mergeVenCustomer(VenCustomer venCustomer);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCustomerSessionEJBRemote#mergeVenCustomerList(
	 * java.util.List)
	 */
	public ArrayList<VenCustomer> mergeVenCustomerList(
			List<VenCustomer> venCustomerList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCustomerSessionEJBRemote#removeVenCustomer(com
	 * .gdn.venice.persistence.VenCustomer)
	 */
	public void removeVenCustomer(VenCustomer venCustomer);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCustomerSessionEJBRemote#removeVenCustomerList
	 * (java.util.List)
	 */
	public void removeVenCustomerList(List<VenCustomer> venCustomerList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCustomerSessionEJBRemote#findByVenCustomerLike
	 * (com.gdn.venice.persistence.VenCustomer, int, int)
	 */
	public List<VenCustomer> findByVenCustomerLike(VenCustomer venCustomer,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCustomerSessionEJBRemote#findByVenCustomerLikeFR
	 * (com.gdn.venice.persistence.VenCustomer, int, int)
	 */
	public FinderReturn findByVenCustomerLikeFR(VenCustomer venCustomer,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
