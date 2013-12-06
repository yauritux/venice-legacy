package com.gdn.venice.hssf;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

public class Import {
	public DataArgs ToObject(String fileExcel) {
		DataArgs data = null;
		
		if (fileExcel != "") {
			data = new DataArgs();
			InputStream xls = null;
			
			try {
				xls = new FileInputStream(fileExcel);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			HSSFWorkbook wb = null;
			
			try {
				wb = new HSSFWorkbook(xls);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			HSSFSheet sheet = wb.getSheetAt(0); //first sheet
			Boolean isFirst = true;
						
			if (sheet != null) {				
				Iterator<Row> rows = sheet.rowIterator(); 
				while (rows.hasNext()) { 
					HSSFRow row = (HSSFRow) rows.next(); 		    
					
					if (data.getColumns().size() > 0) {
						isFirst = false;
					}
					
					DataRow dr = new DataRow();
										
					/*
					 * Corrected this code because had problems with blank cells - DF
					 * 	- added cell index and cell skew to determine if the cell
					 *    columns are contiguous
					 */
				
					int minColIndex = row.getFirstCellNum();
					int maxColIndex = row.getLastCellNum();
					for(int colIndex = minColIndex; colIndex < maxColIndex; colIndex++){
						HSSFCell cell = row.getCell(colIndex); 
						if(isFirst) {
							data.addColumn(new DataColumn(cell!=null?cell.toString():""));
						} else {
							if(cell != null){
								dr.addCell(new com.gdn.venice.hssf.GCell(cell.toString()));
							}else{
								dr.addCell(new com.gdn.venice.hssf.GCell(""));

							}
						}
					}
										
					if (dr.getCells().size() > 0) {
						data.addRow(dr);
					}
				}
			}
		}
		
		return data;
	}
}
