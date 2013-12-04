package com.gdn.venice.facade.finance.journal;

import java.io.Serializable;

public class ManualJournalEntryDTO implements Serializable {
	private static final long serialVersionUID = 1L; 
	
	private Long finAccountId;//A valid accoun identifier
	private Double amount;//The maount of the transaction
	private Boolean creditOrDebit;//A flag signifying credit or debit
	private String wcsOrderId;//A valid WCS order id (not required)
	private Long venPartyId;//A valid party id (not required) 
	private String comments;//Comments for the transaction
	/**
	 * @return the finAccountId
	 */
	public Long getFinAccountId() {
		return finAccountId;
	}
	/**
	 * @param finAccountId the finAccountId to set
	 */
	public void setFinAccountId(Long finAccountId) {
		this.finAccountId = finAccountId;
	}
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * @return the creditOrDebit
	 */
	public Boolean getCreditOrDebit() {
		return creditOrDebit;
	}
	/**
	 * @param creditOrDebit the creditOrDebit to set
	 */
	public void setCreditOrDebit(Boolean creditOrDebit) {
		this.creditOrDebit = creditOrDebit;
	}
	/**
	 * @return the wcsOrderId
	 */
	public String getWcsOrderId() {
		return wcsOrderId;
	}
	/**
	 * @param wcsOrderId the wcsOrderId to set
	 */
	public void setWcsOrderId(String wcsOrderId) {
		this.wcsOrderId = wcsOrderId;
	}
	/**
	 * @return the venPartyId
	 */
	public Long getVenPartyId() {
		return venPartyId;
	}
	/**
	 * @param venPartyId the venPartyId to set
	 */
	public void setVenPartyId(Long venPartyId) {
		this.venPartyId = venPartyId;
	}
	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
