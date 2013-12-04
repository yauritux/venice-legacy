package com.gdn.venice.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemValueFormatter;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.StaticTextItem;
import com.smartgwt.client.widgets.grid.CellFormatter;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

/**
 * A jumbled up bunch of utilities
 * 
 * @author root
 *
 */
public class Util {
	
	public static String authorizationUtilServlet = "AuthorizationUtilServlet";
	
	public static String standardCurrencyFormat = "0,000.00";
	
	public static FormItem formatFormItemAsCurrency(FormItem item) {
		item.setValueFormatter(new FormItemValueFormatter() {
			
			@Override
			public String formatValue(Object value, Record record, DynamicForm form,
					FormItem item) {
				return formatStringAsCurrency((String) value);
			}
		});
		
		return item;
	}
	
	/**
	 * Formats a list grid field as currency in rupiah format only
	 * @param field the field to format
	 * @return the formatted field
	 */
	public static ListGridField formatListGridFieldAsCurrency(ListGridField field) {
		field.setAlign(Alignment.RIGHT);
		field.setCellFormatter(new CellFormatter() {
			public String format(Object value, ListGridRecord record,
					int rowNum, int colNum) {
				if (value instanceof Integer) {
					return formatStringAsCurrency(((Integer) value).toString(), standardCurrencyFormat);
				} if (value instanceof Float) {
					return formatStringAsCurrency(((Float) value).toString(), standardCurrencyFormat);
				}else {
					return formatStringAsCurrency((String) value, standardCurrencyFormat);
				}

			}
		});
		
		return field;
	}
	
	/**
	 * Formats a string representation of a number as currency in rupiah
	 * @param stringToFormat the string to format
	 * @return the formatted string
	 */
	public static String formatStringAsCurrency(String stringToFormat) {
		return formatStringAsCurrency(stringToFormat, standardCurrencyFormat);
	}
	
	/**
	 * Formats a string representation of a number as currency in rupiah with a specific number format
	 * @param string is the string to format
	 * @param format is the format string to use (from the NumberFormat library
	 * @return the formatted string
	 */
	public static String formatStringAsCurrency(String string, String format) {		
		/*
		 * Rewrote this routine because it was completely screwed up - DF
		 */		

		if (string == null || string.equals("")) {
			return "";
		}
	
		String stringToFormat = string;
		String[] splitString = null;

		String integralPart = "";
		
		if(string.contains(".")){
			splitString=string.split("\\.");
			integralPart = splitString[0];
		}else{
			integralPart = string;
		}
		
		String fractionalPart = "00000000000";
		
		/*
		 * If the length is > 1 then a fraction was supplied
		 */
		if(splitString != null && splitString.length > 1){
			fractionalPart = (splitString[1] + fractionalPart).substring(0, 2);
		}
		
		/*
		 * Apperently some of the numbers are passed in
		 * with preceding zeros so we need to chop them
		 */
		while(integralPart.startsWith("0")){
			integralPart = integralPart.substring(1);
		}
		
		stringToFormat = integralPart + "." + fractionalPart;
		
		NumberFormat nf = NumberFormat.getFormat(format);
		
		try {
			stringToFormat = nf.format(new Double(stringToFormat).doubleValue());
			
			while(stringToFormat.startsWith("0") || stringToFormat.startsWith(",")){
				stringToFormat = stringToFormat.substring(1);
			}

			if(stringToFormat.startsWith(".")){
				stringToFormat = "0" + stringToFormat;
			}
			
			return "Rp. " + stringToFormat;
		} catch (Exception e) {
			return stringToFormat;
		}
	}
	

	/**
	 * Formats a list grid field as a percentage
	 * @param field the field to fromat
	 * @return the formatted string
	 */
	public static ListGridField formatListGridFieldAsPercent(ListGridField field) {
		field.setAlign(Alignment.RIGHT);
		field.setCellFormatter(new CellFormatter() {
			public String format(Object value, ListGridRecord record,
					int rowNum, int colNum) {
				if (value instanceof Integer) {
					return formatStringAsPercent(((Integer) value).toString());
				} if (value instanceof Float) {
					return formatStringAsPercent(((Float) value).toString(), "000.00");
				}else {
					return formatStringAsPercent((String) value);
				}

			}
		});
		
		return field;
	}	
	
	/**
	 * Formats a string representation of a number as a percentage
	 * @param stringToFormat the string to format
	 * @return the formatted string
	 */
	public static String formatStringAsPercent(String stringToFormat) {
		return formatStringAsPercent(stringToFormat, "0,000");
	}	
	/**
	 * Formats a string representation of a number as a percentage with a specific NumberFormat string
	 * @param stringToFormat the string to format
	 * @return the formatted string
	 */
	public static String formatStringAsPercent(String stringToFormat, String format) {
		if (stringToFormat==null) {
			return "";
		}
		NumberFormat nf = NumberFormat.getFormat(format);
		try {
			if (stringToFormat.length()>3) {
				return nf.format(new Double(stringToFormat).doubleValue()) + " %";
			} else if (stringToFormat.length() != 0){
				return stringToFormat + " %";
			} else {
				return "";
			}
		} catch (Exception e) {
			return stringToFormat;
		}
	}
	
