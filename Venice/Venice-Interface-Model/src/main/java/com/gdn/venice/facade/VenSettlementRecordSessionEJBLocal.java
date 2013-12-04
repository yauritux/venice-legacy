package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenSettlementRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenSettlementRecordSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenSettlementRecord> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote#persistVenSettlementRecord(com
	 * .gdn.venice.persistence.VenSettlementRecord)
	 */
	public VenSettlementRecord persistVenSettlementRecord(VenSettlementRecord venSettlementRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote#persistVenSettlementRecordList
	 * (java.util.List)
	 */
	public ArrayList<VenSettlementRecord> persistVenSettlementRecordList(
			List<VenSettlementRecord> venSettlementRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote#mergeVenSettlementRecord(com.
	 * gdn.venice.persistence.VenSettlementRecord)
	 */
	public VenSettlementRecord mergeVenSettlementRecord(VenSettlementRecord venSettlementRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote#mergeVenSettlementRecordList(
	 * java.util.List)
	 */
	public ArrayList<VenSettlementRecord> mergeVenSettlementRecordList(
			List<VenSettlementRecord> venSettlementRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote#removeVenSettlementRecord(com
	 * .gdn.venice.persistence.VenSettlementRecord)
	 */
	public void removeVenSettlementRecord(VenSettlementRecord venSettlementRecord);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote#removeVenSettlementRecordList
	 * (java.util.List)
	 */
	public void removeVenSettlementRecordList(List<VenSettlementRecord> venSettlementRecordList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote#findByVenSettlementRecordLike
	 * (com.gdn.venice.persistence.VenSettlementRecord, int, int)
	 */
	public List<VenSettlementRecord> findByVenSettlementRecordLike(VenSettlementRecord venSettlementRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenSettlementRecordSessionEJBRemote#findByVenSettlementRecordLikeFR
	 * (com.gdn.venice.persistence.VenSettlementRecord, int, int)
	 */
	public FinderReturn findByVenSettlementRecordLikeFR(VenSettlementRecord venSettlementRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
