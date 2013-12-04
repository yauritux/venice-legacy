package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafProfile;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafProfileSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafProfile
	 */
	public List<RafProfile> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafProfile persists a country
	 * 
	 * @param rafProfile
	 * @return the persisted RafProfile
	 */
	public RafProfile persistRafProfile(RafProfile rafProfile);

	/**
	 * persistRafProfileList - persists a list of RafProfile
	 * 
	 * @param rafProfileList
	 * @return the list of persisted RafProfile
	 */
	public ArrayList<RafProfile> persistRafProfileList(
			List<RafProfile> rafProfileList);

	/**
	 * mergeRafProfile - merges a RafProfile
	 * 
	 * @param rafProfile
	 * @return the merged RafProfile
	 */
	public RafProfile mergeRafProfile(RafProfile rafProfile);

	/**
	 * mergeRafProfileList - merges a list of RafProfile
	 * 
	 * @param rafProfileList
	 * @return the merged list of RafProfile
	 */
	public ArrayList<RafProfile> mergeRafProfileList(
			List<RafProfile> rafProfileList);

	/**
	 * removeRafProfile - removes a RafProfile
	 * 
	 * @param rafProfile
	 */
	public void removeRafProfile(RafProfile rafProfile);

	/**
	 * removeRafProfileList - removes a list of RafProfile
	 * 
	 * @param rafProfileList
	 */
	public void removeRafProfileList(List<RafProfile> rafProfileList);

	/**
	 * findByRafProfileLike - finds a list of RafProfile Like
	 * 
	 * @param rafProfile
	 * @return the list of RafProfile found
	 */
	public List<RafProfile> findByRafProfileLike(RafProfile rafProfile,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafProfile>LikeFR - finds a list of RafProfile> Like with a finder return object
	 * 
	 * @param rafProfile
	 * @return the list of RafProfile found
	 */
	public FinderReturn findByRafProfileLikeFR(RafProfile rafProfile,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
