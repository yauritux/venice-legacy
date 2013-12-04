package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule22;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule22SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule22SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule22> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule22SessionEJBRemote#persistFrdParameterRule22(com
	 * .gdn.venice.persistence.FrdParameterRule22)
	 */
	public FrdParameterRule22 persistFrdParameterRule22(FrdParameterRule22 frdParameterRule22);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule22SessionEJBRemote#persistFrdParameterRule22List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule22> persistFrdParameterRule22List(
			List<FrdParameterRule22> frdParameterRule22List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule22SessionEJBRemote#mergeFrdParameterRule22(com.
	 * gdn.venice.persistence.FrdParameterRule22)
	 */
	public FrdParameterRule22 mergeFrdParameterRule22(FrdParameterRule22 frdParameterRule22);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule22SessionEJBRemote#mergeFrdParameterRule22List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule22> mergeFrdParameterRule22List(
			List<FrdParameterRule22> frdParameterRule22List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule22SessionEJBRemote#removeFrdParameterRule22(com
	 * .gdn.venice.persistence.FrdParameterRule22)
	 */
	public void removeFrdParameterRule22(FrdParameterRule22 frdParameterRule22);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule22SessionEJBRemote#removeFrdParameterRule22List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule22List(List<FrdParameterRule22> frdParameterRule22List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule22SessionEJBRemote#findByFrdParameterRule22Like
	 * (com.gdn.venice.persistence.FrdParameterRule22, int, int)
	 */
	public List<FrdParameterRule22> findByFrdParameterRule22Like(FrdParameterRule22 frdParameterRule22,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule22SessionEJBRemote#findByFrdParameterRule22LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule22, int, int)
	 */
	public FinderReturn findByFrdParameterRule22LikeFR(FrdParameterRule22 frdParameterRule22,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
