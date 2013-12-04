package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule5;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule5SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule5SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule5> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule5SessionEJBRemote#persistFrdParameterRule5(com
	 * .gdn.venice.persistence.FrdParameterRule5)
	 */
	public FrdParameterRule5 persistFrdParameterRule5(FrdParameterRule5 frdParameterRule5);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule5SessionEJBRemote#persistFrdParameterRule5List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule5> persistFrdParameterRule5List(
			List<FrdParameterRule5> frdParameterRule5List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule5SessionEJBRemote#mergeFrdParameterRule5(com.
	 * gdn.venice.persistence.FrdParameterRule5)
	 */
	public FrdParameterRule5 mergeFrdParameterRule5(FrdParameterRule5 frdParameterRule5);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule5SessionEJBRemote#mergeFrdParameterRule5List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule5> mergeFrdParameterRule5List(
			List<FrdParameterRule5> frdParameterRule5List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule5SessionEJBRemote#removeFrdParameterRule5(com
	 * .gdn.venice.persistence.FrdParameterRule5)
	 */
	public void removeFrdParameterRule5(FrdParameterRule5 frdParameterRule5);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule5SessionEJBRemote#removeFrdParameterRule5List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule5List(List<FrdParameterRule5> frdParameterRule5List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule5SessionEJBRemote#findByFrdParameterRule5Like
	 * (com.gdn.venice.persistence.FrdParameterRule5, int, int)
	 */
	public List<FrdParameterRule5> findByFrdParameterRule5Like(FrdParameterRule5 frdParameterRule5,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule5SessionEJBRemote#findByFrdParameterRule5LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule5, int, int)
	 */
	public FinderReturn findByFrdParameterRule5LikeFR(FrdParameterRule5 frdParameterRule5,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
