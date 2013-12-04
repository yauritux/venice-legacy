package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafProcessInstance;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafProcessInstanceSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessInstanceSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafProcessInstance> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessInstanceSessionEJBRemote#persistRafProcessInstance(com
	 * .gdn.venice.persistence.RafProcessInstance)
	 */
	public RafProcessInstance persistRafProcessInstance(RafProcessInstance rafProcessInstance);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessInstanceSessionEJBRemote#persistRafProcessInstanceList
	 * (java.util.List)
	 */
	public ArrayList<RafProcessInstance> persistRafProcessInstanceList(
			List<RafProcessInstance> rafProcessInstanceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessInstanceSessionEJBRemote#mergeRafProcessInstance(com.
	 * gdn.venice.persistence.RafProcessInstance)
	 */
	public RafProcessInstance mergeRafProcessInstance(RafProcessInstance rafProcessInstance);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessInstanceSessionEJBRemote#mergeRafProcessInstanceList(
	 * java.util.List)
	 */
	public ArrayList<RafProcessInstance> mergeRafProcessInstanceList(
			List<RafProcessInstance> rafProcessInstanceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessInstanceSessionEJBRemote#removeRafProcessInstance(com
	 * .gdn.venice.persistence.RafProcessInstance)
	 */
	public void removeRafProcessInstance(RafProcessInstance rafProcessInstance);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessInstanceSessionEJBRemote#removeRafProcessInstanceList
	 * (java.util.List)
	 */
	public void removeRafProcessInstanceList(List<RafProcessInstance> rafProcessInstanceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessInstanceSessionEJBRemote#findByRafProcessInstanceLike
	 * (com.gdn.venice.persistence.RafProcessInstance, int, int)
	 */
	public List<RafProcessInstance> findByRafProcessInstanceLike(RafProcessInstance rafProcessInstance,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessInstanceSessionEJBRemote#findByRafProcessInstanceLikeFR
	 * (com.gdn.venice.persistence.RafProcessInstance, int, int)
	 */
	public FinderReturn findByRafProcessInstanceLikeFR(RafProcessInstance rafProcessInstance,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
