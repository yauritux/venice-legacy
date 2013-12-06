package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenCity;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenCitySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCitySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenCity> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCitySessionEJBRemote#persistVenCity(com
	 * .gdn.venice.persistence.VenCity)
	 */
	public VenCity persistVenCity(VenCity venCity);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCitySessionEJBRemote#persistVenCityList
	 * (java.util.List)
	 */
	public ArrayList<VenCity> persistVenCityList(
			List<VenCity> venCityList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCitySessionEJBRemote#mergeVenCity(com.
	 * gdn.venice.persistence.VenCity)
	 */
	public VenCity mergeVenCity(VenCity venCity);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCitySessionEJBRemote#mergeVenCityList(
	 * java.util.List)
	 */
	public ArrayList<VenCity> mergeVenCityList(
			List<VenCity> venCityList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCitySessionEJBRemote#removeVenCity(com
	 * .gdn.venice.persistence.VenCity)
	 */
	public void removeVenCity(VenCity venCity);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCitySessionEJBRemote#removeVenCityList
	 * (java.util.List)
	 */
	public void removeVenCityList(List<VenCity> venCityList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCitySessionEJBRemote#findByVenCityLike
	 * (com.gdn.venice.persistence.VenCity, int, int)
	 */
	public List<VenCity> findByVenCityLike(VenCity venCity,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenCitySessionEJBRemote#findByVenCityLikeFR
	 * (com.gdn.venice.persistence.VenCity, int, int)
	 */
	public FinderReturn findByVenCityLikeFR(VenCity venCity,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
