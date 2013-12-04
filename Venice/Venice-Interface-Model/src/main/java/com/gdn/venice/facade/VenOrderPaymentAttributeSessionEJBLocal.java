package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.VenOrderPaymentAttribute;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface VenOrderPaymentAttributeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAttributeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<VenOrderPaymentAttribute> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAttributeSessionEJBRemote#persistVenOrderPaymentAttribute(com
	 * .gdn.venice.persistence.VenOrderPaymentAttribute)
	 */
	public VenOrderPaymentAttribute persistVenOrderPaymentAttribute(VenOrderPaymentAttribute venOrderPaymentAttribute);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAttributeSessionEJBRemote#persistVenOrderPaymentAttributeList
	 * (java.util.List)
	 */
	public ArrayList<VenOrderPaymentAttribute> persistVenOrderPaymentAttributeList(
			List<VenOrderPaymentAttribute> venOrderPaymentAttributeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAttributeSessionEJBRemote#mergeVenOrderPaymentAttribute(com.
	 * gdn.venice.persistence.VenOrderPaymentAttribute)
	 */
	public VenOrderPaymentAttribute mergeVenOrderPaymentAttribute(VenOrderPaymentAttribute venOrderPaymentAttribute);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAttributeSessionEJBRemote#mergeVenOrderPaymentAttributeList(
	 * java.util.List)
	 */
	public ArrayList<VenOrderPaymentAttribute> mergeVenOrderPaymentAttributeList(
			List<VenOrderPaymentAttribute> venOrderPaymentAttributeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAttributeSessionEJBRemote#removeVenOrderPaymentAttribute(com
	 * .gdn.venice.persistence.VenOrderPaymentAttribute)
	 */
	public void removeVenOrderPaymentAttribute(VenOrderPaymentAttribute venOrderPaymentAttribute);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAttributeSessionEJBRemote#removeVenOrderPaymentAttributeList
	 * (java.util.List)
	 */
	public void removeVenOrderPaymentAttributeList(List<VenOrderPaymentAttribute> venOrderPaymentAttributeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAttributeSessionEJBRemote#findByVenOrderPaymentAttributeLike
	 * (com.gdn.venice.persistence.VenOrderPaymentAttribute, int, int)
	 */
	public List<VenOrderPaymentAttribute> findByVenOrderPaymentAttributeLike(VenOrderPaymentAttribute venOrderPaymentAttribute,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.VenOrderPaymentAttributeSessionEJBRemote#findByVenOrderPaymentAttributeLikeFR
	 * (com.gdn.venice.persistence.VenOrderPaymentAttribute, int, int)
	 */
	public FinderReturn findByVenOrderPaymentAttributeLikeFR(VenOrderPaymentAttribute venOrderPaymentAttribute,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
