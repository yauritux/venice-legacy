package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the ven_party database table.
 * 
 */
@Entity
@Table(name="ven_party")
public class VenParty implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="ven_party")  
	@TableGenerator(name="ven_party", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="party_id", unique=true, nullable=false)
	private Long partyId;

	@Column(name="full_or_legal_name", length=200)
	private String fullOrLegalName;

	@Column(name="party_first_name", length=100)
	private String partyFirstName;

	@Column(name="party_last_name", length=100)
	private String partyLastName;

	@Column(name="party_middle_name", length=100)
	private String partyMiddleName;

	@Column(name="party_position", length=100)
	private String partyPosition;

	//bi-directional many-to-one association to FinApInvoice
	@OneToMany(mappedBy="venParty")
	private List<FinApInvoice> finApInvoices;

	//bi-directional many-to-one association to FinApManualJournalTransaction
	@OneToMany(mappedBy="venParty")
	private List<FinApManualJournalTransaction> finApManualJournalTransactions;

	//bi-directional many-to-one association to FinApPayment
	@OneToMany(mappedBy="venParty")
	private List<FinApPayment> finApPayments;

	//bi-directional many-to-one association to FrdFraudActionLog
	@OneToMany(mappedBy="venParty")
	private List<FrdFraudActionLog> frdFraudActionLogs;

	//bi-directional many-to-one association to KpiPartySla
	@OneToMany(mappedBy="venParty")
	private List<KpiPartySla> kpiPartySlas;

	//bi-directional many-to-one association to LogLogisticsProvider
	@OneToMany(mappedBy="venParty")
	private List<LogLogisticsProvider> logLogisticsProviders;

	//bi-directional many-to-one association to RafUser
	@OneToMany(mappedBy="venParty")
	private List<RafUser> rafUsers;

	//bi-directional many-to-one association to VenBank
	@OneToMany(mappedBy="venParty")
	private List<VenBank> venBanks;

	//bi-directional many-to-one association to VenContactDetail
	@OneToMany(mappedBy="venParty")
	private List<VenContactDetail> venContactDetails;

	//bi-directional many-to-one association to VenCustomer
	@OneToMany(mappedBy="venParty")
	private List<VenCustomer> venCustomers;

	//bi-directional many-to-one association to VenMerchant
	@OneToMany(mappedBy="venParty")
	private List<VenMerchant> venMerchants;

	//bi-directional many-to-many association to KpiMeasurementPeriod
    @ManyToMany//(fetch=FetchType.EAGER)
	@JoinTable(
		name="kpi_party_measurement_period"
		, joinColumns={
			@JoinColumn(name="party_id", nullable=false)
			}
		, inverseJoinColumns={
			@JoinColumn(name="kpi_period_id", nullable=false)
			}
		)
	private List<KpiMeasurementPeriod> kpiMeasurementPeriods;
    
    @OneToMany(mappedBy="venParty")
	private List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriod;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="parent_party_id")
	private VenParty venParty;

	//bi-directional many-to-one association to VenParty
	@OneToMany(mappedBy="venParty")
	private List<VenParty> venParties;

	//bi-directional many-to-one association to VenPartyType
    @ManyToOne
	@JoinColumn(name="party_type_id", nullable=false)
	private VenPartyType venPartyType;

	//bi-directional many-to-one association to VenPartyAddress
	@OneToMany(mappedBy="venParty")
	private List<VenPartyAddress> venPartyAddresses;

	//bi-directional many-to-one association to VenPartyPromotionShare
	@OneToMany(mappedBy="venParty")
	private List<VenPartyPromotionShare> venPartyPromotionShares;

	//bi-directional many-to-one association to VenRecipient
	@OneToMany(mappedBy="venParty")
	private List<VenRecipient> venRecipients;

    public VenParty() {
    }

	public Long getPartyId() {
		return this.partyId;
	}

	public void setPartyId(Long partyId) {
		this.partyId = partyId;
	}

	public String getFullOrLegalName() {
		return this.fullOrLegalName;
	}

	public void setFullOrLegalName(String fullOrLegalName) {
		this.fullOrLegalName = fullOrLegalName;
	}

	public String getPartyFirstName() {
		return this.partyFirstName;
	}

	public void setPartyFirstName(String partyFirstName) {
		this.partyFirstName = partyFirstName;
	}

	public String getPartyLastName() {
		return this.partyLastName;
	}

	public void setPartyLastName(String partyLastName) {
		this.partyLastName = partyLastName;
	}

	public String getPartyMiddleName() {
		return this.partyMiddleName;
	}

	public void setPartyMiddleName(String partyMiddleName) {
		this.partyMiddleName = partyMiddleName;
	}

	public String getPartyPosition() {
		return this.partyPosition;
	}

	public void setPartyPosition(String partyPosition) {
		this.partyPosition = partyPosition;
	}

	public List<FinApInvoice> getFinApInvoices() {
		return this.finApInvoices;
	}

	public void setFinApInvoices(List<FinApInvoice> finApInvoices) {
		this.finApInvoices = finApInvoices;
	}
	
	public List<FinApManualJournalTransaction> getFinApManualJournalTransactions() {
		return this.finApManualJournalTransactions;
	}

	public void setFinApManualJournalTransactions(List<FinApManualJournalTransaction> finApManualJournalTransactions) {
		this.finApManualJournalTransactions = finApManualJournalTransactions;
	}
	
	public List<FinApPayment> getFinApPayments() {
		return this.finApPayments;
	}

	public void setFinApPayments(List<FinApPayment> finApPayments) {
		this.finApPayments = finApPayments;
	}
	
	public List<FrdFraudActionLog> getFrdFraudActionLogs() {
		return this.frdFraudActionLogs;
	}

	public void setFrdFraudActionLogs(List<FrdFraudActionLog> frdFraudActionLogs) {
		this.frdFraudActionLogs = frdFraudActionLogs;
	}
	
	public List<KpiPartySla> getKpiPartySlas() {
		return this.kpiPartySlas;
	}

	public void setKpiPartySlas(List<KpiPartySla> kpiPartySlas) {
		this.kpiPartySlas = kpiPartySlas;
	}
	
	public List<LogLogisticsProvider> getLogLogisticsProviders() {
		return this.logLogisticsProviders;
	}

	public void setLogLogisticsProviders(List<LogLogisticsProvider> logLogisticsProviders) {
		this.logLogisticsProviders = logLogisticsProviders;
	}
	
	public List<RafUser> getRafUsers() {
		return this.rafUsers;
	}

	public void setRafUsers(List<RafUser> rafUsers) {
		this.rafUsers = rafUsers;
	}
	
	public List<VenBank> getVenBanks() {
		return this.venBanks;
	}

	public void setVenBanks(List<VenBank> venBanks) {
		this.venBanks = venBanks;
	}
	
	public List<VenContactDetail> getVenContactDetails() {
		return this.venContactDetails;
	}

	public void setVenContactDetails(List<VenContactDetail> venContactDetails) {
		this.venContactDetails = venContactDetails;
	}
	
	public List<VenCustomer> getVenCustomers() {
		return this.venCustomers;
	}

	public void setVenCustomers(List<VenCustomer> venCustomers) {
		this.venCustomers = venCustomers;
	}
	
	public List<VenMerchant> getVenMerchants() {
		return this.venMerchants;
	}

	public void setVenMerchants(List<VenMerchant> venMerchants) {
		this.venMerchants = venMerchants;
	}
	
	public List<KpiMeasurementPeriod> getKpiMeasurementPeriods() {
		return this.kpiMeasurementPeriods;
	}

	public void setKpiMeasurementPeriods(List<KpiMeasurementPeriod> kpiMeasurementPeriods) {
		this.kpiMeasurementPeriods = kpiMeasurementPeriods;
	}
	
	public List<KpiPartyMeasurementPeriod> getKpiPartyMeasurementPeriod() {
		return this.kpiPartyMeasurementPeriod;
	}

	public void setKpiPartyMeasurementPeriod(List<KpiPartyMeasurementPeriod> kpiPartyMeasurementPeriod) {
		this.kpiPartyMeasurementPeriod = kpiPartyMeasurementPeriod;
	}
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
	public List<VenParty> getVenParties() {
		return this.venParties;
	}

	public void setVenParties(List<VenParty> venParties) {
		this.venParties = venParties;
	}
	
	public VenPartyType getVenPartyType() {
		return this.venPartyType;
	}

	public void setVenPartyType(VenPartyType venPartyType) {
		this.venPartyType = venPartyType;
	}
	
	public List<VenPartyAddress> getVenPartyAddresses() {
		return this.venPartyAddresses;
	}

	public void setVenPartyAddresses(List<VenPartyAddress> venPartyAddresses) {
		this.venPartyAddresses = venPartyAddresses;
	}
	
	public List<VenPartyPromotionShare> getVenPartyPromotionShares() {
		return this.venPartyPromotionShares;
	}

	public void setVenPartyPromotionShares(List<VenPartyPromotionShare> venPartyPromotionShares) {
		this.venPartyPromotionShares = venPartyPromotionShares;
	}
	
	public List<VenRecipient> getVenRecipients() {
		return this.venRecipients;
	}

	public void setVenRecipients(List<VenRecipient> venRecipients) {
		this.venRecipients = venRecipients;
	}
	
}