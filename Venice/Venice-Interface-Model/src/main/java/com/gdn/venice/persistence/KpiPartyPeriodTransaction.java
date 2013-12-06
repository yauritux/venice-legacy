package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;


/**
 * The persistent class for the kpi_party_period_transaction database table.
 * 
 */
@Entity
@Table(name="kpi_party_period_transaction")
public class KpiPartyPeriodTransaction implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="kpi_party_period_transaction")  
	@TableGenerator(name="kpi_party_period_transaction", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="kpi_party_period_transaction_id", unique=true, nullable=false)
	private Long kpiPartyPeriodTransactionId;

	@Column(name="kpi_transaction_reason", nullable=false, length=1000)
	private String kpiTransactionReason;

	@Column(name="kpi_transaction_value", nullable=false)
	private Integer kpiTransactionValue;

	@Column(name="transaction_timestamp", nullable=false)
	private Timestamp transactionTimestamp;

	//bi-directional many-to-one association to KpiKeyPerformanceIndicator
    @ManyToOne
	@JoinColumn(name="kpi_id", nullable=false)
	private KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator;

    //change referencedColumnName by Arifin
	//bi-directional many-to-one association to KpiPartyMeasurementPeriod
    @ManyToOne
	@JoinColumns({
		@JoinColumn(name="kpi_period_id", referencedColumnName="kpi_period_id", nullable=false),
		@JoinColumn(name="party_id", referencedColumnName="party_id", nullable=false)
		})
	private KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod;

    public KpiPartyPeriodTransaction() {
    }

	public Long getKpiPartyPeriodTransactionId() {
		return this.kpiPartyPeriodTransactionId;
	}

	public void setKpiPartyPeriodTransactionId(Long kpiPartyPeriodTransactionId) {
		this.kpiPartyPeriodTransactionId = kpiPartyPeriodTransactionId;
	}

	public String getKpiTransactionReason() {
		return this.kpiTransactionReason;
	}

	public void setKpiTransactionReason(String kpiTransactionReason) {
		this.kpiTransactionReason = kpiTransactionReason;
	}

	public Integer getKpiTransactionValue() {
		return this.kpiTransactionValue;
	}

	public void setKpiTransactionValue(Integer kpiTransactionValue) {
		this.kpiTransactionValue = kpiTransactionValue;
	}

	public Timestamp getTransactionTimestamp() {
		return this.transactionTimestamp;
	}

	public void setTransactionTimestamp(Timestamp transactionTimestamp) {
		this.transactionTimestamp = transactionTimestamp;
	}

	public KpiKeyPerformanceIndicator getKpiKeyPerformanceIndicator() {
		return this.kpiKeyPerformanceIndicator;
	}

	public void setKpiKeyPerformanceIndicator(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator) {
		this.kpiKeyPerformanceIndicator = kpiKeyPerformanceIndicator;
	}
	
	public KpiPartyMeasurementPeriod getKpiPartyMeasurementPeriod() {
		return this.kpiPartyMeasurementPeriod;
	}

	public void setKpiPartyMeasurementPeriod(KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod) {
		this.kpiPartyMeasurementPeriod = kpiPartyMeasurementPeriod;
	}
	
}