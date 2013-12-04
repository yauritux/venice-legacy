package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the kpi_measurement_period database table.
 * 
 */
@Entity
@Table(name="kpi_measurement_period")
public class KpiMeasurementPeriod implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="kpi_measurement_period")  
	@TableGenerator(name="kpi_measurement_period", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="kpi_period_id", unique=true, nullable=false)
	private Long kpiPeriodId;

	@Column(name="from_date_time", nullable=false)
	private Timestamp fromDateTime;
	
	@Column(name="to_date_time", nullable=false)
	private Timestamp toDateTime;
	
	//create new Column Description by Arifin
	@Column(name="description", nullable=false, length=100)
	private String description;
	
	//bi-directional many-to-many association to VenParty
	@ManyToMany(mappedBy="kpiMeasurementPeriods")//, fetch=FetchType.EAGER)
	private List<VenParty> venParties;

    @OneToMany(mappedBy="kpiMeasurementPeriod")
	private List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriod;
	
    public KpiMeasurementPeriod() {
    }

	public Long getKpiPeriodId() {
		return this.kpiPeriodId;
	}

	public void setKpiPeriodId(Long kpiPeriodId) {
		this.kpiPeriodId = kpiPeriodId;
	}

	public Timestamp getFromDateTime() {
		return this.fromDateTime;
	}

	public void setFromDateTime(Timestamp fromDateTime) {
		this.fromDateTime = fromDateTime;
	}

	public Timestamp getToDateTime() {
		return this.toDateTime;
	}

	public void setToDateTime(Timestamp toDateTime) {
		this.toDateTime = toDateTime;
	}
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<VenParty> getVenParties() {
		return this.venParties;
	}

	public void setVenParties(List<VenParty> venParties) {
		this.venParties = venParties;
	}
	
	public List<KpiPartyMeasurementPeriod> getKpiPartyMeasurementPeriod() {
		return this.kpiPartyMeasurementPeriod;
	}

	public void setKpiPartyMeasurementPeriod(List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriod) {
		this.kpiPartyMeasurementPeriod = kpiPartyMeasurementPeriod;
	}
	
}