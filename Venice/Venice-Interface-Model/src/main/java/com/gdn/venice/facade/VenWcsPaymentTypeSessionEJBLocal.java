package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenWcsPaymentType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenWcsPaymentTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenWcsPaymentTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenWcsPaymentType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenWcsPaymentTypeSessionEJBRemote#persistVenWcsPaymentType(com
	 * .gdn.venice.persistence.VenWcsPaymentType)
	 */
	public VenWcsPaymentType persistVenWcsPaymentType(VenWcsPaymentType venWcsPaymentType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenWcsPaymentTypeSessionEJBRemote#persistVenWcsPaymentTypeList
	 * (java.util.List)
	 */
	public ArrayList<VenWcsPaymentType> persistVenWcsPaymentTypeList(
			List<VenWcsPaymentType> venWcsPaymentTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenWcsPaymentTypeSessionEJBRemote#mergeVenWcsPaymentType(com.
	 * gdn.venice.persistence.VenWcsPaymentType)
	 */
	public VenWcsPaymentType mergeVenWcsPaymentType(VenWcsPaymentType venWcsPaymentType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenWcsPaymentTypeSessionEJBRemote#mergeVenWcsPaymentTypeList(
	 * java.util.List)
	 */
	public ArrayList<VenWcsPaymentType> mergeVenWcsPaymentTypeList(
			List<VenWcsPaymentType> venWcsPaymentTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenWcsPaymentTypeSessionEJBRemote#removeVenWcsPaymentType(com
	 * .gdn.venice.persistence.VenWcsPaymentType)
	 */
	public void removeVenWcsPaymentType(VenWcsPaymentType venWcsPaymentType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenWcsPaymentTypeSessionEJBRemote#removeVenWcsPaymentTypeList
	 * (java.util.List)
	 */
	public void removeVenWcsPaymentTypeList(List<VenWcsPaymentType> venWcsPaymentTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenWcsPaymentTypeSessionEJBRemote#findByVenWcsPaymentTypeLike
	 * (com.gdn.venice.persistence.VenWcsPaymentType, int, int)
	 */
	public List<VenWcsPaymentType> findByVenWcsPaymentTypeLike(VenWcsPaymentType venWcsPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenWcsPaymentTypeSessionEJBRemote#findByVenWcsPaymentTypeLikeFR
	 * (com.gdn.venice.persistence.VenWcsPaymentType, int, int)
	 */
	public FinderReturn findByVenWcsPaymentTypeLikeFR(VenWcsPaymentType venWcsPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
