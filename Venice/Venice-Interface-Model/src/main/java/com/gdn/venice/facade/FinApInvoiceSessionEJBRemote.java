package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Remote;

import com.gdn.venice.persistence.FinApInvoice;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Remote
public interface FinApInvoiceSessionEJBRemote {

	/**
	 * queryByRange - allows querying by range/block
	 * 
	 * @param jpqlStmt
	 * @param firstResult
	 * @param maxResults
	 * @return a list of FinApInvoice
	 */
	public List<FinApInvoice> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/**
	 * persistFinApInvoice persists a country
	 * 
	 * @param finApInvoice
	 * @return the persisted FinApInvoice
	 */
	public FinApInvoice persistFinApInvoice(FinApInvoice finApInvoice);

	/**
	 * persistFinApInvoiceList - persists a list of FinApInvoice
	 * 
	 * @param finApInvoiceList
	 * @return the list of persisted FinApInvoice
	 */
	public ArrayList<FinApInvoice> persistFinApInvoiceList(
			List<FinApInvoice> finApInvoiceList);

	/**
	 * mergeFinApInvoice - merges a FinApInvoice
	 * 
	 * @param finApInvoice
	 * @return the merged FinApInvoice
	 */
	public FinApInvoice mergeFinApInvoice(FinApInvoice finApInvoice);

	/**
	 * mergeFinApInvoiceList - merges a list of FinApInvoice
	 * 
	 * @param finApInvoiceList
	 * @return the merged list of FinApInvoice
	 */
	public ArrayList<FinApInvoice> mergeFinApInvoiceList(
			List<FinApInvoice> finApInvoiceList);

	/**
	 * removeFinApInvoice - removes a FinApInvoice
	 * 
	 * @param finApInvoice
	 */
	public void removeFinApInvoice(FinApInvoice finApInvoice);

	/**
	 * removeFinApInvoiceList - removes a list of FinApInvoice
	 * 
	 * @param finApInvoiceList
	 */
	public void removeFinApInvoiceList(List<FinApInvoice> finApInvoiceList);

	/**
	 * findByFinApInvoiceLike - finds a list of FinApInvoice Like
	 * 
	 * @param finApInvoice
	 * @return the list of FinApInvoice found
	 */
	public List<FinApInvoice> findByFinApInvoiceLike(FinApInvoice finApInvoice,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/**
	 * findByFinApInvoice>LikeFR - finds a list of FinApInvoice> Like with a finder return object
	 * 
	 * @param finApInvoice
	 * @return the list of FinApInvoice found
	 */
	public FinderReturn findByFinApInvoiceLikeFR(FinApInvoice finApInvoice,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
}
