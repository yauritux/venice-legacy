package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule32;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule32SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule32SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule32> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule32SessionEJBRemote#persistFrdParameterRule32(com
	 * .gdn.venice.persistence.FrdParameterRule32)
	 */
	public FrdParameterRule32 persistFrdParameterRule32(FrdParameterRule32 frdParameterRule32);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule32SessionEJBRemote#persistFrdParameterRule32List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule32> persistFrdParameterRule32List(
			List<FrdParameterRule32> frdParameterRule32List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule32SessionEJBRemote#mergeFrdParameterRule32(com.
	 * gdn.venice.persistence.FrdParameterRule32)
	 */
	public FrdParameterRule32 mergeFrdParameterRule32(FrdParameterRule32 frdParameterRule32);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule32SessionEJBRemote#mergeFrdParameterRule32List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule32> mergeFrdParameterRule32List(
			List<FrdParameterRule32> frdParameterRule32List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule32SessionEJBRemote#removeFrdParameterRule32(com
	 * .gdn.venice.persistence.FrdParameterRule32)
	 */
	public void removeFrdParameterRule32(FrdParameterRule32 frdParameterRule32);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule32SessionEJBRemote#removeFrdParameterRule32List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule32List(List<FrdParameterRule32> frdParameterRule32List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule32SessionEJBRemote#findByFrdParameterRule32Like
	 * (com.gdn.venice.persistence.FrdParameterRule32, int, int)
	 */
	public List<FrdParameterRule32> findByFrdParameterRule32Like(FrdParameterRule32 frdParameterRule32,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule32SessionEJBRemote#findByFrdParameterRule32LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule32, int, int)
	 */
	public FinderReturn findByFrdParameterRule32LikeFR(FrdParameterRule32 frdParameterRule32,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
