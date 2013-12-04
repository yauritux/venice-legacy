package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenHoliday;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenHolidaySessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenHoliday
	 */
	public List<VenHoliday> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenHoliday persists a country
	 * 
	 * @param venHoliday
	 * @return the persisted VenHoliday
	 */
	public VenHoliday persistVenHoliday(VenHoliday venHoliday);

	/**
	 * persistVenHolidayList - persists a list of VenHoliday
	 * 
	 * @param venHolidayList
	 * @return the list of persisted VenHoliday
	 */
	public ArrayList<VenHoliday> persistVenHolidayList(
			List<VenHoliday> venHolidayList);

	/**
	 * mergeVenHoliday - merges a VenHoliday
	 * 
	 * @param venHoliday
	 * @return the merged VenHoliday
	 */
	public VenHoliday mergeVenHoliday(VenHoliday venHoliday);

	/**
	 * mergeVenHolidayList - merges a list of VenHoliday
	 * 
	 * @param venHolidayList
	 * @return the merged list of VenHoliday
	 */
	public ArrayList<VenHoliday> mergeVenHolidayList(
			List<VenHoliday> venHolidayList);

	/**
	 * removeVenHoliday - removes a VenHoliday
	 * 
	 * @param venHoliday
	 */
	public void removeVenHoliday(VenHoliday venHoliday);

	/**
	 * removeVenHolidayList - removes a list of VenHoliday
	 * 
	 * @param venHolidayList
	 */
	public void removeVenHolidayList(List<VenHoliday> venHolidayList);

	/**
	 * findByVenHolidayLike - finds a list of VenHoliday Like
	 * 
	 * @param venHoliday
	 * @return the list of VenHoliday found
	 */
	public List<VenHoliday> findByVenHolidayLike(VenHoliday venHoliday,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenHoliday>LikeFR - finds a list of VenHoliday> Like with a finder return object
	 * 
	 * @param venHoliday
	 * @return the list of VenHoliday found
	 */
	public FinderReturn findByVenHolidayLikeFR(VenHoliday venHoliday,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
