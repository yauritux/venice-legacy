package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule30;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule30SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule30
	 */
	public List<FrdParameterRule30> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule30 persists a country
	 * 
	 * @param frdParameterRule30
	 * @return the persisted FrdParameterRule30
	 */
	public FrdParameterRule30 persistFrdParameterRule30(FrdParameterRule30 frdParameterRule30);

	/**
	 * persistFrdParameterRule30List - persists a list of FrdParameterRule30
	 * 
	 * @param frdParameterRule30List
	 * @return the list of persisted FrdParameterRule30
	 */
	public ArrayList<FrdParameterRule30> persistFrdParameterRule30List(
			List<FrdParameterRule30> frdParameterRule30List);

	/**
	 * mergeFrdParameterRule30 - merges a FrdParameterRule30
	 * 
	 * @param frdParameterRule30
	 * @return the merged FrdParameterRule30
	 */
	public FrdParameterRule30 mergeFrdParameterRule30(FrdParameterRule30 frdParameterRule30);

	/**
	 * mergeFrdParameterRule30List - merges a list of FrdParameterRule30
	 * 
	 * @param frdParameterRule30List
	 * @return the merged list of FrdParameterRule30
	 */
	public ArrayList<FrdParameterRule30> mergeFrdParameterRule30List(
			List<FrdParameterRule30> frdParameterRule30List);

	/**
	 * removeFrdParameterRule30 - removes a FrdParameterRule30
	 * 
	 * @param frdParameterRule30
	 */
	public void removeFrdParameterRule30(FrdParameterRule30 frdParameterRule30);

	/**
	 * removeFrdParameterRule30List - removes a list of FrdParameterRule30
	 * 
	 * @param frdParameterRule30List
	 */
	public void removeFrdParameterRule30List(List<FrdParameterRule30> frdParameterRule30List);

	/**
	 * findByFrdParameterRule30Like - finds a list of FrdParameterRule30 Like
	 * 
	 * @param frdParameterRule30
	 * @return the list of FrdParameterRule30 found
	 */
	public List<FrdParameterRule30> findByFrdParameterRule30Like(FrdParameterRule30 frdParameterRule30,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule30>LikeFR - finds a list of FrdParameterRule30> Like with a finder return object
	 * 
	 * @param frdParameterRule30
	 * @return the list of FrdParameterRule30 found
	 */
	public FinderReturn findByFrdParameterRule30LikeFR(FrdParameterRule30 frdParameterRule30,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
