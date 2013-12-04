package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule6;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule6SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule6SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule6> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule6SessionEJBRemote#persistFrdParameterRule6(com
	 * .gdn.venice.persistence.FrdParameterRule6)
	 */
	public FrdParameterRule6 persistFrdParameterRule6(FrdParameterRule6 frdParameterRule6);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule6SessionEJBRemote#persistFrdParameterRule6List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule6> persistFrdParameterRule6List(
			List<FrdParameterRule6> frdParameterRule6List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule6SessionEJBRemote#mergeFrdParameterRule6(com.
	 * gdn.venice.persistence.FrdParameterRule6)
	 */
	public FrdParameterRule6 mergeFrdParameterRule6(FrdParameterRule6 frdParameterRule6);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule6SessionEJBRemote#mergeFrdParameterRule6List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule6> mergeFrdParameterRule6List(
			List<FrdParameterRule6> frdParameterRule6List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule6SessionEJBRemote#removeFrdParameterRule6(com
	 * .gdn.venice.persistence.FrdParameterRule6)
	 */
	public void removeFrdParameterRule6(FrdParameterRule6 frdParameterRule6);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule6SessionEJBRemote#removeFrdParameterRule6List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule6List(List<FrdParameterRule6> frdParameterRule6List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule6SessionEJBRemote#findByFrdParameterRule6Like
	 * (com.gdn.venice.persistence.FrdParameterRule6, int, int)
	 */
	public List<FrdParameterRule6> findByFrdParameterRule6Like(FrdParameterRule6 frdParameterRule6,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule6SessionEJBRemote#findByFrdParameterRule6LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule6, int, int)
	 */
	public FinderReturn findByFrdParameterRule6LikeFR(FrdParameterRule6 frdParameterRule6,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
