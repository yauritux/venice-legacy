package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the fin_rolled_up_journal_header database table.
 * 
 */
@Entity
@Table(name="fin_rolled_up_journal_header")
public class FinRolledUpJournalHeader implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_rolled_up_journal_header")  
	@TableGenerator(name="fin_rolled_up_journal_header", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="ru_journal_header_id", unique=true, nullable=false)
	private Long ruJournalHeaderId;

	@Column(name="ru_journal_filename_and_path", nullable=false, length=1000)
	private String ruJournalFilenameAndPath;

	@Column(name="ru_journal_header_desc", nullable=false, length=200)
	private String ruJournalHeaderDesc;

	@Column(name="ru_timestamp", nullable=false)
	private Timestamp ruTimestamp;

	//bi-directional many-to-one association to FinRolledUpJournalEntry
	@OneToMany(mappedBy="finRolledUpJournalHeader")
	private List<FinRolledUpJournalEntry> finRolledUpJournalEntries;

	//bi-directional many-to-one association to FinPeriod
    @ManyToOne
	@JoinColumn(name="period_id", nullable=false)
	private FinPeriod finPeriod;

	//bi-directional many-to-one association to FinRolledUpJournalType
    @ManyToOne
	@JoinColumn(name="finance_journal_type_id", nullable=false)
	private FinRolledUpJournalType finRolledUpJournalType;

    public FinRolledUpJournalHeader() {
    }

	public Long getRuJournalHeaderId() {
		return this.ruJournalHeaderId;
	}

	public void setRuJournalHeaderId(Long ruJournalHeaderId) {
		this.ruJournalHeaderId = ruJournalHeaderId;
	}

	public String getRuJournalFilenameAndPath() {
		return this.ruJournalFilenameAndPath;
	}

	public void setRuJournalFilenameAndPath(String ruJournalFilenameAndPath) {
		this.ruJournalFilenameAndPath = ruJournalFilenameAndPath;
	}

	public String getRuJournalHeaderDesc() {
		return this.ruJournalHeaderDesc;
	}

	public void setRuJournalHeaderDesc(String ruJournalHeaderDesc) {
		this.ruJournalHeaderDesc = ruJournalHeaderDesc;
	}

	public Timestamp getRuTimestamp() {
		return this.ruTimestamp;
	}

	public void setRuTimestamp(Timestamp ruTimestamp) {
		this.ruTimestamp = ruTimestamp;
	}

	public List<FinRolledUpJournalEntry> getFinRolledUpJournalEntries() {
		return this.finRolledUpJournalEntries;
	}

	public void setFinRolledUpJournalEntries(List<FinRolledUpJournalEntry> finRolledUpJournalEntries) {
		this.finRolledUpJournalEntries = finRolledUpJournalEntries;
	}
	
	public FinPeriod getFinPeriod() {
		return this.finPeriod;
	}

	public void setFinPeriod(FinPeriod finPeriod) {
		this.finPeriod = finPeriod;
	}
	
	public FinRolledUpJournalType getFinRolledUpJournalType() {
		return this.finRolledUpJournalType;
	}

	public void setFinRolledUpJournalType(FinRolledUpJournalType finRolledUpJournalType) {
		this.finRolledUpJournalType = finRolledUpJournalType;
	}
	
}