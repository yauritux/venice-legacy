package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule4;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule4SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule4SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule4> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule4SessionEJBRemote#persistFrdParameterRule4(com
	 * .gdn.venice.persistence.FrdParameterRule4)
	 */
	public FrdParameterRule4 persistFrdParameterRule4(FrdParameterRule4 frdParameterRule4);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule4SessionEJBRemote#persistFrdParameterRule4List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule4> persistFrdParameterRule4List(
			List<FrdParameterRule4> frdParameterRule4List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule4SessionEJBRemote#mergeFrdParameterRule4(com.
	 * gdn.venice.persistence.FrdParameterRule4)
	 */
	public FrdParameterRule4 mergeFrdParameterRule4(FrdParameterRule4 frdParameterRule4);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule4SessionEJBRemote#mergeFrdParameterRule4List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule4> mergeFrdParameterRule4List(
			List<FrdParameterRule4> frdParameterRule4List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule4SessionEJBRemote#removeFrdParameterRule4(com
	 * .gdn.venice.persistence.FrdParameterRule4)
	 */
	public void removeFrdParameterRule4(FrdParameterRule4 frdParameterRule4);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule4SessionEJBRemote#removeFrdParameterRule4List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule4List(List<FrdParameterRule4> frdParameterRule4List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule4SessionEJBRemote#findByFrdParameterRule4Like
	 * (com.gdn.venice.persistence.FrdParameterRule4, int, int)
	 */
	public List<FrdParameterRule4> findByFrdParameterRule4Like(FrdParameterRule4 frdParameterRule4,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule4SessionEJBRemote#findByFrdParameterRule4LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule4, int, int)
	 */
	public FinderReturn findByFrdParameterRule4LikeFR(FrdParameterRule4 frdParameterRule4,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
