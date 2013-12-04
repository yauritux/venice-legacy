package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_bank database table.
 * 
 */
@Entity
@Table(name="ven_bank")
public class VenBank implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_bank")  
	@TableGenerator(name="ven_bank", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="bank_id", unique=true, nullable=false)
	private Long bankId;

	@Column(name="bank_code", nullable=false, length=100)
	private String bankCode;

	@Column(name="bank_short_name", nullable=false, length=100)
	private String bankShortName;

	//bi-directional many-to-one association to FinArFundsInReportType
	@OneToMany(mappedBy="venBank")
	private List<FinArFundsInReportType> finArFundsInReportTypes;

	//bi-directional many-to-one association to FinBankAccount
	@OneToMany(mappedBy="venBank")
	private List<FinBankAccount> finBankAccounts;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id", nullable=false)
	private VenParty venParty;

	//bi-directional many-to-one association to VenOrderPayment
	@OneToMany(mappedBy="venBank")
	private List<VenOrderPayment> venOrderPayments;

	//bi-directional many-to-one association to FinJournalTransaction
	@OneToMany(mappedBy="venBank")
	private List<FinJournalTransaction> finJournalTransactions;
	
    public List<FinJournalTransaction> getFinJournalTransactions() {
		return finJournalTransactions;
	}

	public void setFinJournalTransactions(
			List<FinJournalTransaction> finJournalTransactions) {
		this.finJournalTransactions = finJournalTransactions;
	}

	public VenBank() {
    }

	public Long getBankId() {
		return this.bankId;
	}

	public void setBankId(Long bankId) {
		this.bankId = bankId;
	}

	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankShortName() {
		return this.bankShortName;
	}

	public void setBankShortName(String bankShortName) {
		this.bankShortName = bankShortName;
	}

	public List<FinArFundsInReportType> getFinArFundsInReportTypes() {
		return this.finArFundsInReportTypes;
	}

	public void setFinArFundsInReportTypes(List<FinArFundsInReportType> finArFundsInReportTypes) {
		this.finArFundsInReportTypes = finArFundsInReportTypes;
	}
	
	public List<FinBankAccount> getFinBankAccounts() {
		return this.finBankAccounts;
	}

	public void setFinBankAccounts(List<FinBankAccount> finBankAccounts) {
		this.finBankAccounts = finBankAccounts;
	}
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
	public List<VenOrderPayment> getVenOrderPayments() {
		return this.venOrderPayments;
	}

	public void setVenOrderPayments(List<VenOrderPayment> venOrderPayments) {
		this.venOrderPayments = venOrderPayments;
	}
	
}