package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenReturItemAddress;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenReturItemAddressSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemAddressSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenReturItemAddress> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemAddressSessionEJBRemote#persistVenReturItemAddress(com
	 * .gdn.venice.persistence.VenReturItemAddress)
	 */
	public VenReturItemAddress persistVenReturItemAddress(VenReturItemAddress venReturItemAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemAddressSessionEJBRemote#persistVenReturItemAddressList
	 * (java.util.List)
	 */
	public ArrayList<VenReturItemAddress> persistVenReturItemAddressList(
			List<VenReturItemAddress> venReturItemAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemAddressSessionEJBRemote#mergeVenReturItemAddress(com.
	 * gdn.venice.persistence.VenReturItemAddress)
	 */
	public VenReturItemAddress mergeVenReturItemAddress(VenReturItemAddress venReturItemAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemAddressSessionEJBRemote#mergeVenReturItemAddressList(
	 * java.util.List)
	 */
	public ArrayList<VenReturItemAddress> mergeVenReturItemAddressList(
			List<VenReturItemAddress> venReturItemAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemAddressSessionEJBRemote#removeVenReturItemAddress(com
	 * .gdn.venice.persistence.VenReturItemAddress)
	 */
	public void removeVenReturItemAddress(VenReturItemAddress venReturItemAddress);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemAddressSessionEJBRemote#removeVenReturItemAddressList
	 * (java.util.List)
	 */
	public void removeVenReturItemAddressList(List<VenReturItemAddress> venReturItemAddressList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemAddressSessionEJBRemote#findByVenReturItemAddressLike
	 * (com.gdn.venice.persistence.VenReturItemAddress, int, int)
	 */
	public List<VenReturItemAddress> findByVenReturItemAddressLike(VenReturItemAddress venReturItemAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenReturItemAddressSessionEJBRemote#findByVenReturItemAddressLikeFR
	 * (com.gdn.venice.persistence.VenReturItemAddress, int, int)
	 */
	public FinderReturn findByVenReturItemAddressLikeFR(VenReturItemAddress venReturItemAddress,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
