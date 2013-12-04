package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule7;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule7SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule7SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule7> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule7SessionEJBRemote#persistFrdParameterRule7(com
	 * .gdn.venice.persistence.FrdParameterRule7)
	 */
	public FrdParameterRule7 persistFrdParameterRule7(FrdParameterRule7 frdParameterRule7);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule7SessionEJBRemote#persistFrdParameterRule7List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule7> persistFrdParameterRule7List(
			List<FrdParameterRule7> frdParameterRule7List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule7SessionEJBRemote#mergeFrdParameterRule7(com.
	 * gdn.venice.persistence.FrdParameterRule7)
	 */
	public FrdParameterRule7 mergeFrdParameterRule7(FrdParameterRule7 frdParameterRule7);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule7SessionEJBRemote#mergeFrdParameterRule7List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule7> mergeFrdParameterRule7List(
			List<FrdParameterRule7> frdParameterRule7List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule7SessionEJBRemote#removeFrdParameterRule7(com
	 * .gdn.venice.persistence.FrdParameterRule7)
	 */
	public void removeFrdParameterRule7(FrdParameterRule7 frdParameterRule7);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule7SessionEJBRemote#removeFrdParameterRule7List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule7List(List<FrdParameterRule7> frdParameterRule7List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule7SessionEJBRemote#findByFrdParameterRule7Like
	 * (com.gdn.venice.persistence.FrdParameterRule7, int, int)
	 */
	public List<FrdParameterRule7> findByFrdParameterRule7Like(FrdParameterRule7 frdParameterRule7,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule7SessionEJBRemote#findByFrdParameterRule7LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule7, int, int)
	 */
	public FinderReturn findByFrdParameterRule7LikeFR(FrdParameterRule7 frdParameterRule7,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
