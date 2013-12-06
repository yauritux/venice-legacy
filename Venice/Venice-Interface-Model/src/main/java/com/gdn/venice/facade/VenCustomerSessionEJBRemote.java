package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenCustomer;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenCustomerSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenCustomer
	 */
	public List<VenCustomer> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenCustomer persists a country
	 * 
	 * @param venCustomer
	 * @return the persisted VenCustomer
	 */
	public VenCustomer persistVenCustomer(VenCustomer venCustomer);

	/**
	 * persistVenCustomerList - persists a list of VenCustomer
	 * 
	 * @param venCustomerList
	 * @return the list of persisted VenCustomer
	 */
	public ArrayList<VenCustomer> persistVenCustomerList(
			List<VenCustomer> venCustomerList);

	/**
	 * mergeVenCustomer - merges a VenCustomer
	 * 
	 * @param venCustomer
	 * @return the merged VenCustomer
	 */
	public VenCustomer mergeVenCustomer(VenCustomer venCustomer);

	/**
	 * mergeVenCustomerList - merges a list of VenCustomer
	 * 
	 * @param venCustomerList
	 * @return the merged list of VenCustomer
	 */
	public ArrayList<VenCustomer> mergeVenCustomerList(
			List<VenCustomer> venCustomerList);

	/**
	 * removeVenCustomer - removes a VenCustomer
	 * 
	 * @param venCustomer
	 */
	public void removeVenCustomer(VenCustomer venCustomer);

	/**
	 * removeVenCustomerList - removes a list of VenCustomer
	 * 
	 * @param venCustomerList
	 */
	public void removeVenCustomerList(List<VenCustomer> venCustomerList);

	/**
	 * findByVenCustomerLike - finds a list of VenCustomer Like
	 * 
	 * @param venCustomer
	 * @return the list of VenCustomer found
	 */
	public List<VenCustomer> findByVenCustomerLike(VenCustomer venCustomer,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenCustomer>LikeFR - finds a list of VenCustomer> Like with a finder return object
	 * 
	 * @param venCustomer
	 * @return the list of VenCustomer found
	 */
	public FinderReturn findByVenCustomerLikeFR(VenCustomer venCustomer,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
