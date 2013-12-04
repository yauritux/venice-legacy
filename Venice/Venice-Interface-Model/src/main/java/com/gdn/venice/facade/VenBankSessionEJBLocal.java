package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenBank;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenBankSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBankSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenBank> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBankSessionEJBRemote#persistVenBank(com
	 * .gdn.venice.persistence.VenBank)
	 */
	public VenBank persistVenBank(VenBank venBank);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBankSessionEJBRemote#persistVenBankList
	 * (java.util.List)
	 */
	public ArrayList<VenBank> persistVenBankList(
			List<VenBank> venBankList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBankSessionEJBRemote#mergeVenBank(com.
	 * gdn.venice.persistence.VenBank)
	 */
	public VenBank mergeVenBank(VenBank venBank);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBankSessionEJBRemote#mergeVenBankList(
	 * java.util.List)
	 */
	public ArrayList<VenBank> mergeVenBankList(
			List<VenBank> venBankList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBankSessionEJBRemote#removeVenBank(com
	 * .gdn.venice.persistence.VenBank)
	 */
	public void removeVenBank(VenBank venBank);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBankSessionEJBRemote#removeVenBankList
	 * (java.util.List)
	 */
	public void removeVenBankList(List<VenBank> venBankList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBankSessionEJBRemote#findByVenBankLike
	 * (com.gdn.venice.persistence.VenBank, int, int)
	 */
	public List<VenBank> findByVenBankLike(VenBank venBank,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenBankSessionEJBRemote#findByVenBankLikeFR
	 * (com.gdn.venice.persistence.VenBank, int, int)
	 */
	public FinderReturn findByVenBankLikeFR(VenBank venBank,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
