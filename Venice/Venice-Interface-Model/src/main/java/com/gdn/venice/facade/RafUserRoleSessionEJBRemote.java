package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;
import com.gdn.venice.persistence.RafUserRole;

@Remote
public interface RafUserRoleSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafUserRole
	 */
	public List<RafUserRole> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafUserRole persists a country
	 * 
	 * @param rafUserRole
	 * @return the persisted RafUserRole
	 */
	public RafUserRole persistRafUserRole(RafUserRole rafUserRole);

	/**
	 * persistRafUserRoleList - persists a list of RafUserRole
	 * 
	 * @param rafUserRoleList
	 * @return the list of persisted RafUserRole
	 */
	public ArrayList<RafUserRole> persistRafUserRoleList(
			List<RafUserRole> rafUserRoleList);

	/**
	 * mergeRafUserRole - merges a RafUserRole
	 * 
	 * @param rafUserRole
	 * @return the merged RafUserRole
	 */
	public RafUserRole mergeRafUserRole(RafUserRole rafUserRole);

	/**
	 * mergeRafUserRoleList - merges a list of RafUserRole
	 * 
	 * @param rafUserRoleList
	 * @return the merged list of RafUserRole
	 */
	public ArrayList<RafUserRole> mergeRafUserRoleList(
			List<RafUserRole> rafUserRoleList);

	/**
	 * removeRafUserRole - removes a RafUserRole
	 * 
	 * @param rafUserRole
	 */
	public void removeRafUserRole(RafUserRole rafUserRole);

	/**
	 * removeRafUserRoleList - removes a list of RafUserRole
	 * 
	 * @param rafUserRoleList
	 */
	public void removeRafUserRoleList(List<RafUserRole> rafUserRoleList);

	/**
	 * findByRafUserRoleLike - finds a list of RafUserRole Like
	 * 
	 * @param rafUserRole
	 * @return the list of RafUserRole found
	 */
	public List<RafUserRole> findByRafUserRoleLike(RafUserRole rafUserRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafUserRole>LikeFR - finds a list of RafUserRole> Like with a finder return object
	 * 
	 * @param rafUserRole
	 * @return the list of RafUserRole found
	 */
	public FinderReturn findByRafUserRoleLikeFR(RafUserRole rafUserRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
