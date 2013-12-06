package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafGroup;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafGroupSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafGroup> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupSessionEJBRemote#persistRafGroup(com
	 * .gdn.venice.persistence.RafGroup)
	 */
	public RafGroup persistRafGroup(RafGroup rafGroup);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupSessionEJBRemote#persistRafGroupList
	 * (java.util.List)
	 */
	public ArrayList<RafGroup> persistRafGroupList(
			List<RafGroup> rafGroupList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupSessionEJBRemote#mergeRafGroup(com.
	 * gdn.venice.persistence.RafGroup)
	 */
	public RafGroup mergeRafGroup(RafGroup rafGroup);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupSessionEJBRemote#mergeRafGroupList(
	 * java.util.List)
	 */
	public ArrayList<RafGroup> mergeRafGroupList(
			List<RafGroup> rafGroupList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupSessionEJBRemote#removeRafGroup(com
	 * .gdn.venice.persistence.RafGroup)
	 */
	public void removeRafGroup(RafGroup rafGroup);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupSessionEJBRemote#removeRafGroupList
	 * (java.util.List)
	 */
	public void removeRafGroupList(List<RafGroup> rafGroupList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupSessionEJBRemote#findByRafGroupLike
	 * (com.gdn.venice.persistence.RafGroup, int, int)
	 */
	public List<RafGroup> findByRafGroupLike(RafGroup rafGroup,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafGroupSessionEJBRemote#findByRafGroupLikeFR
	 * (com.gdn.venice.persistence.RafGroup, int, int)
	 */
	public FinderReturn findByRafGroupLikeFR(RafGroup rafGroup,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
