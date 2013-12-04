package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the fin_approval_status database table.
 * 
 */
@Entity
@Table(name="fin_approval_status")
public class FinApprovalStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="fin_approval_status")  
	@TableGenerator(name="fin_approval_status", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="approval_status_id", unique=true, nullable=false)
	private Long approvalStatusId;

	@Column(name="approval_status_desc", nullable=false, length=100)
	private String approvalStatusDesc;

	//bi-directional many-to-one association to FinApPayment
	@OneToMany(mappedBy="finApprovalStatus")
	private List<FinApPayment> finApPayments;

	//bi-directional many-to-one association to FinArFundsInReconRecord
	@OneToMany(mappedBy="finApprovalStatus")
	private List<FinArFundsInReconRecord> finArFundsInReconRecords;

	//bi-directional many-to-one association to FinJournalApprovalGroup
	@OneToMany(mappedBy="finApprovalStatus")
	private List<FinJournalApprovalGroup> finJournalApprovalGroups;

	//bi-directional many-to-one association to FinSalesRecord
	@OneToMany(mappedBy="finApprovalStatus")
	private List<FinSalesRecord> finSalesRecords;

    public FinApprovalStatus() {
    }

	public Long getApprovalStatusId() {
		return this.approvalStatusId;
	}

	public void setApprovalStatusId(Long approvalStatusId) {
		this.approvalStatusId = approvalStatusId;
	}

	public String getApprovalStatusDesc() {
		return this.approvalStatusDesc;
	}

	public void setApprovalStatusDesc(String approvalStatusDesc) {
		this.approvalStatusDesc = approvalStatusDesc;
	}

	public List<FinApPayment> getFinApPayments() {
		return this.finApPayments;
	}

	public void setFinApPayments(List<FinApPayment> finApPayments) {
		this.finApPayments = finApPayments;
	}
	
	public List<FinArFundsInReconRecord> getFinArFundsInReconRecords() {
		return this.finArFundsInReconRecords;
	}

	public void setFinArFundsInReconRecords(List<FinArFundsInReconRecord> finArFundsInReconRecords) {
		this.finArFundsInReconRecords = finArFundsInReconRecords;
	}
	
	public List<FinJournalApprovalGroup> getFinJournalApprovalGroups() {
		return this.finJournalApprovalGroups;
	}

	public void setFinJournalApprovalGroups(List<FinJournalApprovalGroup> finJournalApprovalGroups) {
		this.finJournalApprovalGroups = finJournalApprovalGroups;
	}
	
	public List<FinSalesRecord> getFinSalesRecords() {
		return this.finSalesRecords;
	}

	public void setFinSalesRecords(List<FinSalesRecord> finSalesRecords) {
		this.finSalesRecords = finSalesRecords;
	}
	
}