package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the kpi_party_measurement_period database table.
 * 
 */
@Embeddable
public class KpiPartyMeasurementPeriodPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="party_id", unique=true, nullable=false)
	private Long partyId;

	@Column(name="kpi_period_id", unique=true, nullable=false)
	private Long kpiPeriodId;

    public KpiPartyMeasurementPeriodPK() {
    }
	public Long getPartyId() {
		return this.partyId;
	}
	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}
	public Long getKpiPeriodId() {
		return this.kpiPeriodId;
	}
	public void setKpiPeriodId(Long kpiPeriodId) {
		this.kpiPeriodId = kpiPeriodId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof KpiPartyMeasurementPeriodPK)) {
			return false;
		}
		KpiPartyMeasurementPeriodPK castOther = (KpiPartyMeasurementPeriodPK)other;
		return 
			this.partyId.equals(castOther.partyId)
			&& this.kpiPeriodId.equals(castOther.kpiPeriodId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.partyId.hashCode();
		hash = hash * prime + this.kpiPeriodId.hashCode();
		
		return hash;
    }
}