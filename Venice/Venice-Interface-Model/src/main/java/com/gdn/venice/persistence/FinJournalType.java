package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_journal_type database table.
 * 
 */
@Entity
@Table(name="fin_journal_type")
public class FinJournalType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_journal_type")  
	@TableGenerator(name="fin_journal_type", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="journal_type_id", unique=true, nullable=false)
	private Long journalTypeId;

	@Column(name="journal_type_desc", nullable=false, length=100)
	private String journalTypeDesc;

	//bi-directional many-to-one association to FinJournal
	@OneToMany(mappedBy="finJournalType")
	private List<FinJournal> finJournals;

    public FinJournalType() {
    }

	public Long getJournalTypeId() {
		return this.journalTypeId;
	}

	public void setJournalTypeId(Long journalTypeId) {
		this.journalTypeId = journalTypeId;
	}

	public String getJournalTypeDesc() {
		return this.journalTypeDesc;
	}

	public void setJournalTypeDesc(String journalTypeDesc) {
		this.journalTypeDesc = journalTypeDesc;
	}

	public List<FinJournal> getFinJournals() {
		return this.finJournals;
	}

	public void setFinJournals(List<FinJournal> finJournals) {
		this.finJournals = finJournals;
	}
	
}