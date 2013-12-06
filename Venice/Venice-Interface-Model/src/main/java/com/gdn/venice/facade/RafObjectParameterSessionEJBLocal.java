package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.RafObjectParameter;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface RafObjectParameterSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectParameterSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<RafObjectParameter> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectParameterSessionEJBRemote#persistRafObjectParameter(com
	 * .gdn.venice.persistence.RafObjectParameter)
	 */
	public RafObjectParameter persistRafObjectParameter(RafObjectParameter rafObjectParameter);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectParameterSessionEJBRemote#persistRafObjectParameterList
	 * (java.util.List)
	 */
	public ArrayList<RafObjectParameter> persistRafObjectParameterList(
			List<RafObjectParameter> rafObjectParameterList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectParameterSessionEJBRemote#mergeRafObjectParameter(com.
	 * gdn.venice.persistence.RafObjectParameter)
	 */
	public RafObjectParameter mergeRafObjectParameter(RafObjectParameter rafObjectParameter);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectParameterSessionEJBRemote#mergeRafObjectParameterList(
	 * java.util.List)
	 */
	public ArrayList<RafObjectParameter> mergeRafObjectParameterList(
			List<RafObjectParameter> rafObjectParameterList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectParameterSessionEJBRemote#removeRafObjectParameter(com
	 * .gdn.venice.persistence.RafObjectParameter)
	 */
	public void removeRafObjectParameter(RafObjectParameter rafObjectParameter);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectParameterSessionEJBRemote#removeRafObjectParameterList
	 * (java.util.List)
	 */
	public void removeRafObjectParameterList(List<RafObjectParameter> rafObjectParameterList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectParameterSessionEJBRemote#findByRafObjectParameterLike
	 * (com.gdn.venice.persistence.RafObjectParameter, int, int)
	 */
	public List<RafObjectParameter> findByRafObjectParameterLike(RafObjectParameter rafObjectParameter,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.RafObjectParameterSessionEJBRemote#findByRafObjectParameterLikeFR
	 * (com.gdn.venice.persistence.RafObjectParameter, int, int)
	 */
	public FinderReturn findByRafObjectParameterLikeFR(RafObjectParameter rafObjectParameter,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
