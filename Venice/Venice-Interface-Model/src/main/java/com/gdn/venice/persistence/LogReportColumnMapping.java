package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the log_report_column_mapping database table.
 * 
 */
@Entity
@Table(name="log_report_column_mapping")
public class LogReportColumnMapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
@GeneratedValue(strategy=GenerationType.TABLE, generator="log_report_column_mapping")  
	@TableGenerator(name="log_report_column_mapping", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="column_mapping_id", unique=true, nullable=false)
	private Long columnMappingId;

	@Column(name="database_column", nullable=false, length=200)
	private String databaseColumn;

	@Column(name="spreadsheet_column", nullable=false, length=2)
	private String spreadsheetColumn;

	//bi-directional many-to-one association to LogReportTemplate
    @ManyToOne
	@JoinColumn(name="template_id", nullable=false)
	private LogReportTemplate logReportTemplate;

    public LogReportColumnMapping() {
    }

	public Long getColumnMappingId() {
		return this.columnMappingId;
	}

	public void setColumnMappingId(Long columnMappingId) {
		this.columnMappingId = columnMappingId;
	}

	public String getDatabaseColumn() {
		return this.databaseColumn;
	}

	public void setDatabaseColumn(String databaseColumn) {
		this.databaseColumn = databaseColumn;
	}

	public String getSpreadsheetColumn() {
		return this.spreadsheetColumn;
	}

	public void setSpreadsheetColumn(String spreadsheetColumn) {
		this.spreadsheetColumn = spreadsheetColumn;
	}

	public LogReportTemplate getLogReportTemplate() {
		return this.logReportTemplate;
	}

	public void setLogReportTemplate(LogReportTemplate logReportTemplate) {
		this.logReportTemplate = logReportTemplate;
	}
	
}