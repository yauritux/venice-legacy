package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule1;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule1SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule1SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule1> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule1SessionEJBRemote#persistFrdParameterRule1(com
	 * .gdn.venice.persistence.FrdParameterRule1)
	 */
	public FrdParameterRule1 persistFrdParameterRule1(FrdParameterRule1 frdParameterRule1);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule1SessionEJBRemote#persistFrdParameterRule1List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule1> persistFrdParameterRule1List(
			List<FrdParameterRule1> frdParameterRule1List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule1SessionEJBRemote#mergeFrdParameterRule1(com.
	 * gdn.venice.persistence.FrdParameterRule1)
	 */
	public FrdParameterRule1 mergeFrdParameterRule1(FrdParameterRule1 frdParameterRule1);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule1SessionEJBRemote#mergeFrdParameterRule1List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule1> mergeFrdParameterRule1List(
			List<FrdParameterRule1> frdParameterRule1List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule1SessionEJBRemote#removeFrdParameterRule1(com
	 * .gdn.venice.persistence.FrdParameterRule1)
	 */
	public void removeFrdParameterRule1(FrdParameterRule1 frdParameterRule1);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule1SessionEJBRemote#removeFrdParameterRule1List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule1List(List<FrdParameterRule1> frdParameterRule1List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule1SessionEJBRemote#findByFrdParameterRule1Like
	 * (com.gdn.venice.persistence.FrdParameterRule1, int, int)
	 */
	public List<FrdParameterRule1> findByFrdParameterRule1Like(FrdParameterRule1 frdParameterRule1,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule1SessionEJBRemote#findByFrdParameterRule1LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule1, int, int)
	 */
	public FinderReturn findByFrdParameterRule1LikeFR(FrdParameterRule1 frdParameterRule1,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
