package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafGroup;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafGroupSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafGroup
	 */
	public List<RafGroup> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafGroup persists a country
	 * 
	 * @param rafGroup
	 * @return the persisted RafGroup
	 */
	public RafGroup persistRafGroup(RafGroup rafGroup);

	/**
	 * persistRafGroupList - persists a list of RafGroup
	 * 
	 * @param rafGroupList
	 * @return the list of persisted RafGroup
	 */
	public ArrayList<RafGroup> persistRafGroupList(
			List<RafGroup> rafGroupList);

	/**
	 * mergeRafGroup - merges a RafGroup
	 * 
	 * @param rafGroup
	 * @return the merged RafGroup
	 */
	public RafGroup mergeRafGroup(RafGroup rafGroup);

	/**
	 * mergeRafGroupList - merges a list of RafGroup
	 * 
	 * @param rafGroupList
	 * @return the merged list of RafGroup
	 */
	public ArrayList<RafGroup> mergeRafGroupList(
			List<RafGroup> rafGroupList);

	/**
	 * removeRafGroup - removes a RafGroup
	 * 
	 * @param rafGroup
	 */
	public void removeRafGroup(RafGroup rafGroup);

	/**
	 * removeRafGroupList - removes a list of RafGroup
	 * 
	 * @param rafGroupList
	 */
	public void removeRafGroupList(List<RafGroup> rafGroupList);

	/**
	 * findByRafGroupLike - finds a list of RafGroup Like
	 * 
	 * @param rafGroup
	 * @return the list of RafGroup found
	 */
	public List<RafGroup> findByRafGroupLike(RafGroup rafGroup,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafGroup>LikeFR - finds a list of RafGroup> Like with a finder return object
	 * 
	 * @param rafGroup
	 * @return the list of RafGroup found
	 */
	public FinderReturn findByRafGroupLikeFR(RafGroup rafGroup,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
