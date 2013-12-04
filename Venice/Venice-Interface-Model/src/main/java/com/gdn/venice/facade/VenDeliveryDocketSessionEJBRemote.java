package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenDeliveryDocket;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenDeliveryDocketSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenDeliveryDocket
	 */
	public List<VenDeliveryDocket> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenDeliveryDocket persists a country
	 * 
	 * @param venDeliveryDocket
	 * @return the persisted VenDeliveryDocket
	 */
	public VenDeliveryDocket persistVenDeliveryDocket(VenDeliveryDocket venDeliveryDocket);

	/**
	 * persistVenDeliveryDocketList - persists a list of VenDeliveryDocket
	 * 
	 * @param venDeliveryDocketList
	 * @return the list of persisted VenDeliveryDocket
	 */
	public ArrayList<VenDeliveryDocket> persistVenDeliveryDocketList(
			List<VenDeliveryDocket> venDeliveryDocketList);

	/**
	 * mergeVenDeliveryDocket - merges a VenDeliveryDocket
	 * 
	 * @param venDeliveryDocket
	 * @return the merged VenDeliveryDocket
	 */
	public VenDeliveryDocket mergeVenDeliveryDocket(VenDeliveryDocket venDeliveryDocket);

	/**
	 * mergeVenDeliveryDocketList - merges a list of VenDeliveryDocket
	 * 
	 * @param venDeliveryDocketList
	 * @return the merged list of VenDeliveryDocket
	 */
	public ArrayList<VenDeliveryDocket> mergeVenDeliveryDocketList(
			List<VenDeliveryDocket> venDeliveryDocketList);

	/**
	 * removeVenDeliveryDocket - removes a VenDeliveryDocket
	 * 
	 * @param venDeliveryDocket
	 */
	public void removeVenDeliveryDocket(VenDeliveryDocket venDeliveryDocket);

	/**
	 * removeVenDeliveryDocketList - removes a list of VenDeliveryDocket
	 * 
	 * @param venDeliveryDocketList
	 */
	public void removeVenDeliveryDocketList(List<VenDeliveryDocket> venDeliveryDocketList);

	/**
	 * findByVenDeliveryDocketLike - finds a list of VenDeliveryDocket Like
	 * 
	 * @param venDeliveryDocket
	 * @return the list of VenDeliveryDocket found
	 */
	public List<VenDeliveryDocket> findByVenDeliveryDocketLike(VenDeliveryDocket venDeliveryDocket,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenDeliveryDocket>LikeFR - finds a list of VenDeliveryDocket> Like with a finder return object
	 * 
	 * @param venDeliveryDocket
	 * @return the list of VenDeliveryDocket found
	 */
	public FinderReturn findByVenDeliveryDocketLikeFR(VenDeliveryDocket venDeliveryDocket,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
