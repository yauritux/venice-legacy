package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafRoleProfile;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafRoleProfileSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafRoleProfile
	 */
	public List<RafRoleProfile> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafRoleProfile persists a country
	 * 
	 * @param rafRoleProfile
	 * @return the persisted RafRoleProfile
	 */
	public RafRoleProfile persistRafRoleProfile(RafRoleProfile rafRoleProfile);

	/**
	 * persistRafRoleProfileList - persists a list of RafRoleProfile
	 * 
	 * @param rafRoleProfileList
	 * @return the list of persisted RafRoleProfile
	 */
	public ArrayList<RafRoleProfile> persistRafRoleProfileList(
			List<RafRoleProfile> rafRoleProfileList);

	/**
	 * mergeRafRoleProfile - merges a RafRoleProfile
	 * 
	 * @param rafRoleProfile
	 * @return the merged RafRoleProfile
	 */
	public RafRoleProfile mergeRafRoleProfile(RafRoleProfile rafRoleProfile);

	/**
	 * mergeRafRoleProfileList - merges a list of RafRoleProfile
	 * 
	 * @param rafRoleProfileList
	 * @return the merged list of RafRoleProfile
	 */
	public ArrayList<RafRoleProfile> mergeRafRoleProfileList(
			List<RafRoleProfile> rafRoleProfileList);

	/**
	 * removeRafRoleProfile - removes a RafRoleProfile
	 * 
	 * @param rafRoleProfile
	 */
	public void removeRafRoleProfile(RafRoleProfile rafRoleProfile);

	/**
	 * removeRafRoleProfileList - removes a list of RafRoleProfile
	 * 
	 * @param rafRoleProfileList
	 */
	public void removeRafRoleProfileList(List<RafRoleProfile> rafRoleProfileList);

	/**
	 * findByRafRoleProfileLike - finds a list of RafRoleProfile Like
	 * 
	 * @param rafRoleProfile
	 * @return the list of RafRoleProfile found
	 */
	public List<RafRoleProfile> findByRafRoleProfileLike(RafRoleProfile rafRoleProfile,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafRoleProfile>LikeFR - finds a list of RafRoleProfile> Like with a finder return object
	 * 
	 * @param rafRoleProfile
	 * @return the list of RafRoleProfile found
	 */
	public FinderReturn findByRafRoleProfileLikeFR(RafRoleProfile rafRoleProfile,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
