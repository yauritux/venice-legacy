package com.gdn.venice.facade;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Local;

import com.gdn.venice.persistence.FrdEmailType;
import com.djarum.raf.utilities.JPQLAdvancedQueryCriteria;
import com.gdn.venice.facade.finder.FinderReturn;

@Local
public interface FrdEmailTypeSessionEJBLocal {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEmailTypeSessionEJBRemote#queryByRange(java.lang
	 * .String, int, int)
	 */
	public List<FrdEmailType> queryByRange(String jpqlStmt, int firstResult,
			int maxResults);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEmailTypeSessionEJBRemote#persistFrdEmailType(com
	 * .gdn.venice.persistence.FrdEmailType)
	 */
	public FrdEmailType persistFrdEmailType(FrdEmailType frdEmailType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEmailTypeSessionEJBRemote#persistFrdEmailTypeList
	 * (java.util.List)
	 */
	public ArrayList<FrdEmailType> persistFrdEmailTypeList(
			List<FrdEmailType> frdEmailTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEmailTypeSessionEJBRemote#mergeFrdEmailType(com.
	 * gdn.venice.persistence.FrdEmailType)
	 */
	public FrdEmailType mergeFrdEmailType(FrdEmailType frdEmailType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEmailTypeSessionEJBRemote#mergeFrdEmailTypeList(
	 * java.util.List)
	 */
	public ArrayList<FrdEmailType> mergeFrdEmailTypeList(
			List<FrdEmailType> frdEmailTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEmailTypeSessionEJBRemote#removeFrdEmailType(com
	 * .gdn.venice.persistence.FrdEmailType)
	 */
	public void removeFrdEmailType(FrdEmailType frdEmailType);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEmailTypeSessionEJBRemote#removeFrdEmailTypeList
	 * (java.util.List)
	 */
	public void removeFrdEmailTypeList(List<FrdEmailType> frdEmailTypeList);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEmailTypeSessionEJBRemote#findByFrdEmailTypeLike
	 * (com.gdn.venice.persistence.FrdEmailType, int, int)
	 */
	public List<FrdEmailType> findByFrdEmailTypeLike(FrdEmailType frdEmailType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
			
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.gdn.venice.facade.FrdEmailTypeSessionEJBRemote#findByFrdEmailTypeLikeFR
	 * (com.gdn.venice.persistence.FrdEmailType, int, int)
	 */
	public FinderReturn findByFrdEmailTypeLikeFR(FrdEmailType frdEmailType,
			JPQLAdvancedQueryCriteria criteria, int firstResult, int maxResults);
	

}
