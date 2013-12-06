package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenSettlementRecord;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenSettlementRecordSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenSettlementRecord
	 */
	public List<VenSettlementRecord> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenSettlementRecord persists a country
	 * 
	 * @param venSettlementRecord
	 * @return the persisted VenSettlementRecord
	 */
	public VenSettlementRecord persistVenSettlementRecord(VenSettlementRecord venSettlementRecord);

	/**
	 * persistVenSettlementRecordList - persists a list of VenSettlementRecord
	 * 
	 * @param venSettlementRecordList
	 * @return the list of persisted VenSettlementRecord
	 */
	public ArrayList<VenSettlementRecord> persistVenSettlementRecordList(
			List<VenSettlementRecord> venSettlementRecordList);

	/**
	 * mergeVenSettlementRecord - merges a VenSettlementRecord
	 * 
	 * @param venSettlementRecord
	 * @return the merged VenSettlementRecord
	 */
	public VenSettlementRecord mergeVenSettlementRecord(VenSettlementRecord venSettlementRecord);

	/**
	 * mergeVenSettlementRecordList - merges a list of VenSettlementRecord
	 * 
	 * @param venSettlementRecordList
	 * @return the merged list of VenSettlementRecord
	 */
	public ArrayList<VenSettlementRecord> mergeVenSettlementRecordList(
			List<VenSettlementRecord> venSettlementRecordList);

	/**
	 * removeVenSettlementRecord - removes a VenSettlementRecord
	 * 
	 * @param venSettlementRecord
	 */
	public void removeVenSettlementRecord(VenSettlementRecord venSettlementRecord);

	/**
	 * removeVenSettlementRecordList - removes a list of VenSettlementRecord
	 * 
	 * @param venSettlementRecordList
	 */
	public void removeVenSettlementRecordList(List<VenSettlementRecord> venSettlementRecordList);

	/**
	 * findByVenSettlementRecordLike - finds a list of VenSettlementRecord Like
	 * 
	 * @param venSettlementRecord
	 * @return the list of VenSettlementRecord found
	 */
	public List<VenSettlementRecord> findByVenSettlementRecordLike(VenSettlementRecord venSettlementRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenSettlementRecord>LikeFR - finds a list of VenSettlementRecord> Like with a finder return object
	 * 
	 * @param venSettlementRecord
	 * @return the list of VenSettlementRecord found
	 */
	public FinderReturn findByVenSettlementRecordLikeFR(VenSettlementRecord venSettlementRecord,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
