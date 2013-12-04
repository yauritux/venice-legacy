package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenReturAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenReturAddressSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturAddressSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenReturAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturAddressSessionEJBRemote#persistVenReturAddress(com
	 * .gdn.venice.persistence.VenReturAddress)
	 */
	public VenReturAddress persistVenReturAddress(VenReturAddress venReturAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturAddressSessionEJBRemote#persistVenReturAddressList
	 * (java.util.List)
	 */
	public ArrayList<VenReturAddress> persistVenReturAddressList(
			List<VenReturAddress> venReturAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturAddressSessionEJBRemote#mergeVenReturAddress(com.
	 * gdn.venice.persistence.VenReturAddress)
	 */
	public VenReturAddress mergeVenReturAddress(VenReturAddress venReturAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturAddressSessionEJBRemote#mergeVenReturAddressList(
	 * java.util.List)
	 */
	public ArrayList<VenReturAddress> mergeVenReturAddressList(
			List<VenReturAddress> venReturAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturAddressSessionEJBRemote#removeVenReturAddress(com
	 * .gdn.venice.persistence.VenReturAddress)
	 */
	public void removeVenReturAddress(VenReturAddress venReturAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturAddressSessionEJBRemote#removeVenReturAddressList
	 * (java.util.List)
	 */
	public void removeVenReturAddressList(List<VenReturAddress> venReturAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturAddressSessionEJBRemote#findByVenReturAddressLike
	 * (com.gdn.venice.persistence.VenReturAddress, int, int)
	 */
	public List<VenReturAddress> findByVenReturAddressLike(VenReturAddress venReturAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturAddressSessionEJBRemote#findByVenReturAddressLikeFR
	 * (com.gdn.venice.persistence.VenReturAddress, int, int)
	 */
	public FinderReturn findByVenReturAddressLikeFR(VenReturAddress venReturAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
