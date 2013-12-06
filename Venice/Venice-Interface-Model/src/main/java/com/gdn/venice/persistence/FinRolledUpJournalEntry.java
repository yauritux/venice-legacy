package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;


/**
 * The persistent class for the fin_rolled_up_journal_entry database table.
 * 
 */
@Entity
@Table(name="fin_rolled_up_journal_entry")
public class FinRolledUpJournalEntry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_rolled_up_journal_entry")  
	@TableGenerator(name="fin_rolled_up_journal_entry", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="finance_journal_entry_id", unique=true, nullable=false)
	private Long financeJournalEntryId;

	@Column(name="credit_debit_flag", nullable=false)
	private Boolean creditDebitFlag;

	@Column(name="ru_journal_entry_timestamp", nullable=false)
	private Timestamp ruJournalEntryTimestamp;

	@Column(name="ru_value", nullable=false, precision=20, scale=2)
	private BigDecimal ruValue;

	//bi-directional many-to-one association to FinJournalApprovalGroup
	@OneToMany(mappedBy="finRolledUpJournalEntry")
	private List<FinJournalApprovalGroup> finJournalApprovalGroups;

	//bi-directional many-to-one association to FinAccount
    @ManyToOne
	@JoinColumn(name="account_id", nullable=false)
	private FinAccount finAccount;

	//bi-directional many-to-one association to FinRolledUpJournalHeader
    @ManyToOne
	@JoinColumn(name="ru_journal_header_id")
	private FinRolledUpJournalHeader finRolledUpJournalHeader;

	//bi-directional many-to-one association to FinRolledUpJournalStatus
    @ManyToOne
	@JoinColumn(name="journal_entry_status_id", nullable=false)
	private FinRolledUpJournalStatus finRolledUpJournalStatus;
    
    @Column(name="group_id", nullable=true)
	private Long groupId;

    public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public FinRolledUpJournalEntry() {
    }

	public Long getFinanceJournalEntryId() {
		return this.financeJournalEntryId;
	}

	public void setFinanceJournalEntryId(Long financeJournalEntryId) {
		this.financeJournalEntryId = financeJournalEntryId;
	}

	public Boolean getCreditDebitFlag() {
		return this.creditDebitFlag;
	}

	public void setCreditDebitFlag(Boolean creditDebitFlag) {
		this.creditDebitFlag = creditDebitFlag;
	}

	public Timestamp getRuJournalEntryTimestamp() {
		return this.ruJournalEntryTimestamp;
	}

	public void setRuJournalEntryTimestamp(Timestamp ruJournalEntryTimestamp) {
		this.ruJournalEntryTimestamp = ruJournalEntryTimestamp;
	}

	public BigDecimal getRuValue() {
		return this.ruValue;
	}

	public void setRuValue(BigDecimal ruValue) {
		this.ruValue = ruValue;
	}

	public List<FinJournalApprovalGroup> getFinJournalApprovalGroups() {
		return this.finJournalApprovalGroups;
	}

	public void setFinJournalApprovalGroups(List<FinJournalApprovalGroup> finJournalApprovalGroups) {
		this.finJournalApprovalGroups = finJournalApprovalGroups;
	}
	
	public FinAccount getFinAccount() {
		return this.finAccount;
	}

	public void setFinAccount(FinAccount finAccount) {
		this.finAccount = finAccount;
	}
	
	public FinRolledUpJournalHeader getFinRolledUpJournalHeader() {
		return this.finRolledUpJournalHeader;
	}

	public void setFinRolledUpJournalHeader(FinRolledUpJournalHeader finRolledUpJournalHeader) {
		this.finRolledUpJournalHeader = finRolledUpJournalHeader;
	}
	
	public FinRolledUpJournalStatus getFinRolledUpJournalStatus() {
		return this.finRolledUpJournalStatus;
	}

	public void setFinRolledUpJournalStatus(FinRolledUpJournalStatus finRolledUpJournalStatus) {
		this.finRolledUpJournalStatus = finRolledUpJournalStatus;
	}	
}