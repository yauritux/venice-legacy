package com.gdn.venice.server.app.fraud.dataimport;

import java.math.BigDecimal;
import java.util.Date;

public class MandiriInstalmentReport implements PojoInterface {
	

	private String transactionid;
	private String merchanttransactionid;        
	private String merchantaccountno;        
	private Date datecreated ;       
	private String cardno;       
	private String cardtype;        
	private String cardholdername ;       
	private String currency;        
	private BigDecimal amount;        
	private String installmentcode;        
	private String errdesc;        
	private Date trandate ;       
	private String status ;       
	private String authorizationcode ;       
	private String responsecode ;       
	private String ecicode ;      
	
	
	public String getTransactionid() {
		return transactionid;
	}
	public void setTransactionid(String transactionid) {
		this.transactionid = transactionid;
	}
	public String getMerchanttransactionid() {
		return merchanttransactionid;
	}
	public void setMerchanttransactionid(String merchanttransactionid) {
		this.merchanttransactionid = merchanttransactionid;
	}
	public String getMerchantaccountno() {
		return merchantaccountno;
	}
	public void setMerchantaccountno(String merchantaccountno) {
		this.merchantaccountno = merchantaccountno;
	}
	public Date getDatecreated() {
		return datecreated;
	}
	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getCardtype() {
		return cardtype;
	}
	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}
	public String getCardholdername() {
		return cardholdername;
	}
	public void setCardholdername(String cardholdername) {
		this.cardholdername = cardholdername;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getInstallmentcode() {
		return installmentcode;
	}
	public void setInstallmentcode(String installmentcode) {
		this.installmentcode = installmentcode;
	}
	public String getErrdesc() {
		return errdesc;
	}
	public void setErrdesc(String errdesc) {
		this.errdesc = errdesc;
	}
	public Date getTrandate() {
		return trandate;
	}
	public void setTrandate(Date trandate) {
		this.trandate = trandate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getAuthorizationcode() {
		return authorizationcode;
	}
	public void setAuthorizationcode(String authorizationcode) {
		this.authorizationcode = authorizationcode;
	}
	public String getResponsecode() {
		return responsecode;
	}
	public void setResponsecode(String responsecode) {
		this.responsecode = responsecode;
	}
	public String getEcicode() {
		return ecicode;
	}
	public void setEcicode(String ecicode) {
		this.ecicode = ecicode;
	}

}
