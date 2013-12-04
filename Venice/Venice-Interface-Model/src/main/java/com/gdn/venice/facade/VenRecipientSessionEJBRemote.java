package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenRecipient;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenRecipientSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenRecipient
	 */
	public List<VenRecipient> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenRecipient persists a country
	 * 
	 * @param venRecipient
	 * @return the persisted VenRecipient
	 */
	public VenRecipient persistVenRecipient(VenRecipient venRecipient);

	/**
	 * persistVenRecipientList - persists a list of VenRecipient
	 * 
	 * @param venRecipientList
	 * @return the list of persisted VenRecipient
	 */
	public ArrayList<VenRecipient> persistVenRecipientList(
			List<VenRecipient> venRecipientList);

	/**
	 * mergeVenRecipient - merges a VenRecipient
	 * 
	 * @param venRecipient
	 * @return the merged VenRecipient
	 */
	public VenRecipient mergeVenRecipient(VenRecipient venRecipient);

	/**
	 * mergeVenRecipientList - merges a list of VenRecipient
	 * 
	 * @param venRecipientList
	 * @return the merged list of VenRecipient
	 */
	public ArrayList<VenRecipient> mergeVenRecipientList(
			List<VenRecipient> venRecipientList);

	/**
	 * removeVenRecipient - removes a VenRecipient
	 * 
	 * @param venRecipient
	 */
	public void removeVenRecipient(VenRecipient venRecipient);

	/**
	 * removeVenRecipientList - removes a list of VenRecipient
	 * 
	 * @param venRecipientList
	 */
	public void removeVenRecipientList(List<VenRecipient> venRecipientList);

	/**
	 * findByVenRecipientLike - finds a list of VenRecipient Like
	 * 
	 * @param venRecipient
	 * @return the list of VenRecipient found
	 */
	public List<VenRecipient> findByVenRecipientLike(VenRecipient venRecipient,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenRecipient>LikeFR - finds a list of VenRecipient> Like with a finder return object
	 * 
	 * @param venRecipient
	 * @return the list of VenRecipient found
	 */
	public FinderReturn findByVenRecipientLikeFR(VenRecipient venRecipient,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
