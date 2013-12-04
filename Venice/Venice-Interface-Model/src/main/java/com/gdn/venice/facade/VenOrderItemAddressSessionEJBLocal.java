package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderItemAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderItemAddressSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderItemAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote#persistVenOrderItemAddress(com
	 * .gdn.venice.persistence.VenOrderItemAddress)
	 */
	public VenOrderItemAddress persistVenOrderItemAddress(VenOrderItemAddress venOrderItemAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote#persistVenOrderItemAddressList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderItemAddress> persistVenOrderItemAddressList(
			List<VenOrderItemAddress> venOrderItemAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote#mergeVenOrderItemAddress(com.
	 * gdn.venice.persistence.VenOrderItemAddress)
	 */
	public VenOrderItemAddress mergeVenOrderItemAddress(VenOrderItemAddress venOrderItemAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote#mergeVenOrderItemAddressList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderItemAddress> mergeVenOrderItemAddressList(
			List<VenOrderItemAddress> venOrderItemAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote#removeVenOrderItemAddress(com
	 * .gdn.venice.persistence.VenOrderItemAddress)
	 */
	public void removeVenOrderItemAddress(VenOrderItemAddress venOrderItemAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote#removeVenOrderItemAddressList
	 * (java.util.List)
	 */
	public void removeVenOrderItemAddressList(List<VenOrderItemAddress> venOrderItemAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote#findByVenOrderItemAddressLike
	 * (com.gdn.venice.persistence.VenOrderItemAddress, int, int)
	 */
	public List<VenOrderItemAddress> findByVenOrderItemAddressLike(VenOrderItemAddress venOrderItemAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderItemAddressSessionEJBRemote#findByVenOrderItemAddressLikeFR
	 * (com.gdn.venice.persistence.VenOrderItemAddress, int, int)
	 */
	public FinderReturn findByVenOrderItemAddressLikeFR(VenOrderItemAddress venOrderItemAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
