package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafRoleProfile;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafRoleProfileSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleProfileSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafRoleProfile> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleProfileSessionEJBRemote#persistRafRoleProfile(com
	 * .gdn.venice.persistence.RafRoleProfile)
	 */
	public RafRoleProfile persistRafRoleProfile(RafRoleProfile rafRoleProfile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleProfileSessionEJBRemote#persistRafRoleProfileList
	 * (java.util.List)
	 */
	public ArrayList<RafRoleProfile> persistRafRoleProfileList(
			List<RafRoleProfile> rafRoleProfileList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleProfileSessionEJBRemote#mergeRafRoleProfile(com.
	 * gdn.venice.persistence.RafRoleProfile)
	 */
	public RafRoleProfile mergeRafRoleProfile(RafRoleProfile rafRoleProfile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleProfileSessionEJBRemote#mergeRafRoleProfileList(
	 * java.util.List)
	 */
	public ArrayList<RafRoleProfile> mergeRafRoleProfileList(
			List<RafRoleProfile> rafRoleProfileList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleProfileSessionEJBRemote#removeRafRoleProfile(com
	 * .gdn.venice.persistence.RafRoleProfile)
	 */
	public void removeRafRoleProfile(RafRoleProfile rafRoleProfile);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleProfileSessionEJBRemote#removeRafRoleProfileList
	 * (java.util.List)
	 */
	public void removeRafRoleProfileList(List<RafRoleProfile> rafRoleProfileList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleProfileSessionEJBRemote#findByRafRoleProfileLike
	 * (com.gdn.venice.persistence.RafRoleProfile, int, int)
	 */
	public List<RafRoleProfile> findByRafRoleProfileLike(RafRoleProfile rafRoleProfile,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafRoleProfileSessionEJBRemote#findByRafRoleProfileLikeFR
	 * (com.gdn.venice.persistence.RafRoleProfile, int, int)
	 */
	public FinderReturn findByRafRoleProfileLikeFR(RafRoleProfile rafRoleProfile,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
