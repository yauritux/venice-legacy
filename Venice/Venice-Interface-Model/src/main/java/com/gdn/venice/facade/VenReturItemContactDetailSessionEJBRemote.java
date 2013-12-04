package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenReturItemContactDetail;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenReturItemContactDetailSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenReturItemContactDetail
	 */
	public List<VenReturItemContactDetail> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenReturItemContactDetail persists a country
	 * 
	 * @param venReturItemContactDetail
	 * @return the persisted VenReturItemContactDetail
	 */
	public VenReturItemContactDetail persistVenReturItemContactDetail(VenReturItemContactDetail venReturItemContactDetail);

	/**
	 * persistVenReturItemContactDetailList - persists a list of VenReturItemContactDetail
	 * 
	 * @param venReturItemContactDetailList
	 * @return the list of persisted VenReturItemContactDetail
	 */
	public ArrayList<VenReturItemContactDetail> persistVenReturItemContactDetailList(
			List<VenReturItemContactDetail> venReturItemContactDetailList);

	/**
	 * mergeVenReturItemContactDetail - merges a VenReturItemContactDetail
	 * 
	 * @param venReturItemContactDetail
	 * @return the merged VenReturItemContactDetail
	 */
	public VenReturItemContactDetail mergeVenReturItemContactDetail(VenReturItemContactDetail venReturItemContactDetail);

	/**
	 * mergeVenReturItemContactDetailList - merges a list of VenReturItemContactDetail
	 * 
	 * @param venReturItemContactDetailList
	 * @return the merged list of VenReturItemContactDetail
	 */
	public ArrayList<VenReturItemContactDetail> mergeVenReturItemContactDetailList(
			List<VenReturItemContactDetail> venReturItemContactDetailList);

	/**
	 * removeVenReturItemContactDetail - removes a VenReturItemContactDetail
	 * 
	 * @param venReturItemContactDetail
	 */
	public void removeVenReturItemContactDetail(VenReturItemContactDetail venReturItemContactDetail);

	/**
	 * removeVenReturItemContactDetailList - removes a list of VenReturItemContactDetail
	 * 
	 * @param venReturItemContactDetailList
	 */
	public void removeVenReturItemContactDetailList(List<VenReturItemContactDetail> venReturItemContactDetailList);

	/**
	 * findByVenReturItemContactDetailLike - finds a list of VenReturItemContactDetail Like
	 * 
	 * @param venReturItemContactDetail
	 * @return the list of VenReturItemContactDetail found
	 */
	public List<VenReturItemContactDetail> findByVenReturItemContactDetailLike(VenReturItemContactDetail venReturItemContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenReturItemContactDetail>LikeFR - finds a list of VenReturItemContactDetail> Like with a finder return object
	 * 
	 * @param venReturItemContactDetail
	 * @return the list of VenReturItemContactDetail found
	 */
	public FinderReturn findByVenReturItemContactDetailLikeFR(VenReturItemContactDetail venReturItemContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
