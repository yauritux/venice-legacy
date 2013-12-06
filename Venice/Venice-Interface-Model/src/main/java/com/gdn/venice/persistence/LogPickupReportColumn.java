package com.gdn.venice.persistence;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the log_pickup_report_column database table.
 * 
 */
@Entity
@Table(name="log_pickup_report_column")
public class LogPickupReportColumn implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="log_pickup_report_column")  
	@TableGenerator(name="log_pickup_report_column", table="openjpaseq", pkColumnName="id", valueColumnName="sequence_value", allocationSize=1)  //flush every 1 insert
	@Column(name="pickup_report_column_id", unique=true, nullable=false)
	private Long pickupReportColumnId;

	@Column(name="column_excel_position", nullable=false, length=100)
	private String columnExcelPosition;

	@Column(name="column_header", nullable=false, length=100)
	private String columnHeader;

	//bi-directional many-to-one association to LogPickupReportCell
	@OneToMany(mappedBy="logPickupReportColumn")
	private List<LogPickupReportCell> logPickupReportCells;

    public LogPickupReportColumn() {
    }

	public Long getPickupReportColumnId() {
		return this.pickupReportColumnId;
	}

	public void setPickupReportColumnId(Long pickupReportColumnId) {
		this.pickupReportColumnId = pickupReportColumnId;
	}

	public String getColumnExcelPosition() {
		return this.columnExcelPosition;
	}

	public void setColumnExcelPosition(String columnExcelPosition) {
		this.columnExcelPosition = columnExcelPosition;
	}

	public String getColumnHeader() {
		return this.columnHeader;
	}

	public void setColumnHeader(String columnHeader) {
		this.columnHeader = columnHeader;
	}

	public List<LogPickupReportCell> getLogPickupReportCells() {
		return this.logPickupReportCells;
	}

	public void setLogPickupReportCells(List<LogPickupReportCell> logPickupReportCells) {
		this.logPickupReportCells = logPickupReportCells;
	}
	
}