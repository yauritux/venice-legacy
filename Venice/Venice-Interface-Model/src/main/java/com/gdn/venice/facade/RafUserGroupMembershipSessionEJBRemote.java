package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;
import com.gdn.venice.persistence.RafUserGroupMembership;

@Remote
public interface RafUserGroupMembershipSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafUserGroupMembership
	 */
	public List<RafUserGroupMembership> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafUserGroupMembership persists a country
	 * 
	 * @param rafUserGroupMembership
	 * @return the persisted RafUserGroupMembership
	 */
	public RafUserGroupMembership persistRafUserGroupMembership(RafUserGroupMembership rafUserGroupMembership);

	/**
	 * persistRafUserGroupMembershipList - persists a list of RafUserGroupMembership
	 * 
	 * @param rafUserGroupMembershipList
	 * @return the list of persisted RafUserGroupMembership
	 */
	public ArrayList<RafUserGroupMembership> persistRafUserGroupMembershipList(
			List<RafUserGroupMembership> rafUserGroupMembershipList);

	/**
	 * mergeRafUserGroupMembership - merges a RafUserGroupMembership
	 * 
	 * @param rafUserGroupMembership
	 * @return the merged RafUserGroupMembership
	 */
	public RafUserGroupMembership mergeRafUserGroupMembership(RafUserGroupMembership rafUserGroupMembership);

	/**
	 * mergeRafUserGroupMembershipList - merges a list of RafUserGroupMembership
	 * 
	 * @param rafUserGroupMembershipList
	 * @return the merged list of RafUserGroupMembership
	 */
	public ArrayList<RafUserGroupMembership> mergeRafUserGroupMembershipList(
			List<RafUserGroupMembership> rafUserGroupMembershipList);

	/**
	 * removeRafUserGroupMembership - removes a RafUserGroupMembership
	 * 
	 * @param rafUserGroupMembership
	 */
	public void removeRafUserGroupMembership(RafUserGroupMembership rafUserGroupMembership);

	/**
	 * removeRafUserGroupMembershipList - removes a list of RafUserGroupMembership
	 * 
	 * @param rafUserGroupMembershipList
	 */
	public void removeRafUserGroupMembershipList(List<RafUserGroupMembership> rafUserGroupMembershipList);

	/**
	 * findByRafUserGroupMembershipLike - finds a list of RafUserGroupMembership Like
	 * 
	 * @param rafUserGroupMembership
	 * @return the list of RafUserGroupMembership found
	 */
	public List<RafUserGroupMembership> findByRafUserGroupMembershipLike(RafUserGroupMembership rafUserGroupMembership,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafUserGroupMembership>LikeFR - finds a list of RafUserGroupMembership> Like with a finder return object
	 * 
	 * @param rafUserGroupMembership
	 * @return the list of RafUserGroupMembership found
	 */
	public FinderReturn findByRafUserGroupMembershipLikeFR(RafUserGroupMembership rafUserGroupMembership,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
