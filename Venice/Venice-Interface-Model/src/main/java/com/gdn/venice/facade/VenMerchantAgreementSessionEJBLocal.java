package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenMerchantAgreement;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenMerchantAgreementSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantAgreementSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenMerchantAgreement> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantAgreementSessionEJBRemote#persistVenMerchantAgreement(com
	 * .gdn.venice.persistence.VenMerchantAgreement)
	 */
	public VenMerchantAgreement persistVenMerchantAgreement(VenMerchantAgreement venMerchantAgreement);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantAgreementSessionEJBRemote#persistVenMerchantAgreementList
	 * (java.util.List)
	 */
	public ArrayList<VenMerchantAgreement> persistVenMerchantAgreementList(
			List<VenMerchantAgreement> venMerchantAgreementList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantAgreementSessionEJBRemote#mergeVenMerchantAgreement(com.
	 * gdn.venice.persistence.VenMerchantAgreement)
	 */
	public VenMerchantAgreement mergeVenMerchantAgreement(VenMerchantAgreement venMerchantAgreement);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantAgreementSessionEJBRemote#mergeVenMerchantAgreementList(
	 * java.util.List)
	 */
	public ArrayList<VenMerchantAgreement> mergeVenMerchantAgreementList(
			List<VenMerchantAgreement> venMerchantAgreementList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantAgreementSessionEJBRemote#removeVenMerchantAgreement(com
	 * .gdn.venice.persistence.VenMerchantAgreement)
	 */
	public void removeVenMerchantAgreement(VenMerchantAgreement venMerchantAgreement);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantAgreementSessionEJBRemote#removeVenMerchantAgreementList
	 * (java.util.List)
	 */
	public void removeVenMerchantAgreementList(List<VenMerchantAgreement> venMerchantAgreementList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantAgreementSessionEJBRemote#findByVenMerchantAgreementLike
	 * (com.gdn.venice.persistence.VenMerchantAgreement, int, int)
	 */
	public List<VenMerchantAgreement> findByVenMerchantAgreementLike(VenMerchantAgreement venMerchantAgreement,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenMerchantAgreementSessionEJBRemote#findByVenMerchantAgreementLikeFR
	 * (com.gdn.venice.persistence.VenMerchantAgreement, int, int)
	 */
	public FinderReturn findByVenMerchantAgreementLikeFR(VenMerchantAgreement venMerchantAgreement,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
