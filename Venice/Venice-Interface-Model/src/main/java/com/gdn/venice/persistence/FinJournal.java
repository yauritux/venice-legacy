package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_journal database table.
 * 
 */
@Entity
@Table(name="fin_journal")
public class FinJournal implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_journal")  
	@TableGenerator(name="fin_journal", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="journal_id", unique=true, nullable=false)
	private Long journalId;

	@Column(name="journal_desc", nullable=false, length=100)
	private String journalDesc;

	//bi-directional many-to-one association to FinJournalType
    @ManyToOne
	@JoinColumn(name="journal_type_id", nullable=false)
	private FinJournalType finJournalType;

	//bi-directional many-to-one association to FinJournalApprovalGroup
	@OneToMany(mappedBy="finJournal")
	private List<FinJournalApprovalGroup> finJournalApprovalGroups;

	//bi-directional many-to-one association to FinJournalTransaction
	@OneToMany(mappedBy="finJournal")
	private List<FinJournalTransaction> finJournalTransactions;

    public FinJournal() {
    }

	public Long getJournalId() {
		return this.journalId;
	}

	public void setJournalId(Long journalId) {
		this.journalId = journalId;
	}

	public String getJournalDesc() {
		return this.journalDesc;
	}

	public void setJournalDesc(String journalDesc) {
		this.journalDesc = journalDesc;
	}

	public FinJournalType getFinJournalType() {
		return this.finJournalType;
	}

	public void setFinJournalType(FinJournalType finJournalType) {
		this.finJournalType = finJournalType;
	}
	
	public List<FinJournalApprovalGroup> getFinJournalApprovalGroups() {
		return this.finJournalApprovalGroups;
	}

	public void setFinJournalApprovalGroups(List<FinJournalApprovalGroup> finJournalApprovalGroups) {
		this.finJournalApprovalGroups = finJournalApprovalGroups;
	}
	
	public List<FinJournalTransaction> getFinJournalTransactions() {
		return this.finJournalTransactions;
	}

	public void setFinJournalTransactions(List<FinJournalTransaction> finJournalTransactions) {
		this.finJournalTransactions = finJournalTransactions;
	}
	
}