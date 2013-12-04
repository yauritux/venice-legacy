package com.gdn.venice.hssf;

public class GCellStyle {
	private Boolean italic = false;
	private Boolean bold = false;
	private Boolean strikeOut = false;
	private String fontName = "";
	private hAlign HorizontalAlignment = null;
	private vAlign VerticalAlignment = null;
	private RGBColor BackgroundColor = null;
	private RGBColor FontColor = null;
	
	
	public Boolean getItalic() {
		return italic;
	}
	public void setItalic(Boolean italic) {
		this.italic = italic;
	}
	public Boolean getBold() {
		return bold;
	}
	public void setBold(Boolean bold) {
		this.bold = bold;
	}
	public Boolean getStrikeOut() {
		return strikeOut;
	}
	public void setStrikeOut(Boolean strikeOut) {
		this.strikeOut = strikeOut;
	}
	public String getFontName() {
		return fontName;
	}
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}
	public hAlign getHorizontalAlignment() {
		return HorizontalAlignment;
	}
	public void setHorizontalAlignment(hAlign horizontalAlignment) {
		HorizontalAlignment = horizontalAlignment;
	}
	public vAlign getVerticalAlignment() {
		return VerticalAlignment;
	}
	public void setVerticalAlignment(vAlign verticalAlignment) {
		VerticalAlignment = verticalAlignment;
	}
	public RGBColor getBackgroundColor() {
		return BackgroundColor;
	}
	public void setBackgroundColor(RGBColor backgroundColor) {
		BackgroundColor = backgroundColor;
	}
	public RGBColor getFontColor() {
		return FontColor;
	}
	public void setFontColor(RGBColor fontColor) {
		FontColor = fontColor;
	}
		
}
