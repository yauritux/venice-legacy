package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenContactDetailType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenContactDetailTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenContactDetailType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote#persistVenContactDetailType(com
	 * .gdn.venice.persistence.VenContactDetailType)
	 */
	public VenContactDetailType persistVenContactDetailType(VenContactDetailType venContactDetailType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote#persistVenContactDetailTypeList
	 * (java.util.List)
	 */
	public ArrayList<VenContactDetailType> persistVenContactDetailTypeList(
			List<VenContactDetailType> venContactDetailTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote#mergeVenContactDetailType(com.
	 * gdn.venice.persistence.VenContactDetailType)
	 */
	public VenContactDetailType mergeVenContactDetailType(VenContactDetailType venContactDetailType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote#mergeVenContactDetailTypeList(
	 * java.util.List)
	 */
	public ArrayList<VenContactDetailType> mergeVenContactDetailTypeList(
			List<VenContactDetailType> venContactDetailTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote#removeVenContactDetailType(com
	 * .gdn.venice.persistence.VenContactDetailType)
	 */
	public void removeVenContactDetailType(VenContactDetailType venContactDetailType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote#removeVenContactDetailTypeList
	 * (java.util.List)
	 */
	public void removeVenContactDetailTypeList(List<VenContactDetailType> venContactDetailTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote#findByVenContactDetailTypeLike
	 * (com.gdn.venice.persistence.VenContactDetailType, int, int)
	 */
	public List<VenContactDetailType> findByVenContactDetailTypeLike(VenContactDetailType venContactDetailType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenContactDetailTypeSessionEJBRemote#findByVenContactDetailTypeLikeFR
	 * (com.gdn.venice.persistence.VenContactDetailType, int, int)
	 */
	public FinderReturn findByVenContactDetailTypeLikeFR(VenContactDetailType venContactDetailType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
