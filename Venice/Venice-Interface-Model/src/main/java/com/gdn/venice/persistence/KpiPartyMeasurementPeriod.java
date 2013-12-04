package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the kpi_party_measurement_period database table.
 * 
 */
@Entity
@Table(name="kpi_party_measurement_period")
public class KpiPartyMeasurementPeriod implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private KpiPartyMeasurementPeriodPK id;

	//bi-directional many-to-one association to KpiPartyPeriodActual
	@OneToMany(mappedBy="kpiPartyMeasurementPeriod")
	private List<KpiPartyPeriodActual> kpiPartyPeriodActuals;

	//bi-directional many-to-one association to KpiPartyPeriodTransaction
	@OneToMany(mappedBy="kpiPartyMeasurementPeriod")
	private List<KpiPartyPeriodTransaction> kpiPartyPeriodTransactions;

	//bi-directional many-to-one association to VenParty by Arifin updated by Olive (Hibernate need this)
	//read http://stackoverflow.com/questions/2468106/hibernate-update-jpa-foreign-key for more info
	@ManyToOne
	@JoinColumn(name="kpi_period_id", nullable=false, insertable=false, updatable=false)
	private KpiMeasurementPeriod kpiMeasurementPeriod;
	
	//bi-directional many-to-one association to VenParty by Arifin updated by Olive (Hibernate need this)
	@ManyToOne
	@JoinColumn(name="party_id", nullable=false, insertable=false, updatable=false)
	private VenParty venParty;
	
    public KpiPartyMeasurementPeriod() {
    }

	public KpiPartyMeasurementPeriodPK getId() {
		return this.id;
	}

	public void setId(KpiPartyMeasurementPeriodPK id) {
		this.id = id;
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
	
	public KpiMeasurementPeriod getKpiMeasurementPeriod() {
		return this.kpiMeasurementPeriod;
	}

	public void setKpiMeasurementPeriod(KpiMeasurementPeriod kpiMeasurementPeriod) {
		this.kpiMeasurementPeriod = kpiMeasurementPeriod;
	}
	
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
}