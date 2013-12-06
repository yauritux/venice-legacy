package com.gdn.venice.hssf;

import java.util.ArrayList;
import java.util.List;

public class DataArgs {
	private String fileName;
	private List<DataColumn> superColumns = new  ArrayList<DataColumn>();
	private List<DataColumn> columns = new  ArrayList<DataColumn>();
	private List<DataRow> rows = new  ArrayList<DataRow>();
	
	public void addSuperColumn(DataColumn column) {
		superColumns.add(column);
	}
	
	public void addColumn(DataColumn column) {
		columns.add(column);
	}
	
	public void addRow(DataRow row) {
		rows.add(row);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<DataColumn> getSuperColumns() {
		return superColumns;
	}

	public void setSuperColumns(List<DataColumn> superColumns) {
		this.superColumns = superColumns;
	}
	
	public List<DataColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<DataColumn> columns) {
		this.columns = columns;
	}

	public List<DataRow> getRows() {
		return rows;
	}

	public void setRows(List<DataRow> rows) {
		this.rows = rows;
	}
}
 