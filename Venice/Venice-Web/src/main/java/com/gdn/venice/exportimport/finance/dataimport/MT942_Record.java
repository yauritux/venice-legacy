package com.gdn.venice.exportimport.finance.dataimport;

import java.util.Date;

/**
 * The record format for a MT942 record (only incorporates the columns of interest for Venice)
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class MT942_Record{
	//The amount of the payment imported as Double
	private Double paymentAmount;
	
	//The bank fee (disbursed)
	private Double bankFee;
	
	//The date of the payment imported as java.util.Date
	private Date paymentDate;
	
	//The account number (possibly VA for VA or WCS order id for IB)
	private String accountNumber;
	
	/**
	 * @return the paymentAmount
	 */
	public Double getPaymentAmount() {
		return paymentAmount;
	}
	
	/**
	 * @param paymentAmount the paymentAmount to set
	 */
	public void setPaymentAmount(Double paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	
	/**
	 * @return the paymentDate
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}
	
	/**
	 * @param paymentDate the paymentDate to set
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}
	
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	/**
	 * @return the bankFee
	 */
	public Double getBankFee() {
		return bankFee;
	}
	
	/**
	 * @param bankFee the bankFee to set
	 */
	public void setBankFee(Double bankFee) {
		this.bankFee = bankFee;
	}
}
