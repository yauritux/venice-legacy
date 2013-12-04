package com.gdn.venice.exportimport.finance.dataimport;
import java.sql.Timestamp;

import com.gdn.venice.hssf.PojoInterface;

/**
 * The record format for a BCA VA or IB record (only incorporates the columns of interest for Venice)
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011
 * 
 */
public class BCA_VA_IB_Record implements PojoInterface{
	//The amount of the payment imported as Double
	private Double paymentAmount;
	//The date of the payment imported as java.util.Date
	private Timestamp paymentDate;
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
	public Timestamp getPaymentDate() {
		return paymentDate;
	}
	/**
	 * @param timestamp the paymentDate to set
	 */
	public void setPaymentDate(Timestamp timestamp) {
		this.paymentDate = timestamp;
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
