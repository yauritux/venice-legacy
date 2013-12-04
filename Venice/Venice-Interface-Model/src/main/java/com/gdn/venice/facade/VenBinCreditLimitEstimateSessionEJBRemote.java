package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenBinCreditLimitEstimateSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenBinCreditLimitEstimate
	 */
	public List<VenBinCreditLimitEstimate> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenBinCreditLimitEstimate persists a country
	 * 
	 * @param venBinCreditLimitEstimate
	 * @return the persisted VenBinCreditLimitEstimate
	 */
	public VenBinCreditLimitEstimate persistVenBinCreditLimitEstimate(VenBinCreditLimitEstimate venBinCreditLimitEstimate);

	/**
	 * persistVenBinCreditLimitEstimateList - persists a list of VenBinCreditLimitEstimate
	 * 
	 * @param venBinCreditLimitEstimateList
	 * @return the list of persisted VenBinCreditLimitEstimate
	 */
	public ArrayList<VenBinCreditLimitEstimate> persistVenBinCreditLimitEstimateList(
			List<VenBinCreditLimitEstimate> venBinCreditLimitEstimateList);

	/**
	 * mergeVenBinCreditLimitEstimate - merges a VenBinCreditLimitEstimate
	 * 
	 * @param venBinCreditLimitEstimate
	 * @return the merged VenBinCreditLimitEstimate
	 */
	public VenBinCreditLimitEstimate mergeVenBinCreditLimitEstimate(VenBinCreditLimitEstimate venBinCreditLimitEstimate);

	/**
	 * mergeVenBinCreditLimitEstimateList - merges a list of VenBinCreditLimitEstimate
	 * 
	 * @param venBinCreditLimitEstimateList
	 * @return the merged list of VenBinCreditLimitEstimate
	 */
	public ArrayList<VenBinCreditLimitEstimate> mergeVenBinCreditLimitEstimateList(
			List<VenBinCreditLimitEstimate> venBinCreditLimitEstimateList);

	/**
	 * removeVenBinCreditLimitEstimate - removes a VenBinCreditLimitEstimate
	 * 
	 * @param venBinCreditLimitEstimate
	 */
	public void removeVenBinCreditLimitEstimate(VenBinCreditLimitEstimate venBinCreditLimitEstimate);

	/**
	 * removeVenBinCreditLimitEstimateList - removes a list of VenBinCreditLimitEstimate
	 * 
	 * @param venBinCreditLimitEstimateList
	 */
	public void removeVenBinCreditLimitEstimateList(List<VenBinCreditLimitEstimate> venBinCreditLimitEstimateList);

	/**
	 * findByVenBinCreditLimitEstimateLike - finds a list of VenBinCreditLimitEstimate Like
	 * 
	 * @param venBinCreditLimitEstimate
	 * @return the list of VenBinCreditLimitEstimate found
	 */
	public List<VenBinCreditLimitEstimate> findByVenBinCreditLimitEstimateLike(VenBinCreditLimitEstimate venBinCreditLimitEstimate,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenBinCreditLimitEstimate>LikeFR - finds a list of VenBinCreditLimitEstimate> Like with a finder return object
	 * 
	 * @param venBinCreditLimitEstimate
	 * @return the list of VenBinCreditLimitEstimate found
	 */
	public FinderReturn findByVenBinCreditLimitEstimateLikeFR(VenBinCreditLimitEstimate venBinCreditLimitEstimate,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
