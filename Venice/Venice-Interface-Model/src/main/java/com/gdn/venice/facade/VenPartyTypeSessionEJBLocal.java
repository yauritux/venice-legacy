package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenPartyType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenPartyTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenPartyType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyTypeSessionEJBRemote#persistVenPartyType(com
	 * .gdn.venice.persistence.VenPartyType)
	 */
	public VenPartyType persistVenPartyType(VenPartyType venPartyType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyTypeSessionEJBRemote#persistVenPartyTypeList
	 * (java.util.List)
	 */
	public ArrayList<VenPartyType> persistVenPartyTypeList(
			List<VenPartyType> venPartyTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyTypeSessionEJBRemote#mergeVenPartyType(com.
	 * gdn.venice.persistence.VenPartyType)
	 */
	public VenPartyType mergeVenPartyType(VenPartyType venPartyType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyTypeSessionEJBRemote#mergeVenPartyTypeList(
	 * java.util.List)
	 */
	public ArrayList<VenPartyType> mergeVenPartyTypeList(
			List<VenPartyType> venPartyTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyTypeSessionEJBRemote#removeVenPartyType(com
	 * .gdn.venice.persistence.VenPartyType)
	 */
	public void removeVenPartyType(VenPartyType venPartyType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyTypeSessionEJBRemote#removeVenPartyTypeList
	 * (java.util.List)
	 */
	public void removeVenPartyTypeList(List<VenPartyType> venPartyTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyTypeSessionEJBRemote#findByVenPartyTypeLike
	 * (com.gdn.venice.persistence.VenPartyType, int, int)
	 */
	public List<VenPartyType> findByVenPartyTypeLike(VenPartyType venPartyType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartyTypeSessionEJBRemote#findByVenPartyTypeLikeFR
	 * (com.gdn.venice.persistence.VenPartyType, int, int)
	 */
	public FinderReturn findByVenPartyTypeLikeFR(VenPartyType venPartyType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
