package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule14;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule14SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule14SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule14> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule14SessionEJBRemote#persistFrdParameterRule14(com
	 * .gdn.venice.persistence.FrdParameterRule14)
	 */
	public FrdParameterRule14 persistFrdParameterRule14(FrdParameterRule14 frdParameterRule14);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule14SessionEJBRemote#persistFrdParameterRule14List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule14> persistFrdParameterRule14List(
			List<FrdParameterRule14> frdParameterRule14List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule14SessionEJBRemote#mergeFrdParameterRule14(com.
	 * gdn.venice.persistence.FrdParameterRule14)
	 */
	public FrdParameterRule14 mergeFrdParameterRule14(FrdParameterRule14 frdParameterRule14);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule14SessionEJBRemote#mergeFrdParameterRule14List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule14> mergeFrdParameterRule14List(
			List<FrdParameterRule14> frdParameterRule14List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule14SessionEJBRemote#removeFrdParameterRule14(com
	 * .gdn.venice.persistence.FrdParameterRule14)
	 */
	public void removeFrdParameterRule14(FrdParameterRule14 frdParameterRule14);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule14SessionEJBRemote#removeFrdParameterRule14List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule14List(List<FrdParameterRule14> frdParameterRule14List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule14SessionEJBRemote#findByFrdParameterRule14Like
	 * (com.gdn.venice.persistence.FrdParameterRule14, int, int)
	 */
	public List<FrdParameterRule14> findByFrdParameterRule14Like(FrdParameterRule14 frdParameterRule14,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule14SessionEJBRemote#findByFrdParameterRule14LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule14, int, int)
	 */
	public FinderReturn findByFrdParameterRule14LikeFR(FrdParameterRule14 frdParameterRule14,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
