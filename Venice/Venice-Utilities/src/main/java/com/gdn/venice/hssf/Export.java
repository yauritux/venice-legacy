package com.gdn.venice.hssf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;

public class Export {
	private short fontColorIndexCounter = 0;
	private short bgColorIndexCounter = 50;

	HSSFWorkbook wb = null;

	HSSFSheet sheet = null;

	public Export() {
		wb = new HSSFWorkbook();
		sheet = wb.createSheet();
	}
	
	/**
	 * Added this method to separate file writing from cell write to workbook -DF
	 */
	public void writeWorkBookToFile(DataArgs data){
		try {

			File f = new File(data.getFileName());
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			FileOutputStream xls = new FileOutputStream(
					data.getFileName());

			try {

				wb.write(xls);

				xls.close();

				System.out.println("Succeed");
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
//	public void ToExcel(DataArgs data) {
//		if (data != null) {
//			List<DataColumn> columns = data.getColumns();
//			int idx = 0;
//
//			if (columns.size() > 0) {
//				try {
//
//					File f = new File(data.getFileName());
//					if (!f.exists()) {
//						try {
//							f.createNewFile();
//						} catch (IOException e) {
//							e.printStackTrace();
//						}
//					}
//
//					FileOutputStream xls = new FileOutputStream(
//							data.getFileName());
//
//					try {
//
//						HSSFRow rowColumn = sheet.createRow((short) idx++);
//
//						for (int i = 0; i < columns.size(); ++i) {
//							DataColumn c = (DataColumn) columns.get(i);
//							HSSFCell cell = rowColumn.createCell((short) i);
//
//							cell.setCellValue((String) c.getText());
//
//							parseCellStyle(wb, cell, c.getStyle(),
//									fontColorIndexCounter, bgColorIndexCounter);
//						}
//
//						for (int j = 0; j < data.getRows().size(); ++j) {
//							DataRow r = data.getRows().get(j);
//
//							fontColorIndexCounter++;
//							bgColorIndexCounter++;
//
//							if (r != null) {
//								if (r.getCells() != null) {
//									HSSFRow row = sheet
//											.createRow((short) idx++);
//
//									for (int k = 0; k < r.getCells().size(); ++k) {
//										GCell cl = (GCell) r.getCells().get(k);
//
//										HSSFCell cell = row
//												.createCell((short) k);
//
//										cell.setCellValue((String) cl
//												.getValue());
//										parseCellStyle(wb, cell, cl.getStyle(),
//												fontColorIndexCounter,
//												bgColorIndexCounter);
//									}
//								}
//							}
//						}
//
//						wb.write(xls);
//
//						xls.close();
//
//						System.out.println("Succeed");
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//
//				} catch (FileNotFoundException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//	}

	/**
	 * Added this method to write the cells to the sheet and allow some
	 * manipulation before writing to file -DF
	 */
	public void writeCellsToSheet(DataArgs data) {
		if (data != null) {
			List<DataColumn> superColumns = data.getSuperColumns();
			List<DataColumn> columns = data.getColumns();
			int idx = 0;
			
			if (superColumns.size() > 0) {
				HSSFCellStyle style = getCellStyle(wb, superColumns.get(0).getStyle(), fontColorIndexCounter, bgColorIndexCounter);
				HSSFRow rowSuperColumn = sheet.createRow((short) idx++);

				for (int i = 0; i < superColumns.size(); ++i) {
					DataColumn c = (DataColumn) superColumns.get(i);
					HSSFCell cell = rowSuperColumn.createCell((short) i);
					cell.setCellValue((String) c.getText());
					cell.setCellStyle(style);				
				}
			}
			
			if (columns.size() > 0) {
				HSSFCellStyle style = getCellStyle(wb, columns.get(0).getStyle(), fontColorIndexCounter, bgColorIndexCounter);
				HSSFRow rowColumn = sheet.createRow((short) idx++);
				for (int i = 0; i < columns.size(); ++i) {
					DataColumn c = (DataColumn) columns.get(i);
					HSSFCell cell = rowColumn.createCell((short) i);
					cell.setCellValue((String) c.getText());
					cell.setCellStyle(style);
				}

				String prevCity = "";
				if (data.getRows().size() > 0){
					style = getCellStyle(wb, data.getRows().get(0).getCells().get(0).getStyle(), fontColorIndexCounter,
							bgColorIndexCounter);
					style.setBorderBottom(CellStyle.BORDER_THIN);
				    style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
				    style.setBorderLeft(CellStyle.BORDER_THIN);
				    style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
				    style.setBorderRight(CellStyle.BORDER_THIN);
				    style.setRightBorderColor(IndexedColors.BLACK.getIndex());
				    style.setBorderTop(CellStyle.BORDER_THIN);
				    style.setTopBorderColor(IndexedColors.BLACK.getIndex());
				}
				
				for (int j = 0; j < data.getRows().size(); ++j) {
					DataRow dr = data.getRows().get(j);

					fontColorIndexCounter++;
					bgColorIndexCounter++;

					if (dr != null) {
						if (dr.getCells() != null) {
							HSSFRow row = sheet.createRow((short) idx++);

							if(!prevCity.equalsIgnoreCase(dr.getCells().get(7).getValue().toString()) && j!=0){
								row = sheet.createRow((short) idx++);
							}
							
							prevCity = dr.getCells().get(7).getValue().toString();
							
							for (int k = 0; k < dr.getCells().size(); ++k) {
								GCell cl = (GCell) dr.getCells().get(k);

								HSSFCell cell = row.createCell((short) k);

								cell.setCellValue((String) cl.getValue());
							    cell.setCellStyle(style);
							}
						}
					}
				}
			}
		}

	}

	private HSSFCellStyle getCellStyle(HSSFWorkbook wb, GCellStyle style, short replaceFontColorIndex,
			short replaceBgColorIndex) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		Font font = wb.createFont();

		font.setItalic(style.getItalic());
		font.setBoldweight(getBoldWeight(style.getBold()));
		font.setStrikeout(style.getStrikeOut());
		font.setFontName(style.getFontName());

		if (style.getFontColor() != null) {
			HSSFPalette palette = wb.getCustomPalette();
			palette.setColorAtIndex(replaceFontColorIndex,// HSSFColor.INDIGO.index,
					(byte) style.getFontColor().getRed(), // RGB red (0-255)
					(byte) style.getFontColor().getGreen(), // RGB green
					(byte) style.getFontColor().getBlue() // RGB blue
			);

			font.setColor(replaceFontColorIndex);// HSSFColor.INDIGO.index);
		}

		cellStyle.setFont(font);

		if (style.getHorizontalAlignment() != null) {
			cellStyle.setAlignment(getHorizontalAlignment(style
					.getHorizontalAlignment()));
		}

		if (style.getVerticalAlignment() != null) {
			cellStyle.setVerticalAlignment(getVerticalAlignment(style
					.getVerticalAlignment()));
		}

		if (style.getBackgroundColor() != null) {
			HSSFPalette palette = wb.getCustomPalette();
			palette.setColorAtIndex(replaceBgColorIndex, // HSSFColor.AQUA.index,
					(byte) style.getBackgroundColor().getRed(), // RGB red
																// (0-255)
					(byte) style.getBackgroundColor().getGreen(), // RGB green
					(byte) style.getBackgroundColor().getBlue() // RGB blue
			);

			cellStyle.setFillForegroundColor(replaceBgColorIndex);// HSSFColor.AQUA.index);
			cellStyle
					.setFillPattern(org.apache.poi.hssf.usermodel.HSSFCellStyle.SOLID_FOREGROUND);
		}

		/*
		 * if (style.getBackgroundColor() != null) {
		 * cellStyle.setFillForegroundColor
		 * (getGroundColor(style.getBackgroundColor()));
		 * cellStyle.setFillPattern
		 * (org.apache.poi.hssf.usermodel.HSSFCellStyle.SOLID_FOREGROUND); }
		 */

		return cellStyle;
	}

	private short getHorizontalAlignment(hAlign align) {
		short ha = 0;

		switch (align) {
		case ALIGN_CENTER:
			ha = XSSFCellStyle.ALIGN_CENTER;
			break;
		case ALIGN_CENTER_SELECTION:
			ha = XSSFCellStyle.ALIGN_CENTER_SELECTION;
			break;
		case ALIGN_FILL:
			ha = XSSFCellStyle.ALIGN_FILL;
			break;
		case ALIGN_GENERAL:
			ha = XSSFCellStyle.ALIGN_GENERAL;
			break;
		case ALIGN_JUSTIFY:
			ha = XSSFCellStyle.ALIGN_JUSTIFY;
			break;
		case ALIGN_LEFT:
			ha = XSSFCellStyle.ALIGN_LEFT;
			break;
		case ALIGN_RIGHT:
			ha = XSSFCellStyle.ALIGN_RIGHT;
			break;
		default:
			break;
		}

		return ha;
	}

	private short getVerticalAlignment(vAlign align) {
		short va = 0;

		switch (align) {
		case VERTICAL_BOTTOM:
			va = XSSFCellStyle.VERTICAL_BOTTOM;
			break;
		case VERTICAL_CENTER:
			va = XSSFCellStyle.VERTICAL_CENTER;
			break;
		case VERTICAL_JUSTIFY:
			va = XSSFCellStyle.VERTICAL_JUSTIFY;
			break;
		case VERTICAL_TOP:
			va = XSSFCellStyle.VERTICAL_TOP;
			break;
		default:
			break;
		}

		return va;
	}

	private short getBoldWeight(Boolean boldWeight) {
		short bw = HSSFFont.BOLDWEIGHT_NORMAL;

		if (boldWeight == true) {
			bw = HSSFFont.BOLDWEIGHT_BOLD;
		}

		return bw;
	}

	@SuppressWarnings("unused")
	private short getGroundColor(com.gdn.venice.hssf.Color color) {
		short bc = 0;

		switch (color) {
		case COLOR_BLACK:
			bc = IndexedColors.BLACK.getIndex();
			break;
		case COLOR_BLUE:
			bc = IndexedColors.BLUE.getIndex();
			break;
		case COLOR_GREEN:
			bc = IndexedColors.GREEN.getIndex();
			break;
		case COLOR_RED:
			bc = IndexedColors.RED.getIndex();
			break;
		case COLOR_WHITE:
			bc = IndexedColors.WHITE.getIndex();
			break;
		case COLOR_YELLOW:
			bc = IndexedColors.YELLOW.getIndex();
			break;
		default:
			break;
		}

		return bc;
	}

	/**
	 * @return the wb
	 */
	public HSSFWorkbook getWb() {
		return wb;
	}

	/**
	 * @param wb
	 *            the wb to set
	 */
	public void setWb(HSSFWorkbook wb) {
		this.wb = wb;
	}

	/**
	 * @return the sheet
	 */
	public HSSFSheet getSheet() {
		return sheet;
	}

	/**
	 * @param sheet
	 *            the sheet to set
	 */
	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}
}
