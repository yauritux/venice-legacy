package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule12;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule12SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule12SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule12> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule12SessionEJBRemote#persistFrdParameterRule12(com
	 * .gdn.venice.persistence.FrdParameterRule12)
	 */
	public FrdParameterRule12 persistFrdParameterRule12(FrdParameterRule12 frdParameterRule12);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule12SessionEJBRemote#persistFrdParameterRule12List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule12> persistFrdParameterRule12List(
			List<FrdParameterRule12> frdParameterRule12List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule12SessionEJBRemote#mergeFrdParameterRule12(com.
	 * gdn.venice.persistence.FrdParameterRule12)
	 */
	public FrdParameterRule12 mergeFrdParameterRule12(FrdParameterRule12 frdParameterRule12);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule12SessionEJBRemote#mergeFrdParameterRule12List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule12> mergeFrdParameterRule12List(
			List<FrdParameterRule12> frdParameterRule12List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule12SessionEJBRemote#removeFrdParameterRule12(com
	 * .gdn.venice.persistence.FrdParameterRule12)
	 */
	public void removeFrdParameterRule12(FrdParameterRule12 frdParameterRule12);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule12SessionEJBRemote#removeFrdParameterRule12List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule12List(List<FrdParameterRule12> frdParameterRule12List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule12SessionEJBRemote#findByFrdParameterRule12Like
	 * (com.gdn.venice.persistence.FrdParameterRule12, int, int)
	 */
	public List<FrdParameterRule12> findByFrdParameterRule12Like(FrdParameterRule12 frdParameterRule12,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule12SessionEJBRemote#findByFrdParameterRule12LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule12, int, int)
	 */
	public FinderReturn findByFrdParameterRule12LikeFR(FrdParameterRule12 frdParameterRule12,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
