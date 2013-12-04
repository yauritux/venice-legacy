package com.gdn.venice.constants;

/**
 * 
 * @author yauritux
 *
 */
public enum FinApprovalStatusConstants {
	
	FIN_APPROVAL_STATUS_NEW(0),
	FIN_APPROVAL_STATUS_SUBMITTED(1),
	FIN_APPROVAL_STATUS_APPROVED(2),
	FIN_APPROVAL_STATUS_REJECTED(3);
	
	private long id;
	
	public Long id() {
		return id;
	}
	
	private FinApprovalStatusConstants(long id) {
		this.id = id;
	}

}
