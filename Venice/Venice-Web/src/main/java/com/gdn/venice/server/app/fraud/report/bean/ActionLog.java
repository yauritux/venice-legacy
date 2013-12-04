package com.gdn.venice.server.app.fraud.report.bean;

import java.sql.Timestamp;

public class ActionLog {
	String rowNumber;
	String actionType;
	String partyName;
	Timestamp actionLogDate;
	String notes;
	
	public String getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(String rowNumber) {
		this.rowNumber = rowNumber;
	}

	public String getActionType() {
		return actionType;
	}
	
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
	public String getPartyName() {
		return partyName;
	}
	
	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}
	
	public Timestamp getActionLogDate() {
		return actionLogDate;
	}
	
	public void setActionLogDate(Timestamp actionLogDate) {
		this.actionLogDate = actionLogDate;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
 }
