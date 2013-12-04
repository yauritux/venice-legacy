package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenMasterChangeType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenMasterChangeTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMasterChangeTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenMasterChangeType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMasterChangeTypeSessionEJBRemote#persistVenMasterChangeType(com
	 * .gdn.venice.persistence.VenMasterChangeType)
	 */
	public VenMasterChangeType persistVenMasterChangeType(VenMasterChangeType venMasterChangeType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMasterChangeTypeSessionEJBRemote#persistVenMasterChangeTypeList
	 * (java.util.List)
	 */
	public ArrayList<VenMasterChangeType> persistVenMasterChangeTypeList(
			List<VenMasterChangeType> venMasterChangeTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMasterChangeTypeSessionEJBRemote#mergeVenMasterChangeType(com.
	 * gdn.venice.persistence.VenMasterChangeType)
	 */
	public VenMasterChangeType mergeVenMasterChangeType(VenMasterChangeType venMasterChangeType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMasterChangeTypeSessionEJBRemote#mergeVenMasterChangeTypeList(
	 * java.util.List)
	 */
	public ArrayList<VenMasterChangeType> mergeVenMasterChangeTypeList(
			List<VenMasterChangeType> venMasterChangeTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMasterChangeTypeSessionEJBRemote#removeVenMasterChangeType(com
	 * .gdn.venice.persistence.VenMasterChangeType)
	 */
	public void removeVenMasterChangeType(VenMasterChangeType venMasterChangeType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMasterChangeTypeSessionEJBRemote#removeVenMasterChangeTypeList
	 * (java.util.List)
	 */
	public void removeVenMasterChangeTypeList(List<VenMasterChangeType> venMasterChangeTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMasterChangeTypeSessionEJBRemote#findByVenMasterChangeTypeLike
	 * (com.gdn.venice.persistence.VenMasterChangeType, int, int)
	 */
	public List<VenMasterChangeType> findByVenMasterChangeTypeLike(VenMasterChangeType venMasterChangeType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMasterChangeTypeSessionEJBRemote#findByVenMasterChangeTypeLikeFR
	 * (com.gdn.venice.persistence.VenMasterChangeType, int, int)
	 */
	public FinderReturn findByVenMasterChangeTypeLikeFR(VenMasterChangeType venMasterChangeType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
