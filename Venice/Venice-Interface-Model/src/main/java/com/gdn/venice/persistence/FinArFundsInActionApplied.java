package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the fin_ar_funds_in_action_applied database table.
 * 
 */
@Entity
@Table(name="fin_ar_funds_in_action_applied")
public class FinArFundsInActionApplied implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_ar_funds_in_action_applied")  
	@TableGenerator(name="fin_ar_funds_in_action_applied", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="action_applied_id", unique=true, nullable=false)
	private Long actionAppliedId;

	@Column(name="action_applied_desc", nullable=false, length=100)
	private String actionAppliedDesc;

	//bi-directional many-to-one association to FinArFundsInReconRecord
	@OneToMany(mappedBy="finArFundsInActionApplied")
	private List<FinArFundsInReconRecord> finArFundsInReconRecords;
	
	//bi-directional many-to-one association to FinArFundsInActionAppliedHistory
	@OneToMany(mappedBy="finArFundsInActionApplied")
	private List<FinArFundsInActionAppliedHistory> finArFundsInActionAppliedHistory;

   	public FinArFundsInActionApplied() {
    }

	public Long getActionAppliedId() {
		return this.actionAppliedId;
	}

	public void setActionAppliedId(Long actionAppliedId) {
		this.actionAppliedId = actionAppliedId;
	}

	public String getActionAppliedDesc() {
		return this.actionAppliedDesc;
	}

	public void setActionAppliedDesc(String actionAppliedDesc) {
		this.actionAppliedDesc = actionAppliedDesc;
	}

	public List<FinArFundsInReconRecord> getFinArFundsInReconRecords() {
		return this.finArFundsInReconRecords;
	}

	public void setFinArFundsInReconRecords(List<FinArFundsInReconRecord> finArFundsInReconRecords) {
		this.finArFundsInReconRecords = finArFundsInReconRecords;
	}
	
	 public List<FinArFundsInActionAppliedHistory> getFinArFundsInActionAppliedHistory() {
			return finArFundsInActionAppliedHistory;
		}

	public void setFinArFundsInActionAppliedHistory(
			List<FinArFundsInActionAppliedHistory> finArFundsInActionAppliedHistory) {
		this.finArFundsInActionAppliedHistory = finArFundsInActionAppliedHistory;
	}
	
}