package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule17;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule17SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule17SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule17> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule17SessionEJBRemote#persistFrdParameterRule17(com
	 * .gdn.venice.persistence.FrdParameterRule17)
	 */
	public FrdParameterRule17 persistFrdParameterRule17(FrdParameterRule17 frdParameterRule17);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule17SessionEJBRemote#persistFrdParameterRule17List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule17> persistFrdParameterRule17List(
			List<FrdParameterRule17> frdParameterRule17List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule17SessionEJBRemote#mergeFrdParameterRule17(com.
	 * gdn.venice.persistence.FrdParameterRule17)
	 */
	public FrdParameterRule17 mergeFrdParameterRule17(FrdParameterRule17 frdParameterRule17);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule17SessionEJBRemote#mergeFrdParameterRule17List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule17> mergeFrdParameterRule17List(
			List<FrdParameterRule17> frdParameterRule17List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule17SessionEJBRemote#removeFrdParameterRule17(com
	 * .gdn.venice.persistence.FrdParameterRule17)
	 */
	public void removeFrdParameterRule17(FrdParameterRule17 frdParameterRule17);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule17SessionEJBRemote#removeFrdParameterRule17List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule17List(List<FrdParameterRule17> frdParameterRule17List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule17SessionEJBRemote#findByFrdParameterRule17Like
	 * (com.gdn.venice.persistence.FrdParameterRule17, int, int)
	 */
	public List<FrdParameterRule17> findByFrdParameterRule17Like(FrdParameterRule17 frdParameterRule17,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule17SessionEJBRemote#findByFrdParameterRule17LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule17, int, int)
	 */
	public FinderReturn findByFrdParameterRule17LikeFR(FrdParameterRule17 frdParameterRule17,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
