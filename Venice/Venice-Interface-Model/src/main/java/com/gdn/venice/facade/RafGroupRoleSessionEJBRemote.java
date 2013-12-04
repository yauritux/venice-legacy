package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafGroupRole;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafGroupRoleSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafGroupRole
	 */
	public List<RafGroupRole> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafGroupRole persists a country
	 * 
	 * @param rafGroupRole
	 * @return the persisted RafGroupRole
	 */
	public RafGroupRole persistRafGroupRole(RafGroupRole rafGroupRole);

	/**
	 * persistRafGroupRoleList - persists a list of RafGroupRole
	 * 
	 * @param rafGroupRoleList
	 * @return the list of persisted RafGroupRole
	 */
	public ArrayList<RafGroupRole> persistRafGroupRoleList(
			List<RafGroupRole> rafGroupRoleList);

	/**
	 * mergeRafGroupRole - merges a RafGroupRole
	 * 
	 * @param rafGroupRole
	 * @return the merged RafGroupRole
	 */
	public RafGroupRole mergeRafGroupRole(RafGroupRole rafGroupRole);

	/**
	 * mergeRafGroupRoleList - merges a list of RafGroupRole
	 * 
	 * @param rafGroupRoleList
	 * @return the merged list of RafGroupRole
	 */
	public ArrayList<RafGroupRole> mergeRafGroupRoleList(
			List<RafGroupRole> rafGroupRoleList);

	/**
	 * removeRafGroupRole - removes a RafGroupRole
	 * 
	 * @param rafGroupRole
	 */
	public void removeRafGroupRole(RafGroupRole rafGroupRole);

	/**
	 * removeRafGroupRoleList - removes a list of RafGroupRole
	 * 
	 * @param rafGroupRoleList
	 */
	public void removeRafGroupRoleList(List<RafGroupRole> rafGroupRoleList);

	/**
	 * findByRafGroupRoleLike - finds a list of RafGroupRole Like
	 * 
	 * @param rafGroupRole
	 * @return the list of RafGroupRole found
	 */
	public List<RafGroupRole> findByRafGroupRoleLike(RafGroupRole rafGroupRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafGroupRole>LikeFR - finds a list of RafGroupRole> Like with a finder return object
	 * 
	 * @param rafGroupRole
	 * @return the list of RafGroupRole found
	 */
	public FinderReturn findByRafGroupRoleLikeFR(RafGroupRole rafGroupRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
