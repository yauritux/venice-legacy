package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule3;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule3SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule3SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule3> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule3SessionEJBRemote#persistFrdParameterRule3(com
	 * .gdn.venice.persistence.FrdParameterRule3)
	 */
	public FrdParameterRule3 persistFrdParameterRule3(FrdParameterRule3 frdParameterRule3);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule3SessionEJBRemote#persistFrdParameterRule3List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule3> persistFrdParameterRule3List(
			List<FrdParameterRule3> frdParameterRule3List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule3SessionEJBRemote#mergeFrdParameterRule3(com.
	 * gdn.venice.persistence.FrdParameterRule3)
	 */
	public FrdParameterRule3 mergeFrdParameterRule3(FrdParameterRule3 frdParameterRule3);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule3SessionEJBRemote#mergeFrdParameterRule3List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule3> mergeFrdParameterRule3List(
			List<FrdParameterRule3> frdParameterRule3List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule3SessionEJBRemote#removeFrdParameterRule3(com
	 * .gdn.venice.persistence.FrdParameterRule3)
	 */
	public void removeFrdParameterRule3(FrdParameterRule3 frdParameterRule3);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule3SessionEJBRemote#removeFrdParameterRule3List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule3List(List<FrdParameterRule3> frdParameterRule3List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule3SessionEJBRemote#findByFrdParameterRule3Like
	 * (com.gdn.venice.persistence.FrdParameterRule3, int, int)
	 */
	public List<FrdParameterRule3> findByFrdParameterRule3Like(FrdParameterRule3 frdParameterRule3,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule3SessionEJBRemote#findByFrdParameterRule3LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule3, int, int)
	 */
	public FinderReturn findByFrdParameterRule3LikeFR(FrdParameterRule3 frdParameterRule3,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
