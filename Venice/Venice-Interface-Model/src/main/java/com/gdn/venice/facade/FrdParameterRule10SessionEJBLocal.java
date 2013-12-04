package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule10;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule10SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule10SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule10> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule10SessionEJBRemote#persistFrdParameterRule10(com
	 * .gdn.venice.persistence.FrdParameterRule10)
	 */
	public FrdParameterRule10 persistFrdParameterRule10(FrdParameterRule10 frdParameterRule10);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule10SessionEJBRemote#persistFrdParameterRule10List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule10> persistFrdParameterRule10List(
			List<FrdParameterRule10> frdParameterRule10List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule10SessionEJBRemote#mergeFrdParameterRule10(com.
	 * gdn.venice.persistence.FrdParameterRule10)
	 */
	public FrdParameterRule10 mergeFrdParameterRule10(FrdParameterRule10 frdParameterRule10);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule10SessionEJBRemote#mergeFrdParameterRule10List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule10> mergeFrdParameterRule10List(
			List<FrdParameterRule10> frdParameterRule10List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule10SessionEJBRemote#removeFrdParameterRule10(com
	 * .gdn.venice.persistence.FrdParameterRule10)
	 */
	public void removeFrdParameterRule10(FrdParameterRule10 frdParameterRule10);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule10SessionEJBRemote#removeFrdParameterRule10List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule10List(List<FrdParameterRule10> frdParameterRule10List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule10SessionEJBRemote#findByFrdParameterRule10Like
	 * (com.gdn.venice.persistence.FrdParameterRule10, int, int)
	 */
	public List<FrdParameterRule10> findByFrdParameterRule10Like(FrdParameterRule10 frdParameterRule10,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule10SessionEJBRemote#findByFrdParameterRule10LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule10, int, int)
	 */
	public FinderReturn findByFrdParameterRule10LikeFR(FrdParameterRule10 frdParameterRule10,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
