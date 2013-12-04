package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderItemContactDetail;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderItemContactDetailSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderItemContactDetail
	 */
	public List<VenOrderItemContactDetail> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderItemContactDetail persists a country
	 * 
	 * @param venOrderItemContactDetail
	 * @return the persisted VenOrderItemContactDetail
	 */
	public VenOrderItemContactDetail persistVenOrderItemContactDetail(VenOrderItemContactDetail venOrderItemContactDetail);

	/**
	 * persistVenOrderItemContactDetailList - persists a list of VenOrderItemContactDetail
	 * 
	 * @param venOrderItemContactDetailList
	 * @return the list of persisted VenOrderItemContactDetail
	 */
	public ArrayList<VenOrderItemContactDetail> persistVenOrderItemContactDetailList(
			List<VenOrderItemContactDetail> venOrderItemContactDetailList);

	/**
	 * mergeVenOrderItemContactDetail - merges a VenOrderItemContactDetail
	 * 
	 * @param venOrderItemContactDetail
	 * @return the merged VenOrderItemContactDetail
	 */
	public VenOrderItemContactDetail mergeVenOrderItemContactDetail(VenOrderItemContactDetail venOrderItemContactDetail);

	/**
	 * mergeVenOrderItemContactDetailList - merges a list of VenOrderItemContactDetail
	 * 
	 * @param venOrderItemContactDetailList
	 * @return the merged list of VenOrderItemContactDetail
	 */
	public ArrayList<VenOrderItemContactDetail> mergeVenOrderItemContactDetailList(
			List<VenOrderItemContactDetail> venOrderItemContactDetailList);

	/**
	 * removeVenOrderItemContactDetail - removes a VenOrderItemContactDetail
	 * 
	 * @param venOrderItemContactDetail
	 */
	public void removeVenOrderItemContactDetail(VenOrderItemContactDetail venOrderItemContactDetail);

	/**
	 * removeVenOrderItemContactDetailList - removes a list of VenOrderItemContactDetail
	 * 
	 * @param venOrderItemContactDetailList
	 */
	public void removeVenOrderItemContactDetailList(List<VenOrderItemContactDetail> venOrderItemContactDetailList);

	/**
	 * findByVenOrderItemContactDetailLike - finds a list of VenOrderItemContactDetail Like
	 * 
	 * @param venOrderItemContactDetail
	 * @return the list of VenOrderItemContactDetail found
	 */
	public List<VenOrderItemContactDetail> findByVenOrderItemContactDetailLike(VenOrderItemContactDetail venOrderItemContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderItemContactDetail>LikeFR - finds a list of VenOrderItemContactDetail> Like with a finder return object
	 * 
	 * @param venOrderItemContactDetail
	 * @return the list of VenOrderItemContactDetail found
	 */
	public FinderReturn findByVenOrderItemContactDetailLikeFR(VenOrderItemContactDetail venOrderItemContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
