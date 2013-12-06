package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FinApInvoice;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FinApInvoiceSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApInvoiceSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FinApInvoice> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApInvoiceSessionEJBRemote#persistFinApInvoice(com
	 * .gdn.venice.persistence.FinApInvoice)
	 */
	public FinApInvoice persistFinApInvoice(FinApInvoice finApInvoice);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApInvoiceSessionEJBRemote#persistFinApInvoiceList
	 * (java.util.List)
	 */
	public ArrayList<FinApInvoice> persistFinApInvoiceList(
			List<FinApInvoice> finApInvoiceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApInvoiceSessionEJBRemote#mergeFinApInvoice(com.
	 * gdn.venice.persistence.FinApInvoice)
	 */
	public FinApInvoice mergeFinApInvoice(FinApInvoice finApInvoice);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApInvoiceSessionEJBRemote#mergeFinApInvoiceList(
	 * java.util.List)
	 */
	public ArrayList<FinApInvoice> mergeFinApInvoiceList(
			List<FinApInvoice> finApInvoiceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApInvoiceSessionEJBRemote#removeFinApInvoice(com
	 * .gdn.venice.persistence.FinApInvoice)
	 */
	public void removeFinApInvoice(FinApInvoice finApInvoice);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApInvoiceSessionEJBRemote#removeFinApInvoiceList
	 * (java.util.List)
	 */
	public void removeFinApInvoiceList(List<FinApInvoice> finApInvoiceList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApInvoiceSessionEJBRemote#findByFinApInvoiceLike
	 * (com.gdn.venice.persistence.FinApInvoice, int, int)
	 */
	public List<FinApInvoice> findByFinApInvoiceLike(FinApInvoice finApInvoice,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FinApInvoiceSessionEJBRemote#findByFinApInvoiceLikeFR
	 * (com.gdn.venice.persistence.FinApInvoice, int, int)
	 */
	public FinderReturn findByFinApInvoiceLikeFR(FinApInvoice finApInvoice,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
