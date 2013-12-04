package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderAddressSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderAddressSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderAddressSessionEJBRemote#persistVenOrderAddress(com
	 * .gdn.venice.persistence.VenOrderAddress)
	 */
	public VenOrderAddress persistVenOrderAddress(VenOrderAddress venOrderAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderAddressSessionEJBRemote#persistVenOrderAddressList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderAddress> persistVenOrderAddressList(
			List<VenOrderAddress> venOrderAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderAddressSessionEJBRemote#mergeVenOrderAddress(com.
	 * gdn.venice.persistence.VenOrderAddress)
	 */
	public VenOrderAddress mergeVenOrderAddress(VenOrderAddress venOrderAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderAddressSessionEJBRemote#mergeVenOrderAddressList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderAddress> mergeVenOrderAddressList(
			List<VenOrderAddress> venOrderAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderAddressSessionEJBRemote#removeVenOrderAddress(com
	 * .gdn.venice.persistence.VenOrderAddress)
	 */
	public void removeVenOrderAddress(VenOrderAddress venOrderAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderAddressSessionEJBRemote#removeVenOrderAddressList
	 * (java.util.List)
	 */
	public void removeVenOrderAddressList(List<VenOrderAddress> venOrderAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderAddressSessionEJBRemote#findByVenOrderAddressLike
	 * (com.gdn.venice.persistence.VenOrderAddress, int, int)
	 */
	public List<VenOrderAddress> findByVenOrderAddressLike(VenOrderAddress venOrderAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderAddressSessionEJBRemote#findByVenOrderAddressLikeFR
	 * (com.gdn.venice.persistence.VenOrderAddress, int, int)
	 */
	public FinderReturn findByVenOrderAddressLikeFR(VenOrderAddress venOrderAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
