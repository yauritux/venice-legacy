package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule21;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule21SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule21
	 */
	public List<FrdParameterRule21> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule21 persists a country
	 * 
	 * @param frdParameterRule21
	 * @return the persisted FrdParameterRule21
	 */
	public FrdParameterRule21 persistFrdParameterRule21(FrdParameterRule21 frdParameterRule21);

	/**
	 * persistFrdParameterRule21List - persists a list of FrdParameterRule21
	 * 
	 * @param frdParameterRule21List
	 * @return the list of persisted FrdParameterRule21
	 */
	public ArrayList<FrdParameterRule21> persistFrdParameterRule21List(
			List<FrdParameterRule21> frdParameterRule21List);

	/**
	 * mergeFrdParameterRule21 - merges a FrdParameterRule21
	 * 
	 * @param frdParameterRule21
	 * @return the merged FrdParameterRule21
	 */
	public FrdParameterRule21 mergeFrdParameterRule21(FrdParameterRule21 frdParameterRule21);

	/**
	 * mergeFrdParameterRule21List - merges a list of FrdParameterRule21
	 * 
	 * @param frdParameterRule21List
	 * @return the merged list of FrdParameterRule21
	 */
	public ArrayList<FrdParameterRule21> mergeFrdParameterRule21List(
			List<FrdParameterRule21> frdParameterRule21List);

	/**
	 * removeFrdParameterRule21 - removes a FrdParameterRule21
	 * 
	 * @param frdParameterRule21
	 */
	public void removeFrdParameterRule21(FrdParameterRule21 frdParameterRule21);

	/**
	 * removeFrdParameterRule21List - removes a list of FrdParameterRule21
	 * 
	 * @param frdParameterRule21List
	 */
	public void removeFrdParameterRule21List(List<FrdParameterRule21> frdParameterRule21List);

	/**
	 * findByFrdParameterRule21Like - finds a list of FrdParameterRule21 Like
	 * 
	 * @param frdParameterRule21
	 * @return the list of FrdParameterRule21 found
	 */
	public List<FrdParameterRule21> findByFrdParameterRule21Like(FrdParameterRule21 frdParameterRule21,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule21>LikeFR - finds a list of FrdParameterRule21> Like with a finder return object
	 * 
	 * @param frdParameterRule21
	 * @return the list of FrdParameterRule21 found
	 */
	public FinderReturn findByFrdParameterRule21LikeFR(FrdParameterRule21 frdParameterRule21,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
