package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.KpiPartyTarget;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface KpiPartyTargetSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of KpiPartyTarget
	 */
	public List<KpiPartyTarget> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistKpiPartyTarget persists a country
	 * 
	 * @param kpiPartyTarget
	 * @return the persisted KpiPartyTarget
	 */
	public KpiPartyTarget persistKpiPartyTarget(KpiPartyTarget kpiPartyTarget);

	/**
	 * persistKpiPartyTargetList - persists a list of KpiPartyTarget
	 * 
	 * @param kpiPartyTargetList
	 * @return the list of persisted KpiPartyTarget
	 */
	public ArrayList<KpiPartyTarget> persistKpiPartyTargetList(
			List<KpiPartyTarget> kpiPartyTargetList);

	/**
	 * mergeKpiPartyTarget - merges a KpiPartyTarget
	 * 
	 * @param kpiPartyTarget
	 * @return the merged KpiPartyTarget
	 */
	public KpiPartyTarget mergeKpiPartyTarget(KpiPartyTarget kpiPartyTarget);

	/**
	 * mergeKpiPartyTargetList - merges a list of KpiPartyTarget
	 * 
	 * @param kpiPartyTargetList
	 * @return the merged list of KpiPartyTarget
	 */
	public ArrayList<KpiPartyTarget> mergeKpiPartyTargetList(
			List<KpiPartyTarget> kpiPartyTargetList);

	/**
	 * removeKpiPartyTarget - removes a KpiPartyTarget
	 * 
	 * @param kpiPartyTarget
	 */
	public void removeKpiPartyTarget(KpiPartyTarget kpiPartyTarget);

	/**
	 * removeKpiPartyTargetList - removes a list of KpiPartyTarget
	 * 
	 * @param kpiPartyTargetList
	 */
	public void removeKpiPartyTargetList(List<KpiPartyTarget> kpiPartyTargetList);

	/**
	 * findByKpiPartyTargetLike - finds a list of KpiPartyTarget Like
	 * 
	 * @param kpiPartyTarget
	 * @return the list of KpiPartyTarget found
	 */
	public List<KpiPartyTarget> findByKpiPartyTargetLike(KpiPartyTarget kpiPartyTarget,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByKpiPartyTarget>LikeFR - finds a list of KpiPartyTarget> Like with a finder return object
	 * 
	 * @param kpiPartyTarget
	 * @return the list of KpiPartyTarget found
	 */
	public FinderReturn findByKpiPartyTargetLikeFR(KpiPartyTarget kpiPartyTarget,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
