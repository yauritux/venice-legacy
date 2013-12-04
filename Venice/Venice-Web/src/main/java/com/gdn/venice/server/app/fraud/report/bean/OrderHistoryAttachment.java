package com.gdn.venice.server.app.fraud.report.bean;

public class OrderHistoryAttachment {
	private String fileName;
	private String description;
	private String createdBy;
	
	public OrderHistoryAttachment(String createdBy, String description, String fileName){
		setCreatedBy(createdBy);
		setDescription(description);
		setFileName(fileName);
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedBy() {
		return createdBy;
	}
}
