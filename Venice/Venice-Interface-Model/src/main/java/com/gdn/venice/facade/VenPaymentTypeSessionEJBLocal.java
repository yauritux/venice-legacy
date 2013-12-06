package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenPaymentType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenPaymentTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenPaymentType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentTypeSessionEJBRemote#persistVenPaymentType(com
	 * .gdn.venice.persistence.VenPaymentType)
	 */
	public VenPaymentType persistVenPaymentType(VenPaymentType venPaymentType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentTypeSessionEJBRemote#persistVenPaymentTypeList
	 * (java.util.List)
	 */
	public ArrayList<VenPaymentType> persistVenPaymentTypeList(
			List<VenPaymentType> venPaymentTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentTypeSessionEJBRemote#mergeVenPaymentType(com.
	 * gdn.venice.persistence.VenPaymentType)
	 */
	public VenPaymentType mergeVenPaymentType(VenPaymentType venPaymentType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentTypeSessionEJBRemote#mergeVenPaymentTypeList(
	 * java.util.List)
	 */
	public ArrayList<VenPaymentType> mergeVenPaymentTypeList(
			List<VenPaymentType> venPaymentTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentTypeSessionEJBRemote#removeVenPaymentType(com
	 * .gdn.venice.persistence.VenPaymentType)
	 */
	public void removeVenPaymentType(VenPaymentType venPaymentType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentTypeSessionEJBRemote#removeVenPaymentTypeList
	 * (java.util.List)
	 */
	public void removeVenPaymentTypeList(List<VenPaymentType> venPaymentTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentTypeSessionEJBRemote#findByVenPaymentTypeLike
	 * (com.gdn.venice.persistence.VenPaymentType, int, int)
	 */
	public List<VenPaymentType> findByVenPaymentTypeLike(VenPaymentType venPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPaymentTypeSessionEJBRemote#findByVenPaymentTypeLikeFR
	 * (com.gdn.venice.persistence.VenPaymentType, int, int)
	 */
	public FinderReturn findByVenPaymentTypeLikeFR(VenPaymentType venPaymentType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
