package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenAddressSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressSessionEJBRemote#persistVenAddress(com
	 * .gdn.venice.persistence.VenAddress)
	 */
	public VenAddress persistVenAddress(VenAddress venAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressSessionEJBRemote#persistVenAddressList
	 * (java.util.List)
	 */
	public ArrayList<VenAddress> persistVenAddressList(
			List<VenAddress> venAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressSessionEJBRemote#mergeVenAddress(com.
	 * gdn.venice.persistence.VenAddress)
	 */
	public VenAddress mergeVenAddress(VenAddress venAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressSessionEJBRemote#mergeVenAddressList(
	 * java.util.List)
	 */
	public ArrayList<VenAddress> mergeVenAddressList(
			List<VenAddress> venAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressSessionEJBRemote#removeVenAddress(com
	 * .gdn.venice.persistence.VenAddress)
	 */
	public void removeVenAddress(VenAddress venAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressSessionEJBRemote#removeVenAddressList
	 * (java.util.List)
	 */
	public void removeVenAddressList(List<VenAddress> venAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressSessionEJBRemote#findByVenAddressLike
	 * (com.gdn.venice.persistence.VenAddress, int, int)
	 */
	public List<VenAddress> findByVenAddressLike(VenAddress venAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressSessionEJBRemote#findByVenAddressLikeFR
	 * (com.gdn.venice.persistence.VenAddress, int, int)
	 */
	public FinderReturn findByVenAddressLikeFR(VenAddress venAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
