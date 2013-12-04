package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdFraudSuspicionPoint;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdFraudSuspicionPointSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdFraudSuspicionPoint> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote#persistFrdFraudSuspicionPoint(com
	 * .gdn.venice.persistence.FrdFraudSuspicionPoint)
	 */
	public FrdFraudSuspicionPoint persistFrdFraudSuspicionPoint(FrdFraudSuspicionPoint frdFraudSuspicionPoint);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote#persistFrdFraudSuspicionPointList
	 * (java.util.List)
	 */
	public ArrayList<FrdFraudSuspicionPoint> persistFrdFraudSuspicionPointList(
			List<FrdFraudSuspicionPoint> frdFraudSuspicionPointList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote#mergeFrdFraudSuspicionPoint(com.
	 * gdn.venice.persistence.FrdFraudSuspicionPoint)
	 */
	public FrdFraudSuspicionPoint mergeFrdFraudSuspicionPoint(FrdFraudSuspicionPoint frdFraudSuspicionPoint);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote#mergeFrdFraudSuspicionPointList(
	 * java.util.List)
	 */
	public ArrayList<FrdFraudSuspicionPoint> mergeFrdFraudSuspicionPointList(
			List<FrdFraudSuspicionPoint> frdFraudSuspicionPointList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote#removeFrdFraudSuspicionPoint(com
	 * .gdn.venice.persistence.FrdFraudSuspicionPoint)
	 */
	public void removeFrdFraudSuspicionPoint(FrdFraudSuspicionPoint frdFraudSuspicionPoint);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote#removeFrdFraudSuspicionPointList
	 * (java.util.List)
	 */
	public void removeFrdFraudSuspicionPointList(List<FrdFraudSuspicionPoint> frdFraudSuspicionPointList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote#findByFrdFraudSuspicionPointLike
	 * (com.gdn.venice.persistence.FrdFraudSuspicionPoint, int, int)
	 */
	public List<FrdFraudSuspicionPoint> findByFrdFraudSuspicionPointLike(FrdFraudSuspicionPoint frdFraudSuspicionPoint,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdFraudSuspicionPointSessionEJBRemote#findByFrdFraudSuspicionPointLikeFR
	 * (com.gdn.venice.persistence.FrdFraudSuspicionPoint, int, int)
	 */
	public FinderReturn findByFrdFraudSuspicionPointLikeFR(FrdFraudSuspicionPoint frdFraudSuspicionPoint,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
