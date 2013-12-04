package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule19;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule19SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule19SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule19> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule19SessionEJBRemote#persistFrdParameterRule19(com
	 * .gdn.venice.persistence.FrdParameterRule19)
	 */
	public FrdParameterRule19 persistFrdParameterRule19(FrdParameterRule19 frdParameterRule19);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule19SessionEJBRemote#persistFrdParameterRule19List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule19> persistFrdParameterRule19List(
			List<FrdParameterRule19> frdParameterRule19List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule19SessionEJBRemote#mergeFrdParameterRule19(com.
	 * gdn.venice.persistence.FrdParameterRule19)
	 */
	public FrdParameterRule19 mergeFrdParameterRule19(FrdParameterRule19 frdParameterRule19);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule19SessionEJBRemote#mergeFrdParameterRule19List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule19> mergeFrdParameterRule19List(
			List<FrdParameterRule19> frdParameterRule19List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule19SessionEJBRemote#removeFrdParameterRule19(com
	 * .gdn.venice.persistence.FrdParameterRule19)
	 */
	public void removeFrdParameterRule19(FrdParameterRule19 frdParameterRule19);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule19SessionEJBRemote#removeFrdParameterRule19List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule19List(List<FrdParameterRule19> frdParameterRule19List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule19SessionEJBRemote#findByFrdParameterRule19Like
	 * (com.gdn.venice.persistence.FrdParameterRule19, int, int)
	 */
	public List<FrdParameterRule19> findByFrdParameterRule19Like(FrdParameterRule19 frdParameterRule19,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule19SessionEJBRemote#findByFrdParameterRule19LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule19, int, int)
	 */
	public FinderReturn findByFrdParameterRule19LikeFR(FrdParameterRule19 frdParameterRule19,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
