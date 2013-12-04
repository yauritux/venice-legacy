package com.gdn.venice.server.app.fraud.report.bean;

public class OrderHistoryRiskPoint {
	String ruleName;
	Integer riskPoint;
	
	public String getRuleName() {
		return ruleName;
	}
	
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}
	
	public Integer getRiskPoint() {
		return riskPoint;
	}
	
	public void setRiskPoint(Integer riskPoint) {
		this.riskPoint = riskPoint;
	}
}
