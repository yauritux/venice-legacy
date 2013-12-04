package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule34;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule34SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule34SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule34> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule34SessionEJBRemote#persistFrdParameterRule34(com
	 * .gdn.venice.persistence.FrdParameterRule34)
	 */
	public FrdParameterRule34 persistFrdParameterRule34(FrdParameterRule34 frdParameterRule34);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule34SessionEJBRemote#persistFrdParameterRule34List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule34> persistFrdParameterRule34List(
			List<FrdParameterRule34> frdParameterRule34List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule34SessionEJBRemote#mergeFrdParameterRule34(com.
	 * gdn.venice.persistence.FrdParameterRule34)
	 */
	public FrdParameterRule34 mergeFrdParameterRule34(FrdParameterRule34 frdParameterRule34);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule34SessionEJBRemote#mergeFrdParameterRule34List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule34> mergeFrdParameterRule34List(
			List<FrdParameterRule34> frdParameterRule34List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule34SessionEJBRemote#removeFrdParameterRule34(com
	 * .gdn.venice.persistence.FrdParameterRule34)
	 */
	public void removeFrdParameterRule34(FrdParameterRule34 frdParameterRule34);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule34SessionEJBRemote#removeFrdParameterRule34List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule34List(List<FrdParameterRule34> frdParameterRule34List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule34SessionEJBRemote#findByFrdParameterRule34Like
	 * (com.gdn.venice.persistence.FrdParameterRule34, int, int)
	 */
	public List<FrdParameterRule34> findByFrdParameterRule34Like(FrdParameterRule34 frdParameterRule34,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule34SessionEJBRemote#findByFrdParameterRule34LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule34, int, int)
	 */
	public FinderReturn findByFrdParameterRule34LikeFR(FrdParameterRule34 frdParameterRule34,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
