package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule11;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule11SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule11
	 */
	public List<FrdParameterRule11> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule11 persists a country
	 * 
	 * @param frdParameterRule11
	 * @return the persisted FrdParameterRule11
	 */
	public FrdParameterRule11 persistFrdParameterRule11(FrdParameterRule11 frdParameterRule11);

	/**
	 * persistFrdParameterRule11List - persists a list of FrdParameterRule11
	 * 
	 * @param frdParameterRule11List
	 * @return the list of persisted FrdParameterRule11
	 */
	public ArrayList<FrdParameterRule11> persistFrdParameterRule11List(
			List<FrdParameterRule11> frdParameterRule11List);

	/**
	 * mergeFrdParameterRule11 - merges a FrdParameterRule11
	 * 
	 * @param frdParameterRule11
	 * @return the merged FrdParameterRule11
	 */
	public FrdParameterRule11 mergeFrdParameterRule11(FrdParameterRule11 frdParameterRule11);

	/**
	 * mergeFrdParameterRule11List - merges a list of FrdParameterRule11
	 * 
	 * @param frdParameterRule11List
	 * @return the merged list of FrdParameterRule11
	 */
	public ArrayList<FrdParameterRule11> mergeFrdParameterRule11List(
			List<FrdParameterRule11> frdParameterRule11List);

	/**
	 * removeFrdParameterRule11 - removes a FrdParameterRule11
	 * 
	 * @param frdParameterRule11
	 */
	public void removeFrdParameterRule11(FrdParameterRule11 frdParameterRule11);

	/**
	 * removeFrdParameterRule11List - removes a list of FrdParameterRule11
	 * 
	 * @param frdParameterRule11List
	 */
	public void removeFrdParameterRule11List(List<FrdParameterRule11> frdParameterRule11List);

	/**
	 * findByFrdParameterRule11Like - finds a list of FrdParameterRule11 Like
	 * 
	 * @param frdParameterRule11
	 * @return the list of FrdParameterRule11 found
	 */
	public List<FrdParameterRule11> findByFrdParameterRule11Like(FrdParameterRule11 frdParameterRule11,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule11>LikeFR - finds a list of FrdParameterRule11> Like with a finder return object
	 * 
	 * @param frdParameterRule11
	 * @return the list of FrdParameterRule11 found
	 */
	public FinderReturn findByFrdParameterRule11LikeFR(FrdParameterRule11 frdParameterRule11,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
