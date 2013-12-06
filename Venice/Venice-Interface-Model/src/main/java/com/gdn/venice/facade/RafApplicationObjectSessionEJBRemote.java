package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafApplicationObject;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafApplicationObjectSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafApplicationObject
	 */
	public List<RafApplicationObject> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafApplicationObject persists a country
	 * 
	 * @param rafApplicationObject
	 * @return the persisted RafApplicationObject
	 */
	public RafApplicationObject persistRafApplicationObject(RafApplicationObject rafApplicationObject);

	/**
	 * persistRafApplicationObjectList - persists a list of RafApplicationObject
	 * 
	 * @param rafApplicationObjectList
	 * @return the list of persisted RafApplicationObject
	 */
	public ArrayList<RafApplicationObject> persistRafApplicationObjectList(
			List<RafApplicationObject> rafApplicationObjectList);

	/**
	 * mergeRafApplicationObject - merges a RafApplicationObject
	 * 
	 * @param rafApplicationObject
	 * @return the merged RafApplicationObject
	 */
	public RafApplicationObject mergeRafApplicationObject(RafApplicationObject rafApplicationObject);

	/**
	 * mergeRafApplicationObjectList - merges a list of RafApplicationObject
	 * 
	 * @param rafApplicationObjectList
	 * @return the merged list of RafApplicationObject
	 */
	public ArrayList<RafApplicationObject> mergeRafApplicationObjectList(
			List<RafApplicationObject> rafApplicationObjectList);

	/**
	 * removeRafApplicationObject - removes a RafApplicationObject
	 * 
	 * @param rafApplicationObject
	 */
	public void removeRafApplicationObject(RafApplicationObject rafApplicationObject);

	/**
	 * removeRafApplicationObjectList - removes a list of RafApplicationObject
	 * 
	 * @param rafApplicationObjectList
	 */
	public void removeRafApplicationObjectList(List<RafApplicationObject> rafApplicationObjectList);

	/**
	 * findByRafApplicationObjectLike - finds a list of RafApplicationObject Like
	 * 
	 * @param rafApplicationObject
	 * @return the list of RafApplicationObject found
	 */
	public List<RafApplicationObject> findByRafApplicationObjectLike(RafApplicationObject rafApplicationObject,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafApplicationObject>LikeFR - finds a list of RafApplicationObject> Like with a finder return object
	 * 
	 * @param rafApplicationObject
	 * @return the list of RafApplicationObject found
	 */
	public FinderReturn findByRafApplicationObjectLikeFR(RafApplicationObject rafApplicationObject,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
