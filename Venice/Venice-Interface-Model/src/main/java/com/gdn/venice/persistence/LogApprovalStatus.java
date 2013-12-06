package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the log_approval_status database table.
 * 
 */
@Entity
@Table(name="log_approval_status")
public class LogApprovalStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_approval_status")  
	@TableGenerator(name="log_approval_status", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="approval_status_id", unique=true, nullable=false)
	private Long approvalStatusId;

	@Column(name="approval_status_desc", nullable=false, length=100)
	private String approvalStatusDesc;

	//bi-directional many-to-one association to LogAirwayBill
	@OneToMany(mappedBy="logApprovalStatus1")
	private List<LogAirwayBill> logAirwayBills1;

	//bi-directional many-to-one association to LogAirwayBill
	@OneToMany(mappedBy="logApprovalStatus2")
	private List<LogAirwayBill> logAirwayBills2;

    public LogApprovalStatus() {
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

	public List<LogAirwayBill> getLogAirwayBills1() {
		return this.logAirwayBills1;
	}

	public void setLogAirwayBills1(List<LogAirwayBill> logAirwayBills1) {
		this.logAirwayBills1 = logAirwayBills1;
	}
	
	public List<LogAirwayBill> getLogAirwayBills2() {
		return this.logAirwayBills2;
	}

	public void setLogAirwayBills2(List<LogAirwayBill> logAirwayBills2) {
		this.logAirwayBills2 = logAirwayBills2;
	}
	
}