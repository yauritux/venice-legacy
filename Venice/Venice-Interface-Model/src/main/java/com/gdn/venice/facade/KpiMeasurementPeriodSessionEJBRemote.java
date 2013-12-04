package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.KpiMeasurementPeriod;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface KpiMeasurementPeriodSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of KpiMeasurementPeriod
	 */
	public List<KpiMeasurementPeriod> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistKpiMeasurementPeriod persists a country
	 * 
	 * @param kpiMeasurementPeriod
	 * @return the persisted KpiMeasurementPeriod
	 */
	public KpiMeasurementPeriod persistKpiMeasurementPeriod(KpiMeasurementPeriod kpiMeasurementPeriod);

	/**
	 * persistKpiMeasurementPeriodList - persists a list of KpiMeasurementPeriod
	 * 
	 * @param kpiMeasurementPeriodList
	 * @return the list of persisted KpiMeasurementPeriod
	 */
	public ArrayList<KpiMeasurementPeriod> persistKpiMeasurementPeriodList(
			List<KpiMeasurementPeriod> kpiMeasurementPeriodList);

	/**
	 * mergeKpiMeasurementPeriod - merges a KpiMeasurementPeriod
	 * 
	 * @param kpiMeasurementPeriod
	 * @return the merged KpiMeasurementPeriod
	 */
	public KpiMeasurementPeriod mergeKpiMeasurementPeriod(KpiMeasurementPeriod kpiMeasurementPeriod);

	/**
	 * mergeKpiMeasurementPeriodList - merges a list of KpiMeasurementPeriod
	 * 
	 * @param kpiMeasurementPeriodList
	 * @return the merged list of KpiMeasurementPeriod
	 */
	public ArrayList<KpiMeasurementPeriod> mergeKpiMeasurementPeriodList(
			List<KpiMeasurementPeriod> kpiMeasurementPeriodList);

	/**
	 * removeKpiMeasurementPeriod - removes a KpiMeasurementPeriod
	 * 
	 * @param kpiMeasurementPeriod
	 */
	public void removeKpiMeasurementPeriod(KpiMeasurementPeriod kpiMeasurementPeriod);

	/**
	 * removeKpiMeasurementPeriodList - removes a list of KpiMeasurementPeriod
	 * 
	 * @param kpiMeasurementPeriodList
	 */
	public void removeKpiMeasurementPeriodList(List<KpiMeasurementPeriod> kpiMeasurementPeriodList);

	/**
	 * findByKpiMeasurementPeriodLike - finds a list of KpiMeasurementPeriod Like
	 * 
	 * @param kpiMeasurementPeriod
	 * @return the list of KpiMeasurementPeriod found
	 */
	public List<KpiMeasurementPeriod> findByKpiMeasurementPeriodLike(KpiMeasurementPeriod kpiMeasurementPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByKpiMeasurementPeriod>LikeFR - finds a list of KpiMeasurementPeriod> Like with a finder return object
	 * 
	 * @param kpiMeasurementPeriod
	 * @return the list of KpiMeasurementPeriod found
	 */
	public FinderReturn findByKpiMeasurementPeriodLikeFR(KpiMeasurementPeriod kpiMeasurementPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
