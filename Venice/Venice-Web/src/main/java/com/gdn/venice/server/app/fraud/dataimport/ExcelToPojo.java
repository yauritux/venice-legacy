package com.gdn.venice.server.app.fraud.dataimport;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;

@SuppressWarnings("rawtypes")
public class ExcelToPojo {
	private ArrayList<PojoInterface> pojoResult = new ArrayList<PojoInterface>();
	private String errorMessage = "";
	
	public ExcelToPojo(String mappingFileName, String excelFileName,
			int startRow, int startColumn, int endColumn, Class pojoTemplate) {
		
		//Read mapping from XML File
		MappingReader mappingReader = new MappingReader();
		mappingReader.readMappingOrder(mappingFileName);
		ArrayList<HashMap<String, Class>> mapping = mappingReader.getMappingResult();
		
		try {
			if (excelFileName != "") {
				InputStream xls = null;
				try {
					xls = new FileInputStream(excelFileName);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
					errorMessage = "Error while opening excel file";
					return;
				}

				HSSFWorkbook wb = null;
				HSSFSheet sheet = null;
				try {
					wb = new HSSFWorkbook(xls);
					sheet = wb.getSheetAt(0);
				} catch (IOException e) {
					e.printStackTrace();
					errorMessage = "Error while opening file, make sure file type is excel";
					return;
				}

				if (sheet != null) {
					Iterator<Row> rows = sheet.rowIterator();
					boolean importRow = false;
					int currentRow = -1;
					ArrayList<String> errorList = new ArrayList<String>();
					
					while (rows.hasNext()) {
						HSSFRow row = (HSSFRow) rows.next();
						currentRow = !importRow && row.equals(sheet.getRow(startRow - 1)) ? startRow : currentRow;
						importRow = importRow || row.equals(sheet.getRow(startRow - 1));
						
						if (importRow) {
							PojoInterface objPojo = (PojoInterface) pojoTemplate.newInstance();
							Method[] methods = pojoTemplate.getMethods();
							
							for(int columnIndex = startColumn - 1; columnIndex < endColumn; columnIndex++) {
								HSSFCell cell = row.getCell(columnIndex, Row.CREATE_NULL_AS_BLANK);
								
								String dataName = "";
								for (String key : mapping.get(columnIndex).keySet()) {
									dataName =  key;
								}
								
								for (Method method : methods) {
									if (method.getName().equalsIgnoreCase("set" + dataName)) {
										try {
											if (mapping.get(columnIndex).get(dataName).getName().toLowerCase().contains("bigdecimal")) {
												switch (cell.getCellType()) {
													case Cell.CELL_TYPE_STRING:
														method.invoke(objPojo, new BigDecimal(cell.getRichStringCellValue().getString().trim()));
									                    break;
									                case Cell.CELL_TYPE_NUMERIC:
									                    if (!DateUtil.isCellDateFormatted(cell)) {
									                    	method.invoke(objPojo, new BigDecimal(cell.getNumericCellValue()));
									                    }
									                    break;
									                case Cell.CELL_TYPE_FORMULA:
														method.invoke(objPojo, new BigDecimal(cell.getRichStringCellValue().getString().trim()));
									                    break;
									                default:
									                    break;
												}
											} else if (mapping.get(columnIndex).get(dataName).getName().toLowerCase().contains("date")) {
												switch (cell.getCellType()) {
													case Cell.CELL_TYPE_STRING:
														method.invoke(objPojo, new Date(new Long(cell.getRichStringCellValue().getString()).longValue()));
									                    break;
									                case Cell.CELL_TYPE_NUMERIC:
									                    if (DateUtil.isCellDateFormatted(cell)) {
									                    	method.invoke(objPojo, cell.getDateCellValue());
									                    } else {
									                    	method.invoke(objPojo, new Date((long) cell.getNumericCellValue()));
									                    }
									                    break;
									                case Cell.CELL_TYPE_FORMULA:
														method.invoke(objPojo, new Date(new Long(cell.getRichStringCellValue().getString()).longValue()));
									                    break;
									                default:
									                    break;
												}
											} else {
												switch (cell.getCellType()) {
													case Cell.CELL_TYPE_STRING:
														method.invoke(objPojo, cell.getRichStringCellValue().getString().trim());
									                    break;
									                case Cell.CELL_TYPE_NUMERIC:
									                    if (DateUtil.isCellDateFormatted(cell)) {
									                    	method.invoke(objPojo, cell.getDateCellValue().toString().trim());
									                    } else {
									                    	method.invoke(objPojo, new Long((long) cell.getNumericCellValue()).toString().trim());
									                    }
									                    break;
									                case Cell.CELL_TYPE_BOOLEAN:
														method.invoke(objPojo, new Boolean(cell.getBooleanCellValue()).toString().trim());
								                    	break;
									                case Cell.CELL_TYPE_FORMULA:
														method.invoke(objPojo, cell.getRichStringCellValue().getString().trim());
									                    break;
									                default:
									                    break;
												}
											}
										} catch (Exception importException) {
											importException.printStackTrace();
											errorList.add("   Row: " + currentRow + ", Column " + (columnIndex + 1) + " is expected using " + mapping.get(columnIndex).get(dataName).getSimpleName() + " type");
											importException.printStackTrace();
										}

										break;
									}
								}
							}
							
							this.pojoResult.add(objPojo);
						}
						
						currentRow++;
					}
					
					if (errorList.size() > 0) {
						errorMessage = "Error while importing data from excel\n" + StringUtils.join(errorList.toArray(), "\n");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage += "Error while importing data from excel";
		}
	}

	public ArrayList<PojoInterface> getPojoResult() {
		return pojoResult;
	}

	public void setPojoResult(ArrayList<PojoInterface> pojoResult) {
		this.pojoResult = pojoResult;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
