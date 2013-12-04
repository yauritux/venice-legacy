package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule30;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule30SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule30SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule30> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule30SessionEJBRemote#persistFrdParameterRule30(com
	 * .gdn.venice.persistence.FrdParameterRule30)
	 */
	public FrdParameterRule30 persistFrdParameterRule30(FrdParameterRule30 frdParameterRule30);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule30SessionEJBRemote#persistFrdParameterRule30List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule30> persistFrdParameterRule30List(
			List<FrdParameterRule30> frdParameterRule30List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule30SessionEJBRemote#mergeFrdParameterRule30(com.
	 * gdn.venice.persistence.FrdParameterRule30)
	 */
	public FrdParameterRule30 mergeFrdParameterRule30(FrdParameterRule30 frdParameterRule30);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule30SessionEJBRemote#mergeFrdParameterRule30List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule30> mergeFrdParameterRule30List(
			List<FrdParameterRule30> frdParameterRule30List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule30SessionEJBRemote#removeFrdParameterRule30(com
	 * .gdn.venice.persistence.FrdParameterRule30)
	 */
	public void removeFrdParameterRule30(FrdParameterRule30 frdParameterRule30);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule30SessionEJBRemote#removeFrdParameterRule30List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule30List(List<FrdParameterRule30> frdParameterRule30List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule30SessionEJBRemote#findByFrdParameterRule30Like
	 * (com.gdn.venice.persistence.FrdParameterRule30, int, int)
	 */
	public List<FrdParameterRule30> findByFrdParameterRule30Like(FrdParameterRule30 frdParameterRule30,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule30SessionEJBRemote#findByFrdParameterRule30LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule30, int, int)
	 */
	public FinderReturn findByFrdParameterRule30LikeFR(FrdParameterRule30 frdParameterRule30,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
