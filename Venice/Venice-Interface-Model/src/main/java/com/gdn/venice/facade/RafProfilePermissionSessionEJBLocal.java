package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafProfilePermission;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafProfilePermissionSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafProfilePermission> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote#persistRafProfilePermission(com
	 * .gdn.venice.persistence.RafProfilePermission)
	 */
	public RafProfilePermission persistRafProfilePermission(RafProfilePermission rafProfilePermission);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote#persistRafProfilePermissionList
	 * (java.util.List)
	 */
	public ArrayList<RafProfilePermission> persistRafProfilePermissionList(
			List<RafProfilePermission> rafProfilePermissionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote#mergeRafProfilePermission(com.
	 * gdn.venice.persistence.RafProfilePermission)
	 */
	public RafProfilePermission mergeRafProfilePermission(RafProfilePermission rafProfilePermission);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote#mergeRafProfilePermissionList(
	 * java.util.List)
	 */
	public ArrayList<RafProfilePermission> mergeRafProfilePermissionList(
			List<RafProfilePermission> rafProfilePermissionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote#removeRafProfilePermission(com
	 * .gdn.venice.persistence.RafProfilePermission)
	 */
	public void removeRafProfilePermission(RafProfilePermission rafProfilePermission);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote#removeRafProfilePermissionList
	 * (java.util.List)
	 */
	public void removeRafProfilePermissionList(List<RafProfilePermission> rafProfilePermissionList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote#findByRafProfilePermissionLike
	 * (com.gdn.venice.persistence.RafProfilePermission, int, int)
	 */
	public List<RafProfilePermission> findByRafProfilePermissionLike(RafProfilePermission rafProfilePermission,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfilePermissionSessionEJBRemote#findByRafProfilePermissionLikeFR
	 * (com.gdn.venice.persistence.RafProfilePermission, int, int)
	 */
	public FinderReturn findByRafProfilePermissionLikeFR(RafProfilePermission rafProfilePermission,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
