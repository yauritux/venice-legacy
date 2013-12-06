package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the fin_journal_approval_group database table.
 * 
 */
@Entity
@Table(name="fin_journal_approval_group")
public class FinJournalApprovalGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_journal_approval_group")  
	@TableGenerator(name="fin_journal_approval_group", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="journal_group_id", unique=true, nullable=false)
	private Long journalGroupId;

	@Column(name="journal_group_desc", nullable=false, length=100)
	private String journalGroupDesc;

	@Column(name="journal_group_timestamp", nullable=false)
	private Timestamp journalGroupTimestamp;

	//bi-directional many-to-one association to FinApprovalStatus
    @ManyToOne
	@JoinColumn(name="approval_status_id", nullable=false)
	private FinApprovalStatus finApprovalStatus;

	//bi-directional many-to-one association to FinJournal
    @ManyToOne
	@JoinColumn(name="journal_id", nullable=false)
	private FinJournal finJournal;

	//bi-directional many-to-one association to FinRolledUpJournalEntry
    @ManyToOne
	@JoinColumn(name="finance_journal_entry_id")
	private FinRolledUpJournalEntry finRolledUpJournalEntry;

	//bi-directional many-to-one association to FinJournalTransaction
	@OneToMany(mappedBy="finJournalApprovalGroup")
	private List<FinJournalTransaction> finJournalTransactions;

    public FinJournalApprovalGroup() {
    }

	public Long getJournalGroupId() {
		return this.journalGroupId;
	}

	public void setJournalGroupId(Long journalGroupId) {
		this.journalGroupId = journalGroupId;
	}

	public String getJournalGroupDesc() {
		return this.journalGroupDesc;
	}

	public void setJournalGroupDesc(String journalGroupDesc) {
		this.journalGroupDesc = journalGroupDesc;
	}

	public Timestamp getJournalGroupTimestamp() {
		return this.journalGroupTimestamp;
	}

	public void setJournalGroupTimestamp(Timestamp journalGroupTimestamp) {
		this.journalGroupTimestamp = journalGroupTimestamp;
	}

	public FinApprovalStatus getFinApprovalStatus() {
		return this.finApprovalStatus;
	}

	public void setFinApprovalStatus(FinApprovalStatus finApprovalStatus) {
		this.finApprovalStatus = finApprovalStatus;
	}
	
	public FinJournal getFinJournal() {
		return this.finJournal;
	}

	public void setFinJournal(FinJournal finJournal) {
		this.finJournal = finJournal;
	}
	
	public FinRolledUpJournalEntry getFinRolledUpJournalEntry() {
		return this.finRolledUpJournalEntry;
	}

	public void setFinRolledUpJournalEntry(FinRolledUpJournalEntry finRolledUpJournalEntry) {
		this.finRolledUpJournalEntry = finRolledUpJournalEntry;
	}
	
	public List<FinJournalTransaction> getFinJournalTransactions() {
		return this.finJournalTransactions;
	}

	public void setFinJournalTransactions(List<FinJournalTransaction> finJournalTransactions) {
		this.finJournalTransactions = finJournalTransactions;
	}
	
}