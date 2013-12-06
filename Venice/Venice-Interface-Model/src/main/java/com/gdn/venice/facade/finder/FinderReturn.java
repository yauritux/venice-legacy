package com.gdn.venice.facade.finder;

import java.io.Serializable;
import java.util.List;

/**
 * Return class for finder methods in the session interface 
 * that encapsulates the total number of rows and the result set list.
 * Each session EJB provides a findByEntityXLike both returning an
 * entity list directly and also findByEntityXLikeFR returning a FinderReturn. 
 * 
 * This is so that the programmer has the choice of either with
 * the count(*) operation for obtaining the number of query rows 
 * or without. For small entiies where caching and buffering across
 * the network is not an issue then the operation without the 
 * FinderReturn should be used. Where network performance is crucial
 * and the result set could be large then using the FinderReturn 
 * method allows the programmer to set the scroll in the client based
 * on the number of total query rows. Note that buffering is also 
 * used with the regular findByEntityXLike methods but the results
 * are returned as a list with no wrapper.  
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class FinderReturn implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long numQueryRows = new Long(0);
	
	private List<?> resultList = null;

	/**
	 * @return the numQueryRows
	 */
	public Long getNumQUeryRows() {
		return numQueryRows;
	}

	/**
	 * @param numQueryRows the numQueryRows to set
	 */
	public void setNumQueryRows(Long numQueryRows) {
		this.numQueryRows = numQueryRows;
	}

	/**
	 * @return the resultList
	 */
	public List<?> getResultList() {
		return resultList;
	}

	/**
	 * @param resultList the resultList to set
	 */
	public void setResultList(List<?> resultList) {
		this.resultList = resultList;
	}

}
