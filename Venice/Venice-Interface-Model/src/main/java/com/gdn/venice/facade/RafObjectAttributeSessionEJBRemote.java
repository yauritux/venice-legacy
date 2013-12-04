package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.RafObjectAttribute;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface RafObjectAttributeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of RafObjectAttribute
	 */
	public List<RafObjectAttribute> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistRafObjectAttribute persists a country
	 * 
	 * @param rafObjectAttribute
	 * @return the persisted RafObjectAttribute
	 */
	public RafObjectAttribute persistRafObjectAttribute(RafObjectAttribute rafObjectAttribute);

	/**
	 * persistRafObjectAttributeList - persists a list of RafObjectAttribute
	 * 
	 * @param rafObjectAttributeList
	 * @return the list of persisted RafObjectAttribute
	 */
	public ArrayList<RafObjectAttribute> persistRafObjectAttributeList(
			List<RafObjectAttribute> rafObjectAttributeList);

	/**
	 * mergeRafObjectAttribute - merges a RafObjectAttribute
	 * 
	 * @param rafObjectAttribute
	 * @return the merged RafObjectAttribute
	 */
	public RafObjectAttribute mergeRafObjectAttribute(RafObjectAttribute rafObjectAttribute);

	/**
	 * mergeRafObjectAttributeList - merges a list of RafObjectAttribute
	 * 
	 * @param rafObjectAttributeList
	 * @return the merged list of RafObjectAttribute
	 */
	public ArrayList<RafObjectAttribute> mergeRafObjectAttributeList(
			List<RafObjectAttribute> rafObjectAttributeList);

	/**
	 * removeRafObjectAttribute - removes a RafObjectAttribute
	 * 
	 * @param rafObjectAttribute
	 */
	public void removeRafObjectAttribute(RafObjectAttribute rafObjectAttribute);

	/**
	 * removeRafObjectAttributeList - removes a list of RafObjectAttribute
	 * 
	 * @param rafObjectAttributeList
	 */
	public void removeRafObjectAttributeList(List<RafObjectAttribute> rafObjectAttributeList);

	/**
	 * findByRafObjectAttributeLike - finds a list of RafObjectAttribute Like
	 * 
	 * @param rafObjectAttribute
	 * @return the list of RafObjectAttribute found
	 */
	public List<RafObjectAttribute> findByRafObjectAttributeLike(RafObjectAttribute rafObjectAttribute,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByRafObjectAttribute>LikeFR - finds a list of RafObjectAttribute> Like with a finder return object
	 * 
	 * @param rafObjectAttribute
	 * @return the list of RafObjectAttribute found
	 */
	public FinderReturn findByRafObjectAttributeLikeFR(RafObjectAttribute rafObjectAttribute,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
