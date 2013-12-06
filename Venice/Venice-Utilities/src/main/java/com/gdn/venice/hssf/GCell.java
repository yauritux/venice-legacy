package com.gdn.venice.hssf;

public class GCell {
	private Object value;
	private GCellStyle style = new GCellStyle();
	
	public GCell () {
		
	}

	public GCell(Object value) {
		this.value = value;		
	}
	
	public GCell(Object value, GCellStyle style) {
		this.value = value;		
		this.style = style;
	}
	
	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public GCellStyle getStyle() {
		return style;
	}

	public void setStyle(GCellStyle style) {
		this.style = style;
	}
}
