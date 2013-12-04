package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderContactDetail;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderContactDetailSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderContactDetail
	 */
	public List<VenOrderContactDetail> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderContactDetail persists a country
	 * 
	 * @param venOrderContactDetail
	 * @return the persisted VenOrderContactDetail
	 */
	public VenOrderContactDetail persistVenOrderContactDetail(VenOrderContactDetail venOrderContactDetail);

	/**
	 * persistVenOrderContactDetailList - persists a list of VenOrderContactDetail
	 * 
	 * @param venOrderContactDetailList
	 * @return the list of persisted VenOrderContactDetail
	 */
	public ArrayList<VenOrderContactDetail> persistVenOrderContactDetailList(
			List<VenOrderContactDetail> venOrderContactDetailList);

	/**
	 * mergeVenOrderContactDetail - merges a VenOrderContactDetail
	 * 
	 * @param venOrderContactDetail
	 * @return the merged VenOrderContactDetail
	 */
	public VenOrderContactDetail mergeVenOrderContactDetail(VenOrderContactDetail venOrderContactDetail);

	/**
	 * mergeVenOrderContactDetailList - merges a list of VenOrderContactDetail
	 * 
	 * @param venOrderContactDetailList
	 * @return the merged list of VenOrderContactDetail
	 */
	public ArrayList<VenOrderContactDetail> mergeVenOrderContactDetailList(
			List<VenOrderContactDetail> venOrderContactDetailList);

	/**
	 * removeVenOrderContactDetail - removes a VenOrderContactDetail
	 * 
	 * @param venOrderContactDetail
	 */
	public void removeVenOrderContactDetail(VenOrderContactDetail venOrderContactDetail);

	/**
	 * removeVenOrderContactDetailList - removes a list of VenOrderContactDetail
	 * 
	 * @param venOrderContactDetailList
	 */
	public void removeVenOrderContactDetailList(List<VenOrderContactDetail> venOrderContactDetailList);

	/**
	 * findByVenOrderContactDetailLike - finds a list of VenOrderContactDetail Like
	 * 
	 * @param venOrderContactDetail
	 * @return the list of VenOrderContactDetail found
	 */
	public List<VenOrderContactDetail> findByVenOrderContactDetailLike(VenOrderContactDetail venOrderContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderContactDetail>LikeFR - finds a list of VenOrderContactDetail> Like with a finder return object
	 * 
	 * @param venOrderContactDetail
	 * @return the list of VenOrderContactDetail found
	 */
	public FinderReturn findByVenOrderContactDetailLikeFR(VenOrderContactDetail venOrderContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
