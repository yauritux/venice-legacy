package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenBank;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenBankSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenBank
	 */
	public List<VenBank> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenBank persists a country
	 * 
	 * @param venBank
	 * @return the persisted VenBank
	 */
	public VenBank persistVenBank(VenBank venBank);

	/**
	 * persistVenBankList - persists a list of VenBank
	 * 
	 * @param venBankList
	 * @return the list of persisted VenBank
	 */
	public ArrayList<VenBank> persistVenBankList(
			List<VenBank> venBankList);

	/**
	 * mergeVenBank - merges a VenBank
	 * 
	 * @param venBank
	 * @return the merged VenBank
	 */
	public VenBank mergeVenBank(VenBank venBank);

	/**
	 * mergeVenBankList - merges a list of VenBank
	 * 
	 * @param venBankList
	 * @return the merged list of VenBank
	 */
	public ArrayList<VenBank> mergeVenBankList(
			List<VenBank> venBankList);

	/**
	 * removeVenBank - removes a VenBank
	 * 
	 * @param venBank
	 */
	public void removeVenBank(VenBank venBank);

	/**
	 * removeVenBankList - removes a list of VenBank
	 * 
	 * @param venBankList
	 */
	public void removeVenBankList(List<VenBank> venBankList);

	/**
	 * findByVenBankLike - finds a list of VenBank Like
	 * 
	 * @param venBank
	 * @return the list of VenBank found
	 */
	public List<VenBank> findByVenBankLike(VenBank venBank,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenBank>LikeFR - finds a list of VenBank> Like with a finder return object
	 * 
	 * @param venBank
	 * @return the list of VenBank found
	 */
	public FinderReturn findByVenBankLikeFR(VenBank venBank,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
