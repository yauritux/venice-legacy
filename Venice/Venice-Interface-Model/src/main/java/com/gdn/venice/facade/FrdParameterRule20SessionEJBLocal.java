package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule20;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule20SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule20SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule20> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule20SessionEJBRemote#persistFrdParameterRule20(com
	 * .gdn.venice.persistence.FrdParameterRule20)
	 */
	public FrdParameterRule20 persistFrdParameterRule20(FrdParameterRule20 frdParameterRule20);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule20SessionEJBRemote#persistFrdParameterRule20List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule20> persistFrdParameterRule20List(
			List<FrdParameterRule20> frdParameterRule20List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule20SessionEJBRemote#mergeFrdParameterRule20(com.
	 * gdn.venice.persistence.FrdParameterRule20)
	 */
	public FrdParameterRule20 mergeFrdParameterRule20(FrdParameterRule20 frdParameterRule20);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule20SessionEJBRemote#mergeFrdParameterRule20List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule20> mergeFrdParameterRule20List(
			List<FrdParameterRule20> frdParameterRule20List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule20SessionEJBRemote#removeFrdParameterRule20(com
	 * .gdn.venice.persistence.FrdParameterRule20)
	 */
	public void removeFrdParameterRule20(FrdParameterRule20 frdParameterRule20);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule20SessionEJBRemote#removeFrdParameterRule20List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule20List(List<FrdParameterRule20> frdParameterRule20List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule20SessionEJBRemote#findByFrdParameterRule20Like
	 * (com.gdn.venice.persistence.FrdParameterRule20, int, int)
	 */
	public List<FrdParameterRule20> findByFrdParameterRule20Like(FrdParameterRule20 frdParameterRule20,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule20SessionEJBRemote#findByFrdParameterRule20LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule20, int, int)
	 */
	public FinderReturn findByFrdParameterRule20LikeFR(FrdParameterRule20 frdParameterRule20,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
