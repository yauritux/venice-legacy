package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule16;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule16SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule16
	 */
	public List<FrdParameterRule16> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule16 persists a country
	 * 
	 * @param frdParameterRule16
	 * @return the persisted FrdParameterRule16
	 */
	public FrdParameterRule16 persistFrdParameterRule16(FrdParameterRule16 frdParameterRule16);

	/**
	 * persistFrdParameterRule16List - persists a list of FrdParameterRule16
	 * 
	 * @param frdParameterRule16List
	 * @return the list of persisted FrdParameterRule16
	 */
	public ArrayList<FrdParameterRule16> persistFrdParameterRule16List(
			List<FrdParameterRule16> frdParameterRule16List);

	/**
	 * mergeFrdParameterRule16 - merges a FrdParameterRule16
	 * 
	 * @param frdParameterRule16
	 * @return the merged FrdParameterRule16
	 */
	public FrdParameterRule16 mergeFrdParameterRule16(FrdParameterRule16 frdParameterRule16);

	/**
	 * mergeFrdParameterRule16List - merges a list of FrdParameterRule16
	 * 
	 * @param frdParameterRule16List
	 * @return the merged list of FrdParameterRule16
	 */
	public ArrayList<FrdParameterRule16> mergeFrdParameterRule16List(
			List<FrdParameterRule16> frdParameterRule16List);

	/**
	 * removeFrdParameterRule16 - removes a FrdParameterRule16
	 * 
	 * @param frdParameterRule16
	 */
	public void removeFrdParameterRule16(FrdParameterRule16 frdParameterRule16);

	/**
	 * removeFrdParameterRule16List - removes a list of FrdParameterRule16
	 * 
	 * @param frdParameterRule16List
	 */
	public void removeFrdParameterRule16List(List<FrdParameterRule16> frdParameterRule16List);

	/**
	 * findByFrdParameterRule16Like - finds a list of FrdParameterRule16 Like
	 * 
	 * @param frdParameterRule16
	 * @return the list of FrdParameterRule16 found
	 */
	public List<FrdParameterRule16> findByFrdParameterRule16Like(FrdParameterRule16 frdParameterRule16,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule16>LikeFR - finds a list of FrdParameterRule16> Like with a finder return object
	 * 
	 * @param frdParameterRule16
	 * @return the list of FrdParameterRule16 found
	 */
	public FinderReturn findByFrdParameterRule16LikeFR(FrdParameterRule16 frdParameterRule16,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
