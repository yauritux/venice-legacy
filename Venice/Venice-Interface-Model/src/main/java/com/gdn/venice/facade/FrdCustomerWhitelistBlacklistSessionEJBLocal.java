package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdCustomerWhitelistBlacklist;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdCustomerWhitelistBlacklistSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdCustomerWhitelistBlacklist> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote#persistFrdCustomerWhitelistBlacklist(com
	 * .gdn.venice.persistence.FrdCustomerWhitelistBlacklist)
	 */
	public FrdCustomerWhitelistBlacklist persistFrdCustomerWhitelistBlacklist(FrdCustomerWhitelistBlacklist frdCustomerWhitelistBlacklist);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote#persistFrdCustomerWhitelistBlacklistList
	 * (java.util.List)
	 */
	public ArrayList<FrdCustomerWhitelistBlacklist> persistFrdCustomerWhitelistBlacklistList(
			List<FrdCustomerWhitelistBlacklist> frdCustomerWhitelistBlacklistList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote#mergeFrdCustomerWhitelistBlacklist(com.
	 * gdn.venice.persistence.FrdCustomerWhitelistBlacklist)
	 */
	public FrdCustomerWhitelistBlacklist mergeFrdCustomerWhitelistBlacklist(FrdCustomerWhitelistBlacklist frdCustomerWhitelistBlacklist);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote#mergeFrdCustomerWhitelistBlacklistList(
	 * java.util.List)
	 */
	public ArrayList<FrdCustomerWhitelistBlacklist> mergeFrdCustomerWhitelistBlacklistList(
			List<FrdCustomerWhitelistBlacklist> frdCustomerWhitelistBlacklistList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote#removeFrdCustomerWhitelistBlacklist(com
	 * .gdn.venice.persistence.FrdCustomerWhitelistBlacklist)
	 */
	public void removeFrdCustomerWhitelistBlacklist(FrdCustomerWhitelistBlacklist frdCustomerWhitelistBlacklist);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote#removeFrdCustomerWhitelistBlacklistList
	 * (java.util.List)
	 */
	public void removeFrdCustomerWhitelistBlacklistList(List<FrdCustomerWhitelistBlacklist> frdCustomerWhitelistBlacklistList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote#findByFrdCustomerWhitelistBlacklistLike
	 * (com.gdn.venice.persistence.FrdCustomerWhitelistBlacklist, int, int)
	 */
	public List<FrdCustomerWhitelistBlacklist> findByFrdCustomerWhitelistBlacklistLike(FrdCustomerWhitelistBlacklist frdCustomerWhitelistBlacklist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistBlacklistSessionEJBRemote#findByFrdCustomerWhitelistBlacklistLikeFR
	 * (com.gdn.venice.persistence.FrdCustomerWhitelistBlacklist, int, int)
	 */
	public FinderReturn findByFrdCustomerWhitelistBlacklistLikeFR(FrdCustomerWhitelistBlacklist frdCustomerWhitelistBlacklist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
