package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafRole;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafRoleSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafRole
	 */
	public List<RafRole> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafRole persists a country
	 * 
	 * @param rafRole
	 * @return the persisted RafRole
	 */
	public RafRole persistRafRole(RafRole rafRole);

	/**
	 * persistRafRoleList - persists a list of RafRole
	 * 
	 * @param rafRoleList
	 * @return the list of persisted RafRole
	 */
	public ArrayList<RafRole> persistRafRoleList(
			List<RafRole> rafRoleList);

	/**
	 * mergeRafRole - merges a RafRole
	 * 
	 * @param rafRole
	 * @return the merged RafRole
	 */
	public RafRole mergeRafRole(RafRole rafRole);

	/**
	 * mergeRafRoleList - merges a list of RafRole
	 * 
	 * @param rafRoleList
	 * @return the merged list of RafRole
	 */
	public ArrayList<RafRole> mergeRafRoleList(
			List<RafRole> rafRoleList);

	/**
	 * removeRafRole - removes a RafRole
	 * 
	 * @param rafRole
	 */
	public void removeRafRole(RafRole rafRole);

	/**
	 * removeRafRoleList - removes a list of RafRole
	 * 
	 * @param rafRoleList
	 */
	public void removeRafRoleList(List<RafRole> rafRoleList);

	/**
	 * findByRafRoleLike - finds a list of RafRole Like
	 * 
	 * @param rafRole
	 * @return the list of RafRole found
	 */
	public List<RafRole> findByRafRoleLike(RafRole rafRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafRole>LikeFR - finds a list of RafRole> Like with a finder return object
	 * 
	 * @param rafRole
	 * @return the list of RafRole found
	 */
	public FinderReturn findByRafRoleLikeFR(RafRole rafRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
