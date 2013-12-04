package com.gdn.venice.hssf;

import java.util.ArrayList;
import java.util.List;

public class DataRow {
	private List<GCell> gCells = new  ArrayList<GCell>();
	
	public void addCell(GCell gCell) {
		gCells.add(gCell);
	}
	
	public void addCells(GCell[] cells) {
		if (cells != null) {
			for (int i = 0; i < cells.length; ++i) {
				this.gCells.add(cells[i]);
			}
		}
	}

	public List<GCell> getCells() {
		return gCells;
	}

	public void setCells(List<GCell> gCells) {
		this.gCells = gCells;
	}
}
