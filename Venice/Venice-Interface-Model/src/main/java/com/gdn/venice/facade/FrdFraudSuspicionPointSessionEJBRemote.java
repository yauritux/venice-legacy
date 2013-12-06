package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdFraudSuspicionPoint;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdFraudSuspicionPointSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdFraudSuspicionPoint
	 */
	public List<FrdFraudSuspicionPoint> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdFraudSuspicionPoint persists a country
	 * 
	 * @param frdFraudSuspicionPoint
	 * @return the persisted FrdFraudSuspicionPoint
	 */
	public FrdFraudSuspicionPoint persistFrdFraudSuspicionPoint(FrdFraudSuspicionPoint frdFraudSuspicionPoint);

	/**
	 * persistFrdFraudSuspicionPointList - persists a list of FrdFraudSuspicionPoint
	 * 
	 * @param frdFraudSuspicionPointList
	 * @return the list of persisted FrdFraudSuspicionPoint
	 */
	public ArrayList<FrdFraudSuspicionPoint> persistFrdFraudSuspicionPointList(
			List<FrdFraudSuspicionPoint> frdFraudSuspicionPointList);

	/**
	 * mergeFrdFraudSuspicionPoint - merges a FrdFraudSuspicionPoint
	 * 
	 * @param frdFraudSuspicionPoint
	 * @return the merged FrdFraudSuspicionPoint
	 */
	public FrdFraudSuspicionPoint mergeFrdFraudSuspicionPoint(FrdFraudSuspicionPoint frdFraudSuspicionPoint);

	/**
	 * mergeFrdFraudSuspicionPointList - merges a list of FrdFraudSuspicionPoint
	 * 
	 * @param frdFraudSuspicionPointList
	 * @return the merged list of FrdFraudSuspicionPoint
	 */
	public ArrayList<FrdFraudSuspicionPoint> mergeFrdFraudSuspicionPointList(
			List<FrdFraudSuspicionPoint> frdFraudSuspicionPointList);

	/**
	 * removeFrdFraudSuspicionPoint - removes a FrdFraudSuspicionPoint
	 * 
	 * @param frdFraudSuspicionPoint
	 */
	public void removeFrdFraudSuspicionPoint(FrdFraudSuspicionPoint frdFraudSuspicionPoint);

	/**
	 * removeFrdFraudSuspicionPointList - removes a list of FrdFraudSuspicionPoint
	 * 
	 * @param frdFraudSuspicionPointList
	 */
	public void removeFrdFraudSuspicionPointList(List<FrdFraudSuspicionPoint> frdFraudSuspicionPointList);

	/**
	 * findByFrdFraudSuspicionPointLike - finds a list of FrdFraudSuspicionPoint Like
	 * 
	 * @param frdFraudSuspicionPoint
	 * @return the list of FrdFraudSuspicionPoint found
	 */
	public List<FrdFraudSuspicionPoint> findByFrdFraudSuspicionPointLike(FrdFraudSuspicionPoint frdFraudSuspicionPoint,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdFraudSuspicionPoint>LikeFR - finds a list of FrdFraudSuspicionPoint> Like with a finder return object
	 * 
	 * @param frdFraudSuspicionPoint
	 * @return the list of FrdFraudSuspicionPoint found
	 */
	public FinderReturn findByFrdFraudSuspicionPointLikeFR(FrdFraudSuspicionPoint frdFraudSuspicionPoint,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
