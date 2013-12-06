package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafProfile;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafProfileSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfileSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafProfile> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfileSessionEJBRemote#persistRafProfile(com
	 * .gdn.venice.persistence.RafProfile)
	 */
	public RafProfile persistRafProfile(RafProfile rafProfile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfileSessionEJBRemote#persistRafProfileList
	 * (java.util.List)
	 */
	public ArrayList<RafProfile> persistRafProfileList(
			List<RafProfile> rafProfileList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfileSessionEJBRemote#mergeRafProfile(com.
	 * gdn.venice.persistence.RafProfile)
	 */
	public RafProfile mergeRafProfile(RafProfile rafProfile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfileSessionEJBRemote#mergeRafProfileList(
	 * java.util.List)
	 */
	public ArrayList<RafProfile> mergeRafProfileList(
			List<RafProfile> rafProfileList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfileSessionEJBRemote#removeRafProfile(com
	 * .gdn.venice.persistence.RafProfile)
	 */
	public void removeRafProfile(RafProfile rafProfile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfileSessionEJBRemote#removeRafProfileList
	 * (java.util.List)
	 */
	public void removeRafProfileList(List<RafProfile> rafProfileList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfileSessionEJBRemote#findByRafProfileLike
	 * (com.gdn.venice.persistence.RafProfile, int, int)
	 */
	public List<RafProfile> findByRafProfileLike(RafProfile rafProfile,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafProfileSessionEJBRemote#findByRafProfileLikeFR
	 * (com.gdn.venice.persistence.RafProfile, int, int)
	 */
	public FinderReturn findByRafProfileLikeFR(RafProfile rafProfile,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
