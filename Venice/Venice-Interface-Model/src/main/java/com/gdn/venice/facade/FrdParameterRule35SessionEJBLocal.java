package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule35;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule35SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule35SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule35> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule35SessionEJBRemote#persistFrdParameterRule35(com
	 * .gdn.venice.persistence.FrdParameterRule35)
	 */
	public FrdParameterRule35 persistFrdParameterRule35(FrdParameterRule35 frdParameterRule35);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule35SessionEJBRemote#persistFrdParameterRule35List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule35> persistFrdParameterRule35List(
			List<FrdParameterRule35> frdParameterRule35List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule35SessionEJBRemote#mergeFrdParameterRule35(com.
	 * gdn.venice.persistence.FrdParameterRule35)
	 */
	public FrdParameterRule35 mergeFrdParameterRule35(FrdParameterRule35 frdParameterRule35);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule35SessionEJBRemote#mergeFrdParameterRule35List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule35> mergeFrdParameterRule35List(
			List<FrdParameterRule35> frdParameterRule35List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule35SessionEJBRemote#removeFrdParameterRule35(com
	 * .gdn.venice.persistence.FrdParameterRule35)
	 */
	public void removeFrdParameterRule35(FrdParameterRule35 frdParameterRule35);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule35SessionEJBRemote#removeFrdParameterRule35List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule35List(List<FrdParameterRule35> frdParameterRule35List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule35SessionEJBRemote#findByFrdParameterRule35Like
	 * (com.gdn.venice.persistence.FrdParameterRule35, int, int)
	 */
	public List<FrdParameterRule35> findByFrdParameterRule35Like(FrdParameterRule35 frdParameterRule35,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule35SessionEJBRemote#findByFrdParameterRule35LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule35, int, int)
	 */
	public FinderReturn findByFrdParameterRule35LikeFR(FrdParameterRule35 frdParameterRule35,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
