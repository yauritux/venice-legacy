package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.KpiKeyPerformanceIndicator;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface KpiKeyPerformanceIndicatorSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of KpiKeyPerformanceIndicator
	 */
	public List<KpiKeyPerformanceIndicator> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistKpiKeyPerformanceIndicator persists a country
	 * 
	 * @param kpiKeyPerformanceIndicator
	 * @return the persisted KpiKeyPerformanceIndicator
	 */
	public KpiKeyPerformanceIndicator persistKpiKeyPerformanceIndicator(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator);

	/**
	 * persistKpiKeyPerformanceIndicatorList - persists a list of KpiKeyPerformanceIndicator
	 * 
	 * @param kpiKeyPerformanceIndicatorList
	 * @return the list of persisted KpiKeyPerformanceIndicator
	 */
	public ArrayList<KpiKeyPerformanceIndicator> persistKpiKeyPerformanceIndicatorList(
			List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList);

	/**
	 * mergeKpiKeyPerformanceIndicator - merges a KpiKeyPerformanceIndicator
	 * 
	 * @param kpiKeyPerformanceIndicator
	 * @return the merged KpiKeyPerformanceIndicator
	 */
	public KpiKeyPerformanceIndicator mergeKpiKeyPerformanceIndicator(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator);

	/**
	 * mergeKpiKeyPerformanceIndicatorList - merges a list of KpiKeyPerformanceIndicator
	 * 
	 * @param kpiKeyPerformanceIndicatorList
	 * @return the merged list of KpiKeyPerformanceIndicator
	 */
	public ArrayList<KpiKeyPerformanceIndicator> mergeKpiKeyPerformanceIndicatorList(
			List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList);

	/**
	 * removeKpiKeyPerformanceIndicator - removes a KpiKeyPerformanceIndicator
	 * 
	 * @param kpiKeyPerformanceIndicator
	 */
	public void removeKpiKeyPerformanceIndicator(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator);

	/**
	 * removeKpiKeyPerformanceIndicatorList - removes a list of KpiKeyPerformanceIndicator
	 * 
	 * @param kpiKeyPerformanceIndicatorList
	 */
	public void removeKpiKeyPerformanceIndicatorList(List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList);

	/**
	 * findByKpiKeyPerformanceIndicatorLike - finds a list of KpiKeyPerformanceIndicator Like
	 * 
	 * @param kpiKeyPerformanceIndicator
	 * @return the list of KpiKeyPerformanceIndicator found
	 */
	public List<KpiKeyPerformanceIndicator> findByKpiKeyPerformanceIndicatorLike(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByKpiKeyPerformanceIndicator>LikeFR - finds a list of KpiKeyPerformanceIndicator> Like with a finder return object
	 * 
	 * @param kpiKeyPerformanceIndicator
	 * @return the list of KpiKeyPerformanceIndicator found
	 */
	public FinderReturn findByKpiKeyPerformanceIndicatorLikeFR(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
