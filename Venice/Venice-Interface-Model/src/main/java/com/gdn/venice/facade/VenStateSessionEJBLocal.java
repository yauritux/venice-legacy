package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenState;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenStateSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenStateSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenState> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenStateSessionEJBRemote#persistVenState(com
	 * .gdn.venice.persistence.VenState)
	 */
	public VenState persistVenState(VenState venState);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenStateSessionEJBRemote#persistVenStateList
	 * (java.util.List)
	 */
	public ArrayList<VenState> persistVenStateList(
			List<VenState> venStateList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenStateSessionEJBRemote#mergeVenState(com.
	 * gdn.venice.persistence.VenState)
	 */
	public VenState mergeVenState(VenState venState);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenStateSessionEJBRemote#mergeVenStateList(
	 * java.util.List)
	 */
	public ArrayList<VenState> mergeVenStateList(
			List<VenState> venStateList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenStateSessionEJBRemote#removeVenState(com
	 * .gdn.venice.persistence.VenState)
	 */
	public void removeVenState(VenState venState);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenStateSessionEJBRemote#removeVenStateList
	 * (java.util.List)
	 */
	public void removeVenStateList(List<VenState> venStateList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenStateSessionEJBRemote#findByVenStateLike
	 * (com.gdn.venice.persistence.VenState, int, int)
	 */
	public List<VenState> findByVenStateLike(VenState venState,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenStateSessionEJBRemote#findByVenStateLikeFR
	 * (com.gdn.venice.persistence.VenState, int, int)
	 */
	public FinderReturn findByVenStateLikeFR(VenState venState,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
