package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the kpi_party_target database table.
 * 
 */
@Embeddable
public class KpiPartyTargetPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="party_sla_id", unique=true, nullable=false)
	private Long partySlaId;

	@Column(name="kpi_id", unique=true, nullable=false)
	private Long kpiId;

	@GeneratedValue(strategy=GenerationType.TABLE, generator="id_gen")  
	@TableGenerator(name="id_gen", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="kpi_party_target_id", unique=true, nullable=false)
	private Long kpiPartyTargetId;
	
	@Column(name="target_baseline_id", unique=true, nullable=false)
	private Long targetBaselineId;

    public KpiPartyTargetPK() {
    }
	public Long getPartySlaId() {
		return this.partySlaId;
	}
	public void setPartySlaId(Long partySlaId) {
		this.partySlaId = partySlaId;
	}
	public Long getKpiPartyTargetId() {
		return this.kpiPartyTargetId;
	}
	public void setKpiPartyTargetId(Long kpiPartyTargetId) {
		this.kpiPartyTargetId = kpiPartyTargetId;
	}
	
	public Long getKpiId() {
		return this.kpiId;
	}
	public void setKpiId(Long kpiId) {
		this.kpiId = kpiId;
	}
	
	public Long getTargetBaselineId() {
		return this.targetBaselineId;
	}
	public void setTargetBaselineId(Long targetBaselineId) {
		this.targetBaselineId = targetBaselineId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof KpiPartyTargetPK)) {
			return false;
		}
		KpiPartyTargetPK castOther = (KpiPartyTargetPK)other;
		return 
			this.partySlaId.equals(castOther.partySlaId)
			&& this.kpiId.equals(castOther.kpiId)
			&& this.targetBaselineId.equals(castOther.targetBaselineId);

    }
    
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.partySlaId.hashCode();
		hash = hash * prime + this.kpiId.hashCode();
		hash = hash * prime + this.targetBaselineId.hashCode();
		
		return hash;
    }
}