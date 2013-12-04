package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule8;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule8SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule8
	 */
	public List<FrdParameterRule8> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule8 persists a country
	 * 
	 * @param frdParameterRule8
	 * @return the persisted FrdParameterRule8
	 */
	public FrdParameterRule8 persistFrdParameterRule8(FrdParameterRule8 frdParameterRule8);

	/**
	 * persistFrdParameterRule8List - persists a list of FrdParameterRule8
	 * 
	 * @param frdParameterRule8List
	 * @return the list of persisted FrdParameterRule8
	 */
	public ArrayList<FrdParameterRule8> persistFrdParameterRule8List(
			List<FrdParameterRule8> frdParameterRule8List);

	/**
	 * mergeFrdParameterRule8 - merges a FrdParameterRule8
	 * 
	 * @param frdParameterRule8
	 * @return the merged FrdParameterRule8
	 */
	public FrdParameterRule8 mergeFrdParameterRule8(FrdParameterRule8 frdParameterRule8);

	/**
	 * mergeFrdParameterRule8List - merges a list of FrdParameterRule8
	 * 
	 * @param frdParameterRule8List
	 * @return the merged list of FrdParameterRule8
	 */
	public ArrayList<FrdParameterRule8> mergeFrdParameterRule8List(
			List<FrdParameterRule8> frdParameterRule8List);

	/**
	 * removeFrdParameterRule8 - removes a FrdParameterRule8
	 * 
	 * @param frdParameterRule8
	 */
	public void removeFrdParameterRule8(FrdParameterRule8 frdParameterRule8);

	/**
	 * removeFrdParameterRule8List - removes a list of FrdParameterRule8
	 * 
	 * @param frdParameterRule8List
	 */
	public void removeFrdParameterRule8List(List<FrdParameterRule8> frdParameterRule8List);

	/**
	 * findByFrdParameterRule8Like - finds a list of FrdParameterRule8 Like
	 * 
	 * @param frdParameterRule8
	 * @return the list of FrdParameterRule8 found
	 */
	public List<FrdParameterRule8> findByFrdParameterRule8Like(FrdParameterRule8 frdParameterRule8,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule8>LikeFR - finds a list of FrdParameterRule8> Like with a finder return object
	 * 
	 * @param frdParameterRule8
	 * @return the list of FrdParameterRule8 found
	 */
	public FinderReturn findByFrdParameterRule8LikeFR(FrdParameterRule8 frdParameterRule8,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
