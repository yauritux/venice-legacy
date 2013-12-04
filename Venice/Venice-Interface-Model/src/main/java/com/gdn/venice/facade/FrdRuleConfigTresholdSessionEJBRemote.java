package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdRuleConfigTreshold;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdRuleConfigTresholdSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdRuleConfigTreshold
	 */
	public List<FrdRuleConfigTreshold> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdRuleConfigTreshold persists a country
	 * 
	 * @param frdRuleConfigTreshold
	 * @return the persisted FrdRuleConfigTreshold
	 */
	public FrdRuleConfigTreshold persistFrdRuleConfigTreshold(FrdRuleConfigTreshold frdRuleConfigTreshold);

	/**
	 * persistFrdRuleConfigTresholdList - persists a list of FrdRuleConfigTreshold
	 * 
	 * @param frdRuleConfigTresholdList
	 * @return the list of persisted FrdRuleConfigTreshold
	 */
	public ArrayList<FrdRuleConfigTreshold> persistFrdRuleConfigTresholdList(
			List<FrdRuleConfigTreshold> frdRuleConfigTresholdList);

	/**
	 * mergeFrdRuleConfigTreshold - merges a FrdRuleConfigTreshold
	 * 
	 * @param frdRuleConfigTreshold
	 * @return the merged FrdRuleConfigTreshold
	 */
	public FrdRuleConfigTreshold mergeFrdRuleConfigTreshold(FrdRuleConfigTreshold frdRuleConfigTreshold);

	/**
	 * mergeFrdRuleConfigTresholdList - merges a list of FrdRuleConfigTreshold
	 * 
	 * @param frdRuleConfigTresholdList
	 * @return the merged list of FrdRuleConfigTreshold
	 */
	public ArrayList<FrdRuleConfigTreshold> mergeFrdRuleConfigTresholdList(
			List<FrdRuleConfigTreshold> frdRuleConfigTresholdList);

	/**
	 * removeFrdRuleConfigTreshold - removes a FrdRuleConfigTreshold
	 * 
	 * @param frdRuleConfigTreshold
	 */
	public void removeFrdRuleConfigTreshold(FrdRuleConfigTreshold frdRuleConfigTreshold);

	/**
	 * removeFrdRuleConfigTresholdList - removes a list of FrdRuleConfigTreshold
	 * 
	 * @param frdRuleConfigTresholdList
	 */
	public void removeFrdRuleConfigTresholdList(List<FrdRuleConfigTreshold> frdRuleConfigTresholdList);

	/**
	 * findByFrdRuleConfigTresholdLike - finds a list of FrdRuleConfigTreshold Like
	 * 
	 * @param frdRuleConfigTreshold
	 * @return the list of FrdRuleConfigTreshold found
	 */
	public List<FrdRuleConfigTreshold> findByFrdRuleConfigTresholdLike(FrdRuleConfigTreshold frdRuleConfigTreshold,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdRuleConfigTreshold>LikeFR - finds a list of FrdRuleConfigTreshold> Like with a finder return object
	 * 
	 * @param frdRuleConfigTreshold
	 * @return the list of FrdRuleConfigTreshold found
	 */
	public FinderReturn findByFrdRuleConfigTresholdLikeFR(FrdRuleConfigTreshold frdRuleConfigTreshold,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
