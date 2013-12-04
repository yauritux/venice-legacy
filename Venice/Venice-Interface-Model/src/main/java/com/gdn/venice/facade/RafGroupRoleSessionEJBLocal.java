package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafGroupRole;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafGroupRoleSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupRoleSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafGroupRole> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupRoleSessionEJBRemote#persistRafGroupRole(com
	 * .gdn.venice.persistence.RafGroupRole)
	 */
	public RafGroupRole persistRafGroupRole(RafGroupRole rafGroupRole);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupRoleSessionEJBRemote#persistRafGroupRoleList
	 * (java.util.List)
	 */
	public ArrayList<RafGroupRole> persistRafGroupRoleList(
			List<RafGroupRole> rafGroupRoleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupRoleSessionEJBRemote#mergeRafGroupRole(com.
	 * gdn.venice.persistence.RafGroupRole)
	 */
	public RafGroupRole mergeRafGroupRole(RafGroupRole rafGroupRole);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupRoleSessionEJBRemote#mergeRafGroupRoleList(
	 * java.util.List)
	 */
	public ArrayList<RafGroupRole> mergeRafGroupRoleList(
			List<RafGroupRole> rafGroupRoleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupRoleSessionEJBRemote#removeRafGroupRole(com
	 * .gdn.venice.persistence.RafGroupRole)
	 */
	public void removeRafGroupRole(RafGroupRole rafGroupRole);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupRoleSessionEJBRemote#removeRafGroupRoleList
	 * (java.util.List)
	 */
	public void removeRafGroupRoleList(List<RafGroupRole> rafGroupRoleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupRoleSessionEJBRemote#findByRafGroupRoleLike
	 * (com.gdn.venice.persistence.RafGroupRole, int, int)
	 */
	public List<RafGroupRole> findByRafGroupRoleLike(RafGroupRole rafGroupRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupRoleSessionEJBRemote#findByRafGroupRoleLikeFR
	 * (com.gdn.venice.persistence.RafGroupRole, int, int)
	 */
	public FinderReturn findByRafGroupRoleLikeFR(RafGroupRole rafGroupRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
