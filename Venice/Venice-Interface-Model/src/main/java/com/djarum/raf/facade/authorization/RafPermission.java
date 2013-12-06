package com.djarum.raf.facade.authorization;

import java.io.Serializable;

/**
 * A RafPermission object containing the canonical name for the object and the permission type
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class RafPermission implements Serializable{
	private static final long serialVersionUID = 1L;
	String applicationObjectCanonicalName = null;
	Long permissionTypeId = null;
	
	/**
	 * @return the applicationObjectCanonicalName
	 */
	public String getApplicationObjectCanonicalName() {
		return applicationObjectCanonicalName;
	}
	
	/**
	 * @param applicationObjectCanonicalName the applicationObjectCanonicalName to set
	 */
	public void setApplicationObjectCanonicalName(
			String applicationObjectCanonicalName) {
		this.applicationObjectCanonicalName = applicationObjectCanonicalName;
	}
	
	/**
	 * @return the permissionTypeId
	 */
	public Long getPermissionTypeId() {
		return permissionTypeId;
	}
	
	/**
	 * @param permissionTypeId the permissionTypeId to set
	 */
	public void setPermissionTypeId(Long permissionTypeId) {
		this.permissionTypeId = permissionTypeId;
	}
}
