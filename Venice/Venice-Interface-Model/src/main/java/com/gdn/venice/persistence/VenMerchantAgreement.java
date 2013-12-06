package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ven_merchant_agreement database table.
 * 
 */
@Entity
@Table(name="ven_merchant_agreement")
public class VenMerchantAgreement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_merchant_agreement")  
	@TableGenerator(name="ven_merchant_agreement", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="merchant_agreement_id", unique=true, nullable=false)
	private Long merchantAgreementId;

    @Temporal( TemporalType.DATE)
	@Column(name="from_date", nullable=false)
	private Date fromDate;

    @Temporal( TemporalType.DATE)
	@Column(name="to_date")
	private Date toDate;

	//bi-directional many-to-one association to KpiPartySla
    @ManyToOne
	@JoinColumn(name="party_sla_id", nullable=false)
	private KpiPartySla kpiPartySla;

	//bi-directional many-to-one association to VenMerchant
    @ManyToOne
	@JoinColumn(name="merchant_id", nullable=false)
	private VenMerchant venMerchant;

    public VenMerchantAgreement() {
    }

	public Long getMerchantAgreementId() {
		return this.merchantAgreementId;
	}

	public void setMerchantAgreementId(Long merchantAgreementId) {
		this.merchantAgreementId = merchantAgreementId;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public KpiPartySla getKpiPartySla() {
		return this.kpiPartySla;
	}

	public void setKpiPartySla(KpiPartySla kpiPartySla) {
		this.kpiPartySla = kpiPartySla;
	}
	
	public VenMerchant getVenMerchant() {
		return this.venMerchant;
	}

	public void setVenMerchant(VenMerchant venMerchant) {
		this.venMerchant = venMerchant;
	}
	
}