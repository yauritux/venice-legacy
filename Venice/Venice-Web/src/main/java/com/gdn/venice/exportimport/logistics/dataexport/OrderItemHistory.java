package com.gdn.venice.exportimport.logistics.dataexport;

import java.util.Date;

public class OrderItemHistory {
	private String orderItemId;
	private Date historyTimestamp;
	private String historyStatus;
	private String historyNotes;

	public String getOrderItemId() {
		return orderItemId;
	}
	public void setOrderItemId(String orderItemId) {
		this.orderItemId = orderItemId;
	}
	public Date getHistoryTimestamp() {
		return historyTimestamp;
	}
	public void setHistoryTimestamp(Date historyTimestamp) {
		this.historyTimestamp = historyTimestamp;
	}
	public String getHistoryStatus() {
		return historyStatus;
	}
	public void setHistoryStatus(String historyStatus) {
		this.historyStatus = historyStatus;
	}
	public String getHistoryNotes() {
		return historyNotes;
	}
	public void setHistoryNotes(String historyNotes) {
		this.historyNotes = historyNotes;
	}
}
