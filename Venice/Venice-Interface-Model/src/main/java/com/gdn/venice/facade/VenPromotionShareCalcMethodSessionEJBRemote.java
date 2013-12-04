package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenPromotionShareCalcMethod;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenPromotionShareCalcMethodSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenPromotionShareCalcMethod
	 */
	public List<VenPromotionShareCalcMethod> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenPromotionShareCalcMethod persists a country
	 * 
	 * @param venPromotionShareCalcMethod
	 * @return the persisted VenPromotionShareCalcMethod
	 */
	public VenPromotionShareCalcMethod persistVenPromotionShareCalcMethod(VenPromotionShareCalcMethod venPromotionShareCalcMethod);

	/**
	 * persistVenPromotionShareCalcMethodList - persists a list of VenPromotionShareCalcMethod
	 * 
	 * @param venPromotionShareCalcMethodList
	 * @return the list of persisted VenPromotionShareCalcMethod
	 */
	public ArrayList<VenPromotionShareCalcMethod> persistVenPromotionShareCalcMethodList(
			List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList);

	/**
	 * mergeVenPromotionShareCalcMethod - merges a VenPromotionShareCalcMethod
	 * 
	 * @param venPromotionShareCalcMethod
	 * @return the merged VenPromotionShareCalcMethod
	 */
	public VenPromotionShareCalcMethod mergeVenPromotionShareCalcMethod(VenPromotionShareCalcMethod venPromotionShareCalcMethod);

	/**
	 * mergeVenPromotionShareCalcMethodList - merges a list of VenPromotionShareCalcMethod
	 * 
	 * @param venPromotionShareCalcMethodList
	 * @return the merged list of VenPromotionShareCalcMethod
	 */
	public ArrayList<VenPromotionShareCalcMethod> mergeVenPromotionShareCalcMethodList(
			List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList);

	/**
	 * removeVenPromotionShareCalcMethod - removes a VenPromotionShareCalcMethod
	 * 
	 * @param venPromotionShareCalcMethod
	 */
	public void removeVenPromotionShareCalcMethod(VenPromotionShareCalcMethod venPromotionShareCalcMethod);

	/**
	 * removeVenPromotionShareCalcMethodList - removes a list of VenPromotionShareCalcMethod
	 * 
	 * @param venPromotionShareCalcMethodList
	 */
	public void removeVenPromotionShareCalcMethodList(List<VenPromotionShareCalcMethod> venPromotionShareCalcMethodList);

	/**
	 * findByVenPromotionShareCalcMethodLike - finds a list of VenPromotionShareCalcMethod Like
	 * 
	 * @param venPromotionShareCalcMethod
	 * @return the list of VenPromotionShareCalcMethod found
	 */
	public List<VenPromotionShareCalcMethod> findByVenPromotionShareCalcMethodLike(VenPromotionShareCalcMethod venPromotionShareCalcMethod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenPromotionShareCalcMethod>LikeFR - finds a list of VenPromotionShareCalcMethod> Like with a finder return object
	 * 
	 * @param venPromotionShareCalcMethod
	 * @return the list of VenPromotionShareCalcMethod found
	 */
	public FinderReturn findByVenPromotionShareCalcMethodLikeFR(VenPromotionShareCalcMethod venPromotionShareCalcMethod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
