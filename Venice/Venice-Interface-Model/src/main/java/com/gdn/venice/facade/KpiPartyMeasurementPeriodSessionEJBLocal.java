package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.KpiPartyMeasurementPeriod;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface KpiPartyMeasurementPeriodSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<KpiPartyMeasurementPeriod> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBRemote#persistKpiPartyMeasurementPeriod(com
	 * .gdn.venice.persistence.KpiPartyMeasurementPeriod)
	 */
	public KpiPartyMeasurementPeriod persistKpiPartyMeasurementPeriod(KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBRemote#persistKpiPartyMeasurementPeriodList
	 * (java.util.List)
	 */
	public ArrayList<KpiPartyMeasurementPeriod> persistKpiPartyMeasurementPeriodList(
			List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBRemote#mergeKpiPartyMeasurementPeriod(com.
	 * gdn.venice.persistence.KpiPartyMeasurementPeriod)
	 */
	public KpiPartyMeasurementPeriod mergeKpiPartyMeasurementPeriod(KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBRemote#mergeKpiPartyMeasurementPeriodList(
	 * java.util.List)
	 */
	public ArrayList<KpiPartyMeasurementPeriod> mergeKpiPartyMeasurementPeriodList(
			List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBRemote#removeKpiPartyMeasurementPeriod(com
	 * .gdn.venice.persistence.KpiPartyMeasurementPeriod)
	 */
	public void removeKpiPartyMeasurementPeriod(KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBRemote#removeKpiPartyMeasurementPeriodList
	 * (java.util.List)
	 */
	public void removeKpiPartyMeasurementPeriodList(List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriodList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBRemote#findByKpiPartyMeasurementPeriodLike
	 * (com.gdn.venice.persistence.KpiPartyMeasurementPeriod, int, int)
	 */
	public List<KpiPartyMeasurementPeriod> findByKpiPartyMeasurementPeriodLike(KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartyMeasurementPeriodSessionEJBRemote#findByKpiPartyMeasurementPeriodLikeFR
	 * (com.gdn.venice.persistence.KpiPartyMeasurementPeriod, int, int)
	 */
	public FinderReturn findByKpiPartyMeasurementPeriodLikeFR(KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
