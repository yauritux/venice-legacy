package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.KpiTargetBaseline;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface KpiTargetBaselineSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiTargetBaselineSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<KpiTargetBaseline> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiTargetBaselineSessionEJBRemote#persistKpiTargetBaseline(com
	 * .gdn.venice.persistence.KpiTargetBaseline)
	 */
	public KpiTargetBaseline persistKpiTargetBaseline(KpiTargetBaseline kpiTargetBaseline);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiTargetBaselineSessionEJBRemote#persistKpiTargetBaselineList
	 * (java.util.List)
	 */
	public ArrayList<KpiTargetBaseline> persistKpiTargetBaselineList(
			List<KpiTargetBaseline> kpiTargetBaselineList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiTargetBaselineSessionEJBRemote#mergeKpiTargetBaseline(com.
	 * gdn.venice.persistence.KpiTargetBaseline)
	 */
	public KpiTargetBaseline mergeKpiTargetBaseline(KpiTargetBaseline kpiTargetBaseline);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiTargetBaselineSessionEJBRemote#mergeKpiTargetBaselineList(
	 * java.util.List)
	 */
	public ArrayList<KpiTargetBaseline> mergeKpiTargetBaselineList(
			List<KpiTargetBaseline> kpiTargetBaselineList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiTargetBaselineSessionEJBRemote#removeKpiTargetBaseline(com
	 * .gdn.venice.persistence.KpiTargetBaseline)
	 */
	public void removeKpiTargetBaseline(KpiTargetBaseline kpiTargetBaseline);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiTargetBaselineSessionEJBRemote#removeKpiTargetBaselineList
	 * (java.util.List)
	 */
	public void removeKpiTargetBaselineList(List<KpiTargetBaseline> kpiTargetBaselineList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiTargetBaselineSessionEJBRemote#findByKpiTargetBaselineLike
	 * (com.gdn.venice.persistence.KpiTargetBaseline, int, int)
	 */
	public List<KpiTargetBaseline> findByKpiTargetBaselineLike(KpiTargetBaseline kpiTargetBaseline,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiTargetBaselineSessionEJBRemote#findByKpiTargetBaselineLikeFR
	 * (com.gdn.venice.persistence.KpiTargetBaseline, int, int)
	 */
	public FinderReturn findByKpiTargetBaselineLikeFR(KpiTargetBaseline kpiTargetBaseline,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
