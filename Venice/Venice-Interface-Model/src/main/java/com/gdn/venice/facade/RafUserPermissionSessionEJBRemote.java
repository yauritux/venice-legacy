package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafUserPermission;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafUserPermissionSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafUserPermission
	 */
	public List<RafUserPermission> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafUserPermission persists a country
	 * 
	 * @param rafUserPermission
	 * @return the persisted RafUserPermission
	 */
	public RafUserPermission persistRafUserPermission(RafUserPermission rafUserPermission);

	/**
	 * persistRafUserPermissionList - persists a list of RafUserPermission
	 * 
	 * @param rafUserPermissionList
	 * @return the list of persisted RafUserPermission
	 */
	public ArrayList<RafUserPermission> persistRafUserPermissionList(
			List<RafUserPermission> rafUserPermissionList);

	/**
	 * mergeRafUserPermission - merges a RafUserPermission
	 * 
	 * @param rafUserPermission
	 * @return the merged RafUserPermission
	 */
	public RafUserPermission mergeRafUserPermission(RafUserPermission rafUserPermission);

	/**
	 * mergeRafUserPermissionList - merges a list of RafUserPermission
	 * 
	 * @param rafUserPermissionList
	 * @return the merged list of RafUserPermission
	 */
	public ArrayList<RafUserPermission> mergeRafUserPermissionList(
			List<RafUserPermission> rafUserPermissionList);

	/**
	 * removeRafUserPermission - removes a RafUserPermission
	 * 
	 * @param rafUserPermission
	 */
	public void removeRafUserPermission(RafUserPermission rafUserPermission);

	/**
	 * removeRafUserPermissionList - removes a list of RafUserPermission
	 * 
	 * @param rafUserPermissionList
	 */
	public void removeRafUserPermissionList(List<RafUserPermission> rafUserPermissionList);

	/**
	 * findByRafUserPermissionLike - finds a list of RafUserPermission Like
	 * 
	 * @param rafUserPermission
	 * @return the list of RafUserPermission found
	 */
	public List<RafUserPermission> findByRafUserPermissionLike(RafUserPermission rafUserPermission,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafUserPermission>LikeFR - finds a list of RafUserPermission> Like with a finder return object
	 * 
	 * @param rafUserPermission
	 * @return the list of RafUserPermission found
	 */
	public FinderReturn findByRafUserPermissionLikeFR(RafUserPermission rafUserPermission,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
