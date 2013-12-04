package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule24;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule24SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule24SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule24> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule24SessionEJBRemote#persistFrdParameterRule24(com
	 * .gdn.venice.persistence.FrdParameterRule24)
	 */
	public FrdParameterRule24 persistFrdParameterRule24(FrdParameterRule24 frdParameterRule24);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule24SessionEJBRemote#persistFrdParameterRule24List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule24> persistFrdParameterRule24List(
			List<FrdParameterRule24> frdParameterRule24List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule24SessionEJBRemote#mergeFrdParameterRule24(com.
	 * gdn.venice.persistence.FrdParameterRule24)
	 */
	public FrdParameterRule24 mergeFrdParameterRule24(FrdParameterRule24 frdParameterRule24);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule24SessionEJBRemote#mergeFrdParameterRule24List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule24> mergeFrdParameterRule24List(
			List<FrdParameterRule24> frdParameterRule24List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule24SessionEJBRemote#removeFrdParameterRule24(com
	 * .gdn.venice.persistence.FrdParameterRule24)
	 */
	public void removeFrdParameterRule24(FrdParameterRule24 frdParameterRule24);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule24SessionEJBRemote#removeFrdParameterRule24List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule24List(List<FrdParameterRule24> frdParameterRule24List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule24SessionEJBRemote#findByFrdParameterRule24Like
	 * (com.gdn.venice.persistence.FrdParameterRule24, int, int)
	 */
	public List<FrdParameterRule24> findByFrdParameterRule24Like(FrdParameterRule24 frdParameterRule24,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule24SessionEJBRemote#findByFrdParameterRule24LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule24, int, int)
	 */
	public FinderReturn findByFrdParameterRule24LikeFR(FrdParameterRule24 frdParameterRule24,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
