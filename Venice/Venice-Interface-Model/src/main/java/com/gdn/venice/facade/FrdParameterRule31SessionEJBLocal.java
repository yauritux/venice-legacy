package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule31;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule31SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule31> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote#persistFrdParameterRule31(com
	 * .gdn.venice.persistence.FrdParameterRule31)
	 */
	public FrdParameterRule31 persistFrdParameterRule31(FrdParameterRule31 frdParameterRule31);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote#persistFrdParameterRule31List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule31> persistFrdParameterRule31List(
			List<FrdParameterRule31> frdParameterRule31List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote#mergeFrdParameterRule31(com.
	 * gdn.venice.persistence.FrdParameterRule31)
	 */
	public FrdParameterRule31 mergeFrdParameterRule31(FrdParameterRule31 frdParameterRule31);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote#mergeFrdParameterRule31List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule31> mergeFrdParameterRule31List(
			List<FrdParameterRule31> frdParameterRule31List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote#removeFrdParameterRule31(com
	 * .gdn.venice.persistence.FrdParameterRule31)
	 */
	public void removeFrdParameterRule31(FrdParameterRule31 frdParameterRule31);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote#removeFrdParameterRule31List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule31List(List<FrdParameterRule31> frdParameterRule31List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote#findByFrdParameterRule31Like
	 * (com.gdn.venice.persistence.FrdParameterRule31, int, int)
	 */
	public List<FrdParameterRule31> findByFrdParameterRule31Like(FrdParameterRule31 frdParameterRule31,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule31SessionEJBRemote#findByFrdParameterRule31LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule31, int, int)
	 */
	public FinderReturn findByFrdParameterRule31LikeFR(FrdParameterRule31 frdParameterRule31,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
