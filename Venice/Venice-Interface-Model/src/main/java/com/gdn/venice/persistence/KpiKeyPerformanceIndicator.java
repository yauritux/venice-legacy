package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the kpi_key_performance_indicator database table.
 * 
 */
@Entity
@Table(name="kpi_key_performance_indicator")
public class KpiKeyPerformanceIndicator implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="kpi_key_performance_indicator")  
	@TableGenerator(name="kpi_key_performance_indicator", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="kpi_id", unique=true, nullable=false)
	private Long kpiId;

	@Column(name="calculation_method", nullable=false)
	private Integer calculationMethod;

	@Column(name="kpi_desc", nullable=false, length=100)
	private String kpiDesc;

	//bi-directional many-to-one association to KpiPartyPeriodActual
	@OneToMany(mappedBy="kpiKeyPerformanceIndicator")
	private List<KpiPartyPeriodActual> kpiPartyPeriodActuals;

	//bi-directional many-to-one association to KpiPartyPeriodTransaction
	@OneToMany(mappedBy="kpiKeyPerformanceIndicator")
	private List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactions;

	//bi-directional many-to-one association to KpiPartyTarget
	@OneToMany(mappedBy="kpiKeyPerformanceIndicator")
	private List<KpiPartyTarget> kpiPartyTargets;

    public KpiKeyPerformanceIndicator() {
    }

	public Long getKpiId() {
		return this.kpiId;
	}

	public void setKpiId(Long kpiId) {
		this.kpiId = kpiId;
	}

	public Integer getCalculationMethod() {
		return this.calculationMethod;
	}

	public void setCalculationMethod(Integer calculationMethod) {
		this.calculationMethod = calculationMethod;
	}

	public String getKpiDesc() {
		return this.kpiDesc;
	}

	public void setKpiDesc(String kpiDesc) {
		this.kpiDesc = kpiDesc;
	}

	public List<KpiPartyPeriodActual> getKpiPartyPeriodActuals() {
		return this.kpiPartyPeriodActuals;
	}

	public void setKpiPartyPeriodActuals(List<KpiPartyPeriodActual> kpiPartyPeriodActuals) {
		this.kpiPartyPeriodActuals = kpiPartyPeriodActuals;
	}
	
	public List<KpiPartyPeriodTransaction> getKpiPartyPeriodTransactions() {
		return this.kpiPartyPeriodTransactions;
	}

	public void setKpiPartyPeriodTransactions(List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactions) {
		this.kpiPartyPeriodTransactions = kpiPartyPeriodTransactions;
	}
	
	public List<KpiPartyTarget> getKpiPartyTargets() {
		return this.kpiPartyTargets;
	}

	public void setKpiPartyTargets(List<KpiPartyTarget> kpiPartyTargets) {
		this.kpiPartyTargets = kpiPartyTargets;
	}
	
}