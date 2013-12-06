package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the log_logistics_provider database table.
 * 
 */
@Entity
@Table(name="log_logistics_provider")
public class LogLogisticsProvider implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_logistics_provider")  
	@TableGenerator(name="log_logistics_provider", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="logistics_provider_id", unique=true, nullable=false)
	private Long logisticsProviderId;

	@Column(name="logistics_provider_code", nullable=false, length=100)
	private String logisticsProviderCode;

	//bi-directional many-to-one association to LogActivityReportUpload
	@OneToMany(mappedBy="logLogisticsProvider")
	private List<LogActivityReportUpload> logActivityReportUploads;

	//bi-directional many-to-one association to LogAirwayBill
	@OneToMany(mappedBy="logLogisticsProvider")
	private List<LogAirwayBill> logAirwayBills;

	//bi-directional many-to-one association to LogInvoiceReportUpload
	@OneToMany(mappedBy="logLogisticsProvider")
	private List<LogInvoiceReportUpload> logInvoiceReportUploads;

	//bi-directional many-to-one association to LogLogisticService
	@OneToMany(mappedBy="logLogisticsProvider")
	private List<LogLogisticService> logLogisticServices;

	//bi-directional many-to-one association to LogReportTemplate
    @ManyToOne
	@JoinColumn(name="invoice_import_template_id", nullable=false)
	private LogReportTemplate logReportTemplate1;

	//bi-directional many-to-one association to LogReportTemplate
    @ManyToOne
	@JoinColumn(name="activity_import_template_id", nullable=false)
	private LogReportTemplate logReportTemplate2;

	//bi-directional many-to-one association to VenParty
    @ManyToOne
	@JoinColumn(name="party_id", nullable=false)
	private VenParty venParty;

	//bi-directional many-to-one association to LogPickupSchedule
	@OneToMany(mappedBy="logLogisticsProvider")
	private List<LogPickupSchedule> logPickupSchedules;

	//bi-directional many-to-one association to LogProviderAgreement
	@OneToMany(mappedBy="logLogisticsProvider")
	private List<LogProviderAgreement> logProviderAgreements;

	//bi-directional many-to-one association to LogProviderPickupOrderReport
	@OneToMany(mappedBy="logLogisticsProvider")
	private List<LogProviderPickupOrderReport> logProviderPickupOrderReports;

    public LogLogisticsProvider() {
    }

	public Long getLogisticsProviderId() {
		return this.logisticsProviderId;
	}

	public void setLogisticsProviderId(Long logisticsProviderId) {
		this.logisticsProviderId = logisticsProviderId;
	}

	public String getLogisticsProviderCode() {
		return this.logisticsProviderCode;
	}

	public void setLogisticsProviderCode(String logisticsProviderCode) {
		this.logisticsProviderCode = logisticsProviderCode;
	}

	public List<LogActivityReportUpload> getLogActivityReportUploads() {
		return this.logActivityReportUploads;
	}

	public void setLogActivityReportUploads(List<LogActivityReportUpload> logActivityReportUploads) {
		this.logActivityReportUploads = logActivityReportUploads;
	}
	
	public List<LogAirwayBill> getLogAirwayBills() {
		return this.logAirwayBills;
	}

	public void setLogAirwayBills(List<LogAirwayBill> logAirwayBills) {
		this.logAirwayBills = logAirwayBills;
	}
	
	public List<LogInvoiceReportUpload> getLogInvoiceReportUploads() {
		return this.logInvoiceReportUploads;
	}

	public void setLogInvoiceReportUploads(List<LogInvoiceReportUpload> logInvoiceReportUploads) {
		this.logInvoiceReportUploads = logInvoiceReportUploads;
	}
	
	public List<LogLogisticService> getLogLogisticServices() {
		return this.logLogisticServices;
	}

	public void setLogLogisticServices(List<LogLogisticService> logLogisticServices) {
		this.logLogisticServices = logLogisticServices;
	}
	
	public LogReportTemplate getLogReportTemplate1() {
		return this.logReportTemplate1;
	}

	public void setLogReportTemplate1(LogReportTemplate logReportTemplate1) {
		this.logReportTemplate1 = logReportTemplate1;
	}
	
	public LogReportTemplate getLogReportTemplate2() {
		return this.logReportTemplate2;
	}

	public void setLogReportTemplate2(LogReportTemplate logReportTemplate2) {
		this.logReportTemplate2 = logReportTemplate2;
	}
	
	public VenParty getVenParty() {
		return this.venParty;
	}

	public void setVenParty(VenParty venParty) {
		this.venParty = venParty;
	}
	
	public List<LogPickupSchedule> getLogPickupSchedules() {
		return this.logPickupSchedules;
	}

	public void setLogPickupSchedules(List<LogPickupSchedule> logPickupSchedules) {
		this.logPickupSchedules = logPickupSchedules;
	}
	
	public List<LogProviderAgreement> getLogProviderAgreements() {
		return this.logProviderAgreements;
	}

	public void setLogProviderAgreements(List<LogProviderAgreement> logProviderAgreements) {
		this.logProviderAgreements = logProviderAgreements;
	}
	
	public List<LogProviderPickupOrderReport> getLogProviderPickupOrderReports() {
		return this.logProviderPickupOrderReports;
	}

	public void setLogProviderPickupOrderReports(List<LogProviderPickupOrderReport> logProviderPickupOrderReports) {
		this.logProviderPickupOrderReports = logProviderPickupOrderReports;
	}
	
}