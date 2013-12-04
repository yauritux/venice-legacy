package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule11;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule11SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule11SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule11> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule11SessionEJBRemote#persistFrdParameterRule11(com
	 * .gdn.venice.persistence.FrdParameterRule11)
	 */
	public FrdParameterRule11 persistFrdParameterRule11(FrdParameterRule11 frdParameterRule11);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule11SessionEJBRemote#persistFrdParameterRule11List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule11> persistFrdParameterRule11List(
			List<FrdParameterRule11> frdParameterRule11List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule11SessionEJBRemote#mergeFrdParameterRule11(com.
	 * gdn.venice.persistence.FrdParameterRule11)
	 */
	public FrdParameterRule11 mergeFrdParameterRule11(FrdParameterRule11 frdParameterRule11);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule11SessionEJBRemote#mergeFrdParameterRule11List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule11> mergeFrdParameterRule11List(
			List<FrdParameterRule11> frdParameterRule11List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule11SessionEJBRemote#removeFrdParameterRule11(com
	 * .gdn.venice.persistence.FrdParameterRule11)
	 */
	public void removeFrdParameterRule11(FrdParameterRule11 frdParameterRule11);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule11SessionEJBRemote#removeFrdParameterRule11List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule11List(List<FrdParameterRule11> frdParameterRule11List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule11SessionEJBRemote#findByFrdParameterRule11Like
	 * (com.gdn.venice.persistence.FrdParameterRule11, int, int)
	 */
	public List<FrdParameterRule11> findByFrdParameterRule11Like(FrdParameterRule11 frdParameterRule11,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule11SessionEJBRemote#findByFrdParameterRule11LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule11, int, int)
	 */
	public FinderReturn findByFrdParameterRule11LikeFR(FrdParameterRule11 frdParameterRule11,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
