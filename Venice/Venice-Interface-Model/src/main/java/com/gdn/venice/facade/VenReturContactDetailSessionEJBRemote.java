package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenReturContactDetail;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenReturContactDetailSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenReturContactDetail
	 */
	public List<VenReturContactDetail> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenReturContactDetail persists a country
	 * 
	 * @param venReturContactDetail
	 * @return the persisted VenReturContactDetail
	 */
	public VenReturContactDetail persistVenReturContactDetail(VenReturContactDetail venReturContactDetail);

	/**
	 * persistVenReturContactDetailList - persists a list of VenReturContactDetail
	 * 
	 * @param venReturContactDetailList
	 * @return the list of persisted VenReturContactDetail
	 */
	public ArrayList<VenReturContactDetail> persistVenReturContactDetailList(
			List<VenReturContactDetail> venReturContactDetailList);

	/**
	 * mergeVenReturContactDetail - merges a VenReturContactDetail
	 * 
	 * @param venReturContactDetail
	 * @return the merged VenReturContactDetail
	 */
	public VenReturContactDetail mergeVenReturContactDetail(VenReturContactDetail venReturContactDetail);

	/**
	 * mergeVenReturContactDetailList - merges a list of VenReturContactDetail
	 * 
	 * @param venReturContactDetailList
	 * @return the merged list of VenReturContactDetail
	 */
	public ArrayList<VenReturContactDetail> mergeVenReturContactDetailList(
			List<VenReturContactDetail> venReturContactDetailList);

	/**
	 * removeVenReturContactDetail - removes a VenReturContactDetail
	 * 
	 * @param venReturContactDetail
	 */
	public void removeVenReturContactDetail(VenReturContactDetail venReturContactDetail);

	/**
	 * removeVenReturContactDetailList - removes a list of VenReturContactDetail
	 * 
	 * @param venReturContactDetailList
	 */
	public void removeVenReturContactDetailList(List<VenReturContactDetail> venReturContactDetailList);

	/**
	 * findByVenReturContactDetailLike - finds a list of VenReturContactDetail Like
	 * 
	 * @param venReturContactDetail
	 * @return the list of VenReturContactDetail found
	 */
	public List<VenReturContactDetail> findByVenReturContactDetailLike(VenReturContactDetail venReturContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenReturContactDetail>LikeFR - finds a list of VenReturContactDetail> Like with a finder return object
	 * 
	 * @param venReturContactDetail
	 * @return the list of VenReturContactDetail found
	 */
	public FinderReturn findByVenReturContactDetailLikeFR(VenReturContactDetail venReturContactDetail,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
