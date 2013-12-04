package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenCardType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenCardTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCardTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenCardType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCardTypeSessionEJBRemote#persistVenCardType(com
	 * .gdn.venice.persistence.VenCardType)
	 */
	public VenCardType persistVenCardType(VenCardType venCardType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCardTypeSessionEJBRemote#persistVenCardTypeList
	 * (java.util.List)
	 */
	public ArrayList<VenCardType> persistVenCardTypeList(
			List<VenCardType> venCardTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCardTypeSessionEJBRemote#mergeVenCardType(com.
	 * gdn.venice.persistence.VenCardType)
	 */
	public VenCardType mergeVenCardType(VenCardType venCardType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCardTypeSessionEJBRemote#mergeVenCardTypeList(
	 * java.util.List)
	 */
	public ArrayList<VenCardType> mergeVenCardTypeList(
			List<VenCardType> venCardTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCardTypeSessionEJBRemote#removeVenCardType(com
	 * .gdn.venice.persistence.VenCardType)
	 */
	public void removeVenCardType(VenCardType venCardType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCardTypeSessionEJBRemote#removeVenCardTypeList
	 * (java.util.List)
	 */
	public void removeVenCardTypeList(List<VenCardType> venCardTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCardTypeSessionEJBRemote#findByVenCardTypeLike
	 * (com.gdn.venice.persistence.VenCardType, int, int)
	 */
	public List<VenCardType> findByVenCardTypeLike(VenCardType venCardType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCardTypeSessionEJBRemote#findByVenCardTypeLikeFR
	 * (com.gdn.venice.persistence.VenCardType, int, int)
	 */
	public FinderReturn findByVenCardTypeLikeFR(VenCardType venCardType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
