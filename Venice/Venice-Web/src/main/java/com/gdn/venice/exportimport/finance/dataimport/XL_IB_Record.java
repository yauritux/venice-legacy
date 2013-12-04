package com.gdn.venice.exportimport.finance.dataimport;
import com.gdn.venice.hssf.PojoInterface;

/**
 * Pojo class for importing the MIGS report from the Excel spreadsheet.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class XL_IB_Record implements PojoInterface{
	private String tempCol;
	private String referenceId;
	private String date;
	private String time;
	private String merchantCode;
	private String merchantName;
	private String transType;
	private String merchtBeginning;
	private String transAmount;
	private String discount;
	private String merchtEnding;
	private String custNumber;
	private String custName;
	private String status;
	
	public String getTempCol() {
		return tempCol;
	}
	public void setTempCol(String tempCol) {
		this.tempCol = tempCol;
	}
	public String getReferenceId() {
		return referenceId;
	}
	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getMerchantCode() {
		return merchantCode;
	}
	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}
	public String getMerchantName() {
		return merchantName;
	}
	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public String getMerchtBeginning() {
		return merchtBeginning;
	}
	public void setMerchtBeginning(String merchtBeginning) {
		this.merchtBeginning = merchtBeginning;
	}
	public String getTransAmount() {
		return transAmount;
	}
	public void setTransAmount(String transAmount) {
		this.transAmount = transAmount;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getMerchtEnding() {
		return merchtEnding;
	}
	public void setMerchtEnding(String merchtEnding) {
		this.merchtEnding = merchtEnding;
	}
	public String getCustNumber() {
		return custNumber;
	}
	public void setCustNumber(String custNumber) {
		this.custNumber = custNumber;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}	
	
	
	
}
