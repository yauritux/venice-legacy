package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenHoliday;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenHolidaySessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenHolidaySessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenHoliday> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenHolidaySessionEJBRemote#persistVenHoliday(com
	 * .gdn.venice.persistence.VenHoliday)
	 */
	public VenHoliday persistVenHoliday(VenHoliday venHoliday);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenHolidaySessionEJBRemote#persistVenHolidayList
	 * (java.util.List)
	 */
	public ArrayList<VenHoliday> persistVenHolidayList(
			List<VenHoliday> venHolidayList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenHolidaySessionEJBRemote#mergeVenHoliday(com.
	 * gdn.venice.persistence.VenHoliday)
	 */
	public VenHoliday mergeVenHoliday(VenHoliday venHoliday);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenHolidaySessionEJBRemote#mergeVenHolidayList(
	 * java.util.List)
	 */
	public ArrayList<VenHoliday> mergeVenHolidayList(
			List<VenHoliday> venHolidayList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenHolidaySessionEJBRemote#removeVenHoliday(com
	 * .gdn.venice.persistence.VenHoliday)
	 */
	public void removeVenHoliday(VenHoliday venHoliday);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenHolidaySessionEJBRemote#removeVenHolidayList
	 * (java.util.List)
	 */
	public void removeVenHolidayList(List<VenHoliday> venHolidayList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenHolidaySessionEJBRemote#findByVenHolidayLike
	 * (com.gdn.venice.persistence.VenHoliday, int, int)
	 */
	public List<VenHoliday> findByVenHolidayLike(VenHoliday venHoliday,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenHolidaySessionEJBRemote#findByVenHolidayLikeFR
	 * (com.gdn.venice.persistence.VenHoliday, int, int)
	 */
	public FinderReturn findByVenHolidayLikeFR(VenHoliday venHoliday,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
