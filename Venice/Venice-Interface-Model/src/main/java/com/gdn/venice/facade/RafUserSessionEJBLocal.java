package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafUser;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafUserSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafUser> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserSessionEJBRemote#persistRafUser(com
	 * .gdn.venice.persistence.RafUser)
	 */
	public RafUser persistRafUser(RafUser rafUser);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserSessionEJBRemote#persistRafUserList
	 * (java.util.List)
	 */
	public ArrayList<RafUser> persistRafUserList(
			List<RafUser> rafUserList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserSessionEJBRemote#mergeRafUser(com.
	 * gdn.venice.persistence.RafUser)
	 */
	public RafUser mergeRafUser(RafUser rafUser);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserSessionEJBRemote#mergeRafUserList(
	 * java.util.List)
	 */
	public ArrayList<RafUser> mergeRafUserList(
			List<RafUser> rafUserList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserSessionEJBRemote#removeRafUser(com
	 * .gdn.venice.persistence.RafUser)
	 */
	public void removeRafUser(RafUser rafUser);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserSessionEJBRemote#removeRafUserList
	 * (java.util.List)
	 */
	public void removeRafUserList(List<RafUser> rafUserList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserSessionEJBRemote#findByRafUserLike
	 * (com.gdn.venice.persistence.RafUser, int, int)
	 */
	public List<RafUser> findByRafUserLike(RafUser rafUser,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafUserSessionEJBRemote#findByRafUserLikeFR
	 * (com.gdn.venice.persistence.RafUser, int, int)
	 */
	public FinderReturn findByRafUserLikeFR(RafUser rafUser,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
