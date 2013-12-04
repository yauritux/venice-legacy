package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule39;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule39SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule39SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule39> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule39SessionEJBRemote#persistFrdParameterRule39(com
	 * .gdn.venice.persistence.FrdParameterRule39)
	 */
	public FrdParameterRule39 persistFrdParameterRule39(FrdParameterRule39 frdParameterRule39);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule39SessionEJBRemote#persistFrdParameterRule39List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule39> persistFrdParameterRule39List(
			List<FrdParameterRule39> frdParameterRule39List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule39SessionEJBRemote#mergeFrdParameterRule39(com.
	 * gdn.venice.persistence.FrdParameterRule39)
	 */
	public FrdParameterRule39 mergeFrdParameterRule39(FrdParameterRule39 frdParameterRule39);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule39SessionEJBRemote#mergeFrdParameterRule39List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule39> mergeFrdParameterRule39List(
			List<FrdParameterRule39> frdParameterRule39List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule39SessionEJBRemote#removeFrdParameterRule39(com
	 * .gdn.venice.persistence.FrdParameterRule39)
	 */
	public void removeFrdParameterRule39(FrdParameterRule39 frdParameterRule39);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule39SessionEJBRemote#removeFrdParameterRule39List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule39List(List<FrdParameterRule39> frdParameterRule39List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule39SessionEJBRemote#findByFrdParameterRule39Like
	 * (com.gdn.venice.persistence.FrdParameterRule39, int, int)
	 */
	public List<FrdParameterRule39> findByFrdParameterRule39Like(FrdParameterRule39 frdParameterRule39,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule39SessionEJBRemote#findByFrdParameterRule39LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule39, int, int)
	 */
	public FinderReturn findByFrdParameterRule39LikeFR(FrdParameterRule39 frdParameterRule39,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
