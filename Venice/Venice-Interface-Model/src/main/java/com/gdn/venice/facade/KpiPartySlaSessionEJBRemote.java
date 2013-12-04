package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.KpiPartySla;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface KpiPartySlaSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of KpiPartySla
	 */
	public List<KpiPartySla> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistKpiPartySla persists a country
	 * 
	 * @param kpiPartySla
	 * @return the persisted KpiPartySla
	 */
	public KpiPartySla persistKpiPartySla(KpiPartySla kpiPartySla);

	/**
	 * persistKpiPartySlaList - persists a list of KpiPartySla
	 * 
	 * @param kpiPartySlaList
	 * @return the list of persisted KpiPartySla
	 */
	public ArrayList<KpiPartySla> persistKpiPartySlaList(
			List<KpiPartySla> kpiPartySlaList);

	/**
	 * mergeKpiPartySla - merges a KpiPartySla
	 * 
	 * @param kpiPartySla
	 * @return the merged KpiPartySla
	 */
	public KpiPartySla mergeKpiPartySla(KpiPartySla kpiPartySla);

	/**
	 * mergeKpiPartySlaList - merges a list of KpiPartySla
	 * 
	 * @param kpiPartySlaList
	 * @return the merged list of KpiPartySla
	 */
	public ArrayList<KpiPartySla> mergeKpiPartySlaList(
			List<KpiPartySla> kpiPartySlaList);

	/**
	 * removeKpiPartySla - removes a KpiPartySla
	 * 
	 * @param kpiPartySla
	 */
	public void removeKpiPartySla(KpiPartySla kpiPartySla);

	/**
	 * removeKpiPartySlaList - removes a list of KpiPartySla
	 * 
	 * @param kpiPartySlaList
	 */
	public void removeKpiPartySlaList(List<KpiPartySla> kpiPartySlaList);

	/**
	 * findByKpiPartySlaLike - finds a list of KpiPartySla Like
	 * 
	 * @param kpiPartySla
	 * @return the list of KpiPartySla found
	 */
	public List<KpiPartySla> findByKpiPartySlaLike(KpiPartySla kpiPartySla,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByKpiPartySla>LikeFR - finds a list of KpiPartySla> Like with a finder return object
	 * 
	 * @param kpiPartySla
	 * @return the list of KpiPartySla found
	 */
	public FinderReturn findByKpiPartySlaLikeFR(KpiPartySla kpiPartySla,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
