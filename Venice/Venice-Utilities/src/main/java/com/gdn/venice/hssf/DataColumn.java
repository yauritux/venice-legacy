package com.gdn.venice.hssf;

public class DataColumn {
	private String text;
	private GCellStyle style = new GCellStyle();
	
	public DataColumn() {
		
	}
	
	public DataColumn(String text) {
		this.text = text;
	}
	
	public DataColumn(String text, GCellStyle style) {
		this.text = text;
		this.style = style;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public GCellStyle getStyle() {
		return style;
	}

	public void setStyle(GCellStyle style) {
		this.style = style;
	}
	
}
