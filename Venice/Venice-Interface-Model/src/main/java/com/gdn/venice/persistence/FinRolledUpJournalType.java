package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_rolled_up_journal_type database table.
 * 
 */
@Entity
@Table(name="fin_rolled_up_journal_type")
public class FinRolledUpJournalType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_rolled_up_journal_type")  
	@TableGenerator(name="fin_rolled_up_journal_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="finance_journal_type_id", unique=true, nullable=false)
	private Long financeJournalTypeId;

	@Column(name="finance_journal_type_desc", nullable=false, length=100)
	private String financeJournalTypeDesc;

	//bi-directional many-to-one association to FinRolledUpJournalHeader
	@OneToMany(mappedBy="finRolledUpJournalType")
	private List<FinRolledUpJournalHeader> finRolledUpJournalHeaders;

    public FinRolledUpJournalType() {
    }

	public Long getFinanceJournalTypeId() {
		return this.financeJournalTypeId;
	}

	public void setFinanceJournalTypeId(Long financeJournalTypeId) {
		this.financeJournalTypeId = financeJournalTypeId;
	}

	public String getFinanceJournalTypeDesc() {
		return this.financeJournalTypeDesc;
	}

	public void setFinanceJournalTypeDesc(String financeJournalTypeDesc) {
		this.financeJournalTypeDesc = financeJournalTypeDesc;
	}

	public List<FinRolledUpJournalHeader> getFinRolledUpJournalHeaders() {
		return this.finRolledUpJournalHeaders;
	}

	public void setFinRolledUpJournalHeaders(List<FinRolledUpJournalHeader> finRolledUpJournalHeaders) {
		this.finRolledUpJournalHeaders = finRolledUpJournalHeaders;
	}
	
}