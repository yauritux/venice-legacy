package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the kpi_party_period_actual database table.
 * 
 */
@Entity
@Table(name="kpi_party_period_actual")
public class KpiPartyPeriodActual implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private KpiPartyPeriodActualPK id;	

	@Column(name="kpi_calculated_value", nullable=false)
	private Integer kpiCalculatedValue;

	//bi-directional many-to-one association to KpiKeyPerformanceIndicator updated by Olive (remove comment, Hibernate need this) 
    @ManyToOne
	@JoinColumn(name="kpi_id", nullable=false, insertable=false, updatable=false)
	private KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator;

    //change referencedColumnName by Arifin updated by Olive (remove comment, Hibernate need this) 
	//bi-directional many-to-one association to KpiPartyMeasurementPeriod
    @ManyToOne
	@JoinColumns({
		@JoinColumn(name="kpi_period_id", referencedColumnName="kpi_period_id", nullable=false, insertable=false, updatable=false),
		@JoinColumn(name="party_id", referencedColumnName="party_id", nullable=false, insertable=false, updatable=false)
		})
	private KpiPartyMeasurementPeriod kpiPartyMeasurementPeriod;

    public KpiPartyPeriodActual() {
    }

	public KpiPartyPeriodActualPK getId() {
		return this.id;
	}

	public void setId(KpiPartyPeriodActualPK id) {
		this.id = id;
	}
	
	public Integer getKpiCalculatedValue() {
		return this.kpiCalculatedValue;
	}

	public void setKpiCalculatedValue(Integer kpiCalculatedValue) {
		this.kpiCalculatedValue = kpiCalculatedValue;
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