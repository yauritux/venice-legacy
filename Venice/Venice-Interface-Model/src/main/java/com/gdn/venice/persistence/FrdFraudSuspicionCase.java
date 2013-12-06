package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the frd_fraud_suspicion_case database table.
 * 
 */
@Entity
@Table(name="frd_fraud_suspicion_case")
public class FrdFraudSuspicionCase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="frd_fraud_suspicion_case")  
	@TableGenerator(name="frd_fraud_suspicion_case", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="fraud_suspicion_case_id", unique=true, nullable=false)
	private Long fraudSuspicionCaseId;

	@Column(name="fraud_case_date_time", nullable=false)
	private Timestamp fraudCaseDateTime;

	@Column(name="fraud_case_desc", nullable=false, length=1000)
	private String fraudCaseDesc;

	@Column(name="fraud_suspicion_notes", nullable=false, length=1000)
	private String fraudSuspicionNotes;

	@Column(name="fraud_total_points", nullable=false)
	private Integer fraudTotalPoints;

	@Column(name="ilog_fraud_status", nullable=false, length=100)
	private String ilogFraudStatus;

	@Column(name="suspicion_reason", nullable=false, length=1000)
	private String suspicionReason;

	//bi-directional many-to-one association to FrdFraudActionLog
	@OneToMany(mappedBy="frdFraudSuspicionCase")
	private List<FrdFraudActionLog> frdFraudActionLogs;

	//bi-directional many-to-one association to FrdFraudCaseHistory
	@OneToMany(mappedBy="frdFraudSuspicionCase")
	private List<FrdFraudCaseHistory> frdFraudCaseHistories;

	//bi-directional many-to-one association to FrdFraudFileAttachment
	@OneToMany(mappedBy="frdFraudSuspicionCase")
	private List<FrdFraudFileAttachment> frdFraudFileAttachments;

	//bi-directional many-to-one association to FrdFraudCaseStatus
    @ManyToOne
	@JoinColumn(name="fraud_case_status_id", nullable=false)
	private FrdFraudCaseStatus frdFraudCaseStatus;

	//bi-directional many-to-many association to FrdFraudSuspicionCase
    @ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(
		name="frd_related_fraud_case"
		, joinColumns={
			@JoinColumn(name="fraud_suspicion_case_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="related_fraud_suspicion_case_id", nullable=false)
			}
		)
	private List<FrdFraudSuspicionCase> frdFraudSuspicionCases1;

	//bi-directional many-to-many association to FrdFraudSuspicionCase
	@ManyToMany(mappedBy="frdFraudSuspicionCases1")//, fetch=FetchType.EAGER)
	private List<FrdFraudSuspicionCase> frdFraudSuspicionCases2;

	//bi-directional many-to-many association to FrdFraudSuspicionCase
    @ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(
		name="frd_related_fraud_case"
		, joinColumns={
			@JoinColumn(name="related_fraud_suspicion_case_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="fraud_suspicion_case_id", nullable=false)
			}
		)
	private List<FrdFraudSuspicionCase> frdFraudSuspicionCases3;

	//bi-directional many-to-many association to FrdFraudSuspicionCase
	@ManyToMany(mappedBy="frdFraudSuspicionCases3")//, fetch=FetchType.EAGER)
	private List<FrdFraudSuspicionCase> frdFraudSuspicionCases4;

	//bi-directional many-to-one association to VenOrder
    @ManyToOne
	@JoinColumn(name="order_id", nullable=false)
	private VenOrder venOrder;

	//bi-directional many-to-one association to FrdFraudSuspicionPoint
	@OneToMany(mappedBy="frdFraudSuspicionCase")
	private List<FrdFraudSuspicionPoint> frdFraudSuspicionPoints;

	//bi-directional many-to-many association to VenOrder
	@ManyToMany(mappedBy="frdFraudSuspicionCases2")//, fetch=FetchType.EAGER)
	private List<VenOrder> venOrders;

	//bi-directional many-to-many association to VenOrderPayment
	@ManyToMany(mappedBy="frdFraudSuspicionCases")//, fetch=FetchType.EAGER)
	private List<VenOrderPayment> venOrderPayments;

    public FrdFraudSuspicionCase() {
    }

	public Long getFraudSuspicionCaseId() {
		return this.fraudSuspicionCaseId;
	}

	public void setFraudSuspicionCaseId(Long fraudSuspicionCaseId) {
		this.fraudSuspicionCaseId = fraudSuspicionCaseId;
	}

	public Timestamp getFraudCaseDateTime() {
		return this.fraudCaseDateTime;
	}

	public void setFraudCaseDateTime(Timestamp fraudCaseDateTime) {
		this.fraudCaseDateTime = fraudCaseDateTime;
	}

	public String getFraudCaseDesc() {
		return this.fraudCaseDesc;
	}

	public void setFraudCaseDesc(String fraudCaseDesc) {
		this.fraudCaseDesc = fraudCaseDesc;
	}

	public String getFraudSuspicionNotes() {
		return this.fraudSuspicionNotes;
	}

	public void setFraudSuspicionNotes(String fraudSuspicionNotes) {
		this.fraudSuspicionNotes = fraudSuspicionNotes;
	}

	public Integer getFraudTotalPoints() {
		return this.fraudTotalPoints;
	}

	public void setFraudTotalPoints(Integer fraudTotalPoints) {
		this.fraudTotalPoints = fraudTotalPoints;
	}

	public String getIlogFraudStatus() {
		return this.ilogFraudStatus;
	}

	public void setIlogFraudStatus(String ilogFraudStatus) {
		this.ilogFraudStatus = ilogFraudStatus;
	}

	public String getSuspicionReason() {
		return this.suspicionReason;
	}

	public void setSuspicionReason(String suspicionReason) {
		this.suspicionReason = suspicionReason;
	}

	public List<FrdFraudActionLog> getFrdFraudActionLogs() {
		return this.frdFraudActionLogs;
	}

	public void setFrdFraudActionLogs(List<FrdFraudActionLog> frdFraudActionLogs) {
		this.frdFraudActionLogs = frdFraudActionLogs;
	}
	
	public List<FrdFraudCaseHistory> getFrdFraudCaseHistories() {
		return this.frdFraudCaseHistories;
	}

	public void setFrdFraudCaseHistories(List<FrdFraudCaseHistory> frdFraudCaseHistories) {
		this.frdFraudCaseHistories = frdFraudCaseHistories;
	}
	
	public List<FrdFraudFileAttachment> getFrdFraudFileAttachments() {
		return this.frdFraudFileAttachments;
	}

	public void setFrdFraudFileAttachments(List<FrdFraudFileAttachment> frdFraudFileAttachments) {
		this.frdFraudFileAttachments = frdFraudFileAttachments;
	}
	
	public FrdFraudCaseStatus getFrdFraudCaseStatus() {
		return this.frdFraudCaseStatus;
	}

	public void setFrdFraudCaseStatus(FrdFraudCaseStatus frdFraudCaseStatus) {
		this.frdFraudCaseStatus = frdFraudCaseStatus;
	}
	
	public List<FrdFraudSuspicionCase> getFrdFraudSuspicionCases1() {
		return this.frdFraudSuspicionCases1;
	}

	public void setFrdFraudSuspicionCases1(List<FrdFraudSuspicionCase> frdFraudSuspicionCases1) {
		this.frdFraudSuspicionCases1 = frdFraudSuspicionCases1;
	}
	
	public List<FrdFraudSuspicionCase> getFrdFraudSuspicionCases2() {
		return this.frdFraudSuspicionCases2;
	}

	public void setFrdFraudSuspicionCases2(List<FrdFraudSuspicionCase> frdFraudSuspicionCases2) {
		this.frdFraudSuspicionCases2 = frdFraudSuspicionCases2;
	}
	
	public List<FrdFraudSuspicionCase> getFrdFraudSuspicionCases3() {
		return this.frdFraudSuspicionCases3;
	}

	public void setFrdFraudSuspicionCases3(List<FrdFraudSuspicionCase> frdFraudSuspicionCases3) {
		this.frdFraudSuspicionCases3 = frdFraudSuspicionCases3;
	}
	
	public List<FrdFraudSuspicionCase> getFrdFraudSuspicionCases4() {
		return this.frdFraudSuspicionCases4;
	}

	public void setFrdFraudSuspicionCases4(List<FrdFraudSuspicionCase> frdFraudSuspicionCases4) {
		this.frdFraudSuspicionCases4 = frdFraudSuspicionCases4;
	}
	
	public VenOrder getVenOrder() {
		return this.venOrder;
	}

	public void setVenOrder(VenOrder venOrder) {
		this.venOrder = venOrder;
	}
	
	public List<FrdFraudSuspicionPoint> getFrdFraudSuspicionPoints() {
		return this.frdFraudSuspicionPoints;
	}

	public void setFrdFraudSuspicionPoints(List<FrdFraudSuspicionPoint> frdFraudSuspicionPoints) {
		this.frdFraudSuspicionPoints = frdFraudSuspicionPoints;
	}
	
	public List<VenOrder> getVenOrders() {
		return this.venOrders;
	}

	public void setVenOrders(List<VenOrder> venOrders) {
		this.venOrders = venOrders;
	}
	
	public List<VenOrderPayment> getVenOrderPayments() {
		return this.venOrderPayments;
	}

	public void setVenOrderPayments(List<VenOrderPayment> venOrderPayments) {
		this.venOrderPayments = venOrderPayments;
	}
	
}