package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenMerchantAgreement;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenMerchantAgreementSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenMerchantAgreement
	 */
	public List<VenMerchantAgreement> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenMerchantAgreement persists a country
	 * 
	 * @param venMerchantAgreement
	 * @return the persisted VenMerchantAgreement
	 */
	public VenMerchantAgreement persistVenMerchantAgreement(VenMerchantAgreement venMerchantAgreement);

	/**
	 * persistVenMerchantAgreementList - persists a list of VenMerchantAgreement
	 * 
	 * @param venMerchantAgreementList
	 * @return the list of persisted VenMerchantAgreement
	 */
	public ArrayList<VenMerchantAgreement> persistVenMerchantAgreementList(
			List<VenMerchantAgreement> venMerchantAgreementList);

	/**
	 * mergeVenMerchantAgreement - merges a VenMerchantAgreement
	 * 
	 * @param venMerchantAgreement
	 * @return the merged VenMerchantAgreement
	 */
	public VenMerchantAgreement mergeVenMerchantAgreement(VenMerchantAgreement venMerchantAgreement);

	/**
	 * mergeVenMerchantAgreementList - merges a list of VenMerchantAgreement
	 * 
	 * @param venMerchantAgreementList
	 * @return the merged list of VenMerchantAgreement
	 */
	public ArrayList<VenMerchantAgreement> mergeVenMerchantAgreementList(
			List<VenMerchantAgreement> venMerchantAgreementList);

	/**
	 * removeVenMerchantAgreement - removes a VenMerchantAgreement
	 * 
	 * @param venMerchantAgreement
	 */
	public void removeVenMerchantAgreement(VenMerchantAgreement venMerchantAgreement);

	/**
	 * removeVenMerchantAgreementList - removes a list of VenMerchantAgreement
	 * 
	 * @param venMerchantAgreementList
	 */
	public void removeVenMerchantAgreementList(List<VenMerchantAgreement> venMerchantAgreementList);

	/**
	 * findByVenMerchantAgreementLike - finds a list of VenMerchantAgreement Like
	 * 
	 * @param venMerchantAgreement
	 * @return the list of VenMerchantAgreement found
	 */
	public List<VenMerchantAgreement> findByVenMerchantAgreementLike(VenMerchantAgreement venMerchantAgreement,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenMerchantAgreement>LikeFR - finds a list of VenMerchantAgreement> Like with a finder return object
	 * 
	 * @param venMerchantAgreement
	 * @return the list of VenMerchantAgreement found
	 */
	public FinderReturn findByVenMerchantAgreementLikeFR(VenMerchantAgreement venMerchantAgreement,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
