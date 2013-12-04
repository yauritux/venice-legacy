package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdParameterRule38;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdParameterRule38SessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdParameterRule38
	 */
	public List<FrdParameterRule38> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdParameterRule38 persists a country
	 * 
	 * @param frdParameterRule38
	 * @return the persisted FrdParameterRule38
	 */
	public FrdParameterRule38 persistFrdParameterRule38(FrdParameterRule38 frdParameterRule38);

	/**
	 * persistFrdParameterRule38List - persists a list of FrdParameterRule38
	 * 
	 * @param frdParameterRule38List
	 * @return the list of persisted FrdParameterRule38
	 */
	public ArrayList<FrdParameterRule38> persistFrdParameterRule38List(
			List<FrdParameterRule38> frdParameterRule38List);

	/**
	 * mergeFrdParameterRule38 - merges a FrdParameterRule38
	 * 
	 * @param frdParameterRule38
	 * @return the merged FrdParameterRule38
	 */
	public FrdParameterRule38 mergeFrdParameterRule38(FrdParameterRule38 frdParameterRule38);

	/**
	 * mergeFrdParameterRule38List - merges a list of FrdParameterRule38
	 * 
	 * @param frdParameterRule38List
	 * @return the merged list of FrdParameterRule38
	 */
	public ArrayList<FrdParameterRule38> mergeFrdParameterRule38List(
			List<FrdParameterRule38> frdParameterRule38List);

	/**
	 * removeFrdParameterRule38 - removes a FrdParameterRule38
	 * 
	 * @param frdParameterRule38
	 */
	public void removeFrdParameterRule38(FrdParameterRule38 frdParameterRule38);

	/**
	 * removeFrdParameterRule38List - removes a list of FrdParameterRule38
	 * 
	 * @param frdParameterRule38List
	 */
	public void removeFrdParameterRule38List(List<FrdParameterRule38> frdParameterRule38List);

	/**
	 * findByFrdParameterRule38Like - finds a list of FrdParameterRule38 Like
	 * 
	 * @param frdParameterRule38
	 * @return the list of FrdParameterRule38 found
	 */
	public List<FrdParameterRule38> findByFrdParameterRule38Like(FrdParameterRule38 frdParameterRule38,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdParameterRule38>LikeFR - finds a list of FrdParameterRule38> Like with a finder return object
	 * 
	 * @param frdParameterRule38
	 * @return the list of FrdParameterRule38 found
	 */
	public FinderReturn findByFrdParameterRule38LikeFR(FrdParameterRule38 frdParameterRule38,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
