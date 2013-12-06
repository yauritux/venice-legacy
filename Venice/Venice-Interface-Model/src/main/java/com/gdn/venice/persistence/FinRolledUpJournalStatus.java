package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_rolled_up_journal_status database table.
 * 
 */
@Entity
@Table(name="fin_rolled_up_journal_status")
public class FinRolledUpJournalStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_rolled_up_journal_status")  
	@TableGenerator(name="fin_rolled_up_journal_status", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="journal_entry_status_id", unique=true, nullable=false)
	private Long journalEntryStatusId;

	@Column(name="journal_entry_status_desc", nullable=false, length=100)
	private String journalEntryStatusDesc;

	//bi-directional many-to-one association to FinRolledUpJournalEntry
	@OneToMany(mappedBy="finRolledUpJournalStatus")
	private List<FinRolledUpJournalEntry> finRolledUpJournalEntries;

    public FinRolledUpJournalStatus() {
    }

	public Long getJournalEntryStatusId() {
		return this.journalEntryStatusId;
	}

	public void setJournalEntryStatusId(Long journalEntryStatusId) {
		this.journalEntryStatusId = journalEntryStatusId;
	}

	public String getJournalEntryStatusDesc() {
		return this.journalEntryStatusDesc;
	}

	public void setJournalEntryStatusDesc(String journalEntryStatusDesc) {
		this.journalEntryStatusDesc = journalEntryStatusDesc;
	}

	public List<FinRolledUpJournalEntry> getFinRolledUpJournalEntries() {
		return this.finRolledUpJournalEntries;
	}

	public void setFinRolledUpJournalEntries(List<FinRolledUpJournalEntry> finRolledUpJournalEntries) {
		this.finRolledUpJournalEntries = finRolledUpJournalEntries;
	}
	
}