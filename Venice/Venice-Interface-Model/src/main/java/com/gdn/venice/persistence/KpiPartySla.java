package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the kpi_party_sla database table.
 * 
 */
@Entity
@Table(name="kpi_party_sla")
public class KpiPartySla implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="kpi_party_sla")  
	@TableGenerator(name="kpi_party_sla", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="party_sla_id", unique=true, nullable=false)
	private Long partySlaId;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id", nullable=false)
	private VenParty venParty;

	//bi-directional many-to-one association to KpiPartyTarget
	@OneToMany(mappedBy="kpiPartySla")
	private List<KpiPartyTarget> kpiPartyTargets;

	//bi-directional many-to-one association to LogProviderAgreement
	@OneToMany(mappedBy="kpiPartySla")
	private List<LogProviderAgreement> logProviderAgreements;

	//bi-directional many-to-one association to VenMerchantAgreement
	@OneToMany(mappedBy="kpiPartySla")
	private List<VenMerchantAgreement> venMerchantAgreements;

    public KpiPartySla() {
    }

	public Long getPartySlaId() {
		return this.partySlaId;
	}

	public void setPartySlaId(Long partySlaId) {
		this.partySlaId = partySlaId;
	}

	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
	public List<KpiPartyTarget> getKpiPartyTargets() {
		return this.kpiPartyTargets;
	}

	public void setKpiPartyTargets(List<KpiPartyTarget> kpiPartyTargets) {
		this.kpiPartyTargets = kpiPartyTargets;
	}
	
	public List<LogProviderAgreement> getLogProviderAgreements() {
		return this.logProviderAgreements;
	}

	public void setLogProviderAgreements(List<LogProviderAgreement> logProviderAgreements) {
		this.logProviderAgreements = logProviderAgreements;
	}
	
	public List<VenMerchantAgreement> getVenMerchantAgreements() {
		return this.venMerchantAgreements;
	}

	public void setVenMerchantAgreements(List<VenMerchantAgreement> venMerchantAgreements) {
		this.venMerchantAgreements = venMerchantAgreements;
	}
	
}