	/**
	 * Gets the list grid fields from a DataSource
	 * @param dataSource the data source to use
	 * @return the array of list grid fields
	 */
	public static ListGridField[] getListGridFieldsFromDataSource(DataSource dataSource) {
		DataSourceField[] dataSourceFields = dataSource.getFields();
		ListGridField[] listGridFields = new ListGridField[dataSourceFields.length];
		
		for (int i=0;i<dataSourceFields.length;i++) {
			listGridFields[i]= new ListGridField(dataSourceFields[i].getName());
		}
		
		return listGridFields;
	}
	
	/**
	 * Gets an array of read-only form items from a DataSource
	 * @param dataSource is the data source to use
	 * @return the array of form items
	 */
	public static FormItem[] getReadOnlyFormItemFromDataSource(DataSource dataSource) {
		DataSourceField[] dataSourceFields = dataSource.getFields();
		FormItem[] formItems = new FormItem[dataSourceFields.length];
		
		for (int i=0;i<dataSourceFields.length;i++) {
			formItems[i] = new StaticTextItem(dataSourceFields[i].getName());
		}
		
		return formItems;
	}
	
	/**
	 * Returns form data as XML representation from a HashMap
	 * @param map the HashMap to use
	 * @return the formatted XML
	 */
	public static String formXMLfromHashMap(HashMap<String, String> map) {
		String xml = "<data>\n";
		
		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			if (map.get(key)!=null) {
				xml += "<" + key + ">" + escapeXML(map.get(key)) + "</" + key + ">\n";
			}
		}
		
		xml += "</data>";
		
		return xml;
	}
	
	/**
	 * Localized utility for escaping XML strings
	 * @param xml the xml to escape
	 * @return the escaped XML
	 */
	private static String escapeXML(String xml) {
		xml = xml.replaceAll("<", "&lt;");
		xml = xml.replaceAll(">", "&gt;");
		xml = xml.replaceAll("\"", "&quot;");
		xml = xml.replaceAll("'", "&apos;");
		xml = xml.replaceAll("&", "&amp;");
		
		return xml;
	}
	
	/**
	 * Returns a HasMap of the form data from the XML representation of form data
	 * @param xml the XML to use
	 * @return the HashMap of form data
	 */
	public static LinkedHashMap<String, String> formHashMapfromXML(String xml) {
		LinkedHashMap<String, String> retVal = new LinkedHashMap<String, String>();
		
		try {
			Document doc = XMLParser.parse(xml);
			NodeList nodeList = doc.getElementsByTagName("data");
			if (nodeList != null && nodeList.getLength() > 0) {
				Node dataNode = nodeList.item(0);
				NodeList childNodes = dataNode.getChildNodes();
				
				for (int i=0;i<childNodes.getLength();i++) {
					if (childNodes.item(i) instanceof Element) {
						Element dataNodeChild = (Element) childNodes.item(i);
						retVal.put(dataNodeChild.getNodeName(), dataNodeChild.getChildNodes().item(0).getNodeValue());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
	
	/**
	 * Returns a LinkedHashMap of name value pairs from the form data
	 * @param mapSrc the source HashMap
	 * @return the new HashMap
	 */
	public static LinkedHashMap<String, String> formComboBoxMap(LinkedHashMap<String, String> mapSrc) {
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
		Iterator<String> iter = mapSrc.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			String value = mapSrc.get(key);
			key = key.substring("data".length(), key.length());
			map.put(key, value);
		}
		return map;
	}
	
	/**
	 * Returns a sorted HashMap of form data
	 * @param map the map to sort
	 * @param AscOrDesc a boolean signifying whether to sort asc or desc
	 * @return the sorted map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap SortedHashMap(Map map,String AscOrDesc){		

		HashMap<String,String> sorted = new LinkedHashMap<String,String>();
		Map<Integer,String> sortedMap = new TreeMap<Integer,String>(map);

		List yourMapKeys = new ArrayList(sortedMap.keySet());
		List yourMapValues = new ArrayList(sortedMap.values());
		Object[] sortedArray = yourMapKeys.toArray();	
		int size = sortedArray.length;
		if(AscOrDesc.equalsIgnoreCase("asc")){
			for (int i=0; i<size; i++) {
				sorted.put("data"+sortedArray[i], yourMapValues.get(yourMapKeys.indexOf(sortedArray[i])).toString());
			}		
		}else if(AscOrDesc.equalsIgnoreCase("desc")){
				for (int i=size-1; i>=0; i--) {
					sorted.put("data"+sortedArray[i], yourMapValues.get(yourMapKeys.indexOf(sortedArray[i])).toString());
				}		
		}
		return sorted;
	}
	
	/**
	 * Shows the wait cursor n the browser for longer running operations
	 */
	public static void showWaitCursor() {
	    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "wait");
	}
	
	/**
	 * Shows the default cursor in the browser
	 */
	public static void showDefaultCursor() {
	    DOM.setStyleAttribute(RootPanel.getBodyElement(), "cursor", "default");
	}


}
