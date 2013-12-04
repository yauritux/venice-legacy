package com.gdn.venice.server.data;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
/**
 * This class provides a data source response interface for general use
 * along with conversion utility methods to convert responses to and from
 * SmartClient XML responses.
 * 
 * <p>
 * <b>author:</b> <a href="mailto:david@pwsindonesia.com">David Forden</a>
 * <p>
 * <b>version:</b> 1.0
 * <p>
 * <b>since:</b> 2011 
 *
 */
public class RafDsResponse {
	public static int RAF_DS_RESPONSE_SUCCESS = 0;
	public static int RAF_DS_RESPONSE_FAIL = -1;
	
	//The status of the response
	private int status;
	
	/*
	 * The row number of the start of the response
	 * related to the overall number of query rows 
	 */
	private int startRow;

	/*
	 * The row number of the end of the response
	 * related to the overall number of query rows 
	 */
	private int endRow;
	
	/*
	 * The total number of rows in the response
	 */
	private int totalRows;
	
	/*
	 * The map representatiion of the data element from the XML response
	 */
	private List<HashMap<String, String>> data;
	
	/*
	 * The map representatiion of the errors from the XML response
	 */
	private List<HashMap<String, String>> errors;
	
	/**
	 * Gets the status of the response
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Sets the status of the response
	 * @param status
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * Gets the start row of the response
	 * @return the start row
	 */
	public int getStartRow() {
		return startRow;
	}
	
	/**
	 * Sets the start row of the response
	 * @param startRow
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
	
	/**
	 * Gets the end row of the response
	 * @return the end row
	 */
	public int getEndRow() {
		return endRow;
	}
	
	/**
	 * Sets the end row of the response
	 * @param endRow
	 */
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}
	
	/**
	 * Gets the total number of rows returned by the response
	 * @return the total rows
	 */
	public int getTotalRows() {
		return totalRows;
	}
	
	/**
	 * Sets the total number of rows returned by the response
	 * @param totalRows
	 */
	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}
	
	/**
	 * Gets the data element map of the response
	 * @return the data map
	 */
	public List<HashMap<String, String>> getData() {
		return data;
	}
	
	/**
	 * Sets the data element map of the response
	 * @param data
	 */
	public void setData(List<HashMap<String, String>> data) {
		this.data = data;
	}
	
	/**
	 * Gets the error map of the response
	 * @return the error map
	 */
	public List<HashMap<String, String>> getErrors() {
		return errors;
	}
	
	/**
	 * Sets the error map of the response
	 * @param errors
	 */
	public void setErrors(List<HashMap<String, String>> errors) {
		this.errors = errors;
	}
	
	/**
	 * Converts the response into the XML format supported by SmartClient
	 * ready for sending as a response via the servlet response
	 * 
	 * @param rafDsResponse is the response to convert
	 * @return the XML string formatted for SmartClient
	 * @throws Exception
	 */
	public static String convertRafDsResponsetoXml(RafDsResponse rafDsResponse) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		
		Element rootElement = doc.createElement("response");
		doc.appendChild(rootElement);
		
		Element statusElement = doc.createElement("status");
		statusElement.setTextContent(new Integer(rafDsResponse.getStatus()).toString());
		rootElement.appendChild(statusElement);
		
		Element startRowElement = doc.createElement("startRow");
		startRowElement.setTextContent(new Integer(rafDsResponse.getStartRow()).toString());
		rootElement.appendChild(startRowElement);
		
		Element endRowElement = doc.createElement("endRow");
		endRowElement.setTextContent(new Integer(rafDsResponse.getEndRow()).toString());
		rootElement.appendChild(endRowElement);
		
		Element totalRowsElement = doc.createElement("totalRows");
		totalRowsElement.setTextContent(new Integer(rafDsResponse.getTotalRows()).toString());
		rootElement.appendChild(totalRowsElement);
		
		Element dataElement = doc.createElement("data");
		
		Iterator<HashMap<String, String>> dataIterator = rafDsResponse.getData().iterator();
		
		/*
		 * Create each of the XMl elements for the body of the response
		 */
		while (dataIterator.hasNext()) {
			HashMap<String, String> row = dataIterator.next();
			
			Element rowElement = doc.createElement("record");
			
			Iterator<String> keys  = row.keySet().iterator();
			
			while (keys.hasNext()) {
				String columnName = keys.next();
				String columnValue = row.get(columnName);
				
				Element columnNode = doc.createElement(columnName);
				columnNode.setTextContent(columnValue);
				rowElement.appendChild(columnNode);
			}
			
			dataElement.appendChild(rowElement);
			
		}
		
		rootElement.appendChild(dataElement);

        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = transfac.newTransformer();
        
        /*
         * This omits the XML declaration from the document produced by the transformer
         * (the header of the XML document)
         */
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        
        /*
         * This formats the string output by the transformer to indented XML
         */
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
		
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        
        return sw.toString();
	}
}
