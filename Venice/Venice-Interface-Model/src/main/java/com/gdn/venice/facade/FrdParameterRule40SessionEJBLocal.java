package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule40;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule40SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule40SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule40> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule40SessionEJBRemote#persistFrdParameterRule40(com
	 * .gdn.venice.persistence.FrdParameterRule40)
	 */
	public FrdParameterRule40 persistFrdParameterRule40(FrdParameterRule40 frdParameterRule40);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule40SessionEJBRemote#persistFrdParameterRule40List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule40> persistFrdParameterRule40List(
			List<FrdParameterRule40> frdParameterRule40List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule40SessionEJBRemote#mergeFrdParameterRule40(com.
	 * gdn.venice.persistence.FrdParameterRule40)
	 */
	public FrdParameterRule40 mergeFrdParameterRule40(FrdParameterRule40 frdParameterRule40);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule40SessionEJBRemote#mergeFrdParameterRule40List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule40> mergeFrdParameterRule40List(
			List<FrdParameterRule40> frdParameterRule40List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule40SessionEJBRemote#removeFrdParameterRule40(com
	 * .gdn.venice.persistence.FrdParameterRule40)
	 */
	public void removeFrdParameterRule40(FrdParameterRule40 frdParameterRule40);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule40SessionEJBRemote#removeFrdParameterRule40List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule40List(List<FrdParameterRule40> frdParameterRule40List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule40SessionEJBRemote#findByFrdParameterRule40Like
	 * (com.gdn.venice.persistence.FrdParameterRule40, int, int)
	 */
	public List<FrdParameterRule40> findByFrdParameterRule40Like(FrdParameterRule40 frdParameterRule40,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule40SessionEJBRemote#findByFrdParameterRule40LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule40, int, int)
	 */
	public FinderReturn findByFrdParameterRule40LikeFR(FrdParameterRule40 frdParameterRule40,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
