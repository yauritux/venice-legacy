package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.KpiPartyPeriodActual;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface KpiPartyPeriodActualSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of KpiPartyPeriodActual
	 */
	public List<KpiPartyPeriodActual> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistKpiPartyPeriodActual persists a country
	 * 
	 * @param kpiPartyPeriodActual
	 * @return the persisted KpiPartyPeriodActual
	 */
	public KpiPartyPeriodActual persistKpiPartyPeriodActual(KpiPartyPeriodActual kpiPartyPeriodActual);

	/**
	 * persistKpiPartyPeriodActualList - persists a list of KpiPartyPeriodActual
	 * 
	 * @param kpiPartyPeriodActualList
	 * @return the list of persisted KpiPartyPeriodActual
	 */
	public ArrayList<KpiPartyPeriodActual> persistKpiPartyPeriodActualList(
			List<KpiPartyPeriodActual> kpiPartyPeriodActualList);

	/**
	 * mergeKpiPartyPeriodActual - merges a KpiPartyPeriodActual
	 * 
	 * @param kpiPartyPeriodActual
	 * @return the merged KpiPartyPeriodActual
	 */
	public KpiPartyPeriodActual mergeKpiPartyPeriodActual(KpiPartyPeriodActual kpiPartyPeriodActual);

	/**
	 * mergeKpiPartyPeriodActualList - merges a list of KpiPartyPeriodActual
	 * 
	 * @param kpiPartyPeriodActualList
	 * @return the merged list of KpiPartyPeriodActual
	 */
	public ArrayList<KpiPartyPeriodActual> mergeKpiPartyPeriodActualList(
			List<KpiPartyPeriodActual> kpiPartyPeriodActualList);

	/**
	 * removeKpiPartyPeriodActual - removes a KpiPartyPeriodActual
	 * 
	 * @param kpiPartyPeriodActual
	 */
	public void removeKpiPartyPeriodActual(KpiPartyPeriodActual kpiPartyPeriodActual);

	/**
	 * removeKpiPartyPeriodActualList - removes a list of KpiPartyPeriodActual
	 * 
	 * @param kpiPartyPeriodActualList
	 */
	public void removeKpiPartyPeriodActualList(List<KpiPartyPeriodActual> kpiPartyPeriodActualList);

	/**
	 * findByKpiPartyPeriodActualLike - finds a list of KpiPartyPeriodActual Like
	 * 
	 * @param kpiPartyPeriodActual
	 * @return the list of KpiPartyPeriodActual found
	 */
	public List<KpiPartyPeriodActual> findByKpiPartyPeriodActualLike(KpiPartyPeriodActual kpiPartyPeriodActual,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByKpiPartyPeriodActual>LikeFR - finds a list of KpiPartyPeriodActual> Like with a finder return object
	 * 
	 * @param kpiPartyPeriodActual
	 * @return the list of KpiPartyPeriodActual found
	 */
	public FinderReturn findByKpiPartyPeriodActualLikeFR(KpiPartyPeriodActual kpiPartyPeriodActual,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
