package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FrdEmailType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FrdEmailTypeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FrdEmailType
	 */
	public List<FrdEmailType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFrdEmailType persists a country
	 * 
	 * @param frdEmailType
	 * @return the persisted FrdEmailType
	 */
	public FrdEmailType persistFrdEmailType(FrdEmailType frdEmailType);

	/**
	 * persistFrdEmailTypeList - persists a list of FrdEmailType
	 * 
	 * @param frdEmailTypeList
	 * @return the list of persisted FrdEmailType
	 */
	public ArrayList<FrdEmailType> persistFrdEmailTypeList(
			List<FrdEmailType> frdEmailTypeList);

	/**
	 * mergeFrdEmailType - merges a FrdEmailType
	 * 
	 * @param frdEmailType
	 * @return the merged FrdEmailType
	 */
	public FrdEmailType mergeFrdEmailType(FrdEmailType frdEmailType);

	/**
	 * mergeFrdEmailTypeList - merges a list of FrdEmailType
	 * 
	 * @param frdEmailTypeList
	 * @return the merged list of FrdEmailType
	 */
	public ArrayList<FrdEmailType> mergeFrdEmailTypeList(
			List<FrdEmailType> frdEmailTypeList);

	/**
	 * removeFrdEmailType - removes a FrdEmailType
	 * 
	 * @param frdEmailType
	 */
	public void removeFrdEmailType(FrdEmailType frdEmailType);

	/**
	 * removeFrdEmailTypeList - removes a list of FrdEmailType
	 * 
	 * @param frdEmailTypeList
	 */
	public void removeFrdEmailTypeList(List<FrdEmailType> frdEmailTypeList);

	/**
	 * findByFrdEmailTypeLike - finds a list of FrdEmailType Like
	 * 
	 * @param frdEmailType
	 * @return the list of FrdEmailType found
	 */
	public List<FrdEmailType> findByFrdEmailTypeLike(FrdEmailType frdEmailType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFrdEmailType>LikeFR - finds a list of FrdEmailType> Like with a finder return object
	 * 
	 * @param frdEmailType
	 * @return the list of FrdEmailType found
	 */
	public FinderReturn findByFrdEmailTypeLikeFR(FrdEmailType frdEmailType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
