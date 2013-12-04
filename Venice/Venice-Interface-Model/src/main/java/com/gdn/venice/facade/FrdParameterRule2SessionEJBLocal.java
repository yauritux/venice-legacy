package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule2;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule2SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule2SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule2> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule2SessionEJBRemote#persistFrdParameterRule2(com
	 * .gdn.venice.persistence.FrdParameterRule2)
	 */
	public FrdParameterRule2 persistFrdParameterRule2(FrdParameterRule2 frdParameterRule2);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule2SessionEJBRemote#persistFrdParameterRule2List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule2> persistFrdParameterRule2List(
			List<FrdParameterRule2> frdParameterRule2List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule2SessionEJBRemote#mergeFrdParameterRule2(com.
	 * gdn.venice.persistence.FrdParameterRule2)
	 */
	public FrdParameterRule2 mergeFrdParameterRule2(FrdParameterRule2 frdParameterRule2);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule2SessionEJBRemote#mergeFrdParameterRule2List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule2> mergeFrdParameterRule2List(
			List<FrdParameterRule2> frdParameterRule2List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule2SessionEJBRemote#removeFrdParameterRule2(com
	 * .gdn.venice.persistence.FrdParameterRule2)
	 */
	public void removeFrdParameterRule2(FrdParameterRule2 frdParameterRule2);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule2SessionEJBRemote#removeFrdParameterRule2List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule2List(List<FrdParameterRule2> frdParameterRule2List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule2SessionEJBRemote#findByFrdParameterRule2Like
	 * (com.gdn.venice.persistence.FrdParameterRule2, int, int)
	 */
	public List<FrdParameterRule2> findByFrdParameterRule2Like(FrdParameterRule2 frdParameterRule2,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule2SessionEJBRemote#findByFrdParameterRule2LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule2, int, int)
	 */
	public FinderReturn findByFrdParameterRule2LikeFR(FrdParameterRule2 frdParameterRule2,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
