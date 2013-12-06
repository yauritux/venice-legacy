package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenParty;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenPartySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenParty> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartySessionEJBRemote#persistVenParty(com
	 * .gdn.venice.persistence.VenParty)
	 */
	public VenParty persistVenParty(VenParty venParty);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartySessionEJBRemote#persistVenPartyList
	 * (java.util.List)
	 */
	public ArrayList<VenParty> persistVenPartyList(
			List<VenParty> venPartyList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartySessionEJBRemote#mergeVenParty(com.
	 * gdn.venice.persistence.VenParty)
	 */
	public VenParty mergeVenParty(VenParty venParty);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartySessionEJBRemote#mergeVenPartyList(
	 * java.util.List)
	 */
	public ArrayList<VenParty> mergeVenPartyList(
			List<VenParty> venPartyList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartySessionEJBRemote#removeVenParty(com
	 * .gdn.venice.persistence.VenParty)
	 */
	public void removeVenParty(VenParty venParty);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartySessionEJBRemote#removeVenPartyList
	 * (java.util.List)
	 */
	public void removeVenPartyList(List<VenParty> venPartyList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartySessionEJBRemote#findByVenPartyLike
	 * (com.gdn.venice.persistence.VenParty, int, int)
	 */
	public List<VenParty> findByVenPartyLike(VenParty venParty,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenPartySessionEJBRemote#findByVenPartyLikeFR
	 * (com.gdn.venice.persistence.VenParty, int, int)
	 */
	public FinderReturn findByVenPartyLikeFR(VenParty venParty,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
