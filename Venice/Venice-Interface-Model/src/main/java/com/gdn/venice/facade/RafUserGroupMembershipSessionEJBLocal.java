package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafUserGroupMembership;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafUserGroupMembershipSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserGroupMembershipSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafUserGroupMembership> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserGroupMembershipSessionEJBRemote#persistRafUserGroupMembership(com
	 * .gdn.venice.persistence.RafUserGroupMembership)
	 */
	public RafUserGroupMembership persistRafUserGroupMembership(RafUserGroupMembership rafUserGroupMembership);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserGroupMembershipSessionEJBRemote#persistRafUserGroupMembershipList
	 * (java.util.List)
	 */
	public ArrayList<RafUserGroupMembership> persistRafUserGroupMembershipList(
			List<RafUserGroupMembership> rafUserGroupMembershipList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserGroupMembershipSessionEJBRemote#mergeRafUserGroupMembership(com.
	 * gdn.venice.persistence.RafUserGroupMembership)
	 */
	public RafUserGroupMembership mergeRafUserGroupMembership(RafUserGroupMembership rafUserGroupMembership);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserGroupMembershipSessionEJBRemote#mergeRafUserGroupMembershipList(
	 * java.util.List)
	 */
	public ArrayList<RafUserGroupMembership> mergeRafUserGroupMembershipList(
			List<RafUserGroupMembership> rafUserGroupMembershipList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserGroupMembershipSessionEJBRemote#removeRafUserGroupMembership(com
	 * .gdn.venice.persistence.RafUserGroupMembership)
	 */
	public void removeRafUserGroupMembership(RafUserGroupMembership rafUserGroupMembership);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserGroupMembershipSessionEJBRemote#removeRafUserGroupMembershipList
	 * (java.util.List)
	 */
	public void removeRafUserGroupMembershipList(List<RafUserGroupMembership> rafUserGroupMembershipList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserGroupMembershipSessionEJBRemote#findByRafUserGroupMembershipLike
	 * (com.gdn.venice.persistence.RafUserGroupMembership, int, int)
	 */
	public List<RafUserGroupMembership> findByRafUserGroupMembershipLike(RafUserGroupMembership rafUserGroupMembership,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserGroupMembershipSessionEJBRemote#findByRafUserGroupMembershipLikeFR
	 * (com.gdn.venice.persistence.RafUserGroupMembership, int, int)
	 */
	public FinderReturn findByRafUserGroupMembershipLikeFR(RafUserGroupMembership rafUserGroupMembership,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
