package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenProductType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenProductTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenProductType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductTypeSessionEJBRemote#persistVenProductType(com
	 * .gdn.venice.persistence.VenProductType)
	 */
	public VenProductType persistVenProductType(VenProductType venProductType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductTypeSessionEJBRemote#persistVenProductTypeList
	 * (java.util.List)
	 */
	public ArrayList<VenProductType> persistVenProductTypeList(
			List<VenProductType> venProductTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductTypeSessionEJBRemote#mergeVenProductType(com.
	 * gdn.venice.persistence.VenProductType)
	 */
	public VenProductType mergeVenProductType(VenProductType venProductType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductTypeSessionEJBRemote#mergeVenProductTypeList(
	 * java.util.List)
	 */
	public ArrayList<VenProductType> mergeVenProductTypeList(
			List<VenProductType> venProductTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductTypeSessionEJBRemote#removeVenProductType(com
	 * .gdn.venice.persistence.VenProductType)
	 */
	public void removeVenProductType(VenProductType venProductType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductTypeSessionEJBRemote#removeVenProductTypeList
	 * (java.util.List)
	 */
	public void removeVenProductTypeList(List<VenProductType> venProductTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductTypeSessionEJBRemote#findByVenProductTypeLike
	 * (com.gdn.venice.persistence.VenProductType, int, int)
	 */
	public List<VenProductType> findByVenProductTypeLike(VenProductType venProductType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenProductTypeSessionEJBRemote#findByVenProductTypeLikeFR
	 * (com.gdn.venice.persistence.VenProductType, int, int)
	 */
	public FinderReturn findByVenProductTypeLikeFR(VenProductType venProductType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
