package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.VenOrderPaymentAttribute;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface VenOrderPaymentAttributeSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of VenOrderPaymentAttribute
	 */
	public List<VenOrderPaymentAttribute> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistVenOrderPaymentAttribute persists a country
	 * 
	 * @param venOrderPaymentAttribute
	 * @return the persisted VenOrderPaymentAttribute
	 */
	public VenOrderPaymentAttribute persistVenOrderPaymentAttribute(VenOrderPaymentAttribute venOrderPaymentAttribute);

	/**
	 * persistVenOrderPaymentAttributeList - persists a list of VenOrderPaymentAttribute
	 * 
	 * @param venOrderPaymentAttributeList
	 * @return the list of persisted VenOrderPaymentAttribute
	 */
	public ArrayList<VenOrderPaymentAttribute> persistVenOrderPaymentAttributeList(
			List<VenOrderPaymentAttribute> venOrderPaymentAttributeList);

	/**
	 * mergeVenOrderPaymentAttribute - merges a VenOrderPaymentAttribute
	 * 
	 * @param venOrderPaymentAttribute
	 * @return the merged VenOrderPaymentAttribute
	 */
	public VenOrderPaymentAttribute mergeVenOrderPaymentAttribute(VenOrderPaymentAttribute venOrderPaymentAttribute);

	/**
	 * mergeVenOrderPaymentAttributeList - merges a list of VenOrderPaymentAttribute
	 * 
	 * @param venOrderPaymentAttributeList
	 * @return the merged list of VenOrderPaymentAttribute
	 */
	public ArrayList<VenOrderPaymentAttribute> mergeVenOrderPaymentAttributeList(
			List<VenOrderPaymentAttribute> venOrderPaymentAttributeList);

	/**
	 * removeVenOrderPaymentAttribute - removes a VenOrderPaymentAttribute
	 * 
	 * @param venOrderPaymentAttribute
	 */
	public void removeVenOrderPaymentAttribute(VenOrderPaymentAttribute venOrderPaymentAttribute);

	/**
	 * removeVenOrderPaymentAttributeList - removes a list of VenOrderPaymentAttribute
	 * 
	 * @param venOrderPaymentAttributeList
	 */
	public void removeVenOrderPaymentAttributeList(List<VenOrderPaymentAttribute> venOrderPaymentAttributeList);

	/**
	 * findByVenOrderPaymentAttributeLike - finds a list of VenOrderPaymentAttribute Like
	 * 
	 * @param venOrderPaymentAttribute
	 * @return the list of VenOrderPaymentAttribute found
	 */
	public List<VenOrderPaymentAttribute> findByVenOrderPaymentAttributeLike(VenOrderPaymentAttribute venOrderPaymentAttribute,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByVenOrderPaymentAttribute>LikeFR - finds a list of VenOrderPaymentAttribute> Like with a finder return object
	 * 
	 * @param venOrderPaymentAttribute
	 * @return the list of VenOrderPaymentAttribute found
	 */
	public FinderReturn findByVenOrderPaymentAttributeLikeFR(VenOrderPaymentAttribute venOrderPaymentAttribute,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
