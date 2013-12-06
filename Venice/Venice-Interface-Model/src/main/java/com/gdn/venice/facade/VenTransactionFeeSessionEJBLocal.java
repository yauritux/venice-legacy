package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenTransactionFee;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenTransactionFeeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenTransactionFeeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenTransactionFee> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenTransactionFeeSessionEJBRemote#persistVenTransactionFee(com
	 * .gdn.venice.persistence.VenTransactionFee)
	 */
	public VenTransactionFee persistVenTransactionFee(VenTransactionFee venTransactionFee);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenTransactionFeeSessionEJBRemote#persistVenTransactionFeeList
	 * (java.util.List)
	 */
	public ArrayList<VenTransactionFee> persistVenTransactionFeeList(
			List<VenTransactionFee> venTransactionFeeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenTransactionFeeSessionEJBRemote#mergeVenTransactionFee(com.
	 * gdn.venice.persistence.VenTransactionFee)
	 */
	public VenTransactionFee mergeVenTransactionFee(VenTransactionFee venTransactionFee);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenTransactionFeeSessionEJBRemote#mergeVenTransactionFeeList(
	 * java.util.List)
	 */
	public ArrayList<VenTransactionFee> mergeVenTransactionFeeList(
			List<VenTransactionFee> venTransactionFeeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenTransactionFeeSessionEJBRemote#removeVenTransactionFee(com
	 * .gdn.venice.persistence.VenTransactionFee)
	 */
	public void removeVenTransactionFee(VenTransactionFee venTransactionFee);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenTransactionFeeSessionEJBRemote#removeVenTransactionFeeList
	 * (java.util.List)
	 */
	public void removeVenTransactionFeeList(List<VenTransactionFee> venTransactionFeeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenTransactionFeeSessionEJBRemote#findByVenTransactionFeeLike
	 * (com.gdn.venice.persistence.VenTransactionFee, int, int)
	 */
	public List<VenTransactionFee> findByVenTransactionFeeLike(VenTransactionFee venTransactionFee,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenTransactionFeeSessionEJBRemote#findByVenTransactionFeeLikeFR
	 * (com.gdn.venice.persistence.VenTransactionFee, int, int)
	 */
	public FinderReturn findByVenTransactionFeeLikeFR(VenTransactionFee venTransactionFee,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
