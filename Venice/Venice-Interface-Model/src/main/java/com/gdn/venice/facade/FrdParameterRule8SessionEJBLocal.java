package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule8;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule8SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule8SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule8> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule8SessionEJBRemote#persistFrdParameterRule8(com
	 * .gdn.venice.persistence.FrdParameterRule8)
	 */
	public FrdParameterRule8 persistFrdParameterRule8(FrdParameterRule8 frdParameterRule8);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule8SessionEJBRemote#persistFrdParameterRule8List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule8> persistFrdParameterRule8List(
			List<FrdParameterRule8> frdParameterRule8List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule8SessionEJBRemote#mergeFrdParameterRule8(com.
	 * gdn.venice.persistence.FrdParameterRule8)
	 */
	public FrdParameterRule8 mergeFrdParameterRule8(FrdParameterRule8 frdParameterRule8);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule8SessionEJBRemote#mergeFrdParameterRule8List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule8> mergeFrdParameterRule8List(
			List<FrdParameterRule8> frdParameterRule8List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule8SessionEJBRemote#removeFrdParameterRule8(com
	 * .gdn.venice.persistence.FrdParameterRule8)
	 */
	public void removeFrdParameterRule8(FrdParameterRule8 frdParameterRule8);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule8SessionEJBRemote#removeFrdParameterRule8List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule8List(List<FrdParameterRule8> frdParameterRule8List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule8SessionEJBRemote#findByFrdParameterRule8Like
	 * (com.gdn.venice.persistence.FrdParameterRule8, int, int)
	 */
	public List<FrdParameterRule8> findByFrdParameterRule8Like(FrdParameterRule8 frdParameterRule8,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule8SessionEJBRemote#findByFrdParameterRule8LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule8, int, int)
	 */
	public FinderReturn findByFrdParameterRule8LikeFR(FrdParameterRule8 frdParameterRule8,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
