package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenCountry;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenCountrySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCountrySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenCountry> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCountrySessionEJBRemote#persistVenCountry(com
	 * .gdn.venice.persistence.VenCountry)
	 */
	public VenCountry persistVenCountry(VenCountry venCountry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCountrySessionEJBRemote#persistVenCountryList
	 * (java.util.List)
	 */
	public ArrayList<VenCountry> persistVenCountryList(
			List<VenCountry> venCountryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCountrySessionEJBRemote#mergeVenCountry(com.
	 * gdn.venice.persistence.VenCountry)
	 */
	public VenCountry mergeVenCountry(VenCountry venCountry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCountrySessionEJBRemote#mergeVenCountryList(
	 * java.util.List)
	 */
	public ArrayList<VenCountry> mergeVenCountryList(
			List<VenCountry> venCountryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCountrySessionEJBRemote#removeVenCountry(com
	 * .gdn.venice.persistence.VenCountry)
	 */
	public void removeVenCountry(VenCountry venCountry);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCountrySessionEJBRemote#removeVenCountryList
	 * (java.util.List)
	 */
	public void removeVenCountryList(List<VenCountry> venCountryList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCountrySessionEJBRemote#findByVenCountryLike
	 * (com.gdn.venice.persistence.VenCountry, int, int)
	 */
	public List<VenCountry> findByVenCountryLike(VenCountry venCountry,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCountrySessionEJBRemote#findByVenCountryLikeFR
	 * (com.gdn.venice.persistence.VenCountry, int, int)
	 */
	public FinderReturn findByVenCountryLikeFR(VenCountry venCountry,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
