package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the log_report_template database table.
 * 
 */
@Entity
@Table(name="log_report_template")
public class LogReportTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_report_template")  
	@TableGenerator(name="log_report_template", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="template_id", unique=true, nullable=false)
	private Long templateId;

	@Column(name="template_desc", nullable=false, length=100)
	private String templateDesc;
	
	@Column(name="template_file", nullable=false, length=100)
	private String templateFile;

	//bi-directional many-to-one association to LogActivityReportUpload
	@OneToMany(mappedBy="logReportTemplate")
	private List<LogActivityReportUpload> logActivityReportUploads;

	//bi-directional many-to-one association to LogInvoiceReportUpload
	@OneToMany(mappedBy="logReportTemplate")
	private List<LogInvoiceReportUpload> logInvoiceReportUploads;

	//bi-directional many-to-one association to LogLogisticsProvider
	@OneToMany(mappedBy="logReportTemplate1")
	private List<LogLogisticsProvider> logLogisticsProviders1;

	//bi-directional many-to-one association to LogLogisticsProvider
	@OneToMany(mappedBy="logReportTemplate2")
	private List<LogLogisticsProvider> logLogisticsProviders2;

	//bi-directional many-to-one association to LogProviderPickupOrderReport
	@OneToMany(mappedBy="logReportTemplate")
	private List<LogProviderPickupOrderReport> logProviderPickupOrderReports;

	//bi-directional many-to-one association to LogReportColumnMapping
	@OneToMany(mappedBy="logReportTemplate")
	private List<LogReportColumnMapping> logReportColumnMappings;

    public LogReportTemplate() {
    }

	public Long getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}

	public String getTemplateDesc() {
		return this.templateDesc;
	}

	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}

	public List<LogActivityReportUpload> getLogActivityReportUploads() {
		return this.logActivityReportUploads;
	}

	public void setLogActivityReportUploads(List<LogActivityReportUpload> logActivityReportUploads) {
		this.logActivityReportUploads = logActivityReportUploads;
	}
	
	public List<LogInvoiceReportUpload> getLogInvoiceReportUploads() {
		return this.logInvoiceReportUploads;
	}

	public void setLogInvoiceReportUploads(List<LogInvoiceReportUpload> logInvoiceReportUploads) {
		this.logInvoiceReportUploads = logInvoiceReportUploads;
	}
	
	public List<LogLogisticsProvider> getLogLogisticsProviders1() {
		return this.logLogisticsProviders1;
	}

	public void setLogLogisticsProviders1(List<LogLogisticsProvider> logLogisticsProviders1) {
		this.logLogisticsProviders1 = logLogisticsProviders1;
	}
	
	public List<LogLogisticsProvider> getLogLogisticsProviders2() {
		return this.logLogisticsProviders2;
	}

	public void setLogLogisticsProviders2(List<LogLogisticsProvider> logLogisticsProviders2) {
		this.logLogisticsProviders2 = logLogisticsProviders2;
	}
	
	public List<LogProviderPickupOrderReport> getLogProviderPickupOrderReports() {
		return this.logProviderPickupOrderReports;
	}

	public void setLogProviderPickupOrderReports(List<LogProviderPickupOrderReport> logProviderPickupOrderReports) {
		this.logProviderPickupOrderReports = logProviderPickupOrderReports;
	}
	
	public List<LogReportColumnMapping> getLogReportColumnMappings() {
		return this.logReportColumnMappings;
	}

	public void setLogReportColumnMappings(List<LogReportColumnMapping> logReportColumnMappings) {
		this.logReportColumnMappings = logReportColumnMappings;
	}

	public void setTemplateFile(String templateFile) {
		this.templateFile = templateFile;
	}

	public String getTemplateFile() {
		return templateFile;
	}
	
}