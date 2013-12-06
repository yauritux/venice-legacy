package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the fin_bank_account database table.
 * 
 */
@Entity
@Table(name="fin_bank_account")
public class FinBankAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_bank_account")  
	@TableGenerator(name="fin_bank_account", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="account_id", unique=true, nullable=false)
	private Long accountId;

	@Column(name="bank_account_number", nullable=false, length=100)
	private String bankAccountNumber;

	//bi-directional one-to-one association to FinAccount
	@OneToOne
	@JoinColumn(name="account_id", nullable=false, insertable=false, updatable=false)
	private FinAccount finAccount;

	//bi-directional many-to-one association to VenBank
    @ManyToOne
	@JoinColumn(name="bank_id", nullable=false)
	private VenBank venBank;

    public FinBankAccount() {
    }

	public Long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public String getBankAccountNumber() {
		return this.bankAccountNumber;
	}

	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}

	public FinAccount getFinAccount() {
		return this.finAccount;
	}

	public void setFinAccount(FinAccount finAccount) {
		this.finAccount = finAccount;
	}
	
	public VenBank getVenBank() {
		return this.venBank;
	}

	public void setVenBank(VenBank venBank) {
		this.venBank = venBank;
	}
	
}