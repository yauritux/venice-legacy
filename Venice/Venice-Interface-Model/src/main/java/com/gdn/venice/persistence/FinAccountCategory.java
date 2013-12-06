package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_account_category database table.
 * 
 */
@Entity
@Table(name="fin_account_category")
public class FinAccountCategory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_account_category")  
	@TableGenerator(name="fin_account_category", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="account_category_id", unique=true, nullable=false)
	private Long accountCategoryId;

	@Column(name="account_category_desc", nullable=false, length=100)
	private String accountCategoryDesc;

	//bi-directional many-to-one association to FinAccount
	@OneToMany(mappedBy="finAccountCategory")
	private List<FinAccount> finAccounts;

    public FinAccountCategory() {
    }

	public Long getAccountCategoryId() {
		return this.accountCategoryId;
	}

	public void setAccountCategoryId(Long accountCategoryId) {
		this.accountCategoryId = accountCategoryId;
	}

	public String getAccountCategoryDesc() {
		return this.accountCategoryDesc;
	}

	public void setAccountCategoryDesc(String accountCategoryDesc) {
		this.accountCategoryDesc = accountCategoryDesc;
	}

	public List<FinAccount> getFinAccounts() {
		return this.finAccounts;
	}

	public void setFinAccounts(List<FinAccount> finAccounts) {
		this.finAccounts = finAccounts;
	}
	
}