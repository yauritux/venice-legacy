package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.KpiKeyPerformanceIndicator;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface KpiKeyPerformanceIndicatorSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<KpiKeyPerformanceIndicator> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#persistKpiKeyPerformanceIndicator(com
	 * .gdn.venice.persistence.KpiKeyPerformanceIndicator)
	 */
	public KpiKeyPerformanceIndicator persistKpiKeyPerformanceIndicator(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#persistKpiKeyPerformanceIndicatorList
	 * (java.util.List)
	 */
	public ArrayList<KpiKeyPerformanceIndicator> persistKpiKeyPerformanceIndicatorList(
			List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#mergeKpiKeyPerformanceIndicator(com.
	 * gdn.venice.persistence.KpiKeyPerformanceIndicator)
	 */
	public KpiKeyPerformanceIndicator mergeKpiKeyPerformanceIndicator(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#mergeKpiKeyPerformanceIndicatorList(
	 * java.util.List)
	 */
	public ArrayList<KpiKeyPerformanceIndicator> mergeKpiKeyPerformanceIndicatorList(
			List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#removeKpiKeyPerformanceIndicator(com
	 * .gdn.venice.persistence.KpiKeyPerformanceIndicator)
	 */
	public void removeKpiKeyPerformanceIndicator(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#removeKpiKeyPerformanceIndicatorList
	 * (java.util.List)
	 */
	public void removeKpiKeyPerformanceIndicatorList(List<KpiKeyPerformanceIndicator> kpiKeyPerformanceIndicatorList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#findByKpiKeyPerformanceIndicatorLike
	 * (com.gdn.venice.persistence.KpiKeyPerformanceIndicator, int, int)
	 */
	public List<KpiKeyPerformanceIndicator> findByKpiKeyPerformanceIndicatorLike(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiKeyPerformanceIndicatorSessionEJBRemote#findByKpiKeyPerformanceIndicatorLikeFR
	 * (com.gdn.venice.persistence.KpiKeyPerformanceIndicator, int, int)
	 */
	public FinderReturn findByKpiKeyPerformanceIndicatorLikeFR(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
