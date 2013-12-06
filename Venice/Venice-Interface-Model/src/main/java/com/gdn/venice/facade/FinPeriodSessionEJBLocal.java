package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinPeriod;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinPeriodSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinPeriodSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinPeriod> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinPeriodSessionEJBRemote#persistFinPeriod(com
	 * .gdn.venice.persistence.FinPeriod)
	 */
	public FinPeriod persistFinPeriod(FinPeriod finPeriod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinPeriodSessionEJBRemote#persistFinPeriodList
	 * (java.util.List)
	 */
	public ArrayList<FinPeriod> persistFinPeriodList(
			List<FinPeriod> finPeriodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinPeriodSessionEJBRemote#mergeFinPeriod(com.
	 * gdn.venice.persistence.FinPeriod)
	 */
	public FinPeriod mergeFinPeriod(FinPeriod finPeriod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinPeriodSessionEJBRemote#mergeFinPeriodList(
	 * java.util.List)
	 */
	public ArrayList<FinPeriod> mergeFinPeriodList(
			List<FinPeriod> finPeriodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinPeriodSessionEJBRemote#removeFinPeriod(com
	 * .gdn.venice.persistence.FinPeriod)
	 */
	public void removeFinPeriod(FinPeriod finPeriod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinPeriodSessionEJBRemote#removeFinPeriodList
	 * (java.util.List)
	 */
	public void removeFinPeriodList(List<FinPeriod> finPeriodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinPeriodSessionEJBRemote#findByFinPeriodLike
	 * (com.gdn.venice.persistence.FinPeriod, int, int)
	 */
	public List<FinPeriod> findByFinPeriodLike(FinPeriod finPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinPeriodSessionEJBRemote#findByFinPeriodLikeFR
	 * (com.gdn.venice.persistence.FinPeriod, int, int)
	 */
	public FinderReturn findByFinPeriodLikeFR(FinPeriod finPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
