package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_account_type database table.
 * 
 */
@Entity
@Table(name="fin_account_type")
public class FinAccountType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_account_type")  
	@TableGenerator(name="fin_account_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
@Column(name="account_type_id", unique=true, nullable=false)
	private Long accountTypeId;

	@Column(name="account_type_desc", nullable=false, length=100)
	private String accountTypeDesc;

	//bi-directional many-to-one association to FinAccount
	@OneToMany(mappedBy="finAccountType")
	private List<FinAccount> finAccounts;

    public FinAccountType() {
    }

	public Long getAccountTypeId() {
		return this.accountTypeId;
	}

	public void setAccountTypeId(Long accountTypeId) {
		this.accountTypeId = accountTypeId;
	}

	public String getAccountTypeDesc() {
		return this.accountTypeDesc;
	}

	public void setAccountTypeDesc(String accountTypeDesc) {
		this.accountTypeDesc = accountTypeDesc;
	}

	public List<FinAccount> getFinAccounts() {
		return this.finAccounts;
	}

	public void setFinAccounts(List<FinAccount> finAccounts) {
		this.finAccounts = finAccounts;
	}
	
}