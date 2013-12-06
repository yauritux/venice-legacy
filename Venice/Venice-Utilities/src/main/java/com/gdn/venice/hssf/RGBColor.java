package com.gdn.venice.hssf;

public class RGBColor {
	private byte Red = 0;
	private byte Green = 0;
	private byte Blue = 0;
	
	public RGBColor() {
		
	}
	
	public RGBColor(byte red, byte green, byte blue) {
		this.Red = red;
		this.Green = green;
		this.Blue = blue;
	}
	
	public byte getRed() {
		return Red;
	}
	
	public void setRed(byte red) {
		Red = red;
	}
	public byte getGreen() {
		return Green;
	}
	public void setGreen(byte green) {
		Green = green;
	}
	public byte getBlue() {
		return Blue;
	}
	public void setBlue(byte blue) {
		Blue = blue;
	}
	
}
