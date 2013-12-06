package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.KpiMeasurementPeriod;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface KpiMeasurementPeriodSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<KpiMeasurementPeriod> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote#persistKpiMeasurementPeriod(com
	 * .gdn.venice.persistence.KpiMeasurementPeriod)
	 */
	public KpiMeasurementPeriod persistKpiMeasurementPeriod(KpiMeasurementPeriod kpiMeasurementPeriod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote#persistKpiMeasurementPeriodList
	 * (java.util.List)
	 */
	public ArrayList<KpiMeasurementPeriod> persistKpiMeasurementPeriodList(
			List<KpiMeasurementPeriod> kpiMeasurementPeriodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote#mergeKpiMeasurementPeriod(com.
	 * gdn.venice.persistence.KpiMeasurementPeriod)
	 */
	public KpiMeasurementPeriod mergeKpiMeasurementPeriod(KpiMeasurementPeriod kpiMeasurementPeriod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote#mergeKpiMeasurementPeriodList(
	 * java.util.List)
	 */
	public ArrayList<KpiMeasurementPeriod> mergeKpiMeasurementPeriodList(
			List<KpiMeasurementPeriod> kpiMeasurementPeriodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote#removeKpiMeasurementPeriod(com
	 * .gdn.venice.persistence.KpiMeasurementPeriod)
	 */
	public void removeKpiMeasurementPeriod(KpiMeasurementPeriod kpiMeasurementPeriod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote#removeKpiMeasurementPeriodList
	 * (java.util.List)
	 */
	public void removeKpiMeasurementPeriodList(List<KpiMeasurementPeriod> kpiMeasurementPeriodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote#findByKpiMeasurementPeriodLike
	 * (com.gdn.venice.persistence.KpiMeasurementPeriod, int, int)
	 */
	public List<KpiMeasurementPeriod> findByKpiMeasurementPeriodLike(KpiMeasurementPeriod kpiMeasurementPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiMeasurementPeriodSessionEJBRemote#findByKpiMeasurementPeriodLikeFR
	 * (com.gdn.venice.persistence.KpiMeasurementPeriod, int, int)
	 */
	public FinderReturn findByKpiMeasurementPeriodLikeFR(KpiMeasurementPeriod kpiMeasurementPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
