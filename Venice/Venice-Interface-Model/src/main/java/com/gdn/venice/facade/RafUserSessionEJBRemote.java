package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafUser;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafUserSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafUser
	 */
	public List<RafUser> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafUser persists a country
	 * 
	 * @param rafUser
	 * @return the persisted RafUser
	 */
	public RafUser persistRafUser(RafUser rafUser);

	/**
	 * persistRafUserList - persists a list of RafUser
	 * 
	 * @param rafUserList
	 * @return the list of persisted RafUser
	 */
	public ArrayList<RafUser> persistRafUserList(
			List<RafUser> rafUserList);

	/**
	 * mergeRafUser - merges a RafUser
	 * 
	 * @param rafUser
	 * @return the merged RafUser
	 */
	public RafUser mergeRafUser(RafUser rafUser);

	/**
	 * mergeRafUserList - merges a list of RafUser
	 * 
	 * @param rafUserList
	 * @return the merged list of RafUser
	 */
	public ArrayList<RafUser> mergeRafUserList(
			List<RafUser> rafUserList);

	/**
	 * removeRafUser - removes a RafUser
	 * 
	 * @param rafUser
	 */
	public void removeRafUser(RafUser rafUser);

	/**
	 * removeRafUserList - removes a list of RafUser
	 * 
	 * @param rafUserList
	 */
	public void removeRafUserList(List<RafUser> rafUserList);

	/**
	 * findByRafUserLike - finds a list of RafUser Like
	 * 
	 * @param rafUser
	 * @return the list of RafUser found
	 */
	public List<RafUser> findByRafUserLike(RafUser rafUser,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafUser>LikeFR - finds a list of RafUser> Like with a finder return object
	 * 
	 * @param rafUser
	 * @return the list of RafUser found
	 */
	public FinderReturn findByRafUserLikeFR(RafUser rafUser,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
