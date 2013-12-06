package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafProfilePermission;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafProfilePermissionSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafProfilePermission
	 */
	public List<RafProfilePermission> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafProfilePermission persists a country
	 * 
	 * @param rafProfilePermission
	 * @return the persisted RafProfilePermission
	 */
	public RafProfilePermission persistRafProfilePermission(RafProfilePermission rafProfilePermission);

	/**
	 * persistRafProfilePermissionList - persists a list of RafProfilePermission
	 * 
	 * @param rafProfilePermissionList
	 * @return the list of persisted RafProfilePermission
	 */
	public ArrayList<RafProfilePermission> persistRafProfilePermissionList(
			List<RafProfilePermission> rafProfilePermissionList);

	/**
	 * mergeRafProfilePermission - merges a RafProfilePermission
	 * 
	 * @param rafProfilePermission
	 * @return the merged RafProfilePermission
	 */
	public RafProfilePermission mergeRafProfilePermission(RafProfilePermission rafProfilePermission);

	/**
	 * mergeRafProfilePermissionList - merges a list of RafProfilePermission
	 * 
	 * @param rafProfilePermissionList
	 * @return the merged list of RafProfilePermission
	 */
	public ArrayList<RafProfilePermission> mergeRafProfilePermissionList(
			List<RafProfilePermission> rafProfilePermissionList);

	/**
	 * removeRafProfilePermission - removes a RafProfilePermission
	 * 
	 * @param rafProfilePermission
	 */
	public void removeRafProfilePermission(RafProfilePermission rafProfilePermission);

	/**
	 * removeRafProfilePermissionList - removes a list of RafProfilePermission
	 * 
	 * @param rafProfilePermissionList
	 */
	public void removeRafProfilePermissionList(List<RafProfilePermission> rafProfilePermissionList);

	/**
	 * findByRafProfilePermissionLike - finds a list of RafProfilePermission Like
	 * 
	 * @param rafProfilePermission
	 * @return the list of RafProfilePermission found
	 */
	public List<RafProfilePermission> findByRafProfilePermissionLike(RafProfilePermission rafProfilePermission,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafProfilePermission>LikeFR - finds a list of RafProfilePermission> Like with a finder return object
	 * 
	 * @param rafProfilePermission
	 * @return the list of RafProfilePermission found
	 */
	public FinderReturn findByRafProfilePermissionLikeFR(RafProfilePermission rafProfilePermission,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
