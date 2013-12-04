package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule26272829;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule26272829SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule26272829SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule26272829> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule26272829SessionEJBRemote#persistFrdParameterRule26272829(com
	 * .gdn.venice.persistence.FrdParameterRule26272829)
	 */
	public FrdParameterRule26272829 persistFrdParameterRule26272829(FrdParameterRule26272829 frdParameterRule26272829);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule26272829SessionEJBRemote#persistFrdParameterRule26272829List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule26272829> persistFrdParameterRule26272829List(
			List<FrdParameterRule26272829> frdParameterRule26272829List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule26272829SessionEJBRemote#mergeFrdParameterRule26272829(com.
	 * gdn.venice.persistence.FrdParameterRule26272829)
	 */
	public FrdParameterRule26272829 mergeFrdParameterRule26272829(FrdParameterRule26272829 frdParameterRule26272829);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule26272829SessionEJBRemote#mergeFrdParameterRule26272829List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule26272829> mergeFrdParameterRule26272829List(
			List<FrdParameterRule26272829> frdParameterRule26272829List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule26272829SessionEJBRemote#removeFrdParameterRule26272829(com
	 * .gdn.venice.persistence.FrdParameterRule26272829)
	 */
	public void removeFrdParameterRule26272829(FrdParameterRule26272829 frdParameterRule26272829);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule26272829SessionEJBRemote#removeFrdParameterRule26272829List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule26272829List(List<FrdParameterRule26272829> frdParameterRule26272829List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule26272829SessionEJBRemote#findByFrdParameterRule26272829Like
	 * (com.gdn.venice.persistence.FrdParameterRule26272829, int, int)
	 */
	public List<FrdParameterRule26272829> findByFrdParameterRule26272829Like(FrdParameterRule26272829 frdParameterRule26272829,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule26272829SessionEJBRemote#findByFrdParameterRule26272829LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule26272829, int, int)
	 */
	public FinderReturn findByFrdParameterRule26272829LikeFR(FrdParameterRule26272829 frdParameterRule26272829,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
