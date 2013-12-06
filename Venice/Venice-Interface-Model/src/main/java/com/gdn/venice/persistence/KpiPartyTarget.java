package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the kpi_party_target database table.
 * 
 */
@Entity
@Table(name="kpi_party_target")
public class KpiPartyTarget implements Serializable {
	private static final long serialVersionUID = 1L;

	//@EmbeddedId
	//private KpiPartyTargetPK id;
	
	//add new methode to create Id for KpiPartyTarget table by arifin
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="kpi_party_target")  
	@TableGenerator(name="kpi_party_target", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="kpi_party_target_id", unique=true, nullable=false)
	private Long kpiPartyTargetId;

	@Column(name="kpi_target_value", nullable=false)
	private Integer kpiTargetValue;

	//add new methode to create relation by arifin
	//bi-directional many-to-one association to KpiKeyPerformanceIndicator
    @ManyToOne
	@JoinColumn(name="kpi_id", nullable=false)//, insertable=false, updatable=false)
	private KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator;

    //add new methode to create relation by arifin
	//bi-directional many-to-one association to KpiPartySla
    @ManyToOne
	@JoinColumn(name="party_sla_id", nullable=false)//, insertable=false, updatable=false)
	private KpiPartySla kpiPartySla;
 
    //add new methode to create relation by arifin
	//bi-directional many-to-one association to KpiTargetBaseline
    @ManyToOne
	@JoinColumn(name="target_baseline_id", nullable=false)//, insertable=false, updatable=false)
	private KpiTargetBaseline kpiTargetBaseline;

    public KpiPartyTarget() {
    }

	public Long getKpiPartyTargetId() {
		return this.kpiPartyTargetId;
	}
	public void setKpiPartyTargetId(Long kpiPartyTargetId) {
		this.kpiPartyTargetId = kpiPartyTargetId;
	}
	
	public Integer getKpiTargetValue() {
		return this.kpiTargetValue;
	}

	public void setKpiTargetValue(Integer kpiTargetValue) {
		this.kpiTargetValue = kpiTargetValue;
	}

	public KpiKeyPerformanceIndicator getKpiKeyPerformanceIndicator() {
		return this.kpiKeyPerformanceIndicator;
	}

	public void setKpiKeyPerformanceIndicator(KpiKeyPerformanceIndicator kpiKeyPerformanceIndicator) {
		this.kpiKeyPerformanceIndicator = kpiKeyPerformanceIndicator;
	}
	
	public KpiPartySla getKpiPartySla() {
		return this.kpiPartySla;
	}

	public void setKpiPartySla(KpiPartySla kpiPartySla) {
		this.kpiPartySla = kpiPartySla;
	}
	
	public KpiTargetBaseline getKpiTargetBaseline() {
		return this.kpiTargetBaseline;
	}

	public void setKpiTargetBaseline(KpiTargetBaseline kpiTargetBaseline) {
		this.kpiTargetBaseline = kpiTargetBaseline;
	}
	
}