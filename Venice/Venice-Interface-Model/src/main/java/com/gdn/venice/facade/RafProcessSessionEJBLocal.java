package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafProcess;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafProcessSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafProcess> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessSessionEJBRemote#persistRafProcess(com
	 * .gdn.venice.persistence.RafProcess)
	 */
	public RafProcess persistRafProcess(RafProcess rafProcess);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessSessionEJBRemote#persistRafProcessList
	 * (java.util.List)
	 */
	public ArrayList<RafProcess> persistRafProcessList(
			List<RafProcess> rafProcessList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessSessionEJBRemote#mergeRafProcess(com.
	 * gdn.venice.persistence.RafProcess)
	 */
	public RafProcess mergeRafProcess(RafProcess rafProcess);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessSessionEJBRemote#mergeRafProcessList(
	 * java.util.List)
	 */
	public ArrayList<RafProcess> mergeRafProcessList(
			List<RafProcess> rafProcessList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessSessionEJBRemote#removeRafProcess(com
	 * .gdn.venice.persistence.RafProcess)
	 */
	public void removeRafProcess(RafProcess rafProcess);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessSessionEJBRemote#removeRafProcessList
	 * (java.util.List)
	 */
	public void removeRafProcessList(List<RafProcess> rafProcessList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessSessionEJBRemote#findByRafProcessLike
	 * (com.gdn.venice.persistence.RafProcess, int, int)
	 */
	public List<RafProcess> findByRafProcessLike(RafProcess rafProcess,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProcessSessionEJBRemote#findByRafProcessLikeFR
	 * (com.gdn.venice.persistence.RafProcess, int, int)
	 */
	public FinderReturn findByRafProcessLikeFR(RafProcess rafProcess,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
