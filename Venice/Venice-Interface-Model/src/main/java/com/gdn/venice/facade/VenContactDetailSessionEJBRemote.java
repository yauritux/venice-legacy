package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenContactDetail;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenContactDetailSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenContactDetail
	 */
	public List<VenContactDetail> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenContactDetail persists a country
	 * 
	 * @param venContactDetail
	 * @return the persisted VenContactDetail
	 */
	public VenContactDetail persistVenContactDetail(VenContactDetail venContactDetail);

	/**
	 * persistVenContactDetailList - persists a list of VenContactDetail
	 * 
	 * @param venContactDetailList
	 * @return the list of persisted VenContactDetail
	 */
	public ArrayList<VenContactDetail> persistVenContactDetailList(
			List<VenContactDetail> venContactDetailList);

	/**
	 * mergeVenContactDetail - merges a VenContactDetail
	 * 
	 * @param venContactDetail
	 * @return the merged VenContactDetail
	 */
	public VenContactDetail mergeVenContactDetail(VenContactDetail venContactDetail);

	/**
	 * mergeVenContactDetailList - merges a list of VenContactDetail
	 * 
	 * @param venContactDetailList
	 * @return the merged list of VenContactDetail
	 */
	public ArrayList<VenContactDetail> mergeVenContactDetailList(
			List<VenContactDetail> venContactDetailList);

	/**
	 * removeVenContactDetail - removes a VenContactDetail
	 * 
	 * @param venContactDetail
	 */
	public void removeVenContactDetail(VenContactDetail venContactDetail);

	/**
	 * removeVenContactDetailList - removes a list of VenContactDetail
	 * 
	 * @param venContactDetailList
	 */
	public void removeVenContactDetailList(List<VenContactDetail> venContactDetailList);

	/**
	 * findByVenContactDetailLike - finds a list of VenContactDetail Like
	 * 
	 * @param venContactDetail
	 * @return the list of VenContactDetail found
	 */
	public List<VenContactDetail> findByVenContactDetailLike(VenContactDetail venContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenContactDetail>LikeFR - finds a list of VenContactDetail> Like with a finder return object
	 * 
	 * @param venContactDetail
	 * @return the list of VenContactDetail found
	 */
	public FinderReturn findByVenContactDetailLikeFR(VenContactDetail venContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
