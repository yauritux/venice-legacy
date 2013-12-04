package com.gdn.venice.server.app.fraud.report.bean;

public class OrderHistoryContactDetail {
	private String contactType;
	private String contactDetail;
	
	public OrderHistoryContactDetail(String _contactType, String _contactDetail) {
		// TODO Auto-generated constructor stub
		setContactType(_contactType);
		setContactDetail(_contactDetail);
	}
	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	public String getContactType() {
		return contactType;
	}
	public void setContactDetail(String contactDetail) {
		this.contactDetail = contactDetail;
	}
	public String getContactDetail() {
		return contactDetail;
	}
}
