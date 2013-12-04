package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdParameterRule18;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdParameterRule18SessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule18SessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdParameterRule18> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule18SessionEJBRemote#persistFrdParameterRule18(com
	 * .gdn.venice.persistence.FrdParameterRule18)
	 */
	public FrdParameterRule18 persistFrdParameterRule18(FrdParameterRule18 frdParameterRule18);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule18SessionEJBRemote#persistFrdParameterRule18List
	 * (java.util.List)
	 */
	public ArrayList<FrdParameterRule18> persistFrdParameterRule18List(
			List<FrdParameterRule18> frdParameterRule18List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule18SessionEJBRemote#mergeFrdParameterRule18(com.
	 * gdn.venice.persistence.FrdParameterRule18)
	 */
	public FrdParameterRule18 mergeFrdParameterRule18(FrdParameterRule18 frdParameterRule18);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule18SessionEJBRemote#mergeFrdParameterRule18List(
	 * java.util.List)
	 */
	public ArrayList<FrdParameterRule18> mergeFrdParameterRule18List(
			List<FrdParameterRule18> frdParameterRule18List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule18SessionEJBRemote#removeFrdParameterRule18(com
	 * .gdn.venice.persistence.FrdParameterRule18)
	 */
	public void removeFrdParameterRule18(FrdParameterRule18 frdParameterRule18);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule18SessionEJBRemote#removeFrdParameterRule18List
	 * (java.util.List)
	 */
	public void removeFrdParameterRule18List(List<FrdParameterRule18> frdParameterRule18List);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule18SessionEJBRemote#findByFrdParameterRule18Like
	 * (com.gdn.venice.persistence.FrdParameterRule18, int, int)
	 */
	public List<FrdParameterRule18> findByFrdParameterRule18Like(FrdParameterRule18 frdParameterRule18,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdParameterRule18SessionEJBRemote#findByFrdParameterRule18LikeFR
	 * (com.gdn.venice.persistence.FrdParameterRule18, int, int)
	 */
	public FinderReturn findByFrdParameterRule18LikeFR(FrdParameterRule18 frdParameterRule18,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
