package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.KpiPartyPeriodActual;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface KpiPartyPeriodActualSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<KpiPartyPeriodActual> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote#persistKpiPartyPeriodActual(com
	 * .gdn.venice.persistence.KpiPartyPeriodActual)
	 */
	public KpiPartyPeriodActual persistKpiPartyPeriodActual(KpiPartyPeriodActual kpiPartyPeriodActual);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote#persistKpiPartyPeriodActualList
	 * (java.util.List)
	 */
	public ArrayList<KpiPartyPeriodActual> persistKpiPartyPeriodActualList(
			List<KpiPartyPeriodActual> kpiPartyPeriodActualList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote#mergeKpiPartyPeriodActual(com.
	 * gdn.venice.persistence.KpiPartyPeriodActual)
	 */
	public KpiPartyPeriodActual mergeKpiPartyPeriodActual(KpiPartyPeriodActual kpiPartyPeriodActual);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote#mergeKpiPartyPeriodActualList(
	 * java.util.List)
	 */
	public ArrayList<KpiPartyPeriodActual> mergeKpiPartyPeriodActualList(
			List<KpiPartyPeriodActual> kpiPartyPeriodActualList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote#removeKpiPartyPeriodActual(com
	 * .gdn.venice.persistence.KpiPartyPeriodActual)
	 */
	public void removeKpiPartyPeriodActual(KpiPartyPeriodActual kpiPartyPeriodActual);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote#removeKpiPartyPeriodActualList
	 * (java.util.List)
	 */
	public void removeKpiPartyPeriodActualList(List<KpiPartyPeriodActual> kpiPartyPeriodActualList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote#findByKpiPartyPeriodActualLike
	 * (com.gdn.venice.persistence.KpiPartyPeriodActual, int, int)
	 */
	public List<KpiPartyPeriodActual> findByKpiPartyPeriodActualLike(KpiPartyPeriodActual kpiPartyPeriodActual,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyPeriodActualSessionEJBRemote#findByKpiPartyPeriodActualLikeFR
	 * (com.gdn.venice.persistence.KpiPartyPeriodActual, int, int)
	 */
	public FinderReturn findByKpiPartyPeriodActualLikeFR(KpiPartyPeriodActual kpiPartyPeriodActual,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
