package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.KpiPartyMeasurementPeriod;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface KpiPartyMeasurementPeriodSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of KpiPartyMeasurementPeriod
	 */
	public List<KpiPartyMeasurementPeriod> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistKpiPartyMeasurementPeriod persists a country
	 * 
	 * @param kpiPartyMeasurementPeriod
	 * @return the persisted KpiPartyMeasurementPeriod
	 */
	public KpiPartyMeasurementPeriod persistKpiPartyMeasurementPeriod(KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod);

	/**
	 * persistKpiPartyMeasurementPeriodList - persists a list of KpiPartyMeasurementPeriod
	 * 
	 * @param kpiPartyMeasurementPeriodList
	 * @return the list of persisted KpiPartyMeasurementPeriod
	 */
	public ArrayList<KpiPartyMeasurementPeriod> persistKpiPartyMeasurementPeriodList(
			List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriodList);

	/**
	 * mergeKpiPartyMeasurementPeriod - merges a KpiPartyMeasurementPeriod
	 * 
	 * @param kpiPartyMeasurementPeriod
	 * @return the merged KpiPartyMeasurementPeriod
	 */
	public KpiPartyMeasurementPeriod mergeKpiPartyMeasurementPeriod(KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod);

	/**
	 * mergeKpiPartyMeasurementPeriodList - merges a list of KpiPartyMeasurementPeriod
	 * 
	 * @param kpiPartyMeasurementPeriodList
	 * @return the merged list of KpiPartyMeasurementPeriod
	 */
	public ArrayList<KpiPartyMeasurementPeriod> mergeKpiPartyMeasurementPeriodList(
			List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriodList);

	/**
	 * removeKpiPartyMeasurementPeriod - removes a KpiPartyMeasurementPeriod
	 * 
	 * @param kpiPartyMeasurementPeriod
	 */
	public void removeKpiPartyMeasurementPeriod(KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod);

	/**
	 * removeKpiPartyMeasurementPeriodList - removes a list of KpiPartyMeasurementPeriod
	 * 
	 * @param kpiPartyMeasurementPeriodList
	 */
	public void removeKpiPartyMeasurementPeriodList(List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriodList);

	/**
	 * findByKpiPartyMeasurementPeriodLike - finds a list of KpiPartyMeasurementPeriod Like
	 * 
	 * @param kpiPartyMeasurementPeriod
	 * @return the list of KpiPartyMeasurementPeriod found
	 */
	public List<KpiPartyMeasurementPeriod> findByKpiPartyMeasurementPeriodLike(KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByKpiPartyMeasurementPeriod>LikeFR - finds a list of KpiPartyMeasurementPeriod> Like with a finder return object
	 * 
	 * @param kpiPartyMeasurementPeriod
	 * @return the list of KpiPartyMeasurementPeriod found
	 */
	public FinderReturn findByKpiPartyMeasurementPeriodLikeFR(KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
