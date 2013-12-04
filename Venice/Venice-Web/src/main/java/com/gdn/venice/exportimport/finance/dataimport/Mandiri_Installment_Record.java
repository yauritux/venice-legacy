package com.gdn.venice.exportimport.finance.dataimport;

import com.gdn.venice.hssf.PojoInterface;

public class Mandiri_Installment_Record implements PojoInterface {
	private String mId ;
	private String merchantOfficial;
	private String tradingName ;
	private String bankMandiriACC; 
	private String otherBankACC;     
	private String  merchACC ;      
	private String trxDate;       
	private String settleDate ;      
	private String trxCodeE ;       
	private String description ;       
	private String card;        
	private String crdType;        
	private String tId ;       
	private String authCode ;      
	private String paymentBatch;        
	private String tIdBach;      
	private String batchSeq;      
	private String amount;       
	private String nonMdrAmount;
	
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public String getMerchantOfficial() {
		return merchantOfficial;
	}
	public void setMerchantOfficial(String merchantOfficial) {
		this.merchantOfficial = merchantOfficial;
	}
	public String getTradingName() {
		return tradingName;
	}
	public void setTradingName(String tradingName) {
		this.tradingName = tradingName;
	}
	public String getBankMandiriACC() {
		return bankMandiriACC;
	}
	public void setBankMandiriACC(String bankMandiriACC) {
		this.bankMandiriACC = bankMandiriACC;
	}
	public String getOtherBankACC() {
		return otherBankACC;
	}
	public void setOtherBankACC(String otherBankACC) {
		this.otherBankACC = otherBankACC;
	}
	public String getMerchACC() {
		return merchACC;
	}
	public void setMerchACC(String merchACC) {
		this.merchACC = merchACC;
	}
	public String getTrxDate() {
		return trxDate;
	}
	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}
	public String getSettleDate() {
		return settleDate;
	}
	public void setSettleDate(String settleDate) {
		this.settleDate = settleDate;
	}
	public String getTrxCodeE() {
		return trxCodeE;
	}
	public void setTrxCodeE(String trxCodeE) {
		this.trxCodeE = trxCodeE;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public String getCrdType() {
		return crdType;
	}
	public void setCrdType(String crdType) {
		this.crdType = crdType;
	}
	public String gettId() {
		return tId;
	}
	public void settId(String tId) {
		this.tId = tId;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getPaymentBatch() {
		return paymentBatch;
	}
	public void setPaymentBatch(String paymentBatch) {
		this.paymentBatch = paymentBatch;
	}
	public String gettIdBach() {
		return tIdBach;
	}
	public void settIdBach(String tIdBach) {
		this.tIdBach = tIdBach;
	}
	public String getBatchSeq() {
		return batchSeq;
	}
	public void setBatchSeq(String batchSeq) {
		this.batchSeq = batchSeq;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getNonMdrAmount() {
		return nonMdrAmount;
	}
	public void setNonMdrAmount(String nonMdrAmount) {
		this.nonMdrAmount = nonMdrAmount;
	}
}
