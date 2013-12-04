package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenPartyAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenPartyAddressSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyAddressSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenPartyAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyAddressSessionEJBRemote#persistVenPartyAddress(com
	 * .gdn.venice.persistence.VenPartyAddress)
	 */
	public VenPartyAddress persistVenPartyAddress(VenPartyAddress venPartyAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyAddressSessionEJBRemote#persistVenPartyAddressList
	 * (java.util.List)
	 */
	public ArrayList<VenPartyAddress> persistVenPartyAddressList(
			List<VenPartyAddress> venPartyAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyAddressSessionEJBRemote#mergeVenPartyAddress(com.
	 * gdn.venice.persistence.VenPartyAddress)
	 */
	public VenPartyAddress mergeVenPartyAddress(VenPartyAddress venPartyAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyAddressSessionEJBRemote#mergeVenPartyAddressList(
	 * java.util.List)
	 */
	public ArrayList<VenPartyAddress> mergeVenPartyAddressList(
			List<VenPartyAddress> venPartyAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyAddressSessionEJBRemote#removeVenPartyAddress(com
	 * .gdn.venice.persistence.VenPartyAddress)
	 */
	public void removeVenPartyAddress(VenPartyAddress venPartyAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyAddressSessionEJBRemote#removeVenPartyAddressList
	 * (java.util.List)
	 */
	public void removeVenPartyAddressList(List<VenPartyAddress> venPartyAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyAddressSessionEJBRemote#findByVenPartyAddressLike
	 * (com.gdn.venice.persistence.VenPartyAddress, int, int)
	 */
	public List<VenPartyAddress> findByVenPartyAddressLike(VenPartyAddress venPartyAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyAddressSessionEJBRemote#findByVenPartyAddressLikeFR
	 * (com.gdn.venice.persistence.VenPartyAddress, int, int)
	 */
	public FinderReturn findByVenPartyAddressLikeFR(VenPartyAddress venPartyAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
