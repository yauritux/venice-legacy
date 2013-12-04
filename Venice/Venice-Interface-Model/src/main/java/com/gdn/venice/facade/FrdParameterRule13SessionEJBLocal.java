package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule13;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule13SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule13SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule13> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule13SessionEJBRemote#persistFrdParameterRule13(com
	 * .gdn.venice.persistence.FrdParameterRule13)
	 */
	public FrdParameterRule13 persistFrdParameterRule13(FrdParameterRule13 frdParameterRule13);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule13SessionEJBRemote#persistFrdParameterRule13List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule13> persistFrdParameterRule13List(
			List<FrdParameterRule13> frdParameterRule13List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule13SessionEJBRemote#mergeFrdParameterRule13(com.
	 * gdn.venice.persistence.FrdParameterRule13)
	 */
	public FrdParameterRule13 mergeFrdParameterRule13(FrdParameterRule13 frdParameterRule13);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule13SessionEJBRemote#mergeFrdParameterRule13List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule13> mergeFrdParameterRule13List(
			List<FrdParameterRule13> frdParameterRule13List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule13SessionEJBRemote#removeFrdParameterRule13(com
	 * .gdn.venice.persistence.FrdParameterRule13)
	 */
	public void removeFrdParameterRule13(FrdParameterRule13 frdParameterRule13);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule13SessionEJBRemote#removeFrdParameterRule13List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule13List(List<FrdParameterRule13> frdParameterRule13List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule13SessionEJBRemote#findByFrdParameterRule13Like
	 * (com.gdn.venice.persistence.FrdParameterRule13, int, int)
	 */
	public List<FrdParameterRule13> findByFrdParameterRule13Like(FrdParameterRule13 frdParameterRule13,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule13SessionEJBRemote#findByFrdParameterRule13LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule13, int, int)
	 */
	public FinderReturn findByFrdParameterRule13LikeFR(FrdParameterRule13 frdParameterRule13,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
