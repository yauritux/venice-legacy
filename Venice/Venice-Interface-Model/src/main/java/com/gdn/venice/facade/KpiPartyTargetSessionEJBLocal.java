package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.KpiPartyTarget;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface KpiPartyTargetSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<KpiPartyTarget> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote#persistKpiPartyTarget(com
	 * .gdn.venice.persistence.KpiPartyTarget)
	 */
	public KpiPartyTarget persistKpiPartyTarget(KpiPartyTarget kpiPartyTarget);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote#persistKpiPartyTargetList
	 * (java.util.List)
	 */
	public ArrayList<KpiPartyTarget> persistKpiPartyTargetList(
			List<KpiPartyTarget> kpiPartyTargetList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote#mergeKpiPartyTarget(com.
	 * gdn.venice.persistence.KpiPartyTarget)
	 */
	public KpiPartyTarget mergeKpiPartyTarget(KpiPartyTarget kpiPartyTarget);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote#mergeKpiPartyTargetList(
	 * java.util.List)
	 */
	public ArrayList<KpiPartyTarget> mergeKpiPartyTargetList(
			List<KpiPartyTarget> kpiPartyTargetList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote#removeKpiPartyTarget(com
	 * .gdn.venice.persistence.KpiPartyTarget)
	 */
	public void removeKpiPartyTarget(KpiPartyTarget kpiPartyTarget);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote#removeKpiPartyTargetList
	 * (java.util.List)
	 */
	public void removeKpiPartyTargetList(List<KpiPartyTarget> kpiPartyTargetList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote#findByKpiPartyTargetLike
	 * (com.gdn.venice.persistence.KpiPartyTarget, int, int)
	 */
	public List<KpiPartyTarget> findByKpiPartyTargetLike(KpiPartyTarget kpiPartyTarget,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyTargetSessionEJBRemote#findByKpiPartyTargetLikeFR
	 * (com.gdn.venice.persistence.KpiPartyTarget, int, int)
	 */
	public FinderReturn findByKpiPartyTargetLikeFR(KpiPartyTarget kpiPartyTarget,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
