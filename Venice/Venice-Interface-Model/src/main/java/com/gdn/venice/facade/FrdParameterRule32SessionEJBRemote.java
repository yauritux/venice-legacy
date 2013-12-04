package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule32;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule32SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule32
	 */
	public List<FrdParameterRule32> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule32 persists a country
	 * 
	 * @param frdParameterRule32
	 * @return the persisted FrdParameterRule32
	 */
	public FrdParameterRule32 persistFrdParameterRule32(FrdParameterRule32 frdParameterRule32);

	/**
	 * persistFrdParameterRule32List - persists a list of FrdParameterRule32
	 * 
	 * @param frdParameterRule32List
	 * @return the list of persisted FrdParameterRule32
	 */
	public ArrayList<FrdParameterRule32> persistFrdParameterRule32List(
			List<FrdParameterRule32> frdParameterRule32List);

	/**
	 * mergeFrdParameterRule32 - merges a FrdParameterRule32
	 * 
	 * @param frdParameterRule32
	 * @return the merged FrdParameterRule32
	 */
	public FrdParameterRule32 mergeFrdParameterRule32(FrdParameterRule32 frdParameterRule32);

	/**
	 * mergeFrdParameterRule32List - merges a list of FrdParameterRule32
	 * 
	 * @param frdParameterRule32List
	 * @return the merged list of FrdParameterRule32
	 */
	public ArrayList<FrdParameterRule32> mergeFrdParameterRule32List(
			List<FrdParameterRule32> frdParameterRule32List);

	/**
	 * removeFrdParameterRule32 - removes a FrdParameterRule32
	 * 
	 * @param frdParameterRule32
	 */
	public void removeFrdParameterRule32(FrdParameterRule32 frdParameterRule32);

	/**
	 * removeFrdParameterRule32List - removes a list of FrdParameterRule32
	 * 
	 * @param frdParameterRule32List
	 */
	public void removeFrdParameterRule32List(List<FrdParameterRule32> frdParameterRule32List);

	/**
	 * findByFrdParameterRule32Like - finds a list of FrdParameterRule32 Like
	 * 
	 * @param frdParameterRule32
	 * @return the list of FrdParameterRule32 found
	 */
	public List<FrdParameterRule32> findByFrdParameterRule32Like(FrdParameterRule32 frdParameterRule32,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule32>LikeFR - finds a list of FrdParameterRule32> Like with a finder return object
	 * 
	 * @param frdParameterRule32
	 * @return the list of FrdParameterRule32 found
	 */
	public FinderReturn findByFrdParameterRule32LikeFR(FrdParameterRule32 frdParameterRule32,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
