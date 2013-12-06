package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the log_pickup_report_cell database table.
 * 
 */
@Entity
@Table(name="log_pickup_report_cell")
public class LogPickupReportCell implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private LogPickupReportCellPK id;

	@Column(name="cell_value", nullable=false, length=100)
	private String cellValue;

	//bi-directional many-to-one association to LogPickupReportColumn
    @ManyToOne
	@JoinColumn(name="pickup_report_column_id", nullable=false, insertable=false, updatable=false)
	private LogPickupReportColumn logPickupReportColumn;

	//bi-directional many-to-one association to LogProviderPickupOrderReport
    @ManyToOne
	@JoinColumn(name="provider_instructions_report_id", nullable=false, insertable=false, updatable=false)
	private LogProviderPickupOrderReport logProviderPickupOrderReport;

    public LogPickupReportCell() {
    }

	public LogPickupReportCellPK getId() {
		return this.id;
	}

	public void setId(LogPickupReportCellPK id) {
		this.id = id;
	}
	
	public String getCellValue() {
		return this.cellValue;
	}

	public void setCellValue(String cellValue) {
		this.cellValue = cellValue;
	}

	public LogPickupReportColumn getLogPickupReportColumn() {
		return this.logPickupReportColumn;
	}

	public void setLogPickupReportColumn(LogPickupReportColumn logPickupReportColumn) {
		this.logPickupReportColumn = logPickupReportColumn;
	}
	
	public LogProviderPickupOrderReport getLogProviderPickupOrderReport() {
		return this.logProviderPickupOrderReport;
	}

	public void setLogProviderPickupOrderReport(LogProviderPickupOrderReport logProviderPickupOrderReport) {
		this.logProviderPickupOrderReport = logProviderPickupOrderReport;
	}
	
}