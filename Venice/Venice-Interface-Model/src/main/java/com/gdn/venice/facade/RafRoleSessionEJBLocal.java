package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafRole;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafRoleSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafRole> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleSessionEJBRemote#persistRafRole(com
	 * .gdn.venice.persistence.RafRole)
	 */
	public RafRole persistRafRole(RafRole rafRole);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleSessionEJBRemote#persistRafRoleList
	 * (java.util.List)
	 */
	public ArrayList<RafRole> persistRafRoleList(
			List<RafRole> rafRoleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleSessionEJBRemote#mergeRafRole(com.
	 * gdn.venice.persistence.RafRole)
	 */
	public RafRole mergeRafRole(RafRole rafRole);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleSessionEJBRemote#mergeRafRoleList(
	 * java.util.List)
	 */
	public ArrayList<RafRole> mergeRafRoleList(
			List<RafRole> rafRoleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleSessionEJBRemote#removeRafRole(com
	 * .gdn.venice.persistence.RafRole)
	 */
	public void removeRafRole(RafRole rafRole);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleSessionEJBRemote#removeRafRoleList
	 * (java.util.List)
	 */
	public void removeRafRoleList(List<RafRole> rafRoleList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleSessionEJBRemote#findByRafRoleLike
	 * (com.gdn.venice.persistence.RafRole, int, int)
	 */
	public List<RafRole> findByRafRoleLike(RafRole rafRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleSessionEJBRemote#findByRafRoleLikeFR
	 * (com.gdn.venice.persistence.RafRole, int, int)
	 */
	public FinderReturn findByRafRoleLikeFR(RafRole rafRole,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
