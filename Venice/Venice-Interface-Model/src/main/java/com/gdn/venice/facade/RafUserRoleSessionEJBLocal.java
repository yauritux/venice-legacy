package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafUserRole;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafUserRoleSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserRoleSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafUserRole> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserRoleSessionEJBRemote#persistRafUserRole(com
	 * .gdn.venice.persistence.RafUserRole)
	 */
	public RafUserRole persistRafUserRole(RafUserRole rafUserRole);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserRoleSessionEJBRemote#persistRafUserRoleList
	 * (java.util.List)
	 */
	public ArrayList<RafUserRole> persistRafUserRoleList(
			List<RafUserRole> rafUserRoleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserRoleSessionEJBRemote#mergeRafUserRole(com.
	 * gdn.venice.persistence.RafUserRole)
	 */
	public RafUserRole mergeRafUserRole(RafUserRole rafUserRole);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserRoleSessionEJBRemote#mergeRafUserRoleList(
	 * java.util.List)
	 */
	public ArrayList<RafUserRole> mergeRafUserRoleList(
			List<RafUserRole> rafUserRoleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserRoleSessionEJBRemote#removeRafUserRole(com
	 * .gdn.venice.persistence.RafUserRole)
	 */
	public void removeRafUserRole(RafUserRole rafUserRole);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserRoleSessionEJBRemote#removeRafUserRoleList
	 * (java.util.List)
	 */
	public void removeRafUserRoleList(List<RafUserRole> rafUserRoleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserRoleSessionEJBRemote#findByRafUserRoleLike
	 * (com.gdn.venice.persistence.RafUserRole, int, int)
	 */
	public List<RafUserRole> findByRafUserRoleLike(RafUserRole rafUserRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserRoleSessionEJBRemote#findByRafUserRoleLikeFR
	 * (com.gdn.venice.persistence.RafUserRole, int, int)
	 */
	public FinderReturn findByRafUserRoleLikeFR(RafUserRole rafUserRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
