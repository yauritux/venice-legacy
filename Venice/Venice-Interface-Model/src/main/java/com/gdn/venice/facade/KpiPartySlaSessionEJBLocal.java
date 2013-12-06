package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.KpiPartySla;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface KpiPartySlaSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartySlaSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<KpiPartySla> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartySlaSessionEJBRemote#persistKpiPartySla(com
	 * .gdn.venice.persistence.KpiPartySla)
	 */
	public KpiPartySla persistKpiPartySla(KpiPartySla kpiPartySla);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartySlaSessionEJBRemote#persistKpiPartySlaList
	 * (java.util.List)
	 */
	public ArrayList<KpiPartySla> persistKpiPartySlaList(
			List<KpiPartySla> kpiPartySlaList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartySlaSessionEJBRemote#mergeKpiPartySla(com.
	 * gdn.venice.persistence.KpiPartySla)
	 */
	public KpiPartySla mergeKpiPartySla(KpiPartySla kpiPartySla);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartySlaSessionEJBRemote#mergeKpiPartySlaList(
	 * java.util.List)
	 */
	public ArrayList<KpiPartySla> mergeKpiPartySlaList(
			List<KpiPartySla> kpiPartySlaList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartySlaSessionEJBRemote#removeKpiPartySla(com
	 * .gdn.venice.persistence.KpiPartySla)
	 */
	public void removeKpiPartySla(KpiPartySla kpiPartySla);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartySlaSessionEJBRemote#removeKpiPartySlaList
	 * (java.util.List)
	 */
	public void removeKpiPartySlaList(List<KpiPartySla> kpiPartySlaList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartySlaSessionEJBRemote#findByKpiPartySlaLike
	 * (com.gdn.venice.persistence.KpiPartySla, int, int)
	 */
	public List<KpiPartySla> findByKpiPartySlaLike(KpiPartySla kpiPartySla,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.KpiPartySlaSessionEJBRemote#findByKpiPartySlaLikeFR
	 * (com.gdn.venice.persistence.KpiPartySla, int, int)
	 */
	public FinderReturn findByKpiPartySlaLikeFR(KpiPartySla kpiPartySla,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
