package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the log_provider_pickup_order_report database table.
 * 
 */
@Entity
@Table(name="log_provider_pickup_order_report")
public class LogProviderPickupOrderReport implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_provider_pickup_order_report")  
	@TableGenerator(name="log_provider_pickup_order_report", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="provider_instructions_report_id", unique=true, nullable=false)
	private Long providerInstructionsReportId;

	@Column(name="file_name_and_location", nullable=false, length=1000)
	private String fileNameAndLocation;

	@Column(name="report_desc", nullable=false, length=1000)
	private String reportDesc;

	@Column(name="report_timestamp", nullable=false)
	private Timestamp reportTimestamp;

	//bi-directional many-to-one association to LogPickupReportCell
	@OneToMany(mappedBy="logProviderPickupOrderReport")
	private List<LogPickupReportCell> logPickupReportCells;

	//bi-directional many-to-one association to LogLogisticsProvider
    @ManyToOne
	@JoinColumn(name="logistics_provider_id", nullable=false)
	private LogLogisticsProvider logLogisticsProvider;

	//bi-directional many-to-one association to LogReportTemplate
    @ManyToOne
	@JoinColumn(name="template_id", nullable=false)
	private LogReportTemplate logReportTemplate;

    public LogProviderPickupOrderReport() {
    }

	public Long getProviderInstructionsReportId() {
		return this.providerInstructionsReportId;
	}

	public void setProviderInstructionsReportId(Long providerInstructionsReportId) {
		this.providerInstructionsReportId = providerInstructionsReportId;
	}

	public String getFileNameAndLocation() {
		return this.fileNameAndLocation;
	}

	public void setFileNameAndLocation(String fileNameAndLocation) {
		this.fileNameAndLocation = fileNameAndLocation;
	}

	public String getReportDesc() {
		return this.reportDesc;
	}

	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}

	public Timestamp getReportTimestamp() {
		return this.reportTimestamp;
	}

	public void setReportTimestamp(Timestamp reportTimestamp) {
		this.reportTimestamp = reportTimestamp;
	}

	public List<LogPickupReportCell> getLogPickupReportCells() {
		return this.logPickupReportCells;
	}

	public void setLogPickupReportCells(List<LogPickupReportCell> logPickupReportCells) {
		this.logPickupReportCells = logPickupReportCells;
	}
	
	public LogLogisticsProvider getLogLogisticsProvider() {
		return this.logLogisticsProvider;
	}

	public void setLogLogisticsProvider(LogLogisticsProvider logLogisticsProvider) {
		this.logLogisticsProvider = logLogisticsProvider;
	}
	
	public LogReportTemplate getLogReportTemplate() {
		return this.logReportTemplate;
	}

	public void setLogReportTemplate(LogReportTemplate logReportTemplate) {
		this.logReportTemplate = logReportTemplate;
	}
	
}