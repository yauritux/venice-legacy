package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenBinCreditLimitEstimate;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenBinCreditLimitEstimateSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenBinCreditLimitEstimate> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote#persistVenBinCreditLimitEstimate(com
	 * .gdn.venice.persistence.VenBinCreditLimitEstimate)
	 */
	public VenBinCreditLimitEstimate persistVenBinCreditLimitEstimate(VenBinCreditLimitEstimate venBinCreditLimitEstimate);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote#persistVenBinCreditLimitEstimateList
	 * (java.util.List)
	 */
	public ArrayList<VenBinCreditLimitEstimate> persistVenBinCreditLimitEstimateList(
			List<VenBinCreditLimitEstimate> venBinCreditLimitEstimateList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote#mergeVenBinCreditLimitEstimate(com.
	 * gdn.venice.persistence.VenBinCreditLimitEstimate)
	 */
	public VenBinCreditLimitEstimate mergeVenBinCreditLimitEstimate(VenBinCreditLimitEstimate venBinCreditLimitEstimate);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote#mergeVenBinCreditLimitEstimateList(
	 * java.util.List)
	 */
	public ArrayList<VenBinCreditLimitEstimate> mergeVenBinCreditLimitEstimateList(
			List<VenBinCreditLimitEstimate> venBinCreditLimitEstimateList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote#removeVenBinCreditLimitEstimate(com
	 * .gdn.venice.persistence.VenBinCreditLimitEstimate)
	 */
	public void removeVenBinCreditLimitEstimate(VenBinCreditLimitEstimate venBinCreditLimitEstimate);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote#removeVenBinCreditLimitEstimateList
	 * (java.util.List)
	 */
	public void removeVenBinCreditLimitEstimateList(List<VenBinCreditLimitEstimate> venBinCreditLimitEstimateList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote#findByVenBinCreditLimitEstimateLike
	 * (com.gdn.venice.persistence.VenBinCreditLimitEstimate, int, int)
	 */
	public List<VenBinCreditLimitEstimate> findByVenBinCreditLimitEstimateLike(VenBinCreditLimitEstimate venBinCreditLimitEstimate,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBinCreditLimitEstimateSessionEJBRemote#findByVenBinCreditLimitEstimateLikeFR
	 * (com.gdn.venice.persistence.VenBinCreditLimitEstimate, int, int)
	 */
	public FinderReturn findByVenBinCreditLimitEstimateLikeFR(VenBinCreditLimitEstimate venBinCreditLimitEstimate,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
