package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinArReconResult;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinArReconResultSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArReconResultSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinArReconResult> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArReconResultSessionEJBRemote#persistFinArReconResult(com
	 * .gdn.venice.persistence.FinArReconResult)
	 */
	public FinArReconResult persistFinArReconResult(FinArReconResult finArReconResult);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArReconResultSessionEJBRemote#persistFinArReconResultList
	 * (java.util.List)
	 */
	public ArrayList<FinArReconResult> persistFinArReconResultList(
			List<FinArReconResult> finArReconResultList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArReconResultSessionEJBRemote#mergeFinArReconResult(com.
	 * gdn.venice.persistence.FinArReconResult)
	 */
	public FinArReconResult mergeFinArReconResult(FinArReconResult finArReconResult);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArReconResultSessionEJBRemote#mergeFinArReconResultList(
	 * java.util.List)
	 */
	public ArrayList<FinArReconResult> mergeFinArReconResultList(
			List<FinArReconResult> finArReconResultList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArReconResultSessionEJBRemote#removeFinArReconResult(com
	 * .gdn.venice.persistence.FinArReconResult)
	 */
	public void removeFinArReconResult(FinArReconResult finArReconResult);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArReconResultSessionEJBRemote#removeFinArReconResultList
	 * (java.util.List)
	 */
	public void removeFinArReconResultList(List<FinArReconResult> finArReconResultList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArReconResultSessionEJBRemote#findByFinArReconResultLike
	 * (com.gdn.venice.persistence.FinArReconResult, int, int)
	 */
	public List<FinArReconResult> findByFinArReconResultLike(FinArReconResult finArReconResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinArReconResultSessionEJBRemote#findByFinArReconResultLikeFR
	 * (com.gdn.venice.persistence.FinArReconResult, int, int)
	 */
	public FinderReturn findByFinArReconResultLikeFR(FinArReconResult finArReconResult,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
