package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_account database table.
 * 
 */
@Entity
@Table(name="fin_account")
public class FinAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_account")  
	@TableGenerator(name="fin_account", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="account_id", unique=true, nullable=false)
	private Long accountId;

	@Column(name="account_desc", nullable=false, length=100)
	private String accountDesc;

	@Column(name="account_number", nullable=false, length=100)
	private String accountNumber;

	@Column(name="summary_account", nullable=false)
	private Boolean summaryAccount;

	//bi-directional many-to-one association to FinAccountCategory
    @ManyToOne
	@JoinColumn(name="account_category_id", nullable=false)
	private FinAccountCategory finAccountCategory;

	//bi-directional many-to-one association to FinAccountType
    @ManyToOne
	@JoinColumn(name="account_type_id", nullable=false)
	private FinAccountType finAccountType;

	//bi-directional many-to-one association to FinApPayment
	@OneToMany(mappedBy="finAccount")
	private List<FinApPayment> finApPayments;

	//bi-directional one-to-one association to FinBankAccount
	@OneToOne(mappedBy="finAccount")
	private FinBankAccount finBankAccount;

	//bi-directional many-to-one association to FinJournalTransaction
	@OneToMany(mappedBy="finAccount")
	private List<FinJournalTransaction> finJournalTransactions;

	//bi-directional many-to-one association to FinRolledUpJournalEntry
	@OneToMany(mappedBy="finAccount")
	private List<FinRolledUpJournalEntry> finRolledUpJournalEntries;

    public FinAccount() {
    }

	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getAccountDesc() {
		return this.accountDesc;
	}

	public void setAccountDesc(String accountDesc) {
		this.accountDesc = accountDesc;
	}

	public String getAccountNumber() {
		return this.accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Boolean getSummaryAccount() {
		return this.summaryAccount;
	}

	public void setSummaryAccount(Boolean summaryAccount) {
		this.summaryAccount = summaryAccount;
	}

	public FinAccountCategory getFinAccountCategory() {
		return this.finAccountCategory;
	}

	public void setFinAccountCategory(FinAccountCategory finAccountCategory) {
		this.finAccountCategory = finAccountCategory;
	}
	
	public FinAccountType getFinAccountType() {
		return this.finAccountType;
	}

	public void setFinAccountType(FinAccountType finAccountType) {
		this.finAccountType = finAccountType;
	}
	
	public List<FinApPayment> getFinApPayments() {
		return this.finApPayments;
	}

	public void setFinApPayments(List<FinApPayment> finApPayments) {
		this.finApPayments = finApPayments;
	}
	
	public FinBankAccount getFinBankAccount() {
		return this.finBankAccount;
	}

	public void setFinBankAccount(FinBankAccount finBankAccount) {
		this.finBankAccount = finBankAccount;
	}
	
	public List<FinJournalTransaction> getFinJournalTransactions() {
		return this.finJournalTransactions;
	}

	public void setFinJournalTransactions(List<FinJournalTransaction> finJournalTransactions) {
		this.finJournalTransactions = finJournalTransactions;
	}
	
	public List<FinRolledUpJournalEntry> getFinRolledUpJournalEntries() {
		return this.finRolledUpJournalEntries;
	}

	public void setFinRolledUpJournalEntries(List<FinRolledUpJournalEntry> finRolledUpJournalEntries) {
		this.finRolledUpJournalEntries = finRolledUpJournalEntries;
	}
	
}