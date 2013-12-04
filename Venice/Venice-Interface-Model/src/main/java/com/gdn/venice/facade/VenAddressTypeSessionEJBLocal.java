package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenAddressType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenAddressTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenAddressType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressTypeSessionEJBRemote#persistVenAddressType(com
	 * .gdn.venice.persistence.VenAddressType)
	 */
	public VenAddressType persistVenAddressType(VenAddressType venAddressType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressTypeSessionEJBRemote#persistVenAddressTypeList
	 * (java.util.List)
	 */
	public ArrayList<VenAddressType> persistVenAddressTypeList(
			List<VenAddressType> venAddressTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressTypeSessionEJBRemote#mergeVenAddressType(com.
	 * gdn.venice.persistence.VenAddressType)
	 */
	public VenAddressType mergeVenAddressType(VenAddressType venAddressType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressTypeSessionEJBRemote#mergeVenAddressTypeList(
	 * java.util.List)
	 */
	public ArrayList<VenAddressType> mergeVenAddressTypeList(
			List<VenAddressType> venAddressTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressTypeSessionEJBRemote#removeVenAddressType(com
	 * .gdn.venice.persistence.VenAddressType)
	 */
	public void removeVenAddressType(VenAddressType venAddressType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressTypeSessionEJBRemote#removeVenAddressTypeList
	 * (java.util.List)
	 */
	public void removeVenAddressTypeList(List<VenAddressType> venAddressTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressTypeSessionEJBRemote#findByVenAddressTypeLike
	 * (com.gdn.venice.persistence.VenAddressType, int, int)
	 */
	public List<VenAddressType> findByVenAddressTypeLike(VenAddressType venAddressType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenAddressTypeSessionEJBRemote#findByVenAddressTypeLikeFR
	 * (com.gdn.venice.persistence.VenAddressType, int, int)
	 */
	public FinderReturn findByVenAddressTypeLikeFR(VenAddressType venAddressType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
