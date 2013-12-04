package com.gdn.venice.finance.integration.bean;

public class Merchant {

	long id;
	String merchantCode;
	String acctNo;
	String bankName;
	String bankBranch;
	String acctName;
	
	public Merchant() {
    }
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getMerchantCode() {
		return merchantCode;
	}
	
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	
	public String getAcctNo() {
		return acctNo;
	}
	
	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}
	
	public String getBankName() {
		return bankName;
	}
	
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	
	public String getBankBranch() {
		return bankBranch;
	}
	
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}
	
	public String getAcctName() {
		return acctName;
	}
	
	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}
}
