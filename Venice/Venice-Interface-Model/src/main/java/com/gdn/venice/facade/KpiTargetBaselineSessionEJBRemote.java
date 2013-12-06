package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.KpiTargetBaseline;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface KpiTargetBaselineSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of KpiTargetBaseline
	 */
	public List<KpiTargetBaseline> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistKpiTargetBaseline persists a country
	 * 
	 * @param kpiTargetBaseline
	 * @return the persisted KpiTargetBaseline
	 */
	public KpiTargetBaseline persistKpiTargetBaseline(KpiTargetBaseline kpiTargetBaseline);

	/**
	 * persistKpiTargetBaselineList - persists a list of KpiTargetBaseline
	 * 
	 * @param kpiTargetBaselineList
	 * @return the list of persisted KpiTargetBaseline
	 */
	public ArrayList<KpiTargetBaseline> persistKpiTargetBaselineList(
			List<KpiTargetBaseline> kpiTargetBaselineList);

	/**
	 * mergeKpiTargetBaseline - merges a KpiTargetBaseline
	 * 
	 * @param kpiTargetBaseline
	 * @return the merged KpiTargetBaseline
	 */
	public KpiTargetBaseline mergeKpiTargetBaseline(KpiTargetBaseline kpiTargetBaseline);

	/**
	 * mergeKpiTargetBaselineList - merges a list of KpiTargetBaseline
	 * 
	 * @param kpiTargetBaselineList
	 * @return the merged list of KpiTargetBaseline
	 */
	public ArrayList<KpiTargetBaseline> mergeKpiTargetBaselineList(
			List<KpiTargetBaseline> kpiTargetBaselineList);

	/**
	 * removeKpiTargetBaseline - removes a KpiTargetBaseline
	 * 
	 * @param kpiTargetBaseline
	 */
	public void removeKpiTargetBaseline(KpiTargetBaseline kpiTargetBaseline);

	/**
	 * removeKpiTargetBaselineList - removes a list of KpiTargetBaseline
	 * 
	 * @param kpiTargetBaselineList
	 */
	public void removeKpiTargetBaselineList(List<KpiTargetBaseline> kpiTargetBaselineList);

	/**
	 * findByKpiTargetBaselineLike - finds a list of KpiTargetBaseline Like
	 * 
	 * @param kpiTargetBaseline
	 * @return the list of KpiTargetBaseline found
	 */
	public List<KpiTargetBaseline> findByKpiTargetBaselineLike(KpiTargetBaseline kpiTargetBaseline,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByKpiTargetBaseline>LikeFR - finds a list of KpiTargetBaseline> Like with a finder return object
	 * 
	 * @param kpiTargetBaseline
	 * @return the list of KpiTargetBaseline found
	 */
	public FinderReturn findByKpiTargetBaselineLikeFR(KpiTargetBaseline kpiTargetBaseline,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
