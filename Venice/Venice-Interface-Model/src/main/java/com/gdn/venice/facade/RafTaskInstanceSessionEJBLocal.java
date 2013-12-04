package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafTaskInstance;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafTaskInstanceSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafTaskInstanceSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafTaskInstance> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafTaskInstanceSessionEJBRemote#persistRafTaskInstance(com
	 * .gdn.venice.persistence.RafTaskInstance)
	 */
	public RafTaskInstance persistRafTaskInstance(RafTaskInstance rafTaskInstance);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafTaskInstanceSessionEJBRemote#persistRafTaskInstanceList
	 * (java.util.List)
	 */
	public ArrayList<RafTaskInstance> persistRafTaskInstanceList(
			List<RafTaskInstance> rafTaskInstanceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafTaskInstanceSessionEJBRemote#mergeRafTaskInstance(com.
	 * gdn.venice.persistence.RafTaskInstance)
	 */
	public RafTaskInstance mergeRafTaskInstance(RafTaskInstance rafTaskInstance);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafTaskInstanceSessionEJBRemote#mergeRafTaskInstanceList(
	 * java.util.List)
	 */
	public ArrayList<RafTaskInstance> mergeRafTaskInstanceList(
			List<RafTaskInstance> rafTaskInstanceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafTaskInstanceSessionEJBRemote#removeRafTaskInstance(com
	 * .gdn.venice.persistence.RafTaskInstance)
	 */
	public void removeRafTaskInstance(RafTaskInstance rafTaskInstance);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafTaskInstanceSessionEJBRemote#removeRafTaskInstanceList
	 * (java.util.List)
	 */
	public void removeRafTaskInstanceList(List<RafTaskInstance> rafTaskInstanceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafTaskInstanceSessionEJBRemote#findByRafTaskInstanceLike
	 * (com.gdn.venice.persistence.RafTaskInstance, int, int)
	 */
	public List<RafTaskInstance> findByRafTaskInstanceLike(RafTaskInstance rafTaskInstance,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafTaskInstanceSessionEJBRemote#findByRafTaskInstanceLikeFR
	 * (com.gdn.venice.persistence.RafTaskInstance, int, int)
	 */
	public FinderReturn findByRafTaskInstanceLikeFR(RafTaskInstance rafTaskInstance,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
