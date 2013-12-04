package com.gdn.venice.exportimport.finance.dataimport;
import com.gdn.venice.hssf.PojoInterface;

import java.util.Date;

/**
 * The record format for a Niaga IB record (only incorporates the columns of interest for Venice)
 * 
 *@author Roland
 *  
 */
public class Niaga_IB_Record implements PojoInterface{
	//The amount of the payment imported as Double
	private Double paymentAmount;
	//The date of the payment imported as java.util.Date
	private Date paymentDate;
	//The account number (possibly VA for VA or WCS order id for IB)
	private String accountNumber;
	//The amount of the bank fees (disbursed across each record in a file)
	private Double bankFee;
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
