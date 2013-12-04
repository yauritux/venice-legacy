package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule9;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule9SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule9SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule9> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule9SessionEJBRemote#persistFrdParameterRule9(com
	 * .gdn.venice.persistence.FrdParameterRule9)
	 */
	public FrdParameterRule9 persistFrdParameterRule9(FrdParameterRule9 frdParameterRule9);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule9SessionEJBRemote#persistFrdParameterRule9List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule9> persistFrdParameterRule9List(
			List<FrdParameterRule9> frdParameterRule9List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule9SessionEJBRemote#mergeFrdParameterRule9(com.
	 * gdn.venice.persistence.FrdParameterRule9)
	 */
	public FrdParameterRule9 mergeFrdParameterRule9(FrdParameterRule9 frdParameterRule9);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule9SessionEJBRemote#mergeFrdParameterRule9List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule9> mergeFrdParameterRule9List(
			List<FrdParameterRule9> frdParameterRule9List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule9SessionEJBRemote#removeFrdParameterRule9(com
	 * .gdn.venice.persistence.FrdParameterRule9)
	 */
	public void removeFrdParameterRule9(FrdParameterRule9 frdParameterRule9);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule9SessionEJBRemote#removeFrdParameterRule9List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule9List(List<FrdParameterRule9> frdParameterRule9List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule9SessionEJBRemote#findByFrdParameterRule9Like
	 * (com.gdn.venice.persistence.FrdParameterRule9, int, int)
	 */
	public List<FrdParameterRule9> findByFrdParameterRule9Like(FrdParameterRule9 frdParameterRule9,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule9SessionEJBRemote#findByFrdParameterRule9LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule9, int, int)
	 */
	public FinderReturn findByFrdParameterRule9LikeFR(FrdParameterRule9 frdParameterRule9,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
