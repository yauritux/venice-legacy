package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafUserPermission;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafUserPermissionSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserPermissionSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafUserPermission> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserPermissionSessionEJBRemote#persistRafUserPermission(com
	 * .gdn.venice.persistence.RafUserPermission)
	 */
	public RafUserPermission persistRafUserPermission(RafUserPermission rafUserPermission);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserPermissionSessionEJBRemote#persistRafUserPermissionList
	 * (java.util.List)
	 */
	public ArrayList<RafUserPermission> persistRafUserPermissionList(
			List<RafUserPermission> rafUserPermissionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserPermissionSessionEJBRemote#mergeRafUserPermission(com.
	 * gdn.venice.persistence.RafUserPermission)
	 */
	public RafUserPermission mergeRafUserPermission(RafUserPermission rafUserPermission);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserPermissionSessionEJBRemote#mergeRafUserPermissionList(
	 * java.util.List)
	 */
	public ArrayList<RafUserPermission> mergeRafUserPermissionList(
			List<RafUserPermission> rafUserPermissionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserPermissionSessionEJBRemote#removeRafUserPermission(com
	 * .gdn.venice.persistence.RafUserPermission)
	 */
	public void removeRafUserPermission(RafUserPermission rafUserPermission);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserPermissionSessionEJBRemote#removeRafUserPermissionList
	 * (java.util.List)
	 */
	public void removeRafUserPermissionList(List<RafUserPermission> rafUserPermissionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserPermissionSessionEJBRemote#findByRafUserPermissionLike
	 * (com.gdn.venice.persistence.RafUserPermission, int, int)
	 */
	public List<RafUserPermission> findByRafUserPermissionLike(RafUserPermission rafUserPermission,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserPermissionSessionEJBRemote#findByRafUserPermissionLikeFR
	 * (com.gdn.venice.persistence.RafUserPermission, int, int)
	 */
	public FinderReturn findByRafUserPermissionLikeFR(RafUserPermission rafUserPermission,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
