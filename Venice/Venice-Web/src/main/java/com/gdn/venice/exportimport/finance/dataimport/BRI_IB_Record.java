package com.gdn.venice.exportimport.finance.dataimport;
import java.sql.Timestamp;

import com.gdn.venice.hssf.PojoInterface;

/**
 * Pojo class for importing the BRI report from the text spreadsheet.
 * 
 * 
 */
public class BRI_IB_Record implements PojoInterface{
	
	private String payeeId;
	private String billReferenceNo;
	private String billAccountNo;
	private Double amount;
	private String status;
	private String paymentRefNo;
	private Timestamp transactionDateTime;
	private Double bankFee;
	
	public String getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}
	public String getBillReferenceNo() {
		return billReferenceNo;
	}
	public void setBillReferenceNo(String billReferenceNo) {
		this.billReferenceNo = billReferenceNo;
	}
	public String getBillAccountNo() {
		return billAccountNo;
	}
	public void setBillAccountNo(String billAccountNo) {
		this.billAccountNo = billAccountNo;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPaymentRefNo() {
		return paymentRefNo;
	}
	public void setPaymentRefNo(String paymentRefNo) {
		this.paymentRefNo = paymentRefNo;
	}
	public Timestamp getTransactionDateTime() {
		return transactionDateTime;
	}
	public void setTransactionDateTime(Timestamp transactionDateTime) {
		this.transactionDateTime = transactionDateTime;
	}	
	public Double getBankFee() {
		return bankFee;
	}
	public void setBankFee(Double bankFee) {
		this.bankFee = bankFee;
	}
	
}
