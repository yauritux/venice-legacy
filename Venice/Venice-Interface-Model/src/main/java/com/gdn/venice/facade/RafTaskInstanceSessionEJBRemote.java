package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafTaskInstance;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafTaskInstanceSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafTaskInstance
	 */
	public List<RafTaskInstance> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafTaskInstance persists a country
	 * 
	 * @param rafTaskInstance
	 * @return the persisted RafTaskInstance
	 */
	public RafTaskInstance persistRafTaskInstance(RafTaskInstance rafTaskInstance);

	/**
	 * persistRafTaskInstanceList - persists a list of RafTaskInstance
	 * 
	 * @param rafTaskInstanceList
	 * @return the list of persisted RafTaskInstance
	 */
	public ArrayList<RafTaskInstance> persistRafTaskInstanceList(
			List<RafTaskInstance> rafTaskInstanceList);

	/**
	 * mergeRafTaskInstance - merges a RafTaskInstance
	 * 
	 * @param rafTaskInstance
	 * @return the merged RafTaskInstance
	 */
	public RafTaskInstance mergeRafTaskInstance(RafTaskInstance rafTaskInstance);

	/**
	 * mergeRafTaskInstanceList - merges a list of RafTaskInstance
	 * 
	 * @param rafTaskInstanceList
	 * @return the merged list of RafTaskInstance
	 */
	public ArrayList<RafTaskInstance> mergeRafTaskInstanceList(
			List<RafTaskInstance> rafTaskInstanceList);

	/**
	 * removeRafTaskInstance - removes a RafTaskInstance
	 * 
	 * @param rafTaskInstance
	 */
	public void removeRafTaskInstance(RafTaskInstance rafTaskInstance);

	/**
	 * removeRafTaskInstanceList - removes a list of RafTaskInstance
	 * 
	 * @param rafTaskInstanceList
	 */
	public void removeRafTaskInstanceList(List<RafTaskInstance> rafTaskInstanceList);

	/**
	 * findByRafTaskInstanceLike - finds a list of RafTaskInstance Like
	 * 
	 * @param rafTaskInstance
	 * @return the list of RafTaskInstance found
	 */
	public List<RafTaskInstance> findByRafTaskInstanceLike(RafTaskInstance rafTaskInstance,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafTaskInstance>LikeFR - finds a list of RafTaskInstance> Like with a finder return object
	 * 
	 * @param rafTaskInstance
	 * @return the list of RafTaskInstance found
	 */
	public FinderReturn findByRafTaskInstanceLikeFR(RafTaskInstance rafTaskInstance,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
