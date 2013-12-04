package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdCustomerWhitelist;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdCustomerWhitelistSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdCustomerWhitelist> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote#persistFrdCustomerWhitelist(com
	 * .gdn.venice.persistence.FrdCustomerWhitelist)
	 */
	public FrdCustomerWhitelist persistFrdCustomerWhitelist(FrdCustomerWhitelist frdCustomerWhitelist);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote#persistFrdCustomerWhitelistList
	 * (java.util.List)
	 */
	public ArrayList<FrdCustomerWhitelist> persistFrdCustomerWhitelistList(
			List<FrdCustomerWhitelist> frdCustomerWhitelistList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote#mergeFrdCustomerWhitelist(com.
	 * gdn.venice.persistence.FrdCustomerWhitelist)
	 */
	public FrdCustomerWhitelist mergeFrdCustomerWhitelist(FrdCustomerWhitelist frdCustomerWhitelist);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote#mergeFrdCustomerWhitelistList(
	 * java.util.List)
	 */
	public ArrayList<FrdCustomerWhitelist> mergeFrdCustomerWhitelistList(
			List<FrdCustomerWhitelist> frdCustomerWhitelistList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote#removeFrdCustomerWhitelist(com
	 * .gdn.venice.persistence.FrdCustomerWhitelist)
	 */
	public void removeFrdCustomerWhitelist(FrdCustomerWhitelist frdCustomerWhitelist);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote#removeFrdCustomerWhitelistList
	 * (java.util.List)
	 */
	public void removeFrdCustomerWhitelistList(List<FrdCustomerWhitelist> frdCustomerWhitelistList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote#findByFrdCustomerWhitelistLike
	 * (com.gdn.venice.persistence.FrdCustomerWhitelist, int, int)
	 */
	public List<FrdCustomerWhitelist> findByFrdCustomerWhitelistLike(FrdCustomerWhitelist frdCustomerWhitelist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdCustomerWhitelistSessionEJBRemote#findByFrdCustomerWhitelistLikeFR
	 * (com.gdn.venice.persistence.FrdCustomerWhitelist, int, int)
	 */
	public FinderReturn findByFrdCustomerWhitelistLikeFR(FrdCustomerWhitelist frdCustomerWhitelist,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
