package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the kpi_target_baseline database table.
 * 
 */
@Entity
@Table(name="kpi_target_baseline")
public class KpiTargetBaseline implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="kpi_target_baseline")  
	@TableGenerator(name="kpi_target_baseline", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="target_baseline_id", unique=true, nullable=false)
	private Long targetBaselineId;

	@Column(name="target_baseline_desc", nullable=false, length=100)
	private String targetBaselineDesc;

	//bi-directional many-to-one association to KpiPartyTarget
	@OneToMany(mappedBy="kpiTargetBaseline")
	private List<KpiPartyTarget> kpiPartyTargets;

    public KpiTargetBaseline() {
    }

	public Long getTargetBaselineId() {
		return this.targetBaselineId;
	}

	public void setTargetBaselineId(Long targetBaselineId) {
		this.targetBaselineId = targetBaselineId;
	}

	public String getTargetBaselineDesc() {
		return this.targetBaselineDesc;
	}

	public void setTargetBaselineDesc(String targetBaselineDesc) {
		this.targetBaselineDesc = targetBaselineDesc;
	}

	public List<KpiPartyTarget> getKpiPartyTargets() {
		return this.kpiPartyTargets;
	}

	public void setKpiPartyTargets(List<KpiPartyTarget> kpiPartyTargets) {
		this.kpiPartyTargets = kpiPartyTargets;
	}
	
}